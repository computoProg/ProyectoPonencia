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
 * $Id: DefaultBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.CommentBuilder;
import org.apache.shale.clay.parser.builder.MorphBuilder;
import org.apache.shale.clay.parser.builder.VerbatimBuilder;

/**
 * <p>
 * This is a <code>Command</code> implementation that will
 * match a HTML {@link Node} with a matching {@link Builder}
 * implementation. This is the default rule and will always return a
 * {@link OutputLinkBuilder} and the chain terminated.  The {@link BuilderFactory} runs this
 * command rule as part of the <code>Globals.BUILDER_CATALOG_NAME</code>
 * found in the <code>Globals.BUILDER_RESOURCE_NAME</code> and
 * invoking the <code>Globals.FIND_BUILDER_COMMAND_NAME</code>
 * </p>
 */
public class DefaultBuilderRule implements Command {

    /**
     * <p>
     * Default static instance of {@link VerbatimBuilder}.
     * <p>
     */
    private static final Builder[] BUILDERS = {new CommentBuilder(), new MorphBuilder(), new VerbatimBuilder()};

    /**
     * <p>
     * Uses the {@link BuilderRuleContext} to find the current html
     * {@link org.apache.shale.clay.parser.Node}. This is the default rule that
     * will return a {@link VerbatimBuilder} when the node is not a comment or
     * has a <code>jsfid</code> attribute. When the html node is a
     * comment, the {@link CommentBuilder} is returned.  If the node is not a comment
     * but has a <code>jsfid</code> attribute, the {@link MorphBuilder} is returned.
     * </p>
     *
     * @param context commons chians
     * @return <code>true</code> if final
     * @exception Exception propagated to the top chain
     */
    public boolean execute(Context context) throws Exception {


        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (node.isComment() || node.isCdata()) {
            builderRuleContext.setBuilder(BUILDERS[0]);
        } else  if (node.getName() != null && node.getAttributes().containsKey("jsfid")) {
            builderRuleContext.setBuilder(BUILDERS[1]);
        } else {
            builderRuleContext.setBuilder(BUILDERS[2]);
        }

        return true;
    }

}
