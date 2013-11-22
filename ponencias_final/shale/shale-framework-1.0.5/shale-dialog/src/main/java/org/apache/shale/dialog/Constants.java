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
 * <p>Manifest constants related to the integration of state machine
 * implementations into the Shale Dialog Manager.</p>
 *
 * @since 1.0.4
 */
public final class Constants {


    /**
     * <p>Request scoped attribute under which the {@link DialogContext} instance
     * for the dialog that is active for this view (if any) is stored.</p>
     */
    public static final String CONTEXT_BEAN = "dialog";


    /**
     * <p>Request parameter containing the dialog identifier of an
     * existing {@link DialogContext} instance to be restored for
     * the current view.  Normally, this identifier will be passed
     * implicitly as part of the JSF view state, but must be passed
     * explicitly if navigation to a view is performed by a redirect.</p>
     */
    public static final String DIALOG_ID =
            "org.apache.shale.dialog.DIALOG_ID";


    /**
     * <p>Request parameter containing the logical name of a new
     * {@link DialogContext} instance to be created for the current view, if
     * there is no active instance already.  If the request parameter
     * specified by <code>Constants.PARENT_ID</code> is also specified,
     * the new instance will be associated with that instance as a parent.</p>
     */
    public static final String DIALOG_NAME =
            "org.apache.shale.dialog.DIALOG_NAME";


    /** 
     * <p>Application scope attribute under which the application may optionally
     * define an instance of {@link DialogLifecycleListener} to receive
     * notification of the creation and removal of {@link DialogContextManager}
     * instances.
     */
    public static final String LIFECYCLE_ATTR =
            "org.apache.shale.dialog.LIFECYCLE_LISTENER";


    /**
     * <p>Default prefix on a logical outcome that indicates a new dialog instance
     * should be initiated.</p>
     */
    public static final String DIALOG_PREFIX = "dialog:";


    /**
     * <p>Context initialization attribute that specifies an override for the
     * default prefix on a logical outcome that indicates a new dialog instance
     * should be initiated.</p>
     */
    public static final String DIALOG_PREFIX_PARAM =
            "org.apache.shale.dialog.DIALOG_PREFIX";


    /**
     * <p>Session scoped attribute under which the {@link DialogContextManager}
     * instance for this user (if any) is stored.  A particular integration will
     * typically declare itself to be a session scoped managed bean with
     * this name.</p>
     */
    public static final String MANAGER_BEAN =
            "org.apache.shale.dialog.MANAGER";


    /**
     * <p>Request parameter containing the {@link DialogContext} instance identifier
     * of a parent instance with which a new child {@link DialogContext} instance
     * (to be created for the current view) should be associated.  Any value for
     * this request parameter will only be processed if the current view has no
     * currently active {@link DialogContext} instance.</p>
     */
    public static final String PARENT_ID =
            "org.apache.shale.dialog.PARENT_ID";


}
