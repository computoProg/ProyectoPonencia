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

import org.apache.shale.dialog.basic.model.SubdialogState;

/**
 * <p>{@link SubdialogStateImpl} is a basic implementation of
 * {@link SubdialogState}.</p>
 *
 * @since 1.0.4
 */

public final class SubdialogStateImpl extends AbstractState implements SubdialogState {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The name of the subordinate dialog to be executed by this state.</p>
     */
    private String dialogName = null;


    // -------------------------------------------------------------- Properties


    /**
     * {@inheritDoc}
     */
    public String getDialogName() {

        return this.dialogName;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Render a printable version of this instance.</p>
     *
     * @return The printable version of this instance
     */
    public String toString() {

        return "SubdialogState[dialog=" +
               ((getDialog() != null) ? getDialog().getName() : "<null>") +
               ",name=" + getName() +
               ",dialogName=" + this.dialogName + "]";

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Set the name of the subordinate dialog to be executed
     * by this state.</p>
     *
     * @param dialogName The subordinate dialog name
     */
    public void setDialogName(String dialogName) {

        this.dialogName = dialogName;

    }


}
