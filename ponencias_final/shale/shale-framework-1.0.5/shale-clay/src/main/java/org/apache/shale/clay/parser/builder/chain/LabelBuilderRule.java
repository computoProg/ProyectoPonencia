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
 * $Id: LabelBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.OutputLabelBuilder;

/**
 * <p>
 * This <code>Command</code> rule has an implied mapping of a html label
 * {@link Node} to a {@link OutputLabelBuilder} regardless of the mapped
 * <code>jsfid</code.
 * </p>
 */
public class LabelBuilderRule implements Command {

    /**
     * <p>
     * Instance of the target {@link OutputLabelBuilder}.
     * </p>
     */
    private static final Builder BUILDER = new OutputLabelBuilder();

    /**
     * <p>
     * If the html {@link Node} is a label, return return the
     * {@link OutputLabelBuilder} and complete the chain by returning a
     * <code>true</code> value. The {@link BuilderRuleContext} contains the
     * current {@link Node} and the target {@link Builder}.
     * </p>
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
            && node.getName().equalsIgnoreCase("label")) {

            builderRuleContext.setBuilder(BUILDER);
            isFinal = true;
        }

        return isFinal;
    }

}
