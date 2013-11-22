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
 * $Id: FormBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * Builds a form {@link org.apache.shale.clay.config.beans.ElementBean} from a
 * HTML {@link Node}.
 * </p>
 */
public class FormBuilder extends Builder {

    /**
     * <p>
     * Returns the <code>jsfid</code> for the
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "form";
    }

    /**
     * <p>
     * Returns the JSF componentType of <code>javax.faces.HtmlForm</code> to
     * populate the {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlForm";
    }

    /**
     * <p>
     * This override returns <code>true</code> indicating that the from JSF
     * component can have children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }

}
