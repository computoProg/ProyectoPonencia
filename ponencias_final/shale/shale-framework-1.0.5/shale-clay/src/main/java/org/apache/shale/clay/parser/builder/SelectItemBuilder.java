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
 * $Id: SelectItemBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * This {@link Builder} will create a {@link ElementBean} that will be used to
 * create a single html option element. The {@link OptionBuilderRule} will
 * handle mapping the html element to this builder.
 * </p>
 */
public class SelectItemBuilder extends Builder {

    /**
     * <p>
     * Returns a <code>jsfid</code> that will be set on the
     * target {@link ElementBean}. </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "selectItem";
    }

    /**
     * <p>
     * Returns the JSF component type of <code>javax.faces.SelectItem</code>
     * that will populate a {@link ElementBean} and create an html option.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.SelectItem";
    }

    /**
     * <p>
     * The default for a single option is to look at the next html node to find
     * the label.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target,
            ComponentBean root) {
        super.encodeBegin(node, target, root);

        if (!getBuildNodeBody(node, target)) {
            return;
        }

        if (target.getAttributes().containsKey("itemValue")
            && node.getAttributes().containsKey("value")) {
            String value = (String) node.getAttributes().get("value");
            AttributeBean attr = target.getAttribute("itemValue");
            createAttribute(attr, value, target);
        }
        if (target.getAttributes().containsKey("itemLabel")
            && node.getChildren().size() == 1) {

            Node child = (Node) node.getChildren().get(0);
            String value = child.getToken().getRawText();
            AttributeBean attr = target.getAttribute("itemLabel");
            createAttribute(attr, value, target);
        }

    }

    /**
     * <p>Returns <code>true</code> by default meaning that the
     * parent will render children.</p>
     *
     * @param node markup
     * @param target child config bean
     * @return <code>false</code> if the node's children should be ignored
     */
    protected boolean getBuildNodeBody(Node node, ElementBean target) {
        if (target.getAllowBody() != null) {
           return super.getBuildNodeBody(node, target);
        }

        return true;
    }

}
