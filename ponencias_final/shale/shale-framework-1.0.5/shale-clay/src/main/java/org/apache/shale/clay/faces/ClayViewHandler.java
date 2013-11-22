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

/*
 * $Id: ClayViewHandler.java 560470 2007-07-28 02:31:40Z gvanmatre $
 */
package org.apache.shale.clay.faces;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.PageNotFoundException;
import org.apache.shale.clay.utils.JSFRuntimeTracker;

/**
 * <p>This <code>ViewHandler</code> will handle full HTML template views using the
 * {@link Clay} component as the single subtree under the view root.  Views will
 * be intercepted having a suffix matching the registered clay template suffix
 * in the web deployment descriptor.  The default suffixes are ".html" and "*.xml".
 * All other view render requests that don't match the suffixes will be delegated to the
 * original decorated view handler.</p>
 */
public class ClayViewHandler extends ViewHandler {

    /**
     * <p>
     * Commons logger utility class instance.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(ClayViewHandler.class);
    }

    /**
     * <p>The decorated view handler.</p>
     */
    private ViewHandler original = null;

    /**
     * <p>The {@link Clay} component <code>id</code> property value.</p>
     */
    private static final String CLAY_VIEW_ID = "clayView";

    /**
     * <p>{@link Clay} <code>componentType</code> that is used to create a instance using
     * the faces Application object.</p>
     */
    private static final String CLAY_COMPONENT_TYPE = "org.apache.shale.clay.component.Clay";

    /**
     * <p>Holds the suffixes used to identify a Clay HTML or XML full view template.</p>
     */
    private String[] suffixes = null;

    /**
     * <p>This is an overloaded constructor passing the <code>original</code>
     * view handler.</p>
     *
     * @param original view handler
     */
    public ClayViewHandler(ViewHandler original) {
        this.original = original;

        if (log.isInfoEnabled()) {
           log.info("Loading Clay View Handler");
        }
    }

    /**
     * @param context faces context
     * @return locale calculated from the original handler
     */
    public Locale calculateLocale(FacesContext context) {
        return original.calculateLocale(context);
    }

    /**
     * <p>Application scope attribute under which the
     * <code>ViewControllerMapper</code> for translating view identifiers
     * to class names of the corresponding <code>ViewController</code>
     * is stored.</p>
     */
    public static final String VIEW_MAPPER =
      "org$apache$shale$view$VIEW_MAPPER";


    /**
     * @param context faces context
     * @return render kit id calculated from the original handler
     */
    public String calculateRenderKitId(FacesContext context) {
        return original.calculateRenderKitId(context);
    }

    /**
     * <p>This method is overridden to check to see if the target view
     * is a clay html or xml template.  If it is, the view id is normalized
     * before the original implementation is invoked.
     * </p>
     *
     * @param context faces context
     * @param viewId name of the page
     * @return root of the component tree for the view id
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        String id = viewId;
        int index = indexOfClayTemplateSuffix(context, id);

        if (index != -1) {
            id = normalizeViewId(id, index);
        }

        return original.createView(context, id);
    }

    /**
     * <p>Changes the suffix of the <code>viewId</code> to that of the
     * suffix of a clay template defined by the <code>index</code> into
     * the <code>suffixes</code> array.
     *
     * @param viewId name of the page
     * @param index into the <code>suffixes</code> array
     * @return viewId with a suffix matching the suffixes[index]
     */
    private String normalizeViewId(String viewId, int index) {

        StringBuffer buff = new StringBuffer(viewId);
        int i = buff.lastIndexOf(".");
        if (i > -1) {
            buff.setLength(i);
            buff.append(suffixes[index]);
        }

        return buff.toString();

    }

    /**
     * <p>
     * If the <code>viewId</code> is suffixed with the Clay template suffix,
     * rewrite the returned actionUrl with a clay suffix. The super
     * implementation will assume ".jsp" or whatever the
     * <code>javax.faces.DEFAULT_SUFFIX</code> is set to in the web deployment
     * descriptor.
     * </p>
     *
     * @param context faces context
     * @param viewId name of the page
     * @return action attribute of the UIForm component
     */
    public String getActionURL(FacesContext context, String viewId) {
        String actionURL = original.getActionURL(context, viewId);
        int index = indexOfClayTemplateSuffix(context, viewId);
        if (index != -1) {
           actionURL = normalizeViewId(actionURL, index);
        } else if (context.getExternalContext().getRequestMap().get(Globals.CLAY_FULL_VIEW_RESTORE_IND) != null) {
           //This stuff is for myfaces.  The action url is calculated from the
           //servlet mappings. When switching from a .html or .xml clay view to a .jsp, myfaces changes the
           //target viewId suffix to match the previous one.  This seems to handle the problem. I'm not
           // sure how common it will be to have a mix of .jsp, .html and .xml within the same application.
           int i = actionURL.lastIndexOf("/");
           if (i > -1) {
              StringBuffer tmp = new StringBuffer(actionURL);
              tmp.setLength(i);
              tmp.append(viewId);
              actionURL = tmp.toString();
           }

        }

        return actionURL;
    }

    /**
     * @param context faces context
     * @param path context root relative path
     * @return full path to the resource
     */
    public String getResourceURL(FacesContext context, String path) {
        return original.getResourceURL(context, path);
    }

    /**
     * <p>This method looks to see if the target view identified by the viewId is a
     * {@link org.apache.shale.clay.component.Clay} HTML or XML full view.
     * This is determined by a value cached in request scope by the
     * {@link ClayViewHandlerCommand} or the suffix of the viewId.  If a match
     * is found, the index position into the suffixes array is returned;
     * Otherwise a -1 is returned.</p>
     *
     * @param context faces context
     * @param viewId name of the page
     * @return index into the suffixes array or -1 if not found
     */
    protected int indexOfClayTemplateSuffix(FacesContext context, String viewId) {

        if (suffixes == null) {
            suffixes = new String[2];

            suffixes[0] = context.getExternalContext().getInitParameter(
                    Globals.CLAY_HTML_TEMPLATE_SUFFIX);
            if (suffixes[0] == null) {
                suffixes[0] = Globals.CLAY_DEFAULT_HTML_TEMPLATE_SUFFIX;
            }

            suffixes[1] = context.getExternalContext().getInitParameter(
                    Globals.CLAY_XML_TEMPLATE_SUFFIX);
            if (suffixes[1] == null) {
                suffixes[1] = Globals.CLAY_DEFAULT_XML_TEMPLATE_SUFFIX;
            }

        }

        //looked for the original first set by the ClayViewHandlerCommand
        String originalSuffix = (String) context.getExternalContext()
                 .getRequestMap().get(Globals.CLAY_FULL_VIEW_SUFFIX);
        if (originalSuffix != null) {
            for (int i = 0; i < suffixes.length; i++) {
                if (originalSuffix.equals(suffixes[i])) {
                    return i;
                }
            }
        }

        //look at the path
        for (int i = 0; i < suffixes.length; i++) {
            if (viewId.endsWith(suffixes[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * <p>The default view handler implementation will try to
     * make the viewId end with ".jsp".  If the viewId ends
     * in the clay template suffix, use the state manager
     * to restore the view.</p>
     *
     * @param context faces context
     * @param viewId name of the page
     * @return root of the page
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot view = null;

        int index = indexOfClayTemplateSuffix(context, viewId);
        if (index != -1) {

            if (log.isDebugEnabled()) {
                log.debug("Clay template restoreView for " + viewId);
            }

            StateManager stateManager = context.getApplication().getStateManager();
            view = stateManager.restoreView(context, normalizeViewId(viewId, index), calculateRenderKitId(context));
            //if a clay template was restored, clear the request param on the forward for
            //clean navigation to non clay templates
            if (view != null) {
                context.getExternalContext().getRequestMap().put(Globals.CLAY_FULL_VIEW_SUFFIX, null);
                context.getExternalContext().getRequestMap().put(Globals.CLAY_FULL_VIEW_RESTORE_IND, Boolean.TRUE);
            }

        } else {
            view = original.restoreView(context, viewId);
        }

        return view;
    }

    /**
     * <p>Invokes the original view handler's writeState.</p>
     *
     * @param context faces context
     * @exception IOException serializing tree
     */
    public void writeState(FacesContext context) throws IOException {
        original.writeState(context);
    }

    /**
     * <p>HTML form markers for client side state saving for MyFaces and Sun RI
     * implementations.</p>
     */
    protected static final String[] FORM_MARKERS = {
        "com.sun.faces.saveStateFieldMarker",    // RI 1.1
        "<!--@@JSF_FORM_STATE_MARKER@@-->",      // myfaces 1.1.x
        "~com.sun.faces.saveStateFieldMarker~"}; // RI 1.2

    /**
     * <p>Returns an index into the <code>FORM_MAKKERS</code> array.  The index will be used to
     * get the form marker matching the JSF runtime. Only the myfaces and Sun RI are supported.
     * The form marker is determined by trying to load the myfaces view handler.  Next,
     * the Sun RI 1.2 JSPVersionTracker is attempted to be loaded.  The default
     * is the marker for the Sun RI.</p>
     *
     * @return index into the FORM_MARKERS array
     */
    protected int indexOfFormMarker() {
        int i = 0;
        if (JSFRuntimeTracker.getJsfRuntime() == JSFRuntimeTracker.MYFACES_1_1) {
          i = 1;
        } else if (JSFRuntimeTracker.getJsfRuntime() == JSFRuntimeTracker.RI_1_2) {
          i = 2;
        }
        return i;
    }

    /**
     * <p>The <code>viewId</code> is check to see if it ends with the
     * same suffix as the full HTML or XML views.  This match might be performed
     * by the {@link ClayViewHandlerCommand} when using the myfaces jsf implementation.
     * If a match is not found, control is passed to the decorated view handler.
     * Otherwise, a {@link org.apache.shale.clay.component.Clay} component is
     * instantiated as a single subtree under the
     * view root. The component's <code>id</code> property is set with a constant,
     * <code>CLAY_VIEW_ID</code>.  The <code>jsfid</code>
     * property is set to the <code>viewId</code>.  The <code>managedBeanName</code> property
     * is set with the Shale <code>ViewControllerMapper</code>.  A <code>ResponseWriter</code>
     * is created and rendering is invoked on the component.  This differs from the base implementation.
     * The base implementation would dispatch to a JSP that would assemble the component tree
     * and invoke rendering to the response writer.</p>
     *
     * @param context faces context
     * @param view root of the component tree
     * @exception IOException response writer
     */
    public void renderView(FacesContext context, UIViewRoot view)
            throws IOException {

        int index = indexOfClayTemplateSuffix(context, view.getViewId());
        //is this view a clay html template view
        if (index != -1) {

            if (log.isDebugEnabled()) {
                log.debug("Clay template renderView for " + view.getViewId());
            }

            //look to see if it already exists
            UIComponent component = view.findComponent(CLAY_VIEW_ID);
            if (component == null) {

            String viewId = normalizeViewId(view.getViewId(), index);
            view.setViewId(viewId);
              component = context.getApplication().createComponent(CLAY_COMPONENT_TYPE);
              ((Clay) component).setId(CLAY_VIEW_ID);
              ((Clay) component).setJsfid(viewId);
              ((Clay) component).setManagedBeanName(getManagedBeanName(context, viewId));
              view.getChildren().add(view.getChildren().size(), component);
            }

            //get the response
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            // TODO create a configurable method to set the content type
            if (response.getContentType() == null) {
                response.setContentType("text/html");
            }
            //set the locale
            response.setLocale(context.getViewRoot().getLocale());

            //builds a buffer to write the page to
            StringWriter writer = new StringWriter();
            //create a buffered response writer
            ResponseWriter buffResponsewriter = context.getRenderKit()
                  .createResponseWriter(writer, null, response.getCharacterEncoding());
            //push buffered writer to the faces context
            context.setResponseWriter(buffResponsewriter);
            //start a document
            buffResponsewriter.startDocument();

            try {
               recursiveRender(view, context);
            } catch (PageNotFoundException e) {
                //look to see if the page not found is a top level page
                if (e.getResource().equals(view.getViewId())) {
                   response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getResource());
                   context.responseComplete();
                   return;
                }

                throw e;
            }
            //end the document
            buffResponsewriter.endDocument();

            //save the view
            StateManager stateManager = context.getApplication().getStateManager();
            StateManager.SerializedView serializedview = stateManager.saveSerializedView(context);

            ResponseWriter responsewriter = context.getRenderKit()
                    .createResponseWriter(response.getWriter(), null, response.getCharacterEncoding());
            //push response writer to the faces context
            context.setResponseWriter(responsewriter);

            StringBuffer buff = writer.getBuffer();
            int curPos = 0;   // current position
            int fndPos = 0;   //start of a marker
            int frmMkrIdx = indexOfFormMarker();

            //might be multiple forms in the document
            do {
                fndPos = buff.indexOf(FORM_MARKERS[frmMkrIdx], curPos);
                if (fndPos > -1) {
                    responsewriter.write(buff.substring(curPos, fndPos));
                    stateManager.writeState(context, serializedview);
                    curPos = fndPos + FORM_MARKERS[frmMkrIdx].length();
                } else {
                    responsewriter.write(buff.substring(curPos));
                }
            } while(curPos < buff.length() && fndPos > -1);


        } else {
           //dispatch (forward) to the jsp
           original.renderView(context, view);
        }
    }

    /**
     * <p>
     * Recursively invokes the rendering of the sub component tree.
     * </p>
     *
     * @param child component to invoke renderering on
     * @param context faces context
     * @exception IOException writing markup
     */
    protected void recursiveRender(UIComponent child,
            FacesContext context) throws IOException {

        if (!child.getRendersChildren()) {
            child.encodeBegin(context);
            Iterator ci = child.getChildren().iterator();
            while (ci.hasNext()) {
                UIComponent c = (UIComponent) ci.next();
                c.encodeBegin(context);

                if (!c.getRendersChildren()) {
                    recursiveRender(c, context);
                } else {
                    c.encodeChildren(context);
                }

                c.encodeEnd(context);
                c = null;
            }
            child.encodeEnd(context);
        } else {
            // let the component handle iterating over the children
            child.encodeBegin(context);
            child.encodeChildren(context);
            child.encodeEnd(context);
        }
    }

    /**
     * <p>Returns the "@managed-bean-name" the view controller is registered under.  The
     * assumed mapping will be the same as in core Shale.</p>
     *
     * @param context faces context
     * @param viewId name of the page
     * @return default managed bean name associated with the view
     */
    protected String getManagedBeanName(FacesContext context, String viewId) {
        String managedBeanName = null;

        Object mapper = context.getApplication().getVariableResolver()
                                 .resolveVariable(context, VIEW_MAPPER);
        // is there a view controller mapper
        if (mapper != null) {
            StringBuffer el = new StringBuffer();
            el.append("#{").append(VIEW_MAPPER).append(".mapViewId").append("}");
            MethodBinding mb = context.getApplication()
                    .createMethodBinding(el.toString(), new Class[] {String.class});
            managedBeanName = (String) mb.invoke(context, new Object[] {viewId});
        }

        return managedBeanName;
    }

}
