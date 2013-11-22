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

package org.apache.shale.view;

/**
 * <p><strong>AbstractRequestBean</strong> is the abstract base class for
 * data bean(s) that are stored in request scope attributes.  It extends
 * {@link AbstractFacesBean}, so it inherits all of the default behavior
 * found there.  In addition, the following lifecycle methods are called
 * automatically when the corresponding events occur:</p>
 * <ul>
 * <li><code>init()</code> - Called when this bean is initially added as a
 *     request scope attribute (typically as the result of
 *     evaluating a value binding or method binding expression).</li>
 * <li><code>destroy()</code> - Called when the bean is removed from the
 *     request attributes (typically as a result of the application
 *     being shut down by the servlet container).</li>
 * </ul>
 *
 * $Id: AbstractRequestBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
public abstract class AbstractRequestBean extends AbstractFacesBean {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Create a new request scope bean.</p>
     */
    public AbstractRequestBean() {
    }


    // ------------------------------------------------------- Lifecycle Methods


    /**
     * <p>This method is called when this bean is initially added to
     * request scope.  Typically, this occurs as a result of evaluating
     * a value binding or method binding expression, which utilizes the
     * managed bean facility to instantiate this bean and store it into
     * request scope.</p>
     *
     * <p>You may customize this method to allocate resources that are required
     * for the lifetime of the current request.</p>
     */
    public void init() {

        // The default implementation does nothing

    }


    /**
     * <p>This method is called when this bean is removed from
     * request scope.  This occurs automatically when the corresponding
     * HTTP response has been completed and sent to the client.</p>
     *
     * <p>You may customize this method to clean up resources allocated
     * during the execution of the <code>init()</code> method, or
     * at any later time during the lifetime of the request.</p>
     */
    public void destroy() {

        // The default implementation does nothing

    }


}
