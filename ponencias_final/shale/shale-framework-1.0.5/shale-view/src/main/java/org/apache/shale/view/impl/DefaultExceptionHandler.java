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

package org.apache.shale.view.impl;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import org.apache.shale.view.ExceptionHandler;
import org.apache.shale.view.faces.FacesConstants;

/**
 * <p>Default implementation of the {@link ExceptionHandler} interface.</p>
 *
 * $Id: ExceptionHandlerImpl.java 422609 2006-07-17 03:17:18Z craigmcc $
 */
public class DefaultExceptionHandler implements ExceptionHandler {


    /**
     * <p>Log the specified exception, and record it in a request scoped
     * <code>List</code> that can be used to report them all at a future
     * point in time to report all of the accumulated exceptions.</p>
     *
     * @param exception Exception to be handled
     */
    public void handleException(Exception exception) {

        // Log the exception unconditionally
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            context.getExternalContext().log(exception.getMessage(), exception);
        } else {
            System.out.println(exception.getMessage());
            exception.printStackTrace(System.out);
        }

        // Are we within the context of a JavaServer Faces request?
        // If so, accumulate this exception to the list that can be
        // reported at the completion of the request.
        if (context == null) {
            return;
        }
        List list = (List) context.getExternalContext().getRequestMap().
                get(FacesConstants.EXCEPTIONS_LIST);
        if (list == null) {
            list = new ArrayList();
            context.getExternalContext().getRequestMap().
                    put(FacesConstants.EXCEPTIONS_LIST, list);
        }
        list.add(exception);

    }


}
