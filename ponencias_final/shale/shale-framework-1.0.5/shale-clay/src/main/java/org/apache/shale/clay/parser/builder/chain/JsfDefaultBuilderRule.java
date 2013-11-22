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
 * $Id: JsfDefaultBuilderRule.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.builder.JsfDefaultBuilder;


/**
 * <p>Delegates all handling to a common
 * {@link org.apache.shale.clay.parser.builder.JsfDefaultBuilder} that can
 * transform markup that looks like JSP tags to a graph of
 * {@link org.apache.shale.clay.config.beans.InnerComponentBean} beans.</p>
 *
 */
public class JsfDefaultBuilderRule implements Command {

    /**
     * <p>Generic {@link Builder} that handles JSP style of markup's.</p>
     */
    private final JsfDefaultBuilder builder = new JsfDefaultBuilder();

    /**
     * <p>Assigns handling of the markup to {@link org.apache.shale.clay.parser.builder.JsfDefaultBuilder}
     * without condition.</p>
     *
     * @param context common chains
     * @return <code>true</code> if the chain is done
     */
    public boolean execute(Context context) {

        BuilderRuleContext builderRuleContext = (BuilderRuleContext) context;
        builderRuleContext.setBuilder(builder);

        return true;
    }

    /**
     * <p>Returns the namespace prefix that will be added to the
     * node name when resolving the clay config.</p>
     *
     * @return URI prefix
     */
    public String getPrefix() {
        return builder.getPrefix();
    }

    /**
     * <p>Sets the namespace preix that will override the template
     * nodeds qname.</p>
     *
     * @param prefix URI prefix
     */
    public void setPrefix(String prefix) {
        builder.setPrefix(prefix);
    }

}

