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

/*
 * $Id: LoadBundle.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.shale.util.Tags;

/**
 * <p>Component counterpart of the standard loadBundle tag. Since it's a component
 * it can be used with HTML templates.</p>
 */
public class LoadBundle extends UIComponentBase {
    /**
     * Base name of the resource bundle to be loaded.
     */
    private String basename = null;

    /**
     * Name of a request scope attribute under which the resource bundle will be
     * exposed as a Map.
     */
    private String var = null;

    /**
     * @return component family, <code>null</code>
     */
    public String getFamily() {
        return null;
    }


    /**
     * <p>Shale tag helper class that contains utility methods for setting
     * component binding and method properties.</p>
     */
    private Tags tagUtils = new Tags();


    /**
     * <p>Sets the base name of the resource bundle to be loaded.</p>
     *
     * @param basename resource bundle name
     */
    public void setBasename(String basename) {
        this.basename = basename;
        if ((var != null) && (basename != null)) {
            loadBundle();
        }
    }

    /**
     * <p>Sets the name of a request scope attribute under which the resource
     * bundle will be exposed as a Map.</p>
     *
     * @param var session scoped attribute
     */
    public void setVar(String var) {
        this.var = var;
        if ((var != null) && (basename != null)) {
            loadBundle();
        }
    }

    /**
     * <p>Load the resource bundle and expose it in the request.</p>
     */
    private void loadBundle() {
        // get the current context
        FacesContext context = FacesContext.getCurrentInstance();
        if (context == null) {
            throw new NullPointerException("No faces context!");
        }

        // get the ClassLoader
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }

        // evaluate any VB expression that we were passed
        String resolvedBasename = tagUtils.evalString(basename);

        if (null == resolvedBasename || null == var) {
            throw new NullPointerException("null basename or var");
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(basename,
                context.getViewRoot().getLocale(), classLoader);
        if (null == bundle) {
            throw new NullPointerException("No ResourceBundle for " + basename);
        }

        context.getExternalContext().getRequestMap().put(var, new BundleMap(bundle));

    }

    /**
     * <p>Invokes rendering of the component.</p>
     * @param context faces context
     * @exception IOException response writer
     */
    public void encodeBegin(FacesContext context) throws IOException {
        // ensure that this component is always transient
        setTransient(true);

        super.encodeBegin(context);
    }

    /**
     * <p>Inner class that wrappers a <code>ResourceBundle</code>.</p>
     */
    private static class BundleMap implements Map {
        /**
         * <p>Wrappered resource bundle.</p>
         */
        private ResourceBundle bundle;

        /**
         * <p>Stores the bundle keys.</p>
         */
        private List values;

        /**
         * @param bundle decorated resource bundle
         */
        public BundleMap(ResourceBundle bundle) {
            this.bundle = bundle;
        }

        /**
         * @param key bundle resource key
         * @return bundle string resource value
         */
        public Object get(Object key) {
            try {
                return bundle.getObject(key.toString());
            } catch (Exception e) {
                return "???" + key + "???";
            }
        }

        /**
         * @return <code>true</code> if the bundle is empty
         */
        public boolean isEmpty() {
            return !bundle.getKeys().hasMoreElements();
        }

        /**
         * @param key bundle resource key
         * @return <code>true</code> if the bundle contains a key
         */
        public boolean containsKey(Object key) {
            return bundle.getObject(key.toString()) != null;
        }

        /**
         * @return collection of bundle resource values
         */
        public Collection values() {
            if (values == null) {
                values = new ArrayList();
                for (Enumeration enumer = bundle.getKeys(); enumer
                        .hasMoreElements();) {
                    String v = bundle.getString((String) enumer.nextElement());
                    values.add(v);
                }
            }
            return values;
        }

        /**
         * @return number of keys in the bundle
         */
        public int size() {
            return values().size();
        }

        /**
         * @param value bundle value string
         * @return <code>true</code> if value is found in the bundle
         */
        public boolean containsValue(Object value) {
            return values().contains(value);
        }

        /**
         * @return set of objects implementing <code>Map.Entry</code>
         */
        public Set entrySet() {
            Set set = new HashSet();
            for (Enumeration enumer = bundle.getKeys(); enumer
                    .hasMoreElements();) {
                final String k = (String) enumer.nextElement();
                set.add(new Map.Entry() {
                    public Object getKey() {
                        return k;
                    }

                    public Object getValue() {
                        return bundle.getObject(k);
                    }

                    public Object setValue(Object value) {
                        throw new UnsupportedOperationException(this.getClass()
                                .getName()
                                + " UnsupportedOperationException");
                    }
                });
            }
            return set;
        }

        /**
         * @return set of resource bundle keys
         */
        public Set keySet() {
            Set set = new HashSet();
            for (Enumeration enumer = bundle.getKeys(); enumer
                    .hasMoreElements();) {
                set.add(enumer.nextElement());
            }
            return set;
        }


        /**
         * @param key unsupported
         * @return unsupported
         */
        public Object remove(Object key) {
            throw new UnsupportedOperationException(this.getClass().getName()
                    + " UnsupportedOperationException");
        }

        /**
         * @param t unsupported
         */
        public void putAll(Map t) {
            throw new UnsupportedOperationException(this.getClass().getName()
                    + " UnsupportedOperationException");
        }

        /**
         * @param key unsupported
         * @param value unsupported
         * @return unsupported
         */
        public Object put(Object key, Object value) {
            throw new UnsupportedOperationException(this.getClass().getName()
                    + " UnsupportedOperationException");
        }

        /**
         * Unsupported <code>Map</code> method.
         */
        public void clear() {
            throw new UnsupportedOperationException(this.getClass().getName()
                    + " UnsupportedOperationException");
        }

    }
}
