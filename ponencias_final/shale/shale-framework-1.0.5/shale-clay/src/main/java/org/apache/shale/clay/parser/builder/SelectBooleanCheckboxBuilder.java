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
 * $Id: SelectBooleanCheckboxBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.parser.Node;


/**
 * This {@link Builder} will create a {@link ElementBean} representing a
 * <code>javax.faces.HtmlSelectBooleanCheckbox</code> JSF component. The
 * mapping of the {@link Node} representing a html input element having a
 * "checkbox" type attribute is handled by the {@link InputBuilderRule}.
 *
 */
public class SelectBooleanCheckboxBuilder extends Builder {

    /**
     * <p>
     * Returns a <code>jsfid</code> for the {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "selectBooleanCheckbox";
    }

    /**
     * <p>
     * Returns the JSF component type of
     * <code>javax.faces.HtmlSelectBooleanCheckbox</code> that will create a
     * checkbox component.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlSelectBooleanCheckbox";
    }

}
