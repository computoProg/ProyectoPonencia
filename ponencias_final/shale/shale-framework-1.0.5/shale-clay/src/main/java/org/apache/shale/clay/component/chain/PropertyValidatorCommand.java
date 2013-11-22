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
 * $Id: PropertyValidatorCommand.java 471910 2006-11-06 22:44:56Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * This <code>Command</code> will create an <code>validator</code> method
 * binding and assign it to the <code>UIComponent</code> implementing the
 * <code>EditableValueHolder</code> interface. This <code>Command</code> is
 * invoked from the {@link AssignPropertiesCommand} chain.
 * </p>
 */
public class PropertyValidatorCommand extends AbstractCommand implements
        Command {

    /**
     * <p>
     * Common logger utility class.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(PropertyValidatorCommand.class);
    }

    /**
     * <p>
     * Looks to see if the {@link AttributeBean} on the {@link ClayContext} is a
     * <code>validator</code> attribute. If it is, create a
     * <code>MethodBinding</code> and assign it to the component returning a
     * <code>true</code> value. Otherwise, return a <code>false</code>
     * value.
     * </p>
     *
     * @param context common chains
     * @return <code>true</code> if the chain is complete
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.clayContext"));
        }
        AttributeBean attributeBean = clayContext.getAttribute();
        if (attributeBean == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.attributeBean"));
        }
        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.componentBean"));
        }
        FacesContext facesContext = clayContext.getFacesContext();
        if (facesContext == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.facesContext"));
        }
        if (attributeBean.getName().equals("validator") && attributeBean.getValue() != null) {
            isFinal = true;

            UIComponent child = (UIComponent) clayContext.getChild();
            if (child == null) {
                throw new NullPointerException("child");
            }

            if (child instanceof EditableValueHolder) {

                String expr = replaceMnemonic(clayContext);
                getTagUtils().setValidator(child, expr);

            } else {
                log.error(getMessages().getMessage("property.validator.error", new Object[] {attributeBean}));
            }
        }

        return isFinal;
    }
}
