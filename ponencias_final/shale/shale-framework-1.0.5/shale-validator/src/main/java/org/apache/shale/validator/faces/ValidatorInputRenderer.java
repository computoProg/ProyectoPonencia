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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import org.apache.shale.util.Tags;
import org.apache.shale.validator.CommonsValidator;


/**
 * <p>This renderer is a hybrid renderer decorator that is dynamically
 * registered by the {@link ValidatorRenderKit}
 * for component renderers in the "javax.faces.Input" family.</p>
 */
public class ValidatorInputRenderer extends Renderer {


    /**
     * <p>The Renderer that we are wrapping.</p>
     */
    private Renderer defaultRenderer = null;


    /**
     * <p>This constant is the name of a reserved attribute that will hold
     * a <code>Map</code> of clientId's for the component.</p>
     */
    public static final String VALIDATOR_CLIENTIDS_ATTR = "org.apache.shale.validator.clientIdSet";


    /**
     * <p>Overloaded constructor is passed the original
     * <code>Renderer</code>.</p>
     *
     * @param defaultRenderer The Renderer we should wrap
     */
    public ValidatorInputRenderer(Renderer defaultRenderer) {
       this.defaultRenderer = defaultRenderer;
    }


    /** {@inheritDoc} */
    public String convertClientId(FacesContext context, String id) {
        return defaultRenderer.convertClientId(context, id);
    }


    /** {@inheritDoc} */
    public void decode(FacesContext context, UIComponent component) {
        defaultRenderer.decode(context, component);
    }


    /**
     * <p>This override captures the clientId of the target component before
     * passing on to the original renderer.  The clientId is added to a Map
     * that is used by the {@link org.apache.shale.component.ValidatorScript}
     * component for adding client side JavaScript validation.  This hook is
     * needed when the {@link org.apache.shale.validator.CommonsValidator}
     * is added to a UIData subclass.  The components in this class are not
     * unique per row so the clientId can only be captured during the rendering
     * process.  The Map also contains a snapshot of validator var arguments
     * that contain value binding expressions.  This snapshot of state at
     * renderering is used by the client side JavaScript.  The snapshot
     * allows client side validation in UIData components.</p>
     *
     * @param context FacesContext for the current request
     * @param component UIComponent being rendered
     *
     * @exception IOException if an input/output error occurs
     */
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

        if (component instanceof EditableValueHolder && component.isRendered()) {

                Tags tagUtils = new Tags();

                EditableValueHolder editableComponent = (EditableValueHolder) component;

                // A map that captures information about a component that might contain
                // commons validators.  The map is organized by a hierarchy "clientId/validatorType/vars"
                Map ids = (Map) component.getAttributes().get(VALIDATOR_CLIENTIDS_ATTR);
                if (ids == null) {
                    ids = new TreeMap();
                    component.getAttributes().put(VALIDATOR_CLIENTIDS_ATTR, ids);
                }

                // captrue the clientId before renderering
                String clientId = component.getClientId(context);
                Map validatorVars = (Map) ids.get(clientId);
                if (validatorVars == null) {
                    validatorVars = new TreeMap();
                    ids.put(clientId, validatorVars);
                }


                Validator[] validators = editableComponent.getValidators();
                // look for components using CommonsValidator
                for (int i = 0; i < validators.length; i++) {
                    if (validators[i] instanceof CommonsValidator) {
                       CommonsValidator validator = (CommonsValidator) validators[i];

                       // look for a map of var's by component type
                       Map localVars = (Map) validatorVars.get(validator.getType());
                       if (localVars == null) {
                           localVars = new TreeMap();
                           validatorVars.put(validator.getType(), localVars);
                       } else {
                           localVars.clear();
                       }

                       Map vars = validator.getVars();
                       Iterator vi = vars.entrySet().iterator();
                       while (vi.hasNext()) {
                          Map.Entry e = (Map.Entry) vi.next();
                          // only override if the var contains a value binding expression
                          if (e.getValue() != null && e.getValue() instanceof String
                              && isValueReference((String) e.getValue())) {

                             localVars.put(e.getKey(), tagUtils.eval((String) e.getValue()));

                          }
                       }

                    }
                }

        }

        defaultRenderer.encodeBegin(context, component);
    }


    /** {@inheritDoc} */
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        defaultRenderer.encodeChildren(context, component);
    }


    /** {@inheritDoc} */
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        defaultRenderer.encodeEnd(context, component);
    }


    /** {@inheritDoc} */
    public Object getConvertedValue(FacesContext context, UIComponent component,
            Object value) throws ConverterException {
        return defaultRenderer.getConvertedValue(context, component, value);
    }


    /** {@inheritDoc} */
    public boolean getRendersChildren() {
        return defaultRenderer.getRendersChildren();
    }


    /**
     * <p>Return true if the specified string contains an EL expression.</p>
     * 
     * <p>This is taken almost verbatim from {@link javax.faces.webapp.UIComponentTag}
     * in order to remove JSP dependencies from the renderers.</p>
     *
     * @param value String to be checked for being an expression
     */
    private boolean isValueReference(String value) {

        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");
        if (start < 0) {
            return false;
        }

        int end = value.lastIndexOf('}');
        return (end >= 0) && (start < end);
    }


}
