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

package org.apache.shale.dialog.basic;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextListener;
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.base.AbstractDialogContext;
import org.apache.shale.dialog.basic.model.ActionState;
import org.apache.shale.dialog.basic.model.Dialog;
import org.apache.shale.dialog.basic.model.EndState;
import org.apache.shale.dialog.basic.model.State;
import org.apache.shale.dialog.basic.model.SubdialogState;
import org.apache.shale.dialog.basic.model.Transition;
import org.apache.shale.dialog.basic.model.ViewState;

/**
 * <p>Implementation of {@link DialogContext} for integrating
 * basic dialog support into the Shale Dialog Manager.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Takes on the responsibilities
 * of the <code>org.apache.shale.dialog.Status</code> implementation in the
 * original approach.</p>
 *
 * @since 1.0.4
 */
final class BasicDialogContext extends AbstractDialogContext
  implements Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4858871161274193403L;


    /**
     * <p>Construct a new instance.</p>
     *
     * @param manager {@link DialogContextManager} instance that owns us
     * @param dialog Configured dialog definition we will be following
     * @param id Dialog identifier assigned to this instance
     * @param parentDialogId Dialog identifier of the parent DialogContext
     *  instance associated with this one (if any)
     */
    BasicDialogContext(DialogContextManager manager, Dialog dialog,
                       String id, String parentDialogId) {

        this.manager = manager;
        this.dialog = dialog;
        this.dialogName = dialog.getName();
        this.id = id;
        this.parentDialogId = parentDialogId;

        if (log().isDebugEnabled()) {
            log().debug("Constructor(id=" + id + ", name="
                      + dialogName + ")");
        }

    }


    // ------------------------------------------------- DialogContext Variables


    /**
     * <p>Flag indicating that this {@link DialogContext} is currently active.</p>
     */
    private boolean active = true;


    /**
     * <p>The {@link Dialog} that this instance is executing.  This value is
     * transient and may need to be regenerated.</p>
     */
    private transient Dialog dialog = null;


    /**
     * <p>The name of the {@link Dialog} that this instance is executing.</p>
     */
    private String dialogName = null;


    /**
     * <p><code>Map</code> of configured dialogs, keyed by logical name.  This value is
     * transient and may need to be regenerated.</p>
     */
    private transient Map dialogs = null;


    /**
     * <p>Dialog identifier for this instance.</p>
     */
    private String id = null;


    /**
     * <p>{@link DialogContextManager} instance that owns us.</p>
     */
    private DialogContextManager manager = null;


    /**
     * <p>The <code>Log</code> instance for this dialog context.
     * This value is lazily created (or recreated) as necessary.</p>
     */
    private transient Log log = null;


    /**
     * <p>Identifier of the parent {@link DialogContext} associated with
     * this {@link DialogContext}, if any.  If there is no such parent,
     * this value is set to <code>null</code>.</p>
     */
    private String parentDialogId = null;


    /**
     * <p>The stack of currently stored Position instances.  If there
     * are no entries, we have completed the exit state and should deactivate
     * ourselves.</p>
     */
    private List positions = new ArrayList();


    /**
     * <p>Flag indicating that execution has started for this dialog.</p>
     */
    private boolean started = false;


    /**
     * <p>The strategy identifier for dealing with saving and restoring
     * state information across requests.  This value is lazily
     * instantiated, and can be recalculated on demand as needed.</p>
     */
    private transient String strategy = null;


    /**
     * <p>An empty parameter list to pass to the action method called by
     * a method binding expression.</p>
     */
    private static final Object[] ACTION_STATE_PARAMETERS = new Object[0];


    /**
     * <p>Parameter signature we create for method bindings used to execute
     * expressions specified by an {@link ActionState}.</p>
     */
    private static final Class[] ACTION_STATE_SIGNATURE = new Class[0];


    /**
     * <p>Flag outcome value that signals a need to transition to the
     * start state for this dialog.</p>
     */
    private static final String START_OUTCOME =
            "org.apache.shale.dialog.basic.START_OUTCOME";


    // ------------------------------------------------ DialogContext Properties


    /** {@inheritDoc} */
    public boolean isActive() {

        return this.active;

    }


    /** {@inheritDoc} */
    public Object getData() {

        synchronized (positions) {
            int index = positions.size() - 1;
            if (index < 0) {
                return null;
            }
            Position position = (Position) positions.get(index);
            return position.getData();
        }

    }



    /** {@inheritDoc} */
    public void setData(Object data) {

        synchronized (positions) {
            int index = positions.size() - 1;
            if (index < 0) {
                throw new IllegalStateException("Cannot set data when no positions are stacked");
            }
            Position position = (Position) positions.get(index);
            Object old = position.getData();
            if ((old != null) && (old instanceof DialogContextListener)) {
                removeDialogContextListener((DialogContextListener) old);
            }
            position.setData(data);
            if ((data != null) && (data instanceof DialogContextListener)) {
                addDialogContextListener((DialogContextListener) data);
            }
        }

    }


    /** {@inheritDoc} */
    public String getId() {

        return this.id;

    }


    /** {@inheritDoc} */
    public String getName() {

        Position position = peek();
        if (position != null) {
            return position.getDialog().getName();
        } else {
            return null;
        }

    }


    /** {@inheritDoc} */
    public Object getOpaqueState() {

        if ("top".equals(strategy())) {
            if (log().isTraceEnabled()) {
                log().trace("getOpaqueState<top> returns " + new TopState(peek().getState().getName(), positions.size()));
            }
            return new TopState(peek().getState().getName(), positions.size());
        } else if ("stack".equals(strategy())) {
            if (log().isTraceEnabled()) {
                log().trace("getOpaqueStrategy<stack> returns stack of " + positions.size());
            }
            return positions;
        } else {
            if (log().isTraceEnabled()) {
                log().trace("getOpaqueStrategy<none> returns nothing");
            }
            return null;
        }

    }


    /** {@inheritDoc} */
    public void setOpaqueState(Object opaqueState) {

        if ("top".equals(strategy())) {
            TopState topState = (TopState) opaqueState;
            if (log().isTraceEnabled()) {
                log().trace("setOpaqueState<top> restores " + topState);
            }
            if (topState.stackDepth != positions.size()) {
                throw new IllegalStateException("Restored stack depth expects "
                        + positions.size() + " but is actually "
                        + topState.stackDepth);
            }
            Position top = peek();
            String oldStateName = top.getState().getName();
            if (!oldStateName.equals(topState.stateName)) {
                fireOnExit(oldStateName);
                top.setState(top.getDialog().findState(topState.stateName));
                fireOnEntry(topState.stateName);
            }
        } else if ("stack".equals(strategy())) {
            if (log().isTraceEnabled()) {
                log().trace("setOpaqueState<stack> restores stack of " + ((List) opaqueState).size());
            }
            List list = (List) opaqueState;
            String oldStateName = peek().getState().getName();
            String newStateName = ((Position) list.get(list.size() - 1)).getState().getName();
            if (!oldStateName.equals(newStateName) || (list.size() != positions.size())) {
                fireOnExit(oldStateName);
                positions = list;
                fireOnEntry(newStateName);
            }
        } else {
            if (log().isTraceEnabled()) {
                log().trace("setOpaqueState<none> restores nothing");
            }
            ; // Do nothing
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


    // --------------------------------------------------- DialogContext Methods


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
                log().trace("punt early since outcome is null");
            }
            return;
        }

        // Perform an initial transition from the current state based on
        // the logical outcome received from the (current) view state
        Position position = peek();
        if (!START_OUTCOME.equals(outcome)) {
            transition(position, outcome);
        }
        State state = position.getState();
        String viewId = null;
        boolean redirect = false;

        // Advance through states until we again encounter the
        // need to render a view state
        while (true) {

            if (state instanceof ActionState) {
                ActionState astate = (ActionState) state;
                if (log().isTraceEnabled()) {
                    log().trace("-->ActionState(method=" + astate.getMethod() + ")");
                }
                try {
                    MethodBinding mb = context.getApplication().
                      createMethodBinding(astate.getMethod(), ACTION_STATE_SIGNATURE);
                    outcome = (String) mb.invoke(context, ACTION_STATE_PARAMETERS);
                } catch (Exception e) {
                    fireOnException(e);
                }
                transition(position, outcome);
                state = position.getState();
                continue;
            } else if (state instanceof EndState) {
                if (log().isTraceEnabled()) {
                    log().trace("-->EndState()");
                }
                pop();
                position = peek();
                if (position == null) {
                    stop(context);
                } else {
                    transition(position, outcome);
                    state = position.getState();
                    continue;
                }
                viewId = ((EndState) state).getViewId();
                redirect = ((EndState) state).isRedirect();
                break;
            } else if (state instanceof SubdialogState) {
                SubdialogState sstate = (SubdialogState) state;
                if (log().isTraceEnabled()) {
                    log().trace("-->SubdialogState(dialogName="
                              + sstate.getDialogName() + ")");
                }
                Dialog subdialog = (Dialog) dialogs(context).get(sstate.getDialogName());
                if (subdialog == null) {
                    throw new IllegalStateException("Cannot find dialog definition '"
                      + sstate.getDialogName() + "'");
                }
                start(subdialog);
                position = peek();
                state = position.getState();
                continue;
            } else if (state instanceof ViewState) {
                viewId = ((ViewState) state).getViewId();
                redirect = ((ViewState) state).isRedirect();
                if (log().isTraceEnabled()) {
                    log().trace("-->ViewState(viewId="
                              + ((ViewState) state).getViewId()
                              + ",redirect=" + ((ViewState) state).isRedirect()
                              + ")");
                }
                break;
            } else {
                throw new IllegalStateException
                  ("State '" + state.getName()
                   + "' of dialog '" + position.getDialog().getName()
                   + "' is of unknown type '" + state.getClass().getName() + "'");
            }
        }

        // Navigate to the requested view identifier (if any)
        if (viewId == null) {
            return;
        }
        if (log().isTraceEnabled()) {
            log().trace("-->Navigate(viewId=" + viewId + ")");
        }
        ViewHandler vh = context.getApplication().getViewHandler();
        if (redirect) {
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

        // inform listeners we've been started
        fireOnStart();

        // set the dialog in motion
        start(dialog);

        // Advance the computation of our dialog until a view state
        // is encountered, then navigate to it
        advance(context, START_OUTCOME);

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
     * {@link DialogContextManager}, or the logic of our <code>stop()</code>
     * method.</p>
     */
    void deactivate() {

        while (positions.size() > 0) {
            pop();
        }
        this.active = false;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link Dialog} instance for the dialog we are executing,
     * regenerating it if necessary first.</p>
     *
     * @param context FacesContext for the current request
     * @return The {@link Dialog} instance we are executing
     */
    private Dialog dialog(FacesContext context) {

        if (this.dialog == null) {
            this.dialog = (Dialog) dialogs(context).get(this.dialogName);
        }
        return this.dialog;

    }


    /**
     * <p>Return a <code>Map</code> of the configured {@link Dialog}s, keyed
     * by logical dialog name.</p>
     *
     * @param context FacesContext for the current request
     * @return The map of available dialogs, keyed by dialog logical name
     *
     * @exception IllegalStateException if dialog configuration has not
     *  been completed
     */
    private Map dialogs(FacesContext context) {

        // Return the cached instance (if any)
        if (this.dialogs != null) {
            return this.dialogs;
        }

        // Return the previously configured application scope instance (if any)
        this.dialogs = (Map)
          context.getExternalContext().getApplicationMap().get(Globals.DIALOGS);
        if (this.dialogs != null) {
            return this.dialogs;
        }

        // Throw an exception if dialog configuration resources have not
        // been processed yet
        throw new IllegalStateException("Dialog configuration resources have not yet been processed");

    }


    /**
     * <p>Return the <code>Log</code> instance for this dialog context,
     * creating one if necessary.</p>
     */
    private Log log() {

        if (log == null) {
            log = LogFactory.getLog(BasicDialogContext.class);
        }
        return log;

    }


    /**
     * <p>Return a <code>Position</code> representing the currently executing
     * dialog and state (if any); otherwise, return <code>null</code>.</p>
     *
     * @return Position representing the currently executing dialog and
     *         state, may be null
     */
    private Position peek() {

        synchronized (positions) {
            int index = positions.size() - 1;
            if (index < 0) {
                return null;
            }
            return (Position) positions.get(index);
        }

    }


    /**
     * <p>Pop the currently executing <code>Position</code> and return the
     * previously executing <code>Position</code> (if any); otherwise,
     * return <code>null</code>.</p>
     *
     * @return Position representing the previously executing dialog and
     *         state (may be null), currently executing dialog Position
     *         is lost.
     */
    private Position pop() {

        synchronized (positions) {
            int index = positions.size() - 1;
            if (index < 0) {
                throw new IllegalStateException("No position to be popped");
            }
            Object data = ((Position) positions.get(index)).getData();
            if ((data != null) && (data instanceof DialogContextListener)) {
                removeDialogContextListener((DialogContextListener) data);
            }
            positions.remove(index);
            if (index > 0) {
                return (Position) positions.get(index - 1);
            } else {
                return null;
            }
        }

    }


    /**
     * <p>Push the specified <code>Position</code>, making it the currently
     * executing one.</p>
     *
     * @param position The new currently executing <code>Position</code>
     */
    private void push(Position position) {

        if (position == null) {
            throw new IllegalArgumentException();
        }
        Object data = position.getData();
        if ((data != null) && (data instanceof DialogContextListener)) {
            addDialogContextListener((DialogContextListener) data);
        }
        synchronized (positions) {
            positions.add(position);
        }

    }


    /**
     * <p>Push a new {@link Position} instance representing the starting
     * {@link State} of the specified {@link Dialog}.</p>
     *
     * @param dialog {@link Dialog} instance to be started and pushed
     */
    private void start(Dialog dialog) {

        // Look up the initial state for the specified dialog
        State state = dialog.findState(dialog.getStart());
        if (state == null) {
            throw new IllegalStateException
              ("Cannot find starting state '"
               + dialog.getStart()
               + "' for dialog '" + dialog.getName() + "'");
        }

        // Construct an appropriate data object for the specified dialog
        Object data = null;
        try {
            data = dialog.getDataClass().newInstance();
        } catch (Exception e) {
            fireOnException(e);
        }

        // Push a Position and corresponding data object to our stacks
        push(new Position(dialog, state, data));

        // inform listeners
        fireOnEntry(state.getName());

    }


    /**
     * <p>Return the strategy identifier for dealing with saving state
     * information across requests.</p>
     */
    private String strategy() {

        if (this.strategy == null) {
            this.strategy = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(Globals.STRATEGY);
            if (this.strategy == null) {
                this.strategy = "top";
            } else {
                this.strategy = this.strategy.toLowerCase();
            }
        }
        return this.strategy;

    }


    /**
     * <p>Transition the specified {@link Position}, based on the specified
     * logical outcome, to the appropriate next {@link State}.</p>
     *
     * @param position {@link Position} to be transitioned
     * @param outcome Logical outcome to use for transitioning
     */
    private void transition(Position position, String outcome) {

        // If the outcome is null, stay on the same state to be consistent
        // with JSF semantics on null outcomes
        if (outcome == null) {
            return;
        }

        State current = position.getState();
        String fromStateId = current.getName();

        // Select the next state based on the specified outcome
        Transition transition = current.findTransition(outcome);
        if (transition == null) {
            transition = position.getDialog().findTransition(outcome);
        }
        if (transition == null) {
            throw new IllegalStateException
              ("Cannot find transition '" + outcome
               + "' for state '" + fromStateId
               + "' of dialog '" + position.getDialog().getName() + "'");
        }
        State next = position.getDialog().findState(transition.getTarget());
        if (next == null) {
            throw new IllegalStateException
              ("Cannot find state '" + transition.getTarget()
               + "' for dialog '" + position.getDialog().getName() + "'");
        }
        String toStateId = next.getName();
        position.setState(next);

        // We inform listeners at the end, the assumption being that a transition
        // must be an atomic transaction, either all associated callbacks must
        // occur as the application would expect, or none should be initiated

        // On a temporal axis, we fire the source state exit handlers first ...
        fireOnExit(fromStateId);

        // ... followed by the transition handlers ...
        fireOnTransition(fromStateId, toStateId);

        // ... and finally, the target state entry handlers
        fireOnEntry(toStateId);

    }


    // ------------------------------------------------ State Storage Subclasses


    /**
     * <p>Class that represents the saved opaque state information when the
     * <code>top</code> strategy is selected.</p>
     */
    public class TopState implements Serializable {

        /**
         * Serial version UID.
         */
        private static final long serialVersionUID = 1L;


        /**
         * <p>Construct an uninitialized instance of this state class.</p>
         */
        public TopState() {
            ;
        }


        /**
         * <p>Construct an initialized instance of this state class.</p>
         *
         * @param stateName Name of the current state in the topmost position
         * @param stackDepth Depth of the position stack
         */
        public TopState(String stateName, int stackDepth) {
            this.stateName = stateName;
            this.stackDepth = stackDepth;
        }


        /**
         * <p>The name of the current state within the topmost
         * <code>Position</code> on the stack.</p>
         */
        public String stateName;


        /**
         * <p>The stack depth of the <code>Position</code> stack.  This is
         * used to detect scenarios where using the back and forward buttons
         * navigates across a subdialog boundary, which means that the
         * saved <code>stateName</code> is likely no longer relevant.</p>
         */
        public int stackDepth;


        /**
         * <p>Return a string representation of this instance.</p>
         */
        public String toString() {
            return "TopState[stateName=" + this.stateName
                   + ", stackDepth=" + this.stackDepth + "]";
        }



    }



}
