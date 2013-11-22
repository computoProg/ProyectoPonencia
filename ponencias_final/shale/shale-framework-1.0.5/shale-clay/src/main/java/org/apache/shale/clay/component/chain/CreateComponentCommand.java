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
 * $Id: CreateComponentCommand.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.Attributes;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * This <code>Command</code> is used to build <strong>parent</strong> and
 * <strong>child</strong> <code>UIComponent</code>'s from the
 * <code>displayElement</code> in the {@link org.apache.shale.clay.component.chain.ClayContext}.
 * </p>
 */
public class CreateComponentCommand extends AbstractCommand {

    /**
     * <p>
     * Common Logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(CreateComponentCommand.class);
    }


    /**
     * <p>
     * Creates a new faces component from the component metadata.
     * </p>
     * @param context common chains
     * @return <code>true</code> if the chain is complete
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = true;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.clayContext"));
        }

        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.componentBean"));
        }

        UIComponent parent = clayContext.getParent();
        if (parent == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.parentBean"));
        }

        FacesContext facesContext = clayContext.getFacesContext();
        if (facesContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.facesContext"));
        }

        // create a new scoped symbol table
        Map symbolTable = new Attributes();
        // inherit the parents symbols
        symbolTable.putAll(clayContext.getSymbols());
        // override config (XML, HTML) symbols
        symbolTable.putAll(displayElement.getSymbols());
        // push to context
        clayContext.setSymbols(symbolTable);

        // evaluate nested symbols; symbols having symbols as values
        realizeSymbols(clayContext);

        UIComponent child = null;
        Long jspId = new Long(displayElement.getJspId());
        clayContext.getJspIds().add(jspId);

        String facetName = displayElement.getFacetName();
        if (facetName != null) {
            facetName = replaceMnemonic(clayContext, facetName);
        }
        if (facetName != null) {
            child = parent.getFacet(displayElement.getFacetName());
        }
        if (child == null) {
           child = this.findComponentByJspId(parent, jspId);
        }
        // always burn an id; this is to support the early versions of myfaces & RI 1.1
        // the UIViewRoot didn't persist the sequence that is used by the call to createUniqueId
        String id = facesContext.getViewRoot().createUniqueId();
        if (child == null) {
            if (displayElement.getId() != null) {
                id = replaceMnemonic(clayContext, displayElement.getId());
            }
        } else {
            id = child.getId();
        }

        //Check to see if the replacement failed.  This can happen if the
        //symbol is missing.  The id will still containing the symbol delimiter
        //character.  The other scenario is if the value of the symbol is null.

        if (id == null || id.indexOf('@') > -1) {

           if (id == null) {
              id = displayElement.getId();
           }

           throw new RuntimeException(getMessages().getMessage("create.component.invalid.id",
                   new Object[] {id, symbolTable}));
        }

        if (child == null) {
            try {
                AttributeBean attr = displayElement.getAttribute("binding");
                if (attr != null
                    && attr.getValue() != null) {

                   clayContext.setAttribute(attr);
                   String expr = replaceMnemonic(clayContext);
                   ValueBinding vb = facesContext.getApplication().createValueBinding(expr);
                   child = facesContext.getApplication().createComponent(vb, facesContext,
                                                                    displayElement.getComponentType());
                   child.setValueBinding("binding", vb);

                } else {

                   child = facesContext.getApplication().createComponent(
                        displayElement.getComponentType());
                }

            } catch (Exception e) {
                log.error(getMessages().getMessage("create.component.error",
                        new Object[] { displayElement }), e);
                        throw e;
            }

            child.setId(id);
            child.getAttributes().put(Globals.CLAY_JSPID_ATTRIBUTE, jspId);
            if (facetName != null) {
                parent.getFacets().put(facetName, child);

                if (log.isDebugEnabled()) {
                    log.debug(getMessages().getMessage("create.facet.component",
                        new Object[] { id, displayElement.getJsfid()}));
                }

            } else {
                parent.getChildren().add(clayContext.getChildIndex(), child);

                if (log.isDebugEnabled()) {
                    log.debug(getMessages().getMessage("create.component",
                        new Object[] { id, displayElement.getJsfid(), new Integer(clayContext.getChildIndex()) }));
                }

            }

            if (child instanceof Clay) {
                //save the display element used to create the component for exception
                //reporting when the jsfid is null.  This should only happen when using
                //symbol replacement of a nested clay component with HTML views
                child.getAttributes().put(Globals.CLAY_RESERVED_ATTRIBUTE, displayElement);
            }

            // continue with the addComponent chain
            isFinal = false;
        } else {
            if (log.isDebugEnabled()) {
                log.debug(getMessages().getMessage("create.component.exists",
                    new Object[] { id, displayElement.getJsfid(), new Integer(clayContext.getChildIndex()) }));
            }
        }

        // if target is a Clay component it might contain symbols
        if (child instanceof Clay) {
            // override symbols from nested clay component
            symbolTable.putAll(((Clay) child).getSymbols());
            // capture current symbols for the root of the nested component
            ((Clay) child).getSymbols().putAll(symbolTable);

            // push to context
            clayContext.setSymbols(symbolTable);

            // evaluate nested symbols
            realizeSymbols(clayContext);
        }


        // reassign the child to the converter for the
        // AssignPropertiesCommand
        clayContext.setChild(child);

        return isFinal;
    }

    /**
     * <p>Searches through the parent's children looking for a child
     * component having a matching "org.apache.shale.clay.jspid" attribute.
     * </p>
     * @param parent owning <code>UIComponent</code>
     * @param id target jsfid
     * @return the child <code>UIComponent</code> if a match is found; otherwise;
     *   a <code>null</code> value.
     */
    private UIComponent findComponentByJspId(UIComponent parent, Long id) {
        Iterator ci = parent.getChildren().iterator();
        while (ci.hasNext()) {
           UIComponent child = (UIComponent) ci.next();
           Long jspId = (Long) child.getAttributes().get(Globals.CLAY_JSPID_ATTRIBUTE);
           if (jspId != null && jspId.equals(id)) {
              return child;
           }
        }
        return null;
    }
}
