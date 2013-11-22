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
 * $Id: InputTextBuilder.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>
 * Builds a {@link ElementBean} from a HTML {@link Node} where the
 * {@link org.apache.shale.clay.parser.builder.chain.TextareaBuilderRule}
 * handles the mapping.
 * </p>
 */
public class InputTextBuilder extends Builder {

    /**
     * <p>
     * The default for this builder is that builder will handle the children
     * html nodes meaning that the default should be <code>true</code>. The
     * default can be overridden by the "allowBody" attribute in the component
     * metadata.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeEnd(Node node, ElementBean target,
            ComponentBean root) {
    }

    /**
     * <p>
     * Returns the <code>jsfid</code> used to populate the {@link ElementBean}.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        return "inputText";
    }

    /**
     * <p>
     * Returns the JSF componentType of
     * <code>javax.faces.HtmlInputTextarea</code> used to define the
     * {@link ElementBean} from the HTML {@link Node}.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        return "javax.faces.HtmlInputText";
    }

}
