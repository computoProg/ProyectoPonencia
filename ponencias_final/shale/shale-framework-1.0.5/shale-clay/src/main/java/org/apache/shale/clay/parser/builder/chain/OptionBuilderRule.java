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
 * $Id: OptionBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.SelectItemBuilder;
import org.apache.shale.clay.parser.builder.SelectItemsBuilder;

/**
 * <p>
 * This <code>Command</code> will return either a {@link SelectItemBuilder} or
 * a {@link SelectItemsBuilder} if the html {@link Node} is an option and the
 * option has children.
 * </p>
 */
public class OptionBuilderRule implements Command {

    /**
     * <p>
     * Array of {@link Builder}s.
     * </p>
     */
    private static final Builder[] BUILDERS = { new SelectItemBuilder(), new SelectItemsBuilder() };

    /**
     * <p>Returns a <code>true</code> value if the current html {@link Node} is
     * an option. A <code>true</code> result means that a rule has been
     * assigned to the {@link BuilderRuleContext}.
     * <p>
     *
     * @param context chains context
     * @return <code>true</code> if the chain is done
     * @exception Exception up the calling chain
     */
    public boolean execute(Context context) throws Exception {
        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
            && node.getName().equalsIgnoreCase("option")) {

            if (node.getChildren().size() > 0) {
                builderRuleContext.setBuilder(BUILDERS[0]);
                isFinal = true;
            } else {
                // no value attribute, must be bound to a component
                builderRuleContext.setBuilder(BUILDERS[1]);
                isFinal = true;
            }

        }

        return isFinal;
    }

}
