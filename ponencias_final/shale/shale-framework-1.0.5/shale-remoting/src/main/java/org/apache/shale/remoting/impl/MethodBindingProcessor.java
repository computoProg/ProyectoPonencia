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
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Constants;
import org.apache.shale.remoting.impl.FilteringProcessor;
import org.apache.shale.remoting.Processor;

/**
 * <p>Implementation of {@link Processor} which maps a resource identifier
 * to a method binding expression, then delegates the creation of the current
 * response to the execution of that method.  The precise details of how a
 * resource identifier gets mapped are encapsulated in the <code>mapResourceId</code>
 * method, which may be specialized as desired in a subclass.</p>
 */
public class MethodBindingProcessor extends FilteringProcessor {


    // ------------------------------------------------------------ Constructors



    // ------------------------------------------------------ Instance Variables


    /**
     * <p><code>Log</code> instance for this class.</p>
     */
    private transient Log log = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Force our default excludes list to be included.</p>
     *
     * @param excludes Application specified excludes list
     */
    public void setExcludes(String excludes) {

        if ((excludes != null) && (excludes.length() > 0)) {
            super.setExcludes(Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT
                              + "," + excludes);
        } else {
            super.setExcludes(Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT);
        }

    }


    // ------------------------------------------------------- Processor Methods


    /**
     * <p>Convert the specified resource identifier into a method binding
     * expression, and delegate creation of the response to a call to the
     * identified method.  Call <code>FacesContext.responseComplete()</code>
     * to tell JavaServer Faces that the entire response has already been
     * created.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier used to select the appropriate response
     *  (this will generally be a context relative path starting with "/")
     *
     * @exception IllegalArgumentException if the view identifier is not
     *  well formed (starting with a '/' character)
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>viewId</code> is <code>null</code>
     */
    public void process(FacesContext context, String resourceId) throws IOException {

        // If someone else has completed the response, we do not have
        // anything to do
        if (context.getResponseComplete()) {
            return;
        }

        // Filter based on our includes and excludes patterns
        if (!accept(resourceId)) {
            if (log().isTraceEnabled()) {
                log().trace("Resource id '" + resourceId
                            + "' rejected by include/exclude rules");
            }
            // Send an HTTP "not found" response to avoid giving the client
            // any information about a resource that exists and was refused,
            // versus a resource that does not exist
            sendNotFound(context, resourceId);
            context.responseComplete();
            return;
        }

        // Create and execute a method binding based on this resource identifier
        MethodBinding mb = mapResourceId(context, resourceId);
        if (log().isDebugEnabled()) {
            log().debug("Translated resource id '" + resourceId
                        + "' to method binding expression '"
                        + mb.getExpressionString() + "'");
        }
        mb.invoke(context, new Object[] { });

        // Tell JavaServer Faces that the current response has been completed
        context.responseComplete();

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Map the specified resource identifier into a corresponding
     * <code>MethodBinding</code> which identifies the method which will be
     * called to produce this response.</p>
     *
     * <p>The default algorithm performs this conversion as follows:</p>
     * <ul>
     * <li>Strip any leading slash character.</li>
     * <li>Convert embedded slash characters to periods.</li>
     * <li>Surround the result with "#{" and "}" delimiters.</li>
     * <li>Ask JavaServer Faces to create a method binding, using this
     *     expression, for a method that takes no parameters.</li>
     * </ul>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to be mapped
     */
    protected MethodBinding mapResourceId(FacesContext context, String resourceId) {

        // Strip any leading slash character
        if (resourceId.startsWith("/")) {
            resourceId = resourceId.substring(1);
        }

        // Convert nested slash characters into periods
        // FIXME - this will have problematic results on managed bean names that have periods
        resourceId = resourceId.replace('/', '.');

        // Surround the result with "#{" and "}" delimiters
        resourceId = "#{" + resourceId + "}";

        // Return a MethodBinding expression based on this mapping
        return context.getApplication().createMethodBinding(resourceId,
                new Class[] { });

    }



    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(MethodBindingProcessor.class);
        }
        return log;

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
     *
     * @since 1.0.4
     */
    private void sendNotFound(FacesContext context, String resourceId) throws IOException {

        if (servletRequest(context)) {
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, resourceId);
        } else {
            throw new IllegalArgumentException(resourceId);
        }

    }


    /**
     * <p>Return <code>true</code> if we are processing a servlet request (as
     * opposed to a portlet request).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     *
     * @since 1.0.4
     */
    private boolean servletRequest(FacesContext context) {

        return context.getExternalContext().getContext() instanceof ServletContext;

    }


}
