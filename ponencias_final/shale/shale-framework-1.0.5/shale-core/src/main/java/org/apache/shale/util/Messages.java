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

package org.apache.shale.util;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Utility wrapper around resource bundles that provides locale-specific
 * message string lookups, as well as parameter replacement services.</p>
 *
 * <p>If desired, this class can be used to define a managed bean wrapping
 * a specified resource bundle, with a declaration like this in a
 * <code>faces-config.xml</code> configuration file:</p>
 * <pre>
 *    &lt;managed-bean&gt;
 *      &lt;managed-bean-name&gt;messages&lt;/managed-bean-name&gt;
 *      &lt;managed-bean-class&gt;
 *        org.apache.shale.util.Messages
 *      &lt;/managed-bean-class&gt;
 *      &lt;managed-bean-scope&gt;application&lt;/managed-bean-scope&gt;
 *      &lt;managed-property&gt;
 *        &lt;property-name&gt;name&lt;/property-name&gt;
 *        &lt;value&gt;com.mycompany.mypackage.Bundle&lt;/value&gt;
 *      &lt;/managed-property&gt;
 *    &lt;/managed-bean&gt;
 * </pre>
 *
 * $Id: Messages.java 481403 2006-12-01 21:27:54Z rahul $
 */
public class Messages {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an initialized {@link Messages} wrapper.  At least the
     * <code>name</code> property must be initialized before the message
     * retrieval public methods may be successfully utilized.</p>
     */
    public Messages() {

        this(null, null);

    }


    /**
     * <p>Construct a new {@link Messages} wrapper around the specified
     * resource bundle name, loaded by the default class loader.</p>
     *
     * @param name Name of the requested <code>ResourceBundle</code>
     */
    public Messages(String name) {

        this(name, null);

    }


    /**
     * <P>Construct a new {@link Messages} wrapper around the specified
     * resource bundle name, loaded by the specified class loader.</p>
     *
     * @param name Name of the requested <code>ResourceBundle</code>
     * @param cl <code>ClassLoader</code> to use for loading this
     *  resource bundle, or <code>null</code> for the default (which
     *  selects the thread context class loader)
     */
    public Messages(String name, ClassLoader cl) {

        this.name = name;
        this.cl = cl;

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Set of localized <code>ResourceBundle</code> instances we have ever
     * retrieved, keyed by <code>Locale</code>.</p>
     */
    private Map bundles = new HashMap();


    /**
     * <p>The default <code>Locale</code> for this server.</p>
     */
    private Locale defaultLocale = Locale.getDefault();


    /**
     * <p><code>MessageFormat</code> used to perform parameter substitution.</p>
     */
    private MessageFormat format = new MessageFormat("");


    /**
     * <p>Log instance for this class.</p>
     */
    private transient Log log = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p><code>ClassLoader</code> from which to load the specfied
     * resource bundle, or <code>null</code> for the thread context
     * class loader.</p>
     */
    private ClassLoader cl = null;


    /**
     * <p>Return the <code>ClassLoader</code> from which to load the
     * specified resource bundle, or <code>null</code> for the thread
     * context class loader.</p>
     */
    public ClassLoader getClassLoader() {

        return this.cl;

    }


    /**
     * <p>Set the <code>ClassLoader</code> from which to load the
     * specified resource bundle.</p>
     *
     * @param cl The new class loader, or <code>null</code> for the
     *  thread context class loader
     */
    public void setClassLoader(ClassLoader cl) {

        this.cl = null;
        reset();

    }


    /**
     * <p>Name of the resource bundle to be retrieved.</p>
     */
    private String name = null;


    /**
     * <p>Return the name of the resource bundle to be retrieved.</p>
     */
    public String getName() {

        return this.name;

    }


    /**
     * <p>Set the name of the resource bunde to be retrieved.</p>
     *
     * @param name New name of the resource bundle to be retrieved
     */
    public void setName(String name) {

        this.name = name;
        reset();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Retrieve the specified message string for the default locale.  If no
     * message can be found, return <code>null</code>.</p>
     *
     * @param key Key to the message string to look up
     */
    public String getMessage(String key) {

        return getMessage(key, defaultLocale);

    }


    /**
     * <p>Retrieve the specified message string for the default locale, and
     * perform parameter substitution with the specified parameters.  If no
     * message can be found, return <code>null</code>.</p>
     *
     * @param key Key to the message string to look up
     * @param params Parameter replacement values
     */
    public String getMessage(String key, Object params[]) {

        return getMessage(key, defaultLocale, params);

    }


    /**
     * <p>Retrieve the specified message string for the specified locale.  If no
     * message can be found, return <code>null</code>.</p>
     *
     * @param key Key to the message string to look up
     * @param locale Locale used to localize this message
     */
    public String getMessage(String key, Locale locale) {

        ResourceBundle rb = getBundle(locale);
        try {
            return rb.getString(key);
        } catch (MissingResourceException e) {
            if (log().isWarnEnabled()) {
                log().warn("Key " + key + " was not found in resource bundle '" +
                           getName() + "' for locale '" + locale + "'");
            }
            return null;
        }

    }


    /**
     * <p>Retrieve the specified message string for the specified locale, and
     * perform parameter substitution with the specified parameters.  If no
     * message can be found, return <code>null</code>.</p>
     *
     * @param key Key to the message string to look up
     * @param locale Locale used to localize this message
     * @param params Parameter replacement values
     */
    public String getMessage(String key, Locale locale, Object params[]) {

        String message = getMessage(key, locale);
        if ((message == null) || (params == null) || (params.length < 1)) {
            return message;
        }
        synchronized (format) {
            format.applyPattern(message);
            message = format.format(params);
        }
        return message;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the localized <code>ResourceBundle</code> for the specified
     * <code>Locale</code>.</p>
     *
     * @param locale Locale used to select the appropriate resource bundle
     */
    private ResourceBundle getBundle(Locale locale) {

        assert locale != null;
        ResourceBundle rb = null;
        ClassLoader rbcl = cl;
        if (rbcl == null) {
            rbcl = Thread.currentThread().getContextClassLoader();
        }
        synchronized (bundles) {
            rb = (ResourceBundle) bundles.get(locale);
            if (rb == null) {
                try {
                    rb = ResourceBundle.getBundle(name, locale, rbcl);
                } catch (MissingResourceException e) {
                    rb = ResourceBundle.getBundle(name, defaultLocale, rbcl);
                }
                if (rb == null) {
                    if (log().isWarnEnabled()) {
                        log().warn("Resource bundle '" + getName() +
                                   "' was not found for locale '" + locale + "'");
                    }
                } else {
                    bundles.put(locale, rb);
                }
            }
            return rb;
        }

    }


    /**
     * <p>Return the <code>Log</code> instance for this class.</p>
     */
    private Log log() {

        if (log == null) {
            log = LogFactory.getLog(Messages.class);
        }
        return log;

    }


    /**
     * <p>Reset any cached <code>ResourceBundle</code> instances due to a
     * change in one of the relevant properties.</p>
     */
    private void reset() {

        synchronized (bundles) {
            bundles.clear();
        }

    }


}
