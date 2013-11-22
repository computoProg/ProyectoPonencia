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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.validator.Arg;
import org.apache.commons.validator.ValidatorResources;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.validator.Globals;
import org.apache.shale.validator.util.AbstractUtilities;
import org.apache.shale.validator.util.ShaleValidatorAction;

/**
 * <p>Abstract base class for validators that use Apache Commons Validator
 * as their foundation.</p>
 */
public abstract class AbstractValidator extends AbstractUtilities
  implements Validator {
    

    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Variable name in a <code>vars</code> map representing the argument
     * that was being evaluated, and should be included in any error message.
     * Typically, this will be either the value itself or a user-friendly
     * (localized) field label.</p>
     */
    protected static final String ARG_VALUE =
            "arg";


    /**
     * <p>Variable name in a <code>vars</code> map representing the maximum
     * value that should be accepted by this <code>Validator</code>.</p>
     */
    protected static final String MAXIMUM_VALUE =
            "max";


    /**
     * <p>Variable name in a <code>vars</code> map representing the minimum
     * value that should be accepted by this <code>Validator</code>.</p>
     */
    protected static final String MINIMUM_VALUE =
            "min";


    /**
     * <p>Variable name in a <code>vars</code> map representing the submitted
     * value that should be validated by this <code>Validator</code>.</p>
     */
    protected static final String SUBMITTED_VALUE =
            "submittedValue";


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Converter helper instance we can use in the <code>convert()</code>
     * method implementation.</p>
     */
    protected static final ConverterHelper helper = new ConverterHelper();



    // -------------------------------------------------------------- Properties


    private boolean client = true;


    /**
     * <p>Return a flag describing whether this validator should be enforced
     * on the client side or not.  Default is <code>true</code>.</p>
     */
    public boolean isClient() {
        return this.client;
    }


    /**
     * <p>Set a flag describing whether this validator should be enforced
     * on the client side or not.</p>
     *
     * @param client The new client enforcement flag
     */
    public void setClient(boolean client) {
        this.client = client;
    }


    // ------------------------------------------------------- Validator Methods


    /**
     * <p>Perform the correctness checks implemented by this
     * {@link Validator} against the specified {@link UIComponent}.
     * If any violations are found, a {@link ValidatorException}
     * will be thrown containing the {@link javax.faces.application.FacesMessage}
     * describing the failure.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong>:  Unlike earlier implementations
     * of Shale Validator integration, validators that subclass this class do
     * not support the option to skip server side validation.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> we are checking for correctness
     * @param value The value to validate
     *
     * @throws ValidatorException if validation fails
     * @throws NullPointerException if <code>context</code>
     *  or <code>component</code> is <code>null</code>
     */
    public abstract void validate(FacesContext context,
                                  UIComponent  component,
                                  Object       value) throws ValidatorException;


    // ----------------------------------------------------- StateHolder Methods


    /** {@inheritDoc} */
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        this.client = Boolean.TRUE.equals((Boolean) values[1]);
    }


    /** {@inheritDoc} */
    public Object saveState(FacesContext context) {
        Object[] values = new Object[2];
        values[0] = super.saveState(context);
        values[1] = this.client ? Boolean.TRUE : Boolean.FALSE;
        return values;
    }


    // ---------------------------------------------------------- Object Methods


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return an array of <code>ShaleValidatorAction</code>s to execute
     * for a given validation, starting with the configured dependent
     * actions, and ending with the action corresponding to the specified
     * action type.  If there is no defined action with the specified
     * type, return <code>null</code>.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param type Type of the validator action for which to return actions
     */
    protected ShaleValidatorAction[] actions(FacesContext context, String type) {

        Map actions = (Map) context.getExternalContext().
          getApplicationMap().get(Globals.VALIDATOR_ACTIONS);
        return (ShaleValidatorAction[]) actions.get(type);

    }


    /**
     * <p>Use the registered converters to convert the specified value
     * to the specified type.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param value Value to be converted
     * @param type Type to which the value should be converted
     */
    protected Object convert(FacesContext context, Object value, Class type) {

        // Is the specified value null?  If so, return it unchanged
        if (value == null) {
            return null;
        }

        // Is the specified value of the correct type already?
        // If so, return it unchanged
        if (type.isInstance(value)) {
            return value;
        }

        // Is the target type String?  If so, use the asString() conversion
        if (type == String.class) {
            if (value instanceof String) {
                return value;
            } else {
                return helper.asString(context, value.getClass(), value);
            }
        }

        // Is the source type String?  If so, use the asObject() conversion
        if (value instanceof String) {
            return helper.asObject(context, type, (String) value);
        }

        // Fall back to converting to String, then to the requested type
        String string = helper.asString(context, value.getClass(), value);
        return helper.asObject(context, type, string);

    }


    /**
     * <p>Return the <code>ValidatorResources</code> that describe the
     * validation rules to be enforced by this application.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    protected ValidatorResources resources(FacesContext context) {

        return (ValidatorResources) context.getExternalContext().
          getApplicationMap().get(Globals.VALIDATOR_RESOURCES);

    }


    /**
     * <p>Perform a validation using the specified Commons Validator type.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> whose value is being validated
     * @param value The value being validated
     * @param type Type of validation to be performed
     * @param vars Mutable map of values to be passed in to the validation
     *
     * @exception ValidatorException if a validation error occurs
     */
    protected void validate(FacesContext context, UIComponent component,
                            Object value, String type, Map vars)
      throws ValidatorException {

        // Look up the actions we must perform
        ShaleValidatorAction[] actions = actions(context, type);
        if (actions == null) {
            throw new IllegalArgumentException("No validator for type '"
                                               + type + "' has been configured");
        }

        // Perform the actions in order, throwing a ValidatorException
        // on the first one that fails (per the standard Validator contract)
        for (int i = 0; i < actions.length; i++) {
            Object instance = actions[i].getInstance();
            Method method = actions[i].getMethod();
            Class[] signature = actions[i].getSignature();
            Arg[] args = actions[i].getParameterArgs();
            Object[] parameters = new Object[signature.length];
            for (int j = 0; j < parameters.length; j++) {
                parameters[j] = convert(context, vars.get(args[j].getKey()), signature[j]);
            }
            Boolean result = null;
            try {
                result = (Boolean) method.invoke(instance, parameters);
            } catch (IllegalAccessException e) {
                ;
            } catch (InvocationTargetException e) {
                ;
            }
            if (!result.booleanValue()) {
                String error = getMessage();
                if (error == null) {
                    error = message(context, actions[i].getMessageKey());
                }
                args = actions[i].getMessageArgs();
                parameters = new Object[args.length];
                for (int j = 0; j < parameters.length; j++) {
                    parameters[j] = vars.get(args[j].getKey());
                }
                Locale locale = context.getViewRoot().getLocale();
                String formatted = new MessageFormat(error, locale).format(parameters);
                FacesMessage message =
                  new FacesMessage(FacesMessage.SEVERITY_ERROR, formatted, null);
                throw new ValidatorException(message);
            }
        }

    }
    

}
