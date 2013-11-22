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
 * $Id: SpanBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.MorphBuilder;

/**
 * <p>
 * This <code>Command</code> rule matches a html span {@link Node} to a
 * {@link MorphBuilder}. This rule is different in that <code>jsfid</code> is
 * used to find the target componentType to replace the html span tag. If a
 * <code>jsfid</code> is not found as an attribute in the html {@link Node},
 * no match is made.
 * <p>
 */
public class SpanBuilderRule implements Command {

    /**
     * <p>
     * Instance of the {@link MorphBuilder} that can become any type of
     * component.
     * </p>
     */
    private static final Builder BUILDER = new MorphBuilder();

    /**
     * <p>
     * Returns a <code>true</code> value, ending the chain execution if the
     * html {@link Node} has a <code>jsfid</code> html attribute and is a span
     * HTML element.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if final
     * @exception Exception propagated to the top chain
     */
    public boolean execute(Context context) throws Exception {
        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
            && node.getName().equals("span")
            && node.getAttributes().containsKey("jsfid")) {

            builderRuleContext.setBuilder(BUILDER);
            isFinal = true;
        }

        return isFinal;
    }

}
