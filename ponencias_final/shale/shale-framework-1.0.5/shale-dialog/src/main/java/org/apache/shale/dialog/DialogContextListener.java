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


/**
 * <p>JavaBeans listener for a single instance of a Shale dialog.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Implementations of this interface
 * will be stored in session scope, so they should be serializable.</p>
 *
 * @since 1.0.4
 */
public interface DialogContextListener {


    //----------------------------------------------- Coarse grained callbacks

    /**
     * <p>Handle the starting of the dialog instance.</p>
     */
    public void onStart();


    /**
     * <p>Handle the stopping of the dialog instance.</p>
     */
    public void onStop();


    /**
     * <p>Handle an unexpected failure during the execution of this dialg
     * instance.</p>
     *
     * @param exception A potentially implementation specific exception
     *                  during the execution of this dialog instance
     */
    public void onException(Exception exception);


    //------------------------------------------------- Fine grained callbacks

    /**
     * <p>Handle an entry into a dialog state.</p>
     *
     * @param stateId Implementation specific identifier of the state
     *                that has been entered
     */
    public void onEntry(String stateId);


    /**
     * <p>Handle an exit from a dialog state.</p>
     *
     * @param stateId Implementation specific identifier of the state
     *                that has been exited
     */
    public void onExit(String stateId);


    /**
     * <p>Handle a transition being followed.</p>
     *
     * @param fromStateId Implementation specific identifier of the source
     *                    state for the transition that has been followed
     * @param toStateId Implementation specific identifier of the target
     *                  state for the transition that has been followed
     */
    public void onTransition(String fromStateId, String toStateId);


    //-------------------------------------------------------------- Ownership

    /**
     * <p>Return the {@link DialogContext} instance associated with this
     * {@link DialogContextListener}.</p>
     *
     * @return The {@link DialogContext} whose execution we are listening to
     */
    public DialogContext getDialogContext();


    /**
     * <p>Set the {@link DialogContext} instance associated with this
     * {@link DialogContextListener}.</p>
     *
     * @param dialogContext The {@link DialogContext} whose execution we
     *                      will track
     */
    public void setDialogContext(DialogContext dialogContext);


}
