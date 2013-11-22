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

import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.DialogLifecycleListener;

/**
 * <p>Convenience abstract base class for {@link DialogLifecycleListener}
 * instances.  The default event handling methods do nothing.</p>
 *
 * @since 1.0.4
 */
public abstract class AbstractDialogLifecycleListener implements DialogLifecycleListener {
    

    // -------------------------------------------------- Event Handling Methods


    /** {@inheritDoc} */
    public void onInit(DialogContextManager manager) {

        ; // Do nothing

    }


    /** {@inheritDoc} */
    public void onDestroy(DialogContextManager manager) {

        ; // Do nothing

    }


}
