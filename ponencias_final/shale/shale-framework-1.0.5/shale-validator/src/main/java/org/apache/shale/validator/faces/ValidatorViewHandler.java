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

package org.apache.shale.validator.faces;

import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;

/**
 * <p>{@link ValidatorViewHandler} is a custom implementation of <code>ViewHandler</code>
 * that adds support for setting up a decorated RenderKit.</p>
 */

public class ValidatorViewHandler extends ViewHandler {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Create a {@link ValidatorViewHandler} instance that decorates the
     * specified <code>ViewHandler</code> provided by the JSF runtime
     * implementation.</p>
     *
     * @param original Original <code>ViewHandler</code> to be decorated
     */
    public ValidatorViewHandler(ViewHandler original) {
        this.original = original;
    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Log instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(ValidatorViewHandler.class);


    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.validator.resources.Bundle",
                   ValidatorViewHandler.class.getClassLoader());


    // ------------------------------------------------------ Instance Variables


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
     * ensure that our decorator RenderKit has been initialized.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param viewId View identifier of the view to be created
     */
    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot view = original.createView(context, viewId);
        setupRenderKit(context, view);
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


    /** {@inheritDoc} */
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return original.restoreView(context, viewId);
    }


    /** {@inheritDoc} */
    public void writeState(FacesContext context) throws IOException {
        original.writeState(context);
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>The default RenderKit is decorated with {@link ValidatorRenderKit}.
     * This wrapper intercepts component renderer's decorating them.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param view <code>UIViewRoot</code> for the current component tree
     */
    private void setupRenderKit(FacesContext context, UIViewRoot view) {

        RenderKitFactory factory =
          (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit defaultRenderKit =
          factory.getRenderKit(context, view.getRenderKitId());
        if (!(defaultRenderKit instanceof ValidatorRenderKit)) {
            factory.addRenderKit(view.getRenderKitId(),
                                 new ValidatorRenderKit(defaultRenderKit));
        }

    }


}
