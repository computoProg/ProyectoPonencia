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

package org.apache.shale.remoting.faces;

import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Factory class for returning <code>ResponseStream</code> or
 * <code>ResponseWriter</code> instances that may be used to produce output
 * response content that is independent of whether we are running in a servlet
 * or portlet environment.  The <code>RenderKit</code> for the current request
 * will be used to manufacture stream or writer instances, if necessary.</p>
 */
public class ResponseFactory {


    // ------------------------------------------------------------ Constructors



    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the configured <code>ResponseStream</code> for the current
     * request, creating and installing a new one if necessary.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType Content type to be set on the response,
     *  or <code>null</code> to let this be defaulted
     */
    public ResponseStream getResponseStream(FacesContext context, String contentType) {

        ResponseStream stream = context.getResponseStream();
        if (stream == null) {
            stream = createResponseStream(context, contentType);
            context.setResponseStream(stream);
        }
        return stream;

    }


    /**
     * <p>Return the configured <code>ResponseWriter</code> for the current
     * request, creating and installing a new one if necessary.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType Content type to be set on the response,
     *  or <code>null</code> to let this be defaulted
     */
    public ResponseWriter getResponseWriter(FacesContext context, String contentType) {

        ResponseWriter writer = context.getResponseWriter();
        if (writer == null) {
            writer = createResponseWriter(context, contentType);
            context.setResponseWriter(writer);
        }
        return writer;

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Create a new <code>ResponseStream</code> that writes to the servlet
     * or portlet response stream for the current request.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType Content type to be set on the response,
     *  or <code>null</code> to let this be defaulted
     *
     * @exception IllegalStateException if a writer for the current response
     *  has already been acquired
     */
    protected ResponseStream createResponseStream(FacesContext context, String contentType) {

        Object response = context.getExternalContext().getResponse();

        // Set the content type (if specified)
        if (contentType != null) {
            try {
                Method method =
                  response.getClass().getMethod("setContentType",
                                                new Class[] { String.class });
                method.invoke(response, new Object[] { contentType });
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e);
            } catch (NoSuchMethodException e) {
                throw new FacesException(e);
            }
        }

        // Acquire the output stream we will be wrapping
        final OutputStream stream;
        try {
            String methodName =
              (response instanceof HttpServletResponse) ? "getOutputStream" : "getPortletOutputStream";
            Method method =
              response.getClass().getMethod(methodName, new Class[] { });
            stream = (OutputStream) method.invoke(response, new Object[] { });
        } catch (IllegalAccessException e) {
            throw new FacesException(e);
        } catch (InvocationTargetException e) {
            throw new FacesException(e);
        } catch (NoSuchMethodException e) {
            throw new FacesException(e);
        }

        // Construct a ResponseStream that wraps this stream
        return renderKit(context).createResponseStream(stream);

    }


    /**
     * <p>Create a new <code>ResponseWriter</code> that writes to the servlet
     * or portlet response writer for the current request.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType Content type to be set on the response,
     *  or <code>null</code> to let this be defaulted
     *
     * @exception IllegalStateException if a writer for the current response
     *  has already been acquired
     */
    protected ResponseWriter createResponseWriter(FacesContext context, String contentType) {

        Object response = context.getExternalContext().getResponse();

        // Set the content type (if specified)
        if (contentType != null) {
            try {
                Method method =
                  response.getClass().getMethod("setContentType",
                                                new Class[] { String.class });
                method.invoke(response, new Object[] { contentType });
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e);
            } catch (NoSuchMethodException e) {
                throw new FacesException(e);
            }
        }

        // Acquire the writer we will be wrapping
        final Writer writer;
        try {
            String methodName = "getWriter";
            Method method =
              response.getClass().getMethod(methodName, new Class[] { });
            writer = (Writer) method.invoke(response, new Object[] { });
        } catch (IllegalAccessException e) {
            throw new FacesException(e);
        } catch (InvocationTargetException e) {
            throw new FacesException(e);
        } catch (NoSuchMethodException e) {
            throw new FacesException(e);
        }

        // From JSF 1.2 on, we can call ExternalContext.getResponseCharacterEncoding(),
        // but for JSF 1.1 we must use response.getCharacterEncoding() instead
        String encoding = null;
        try {
            String methodName = "getCharacterEncoding";
            Method method =
                response.getClass().getMethod(methodName, new Class[] { });
            encoding = (String) method.invoke(response, new Object[] { });
        } catch (IllegalAccessException e) {
            throw new FacesException(e);
        } catch (InvocationTargetException e) {
            throw new FacesException(e);
        } catch (NoSuchMethodException e) {
            throw new FacesException(e);
        }

        // Construct a ResponseWriter that wraps this stream
        if ((contentType != null) && contentType.startsWith("text/html")) {
            return renderKit(context).createResponseWriter(writer, contentType, encoding);
        } else {
            return new BasicResponseWriter(writer, contentType, encoding);
        }


    }


    /**
     * <p>Return the relevant <code>RenderKit</code> to construct response
     * stream or writer instances for this request.  If there is no render kit
     * identified yet, the default <code>RenderKit</code> will be returned.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    protected RenderKit renderKit(FacesContext context) {

        // Identify the RenderKit we will use to create instances
        String renderKitId = null;
        UIViewRoot root = context.getViewRoot();
        if (root != null) {
            renderKitId = root.getRenderKitId();
        }
        if (renderKitId == null) {
            renderKitId = RenderKitFactory.HTML_BASIC_RENDER_KIT;
        }

        // Return an instance of the requested RenderKit
        RenderKitFactory factory = (RenderKitFactory)
          FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        return factory.getRenderKit(context, renderKitId);

    }


}
