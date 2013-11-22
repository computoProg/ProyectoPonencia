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

import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextListener;

/**
 * <p>Convenience abstract {@link DialogContextListener} implementation. Subclasses
 * are expected to be serializable.</p>
 *
 * @since 1.0.4
 */
public abstract class AbstractDialogContextListener implements DialogContextListener {

    //------------------------------------------------------------- Properties

    /**
     * The {@link DialogContext} we are interested in.
     */
    private DialogContext dialogContext;


    //------------------------------------------------- DialogContextListener methods

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onStart()
     */
    public void onStart() {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onStop()
     */
    public void onStop() {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onException(java.lang.Exception)
     */
    public void onException(Exception e) {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onEntry(java.lang.String)
     */
    public void onEntry(String stateId) {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onExit(java.lang.String)
     */
    public void onExit(String stateId) {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#onTransition(java.lang.String,java.lang.String)
     */
    public void onTransition(String fromStateId, String toStateId) {

        // Do nothing

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#getDialogContext()
     */
    public DialogContext getDialogContext() {

        return dialogContext;

    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.shale.dialog.DialogContextListener#setDialogContext(org.apache.shale.dialog.DialogContext)
     */
    public void setDialogContext(DialogContext dialogContext) {

        this.dialogContext = dialogContext;

    }

}
