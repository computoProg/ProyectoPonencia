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

package org.apache.shale.view.faces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.view.AbstractRequestBean;
import org.apache.shale.view.ApplicationException;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ExceptionHandler;
import org.apache.shale.view.ViewController;

/**
 * <p>{@link ViewPhaseListener} is a JavaServer Faces <code>PhaseListener</code>
 * that implements phase related functionality for the view controller
 * portion of Shale.</p>
 *
 * $Id: ViewPhaseListener.java 560353 2007-07-27 18:49:58Z gvanmatre $
 */

public class ViewPhaseListener implements PhaseListener {


    // -------------------------------------------------------- Static Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2096731130324222021L;


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(ViewPhaseListener.class);


    /**
     * <p>HTTP status to report in the exception attributes we set up.</p>
     */
    private static final int HTTP_STATUS = 200;


    /**
     * <p>Request scope attribute under which the {@link PhaseId} for the
     * current phase is exposed.</p>
     */
    public static final String PHASE_ID = "org.apache.shale.view.PHASE_ID";


    // --------------------------------------------------- PhaseListener Methods


    /**
     * <p>Perform after-phase processing for the phase defined in the
     * specified event.</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     */
    public void afterPhase(PhaseEvent event) {

        if (log.isTraceEnabled()) {
            log.trace("afterPhase(" + event.getFacesContext()
                      + "," + event.getPhaseId() + ")");
        }
        
        if (afterPhaseExceptionCheck(event)) {
            // dispatched to the target error page, stop further processing
            return;
        }

        PhaseId phaseId = event.getPhaseId();
        if (PhaseId.RESTORE_VIEW.equals(phaseId)) {
            afterRestoreView(event);
        } else if (PhaseId.RENDER_RESPONSE.equals(phaseId)
                   || event.getFacesContext().getResponseComplete()) {
            afterRenderResponse(event);
        }
        event.getFacesContext().getExternalContext().getRequestMap().remove(PHASE_ID);

    }


    /**
     * <p>Perform before-phase processing for the phase defined in the
     * specified event.</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     */
    public void beforePhase(PhaseEvent event) {

        if (log.isTraceEnabled()) {
            log.trace("beforePhase(" + event.getFacesContext()
                      + "," + event.getPhaseId() + ")");
        }
        PhaseId phaseId = event.getPhaseId();
        event.getFacesContext().getExternalContext().getRequestMap().put(PHASE_ID, phaseId);
        if (PhaseId.RENDER_RESPONSE.equals(phaseId)) {
            beforeRenderResponse(event);
        }

    }


    /**
     * <p>Return <code>PhaseId.ANY_PHASE</code>, indicating our interest
     * in all phases of the request processing lifecycle.</p>
     */
    public PhaseId getPhaseId() {

        return PhaseId.ANY_PHASE;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>If any exceptions have been accumulated, and the user has specified
     * a forwarding URL, forward to that URL instead of allowing rendering
     * to proceed.</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     * @return <code>true</code> if exceptions have been handled
     * and dispatched to the specified path
     */
    private boolean afterPhaseExceptionCheck(PhaseEvent event) {

        // Have we accumulated any exceptions during the current request?
        // Have we already logged the exception?
        FacesContext context = event.getFacesContext();
        ExternalContext econtext = context.getExternalContext();
        List list = (List)
          econtext.getRequestMap().get(FacesConstants.EXCEPTIONS_LIST);
        if (list == null
         || econtext.getRequestMap().get("javax.servlet.error.exception") != null) {
            return false;
        }

        // Has the user specified a forwarding URL for handling exceptions?
        String path =
          econtext.getInitParameter(Constants.EXCEPTION_DISPATCH_PATH);
        if (path == null) {
            return false;
        }

        // Forward control to the specified path instead of allowing
        // rendering to complete, while simulating container error handling
        try {
            // Set up request attributes reflecting the error conditions,
            // similar to what is passed to an error handler by the servlet
            // container (see Section 9.9.1 of the Servlet Specification)
            ApplicationException exception = new ApplicationException(list);
            Map map = econtext.getRequestMap();
            map.put("javax.servlet.error.status_code", new Integer(HTTP_STATUS)); // Not an HTTP error
            map.put("javax.servlet.error.exception_type", ApplicationException.class);
            map.put("javax.servlet.error.message", exception.getMessage());
            map.put("javax.servlet.error.exception", exception);
            StringBuffer sb = new StringBuffer("");
            if (econtext.getRequestServletPath() != null) {
                sb.append(econtext.getRequestServletPath());
            }
            if (econtext.getRequestPathInfo() != null) {
                sb.append(econtext.getRequestPathInfo());
            }
            map.put("javax.servlet.error.request_uri", sb.toString());
            map.put("javax.servlet.error.servlet_name", "javax.faces.webapp.FacesServlet"); // Best we can do ...
            // Dispatch to the specified error handler
            context.responseComplete();
            // force a destroy before dispatching to the error page
            econtext.getRequestMap().remove(FacesConstants.VIEWS_INITIALIZED);
            econtext.dispatch(path);

        } catch (IOException e) {
            handleException(context, e);
        }

        return true;
    }


    /**
     * <p>Remove all request scoped attributes that implement the
     * <code>ViewController</code> interface or extend the
     * <code>AbstractRequestBean</code> base class.  This will trigger
     * a callback to the <code>destroy()</code> method of such beans,
     * while we are still in the context of a JSF request.</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     */
    private void afterRenderResponse(PhaseEvent event) {

        // Initialize local values we will need
        Map map = event.getFacesContext().getExternalContext().getRequestMap();
        // Remove our list of initialized views explicitly
        map.remove(FacesConstants.VIEWS_INITIALIZED);

        List list = new ArrayList();
        Iterator entries = map.entrySet().iterator();

        ViewControllerCallbacks callbackHandler = getViewControllerCallbacks(event.getFacesContext());
        // select all the ViewController and AbstractRequestBean instances
         while (entries.hasNext()) {
             Map.Entry entry = (Map.Entry) entries.next();
            if (callbackHandler.isViewController(entry.getValue())) {
                 list.add(entry.getKey());
            }
         }

        // Iterate through the keys in the specified order, removing the
        // corresponding request scope attribute instances
        Iterator keys = list.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                map.remove(key);
            } catch (Exception e) {
                handleException(event.getFacesContext(), e);
            }
        }

    }


    /**
     * <p>Call the <code>preprocess()</code> method of the {@link ViewController}
     * that has been restored, if this is a postback.</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     */
    private void afterRestoreView(PhaseEvent event) {

        Map map = event.getFacesContext().getExternalContext().getRequestMap();
        List list = (List) map.get(FacesConstants.VIEWS_INITIALIZED);
        if (list == null) {
            return;
        }
        if (!event.getFacesContext().getExternalContext().getRequestMap().containsKey(FacesConstants.VIEW_POSTBACK)) {
            return;
        }
        Iterator vcs = list.iterator();
        while (vcs.hasNext()) {
            Object vc = vcs.next();
            try {
                getViewControllerCallbacks(event.getFacesContext()).preprocess(vc);
            } catch (Exception e) {
                handleException(event.getFacesContext(), e);
            }
        }

    }



    /**
     * <p>Call the <code>prerender()</code> method of the {@link ViewController}
     * for the view about to be rendered (if any).</p>
     *
     * @param event <code>PhaseEvent</code> for the current event
     */
    private void beforeRenderResponse(PhaseEvent event) {

        Map map = event.getFacesContext().getExternalContext().getRequestMap();
        String viewName = (String) map.get(FacesConstants.VIEW_NAME_RENDERED);
        if (viewName == null) {
            return;
        }
        Object vc = map.get(viewName);
        if (vc == null) {
            return;
        }
        try {
            getViewControllerCallbacks(event.getFacesContext()).prerender(vc);
        } catch (Exception e) {
            handleException(event.getFacesContext(), e);
        }
        map.remove(FacesConstants.VIEW_NAME_RENDERED);

    }


    /**
     * <p>Return the {@link ViewControllerCallbacks} instance we
     * will use.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private ViewControllerCallbacks getViewControllerCallbacks(FacesContext context) {

        ValueBinding vb = context.getApplication().createValueBinding
          ("#{" + FacesConstants.VIEW_CALLBACKS + "}");
        return (ViewControllerCallbacks) vb.getValue(context);

    }


    /**
     * <p>Handle the specified exception according to the strategy
     * defined by our current {@link ExceptionHandler}.</p>
     *
     * @param context FacesContext for the current request
     * @param exception Exception to be handled
     */
    private void handleException(FacesContext context, Exception exception) {

        if (context == null) {
            exception.printStackTrace(System.out);
            return;
        }
        ExceptionHandler handler = (ExceptionHandler)
          context.getApplication().getVariableResolver().resolveVariable
                (context, Constants.EXCEPTION_HANDLER);
        handler.handleException(exception);

    }


}
