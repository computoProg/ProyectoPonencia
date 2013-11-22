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

package org.apache.shale.dialog;

import javax.faces.context.FacesContext;

/**
 * <p>Interface describing the current state of a particular dialog
 * context instance.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Implementations of this interface
 * will be stored in session scope, so they should be serializable.</p>
 *
 * @since 1.0.4
 */
public interface DialogContext {


    // ------------------------------------------------ DialogContext Properties


    /**
     * <p>Return <code>true</code> if this {@link DialogContext} is currently
     * active (created but not yet removed).</p>
     *
     * @return Whether this dialog instance is active
     */
    public boolean isActive();


    /**
     * <p>Return the generic data object representing model state for this
     * dialog instance.</p>
     *
     * @return The data object for this dialog instance
     */
    public Object getData();


    /**
     * <p>Set the generic data object representing model state for this
     * dialog instance.  As a value added feature, if the class of the
     * specified data object implements {@link DialogContextListener},
     * ensure that the data object is registered as a listener with this
     * {@link DialogContext}, and deregistered when the {@link DialogContext}
     * is completed (or this instance is replaced).</p>
     *
     * @param data The new data instance
     */
    public void setData(Object data);


    /**
     * <p>Return the context identifier for this instance of the specified
     * dialog.</p>
     *
     * @return The identifier for this dialog instance, unique for
     *         the manager associated with this dialog instance
     */
    public String getId();


    /**
     * <p>Return the logical name of the dialog being executed by this instance.<?p>
     *
     * @return The logical name of the dialog which this {@link DialogContext}
     *         is an instance of
     */
    public String getName();


    /**
     * <p>Return an opaque object containing any state information (besides the
     * context identifier, which is already saved) that this {@link DialogContext}
     * instance would like to have saved in the JavaServer Faces component tree,
     * and then restored (via a call to <code>setOpaqueData()</code> on the
     * subsequent form submit.  If there is no such information to be recorded,
     * return <code>null</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - Because this object will be
     * stored as part of the JSF component tree, it must be <code>Serializable</code>.</p>
     *
     * <p><strong>WARNING</strong> - This method should <strong>ONLY</strong>
     * be called by the dialog framework infrastructure.  It should
     * <strong>NOT</strong> be called by the application.</p>
     */
    public Object getOpaqueState();


    /**
     * <p>Restore state information that was previously returned by a call to
     * <code>getOpaqueState()</code> on this {@link DialogContext} instance.
     * If the previous call to <code>getOpaqueState()</code> returned <code>null</code>,
     * this method will <strong>NOT</strong> be called.</p>
     *
     * <p><strong>WARNING</strong> - This method should <strong>ONLY</strong>
     * be called by the dialog framework infrastructure.  It should
     * <strong>NOT</strong> be called by the application.</p>
     *
     * @param opaqueState The opaque state object that was previously returned
     *  by a call to <code>getOpaqueState()</code> after potentially being
     *  serialized and deserialized by the JSF state saving functionality
     */
    public void setOpaqueState(Object opaqueState);


    /**
     * <p>Return the parent {@link DialogContext} instance associated with this
     * child {@link DialogContext}, if any; otherwise, return <code>null</code>.</p>
     *
     * @exception IllegalStateException if a parent {@link DialogContext} initially
     *  associated with this {@link DialogContext} is no longer available
     * @return The parent {@link DialogContext}, may be null
     */
    public DialogContext getParent();


    // --------------------------------------------------- DialogContext Methods


    /**
     * <p>Advance the execution of this {@link DialogContext} instance,
     * until an interaction with the user is required.  At that
     * point, navigate to the appropriate view, call
     * <code>FacesContext.renderResponse()</code>, and return.</p>
     *
     * @param context FacesContext for the current request
     * @param outcome Logical outcome to use for driving a transition
     *  out of a state that was waiting for user input, or <code>null</code>
     *  if no transition should be performed
     *
     * @exception IllegalStateException if this {@link DialogContext}
     *  instance has not yet been started
     */
    public void advance(FacesContext context, String outcome);


    /**
     * <p>Start the execution of this {@link DialogContext} instance,
     * advancing until an interaction with the user is required.
     * At that point, navigate to the appropriate view, call
     * <code>FacesContext.renderResopnse()</code>, and return.</p>
     *
     * @param context FacesContext for the current request
     *
     * @exception IllegalStateException if this {@link DialogContext}
     *  instance has already been started
     */
    public void start(FacesContext context);


    /**
     * <p>Stop the execution of this {@link DialogContext} instance,
     * resulting in no currently active dialog for the current
     * JavaServer Faces view.</p>
     *
     * @param context FacesContext for the current request
     *
     * @exception IllegalStateException if this {@link DialogContext}
     *  instance has not yet been started
     */
    public void stop(FacesContext context);


    // ------------------------------------------------- DialogContext Listeners


    /**
     * Register given {@link DialogContextListener} for this {@link DialogContext}.
     * Listener cannot be <code>null</code>.
     *
     * @param listener The {@link DialogContextListener} instance.
     */
    public void addDialogContextListener(DialogContextListener listener);


    /**
     * Return the set of currently registered {@link DialogContextListener}s.
     */
    public DialogContextListener[] getDialogContextListeners();


    /**
     * Remove this previously registered {@link DialogContextListener} for this
     * {@link DialogContext}. The listener will no longer receive any
     * associated callbacks.
     *
     * @param listener The {@link DialogContextListener} instance.
     */
    public void removeDialogContextListener(DialogContextListener listener);


}
