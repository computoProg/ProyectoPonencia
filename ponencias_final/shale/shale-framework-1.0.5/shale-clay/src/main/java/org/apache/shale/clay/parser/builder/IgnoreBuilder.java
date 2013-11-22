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
 * $Id: IgnoreBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import java.util.Iterator;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>This {@link Builder} is designed to ignore processing on a
 * block of HTML.  If the jsfid attribute of the HTML node is
 * "ignore", all of the child nodes under the marked nodes are
 * excluded from further processing.  The are treated like a
 * verbatim block.  The outer node is removed from the output.
 * </p>
 *
 */
public class IgnoreBuilder extends CommentBuilder {

    /**
     * <p>Returns the <code>jsfid</code> for the target {@link ElementBean}.</p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "ignore";
    }

    /**
     * <p>Builds a outputText component.  The attribute value is the content of the
     * child nodes.  The root HTML {@link Node} is not included in the content
     * of the <code>target</code> {@link ElementBean} node.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        StringBuffer value = new StringBuffer();
        captureComment(node, value);

        AttributeBean attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
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
     * <p>Recursively traverses the children of the HTML
     * {@link org.apache.shale.clay.parser.Node} concatenating
     * the raw text of each {@link org.apache.shale.clay.parser.Token}
     * into the <code>commentBody</code>.  Ending nodes are not kept
     * with the parser.  If a start node is found that is not self
     * terminated, a matching ending node is created.</p>
     *
     * @param node markup
     * @param commentBody concatenated raw text of all the children
     */
    protected void captureComment(Node node, StringBuffer commentBody) {
        Iterator ni = node.getChildren().iterator();
        while (ni.hasNext()) {
           Node child = (Node) ni.next();
           commentBody.append(child.getToken().getRawText());
           captureComment(child, commentBody);
           if (child.isStart() && !child.isEnd() && child.isWellFormed()) {
              commentBody.append("</").append(child.getName()).append(">");
           }
        }
    }

}
