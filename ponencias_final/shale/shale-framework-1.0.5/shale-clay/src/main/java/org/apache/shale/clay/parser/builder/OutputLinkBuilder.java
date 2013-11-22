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
 * $Id: OutputLinkBuilder.java 519627 2007-03-18 15:23:04Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * This {@link Builder} will create a target
 * {@link org.apache.shale.clay.config.beans.ElementBean} for a "&lt;a&gt;"
 * {@link Node}. The mapping between the html and the builder is handled by the
 * {@link AnchorBuilderRule}.
 * </p>
 */
public class OutputLinkBuilder extends Builder {

    /**
     * <p>
     * Returns a JSF component type of <code>javax.faces.HtmlOutputLink</code>.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlOutputLink";
    }

    /**
     * <p>
     * Returns a <code>jsfid</code> that will populate the target
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "outputLink";
    }

    /**
     * <p>
     * Returns <code>true</code> meaning that the target JSF component can
     * have children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }

    /**
     * <p>Calls super to populate the <code>target</code> config bean with the
     * html <code>node</code>'s values.  The "href" attribute doesn't have a
     * corresponding outputLink value so it will become a symbol.  If there
     * is a "value" attribute, connect the component's "value" to the
     * "@href" symbol.</p>
     *
     * @param node markup node
     * @param target config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        super.encodeBegin(node, target, root);
        if (getComponentType(node).equals(target.getComponentType())) {
            AttributeBean attr = target.getAttribute("value");
            SymbolBean symbol = target.getSymbol("href");
            if ((symbol != null && attr != null)
                    && (attr.getValue() == null || attr.getValue().length() == 0)) {
                createAttribute(attr, "@href", target);
            }
        }
    }

}
