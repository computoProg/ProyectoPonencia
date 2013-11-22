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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

/**
 * <p>Abstract base class for converters and validators that use Apache Commons
 * Validator as their foundation.  This class provides common utility methods
 * for concrete converter and validator implementations.</p>
 */
public abstract class AbstractUtilities implements StateHolder {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Name of the resource bundle containing default message strings.</p>
     */
    public static final String DEFAULT_RESOURCE_BUNDLE =
            "org.apache.shale.validator.resources.Bundle";


    // -------------------------------------------------------------- Properties


    /**
     * <p>Custom error message template, if any.</p>
     */
    private String message = null;


    /**
     * <p>Return the custom error message template for this converter or
     * validator, if any.  If not defined, a standard error message template
     * will be used instead.</p>
     */
    public String getMessage() {
        return this.message;
    }


    /**
     * <p>Set the custom error message template for this validator.</p>
     *
     * @param message The new custom error message template, or <code>null</code>
     *  to select the standard template
     */
    public void setMessage(String message) {
        this.message = message;
    }


    // ----------------------------------------------------- StateHolder Methods


    /** {@inheritDoc} */
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        this.message = (String) values[0];
    }


    /** {@inheritDoc} */
    public Object saveState(FacesContext context) {
        Object[] values = new Object[1];
        values[1] = this.message;
        return values;
    }


    private boolean transientValue = false;


    /** {@inheritDoc} */
    public boolean isTransient() {
        return this.transientValue;
    }


    /** {@inheritDoc} */
    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }


    // ---------------------------------------------------------- Object Methods


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return a locale-sensitive message for this converter or
     * validator.  The following algorithm is applied to select the
     * appropriate message:</p>
     * <ul>
     * <li>If the <code>message</code> property has been set, use
     *     that value explicitly</li>
     * <li>If the application specifies a message resource bundle,
     *     and this resource bundle includes a value for the specified
     *     key, use that value</li>
     * <li>If the default message resource bundle for this module
     *     includes a value for the specified key, use that value</li>
     * <li>Create and return a dummy value</li>
     * </ul>
     *
     * @param context <code>FaceContext</code> for the current request
     * @param key Message key for the requested message
     */
    protected String message(FacesContext context, String key) {

        // Return any explicitly specified message
        String message = getMessage();
        if (message != null) {
            return message;
        }

        // Set up variables we will need
        ResourceBundle bundle = null;
        Locale locale = context.getViewRoot().getLocale();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        // Search for a match in the application resource bundle (if any)
        String name = context.getApplication().getMessageBundle();
        if (name != null) {
            bundle = ResourceBundle.getBundle(name, locale, loader);
            if (bundle != null) {
                try {
                    message = bundle.getString(key);
                } catch (MissingResourceException e) {
                    message = null;
                }
                if (message != null) {
                    return message;
                }
            }
        }

        // Otherwise, search the default resource bundle
        bundle = ResourceBundle.getBundle(DEFAULT_RESOURCE_BUNDLE, locale, loader);
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "???" + key + "???";
        }

    }


    /**
     * <p>Retrieve a locale-specific message for the specified key, then
     * treat it as a message format pattern, and substitute in the specified
     * parameter values and return the resulting string.</p>
     *
     * @param context <code>FaceContext</code> for the current request
     * @param key Message key for the requested message
     * @param parameters Replacement parameters to substitute in to
     *  the retrieved message
     */
    protected String message(FacesContext context, String key,
                             Object[] parameters) {

        String message = message(context, key);
        if ((parameters == null) || (parameters.length < 1)) {
            return message;
        }
        return message(context.getViewRoot().getLocale(), message, parameters);

    }


    /**
     * <p>Use the specified message as a message format pattern, substitute
     * in the specified parameter values, and return the resulting string.</p>
     *
     * @param locale Locale for performing parameter replacement
     * @param message Message format pattern string
     * @param parameters Replacement parameters to substitute in to
     *  the message format pattern
     */
    protected String message(Locale locale, String message, Object[] parameters) {

        MessageFormat format =
          new MessageFormat(message, locale);
        return format.format(parameters);

    }


}
