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
 * $Id: SelectOneMenuBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * This {@link Builder} will create a target
 * {@link org.apache.shale.clay.config.beans.ElementBean} for a single
 * select html {@link org.apache.shale.clay.parser.Node}. The mapping between the
 * html and the builder is handled by the
 * {@link org.apache.shale.clay.parser.builder.chain.SelectBuilderRule}.
 * </p>
 */
public class SelectOneMenuBuilder extends Builder {

    /**
     * <p>
     * Returns a JSF component type of
     * <code>javax.faces.HtmlSelectOneMenu</code> that will populate the
     * target {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup node
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlSelectOneMenu";
    }

    /**
     * <p>
     * Returns the <code>jsfid</code> that will populate the target
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup node
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "selectOneMenu";
    }

    /**
     * <p>
     * Returns a <code>true</code> value indicating that the target JSF
     * component can have children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }


    /**
     * <p>Remove any child nodes that are not "option" nodes.</p>
     *
     * @param node markup
     * @param target child node
     * @param root child's parent
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {

        //remove any children that are not option nodes
        ArrayList delList = new ArrayList();
        Iterator ci = node.getChildren().iterator();
        while (ci.hasNext()) {
           Node child = (Node) ci.next();
           if (child.getName() == null || !child.getName().equalsIgnoreCase("option")) {
              delList.add(child);
           }
        }
        for (int i = 0; i < delList.size(); i++) {
           node.getChildren().remove(delList.get(i));
        }
        delList.clear();

        super.encodeBegin(node, target, root);
    }

}

