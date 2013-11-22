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
 * $Id: VerbatimBuilder.java 359115 2005-12-26 22:44:06Z wsmoak $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.parser.Node;


/**
 * <p>
 * This is a {@link Builder} that will create a void {@link ElementBean}.
 * The element bean will be an empty placeholder.  The HTML element will
 * contain nothing.</p>
 */
public class VoidBuilder extends Builder {

    /**
     * <p>
     * Returns the <code>jsfid</code> for the target {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "void";
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


}
