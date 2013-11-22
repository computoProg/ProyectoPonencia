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
 * $Id: JspIncludeDirectiveBuilder.java 473459 2006-11-10 20:30:12Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import java.util.Iterator;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.clay.parser.Node;


/**
 * <p>Simulates a JSP include directive.include using the
 * {@link org.apache.shale.clay.component.Clay} component.
 * For a "jsp:include" node, the child "param" nodes
 * will be converted into symbols.</p>
 */
public class JspIncludeDirectiveBuilder extends Builder {



    /**
     * <p>
     * Returns the <code>jsfid</code> associated with the {@link ElementBean}
     * being build.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "clay";
    }

    /**
     * <p>
     * Returns the JSF component type of
     * <code>org.apache.shale.clay.component.Clay</code> that will populate the
     * componentType property of the {@link ElementBean} being created.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "org.apache.shale.clay.component.Clay";
    }

    /**
     * <p>
     * Returns a boolean value that will indicate if the target JSF component
     * will support children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }

    /**
     * <p>Calls super to populate the <code>target</code> config bean with the
     * html <code>node</code>'s values.
     * When processing a "jsp:directive.include", the "file" attribute doesn't have a
     * corresponding clay value so it will become a symbol aliased to
     * clay's "clayJsfid" attribute.  The same goes for the "jsp:include".  The
     * "page" symbol is aliased to the "clayJsfid" attribute.  Nested "param"
     * nodes are converted into symbols.</p>
     *
     * @param node markup node
     * @param target config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        super.encodeBegin(node, target, root);

        if (node.getName().equals("directive.include")) {
            AttributeBean attr = target.getAttribute("clayJsfid");
            SymbolBean symbol = target.getSymbol("file");
            if (symbol != null && attr != null) {
                createAttribute(attr, "@file", target);
            }
        } else if (node.getName().equals("include")) {
            AttributeBean attr = target.getAttribute("clayJsfid");
            SymbolBean symbol = target.getSymbol("@page");
            if (symbol != null && attr != null) {
                createAttribute(attr, "@page", target);
            }
            Iterator ai = node.getChildren().iterator();
            while (ai.hasNext()) {
                Node child = (Node) ai.next();
                if (child.getName() != null && child.getName().equals("param")) {
                    String name = (String) child.getAttributes().get("name");
                    String value = (String) child.getAttributes().get("value");

                    SymbolBean paramSymbol = new SymbolBean();
                    paramSymbol.setName(name);
                    paramSymbol.setValue(value);
                    target.addSymbol(paramSymbol);
                }
            }
            // remove the children, don't waste any more time processing
            node.getChildren().clear();
        }
    }

    /**
     * <p>This builder handles converting the <code>nodes</code>'s children.</p>
     * @param node markup node
     * @param target config bean
     * @return <code>true</code> indicating that children of the <code>node</code>
     * should be ignored.
     */
    protected boolean getBuildNodeBody(Node node, ElementBean target) {
        return true;
    }

}
