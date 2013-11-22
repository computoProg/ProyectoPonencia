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
 * <p>Management functions for the {@link DialogContext} instances related
 * to a particular user's session.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Implementations of this interface
 * will be stored in session scope, so they should be serializable.</p>
 *
 * @since 1.0.4
 */
public interface DialogContextManager {


    /**
     * <p>Create a new instance of the specified dialog configuration, returning
     * the newly created instance.  This instance must contain an
     * <code>id</code> value that is guaranteed to be unique among all active
     * instances for the current user.  The instance must also have been stored
     * as a request scope attribute under key <code>Constants.INSTANCE_BEAN</code>.
     * The new instance will not be associated with any parent instance.</p>
     *
     * @param context FacesContext for the current request
     * @param name Logical name of the dialog to be created
     * @return The newly created {@link DialogContext} instance, with no
     *         parent dialog association
     *
     * @exception IllegalArgumentException if no dialog definition
     * can be found for the specified logical name
     */
    public DialogContext create(FacesContext context, String name);


    /**
     * <p>Create a new instance of the specified dialog configuration, returning
     * the newly created instance.  This instance must contain an
     * <code>id</code> value that is guaranteed to be unique among all active
     * instances for the current user.  The instance must also have been stored
     * as a request scope attribute under key <code>Constants.INSTANCE_BEAN</code>.
     * The new instance will be associated with the specified parent instance,
     * which must be managed by this {@link DialogContextManager} and therefore
     * belong to the same user.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - Applications should generally
     * not call this method directly, because it will be awkward to associate
     * the newly created {@link DialogContext} instance with a different view
     * instance.  Instead, this method is primarily intended to support the
     * ability to automatically associate the {@link DialogContext} for, say,
     * a pop-up window with the {@link DialogContext} of the corresponding
     * main window.  This facility is supported automatically by the phase
     * listener that manages saving and restoring the association of a
     * {@link DialogContext} and its corresponding JSF view.</p>
     *
     * @param context FacesContext for the current request
     * @param name Logical name of the dialog to be executed
     * @param parent Parent DialogContext with which the new instance
     *  will be assocated (if any)
     * @return The newly created "child" {@link DialogContext} instance
     *
     * @exception IllegalArgumentException if no dialog definition
     *  can be found for the specified logical name
     * @exception IllegalStateException if the specified <code>parent</code>
     *  instance is not managed by this {@link DialogContextManager}
     */
    public DialogContext create(FacesContext context, String name,
                                DialogContext parent);


    /**
     * <p>Return the {@link DialogContext} instance for the specified identifier
     * (if any); otherwise, return <code>null</code>.</p>
     *
     * @param id Dialog identifier for which to return an instance
     * @return The {@link DialogContext} instance for this identifier,
     *         may be null
     */
    public DialogContext get(String id);


    /**
     * <p>Remove the specified {@link DialogContext} instance from the set of
     * active instances for the current user.</p>
     *
     * @param instance {@link DialogContext} instance to be removed
     */
    public void remove(DialogContext instance);


    // ------------------------------------------ DialogContextManager Listeners


    /**
     * Register given {@link DialogContextManagerListener} for this
     * {@link DialogContextManager}.  Listener cannot be <code>null</code>.
     *
     * @param listener The {@link DialogContextManagerListener} instance.
     */
    public void addDialogContextManagerListener(DialogContextManagerListener listener);


    /**
     * Return the set of currently registered {@link DialogContextManagerListener}s.
     */
    public DialogContextManagerListener[] getDialogContextManagerListeners();


    /**
     * Remove this previously registered {@link DialogContextManagerListener}
     * for this {@link DialogContextManager}. The listener will no longer receive
     * any associated callbacks.
     *
     * @param listener The {@link DialogContextManagerListener} instance.
     */
    public void removeDialogContextManagerListener(DialogContextManagerListener listener);


}
