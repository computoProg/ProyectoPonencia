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

package org.apache.shale.dialog.basic.model;

/**
 * <p>A {@link ViewState} encapsulates the rendering of a JavaServer Faces
 * <em>view</em>, identified by a specified view identifier.  When the
 * view is rendered, standard {@link org.apache.shale.view.ViewController}
 * functionality will be supported if there is such a corresponding backing
 * bean.  The logical outcome returned by a {@link ViewState} will be the
 * one returned by the JavaServer Faces action method that was invoked
 * (if any).</p>
 *
 * @since 1.0.4
 */

public interface ViewState extends State {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return <code>true</code> if the transition to this view should
     * be done with a redirect, instead of the normal view creation process.</p>
     */
    public boolean isRedirect();


    /**
     * <p>Return the view identifier of the JavaServer Faces view to render
     * if this state is entered.</p>
     *
     * @return The view identifier of the JavaServer Faces view to render
     */
    public String getViewId();


}
