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
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.DialogContextManagerListener;

/**
 * <p>Convenience abstract base class for {@link DialogContextManagerListener}
 * instances.  The default event handling methods do nothing.
 * Subclasses are expected to be serializable.</p>
 *
 * @since 1.0.4
 */
public abstract class AbstractDialogContextManagerListener
  implements DialogContextManagerListener {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link DialogContextManager} instance we are associated with.</p>
     */
    private DialogContextManager instance = null;


    // -------------------------------------------------- Event Handling Methods


    /** {@inheritDoc} */
    public void onCreate(DialogContext context) {

        ; // Do nothing

    }


    /** {@inheritDoc} */
    public void onRemove(DialogContext context) {

        ; // Do nothing

    }


    // --------------------------------------------------------------- Ownership


    /** {@inheritDoc} */
    public DialogContextManager getDialogContextManager() {
        return this.instance;
    }


    /** {@inheritDoc} */
    public void setDialogContextManager(DialogContextManager instance) {
        this.instance = instance;
    }


}
