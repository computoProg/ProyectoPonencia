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

package org.apache.shale.dialog.scxml;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

import org.apache.commons.scxml.env.SimpleContext;


/**
 * <p>EL context used by the Commons SCXML executor driving the Shale
 * dialog, backed by application {@link VariableResolver}.</p>
 *
 * @since 1.0.5
 *
 * $Id: ShaleDialogELContext.java 501974 2007-01-31 20:18:21Z rahul $
 */

public class ShaleDialogELContext extends SimpleContext {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

   /**
     * Constructor.
     */
    public ShaleDialogELContext() {
        super();
    }


    /**
     * Get the value of this variable from the application
     * {@link VariableResolver} in use; delegating to parent.
     *
     * @param name The variable name
     * @return Object The variable value
     * @see org.apache.commons.scxml.Context#get(java.lang.String)
     */
    public Object get(final String name) {
        FacesContext context = FacesContext.getCurrentInstance();
        VariableResolver vr = context.getApplication().getVariableResolver();
        Object value = vr.resolveVariable(context, name);
        if (value != null) {
            return value;
        }
        return super.get(name);
    }


}
