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

package org.apache.shale.clay.component.chain;

import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.chain.Context;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.Attributes;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.util.Tags;

/**
 * <p>This Command assigns properties to an existing UIViewRoot.
 * It can only update the renderKit and locale properties.
 * There are two new properties on the view root in JSF 1.2,
 * beforePhaseListener and afterPhaseListener. These two require
 * MethodExpression that is introduced in JSP 2.1.  We will have to wait
 * until we can migrate to 1.2 before we can support these attributes.  For some reason,
 * these are not wrappered like the ValueBinding is wrapperd by the ValueExpression.
 * </p>
 */
public class AssignViewRootCommand extends AbstractCommand {

    /**
     * <p>If the target <code>componentType</code> is "javax.faces.ViewRoot",
     * assign the property overrides is present.</p>
     * @param context commons chains context
     * @return <code>true</code> if the current display element is for the view root;
     * Otherwise, return <code>false</code> to create/update a component.
     * @exception Exception any error that might terminate processing
     */
    public boolean execute(Context context) throws Exception {
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

        if (!displayElement.getComponentType().equals("javax.faces.ViewRoot")) {
           return false;
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

        AttributeBean attr = null;
        attr = displayElement.getAttribute("renderKitId");
        if (attr != null && attr.getValue() != null) {
            clayContext.setAttribute(attr);
            String expr = replaceMnemonic(clayContext);
            if (expr != null) {
                if (isValueReference(expr)) {
                    getTagUtils().setValueBinding(facesContext.getViewRoot(), "renderKitId", expr);
                } else {
                    facesContext.getViewRoot().setRenderKitId(expr);
                }
            }
            clayContext.setAttribute(null);
        }
        attr = displayElement.getAttribute("locale");
        if (attr != null && attr.getValue() != null) {
            clayContext.setAttribute(attr);
            String expr = replaceMnemonic(clayContext);
            if (expr != null) {
                if (isValueReference(expr)) {
                    getTagUtils().setValueBinding(facesContext.getViewRoot(), "locale", expr);
                    Object locale = getTagUtils().eval(expr);
                    if(locale != null && locale instanceof Locale) {
                      facesContext.getViewRoot().setLocale((Locale) locale);
                    } else {
                      // expr does not return something useful yet
                      Locale calcLocale = facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
                      facesContext.getViewRoot().setLocale(calcLocale);
                    }
                } else {
                    final int language = 0;
                    final int country = 1;
                    StringTokenizer tokenizer = new StringTokenizer(expr, "-_");
                    String[] tokens = new String[2];
                    int i = 0;
                    while (tokenizer.hasMoreTokens()) {
                        tokens[i++] = tokenizer.nextToken();
                    }
                    Locale locale = null;
                    if (tokens[language] != null && tokens[country] != null) {
                        locale = new Locale(tokens[language], tokens[country]);
                    } else if (tokens[language] != null) {
                        locale = new Locale(tokens[language]);
                    }
                    if (locale != null) {
                        facesContext.getViewRoot().setLocale(locale);
                    }
                }
            }
            clayContext.setAttribute(null);
        }
        // Two new properties on the view root in JSF 1.2 beforePhaseListener and afterPhaseListener.
        // These two require MethodExpression that is introduced in JSP 2.1.  We will have to wait
        // until we can migrate to 1.2 before we can support these attributes.  For some reason,
        // these are not wrappered like the ValueBinding is wrapperd by the ValueExpression.

        // assign any children of the display element to the parent
        // we return "true" indicating the component already exists
        // and the "createComponent" chain will stop and return back to the
        // "addComponent" chain
        clayContext.setChild(clayContext.getParent());

        return true;
    }

}
