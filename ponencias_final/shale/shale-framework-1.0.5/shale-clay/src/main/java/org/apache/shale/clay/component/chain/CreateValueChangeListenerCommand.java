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
 * $Id: CreateValueChangeListenerCommand.java 471910 2006-11-06 22:44:56Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ValueChangeListener;

import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * Creates a {@link ValueChangeListener} and assign it to the
 * <code>parent</code> property in the {@link ClayContext}.
 */
public class CreateValueChangeListenerCommand extends AbstractCommand {

    /**
     * <p>
     * Common Logging utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(CreateValueChangeListenerCommand.class);
    }

    /**
     * <p>
     * Creates a <code>ValueChangeListener</code> and assigns it to the
     * <code>parent</code> attribute.
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

        ValueChangeListener listener = null;
        try {

            AttributeBean attr = displayElement.getAttribute("binding");
            if (attr != null && isValueReference(attr.getValue())) {
                clayContext.setAttribute(attr);
                String expr = replaceMnemonic(clayContext);
                ValueBinding vb = facesContext.getApplication().createValueBinding(expr);
                listener = (ValueChangeListener) vb.getValue(facesContext);

            } else {

                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                if (loader == null) {
                    loader = getClass().getClassLoader();
                }

                listener = (ValueChangeListener) loader.loadClass(
                        displayElement.getComponentType()).newInstance();

                loader = null;
            }
        } catch (Exception e) {
            log.error(getMessages().getMessage("create.valueChangeListener"), e);
            throw e;
        }
        parent.addValueChangeListener(listener);
        // reassign the child to the valueChangeListener for the
        // AssignPropertiesCommand
        clayContext.setChild(listener);

        return isFinal;
    }

}
