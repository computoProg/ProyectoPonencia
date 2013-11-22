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
 * $Id: ClayNamespaceBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.ElementBuilder;

/**
 * <p>This class defines the rules that bind HTML
 * {@link org.apache.shale.clay.parser.Node}'s resembling
 * the {@link org.apache.shale.clay.component.Clay} DTD into
 * corresponding {@link Builder}'s.  The html nodes will be
 * defined in their own namespace.</p>
 */
public class ClayNamespaceBuilderRule implements Command {

    /**
     * <p>A list of {@link Builder}'s used to handle converting
     * the xhtml clay namespace into {@link org.apache.shale.clay.config.beans.ElementBean}'s
     * used by the {@link org.apache.shale.clay.component.Clay} component.</p>
     */
    private static final Builder[] BUILDERS = {new ElementBuilder()};

    /**
     * <p>Maps matching html {@link org.apache.shale.clay.parser.Node}'s to
     * corresponding builders.</p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is done
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        Node node = builderRuleContext.getNode();

        if (node.isWellFormed() && node.getName().equals("element")) {
            builderRuleContext.setBuilder(BUILDERS[0]);
            isFinal = true;
        }

        return isFinal;
    }

}

