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

package org.apache.shale.dialog.scxml;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.SCXMLListener;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.SimpleErrorReporter;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.Transition;
import org.apache.commons.scxml.model.TransitionTarget;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextListener;
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.base.AbstractDialogContext;
import org.apache.shale.dialog.scxml.config.DialogMetadata;

/**
 * <p>Implementation of {@link DialogContextManager} for integrating
 * Commons SCXML into the Shale Dialog Manager.</p>
 *
 *
 * @since 1.0.4
 */
final class SCXMLDialogContext extends AbstractDialogContext
  implements Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 8423853327094172716L;


    /**
     * <p>Construct a new instance.</p>
     *
     * @param manager {@link DialogContextManager} instance that owns us
     * @param dialog The dialog's metadata (whose executable instance needs
     *               to be created)
     * @param id Dialog identifier assigned to this instance
     * @param parentDialogId Dialog identifier assigned to the parent of
     *                       this instance
     */
    SCXMLDialogContext(DialogContextManager manager, DialogMetadata dialog, String id,
                       String parentDialogId) {
        this.manager = manager;
        this.name = dialog.getName();
        this.dataClassName = dialog.getDataclassname();
        this.id = id;
        this.parentDialogId = parentDialogId;

        // Create a working instance of the state machine for this dialog, but do not
        // set it in motion
        this.executor = new SCXMLExecutor(new ShaleDialogELEvaluator(),
                        new SimpleDispatcher(), new SimpleErrorReporter());
        SCXML statemachine = dialog.getStateMachine();
        this.executor.setStateMachine(statemachine);
        Context rootCtx = new ShaleDialogELContext();
        rootCtx.setLocal(Globals.DIALOG_PROPERTIES, new DialogProperties());
        this.executor.setRootContext(rootCtx);
        this.executor.addListener(statemachine, new DelegatingSCXMLListener());

        if (log().isDebugEnabled()) {
            log().debug("Constructor(id=" + id + ", name="
                      + name + ")");
        }

    }


    // ------------------------------------------------------ DialogContext Variables


    /**
     * <p>Flag indicating that this {@link DialogContext} is currently active.</p>
     */
    private boolean active = true;


    /**
     * <p>Generic data object containing state information for this instance.</p>
     */
    private Object data = null;


    /**
     * <p>Type of data object (FQCN to be instantiated).</p>
     */
    private String dataClassName = null;


    /**
     * <p>Identifier of the parent {@link DialogContext} associated with
     * this {@link DialogContext}, if any.  If there is no such parent,
     * this value is set to <code>null</code>.</p>
     */
    private String parentDialogId = null;

    /**
     * <p>Dialog identifier for this instance.</p>
     */
    private String id = null;


    /**
     * <p>{@link DialogContextManager} instance that owns us.</p>
     */
    private DialogContextManager manager = null;


    /**
     * <p>Logical name of the dialog to be executed.</p>
     */
    private String name = null;


    /**
     * <p>The {@link SCXMLExecutor}, an instance of the state machine
     * defined for the SCXML document for this dialog.</p>
     *
     */
    private SCXMLExecutor executor = null;


    /**
     * <p>Flag indicating that execution has started for this dialog.</p>
     */
    private boolean started = false;


    /**
     * <p>The current SCXML state ID for this dialog instance, maintained
     * to reorient the dialog in accordance with any client-side navigation
     * between "view states" that may have happened since we last left off.
     * Serves as the "opaqueState" for this implementation.</p>
     */
    private String stateId = null;


    /**
     * <p>The <code>Log</code> instance for this dialog context.
     * This value is lazily created (or recreated) as necessary.</p>
     */
    private transient Log log = null;


    // ----------------------------------------------------- DialogContext Properties


    /** {@inheritDoc} */
    public boolean isActive() {
        return this.active;
    }


    /** {@inheritDoc} */
    public Object getData() {
         return this.data;
     }



    /** {@inheritDoc} */
    public void setData(Object data) {
        Object old = this.data;
        if ((old != null) && (old instanceof DialogContextListener)) {
            removeDialogContextListener((DialogContextListener) old);
        }
        this.data = data;
        if ((data != null) && (data instanceof DialogContextListener)) {
            addDialogContextListener((DialogContextListener) data);
        }
    }


    /** {@inheritDoc} */
    public String getId() {
        return this.id;
    }


    /** {@inheritDoc} */
    public String getName() {
        return this.name;
    }


    /** {@inheritDoc} */
    public Object getOpaqueState() {

        return stateId;

    }


    /** {@inheritDoc} */
    public void setOpaqueState(Object opaqueState) {

        String viewStateId = String.valueOf(opaqueState);
        if (viewStateId == null) {
            throw new IllegalArgumentException("Dialog instance '" + getId()
                + "' for dialog name '" + getName()
                + "': null opaqueState received");
        }

        // account for user agent navigation
        if (!viewStateId.equals(stateId)) {

            if (log().isTraceEnabled()) {
                log().trace("Dialog instance '" + getId() + "' of dialog name '"
                    + getName() + "': user navigated to view for state '"
                    + viewStateId + "', setting dialog to this state instead"
                    + " of '" + stateId + "'");
            }

            Map targets = executor.getStateMachine().getTargets();
            State serverState = (State) targets.get(stateId);
            State clientState = (State) targets.get(viewStateId);
            if (clientState == null) {
                throw new IllegalArgumentException("Dialog instance '"
                    + getId() + "' for dialog name '" + getName()
                    + "': opaqueState is not a SCXML state ID for the "
                    + "current dialog state machine");
            }

            Set states = executor.getCurrentStatus().getStates();
            if (states.size() != 1) {
                throw new IllegalStateException("Dialog instance '"
                    + getId() + "' for dialog name '" + getName()
                    + "': Cannot have multiple leaf states active when the"
                    + " SCXML dialog is in a 'view' state");
            }

            // remove last known server-side state, set to correct
            // client-side state and fire the appropriate DCL events
            states.remove(serverState);
            fireOnExit(serverState.getId());

            fireOnEntry(clientState.getId());
            states.add(clientState);

        }

    }


    /** {@inheritDoc} */
    public DialogContext getParent() {

        if (this.parentDialogId != null) {
            DialogContext parent = manager.get(this.parentDialogId);
            if (parent == null) {
                throw new IllegalStateException("Dialog instance '"
                        + parentDialogId + "' was associated with this instance '"
                        + getId() + "' but is no longer available");
            }
            return parent;
        } else {
            return null;
        }

    }


    // -------------------------------------------------------- DialogContext Methods


    /** {@inheritDoc} */
    public void advance(FacesContext context, String outcome) {

        if (!started) {
            throw new IllegalStateException("Dialog instance '"
                    + getId() + "' for dialog name '"
                    + getName() + "' has not yet been started");
        }

        if (log().isDebugEnabled()) {
            log().debug("advance(id=" + getId() + ", name=" + getName()
                      + ", outcome=" + outcome + ")");
        }

        // If the incoming outcome is null, we want to stay in the same
        // (view) state *without* recreating it, which would destroy
        // any useful information that components might have stored
        if (outcome == null) {
            if (log().isTraceEnabled()) {
                log().trace("advance(outcome is null, stay in same view)");
            }
            return;
        }

        ((ShaleDialogELEvaluator) executor.getEvaluator()).
                    setFacesContext(context);
        executor.getRootContext().setLocal(Globals.POSTBACK_OUTCOME, outcome);

        try {
            executor.triggerEvent(new TriggerEvent(Globals.POSTBACK_EVENT,
                                TriggerEvent.SIGNAL_EVENT));
        } catch (ModelException me) {
            fireOnException(me);
        }

        Iterator iterator = executor.getCurrentStatus().getStates().iterator();
        this.stateId = ((State) iterator.next()).getId();
        DialogProperties dp = (DialogProperties) executor.getRootContext().
            get(Globals.DIALOG_PROPERTIES);

        // If done, stop context
        if (executor.getCurrentStatus().isFinal()) {
            stop(context);
        }

        navigateTo(stateId, context, dp);

    }


    /** {@inheritDoc} */
    public void start(FacesContext context) {

        if (started) {
            throw new IllegalStateException("Dialog instance '"
                    + getId() + "' for dialog name '"
                    + getName() + "' has already been started");
        }
        started = true;

        if (log().isDebugEnabled()) {
            log().debug("start(id=" + getId() + ", name="
                      + getName() + ")");
        }

        // inform listeners we're good to go
        fireOnStart();

        // Construct an appropriate data object for the specified dialog
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = SCXMLDialogContext.class.getClassLoader();
        }
        Class dataClass = null;
        try {
            dataClass = loader.loadClass(dataClassName);
            data = dataClass.newInstance();
        } catch (Exception e) {
            fireOnException(e);
        }

        if (data != null && data instanceof DialogContextListener) {
            addDialogContextListener((DialogContextListener) data);
        }

        // set state machine in motion
        ((ShaleDialogELEvaluator) executor.getEvaluator()).
            setFacesContext(context);
        try {
            executor.go();
        } catch (ModelException me) {
            fireOnException(me);
        }

        Iterator iterator = executor.getCurrentStatus().getStates().iterator();
        this.stateId = ((State) iterator.next()).getId();
        DialogProperties dp = (DialogProperties) executor.getRootContext().
            get(Globals.DIALOG_PROPERTIES);

        // Might be done at the beginning itself, if so, stop context
        if (executor.getCurrentStatus().isFinal()) {
            stop(context);
        }

        navigateTo(stateId, context, dp);

    }


    /** {@inheritDoc} */
    public void stop(FacesContext context) {

        if (!started) {
            throw new IllegalStateException("Dialog instance '"
                    + getId() + "' for dialog name '"
                    + getName() + "' has not yet been started");
        }
        started = false;

        if (log().isDebugEnabled()) {
            log().debug("stop(id=" + getId() + ", name="
                      + getName() + ")");
        }

        deactivate();
        manager.remove(this);

        // inform listeners
        fireOnStop();

    }


    // ------------------------------------------------- Package Private Methods


    /**
     * <p>Mark this {@link DialogContext} as being deactivated.  This should only
     * be called by the <code>remove()</code> method on our associated
     * {@link DialogContextManager}.</p>
     */
    void deactivate() {
        setData(null);
        this.active = false;
    }


    //  ------------------------------------------------- Private Methods


    /**
     * <p>Navigate to the JavaServer Faces <code>view identifier</code>
     * that is mapped to by the current state identifier for this dialog.</p>
     *
     * @param stateId The current state identifier for this dialog.
     * @param context The current <code>FacesContext</code>
     * @param dp The <code>DialogProperties</code> for the current dialog
     */
    private void navigateTo(String stateId, FacesContext context, DialogProperties dp) {
        // Determine the view identifier
        String viewId = dp.getNextViewId();
        if (viewId == null) {
            ValueBinding vb = context.getApplication().createValueBinding
                ("#{" + Globals.STATE_MAPPER + "}");
            DialogStateMapper dsm = (DialogStateMapper) vb.getValue(context);
            viewId = dsm.mapStateId(name, stateId, context);
        } else {
            dp.setNextViewId(null); // one time use
        }

        // Navigate to the requested view identifier (if any)
        if (viewId == null) {
            return;
        }
        if (!viewId.startsWith("/")) {
            viewId = "/" + viewId;
        }

        // The public API is advance, so thats part of the message
        if (log().isDebugEnabled()) {
            log().debug("advance(id=" + getId() + ", name=" + getName()
                      + ", navigating to view: '" + viewId + "')");
        }

        ViewHandler vh = context.getApplication().getViewHandler();
        if (dp.isNextRedirect()) {
            // clear redirect flag
            dp.setNextRedirect(false);
            String actionURL = vh.getActionURL(context, viewId);
            if (actionURL.indexOf('?') < 0) {
                actionURL += '?';
            } else {
                actionURL += '&';
            }
            actionURL += Constants.DIALOG_ID + "=" + this.id;
            try {
                ExternalContext econtext = context.getExternalContext();
                econtext.redirect(econtext.encodeActionURL(actionURL));
                context.responseComplete();
            } catch (IOException e) {
                throw new FacesException("Cannot redirect to " + actionURL, e);
            }
        } else {
            UIViewRoot view = vh.createView(context, viewId);
            view.setViewId(viewId);
            context.setViewRoot(view);
            context.renderResponse();
        }
    }


    /**
     * <p>Return the <code>Log</code> instance for this dialog context,
     * creating one if necessary.</p>
     *
     * @return The log instance.
     */
    private Log log() {

        if (log == null) {
            log = LogFactory.getLog(SCXMLDialogContext.class);
        }
        return log;

    }


    /**
     * A {@link SCXMLListener} that delegates to the Shale
     * {@link DialogContextListener}s attached to this {@link DialogContext}.
     */
    class DelegatingSCXMLListener implements SCXMLListener, Serializable {

        /**
         * Serial version UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Handle entry callbacks.
         *
         * @param tt The <code>TransitionTarget</code> being entered.
         */
        public void onEntry(TransitionTarget tt) {

            fireOnEntry(tt.getId());

        }

        /**
         * Handle transition callbacks.
         *
         * @param from The source <code>TransitionTarget</code>
         * @param to The destination <code>TransitionTarget</code>
         * @param t The <code>Transition</code>
         */
        public void onTransition(TransitionTarget from, TransitionTarget to,
                                 Transition t) {

            fireOnTransition(from.getId(), to.getId());

        }

        /**
         * Handle exit callbacks.
         *
         * @param tt The <code>TransitionTarget</code> being exited.
         */
        public void onExit(TransitionTarget tt) {

            fireOnExit(tt.getId());

        }

    }

}

