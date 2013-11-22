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

package org.apache.shale.validator.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;

/**
 * <p>Custom wrapper around a Commons Validator <code>ValidatorAction</code>
 * that precalculates as many of the introspective lookup operations as
 * possible.  This ensures that runtime operation of the validator checks
 * can proceed as quickly as possible.</p>
 */
public final class ShaleValidatorAction {
    

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new instance of ShaleValidatorAction that wraps the
     * specified <code>ValidatorAction</code>.</p>
     *
     * @param resources <code>ValidatorResources</code> for this application
     * @param action The <code>ValidatorAction</code> to be wrapped
     *
     * @exception IllegalArgumentException if configuration data is missing
     *  or incorrect
     */
    public ShaleValidatorAction(ValidatorResources resources,
                                ValidatorAction action) {

        this.resources = resources;
        this.action = action;
        precalculate();

    }
    

    // -------------------------------------------------------- Static Variables


    /**
     * <p>The prefix for form names that correspond to validator types.</p>
     */
    private static final String FORM_NAME_PREFIX = "org.apache.shale.validator.";


    /**
     * <p>Map of the classes for primitive Java types, keyed by the Java
     * keywords for the corresponding primitive.</p>
     */
    private static final Map PRIMITIVE_TYPES = new HashMap();
    static {
        PRIMITIVE_TYPES.put("boolean", boolean.class);
        PRIMITIVE_TYPES.put("byte", byte.class);
        PRIMITIVE_TYPES.put("char", char.class);
        PRIMITIVE_TYPES.put("double", double.class);
        PRIMITIVE_TYPES.put("float", float.class);
        PRIMITIVE_TYPES.put("int", int.class);
        PRIMITIVE_TYPES.put("long", long.class);
        PRIMITIVE_TYPES.put("short", short.class);
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>ValidatorAction</code> that we are wrapping.</p>
     */
    private ValidatorAction action = null;


    /**
     * <p>The class containing the validation method to be called for this action.</p>
     */
    private Class clazz = null;


    /**
     * <p>The field definition containing the mappings for parameter and
     * message subtitution for this validator type.</p>
     */
    private Field field = null;


    /**
     * <p>The form definition corresponding to this validator type that is used
     * for mapping parameter and message arguments.</p>
     */
    private Form form = null;


    /**
     * <p>Return an instance of the specified validation class, if the requested
     * validation method is not static.</p>
     */
    private Object instance = null;


    /**
     * <p>Array of message arguments defining replacement parameters for error
     * messages related to this validator.</p>
     */
    private Arg[] messageArgs = null;


    /**
     * <p>The validation <code>Method</code> to be called for this action.</p>
     */
    private Method method = null;


    /**
     * <p>Array of parameter arguments defining values to be sent in to
     * the validator method for this validator.</p>
     */
    private Arg[] parameterArgs = null;


    /**
     * <p>The <code>ValidatorResources</code> for this application.</p>
     */
    private ValidatorResources resources = null;


    /**
     * <p>The parameter signature for the validation method to be called
     * for this action.</p>
     */
    private Class[] signature = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the <code>ValidatorAction</code> instance we are wrapping.</p>
     */
    public ValidatorAction getAction() {
        return this.action;
    }


    /**
     * <p>Return an instance of the requested validator class, if the requested
     * validation method is not static.  If the method is static, return
     * <code>null</code> instead.</p>
     */
    public Object getInstance() {
        return this.instance;
    }


    /**
     * <p>Return an array of argument metadata describing subtitution values
     * for error messages emitted by this validator type.</p>
     */
    public Arg[] getMessageArgs() {
        return this.messageArgs;
    }


    /**
     * <p>Return the lookup key for the error message template to be used
     * if this validation fails.</p>
     */
    public String getMessageKey() {
        return this.action.getMsg();
    }


    /**
     * <p>Return the validation <code>Method</code> to be called for this
     * action.</p>
     */
    public Method getMethod() {
        return this.method;
    }


    /**
     * <p>Return an array of argument metadata describing the parameter values
     * to be sent to the validation method for this validator type.</p>
     */
    public Arg[] getParameterArgs() {
        return this.parameterArgs;
    }


    /**
     * <p>Return the parameter signature for the validation method to be called
     * for this action.</p>
     */
    public Class[] getSignature() {
        return this.signature;
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Precalculate the values to be returned by our public properties.</p>
     *
     * @exception IllegalArgumentException if configuration data is missing
     *  or incorrect
     */
    private void precalculate() {

        List list = null;
        String value = null;
        String values = null;

        // Acquire a reference to the class loader we will use
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = this.getClass().getClassLoader();
        }

        // Look up the class that contains the validation method
        // we will be calling
        try {
            this.clazz = loader.loadClass(action.getClassname());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("ClassNotFoundException:"
                + " Cannot load class '" + action.getClassname()
                + "' specified by ValidatorAction with name '"
                + action.getName() + "'");
        }

        // Calculate the method parameter signature of the validation
        // method we will be calling
        list = new ArrayList();
        values = action.getMethodParams().trim();
        while (values.length() > 0) {
            // Identify the class name of the next class to be loaded
            int comma = values.indexOf(',');
            if (comma >= 0) {
                value = values.substring(0, comma).trim();
                values = values.substring(comma + 1);
            } else {
                value = values.trim();
                values = "";
            }
            if (value.length() == 0) {
                break;
            }
            // Add the corresponding class instance to our list
            Class clazz = (Class) PRIMITIVE_TYPES.get(value);
            if (clazz == null) {
                try {
                    clazz = loader.loadClass(value);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("ClassNotFoundException:"
                        + " Cannot load method parameter class '" + value
                        + "' specified by ValidatorAction with name '"
                        + action.getName() + "'");
                }
            }
            list.add(clazz);
        }
        this.signature = (Class[]) list.toArray(new Class[list.size()]);

        // Look up the validation method we will be calling
        try {
            this.method = this.clazz.getMethod(action.getMethod(), this.signature);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("NoSuchMethodException:"
                + " Method named '" + action.getMethod() + "' with parameters '"
                + action.getMethodParams() + "' for class '" + action.getClassname()
                + "' not found. Specified by ValidatorAction with name '"
                + action.getName() + "'");
        }

        // Create an instance of the validator class if we need one
        // (which is true only if the validation method is not static
        if (!Modifier.isStatic(this.method.getModifiers())) {
            try {
                this.instance = clazz.newInstance();
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("IllegalAccessException:"
                    + " Not allowed to instantiate class '" + action.getClassname()
                    + "' specified by ValidatorAction with name '" + action.getName()
                    + "' (validation method with name '" + action.getMethod()
                    + "' is not static)");
            } catch (InstantiationException e) {
                throw new IllegalArgumentException("InstantiationException:"
                    + " Could not instantiate class '" + action.getClassname()
                    + "' specified by ValidatorAction with name '" + action.getName()
                    + "' (validation method with name '" + action.getMethod()
                    + "' is not static)");
            }
        }

        // Cache the form definition associated with this validator action type
        this.form = resources.getForm(Locale.getDefault(),
                                      FORM_NAME_PREFIX + action.getName());
        if (this.form == null) {
            throw new IllegalArgumentException(FORM_NAME_PREFIX + action.getName());
        }

        // Look up the predefined "fields" from our form definition
        // and cache interesting information
        this.field = this.form.getField(action.getName());
        if (this.field == null) {
            throw new IllegalArgumentException("Field " + action.getName());
        }
        this.messageArgs = field.getArgs("message");
        if (this.messageArgs == null) {
            this.messageArgs = new Arg[0];
        }
        this.parameterArgs = field.getArgs("parameter");
        if (this.parameterArgs == null) {
            this.parameterArgs = new Arg[0];
        }

        // FIXME - For some reason, Commons Validator is returning a trailing
        // null Arg in the parameterArgs array sometimes, so strip it off
        // if present
        if ((this.parameterArgs.length > 0)
         && (this.parameterArgs[this.parameterArgs.length - 1] == null)) {
            Arg[] results = new Arg[this.parameterArgs.length - 1];
            System.arraycopy(this.parameterArgs, 0, results, 0, results.length);
            this.parameterArgs = results;
        }

        // For robustness, validate the length of the parameter arguments
        // and parameter signature arrays, which should be identical
        if (this.parameterArgs.length != this.signature.length) {
            throw new IllegalArgumentException(this.action.getName()
              + ": signature defines " + this.signature.length
              + " elements but " + this.parameterArgs.length
              + " parameter arguments are specified");
        }

    }


}
