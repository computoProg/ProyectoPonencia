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
 * $Id: AssignChildrenCommand.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.util.Iterator;

import javax.faces.component.UIComponent;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * Iterates over the child {@link org.apache.shale.clay.config.beans.ElementBean} collection of a
 * {@link ComponentBean}, and invokes the {@link CreateComponentCommand} for
 * each.
 * <p>
 */
public class AssignChildrenCommand extends AbstractCommand {

    /**
     * <p>
     * This {@link Command} iterates over the {@link ComponentBean}
     * <code>children</code> collection and invokes the
     * <code>Globals.ADD_COMPONENT_COMMAND_NAME</code> for each
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is complete and should stop
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.clayContext"));
        }

        UIComponent parent = (UIComponent) clayContext.getChild();
        if (parent == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.childComponent"));
        }

        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.componentBean"));
        }

        Iterator vi = displayElement.getChildrenIterator();

        int childIndex = 0;
        while (vi.hasNext()) {
            ComponentBean childDisplayElement = (ComponentBean) vi.next();

            ClayContext subContext = (ClayContext) clayContext.clone();
            subContext.setDisplayElement(childDisplayElement);
            subContext.setParent(parent);
            subContext.setChild(null);
            subContext.setChildIndex(childIndex);
            subContext.setJspIds(clayContext.getJspIds());

            Catalog catalog = getCatalog();
            Command command = catalog
                    .getCommand(Globals.ADD_COMPONENT_COMMAND_NAME);
            command.execute(subContext);

            UIComponent child = (UIComponent) subContext.getChild();

            // Increment the index if the new component is not a facet
            if (parent.getChildren().contains(child)) {
                ++childIndex;
            }
        }
        
        //clay manages its own subtree; components that
        //renders children still do not build their own
        //composition.  clay does both, build its own subtree
        //and renders its own children.
        
        if (!(parent instanceof Clay)) {
            // remove any components not represented by the metadata graph
            for (int i = parent.getChildCount() - 1; i > -1; i--) {
                UIComponent child = (UIComponent) parent.getChildren().get(i);
                Long jspId = (Long) child.getAttributes().get(
                        Globals.CLAY_JSPID_ATTRIBUTE);
                if (jspId != null
                        && !clayContext.getJspIds().contains(jspId)) {
                    parent.getChildren().remove(i);
                    child.setParent(null);
                }
            }
        }
       
        
        return isFinal;
    }

}

