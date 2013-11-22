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

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.DialogContextManagerListener;
import org.apache.shale.dialog.DialogLifecycleListener;

/**
 * <p>Abstract base class for {@link DialogContextManager} implementations.
 * Provides listener registration and event firing convenience methods.
 * Subclasses are expected to be serializable.</p>
 *
 * @since 1.0.4
 */
public abstract class AbstractDialogContextManager
  implements DialogContextManager, HttpSessionBindingListener {
    

    // ------------------------------------------------------ Instance Variables


    /**
     * <p><code>List</code> of registered {@link DialogContextManagerListener}
     * instances.</p>
     */
    private List listeners = new ArrayList();


    // -------------------------------------- HttpSessionBindingListener Methods


    /**
     * <p>Handle an instance of this class being bound into an HttpSession.</p>
     *
     * @param event HttpSessionBindingEvent to be handled
     */
    public void valueBound(HttpSessionBindingEvent event) {

        DialogLifecycleListener listener = lifecycleListener();
        if (listener != null) {
            listener.onInit(this);
        }

    }


    /**
     * <p>Handle an instance of this class being unbound from an HttpSession.</p>
     *
     * @param event HttpSessionBindingEvent to be handled
     */
    public void valueUnbound(HttpSessionBindingEvent event) {

        DialogLifecycleListener listener = lifecycleListener();
        if (listener != null) {
            listener.onDestroy(this);
        }

    }


    // --------------------------------------------------- Listener Registration


    /**
     * <p>Register a new {@link DialogContextManagerListener} instance.</p>
     *
     * @param listener The new listener instance to be registered
     */
    public void addDialogContextManagerListener(DialogContextManagerListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Cannot register null DialogContextManagerListener");
        }

        synchronized (listeners) {
            if (listeners.contains(listener)) {
                throw new IllegalArgumentException("DialogContextManagerListener already registered");
            }
            listener.setDialogContextManager(this); // attach self reference
            listeners.add(listener);
        }

    }


    /**
     * <p>Return the set of currently registered {@link DialogContextManagerListener}s.
     * If there are no registered listeners, a zero-length array is returned.</p>
     */
    public DialogContextManagerListener[] getDialogContextManagerListeners() {

        synchronized (listeners) {
            return (DialogContextManagerListener[])
              listeners.toArray(new DialogContextManagerListener[listeners.size()]);
        }

    }


    /**
     * <p>Deregister an existing {@link DialogContextManagerListener} instance.</p>
     *
     * @param listener The existing listener to be deregistered
     */
    public void removeDialogContextManagerListener(DialogContextManagerListener listener) {

        if (listener == null) {
            throw new IllegalArgumentException("Cannot remove null DialogContextManagerListener");
        }

        boolean removed;
        synchronized (listeners) {
            removed = listeners.remove(listener);
        }
        if (removed) {
            listener.setDialogContextManager(null); // detach self reference
        }


    }


    // ---------------------------------------------------- Event Firing Methods


    /**
     * <p>Fire an <code>onCreate()</code> event to all registered listeners.</p>
     *
     * @param context The {@link DialogContext} instance that has been created
     */
    protected void fireOnCreate(DialogContext context) {

        DialogContextManagerListener[] listeners =
          getDialogContextManagerListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onCreate(context);
        }

    }


    /**
     * <p>Fire an <code>onRemove()</code> event to all registered listeners.</p>
     *
     * @param context The {@link DialogContext} instance that has been removed
     */
    protected void fireOnRemove(DialogContext context) {

        DialogContextManagerListener[] listeners =
          getDialogContextManagerListeners();
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].onRemove(context);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link DialogLifecycleListener} for this application
     * (if any); otherwise, return <code>null</code>.</p>
     */
    private DialogLifecycleListener lifecycleListener() {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            return null;
        }
        Object result =
          context.getApplication().getVariableResolver().
          resolveVariable(context, Constants.LIFECYCLE_ATTR);
        if ((result != null) && (result instanceof DialogLifecycleListener)) {
            return (DialogLifecycleListener) result;
        } else {
            return null;
        }

    }


}
