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

package org.apache.shale.dialog.basic.config;

import org.apache.shale.dialog.basic.model.ViewState;

/**
 * <p>{@link ViewStateImpl} is a basic implementation of
 * {@link ViewState}.</p>
 *
 * @since 1.0.4
 */

public class ViewStateImpl extends AbstractState implements ViewState {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Flag indicating that the transition to this view should be done
     * with a redirect.</p>
     */
    private boolean redirect = false;


    /**
     * <p>The view identifier of the JavaServer Faces view to render if this
     * state is entered.</p>
     */
    private String viewId = null;


    // -------------------------------------------------------------- Properties


    /**
     * {@inheritDoc}
     */
    public boolean isRedirect() {

        return this.redirect;

    }


    /**
     * {@inheritDoc}
     */
    public String getViewId() {

        return this.viewId;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Render a printable version of this instance.</p>
     *
     * @return The printable version of this instance
     */
    public String toString() {

        return "ViewState[dialog=" +
               ((getDialog() != null) ? getDialog().getName() : "<null>") +
               ",name=" + getName() +
               ",redirect=" + this.redirect +
               ",viewId=" + this.viewId + "]";

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Set the redirect flag for this state.</p>
     *
     * @param redirect The new redirect flag
     */
    public void setRedirect(boolean redirect) {

        this.redirect = redirect;

    }


    /**
     * <p>Set the view identifier of the JavaServer Faces view to render
     * if this state is entered.</p>
     *
     * @param viewId The new view identifier
     */
    public void setViewId(String viewId) {

        this.viewId = viewId;

    }


}
