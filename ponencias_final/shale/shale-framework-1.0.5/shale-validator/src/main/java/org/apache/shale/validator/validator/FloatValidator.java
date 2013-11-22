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

package org.apache.shale.validator.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 * <p><code>Validator</code> implementation that will perform both format
 * and (optional) range checks on a Float value.</p>
 */
public final class FloatValidator extends AbstractValidator
  implements StateHolder {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The Apache Commons Validator instance we will be using.</p>
     */
    private static final
      org.apache.commons.validator.routines.FloatValidator INSTANCE =
      org.apache.commons.validator.routines.FloatValidator.getInstance();


    // -------------------------------------------------------------- Properties


    private float maximum = Float.MAX_VALUE;
    private boolean maximumSet = false;


    /**
     * <p>Return the configured maximum value for this validator.</p>
     */
    public float getMaximum() {
        return this.maximum;
    }
    

    /**
     * <p>Set the configured maximum value for this validator.</p>
     *
     * @param maximum The new maximum value
     */
    public void setMaximum(float maximum) {
        this.maximum = maximum;
        this.maximumSet = true;
    }


    private float minimum = Float.MIN_VALUE;
    private boolean minimumSet = false;


    /**
     * <p>Return the configured minimum value for this validator.</p>
     */
    public float getMinimum() {
        return this.minimum;
    }
    

    /**
     * <p>Set the configured minimum value for this validator.</p>
     *
     * @param minimum The new minimum value
     */
    public void setMinimum(float minimum) {
        this.minimum = minimum;
        this.minimumSet = true;
    }


    // ------------------------------------------------------- Validator Methods


    /** {@inheritDoc} */
    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException {

        // Validate our parameters
        if ((context == null) || (component == null)) {
            throw new NullPointerException(message(context, "common.null"));
        }
        if (value == null) {
            return;
        }

        // Conversion is expected to have occurred already, via an
        // explicit or implicit Converter having been applied.
        // Throw a validation error if this is not the case
        if (!(value instanceof Float)) {
            throw new ValidatorException
              (new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                message(context, "Float.unconverted"),
                                null));
        }

        // Perform the requested range checks (if any)
        Float fvalue = (Float) value;
        if (minimumSet) {
            if (maximumSet) {
                if (!INSTANCE.isInRange(fvalue, minimum, maximum)) {
                    throw new ValidatorException
                      (new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        message(context, "Common.range",
                                                new Object[] { fvalue,
                                                               new Float(minimum),
                                                               new Float(maximum) }),
                                        null));
                }
            } else {
                if (!INSTANCE.minValue(fvalue, minimum)) {
                    throw new ValidatorException
                      (new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        message(context, "Common.minimum",
                                                new Object[] { fvalue,
                                                               new Float(minimum) }),
                                        null));
                }
            }
        } else if (maximumSet) {
            if (!INSTANCE.maxValue(fvalue, maximum)) {
                throw new ValidatorException
                  (new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    message(context, "Common.maximum",
                                            new Object[] { fvalue,
                                                           new Float(maximum) }),
                                    null));
            }
        }

    }


    // ----------------------------------------------------- StateHolder Methods


    /** {@inheritDoc} */
    public void restoreState(FacesContext context, Object state) {

        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        maximum = ((Float) values[1]).floatValue();
        maximumSet = ((Boolean) values[2]).booleanValue();
        minimum = ((Float) values[3]).floatValue();
        minimumSet = ((Boolean) values[4]).booleanValue();

    }


    /** {@inheritDoc} */
    public Object saveState(FacesContext context) {

        Object[] values = new Object[5];
        values[0] = super.saveState(context);
        values[1] = new Float(maximum);
        values[2] = this.maximumSet ? Boolean.TRUE : Boolean.FALSE;
        values[3] = new Float(minimum);
        values[4] = this.minimumSet ? Boolean.TRUE : Boolean.FALSE;
        return values;

    }


    // ---------------------------------------------------------- Object Methods


}
