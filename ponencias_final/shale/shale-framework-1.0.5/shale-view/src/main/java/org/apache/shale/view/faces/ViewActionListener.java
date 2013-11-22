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

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ExceptionHandler;

/**
 * <p>Replacement for the default <code>ActionListener</code> implementation
 * used during the <em>Invoke Application</em> phase of the request processing
 * lifecycle.</p>
 *
 * @since 1.0.3
 *
 * $Id: ViewActionListener.java 464373 2006-10-16 04:21:54Z rahul $
 */
public final class ViewActionListener implements ActionListener {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Create a new action listener instance.</p>
     *
     * @param original The original action listener instance we are wrapping
     */
    public ViewActionListener(ActionListener original) {
        this.original = original;
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The original <code>ActionListener</code> instance we are wrapping.</p>
     */
    private ActionListener original = null;


    // -------------------------------------------------- ActionListener Methods


    /**
     * <p>Handle a default action event.</p>
     *
     * @param event The <code>ActionEvent</code> to be handled
     */
    public void processAction(ActionEvent event) {

        // FIXME - this is probably not the final answer, but gives
        // us a starting point to deal with application event handlers
        // throwing exceptions in a way consistent with elsewhere
        try {
            original.processAction(event);
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    // --------------------------------------------------------- Private Methods


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
