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
 * $Id: SelectBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.SelectManyMenuBuilder;
import org.apache.shale.clay.parser.builder.SelectOneMenuBuilder;

/**
 * <p>
 * This rule will map a html select {@link Node} to a
 * {@link org.apache.shale.clay.parser.builder.SelectOneMenuBuilder}
 * or a {@link org.apache.shale.clay.parser.builder.SelectManyMenuBuilder}.
 * </p>
 */
public class SelectBuilderRule implements Command {

    /**
     * <p>
     * An array of target {@link org.apache.shale.clay.parser.builder.Builder}s.
     * </p>
     */
    private static final Builder[] BUILDERS = { new SelectOneMenuBuilder(),
            new SelectManyMenuBuilder() };

    /**
     * <p>Returns <code>true</code> if a match was made from a select
     * {@link Node} to a {@link org.apache.shale.clay.parser.builder.SelectOneMenuBuilder}
     * or a {@link org.apache.shale.clay.parser.builder.SelectManyMenuBuilder}.
     * <p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is done
     * @exception Exception pass back up the calling chain
     */
    public boolean execute(Context context) throws Exception {
        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
            && node.getName().equalsIgnoreCase("select")) {

            if (!node.getAttributes().containsKey("multiple")) {
                builderRuleContext.setBuilder(BUILDERS[0]);
                isFinal = true;
            } else {
                builderRuleContext.setBuilder(BUILDERS[1]);
                isFinal = true;
            }

        }

        return isFinal;
    }

}
