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

import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import javax.faces.component.UIViewRoot;

import javax.faces.context.FacesContext;

/**
 * <p>Utility class emulating the behavior of the standard JSF Core Library
 * tag <code>&lt;f:loadBundle&gt;</code>.  This class is designed to be used
 * as a managed bean, and exposes a <code>map</code> property containing the
 * messages in the resoruce bundle specified by the <code>basename</code>
 * property, localized for the <code>Locale</code> specified on the current
 * request.</p>
 *
 * <p>A typical use of this class would be to declare a managed bean like this:</p>
 * <pre>
 *    &lt;managed-bean&gt;
 *      &lt;managed-bean-name&gt;bundle&lt;/managed-bean-name&gt;
 *      &lt;managed-bean-class&gt;
 *        org.apache.shale.util.LoadBundle
 *      &lt;/managed-bean-class&gt;
 *      &lt;managed-bean-scope&gt;request&lt;/managed-bean-scope&gt;
 *      &lt;managed-property&gt;
 *        &lt;property-name&gt;basename&lt;/property-name&gt;
 *        &lt;value&gt;com.mycompany.mypackage.Bundle&lt;/value&gt;
 *      &lt;/managed-property&gt;
 *    &lt;/managed-bean&gt;
 * </pre>
 *
 * <p>This will result in creation of a request scope object whose <code>map</code>
 * property will return a <code>Map</code> representing the localized messages for
 * the <code>com.mycompany.mypackage.Bundle</code> resource bundle.  You can look
 * up localized messages in this <code>Map</code> by evaluating a value binding
 * expression like this:</p>
 * <blockquote>
 *   <code>#{messages.['message.key']}</code>
 * </blockquote>
 * <p>where <code>message.key</code> is the key for which to retrieve a
 * localized message.</p>
 *
 * <p>(Since 1.0.1) <strong>IMPLEMENTATION NOTE</strong> - For backwards
 * compatibility in applications that utilized the 1.0.0 version of this
 * class, the following sort of expression resolves to the same value:</p>
 * <blockquote>
 *   <code>#{messages.map['message.key']}</code>
 * </blockquote>
 */
public class LoadBundle {

   // ------------------------------------------------------------- Constructors


   /** Creates a new instance of LoadBundle. */
   public LoadBundle() {
        this(null);
   }


   /** <p>Creates a new instance of LoadBundle for the specified bundle.</p>
    *
    * @param basename Base resource bundle name for this <code>LoadBundle</code>
    */
   public LoadBundle(String basename) {
       this.basename = basename;
   }


   // --------------------------------------------------------- Static Variables


   /**
    * <p>The default <code>Locale</code> for this application.</p>
    */
   private static final Locale defaultLocale = Locale.getDefault();


   // --------------------------------------------------------------- Properties


   /**
    * <p>The base resource bundle name for this <code>LoadBundle</code> instance.</p>
    */
   private String basename = null;


   /**
    * <p>Return the base resource bundle name for this <code>LoadBundle</code>
    * instance.</p>
    */
   public String getBasename() {
       return this.basename;
   }


   /**
    * <p>Set the base resource bundle name for this <code>LoadBundle</code>
    * instance.</p>
    *
    * @param basename The new base resource bundle name
    */
   public void setBasename(String basename) {
       this.basename = basename;
   }


   // ----------------------------------------------------------- Public Methods


   /**
    * <p>Return a <code>Map</code> whose keys and values represent the content
    * of the application resource bundle specified by the <code>basename</code>
    * property, localized for the <code>Locale</code> stored in the
    * <code>UIViewRoot</code> for the current request.</p>
    *
    * @exception IllegalStateException if we are not inside a Faces request,
    *  or if there is not a current view root with a valid locale
    */
   public Map getMap() throws IllegalStateException {

       // Validate our current state
       if (basename == null) {
           if (Beans.isDesignTime()) {
               return Collections.EMPTY_MAP;
           }
           throw new IllegalStateException("The 'basename' property cannot be null"); // FIXME - i18n
       }
       FacesContext context = FacesContext.getCurrentInstance();
       UIViewRoot root = null;
       Locale locale = null;
       if (context != null) {
           root = context.getViewRoot();
       }
       if (root != null) {
           locale = root.getLocale();
       }
       if (locale == null) {
           throw new IllegalStateException("Cannot retrieve locale-specific map if there " +
                   "is not a current Faces request, containing a valid view root, with" +
                   "a Locale instance inside.");
       }

       // Look up the requested resource bundle
       final ResourceBundle bundle = getBundle(basename, locale);
       if (bundle == null) {
           throw new IllegalArgumentException
             ("No resource bundle found for base name '" + basename + "' and locale '" + locale + "'"); // FIXME - i18n
       }

       // Construct and return an immutable Map representing these contents
       Map map = new Map() {

           public void clear() {
               throw new UnsupportedOperationException();
           }

           public boolean containsKey(Object key) {
               boolean result = false;
               if (key != null) {
                   result = bundle.getObject(key.toString()) != null;
               }
               return result;
           }

           public boolean containsValue(Object value) {
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   Object val = bundle.getObject(keys.nextElement().toString());
                   if ((val != null) && val.equals(value)) {
                       return true;
                   }
               }
               return false;
           }


           public Set entrySet() {
               Map map = new HashMap();
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   String key = keys.nextElement().toString();
                   Object value = bundle.getObject(key);
                   map.put(key, value);
               }
               return map.entrySet();
           }

           public boolean equals(Object o) {
               if ((o == null) || !(o instanceof Map)) {
                   return false;
               }
               return entrySet().equals(((Map) o).entrySet());
           }

           public Object get(Object key) {
               if (key == null) {
                   return null;
               }
               try {
                   return bundle.getObject(key.toString());
               } catch (MissingResourceException e) {
                   return "???" + key.toString() + "???";
               }
           }

           public int hashCode() {
               return bundle.hashCode();
           }

           public boolean isEmpty() {
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   return false;
               }
               return true;
           }

           public Set keySet() {
               Set set = new HashSet();
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   set.add(keys.nextElement());
               }
               return set;
           }

           public Object put(Object key, Object value) {
               throw new UnsupportedOperationException();
           }

           public void putAll(Map map) {
               throw new UnsupportedOperationException();
           }

           public Object remove(Object key) {
               throw new UnsupportedOperationException();
           }

           public int size() {
               int size = 0;
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   keys.nextElement();
                   size++;
               }
               return size;
           }

           public Collection values() {
               List list = new ArrayList();
               Enumeration keys = bundle.getKeys();
               while (keys.hasMoreElements()) {
                   String key = keys.nextElement().toString();
                   list.add(bundle.getObject(key));
               }
               return list;
           }

       };
       return map;

   }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the localized <code>ResourceBundle</code> for the specified
     * <code>Locale</code>.</p>
     *
     * @param basename Base name of the resource bundle to return
     * @param locale Locale used to select the appropriate resource bundle
     */
    private ResourceBundle getBundle(String basename, Locale locale) {

        assert basename != null;
        assert locale != null;
        ClassLoader rbcl = Thread.currentThread().getContextClassLoader();
        if (rbcl == null) {
            rbcl = this.getClass().getClassLoader();
        }
        try {
            return ResourceBundle.getBundle(basename, locale, rbcl);
        } catch (MissingResourceException e) {
            return ResourceBundle.getBundle(basename, defaultLocale, rbcl);
        }

    }


}
