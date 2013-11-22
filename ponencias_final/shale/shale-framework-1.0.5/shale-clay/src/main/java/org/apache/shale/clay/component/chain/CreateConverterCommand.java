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
 * $Id: CreateConverterCommand.java 472598 2006-11-08 19:13:12Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;

import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * This <code>Command</code> will create a <code>Converter</code> from the
 * <code>displayElement</code> attribute of the
 * {@link org.apache.shale.clay.component.chain.ClayContext} assigning
 * it to the <code>parent</code>.
 * </p>
 *
 */

public class CreateConverterCommand extends AbstractCommand {

    /**
     * <p>
     * Common Logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(CreateConverterCommand.class);
    }

    /**
     * <p>
     * Creates a object instance realizing the {@link Converter} interface
     * assigning to the <code>parent</code>. The <code>parent</code> must
     * implement the {@link ValueHolder} interface.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if chain is done
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.clayContext"));
        }

        UIComponent child = (UIComponent) clayContext.getChild();
        if (child == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.childComponent"));
        }

        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.componentBean"));
        }

        ValueHolder parent = (ValueHolder) clayContext.getParent();
        if (parent == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.parentComponent"));
        }

        FacesContext facesContext = clayContext.getFacesContext();
        if (facesContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.facesContext"));
        }
        Converter converter = null;
        try {

            AttributeBean attr = displayElement.getAttribute("binding");
            if (attr != null && isValueReference(attr.getValue())) {
                clayContext.setAttribute(attr);
                String expr = replaceMnemonic(clayContext);
                ValueBinding vb = facesContext.getApplication().createValueBinding(expr);
                converter = (Converter) vb.getValue(facesContext);
            } else {
                // the default converter id comes for the component type
                String converterId = displayElement.getComponentType();
                // check for a converterId attribute override
                attr = displayElement.getAttribute("converterId");
                if (attr != null && attr.getValue() != null
                        && attr.getValue().length() > 0) {
                    clayContext.setAttribute(attr);
                    String tmp = getTagUtils().evalString(replaceMnemonic(clayContext));
                    if (tmp != null && tmp.length() > 0) {
                        converterId = tmp;
                    }
                }
                converter = facesContext.getApplication().createConverter(converterId);
            }
        } catch (Exception e) {
            log.error(getMessages().getMessage("create.converter.error",
                    new Object[] { displayElement }), e);
            throw e;
        }

        if (converter != null) {
            parent.setConverter(converter);
            // reassign the child to the converter for the
            // AssignPropertiesCommand
            clayContext.setChild(converter);
        } else {
            isFinal = true;
        }

        return isFinal;
    }

}
