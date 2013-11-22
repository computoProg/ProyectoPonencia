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
 * $Id: DirectiveBuilderRule.java 473459 2006-11-10 20:30:12Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder.chain;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.IgnoreBuilder;
import org.apache.shale.clay.parser.builder.JspIncludeDirectiveBuilder;
import org.apache.shale.clay.parser.builder.VoidBuilder;

/**
 * <p>This builder will be ordered first in the rule registration list.
 * If the node has a <code>jsfid</code> attribute, and its value is
 * "ignore", or "void" then special assumptions will be made.
 * Beside the clay "ignore" and "void" directives, handles
 * all "jsp:" nodes.  Most are are assigned to the clay "void"
 * directive except the "jsp:include" and "jsp:directive.include"
 * elements.  The include and directive.include are
 * converted into a nested clay components.
 */
public class DirectiveBuilderRule implements Command {

    /**
     * <p>The builders that will assined to a "ignore" or "void"
     * {@link Builder}.
     * <p>
     */
    private static final Builder[] BUILDERS = {new IgnoreBuilder(),
              new VoidBuilder(), new JspIncludeDirectiveBuilder()};

    /**
     * <p>If the node has a <code>jsfid</code> attribute, and its value is
     * "ignore", child elements will be rendered as comment/verbatim
     * content.  The enclosing tag will not be rendered in the document,
     * only it's children. If the <code>jsfid</code> is "void",
     * the element will not be rendered but its children will
     * keep their original characteristics.<br/><br/>
     * All "jsp:" nodes are assigned to the clay void directive except the
     * "jsp:include" and "jsp:directive.include".  These are converted into a
     * nested clay component.  Nodes with a jsp prefix must be in the
     * "http://java.sun.com/JSP/Page" namespace to be eligible for this
     * processing.
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
        if (!node.isComment() && node.isStart() && node.getName() != null) {
            String jsfid = (String) node.getAttributes().get("jsfid");
            if (jsfid != null) {
               if (jsfid.equals("ignore")) {
                   builderRuleContext.setBuilder(BUILDERS[0]);
                   isFinal = true;
               } else if (jsfid.equals("void")) {
                   builderRuleContext.setBuilder(BUILDERS[1]);
                   isFinal = true;
               }
            } else if (node.getQname() != null) {
                String uri = node.getNamespaceURI(node.getQname());
                if (uri != null && uri.equals("http://java.sun.com/JSP/Page")) {
                    if (node.getName().equals("directive.include")
                        || node.getName().equals("include")) {
                        builderRuleContext.setBuilder(BUILDERS[2]);
                        isFinal = true;
                    } else {
                        builderRuleContext.setBuilder(BUILDERS[1]);
                        isFinal = true;
                    }
                }
            }
        }

        return isFinal;
    }

}
