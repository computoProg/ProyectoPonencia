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
 * $Id: InputBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.CommandButtonBuilder;
import org.apache.shale.clay.parser.builder.InputTextBuilder;
import org.apache.shale.clay.parser.builder.SelectBooleanCheckboxBuilder;
import org.apache.shale.clay.parser.builder.SelectOneRadioBuilder;

/**
 * <p>
 * This rule <code>Command</code> has an implied mapping to several html
 * element types. The associated {@link Builder} will be returned regardless of
 * the <code>jsfid</code>.
 * <p>
 */
public class InputBuilderRule implements Command {

    /**
     * <p>Array of target input {@link Builder}s.</p>
     */
    private static final Builder[] BUILDERS = { new InputTextBuilder(),
            new SelectBooleanCheckboxBuilder(), new SelectOneRadioBuilder(),
            new CommandButtonBuilder() };

    /**
     * <p>
     * If the html {@link Node} is a input element, return the correct builder.
     * If a match is found, the chain is ended.<br>
     * <dl>
     * <dt>Input type attribute to {@link Builder}
     * <dd><b>text</b> {@link org.apache.shale.clay.parser.builder.InputTextBuilder}
     * <dd><b>checkbox</b> {@link org.apache.shale.clay.parser.builder.SelectBooleanCheckboxBuilder}
     * <dd><b>raido</b> {@link org.apache.shale.clay.parser.builder.SelectOneRadioBuilder}
     * <dd><b>submit</b>{@link org.apache.shale.clay.parser.builder.CommandButtonBuilder}
     * </dl>
     * <br>
     * The {@link BuilderRuleContext} will hold the current html {@link Node} and
     * target {@link Builder}.
     * </p>
     *
     * @param context common chains context
     * @return <code>true</code> if the chain is done
     * @throws Exception checked exception
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();
        if (!node.isComment() && node.getName() != null
             && node.getName().equalsIgnoreCase("input")) {

            String type = (String) node.getAttributes().get("type");
            if (type != null && type.equalsIgnoreCase("text")) {
                builderRuleContext.setBuilder(BUILDERS[0]);
                isFinal = true;
            } else if (type != null && type.equalsIgnoreCase("checkbox")) {
                builderRuleContext.setBuilder(BUILDERS[1]);
                isFinal = true;
            } else if (type != null && type.equalsIgnoreCase("radio")) {
                builderRuleContext.setBuilder(BUILDERS[2]);
                isFinal = true;
            } else if (type != null && type.equalsIgnoreCase("submit")) {
                builderRuleContext.setBuilder(BUILDERS[BUILDERS.length - 1]);
                isFinal = true;
            }

        }

        return isFinal;
    }

}

