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
 * $Id: AnchorBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

/**
 * <p>
 * This is a <code>Command</code> implementation that will
 * match a HTML {@link Node} with a matching {@link Builder}
 * implementation.  If the html node is an anchored tag, a
 * {@link OutputLinkBuilder} will be returned and the
 * chain terminated.  The {@link BuilderFactory} runs this
 * command rule as part of the <code>Globals.BUILDER_CATALOG_NAME</code>
 * found in the <code>Globals.BUILDER_RESOURCE_NAME</code> and
 * invoking the <code>Globals.FIND_BUILDER_COMMAND_NAME</code>
 * </p>
 */
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.OutputLinkBuilder;

/**
 *
 *
 */
public class AnchorBuilderRule implements Command {

    /**
     * <p>
     * Static instance of the builder.
     * </p>
     */
    private static final Builder BUILDER = new OutputLinkBuilder();

    /**
     * <p>
     * Uses the {@link BuilderRuleContext} to find the current html {@link Node}.
     * If the node is an anchored element, return an instance if the builder and
     * stop the chain.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if is final
     * @exception Exception throws back to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
            && node.getName().equalsIgnoreCase("a")) {
            builderRuleContext.setBuilder(BUILDER);
            isFinal = true;

        }

        return isFinal;
    }

}
