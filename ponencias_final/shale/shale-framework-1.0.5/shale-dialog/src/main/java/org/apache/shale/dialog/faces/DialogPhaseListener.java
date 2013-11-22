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

package org.apache.shale.dialog.faces;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.Constants;
import org.apache.shale.dialog.DialogContext;
import org.apache.shale.dialog.DialogContextManager;

/**
 * <p>Phase listener that saves and restores the dialog identifier for the
 * currently active dialog, if any.  Based on the presence of certain
 * request parameters, it can also cause a new {@link DialogContext}
 * instance to be created and started, optionally associated with a parent
 * {@link DialogContext} instance also belonging to the same user.</p>
 *
 * @since 1.0.4
 */
public final class DialogPhaseListener implements PhaseListener {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5219990658057949928L;


    /**
     * <p>Creates a new instance of DialogPhaseListener.</p>
     */
    public DialogPhaseListener() {
        if (log.isInfoEnabled()) {
            log.info("Instantiating DialogPhaseListener()");
        }
    }


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Generic attribute name (on the view root component of the current
     * JSF component tree) under which the context identifier for the
     * dialog instance that is current for this view (if any) should be
     * stored and retrieved.</p>
     */
    private static final String CONTEXT_ID_ATTR =
            "org.apache.shale.dialog.CONTEXT_ID";


    /**
     * <p>Generic attribute name (on the view root component of the current
     * JSF component tree) under which the opaque state information for the
     * current {@link DialogContext} instance (if any) should be stored and
     * retrieved.</p>
     */
    private static final String CONTEXT_OPAQUE_ATTR =
            "org.apache.shale.dialog.OPAQUE_STATE";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private Log log = LogFactory.getLog(DialogPhaseListener.class);


    // --------------------------------------------------- PhaseListener Methods


    /**
     * <p>Return the phase identifier we are interested in.</p>
     *
     * @return The phase identifier of interest
     */
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }


    /**
     * <p>After the <em>Restore View</em> phase, retrieve the current
     * dialog identifier (if any), and restore the corresponding
     * {@link DialogContext}.  If this view is not currently executing
     * a {@link DialogContext} instance, optionally create a new
     * instance based o the presence of request parameters.</p>
     *
     * @param event The phase event to be processed
     */
    public void afterPhase(PhaseEvent event) {

        if (log.isTraceEnabled()) {
            log.trace("afterPhase(phaseId='" + event.getPhaseId()
                      + "',facesContext='" + event.getFacesContext() + "')");
        }

        if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
            afterRestoreView(event.getFacesContext());
        }

    }


    /**
     * <p>Before the <em>Render Response</em> phase, acquire the current
     * dialog identifier (if any), and store it in the view.</p>
     *
     * @param event The phase event to be processed
     */
    public void beforePhase(PhaseEvent event) {

        if (log.isTraceEnabled()) {
            log.trace("beforePhase(phaseId='" + event.getPhaseId()
                      + "',facesContext='" + event.getFacesContext() + "')");
        }

        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            beforeRenderResponse(event.getFacesContext());
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Perform the required processing after the <em>Restore View Phase</em>
     * of the request processing lifecycle has been completed for the current
     * request:</p>
     * <ul>
     * <li>If the restored view contains an appropriate attribute containing
     *     the <code>id</code> of an existing {@link DialogContext} instance
     *     for the current user, this instance is restored.<li>
     * <li>If there is no such <code>id</code> of an existing {@link DialogContext}
     *     instance, AND if the request includes a parameter specifying a
     *     dialog name, a new instance of the specified dialog will be created
     *     and associated with the current view.</li>
     * <li>In the latter case, if the request also includes a parameter specifying
     *     the <code>id</code> of an active {@link DialogContext} instance for
     *     the current user, that existing instance will be configured as the
     *     parent {@link DialogContext} instance for the newly created instance.</li>
     * </ul>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private void afterRestoreView(FacesContext context) {

        // If this view has a currently active dialog context instance,
        // make it visible in request scope and return.  Normally, the
        // instance identifier is passed as part of the JSF view state,
        // but it might also have been passed as a request parameter in
        // the case of a redirect
        String id = (String)
          context.getViewRoot().getAttributes().get(CONTEXT_ID_ATTR);
        if (id == null) {
            id = (String) context.getExternalContext().getRequestParameterMap().
                    get(Constants.DIALOG_ID);
        }
        if (id != null) {
            restore(context, id);
            return;
        }

        // If this request includes a request parameter naming a dialog to be
        // created, create a corresponding {@link DialogContext} instance and
        // associate it with the current request.  If the request also specifies
        // the identifier of an existing {@link DialogContext} instance for the
        // current user, make that instance the parent of the newly created
        // instance
        String dialogName = (String) context.getExternalContext().
                getRequestParameterMap().get(Constants.DIALOG_NAME);
        String parentId = (String) context.getExternalContext().
                getRequestParameterMap().get(Constants.PARENT_ID);
        if (dialogName != null) {

            // Create a new DialogContext instance
            DialogContext dcontext = create(context, dialogName, parentId);
            if (dcontext == null) {
                return;
            }

            // Start the new DialogContext instance
            dcontext.start(context);
            if (log.isDebugEnabled()) {
                log.debug("afterRestoreView() creating dialog context with id '"
                          + id + "' for FacesContext instance '"
                          + context + "' associated with parent dialog context id '"
                          + parentId + "' and advancing to viewId '"
                          + context.getViewRoot().getViewId() + "'");
            }

        }

    }


    /**
     * <p>Before the <em>Render Response</em> phase, acquire the current
     * dialog identifier (if any), along with any corresponding opaque
     * state information, and store it in the view.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private void beforeRenderResponse(FacesContext context) {

        DialogContext dcontext = (DialogContext)
          context.getExternalContext().getRequestMap().get(Constants.CONTEXT_BEAN);
        Map map = context.getViewRoot().getAttributes();
        if ((dcontext != null) && dcontext.isActive()) {
            if (log.isDebugEnabled()) {
                log.debug("beforeRenderResponse() saving dialog context id '"
                          + dcontext.getId()
                          + "' for FacesContext instance '"
                          + context + "'");
            }
            map.put(CONTEXT_ID_ATTR, dcontext.getId());
            Object opaqueState = dcontext.getOpaqueState();
            if (opaqueState != null) {
                map.put(CONTEXT_OPAQUE_ATTR, opaqueState);
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("beforeRenderResponse() erasing dialog context id "
                          + " for FacesContext instance '"
                          + context + "'");
            }
            map.remove(CONTEXT_ID_ATTR);
            map.remove(CONTEXT_OPAQUE_ATTR);
        }

    }



    /**
     * <p>Create and return a new {@link DialogContext} for the specified
     * dialog name and optional parent id.  If no such {@link DialogContext}
     * can be created, return <code>null</code> instead.</p>
     *
     * @param context FacesContext for the current request
     * @param dialogName Logical name of the dialog to be created
     * @param parentId Parent dialog context instance (if any)
     * @return The newly created {@link DialogContext}, may be null
     */
    private DialogContext create(FacesContext context, String dialogName,
                                 String parentId) {

        DialogContextManager manager = (DialogContextManager)
          context.getApplication().getVariableResolver().
                resolveVariable(context, Constants.MANAGER_BEAN);
        if (manager == null) {
            return null;
        }
        DialogContext parent = null;
        if (parentId != null) {
            parent = manager.get(parentId);
        }
        DialogContext dcontext = manager.create(context, dialogName, parent);
        return dcontext;

    }


    /**
     * <p>Restore access to the {@link DialogContext} with the specified id,
     * if possible.  If there was any opaque state information stored, update
     * the corresponding {@link DialogContext} instance as well.</p>
     *
     * @param context FacesContext for the current request
     * @param dialogId Dialog identifier of the {@link DialogContext}
     *  to be restored
     */
    private void restore(FacesContext context, String dialogId) {

        DialogContextManager manager = (DialogContextManager)
          context.getApplication().getVariableResolver().
                resolveVariable(context, Constants.MANAGER_BEAN);
        if (manager == null) {
            return;
        }
        DialogContext dcontext = manager.get(dialogId);
        if (dcontext == null) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("afterPhase() restoring dialog context with id '"
                      + dialogId + "' for FacesContext instance '"
                      + context + "'");
        }
        context.getExternalContext().getRequestMap().put(Constants.CONTEXT_BEAN, dcontext);
        Object opaqueState = context.getViewRoot().getAttributes().get(CONTEXT_OPAQUE_ATTR);
        if (opaqueState != null) {
            dcontext.setOpaqueState(opaqueState);
        }

    }


}
