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

package org.apache.shale.dialog.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextListener;

/**
 * <p>Convenience abstract {@link DialogContext} implementation.
 * Provides listener registration and convenience event firing methods.
 * Subclasses are expected to be serializable.</p>
 *
 * @since 1.0.4
 */
public abstract class AbstractDialogContext implements DialogContext {


    // -------------------------------------------------------------- Properties


    /**
     * The list of all registered {@link DialogContextListener}s for this
     * {@link DialogContext}.
     */
    private List listeners = new ArrayList();


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private transient Log log;


    // ---------------------------------------------------- Listener Bookkeeping


    /**
     * Register given {@link DialogContextListener} for this {@link DialogContext}.
     * Listener cannot be <code>null</code>.
     *
     * @param listener The {@link DialogContextListener} instance.
     */
    public void addDialogContextListener(DialogContextListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Cannot register null DialogContextListener");
        }

        synchronized (listeners) {
            if (listeners.contains(listener)) {
                throw new IllegalArgumentException("DialogContextListener already registered");
            }
            listener.setDialogContext(this); // attach self reference
            listeners.add(listener);
        }
    }


    /**
     * Return the set of currently registered {@link DialogContextListener}s.
     */
    public DialogContextListener[] getDialogContextListeners() {

        synchronized (listeners) {
            return (DialogContextListener[])
              listeners.toArray(new DialogContextListener[listeners.size()]);
        }

    }


    /**
     * Remove this previously registered {@link DialogContextListener} for this
     * {@link DialogContext}. The listener will no longer receive any
     * associated callbacks.
     *
     * @param listener The {@link DialogContextListener} instance.
     */
    public void removeDialogContextListener(DialogContextListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Cannot remove null DialogContextListener");
        }

        boolean removed;
        synchronized (listeners) {
            removed = listeners.remove(listener);
        }
        if (removed) {
            listener.setDialogContext(null); // detach self reference
        }

    }


    // -------------------- Utilities for firing DialogContextListener callbacks


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog
     * instance has begun execution.
     *
     */
    protected void fireOnStart() {

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onStart();
        }

    }


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog
     * instance has finished execution normally.
     *
     */
    protected void fireOnStop() {

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onStop();
        }

    }


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog
     * instance has encountered an unexpected error condition. The exception
     * is first logged for archival.
     *
     * @param exception A potentially implementation specific exception
     *                  during the execution of this dialog instance
     */
    protected void fireOnException(Exception exception) {

        log().error(exception.getMessage(), exception);

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onException(exception);
        }

    }


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog instance
     * execution has entered a particular state.
     *
     * @param stateId Implementation specific identifier of the state
     *                that has been entered
     */
    protected void fireOnEntry(String stateId) {

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onEntry(stateId);
        }

    }


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog instance
     * execution has exited a particular state.
     *
     * @param stateId Implementation specific identifier of the state
     *                that has been exited
     */
    protected void fireOnExit(String stateId) {

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onExit(stateId);
        }

    }


    /**
     * Inform all registered {@link DialogContextListener}s that the dialog instance
     * execution has followed a particular transition.
     *
     * @param fromStateId Implementation specific identifier of the source
     *                    state for the transition that has been followed
     * @param toStateId Implementation specific identifier of the target
     *                  state for the transition that has been followed
     */
    protected void fireOnTransition(String fromStateId, String toStateId) {

        DialogContextListener[] listeners = getDialogContextListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onTransition(fromStateId, toStateId);
        }

    }


    /**
     * <p>Return the <code>Log</code> instance for this class.</p>
     */
    private Log log() {

        if (log == null) {
            log = LogFactory.getLog(AbstractDialogContext.class);
        }
        return log;

    }

}
