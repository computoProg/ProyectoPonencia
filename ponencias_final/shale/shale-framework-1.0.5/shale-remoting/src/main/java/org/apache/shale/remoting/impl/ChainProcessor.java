/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shale.remoting.impl;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Processor;

/**
 * <p>Implementation of {@link Processor} which maps a resource identifier
 * to the name of a <a href="http://commons.apache.org/chain">Commons
 * Chain</a> command or chain, in an appropriate catalog.  The command or chain
 * that is executed is passed an appropriate <code>Context</code> object, and
 * it will also have access to the current JavaServer Faces state by calling
 * <code>FacesContext.getCurrentInstance()</code>.</p>
 */
public class ChainProcessor implements Processor {


    // ------------------------------------------------------------ Constructors



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private transient Log log = null;


    // ------------------------------------------------------- Processor Methods


    /**
     * <p>Map the specified resource identifier to an appropriate Commons
     * Chain command or chain, in an appropriate catalog.  Construct an
     * appropriate <code>Context</code> object, and execute the specified
     * command or chain, to which we delegate responsibility for creating
     * the response for the current request.  Call
     * <code>FacesContext.responseComplete()</code> to tell JavaServer Faces
     * that the entire response has already been created.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier used to select the appropriate response
     *  (this will generally be a context relative path starting with "/")
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>viewId</code> is <code>null</code>
     */
    public void process(FacesContext context, String resourceId) throws IOException {

        if (log().isDebugEnabled()) {
            log().debug("Translated resource id '" + resourceId + "' to catalog '"
                        + mapCatalog(context, resourceId) + "' and command '"
                        + mapCommand(context, resourceId) + "'");
        }

        // Identify the Commons Chain catalog we will be using
        String catalogName = mapCatalog(context, resourceId);
        Catalog catalog = CatalogFactory.getInstance().getCatalog(catalogName);
        if (catalog == null) {
            if (log().isErrorEnabled()) {
                log().error("Cannot find catalog '" + catalogName + "' for resource '"
                            + resourceId + "'");
            }
            sendNotFound(context, resourceId);
            context.responseComplete();
            return;
        }

        // Identify the Commons Chain chain or command we will be executing
        String commandName = mapCommand(context, resourceId);
        Command command = catalog.getCommand(commandName);
        if (command == null) {
            if (log().isErrorEnabled()) {
                log().error("Cannot find command '" + commandName + "' in catalog '"
                            + catalogName + "' for resource '" + resourceId + "'");
            }
            sendNotFound(context, resourceId);
            context.responseComplete();
            return;
        }

        // Create a new context and pass it to the specified command
        try {
            command.execute(createContext(context, resourceId));
        } catch (Exception e) {
            if (log().isErrorEnabled()) {
                log().error("Exception executing command '" + commandName
                            + "' from catalog '" + catalogName + "' for resource '"
                            + resourceId + "'", e);
            }
            sendServerError(context, resourceId, e);
        }

        // Tell JavaServer Faces that the current response has been completed
        context.responseComplete();

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create and return an appropriate <code>Context</code> instance to be
     * passed to the command or chain that is executed.</p>
     *
     * <p>The default algorithm constructs and returns an instance of
     * {@link ChainContext} that wraps the specified <code>FacesContext</code>.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to be mapped
     */
    protected Context createContext(FacesContext context, String resourceId) {

        return new ChainContext(context);

    }


    /**
     * <p>Map the specified resource identifier to the name of a Commons Chain
     * <code>Catalog</code> from which the command or chain instance will be
     * acquired.</p>
     *
     * <p>The default implementation returns <code>remoting</code>
     * unconditionally.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to be mapped
     */
    protected String mapCatalog(FacesContext context, String resourceId) {

        return "remoting";

    }


    /**
     * <p>Map the specified resource identifier to the name of a Commons Chain
     * <code>Command</code> or <code>Chain</code>, which will be acquired from
     * a mapped <code>Catalog</code>.</p>
     *
     * <p>The default algorithm performs this conversion as follows:</p>
     * <ul>
     * <li>Strip any leading slash character.</li>
     * <li>Convert embedded slash characters to periods.</li>
     * </ul>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to be mapped
     */
    protected String mapCommand(FacesContext context, String resourceId) {

        // Strip any leading slash character
        if (resourceId.startsWith("/")) {
            resourceId = resourceId.substring(1);
        }

        // Convert nested slash characters into periods
        resourceId = resourceId.replace('/', '.');

        // Return the resulting string
        return resourceId;

    }


    /**
     * <p>Send a "not found" HTTP response, if possible.  Otherwise, throw an
     * <code>IllegalArgumentException</code> that will ripple out.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource that was not found
     *
     * @exception IllegalArgumentException if we cannot send an HTTP response
     * @exception IOException if an input/output error occurs
     */
    protected void sendNotFound(FacesContext context, String resourceId) throws IOException {

        if (servletRequest(context)) {
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, resourceId);
        } else {
            throw new IllegalArgumentException(resourceId);
        }

    }


    /**
     * <p>Send a "server error" HTTP response, if possible.  Otherwise, throw a
     * <code>FacesException</code> that will ripple out.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource that was not found
     * @param e Server exception to be reported
     *
     * @exception FacesException if we cannot send an HTTP response
     * @exception IOException if an input/output error occurs
     */
    protected void sendServerError(FacesContext context, String resourceId,
                                   Exception e) throws IOException {

        if (servletRequest(context)) {
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resourceId);
        } else {
            throw new FacesException(resourceId);
        }

    }


    /**
     * <p>Return <code>true</code> if we are processing a servlet request (as
     * opposed to a portlet request).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    protected boolean servletRequest(FacesContext context) {

        return context.getExternalContext().getContext() instanceof ServletContext;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(ChainProcessor.class);
        }
        return log;

    }


}
