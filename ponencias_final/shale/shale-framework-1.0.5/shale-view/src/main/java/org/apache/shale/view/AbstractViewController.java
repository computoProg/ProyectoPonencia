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

import javax.faces.context.FacesContext;
import org.apache.shale.view.faces.FacesConstants;


/**
 * <p>{@link AbstractViewController} is a convenience base implementation of
 * {@link ViewController}.  It also provides convenience methods inherited
 * from {@link AbstractFacesBean} to all of its subclasses.</p>
 *
 * $Id: AbstractViewController.java 478339 2006-11-22 22:03:44Z craigmcc $
 */

public abstract class AbstractViewController extends AbstractFacesBean
    implements ViewController {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Pre-initialize the <code>postBack</code> property appropriately
     * if we can.</p>
     */
    public AbstractViewController() {

        FacesContext context = FacesContext.getCurrentInstance();
        if ((context != null)
            && context.getExternalContext().getRequestMap().containsKey(FacesConstants.VIEW_POSTBACK)) {
            setPostBack(true);
        }

    }



    // -------------------------------------------------------------- Properties


    /**
     * <p>Flag indicating that this is a postback request.</p>
     */
    private boolean postBack = false;


    /** {@inheritDoc} */
    public boolean isPostBack() {

        return this.postBack;

    }


    /** {@inheritDoc} */
    public void setPostBack(boolean postBack) {

        this.postBack = postBack;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>The default implementation does nothing.</p>
     */
    public void destroy() {
        // do nothing
    }


    /**
     * <p>The default implementation does nothing.</p>
     */
    public void init() {
        // do nothing
    }


    /**
     * <p>The default implementation does nothing.</p>
     */
    public void preprocess() {
        // do nothing
    }


    /**
     * <p>The default implementation does nothing.</p>
     */
    public void prerender() {
        // do nothing
    }


}
