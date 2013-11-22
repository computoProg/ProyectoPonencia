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

package org.apache.shale.remoting;

import java.beans.Beans;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.apache.shale.remoting.faces.MappingsHelper;

/**
 * <p>Helper bean for rendering links to download resources commonly used
 * in HTML and XHTML pages.  The specified resource identifier is automatically
 * mapped based upon the Shale Remoting configuration that this application
 * is using, as well as adapting to the servlet mapping for the JavaServer
 * Faces controller servlet.  A given resource identifier will only be linked
 * once for a given request.</p>
 *
 * <p>Instances of this class are stateless and have no side effects.</p>
 */
public class XhtmlHelper {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The prefix to the request attributes that we will use to keep track
     * of whether a particular resource has been linked already.</p>
     */
    protected static final String PREFIX = "org.apache.shale.remoting.LINKED";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Helper to retrieve the {@link Mappings} instance for this application.</p>
     */
    private MappingsHelper helper = new MappingsHelper();


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Render a link to a JavaScript resource at the specified resource
     * identifier.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> being rendered
     * @param writer <code>ResponseWriter</code> to render output to
     * @param mechanism Mechanism used to retrieve the specified resource
     *  (used to select the appropriate {@link Processor}
     * @param resourceId Resource identifier used to retrieve the requested
     *  JavaScript resource
     *
     * @exception IllegalArgumentException if <code>mechanism</code> or
     *  <code>resourceId</code> is <code>null</code>
     * @exception IllegalStateException if a configuration error prevents
     *  the mapping of this resource identifier to a corresponding URI
     * @exception IOException if an input/output error occurs
     */
    public void linkJavascript(FacesContext context, UIComponent component,
                               ResponseWriter writer,
                               Mechanism mechanism, String resourceId)
        throws IOException {

        linkJavascript(context, component, writer,
                       mechanism, resourceId, "text/javascript");

    }


    /**
     * <p>Render a link to a JavaScript resource at the specified resource
     * identifier.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> being rendered
     * @param writer <code>ResponseWriter</code> to render output to
     * @param mechanism Mechanism used to retrieve the specified resource
     *  (used to select the appropriate {@link Processor}
     * @param resourceId Resource identifier used to retrieve the requested
     *  JavaScript resource
     * @param contentType Content type to specify (for pulling specific
     *  versions of JavaScript resources)
     *
     * @exception IllegalArgumentException if <code>mechanism</code> or
     *  <code>resourceId</code> is <code>null</code>
     * @exception IllegalStateException if a configuration error prevents
     *  the mapping of this resource identifier to a corresponding URI
     * @exception IOException if an input/output error occurs
     */
    public void linkJavascript(FacesContext context, UIComponent component,
                               ResponseWriter writer,
                               Mechanism mechanism, String resourceId,
                               String contentType) throws IOException {

        if (linked(context, resourceId)) {
            return;
        }

        writer.startElement("script", component);
        writer.writeAttribute("type", contentType, null);
        writer.writeURIAttribute("src", mapResourceId(context, mechanism, resourceId), null);
        writer.endElement("script");
        writer.write("\n");

        link(context, resourceId);

    }


    /**
     * <p>Render a link to a CSS stylesheet at the specified resource
     * identifier.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> being rendered
     * @param writer <code>ResponseWriter</code> to render output to
     * @param mechanism Mechanism used to retrieve the specified resource
     *  (used to select the appropriate {@link Processor}
     * @param resourceId Resource identifier used to retrieve the requested
     *  stylesheet resource
     *
     * @exception IllegalArgumentException if <code>mechanism</code> or
     *  <code>resourceId</code> is <code>null</code>
     * @exception IllegalStateException if a configuration error prevents
     *  the mapping of this resource identifier to a corresponding URI
     * @exception IOException if an input/output error occurs
     */
    public void linkStylesheet(FacesContext context, UIComponent component,
                               ResponseWriter writer,
                               Mechanism mechanism, String resourceId)
        throws IOException {

        if (linked(context, resourceId)) {
            return;
        }

        writer.startElement("link", component);
        writer.writeAttribute("type", "text/css", null);
        writer.writeAttribute("rel", "stylesheet", null);
        writer.writeURIAttribute("href", mapResourceId(context, mechanism, resourceId), null);
        writer.endElement("link");
        writer.write("\n");

        link(context, resourceId);

    }



    /**
     * <p>Map the specified resource identifier to a request URL, taking into
     * account the mappings for the specified mechanism and the servlet mapping
     * for the JavaServer Faces controller servlet.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mechanism Requested mechanism
     * @param resourceId Resource identifier to be mapped
     *
     * @exception IllegalArgumentException if <code>mechanism</code> or
     *  <code>resourceId</code> is <code>null</code>
     * @exception IllegalStateException if a configuration error prevents
     *  the mapping of this resource identifier to a corresponding URI
     */
    public String mapResourceId(FacesContext context, Mechanism mechanism,
                                String resourceId) {

        // Validate our incoming parameters
        if (resourceId == null) {
            throw new IllegalArgumentException
                    (resourceBundle(context).getString("xhtml.noResourceId"));
        }
        if (mechanism == null) {
            throw new IllegalArgumentException
                    (resourceBundle(context).getString("xhtml.noMechanism"));
        }

        // If we are running inside a design time tool, the runtime
        // initialization might not have been performed.  Therefore,
        // just return the incoming resource identifier unchanged, sinc
        // it is not going to be executed anyway.
        if (Beans.isDesignTime()) {
            return resourceId;
        }

        // Acquire a reference to the Mappings instance for this application
        Mappings mappings = helper.getMappings(context);
        if (mappings == null) {
            throw new IllegalStateException
                    (resourceBundle(context).getString("xhtml.noMappings"));
        }

        // Acquire the Mapping instance to be used for the requesed mechanism
        List list = mappings.getMappings();
        if (list == null) {
            list = new ArrayList();
        }
        Iterator instances = list.iterator();
        Mapping mapping = null;
        while (instances.hasNext()) {
            Mapping instance = (Mapping) instances.next();
            if (mechanism == instance.getMechanism()) {
                mapping = instance;
                break;
            }
        }
        if (mapping == null) {
            throw new IllegalArgumentException(mechanism.toString());
        }

        // Ask this Mapping to map the resource identifier appropriately
        return mapping.mapResourceId(context, resourceId);

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Mark the specified resource identifier as having already been
     * linked in the current request.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to mark as having been linked
     */
    protected void link(FacesContext context, String resourceId) {

        context.getExternalContext().getRequestMap().
                put(PREFIX + resourceId, Boolean.TRUE);

    }


    /**
     * <p>Return <code>true</code> if the specified resource identifier has
     * already been linked in the current request, and should therefore not
     * be linked again.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to check for prior linking
     */
    protected boolean linked(FacesContext context, String resourceId) {

        return context.getExternalContext().getRequestMap().
                containsKey(PREFIX + resourceId);

    }


    /**
     * <p>Return the localized resource bundle we should use to generate
     * exception or log messages for this request.</p>
     *
     * @param context <code>FacesContext</code> for this request
     */
    protected ResourceBundle resourceBundle(FacesContext context) {

        return ResourceBundle.getBundle("org.apache.shale.remoting.Bundle",
                                        context.getViewRoot().getLocale(),
                                        Thread.currentThread().getContextClassLoader());

    }


}
