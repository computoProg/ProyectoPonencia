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
 * $Id: CommandButtonBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.parser.Node;

/**
 * Builds a command button {@link ElementBean} object from a {@link Node}. The
 * mapping of this {@link Builder} to an HTML element is handled by the
 * {@link InputBuilderRule}.
 */
public class CommandButtonBuilder extends Builder {

    /**
     * <p>
     * Returns the <code>jsfid</code> assigned to the {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "commandButton";
    }

    /**
     * <p>
     * The JSF componentType that is registered in the faces configuration giving a
     * logical name to a JSF <code>javax.faces.HtmlCommandButton</code>
     * component.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlCommandButton";
    }

}
