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
import java.util.List;
import java.util.Map;
import javax.faces.component.UINamingContainer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ExceptionHandler;
import org.apache.shale.view.ViewController;

/**
 * <p>Specialized implementation of <code>UINamingContainer</code> that
 * provides {@link ViewController} functionality for subviews.</p>
 *
 * $Id: SubviewComponent.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class SubviewComponent extends UINamingContainer {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Log instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(SubviewComponent.class);


    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.view.resources.Bundle",
                   SubviewComponent.class.getClassLoader());


    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>Return the component family for this component.</p>
     */
    public String getFamily() {

        return "org.apache.shale.view.Subview";

    }


    /**
     * <p>Prior to the standard processing, call the <code>prerender()</code>
     * callback on the {@link ViewController} associated with this subview,
     * if any.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     *
     * @exception IOException if an input/output error occurs
     */
    public void encodeBegin(FacesContext context) throws IOException {

        Object vc = getViewController(context, false);
        if (vc != null) {
            try {
                getViewControllerCallbacks(context).prerender(vc);
            } catch (Exception e) {
                handleException(context, e);
            }
        }
        super.encodeBegin(context);

    }


    /**
     * <p>Prior to the standard processing, call the <code>preprocess()</code>
     * callback on the {@link ViewController} associated with this subview,
     * if any.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public void processDecodes(FacesContext context) {

        Object vc = getViewController(context, true);
        if (vc != null) {
            try {
                getViewControllerCallbacks(context).preprocess(vc);
            } catch (Exception e) {
                handleException(context, e);
            }
        }
        super.processDecodes(context);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the {@link ViewController} associated with this component,
     * if any; otherwise, return <code>null</code>.  Note that the signature
     * for this method is <code>Object</code>, because the instance might
     * have the <code>@View</code> annotation rather than implementing the
     * <code>ViewController</code> interface.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param postback Are we processing a postback?
     */
    private Object getViewController(FacesContext context,
                                     boolean postback) {

        // If there is an existing ViewController instance, return it
        // FIXME - different exception for class cast problems?
        String name = getId(); // Name of the managed bean we are looking for
        ExternalContext econtext = context.getExternalContext();
        Object vc = null;
        vc = econtext.getRequestMap().get(name);
        if (vc == null) {
            vc = econtext.getSessionMap().get(name);
        }
        if (vc == null) {
            vc = econtext.getApplicationMap().get(name);
        }
        if (vc != null) {
            return vc;
        }

        // Construct and initialize a new ViewController, if any is associated
        String expr = "#{" + name + "}";
        vc = context.getApplication().
           createValueBinding(expr).getValue(context);
        if (vc == null) {
            log.debug(messages.getMessage("subview.noBean",
                                          new Object[] { getId() }));
            return null;
        }

        // Initialize the ViewController as needed
        if (vc instanceof ViewController) {
            ((ViewController) vc).setPostBack(postback);
        }

        // Schedule this instance for later processing as needed
        Map map = econtext.getRequestMap();
        List list = (List) map.get(FacesConstants.VIEWS_INITIALIZED);
        if (list == null) {
            list = new ArrayList();
            map.put(FacesConstants.VIEWS_INITIALIZED, list);
        }
        list.add(vc);

        // Return the initialized ViewController
        return vc;

    }


    /**
     * <p>Return the <code>ViewControllerCallbacks</code> instance to be used
     * to forward prerender and preprocess callbacks to our view controller,
     * whether or not it implements the <code>ViewController</code> interface
     * (it may not if it is using the <code>@View</code> annotation from the
     * shale-tiger module).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private ViewControllerCallbacks getViewControllerCallbacks(FacesContext context) {

        ViewControllerCallbacks vcb = (ViewControllerCallbacks)
          context.getApplication().getVariableResolver().resolveVariable
                (context, FacesConstants.VIEW_CALLBACKS);
        return vcb;

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
