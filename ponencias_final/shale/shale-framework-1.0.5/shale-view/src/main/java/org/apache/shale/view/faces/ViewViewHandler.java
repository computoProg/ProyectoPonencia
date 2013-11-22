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

package org.apache.shale.view.faces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ViewController;
import org.apache.shale.view.ViewControllerMapper;

/**
 * <p>{@link ViewViewHandler} is a custom implementation of <code>ViewHandler</code>
 * that adds support for on-demand creation and configuration of {@link ViewController}
 * instances, and other view related functionality.</p>
 *
 * $Id: ViewViewHandler.java 489926 2006-12-23 20:56:29Z craigmcc $
 */

public class ViewViewHandler extends ViewHandler {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Create a {@link ViewViewHandler} instance that decorates the
     * specified <code>ViewHandler</code> provided by the JSF runtime
     * implementation.</p>
     *
     * @param original Original <code>ViewHandler</code> to be decorated
     */
    public ViewViewHandler(ViewHandler original) {
        this.original = original;
    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Log instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(ViewViewHandler.class);


    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.view.resources.Bundle",
                   ViewViewHandler.class.getClassLoader());


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Cached {@link ViewControllerMapper} we will use to translate
     * view identifiers to the class name of a {@link ViewController}.</p>
     */
    private ViewControllerMapper mapper = null;


    /**
     * <p>The <code>ViewHandler</code> instance we are decorating.  All requests
     * are delegated to this instance, before or after any special handling that
     * is required.</p>
     */
    private ViewHandler original = null;


    // ----------------------------------------------------- ViewHandler Methods


    /** {@inheritDoc} */
    public Locale calculateLocale(FacesContext context) {
        return original.calculateLocale(context);
    }


    /** {@inheritDoc} */
    public String calculateRenderKitId(FacesContext context) {
        return original.calculateRenderKitId(context);
    }


    /**
     * <p>After delegating to our original <code>ViewHandler</code>,
     * create and initialize any {@link ViewController} associated with
     * the specified view identifier.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param viewId View identifier of the view to be created
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot view = original.createView(context, viewId);
        setupViewController(context, view, viewId, false);
        return view;
    }


    /** {@inheritDoc} */
    public String getActionURL(FacesContext context, String viewId) {
        return original.getActionURL(context, viewId);
    }


    /** {@inheritDoc} */
    public String getResourceURL(FacesContext context, String path) {
        return original.getResourceURL(context, path);
    }


    /** {@inheritDoc} */
    public void renderView(FacesContext context, UIViewRoot view)
      throws IOException, FacesException {
        original.renderView(context, view);
    }


    /**
     * <p>After delegating to our original <code>ViewHandler</code>,
     * create and initialize any {@link ViewController} associated with
     * the specified view identifier.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param viewId View identifier of the view to be restored
     */
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot view = original.restoreView(context, viewId);
        setupViewController(context, view, viewId, true);
        return view;
    }


    /** {@inheritDoc} */
    public void writeState(FacesContext context) throws IOException {
        original.writeState(context);
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link ViewControllerMapper} instance we will use to
     * map view identifiers to class names of the corresponding
     * {@link ViewController} class.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private ViewControllerMapper getViewControllerMapper(FacesContext context) {

        // DEPRECATED - respect the context init parameter if specified
        if (mapper == null) {
            mapper = getViewControllerMapperInstance(context);
        }

        // DEPRECATED - return cached instance from context init parameter
        if (mapper != null) {
            return mapper;
        }

        // Live lookup of the appropriate mapper as a managed bean
        ValueBinding vb = context.getApplication().createValueBinding
          ("#{" + Constants.VIEW_MAPPER + "}");
        ViewControllerMapper vcm = (ViewControllerMapper) vb.getValue(context);
        return vcm;

    }


    /**
     * <p>Create and return the custom configured {@link ViewControllerMapper}
     * instance we will use for this application, or <code>null</code>
     * if there is no such instance.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     *
     * @deprecated As of version 1.0.3, replace the application scoped
     *  managed bean at FacesConstants.VIEW_MAPPER instead of using the
     *  deprecated context initialization parameter mentioned here
     */
    private ViewControllerMapper getViewControllerMapperInstance
            (FacesContext context) {

        String className =
          context.getExternalContext().getInitParameter(Constants.VIEW_CONTROLLER_MAPPER);
        if (className == null) {
            return null;
        }
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        try {
            Class clazz = cl.loadClass(className);
            return (ViewControllerMapper) clazz.newInstance();
        } catch (ClassCastException e) {
            throw new FacesException
              (messages.getMessage("view.vcmCast",
                                   new Object[] { className }), e);
        } catch (ClassNotFoundException e) {
            throw new FacesException
              (messages.getMessage("view.vcmClass",
                                   new Object[] { className }), e);
        } catch (IllegalAccessException e) {
            throw new FacesException
              (messages.getMessage("view.vcmAccess",
                                   new Object[] { className }), e);
        } catch (InstantiationException e) {
            throw new FacesException
              (messages.getMessage("view.vcmInstantiate",
                                   new Object[] { className }), e);
        }

    }


    /**
     * <p>Create and initialize an appropriate {@link ViewController} instance
     * associated with the specified view, which was just created or just
     * restored.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param view <code>UIViewRoot</code> just created or restored
     *  (or <code>null</code> if there was no such view)
     * @param viewId of the <code>UIViewRoot</code> just created or
     *  restored
     * @param postBack <code>true</code> if this is a post back to
     *  an existing view
     */
    private void setupViewController(FacesContext context, UIViewRoot view,
                                     String viewId, boolean postBack) {

        // Is there actually a view for us to procses?
        if (view == null) {
            return;
        }

        // Cache the postback flag so that it can be used inside
        // the AbstractViewcontroller implementation
        if (postBack) {
            context.getExternalContext().getRequestMap().
              put(FacesConstants.VIEW_POSTBACK, Boolean.TRUE);
        }

        // Its not the responsibilty of createView method to set the viewId of the view
        // (See JSF 1.1 spec. pg. 7-16)
        //String viewId = view.getViewId();
        if (log.isDebugEnabled()) {
            log.debug("setupViewController(" + viewId + "," + postBack + ")");
        }

        // Map our view identifier to a corresponding managed bean name
        ViewControllerMapper viewControllerMapper = getViewControllerMapper(context);
        if (viewControllerMapper == null) {
            log.warn(messages.getMessage("view.noViewControllerMapper"));
            return;
        }
        String viewName = viewControllerMapper.mapViewId(viewId);

        // Retrieve an existing instance, or one created and configured by
        // the managed bean facility
        Object vc = null;
        VariableResolver vr =
            context.getApplication().getVariableResolver();
        try {
            vc = vr.resolveVariable(context, viewName);
            if (vc == null) {
                if (log.isDebugEnabled()) {
                    log.debug(messages.getMessage("view.noViewController",
                                                  new Object[] { viewId, viewName }));
                }
                // We are navigating to a page with no ViewControllerMapper
                // compatible managed bean defined.  Therefore, clear the
                // flag that would otherwise cause an originating ViewController's
                // prerender() method to be called
                context.getExternalContext().getRequestMap().remove
                        (FacesConstants.VIEW_NAME_RENDERED);
                return;
            }
        } catch (EvaluationException e) {
            log.warn(messages.getMessage("view.evalException",
                                          new Object[] { viewId, viewName }), e);
            return;
        }

        // Set the postBack property on a ViewController instance
        if (vc instanceof ViewController) {
            ((ViewController) vc).setPostBack(postBack);
        }

        // Schedule this instance for later processing as needed
        Map map = context.getExternalContext().getRequestMap();
        map.put(FacesConstants.VIEW_NAME_RENDERED, viewName);
        List list = (List) map.get(FacesConstants.VIEWS_INITIALIZED);
        if (list == null) {
            list = new ArrayList();
            map.put(FacesConstants.VIEWS_INITIALIZED, list);
        }
        list.add(vc);

    }


    /**
     * <p>Return the {@link ViewControllerCallbacks} instance we
     * will use.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     *
     * @since 1.0.1
     */
    private ViewControllerCallbacks getViewControllerCallbacks(FacesContext context) {

        ValueBinding vb = context.getApplication().createValueBinding
          ("#{" + FacesConstants.VIEW_CALLBACKS + "}");
        return (ViewControllerCallbacks) vb.getValue(context);

    }


}
