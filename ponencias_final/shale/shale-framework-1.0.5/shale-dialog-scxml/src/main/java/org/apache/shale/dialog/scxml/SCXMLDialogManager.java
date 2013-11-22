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
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.model.SCXML;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextManager;
import org.apache.shale.dialog.base.AbstractDialogContextManager;
import org.apache.shale.dialog.scxml.config.DialogMetadata;

/**
 * <p>Implementation of {@link DialogContextManager} for integrating
 * Commons SCXML into the Shale Dialog Manager.</p>
 *
 * @since 1.0.4
 */
public final class SCXMLDialogManager extends AbstractDialogContextManager
  implements Serializable {


    // ------------------------------------------------------ SCXMLDialogManager Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -4358734655564376781L;


    /**
     * <p><code>Map</code> of {@link SCXML} configurations, keyed
     * by dialog name.  This value is lazily instantiated.</p>
     */
    private Map dialogs = null;


    /**
     * <p>Map containing all currently active {@link DialogContext} instances for
     * the current user.</p>
     */
    private final Map map = new HashMap();


    /**
     * <p>The <code>Log</code> instance for this class.  This value is lazily
     * instantiated, and is also transient and may need to be regenerated.</p>
     */
    private transient Log log = null;


    /**
     * <p>Serial number used to generate dialog instance identifiers.</p>
     */
    private int serial = 0;


    // -------------------------------------------------------- DialogContextManager Methods


    /** {@inheritDoc} */
    public DialogContext create(FacesContext context, String name) {

        return create(context, name, null);

    }


    /** {@inheritDoc} */
    public DialogContext create(FacesContext context, String name, DialogContext parent) {

        // Obtain the dialog metadata for the specified dialog
        DialogMetadata dialog = (DialogMetadata) dialogs(context).get(name);
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

        // Configure a new SCXMLDialogContext instance
        SCXMLDialogContext instance = new SCXMLDialogContext(this, dialog, generateId(),
                parentDialogId);
        instance.setData(new HashMap());
        synchronized (map) {
            map.put(instance.getId(), instance);
        }
        context.getExternalContext().getRequestMap().put(Constants.CONTEXT_BEAN, instance);
        fireOnCreate(instance);

        if (log().isDebugEnabled()) {
            log().debug("create(Created DialogContext instance with ID '"
                + instance.getId() + "' for dialog with name '" + name + "'");
        }

        return instance;
    }


    /** {@inheritDoc} */
    public DialogContext get(String id) {
        return (DialogContext) map.get(id);
    }


    /** {@inheritDoc} */
    public void remove(DialogContext instance) {
        boolean found = false;
        // Cache ID in case any of the listeners destroy instance onRemove()
        String id = instance.getId();
        synchronized (map) {
            found = map.remove(id) == instance;
        }
        if (found) {
            ((SCXMLDialogContext) instance).deactivate();
            fireOnRemove(instance);

            if (log().isDebugEnabled()) {
                log().debug("remove(Removed DialogContext instance with ID '"
                    + id + "'");
            }

        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return a <code>Map</code> of the configured {@link SCXML} instances,
     * keyed by logical dialog name.</p>
     *
     * @param context FacesContext for the current request
     * @return Map of {@link SCXML} instances, keyed by logical dialog name
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
     * @return The generated identifier
     */
    private String generateId() {
        return "" + ++serial;
    }


    /**
     * <p>Return the <code>Log</code> instance for this instance.</p>
     *
     * @return The log instance
     */
    private Log log() {
        if (this.log == null) {
            this.log = LogFactory.getLog(SCXMLDialogManager.class);
        }
        return this.log;
    }

}

