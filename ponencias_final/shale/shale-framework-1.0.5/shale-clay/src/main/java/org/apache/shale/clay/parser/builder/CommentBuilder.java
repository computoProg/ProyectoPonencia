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
 * $Id: CommentBuilder.java 480266 2006-11-28 23:04:10Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import java.util.Iterator;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>This {@link Builder} will render a HTML {@link Node} as
 * an HTML comment.  All nodes under the comment will be concatenated
 * as their original raw text value within the HTML document.
 * </p>
 *
 */
public class CommentBuilder extends VerbatimBuilder {

    /**
     * <p>This method is overridden to return a <code>true</code> value indicating
     * that this {@link Builder} will handle child nodes under the
     * associated HTML {@link Node}.  This method can easily be confused
     * with the <code>isChildrenAllowed()</code> method of the super
     * class {@link Builder}.  The distinction is that the
     * <code>isChildrenAllowed()</code> method signifies that the
     * associated JSF component supports children components. This method
     * signifies that this builder will handle building child nodes
     * similarly to the jsf component's <code>getRendersChildren()</code> method.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @return <code>true</code>
     */
    protected boolean getBuildNodeBody(Node node, ElementBean target) {
        return true;
    }

    /**
     * <p>The super implementation is invoked to build a target
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * The body of the comment is constructed by capturing the text
     * of all child HTML nodes within the comment body.</p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        super.encodeBegin(node, target, root);

        AttributeBean valueAttr = target.getAttribute("value");
        StringBuffer comment = new StringBuffer(valueAttr.getValue());
        captureComment(node, comment);
        valueAttr.setValue(comment.toString());
        valueAttr.setBindingType(AttributeBean.BINDING_TYPE_NONE);

    }

    /**
     * <p>Recursively traverses the children of the HTML
     * {@link org.apache.shale.clay.parser.Node} concatenating
     * the raw text of each {@link org.apache.shale.clay.parser.Token}
     * into the <code>commentBody</code>.</p>
     *
     * @param node markup
     * @param commentBody concatenated child node's raw text
     */
    protected void captureComment(Node node, StringBuffer commentBody) {
        Iterator ni = node.getChildren().iterator();
        while (ni.hasNext()) {
           Node child = (Node) ni.next();
           commentBody.append(child.getToken().getRawText());
           captureComment(child, commentBody);
        }
    }


    /**
     * <p>This override cancels the super implementation.  The overridden
     * method handles the ending comment tag, "--&gt;".</p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeEnd(Node node, ElementBean target, ComponentBean root) {

    }


}
