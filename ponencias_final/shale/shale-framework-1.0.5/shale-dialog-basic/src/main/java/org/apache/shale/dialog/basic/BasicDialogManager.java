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

package org.apache.shale.dialog.basic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.base.AbstractDialogContextManager;
import org.apache.shale.dialog.basic.model.Dialog;

/**
 * <p>Implementation of {@link DialogContextManager} for integrating
 * basic dialog support into the Shale Dialog Manager.</p>
 *
 * @since 1.0.4
 */
public final class BasicDialogManager extends AbstractDialogContextManager
  implements Serializable {


    // ------------------------------------------------- DialogContext Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 7528541012789489425L;


    /**
     * <p><code>Map</code> of {@link Dialog} configurations, keyed
     * by dialog name.  This value is lazily instantiated, and is also
     * transient and may need to be regenerated.</p>
     */
    private transient Map dialogs = null;


    /**
     * <p>The <code>Log</code> instance for this class.  This value is lazily
     * instantiated, and is also transient and may need to be regenerated.</p>
     */
    private transient Log log = null;


    /**
     * <p>Map containing all currently active {@link DialogContext} instances for
     * the current user.</p>
     */
    private final Map map = new HashMap();


    /**
     * <p>Serial number used to generate dialog instance identifiers.</p>
     */
    private int serial = 0;


    // -------------------------------------------- DialogContextManager Methods


    /** {@inheritDoc} */
    public DialogContext create(FacesContext context, String name) {

        return create(context, name, null);

    }


    /** {@inheritDoc} */
    public DialogContext create(FacesContext context, String name, DialogContext parent) {

        // Look up the specified dialog configuration
        Map dialogs = dialogs(context);
        Dialog dialog = (Dialog) dialogs.get(name);
        if (dialog == null) {
            throw new IllegalArgumentException("No definition for dialog name '"
                                               + name + "' can be found");
        }

        // Validate the specified parent (if any)
        String parentDialogId = null;
        if (parent != null) {
            parentDialogId = parent.getId();
            if (parent != get(parentDialogId)) {
                throw new IllegalStateException("The specified parent DialogContext '"
                        + parentDialogId + "' is not managed by this DialogContextManager");
            }
        }

        // Configure a new BasicDialogContext instance
        BasicDialogContext instance = new BasicDialogContext(this, dialog, generateId(),
                                                              parentDialogId);
        synchronized (map) {
            map.put(instance.getId(), instance);
        }
        context.getExternalContext().getRequestMap().put(Constants.CONTEXT_BEAN, instance);
        fireOnCreate(instance);
        return instance;

    }


    /** @{inheritDoc} */
    public DialogContext get(String id) {
        return (DialogContext) map.get(id);
    }


    /** @{inheritDoc} */
    public void remove(DialogContext instance) {
        boolean found = false;
        synchronized (map) {
            found = map.remove(instance.getId()) == instance;
        }
        if (found) {
            ((BasicDialogContext) instance).deactivate();
            fireOnRemove(instance);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return a <code>Map</code> of the configured {@link Dialog}s, keyed
     * by logical dialog name.</p>
     *
     * @param context FacesContext for the current request
     * @return The map of available dialogs, keyed by dialog logical name
     */
    private Map dialogs(FacesContext context) {

        // Return the cached instance (if any)
        if (this.dialogs != null) {
            return this.dialogs;
        }

        // Return the previously configured application scope instance (if any)
        this.dialogs = (Map)
          context.getExternalContext().getApplicationMap().get(Globals.DIALOGS);
        return this.dialogs;

    }


    /**
     * <p>Generate and return a new dialog identifier.  FIXME - switch to
     * something that creates randomized identifiers?</p>
     *
     * @return The new dialog identifier
     */
    private String generateId() {
        return "" + ++serial;
    }


    /**
     * <p>Return the <code>Log</code> instance for this instance.</p>
     *
     * @return The {@link Log} instance used by this manager
     */
    private Log log() {
        if (this.log == null) {
            this.log = LogFactory.getLog(BasicDialogManager.class);
        }
        return this.log;
    }


}
