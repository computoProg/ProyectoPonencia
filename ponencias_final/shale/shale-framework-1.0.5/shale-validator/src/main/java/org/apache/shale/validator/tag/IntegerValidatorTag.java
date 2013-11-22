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

package org.apache.shale.validator.tag;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.Validator;
import javax.faces.webapp.UIComponentTag;
import javax.faces.webapp.ValidatorTag;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.validator.validator.IntegerValidator;

/**
 * <p>JSP custom action for <code>IntegerValidator</code>.</p>
 */
public final class IntegerValidatorTag extends ValidatorTag {
    

    // -------------------------------------------------------- Static Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * <p>Helper object for performing conversions.</p>
     */
    private static final ConverterHelper HELPER =
            new ConverterHelper();


    // -------------------------------------------------------------- Properties


    private String client = null;
    public void setClient(String client) {
        this.client = client;
    }


    private String maximum = null;
    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }


    private String message = null;
    public void setMessage(String message) {
        this.message = message;
    }


    private String minimum = null;
    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }


    // ------------------------------------------------------------- Tag Methods


    /**
     * <p>Release resources allocated for this instance.</p>
     */
    public void release() {

        client = null;
        maximum = null;
        message = null;
        minimum = null;

        super.release();

    }


    // ---------------------------------------------------- ValidatorTag Methods


    /**
     * <p>Create and return a new <code>Validator</code> instance to be
     * registered on our corresponding <code>UIComponent</code>.</p>
     */
    protected Validator createValidator() {

        FacesContext context = FacesContext.getCurrentInstance();
        IntegerValidator validator = new IntegerValidator();

        if (client != null) {
            Boolean value = null;
            if (UIComponentTag.isValueReference(client)) {
                ValueBinding vb = context.getApplication().createValueBinding(client);
                value = (Boolean) vb.getValue(context);
            } else {
                value = (Boolean) HELPER.asObject(context, Boolean.class, client);
            }
            validator.setClient(value.booleanValue());
        }

        if (maximum != null) {
            Integer value = null;
            if (UIComponentTag.isValueReference(maximum)) {
                ValueBinding vb = context.getApplication().createValueBinding(maximum);
                value = (Integer) vb.getValue(context);
            } else {
                value = (Integer) HELPER.asObject(context, Integer.class, maximum);
            }
            validator.setMaximum(value.intValue());
        }

        if (message != null) {
            String value = null;
            if (UIComponentTag.isValueReference(message)) {
                ValueBinding vb = context.getApplication().createValueBinding(message);
                value = (String) vb.getValue(context);
            } else {
                value = message;
            }
            validator.setMessage(value);
        }

        if (minimum != null) {
            Integer value = null;
            if (UIComponentTag.isValueReference(minimum)) {
                ValueBinding vb = context.getApplication().createValueBinding(minimum);
                value = (Integer) vb.getValue(context);
            } else {
                value = (Integer) HELPER.asObject(context, Integer.class, minimum);
            }
            validator.setMinimum(value.intValue());
        }

        return validator;

    }


}
