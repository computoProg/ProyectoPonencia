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
 * $Id: VerbatimBuilder.java 465411 2006-10-18 23:06:50Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;


/**
 * <p>
 * This is the default {@link Builder} that will create a {@link ElementBean}
 * having characteristics like the JSP verbatim tag. All html elements that are
 * not mapped to a specific {@link Builder} will be handled by the
 * {@link org.apache.shale.clay.parser.builder.chain.DefaultBuilderRule}.
 * </p>
 */
public class VerbatimBuilder extends Builder {

    /**
     * <p>
     * Returns the <code>jsfid</code> for the target {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "f:verbatim";
    }

    /**
     * <p>
     * Calls to the super implementation to populate the target
     * {@link ElementBean} and then sets the value attribute to the raw text of
     * the html node. Set the <code>escape</code> attribute to
     * <code>false</code> so that the special html characters will not be
     * excaped
     * </p>
     *
     * @param node markup
     * @param target child
     * @param root parent
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        String value = node.getToken().getRawText();

        AttributeBean attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_VALUE);
        attr.setName("value");
        attr.setValue(value.toString());
        target.addAttribute(attr);

        attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setName("escape");
        attr.setValue(Boolean.FALSE.toString());
        target.addAttribute(attr);

        attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setName("isTransient");
        attr.setValue(Boolean.TRUE.toString());
        target.addAttribute(attr);

    }

    /**
     * <p>
     * Returns a JSF component type of <code>javax.faces.HtmlOutputText</code>.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlOutputText";
    }

    /**
     * <p>
     * If the html node is well-formed, the create a ending html tag using
     * another verbatim {@link org.apache.shale.clay.config.beans.ElementBean}.
     * Set the <code>escape</code> attribute to <code>false</code> so that
     * the special html characters will not be escaped
     * </p>
     *
     * @param node markup
     * @param target child
     * @param root parent
     */
    protected void encodeEnd(Node node, ElementBean target, ComponentBean root) {

        // verbatim ending tags must be another node since they are flat and
        // don't have children
        if (node.isStart() && !node.isEnd() && node.isWellFormed()) {

            ElementBean endTarget = createElement(node);
            root.addChild(endTarget);

            StringBuffer tmp = new StringBuffer();
            tmp.append("</").append(node.getName()).append(">");

            AttributeBean attr = new AttributeBean();
            attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
            attr.setName("value");
            attr.setValue(tmp.toString());
            endTarget.addAttribute(attr);

            attr = new AttributeBean();
            attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
            attr.setName("escape");
            attr.setValue(Boolean.FALSE.toString());
            endTarget.addAttribute(attr);

            attr = new AttributeBean();
            attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
            attr.setName("isTransient");
            attr.setValue(Boolean.TRUE.toString());
            endTarget.addAttribute(attr);

        }
    }

    /**
     * <p>Skip the processing of attributes for a verbatim node.  This
     * was a bug uncovered with JSF RI 1.2.  The <code>TextRenderer</code>
     * is sensitive to pass thru attributes.  If it finds a pass thru
     * attribute, it wraps the text in a HTML span tag.</p>
     *
     * @param node markup node
     * @param target config bean built for the markup node
     */
    protected void assignAttributes(Node node, ComponentBean target) {
       //NA for a verbatim
    }
}
