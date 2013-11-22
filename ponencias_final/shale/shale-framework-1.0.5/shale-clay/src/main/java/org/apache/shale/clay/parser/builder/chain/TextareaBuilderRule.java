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
 * $Id: TextareaBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.InputTextareaBuilder;

/**
 * <p>
 * This <code>Command</code> rule will map an html textarea {@link Node} to a
 * {@link InputTextareaBuilder}.
 * </p>
 */
public class TextareaBuilderRule implements Command {

    /**
     * <p>
     * Instance of the target
     * {@link org.apache.shale.clay.parser.builder.InputTextareaBuilder}.
     * </p>
     */
    private static final Builder BUILDER = new InputTextareaBuilder();

    /**
     * <p>
     * Returns <code>false</code> if the html {@link Node} was not a textarea.
     * The {@link BuilderRuleContext} will be passed as a parameter. If a match
     * is found, a <code>true</code> value is returned completing the chain.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if the command ends the chain
     * @exception Exception pushes an exception back to the invoking Command
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
            && node.getName().equalsIgnoreCase("textarea")) {

            builderRuleContext.setBuilder(BUILDER);
            isFinal = true;

        }

        return isFinal;
    }

}
