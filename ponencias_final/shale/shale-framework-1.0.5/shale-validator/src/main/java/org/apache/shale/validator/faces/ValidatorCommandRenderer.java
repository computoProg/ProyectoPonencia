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
import java.io.StringWriter;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;

/**
 * <p>This Renderer is a hybrid renderer decorator that is dynamically
 * registered by {@link ValidatorRenderKit}
 * for component renderers in the "javax.faces.Command" family.</p>
 */
public class ValidatorCommandRenderer extends Renderer {


    /**
     * <p>The Original Renderer.</p>
     */
    private Renderer defaultRenderer = null;


    /**
     * <p>The overloaded constructor is passed the original
     * <code>Renderer</code> for the family and component type.</p>
     *
     * @param defaultRenderer The default Renderer we should delegate to
     */
    public ValidatorCommandRenderer(Renderer defaultRenderer) {
       this.defaultRenderer = defaultRenderer;
    }


    /**
     * <p>Attribute name used to override the default behavior of how the immediate
     * attribute effects the execution of client side javascript validation.</p>
     */
    public static final String OVERRIDE_IMMEDIATE = "org.apache.shale.validator.immediate";

    private static final int ENCODE_BEGIN = 0;

    private static final int ENCODE_CHILDREN = 1;

    private static final int ENCODE_END = 2;


    /**
     * <b>Interrogates the component's immediate property and the component's
     * immediate override attribute to determine if client side validation is
     * invoked. If either the property or attribute override is false, client
     * side validation is invoked. Otherwise, the response writer is hijacked
     * and the original render is invoked. The result is buffered and a
     * statement of javascript is injected into the onclick event which cancels
     * client side validation. The original response writer is restored and the
     * modified markup is written to the response writer. The
     * <code>encodeSwitch</code> determines if the encodeBegin, encodeChildren
     * or encodeEnd methods should be invoked on the decorated renderer.</b>
     *
     * @param context FacesContext for the current request
     * @param component UIComponent being rendered
     * @param encodeSwitch FIXME - encode switch?
     *
     * @exception IOException if an input/output error occurs
     */
    protected void encode(FacesContext context, UIComponent component,
            int encodeSwitch) throws IOException {

        UICommand command = (UICommand) component;

        // look for a override to the default
        boolean immediateOverride = true;
        String attr = (String) component.getAttributes()
                .get(OVERRIDE_IMMEDIATE);
        if (attr != null) {
            immediateOverride = Boolean.valueOf(attr).booleanValue();
        }

        if (command.isImmediate() && immediateOverride) {

            ResponseWriter hijackedWriter = context.getResponseWriter();
            // builds a buffer to write the page to
            StringWriter writer = new StringWriter();
            // create a buffered response writer
            ResponseWriter buffResponsewriter = context.getRenderKit()
                    .createResponseWriter(writer, null,
                            hijackedWriter.getCharacterEncoding());
            // push buffered writer to the faces context
            context.setResponseWriter(buffResponsewriter);

            if (encodeSwitch == ENCODE_BEGIN) {
                defaultRenderer.encodeBegin(context, component);
            } else if (encodeSwitch == ENCODE_CHILDREN) {
                defaultRenderer.encodeChildren(context, component);
            } else {
                defaultRenderer.encodeEnd(context, component);
            }

            buffResponsewriter.write(' ');
            buffResponsewriter.flush();
            buffResponsewriter.close();
            writer.flush();
            writer.close();
            StringBuffer buff = writer.getBuffer();
            int i = buff.indexOf("onclick=\"");
            if (i > 0) {
                buff.insert(i + "onclick=\"".length(), "bCancel=true;");
            }

            hijackedWriter.write(buff.toString());
            context.setResponseWriter(hijackedWriter);

        } else {

            if (encodeSwitch == ENCODE_BEGIN) {
                defaultRenderer.encodeBegin(context, component);
            } else if (encodeSwitch == ENCODE_CHILDREN) {
                defaultRenderer.encodeChildren(context, component);
            } else {
                defaultRenderer.encodeEnd(context, component);
            }

        }

    }


    /** {@inheritDoc} */
    public String convertClientId(FacesContext context, String id) {
        return defaultRenderer.convertClientId(context, id);
    }


    /** {@inheritDoc} */
    public Object getConvertedValue(FacesContext context, UIComponent component, Object o) throws ConverterException {
        return defaultRenderer.getConvertedValue(context, component, o);
    }


    /** {@inheritDoc} */
    public void decode(FacesContext context, UIComponent component) {
        defaultRenderer.decode(context, component);
    }


    /**
     * <p>
     * Invokes the <code>encode</code> method passing
     * <code>ENCODE_BEGIN</code> for the encodeSwitch parameter.
     * </p>
     *
     * @param context FacesContext for the current request
     * @param component UIComponent being rendered
     *
     * @exception IOException if an input/output error occurs
     */
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        encode(context, component, ENCODE_BEGIN);
    }


    /**
     * <p>Invokes the <code>encode</code> method passing
     * <code>ENCODE_CHILDREN</code> for the encodeSwitch parameter.</p>
     *
     * @param context FacesContext for the current request
     * @param component UIComponent being rendered
     *
     * @exception IOException if an input/output error occurs
     */
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        encode(context, component, ENCODE_CHILDREN);
    }


    /**
     * <p>Invokes the <code>encode</code> method passing <code>ENCODE_END</code>
     * for the encodeSwitch parameter.</p>
     *
     * @param context FacesContext for the current reqauest
     * @param component UIComponent being rendered
     *
     * @exception IOException if an input/output error occurs
     */
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        encode(context, component, ENCODE_END);
    }


    /** {@inheritDoc} */
    public boolean getRendersChildren() {
        return defaultRenderer.getRendersChildren();
    }


}
