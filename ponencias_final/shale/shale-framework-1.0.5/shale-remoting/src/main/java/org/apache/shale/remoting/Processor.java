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

package org.apache.shale.remoting;

import java.io.IOException;
import javax.faces.context.FacesContext;

/**
 * <p>Interface describing business logic responsible for processing an incoming
 * remoting request, and creating the corresponding response.</p>
 */
public interface Processor {


    /**
     * <p>Process the current request, producing the corresponding response
     * by whatever means is appropriate.  The state of the current request can
     * be derived by calling <code>FacesContext.getCurrentInstance()</code>.
     * Typically, an implementation of this method will call the
     * <code>responseComplete()</code> method on this <code>FacesContext</code>
     * instance, to bypass the remainder of the standard JavaServer Faces
     * request processing lifecycle.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier used to select the appropriate response
     *  (this will generally be a context relative path starting with "/")
     *
     * @exception IOException if an input/output error occurs
     * @exception NullPointerException if <code>resourceId</code> is <code>null</code>
     */
    public void process(FacesContext context, String resourceId) throws IOException;


}
