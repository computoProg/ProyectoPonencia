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
 * $Id: SelectItemsBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * This is a {@link Builder} that will create a {@link ElementBean} for a JSF
 * <code>javax.faces.SelectItems</code> component. This builder is mapped to a
 * {@link Node} by the {@link OptionBuilderRule}.
 * </p>
 */
public class SelectItemsBuilder extends Builder {

    /**
     * <p>
     * Returns a <code>jsfid</code> used to populate the target
     * {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "selectItems";
    }

    /**
     * <p>
     * Returns the JSF component type <code>javax.faces.SelectItems</code>
     * that will be set to the target {@link ElementBean} and used to create
     * options for a collection.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.SelectItems";
    }


    /**
     * <p>Returns <code>true</code> by default meaning that the
     * parent will render children.</p>
     *
     * @param node markup
     * @param target child bean
     * @return <code>false</code> if the node body should be ignored
     */
    protected boolean getBuildNodeBody(Node node, ElementBean target) {
        if (target.getAllowBody() != null) {
           return super.getBuildNodeBody(node, target);
        }

        return true;
    }


}
