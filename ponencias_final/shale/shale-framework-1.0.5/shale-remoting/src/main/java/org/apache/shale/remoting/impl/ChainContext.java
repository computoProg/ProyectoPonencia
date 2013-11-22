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

package org.apache.shale.remoting.impl;

import javax.faces.context.FacesContext;
import org.apache.commons.chain.impl.ContextBase;

/**
 * <p>Implementation of <code>Context</code> suitable for use with commands
 * or chains executed via Shale Remoting.</p>
 */
public class ChainContext extends ContextBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -4676385684759991167L;


    /**
     * <p>Construct a new {@link ChainContext} instance wrapping the
     * specified <code>FacesContext</code> instance.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public ChainContext(FacesContext context) {
        this.context = context;
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>FacesContext</code> instance that is wrapped by this
     * <code>Context</code> instance.</p>
     */
    private FacesContext context = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the <code>FacesContext</code> instance that is wrapped by this
     * <code>Context</code> instance.</p>
     */
    public FacesContext getFacesContext() {
        return this.context;
    }


}
