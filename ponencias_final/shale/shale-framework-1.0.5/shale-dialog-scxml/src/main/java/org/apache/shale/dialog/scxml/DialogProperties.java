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

package org.apache.shale.dialog.scxml;

import java.io.Serializable;

/**
 * <p>Simple POJO properties class that gets stored in the root context
 * of each state machine instance driving a Shale dialog.</p>
 *
 * @since 1.0.4
 *
 * $Id: DialogProperties.java 491384 2006-12-31 04:43:49Z rahul $
 */
public class DialogProperties implements Serializable {

    //--------------------------------------------------------- Constructors

    /**
     * This is part of the internal contract of Shale dialogs and should never
     * need to be instantiated outside the parent package.
     */
    DialogProperties() {
        super();
    }

    //---------------------------------  Properties, with getters and setters


    //------------------------------------------------------------- Redirects

    /**
     * Whether the next view rendered by this dialog should use a redirect.
     */
    private boolean nextRedirect = false;

    /**
     * Should next view rendered by this dialog be redirected.
     *
     * @return true, if next view is to be redirected.
     */
    public boolean isNextRedirect() {
        return nextRedirect;
    }

    /**
     * Set next view redirect property.
     *
     * @param nextRedirect Whether next view rendered should be redirected.
     */
    public void setNextRedirect(boolean nextRedirect) {
        this.nextRedirect = nextRedirect;
    }


    //------------------------------------------------------ View Identifiers

    /**
     * The JSF view identifier of the next view to be rendered in
     * this dialog.
     */
    private String nextViewId = null;

    /**
     * Get the JSF view identifier of the next view to be rendered in
     * this dialog.
     *
     * @return The JSF view identifier
     */
    public String getNextViewId() {
        return nextViewId;
    }

    /**
     * Set the JSF view identifier of the next view to be rendered in
     * this dialog.
     *
     * @param nextViewId The JSF view identifier
     */
    public void setNextViewId(String nextViewId) {
        this.nextViewId = nextViewId;
    }


    //---------------------------------------------- Other instance variables

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

}
