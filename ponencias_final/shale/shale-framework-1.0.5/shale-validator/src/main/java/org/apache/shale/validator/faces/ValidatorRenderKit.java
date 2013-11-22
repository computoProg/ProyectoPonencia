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

import java.io.OutputStream;
import java.io.Writer;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;

/**
 * <p>Decorates the original <code>RenderKit</code> passed by the
 * overloaded constructor in the {@link org.apache.shale.view.faces.ViewViewHandler}.
 * The majority of the implementation is passed on the the original
 * <code>RenderKit</code> but requests for renderers registered with the
 * "javax.faces.Command" and "javax.faces.Input" families are decorated.
 * Only renderers in the "javax.faces.Command" family of types
 * "javax.faces.Link" and "javax.faces.Button" are considered.  These are
 * renderers from the vanilla JSF runtime that have predictable behavior.
 * The wrapper adds special behavior for the
 * {@link org.apache.shale.validator.CommonsValidator} validator and
 * {@link org.apache.shale.component.ValidatorScript} component.</p>
 */
public class ValidatorRenderKit extends RenderKit {

    private static final String COMMAND_FAMILY = "javax.faces.Command";
    private static final String INPUT_FAMILY = "javax.faces.Input";
    private static final String COMMAND_LINK_TYPE = "javax.faces.Link";
    private static final String COMMAND_BUTTON_TYPE = "javax.faces.Button";


    /**
     * <p>The original RenderKit.</p>
     */
    private RenderKit defaultRenderKit = null;


    /**
     * <p>This constructor is overloaded to pass the original
     * <code>RenderKit</code></p>.
     *
     * @param defaultRenderKit The default RenderKit that we will be wrapping
     */
    public ValidatorRenderKit(RenderKit defaultRenderKit) {
       this.defaultRenderKit = defaultRenderKit;
    }


    /** {@inheritDoc} */
    public void addRenderer(String componentFamily, String rendererType, Renderer renderer) {
       this.defaultRenderKit.addRenderer(componentFamily, rendererType, renderer);
    }


    /**
     * <p>If the component family is not "javax.faces.Command" or
     * "javax.faces.Input", the <code>defaultRenderKit</code> handles the
     * request.  If the family is "javax.faces.Command", and the renderer type
     * is "javax.faces.Link" or "javax.faces.Button" the default
     * renderer is decorated with {@link org.apache.shale.renderer.ValidatorCommandRenderer}.
     * If the component family is "javax.faces.Input", the default
     * renderer is decorated with {@link org.apache.shale.renderer.ValidatorInputRenderer}.
     *
     * @param componentFamily Component family for which to retrieve a Renderer
     * @param rendererType Renderer type for which to retrieve a Renderer
     */
    public Renderer getRenderer(String componentFamily, String rendererType) {
        Renderer target = defaultRenderKit.getRenderer(componentFamily, rendererType);
        if (componentFamily.equals(COMMAND_FAMILY) &&
            (rendererType.equals(COMMAND_LINK_TYPE) ||
             rendererType.equals(COMMAND_BUTTON_TYPE))) {
           if (!(target instanceof ValidatorCommandRenderer)) {
               target = new ValidatorCommandRenderer(target);
               addRenderer(componentFamily, rendererType, target);
           }
        } else if (componentFamily.equals(INPUT_FAMILY)) {
            if (!(target instanceof ValidatorInputRenderer)) {
                target = new ValidatorInputRenderer(target);
                addRenderer(componentFamily, rendererType, target);
            }
        }
        return target;
    }


    /** {@inheritDoc} */
    public ResponseStateManager getResponseStateManager() {
        return defaultRenderKit.getResponseStateManager();
    }


    /** {@inheritDoc} */
    public ResponseWriter createResponseWriter(Writer writer,
            String contentTypeList,
            String characterEncoding) {
        return defaultRenderKit.createResponseWriter(writer, contentTypeList, characterEncoding);
    }


    /** {@inheritDoc} */
    public ResponseStream createResponseStream(OutputStream outputStream) {
        return defaultRenderKit.createResponseStream(outputStream);
    }


}
