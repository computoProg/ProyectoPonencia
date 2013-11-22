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
 * $Id: MorphBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * This Builder is mapped to the HTML span tag and will morph into any clay
 * component defined by the <code>jsfid</code> attribute in the HTML element.
 * For most of the builder there is an assumed mapping between the source and
 * target but the HTML span can be mapped to any clay meta component graph. The
 * {@link org.apache.shale.clay.parser.builder.chain.SpanBuilderRule} handle
 * the association of the HTML node with this class.
 * </p>
 */
public class MorphBuilder extends Builder {

    /**
     * <p>
     * Returns the <code>jsfid</code> the
     * {@link org.apache.shale.clay.config.beans.ElementBean} will take.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "morph";
    }

    /**
     * <p>
     * Returns a bogus componentType that is overridden by the mapped component
     * using the <code>jsfid</code> as an HTML attribute.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.Morph";
    }

    /**
     * <p>
     * Returns <code>true</code> indicating that the JSF component mapped to
     * this html span tag can have children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }

}
