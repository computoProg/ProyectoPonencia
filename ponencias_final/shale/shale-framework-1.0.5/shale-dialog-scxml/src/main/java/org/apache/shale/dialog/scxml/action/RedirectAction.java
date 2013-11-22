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

package org.apache.shale.dialog.scxml.action;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.apache.shale.dialog.scxml.DialogProperties;
import org.apache.shale.dialog.scxml.Globals;

/**
 * <p>Custom Commons SCXML action to signal that the next rendered view
 * in this dialog should use a <i>redirect</i>.</p>
 *
 * @since 1.0.4
 *
 * $Id: RedirectAction.java 486006 2006-12-12 03:46:29Z rahul $
 */
public class RedirectAction extends Action {

    /**
     * <p>Set redirection flag in dialog properties so the next rendered
     * dialog view is issued as a redirect.</p>
     *
     * @param evtDispatcher The EventDispatcher for this execution instance
     * @param errRep        The ErrorReporter
     * @param scInstance    The state machine execution instance information
     * @param appLog        The application log
     * @param derivedEvents The collection of internal events
     * @throws ModelException If execution causes a non-deterministic state
     * @throws SCXMLExpressionException Bad expression
     */
    public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
            SCInstance scInstance, Log appLog, Collection derivedEvents)
    throws ModelException, SCXMLExpressionException {

        DialogProperties dp = (DialogProperties) scInstance.getRootContext().
            get(Globals.DIALOG_PROPERTIES);
        dp.setNextRedirect(true);

        if (log().isDebugEnabled()) {
            log().debug("<redirect>: Next view will be issued via a redirect");
        }

    }


    // --------------------------------------------------------------- Logging

    /**
     * <p>The <code>Log</code> instance for this class.  This value is lazily
     * instantiated, and is also transient and may need to be regenerated.</p>
     */
    private transient Log log = null;


    /**
     * <p>Return the <code>Log</code> instance for this instance.</p>
     *
     * @return The {@link Log} instance used by this manager
     */
    private Log log() {
        if (this.log == null) {
            this.log = LogFactory.getLog(RedirectAction.class);
        }
        return this.log;
    }

}
