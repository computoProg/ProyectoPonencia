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
 * $Id: CreateValidatorCommand.java 471910 2006-11-06 22:44:56Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;

import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * Creates an object implementing the {@link Validator} interface and assigns it
 * to the <code>parent</code> from the {@link ClayContext}.
 * </p>
 */
public class CreateValidatorCommand extends AbstractCommand {

    /**
     * <p>
     * Common Logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(CreateValidatorCommand.class);
    }

    /**
     * <p>
     * Creates a faces validator object that is registered in the
     * <strong>faces-config.xml</strong> file and assigns it to the parent.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is final
     * @exception Exception propagated to the top chain
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

        EditableValueHolder parent = (EditableValueHolder) clayContext
                .getParent();
        if (parent == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.parentComponent"));
        }

        FacesContext facesContext = clayContext.getFacesContext();
        if (facesContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.facesContext"));
        }

        Validator validator = null;
        try {
            AttributeBean attr = displayElement.getAttribute("binding");
            if (attr != null && isValueReference(attr.getValue())) {
                clayContext.setAttribute(attr);
                String expr = replaceMnemonic(clayContext);
                ValueBinding vb = facesContext.getApplication()
                        .createValueBinding(expr);
                validator = (Validator) vb.getValue(facesContext);

            } else {
                validator = facesContext.getApplication().createValidator(
                        displayElement.getComponentType());
            }
        } catch (Exception e) {
            log.error(getMessages().getMessage("create.validator.error",
                    new Object[] { displayElement }), e);
            throw e;
        }
        parent.addValidator(validator);
        // reassign the child to the validator for the
        // AssignPropertiesCommand
        clayContext.setChild(validator);

        return isFinal;
    }

}
