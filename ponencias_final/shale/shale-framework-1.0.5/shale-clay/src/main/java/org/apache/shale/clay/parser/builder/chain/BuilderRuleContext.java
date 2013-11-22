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
 * $Id: BuilderRuleContext.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.impl.ContextBase;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;

/**
 * <p>
 * This context is used by the {@link org.apache.shale.clay.parser.builder.BuilderFactory}
 * to invoke the <code>Globals.FIND_BUILDER_COMMAND_NAME</code> chain passing this context.
 * When the chain has stopped, the context will contain the
 * {@link org.apache.shale.clay.parser.builder.Builder}
 * needed to convert the html {@link org.apache.shale.clay.parser.Node}
 * into a {@link org.apache.shale.clay.config.beans.ElementBean}.
 */
public class BuilderRuleContext extends ContextBase {

    /**
     * <p>Unique serial id.</p>
     */
    private static final long serialVersionUID = 4123103940092377137L;

    /**
     * <p>
     * The current html node.
     * </p>
     */
    private Node node = null;

    /**
     * <p>
     * Returns the current html {@link org.apache.shale.clay.parser.Node}.
     * </p>
     *
     * @return current node
     */
    public Node getNode() {
        return node;
    }

    /**
     * <p>
     * Sets the current html {@link org.apache.shale.clay.parser.Node}.
     * </p>
     *
     * @param node current html node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * <p>
     * The target builder that the
     * <code>Globals.FIND_BUILDER_COMMAND_NAME</code> chain will set.
     */
    private Builder builder = null;

    /**
     * <p>
     * Returns the target {@link org.apache.shale.clay.parser.builder.Builder}.
     * </p>
     *
     * @return assigned builder
     */
    public Builder getBuilder() {
        return builder;
    }

    /**
     * <p>
     * Sets the target {@link org.apache.shale.clay.parser.builder.Builder}.
     * </p>
     *
     * @param builder assigned to the node
     */
    public void setBuilder(Builder builder) {
        this.builder = builder;
    }
}
