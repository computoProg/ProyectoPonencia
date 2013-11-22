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

package org.apache.shale.dialog.faces;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextManager;

/**
 * <p><code>NavigationHandler</code> implementation that allows dialogs to
 * be created via a logical outcome that returns with a specified prefix.
 * This is not the only way to start a dialog instance (applications can also
 * do this programmatically), but it is convenient in many cases.</p>
 *
 * @since 1.0.4
 */
public final class DialogNavigationHandler extends NavigationHandler {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Creates a new instance of DialogNavigationHandler that delegates
     * to the specified original handler.</p>
     *
     * @param original Original NavigationHandler
     */
    public DialogNavigationHandler(NavigationHandler original) {
        if (log.isInfoEnabled()) {
            log.info("Instantiating DialogNavigationHandler (wrapping instance '"
                     + original + "')");
        }
        this.original = original;
    }


    // ------------------------------------------------- DialogContext Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private Log log = LogFactory.getLog(DialogNavigationHandler.class);


    /**
     * <p>The original <code>NavigationHandler</code> we delegate to.</p>
     */
    private NavigationHandler original = null;


    // ----------------------------------------------- NavigationHandler Methods


    /**
     * <p>Handle a navigation request from the application.  The following
     * rules are applied:</p>
     * <ul>
     * <li>If this view is <strong>NOT</strong> currently executing
     *     a {@link DialogContext} instance:
     *     <ul>
     *     <li>If the logical outcome begins with the prefix identified
     *         by the prefix specified by context init parameter
     *         <code>Constants.DIALOG_PREFIX_PARAM</code> (with a default
     *         value of <code>Constants.DIALOG_PREFIX</code>), create and start
     *         a new {@link DialogContext} instance for a logical name
     *         based on the remainder of the logical outcome after the
     *         prefix.</li>
     *     <li>Otherwise, delegate control to the original navigation
     *         handler passed to our constructor.</li>
     *     </ul></li>
     * <li>If this view is currently executing a {@link DialogContext}
     *     instance, advance its progress based on the specified logical
     *     outcome.</li>
     * </ul>
     *
     * @param context FacesContext for the current request
     * @param fromAction Action that was invoked
     * @param outcome Logical outcome from the invoked action
     */
    public void handleNavigation(FacesContext context, String fromAction,
                                 String outcome) {

        if (log.isTraceEnabled()) {
            log.trace("handleNavigation(context='"
                      + context + "', fromAction='"
                      + fromAction + "', outcome='"
                      + outcome + "')");
        }

        DialogContext dcontext = (DialogContext)
          context.getExternalContext().getRequestMap().get(Constants.CONTEXT_BEAN);
        String prefix = prefix(context);
        if (dcontext == null) {
            if ((outcome != null) && outcome.startsWith(prefix)) {
                // Create and start a new DialogContext instance
                DialogContextManager manager = (DialogContextManager)
                  context.getApplication().getVariableResolver().
                  resolveVariable(context, Constants.MANAGER_BEAN);
                dcontext =
                  manager.create(context, outcome.substring(prefix.length()));
                dcontext.start(context);
                if (log.isDebugEnabled()) {
                    log.debug("Starting dialog '"
                              + outcome.substring(prefix.length())
                              + "' for FacesContext instance '"
                              + context + "' with navigation to viewId '"
                              + context.getViewRoot().getViewId() + "'");
                }
                return;
            } else {
                // No active dialog, so delegate to the original handler
                original.handleNavigation(context, fromAction, outcome);
                return;
            }
        } else {
            // Advance the currently active DialogContext instance
            dcontext.advance(context, outcome);
            if (log.isDebugEnabled()) {
                log.debug("Advancing dialog '"
                          + dcontext.getName() + "' for FacesContext '"
                          + context + "' with navigation to viewId '"
                          + context.getViewRoot().getViewId() + "'");
            }
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Cache the calculated value of the prefix that triggers starting
     * a dialog.</p>
     */
    private String prefix = null;


    /**
     * <p>Return the prefix string that indicates a logical outcome that
     * should trigger starting a dialog.  The default value can be overridden
     * by an appropriate context initialization parameter.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private String prefix(FacesContext context) {

        if (prefix != null) {
            return prefix;
        }
        prefix = context.getExternalContext().
          getInitParameter(Constants.DIALOG_PREFIX_PARAM);
        if (prefix == null) {
            prefix = Constants.DIALOG_PREFIX;
        }
        return prefix;

    }


}
