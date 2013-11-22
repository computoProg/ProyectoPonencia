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
 * $Id: AssignPropertiesCommand.java 473102 2006-11-09 22:26:21Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.util.Iterator;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>
 * Sets the <code>UIComponent</code> properties using the attributes collection of
 * {@link org.apache.shale.clay.config.beans.AttributeBean} by invoking the
 * <code>Globals.SET_ATTRIBUTE_COMMAND_NAME</code> <code>Chain</code>.
 * </p>
 */
public class AssignPropertiesCommand extends AbstractCommand {

    /**
     * <p>
     * Common logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(AssignPropertiesCommand.class);
    }

    /**
     * <p>
     * Invokes a chain that fires several Commands.
     * <dl>
     * <dt>Associated Property Commands
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyActionCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyActionListenerCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValidatorCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValueChangeListenerCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValueCommand}
     * </dl>
     * </p>
     *
     * @param context common chains
     * @return <code>true</code> if the chain is complete
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = false;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.clayContext"));
        }

        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages()
                    .getMessage("clay.null.componentBean"));
        }

        Iterator ai = displayElement.getAttributeIterator();
        Catalog defaultCatalog = getCatalog();
        Catalog customizationCatalog = getCustomizationCatalog();

        Command defaultCommand = defaultCatalog.getCommand(Globals.SET_ATTRIBUTE_COMMAND_NAME);
        next: while (ai.hasNext()) {
            AttributeBean a = (AttributeBean) ai.next();
            if (a.getValue() == null) {
               continue next;
            }

            Command command = null;
            //look for a command override in the customization catalog first
            if (customizationCatalog != null) {
                command = customizationCatalog.getCommand(a.getName());
            }
            //look for a command override in the defaut catalog
            if (command == null) {
               command = defaultCatalog.getCommand(a.getName());
            }
            //use the default command
            if (command == null) {
               command = defaultCommand;
            }

            clayContext.setAttribute(a);

            try {
                command.execute(clayContext);
            } catch (Exception e) {
                log.error(getMessages().getMessage("assign.property.error",
                        new Object[] { a }), e);
                        throw e;

            }

        }
        ai = null;

        return isFinal;
    }

}
