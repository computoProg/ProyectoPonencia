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
 * $Id: AssignActionListenersCommand.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.component.chain;

import java.util.Iterator;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.ActionListenerBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * Iterates over the action listeners defined in the {@link ComponentBean}
 * invoking {@link CreateActionListenerCommand}.
 * </p>
 */
public class AssignActionListenersCommand extends AbstractCommand {

    /**
     * <p>
     * Common logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(AssignActionListenersCommand.class);
    }

    /**
     * <p>
     * This {@link Command} will use the {@link ClayContext} to invoke the
     * <code>Globals.ADD_ACTION_LISTENER_COMMAND_NAME</code>.</p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is done
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.clayContext"));
        }

        UIComponent child = (UIComponent) clayContext.getChild();
        if (child == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.childComponent"));
        }

        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.componentBean"));
        }

        if (displayElement.getActionListeners().size() > 0) {
            if (child instanceof ActionSource) {

                Iterator vi = displayElement.getActionListenerIterator();
                while (vi.hasNext()) {
                    ActionListenerBean actionListener = (ActionListenerBean) vi
                            .next();

                    ClayContext subContext = (ClayContext) clayContext.clone();
                    subContext.setDisplayElement(actionListener);
                    subContext.setParent(child);

                    Catalog catalog = getCatalog();
                    Command command = catalog
                            .getCommand(Globals.ADD_ACTION_LISTENER_COMMAND_NAME);
                    command.execute(subContext);

                }
            } else {
                log.error(getMessages().getMessage("assign.action.listener.error",
                        new Object[] { displayElement }));
            }
        }

        return isFinal;
    }

}

