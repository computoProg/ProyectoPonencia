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

import org.apache.shale.view.AbstractRequestBean;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ExceptionHandler;
import org.apache.shale.view.ViewController;

/**
 * <p>Utility class to perform the event callbacks specified by the
 * {@link ViewController} interface.  The method signatures make it
 * possible to specialize the behavior for annotated callbacks in the
 * <em>Shale Tiger Extensions</em> module.</p>
 *
 * @since 1.0.1
 *
 * $Id: ViewControllerCallbacks.java 560353 2007-07-27 18:49:58Z gvanmatre $
 */
public class ViewControllerCallbacks {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Perform the <code>preprocess</code> callback on the specified
     * instance.</p>
     *
     * @param instance Bean instance on which to perform this callback
     */
    public void preprocess(Object instance) {

        if (instance instanceof ViewController) {
            try {
                ((ViewController) instance).preprocess();
            } catch (Exception e) {
                handleException(FacesContext.getCurrentInstance(), e);
            }
        }

    }


    /**
     * <p>Perform the <code>prerender</code> callback on the specified
     * instance.</p>
     *
     * @param instance Bean instance on which to perform this callback
     */
    public void prerender(Object instance) {

        if (instance instanceof ViewController) {
            try {
                ((ViewController) instance).prerender();
            } catch (Exception e) {
                handleException(FacesContext.getCurrentInstance(), e);
            }
        }

    }

    /**
     * Checks the <code>instance</code> to determine if it is a
     * shale <code>ViewController</code> or <code>AbstractRequestBean</code>.
     *
     * @param instance Bean instance on which to perform this callback
     * @return <code>true</code> if the instance implements <code>ViewController</code>
     */

    public boolean isViewController(Object instance) {
        
        return (instance instanceof ViewController
                || (instance instanceof AbstractRequestBean));
    }

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Handle the specified exception according to the strategy
     * defined by our current {@link ExceptionHandler}.</p>
     *
     * @param context FacesContext for the current request
     * @param exception Exception to be handled
     */
    protected void handleException(FacesContext context, Exception exception) {

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
