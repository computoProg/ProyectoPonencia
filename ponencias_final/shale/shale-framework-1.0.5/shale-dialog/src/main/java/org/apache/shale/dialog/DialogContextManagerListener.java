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
 * <p>JavaBeans event listener for events on a {@link DialogContextManager}
 * instance.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Implementations of this interface
 * will be stored in session scope, so they should be serializable.</p>
 *
 * @since 1.0.4
 */
public interface DialogContextManagerListener {
    

    // ------------------------------------------------ Coarse Grained Callbacks


    /**
     * <p>Handle the case where a new {@link DialogContext} instance has
     * been created.  This event will be fired <strong>before</strong> the
     * instance has been started; however, it is legitimate to do things
     * like register fine grained listeners on this instance.</p>
     *
     * @param context The {@link DialogContext} instance being created
     */
    public void onCreate(DialogContext context);


    /**
     * <p>Handle the case where a new {@link DialogContext} instance has
     * been removed.  This event will be fired <strong>after</strong> the
     * instance has been stopped; however, it is legitimate to do things
     * like deregister fine grained listeners on this instance.</p>
     *
     * @param context The {@link DialogContext} instance being created
     */
    public void onRemove(DialogContext context);


    // --------------------------------------------------------------- Ownership


    /**
     * <p>Return the {@link DialogContextManager} instance associated with
     * this {@link DialogContextManagerListener}.</p>
     */
    public DialogContextManager getDialogContextManager();


    /**
     * <p>Set the {@link DialogContextManager} instance associated with
     * this {@link DialogContextManagerListener}.</p>
     *
     * @param manager The new {@link DialogContextManager}
     */
    public void setDialogContextManager(DialogContextManager manager);


}
