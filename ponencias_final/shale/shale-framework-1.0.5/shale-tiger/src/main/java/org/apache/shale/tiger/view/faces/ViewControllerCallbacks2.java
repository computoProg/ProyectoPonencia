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

package org.apache.shale.tiger.view.faces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.shale.tiger.view.Destroy;
import org.apache.shale.tiger.view.Preprocess;
import org.apache.shale.tiger.view.Prerender;
import org.apache.shale.tiger.view.View;
import org.apache.shale.view.ViewController;
import org.apache.shale.view.faces.ViewControllerCallbacks;

/**
 * <p>Utility class to perform the event callbacks specified by the
 * {@link ViewController} interface.  This version will call through
 * to ViewController methods if the bean class actually implements this
 * interface, or ituses annotations to identify the relevant methods.</p>
 *
 * <p><strong>NOTE</strong> - The annotated callback methods must be
 * public, and take no arguments.  They may exist on the class of the
 * instance being passed in, or be inherited from a superclass.</p>
 *
 * $Id: ViewControllerCallbacks2.java 560353 2007-07-27 18:49:58Z gvanmatre $
 *
 * @since 1.0.1
 */
public class ViewControllerCallbacks2 extends ViewControllerCallbacks {


    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------------ Instance Variables


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Perform the <code>preprocess</code> callback on the specified
     * instance.</p>
     *
     * @param instance Bean instance on which to perform this callback
     */
    public void preprocess(Object instance) {

        if (instance instanceof ViewController) {
            try {
                ((ViewController) instance).preprocess();
            } catch (Exception e) {
                handleException(FacesContext.getCurrentInstance(), e);
            }
            return;
        }

        Method method = method(instance, Preprocess.class);
        if (method != null) {
            try {
                method.invoke(instance, new Object[0]);
            } catch (IllegalAccessException e) {
                handleException(FacesContext.getCurrentInstance(), e);
            } catch (InvocationTargetException e) {
                handleException(FacesContext.getCurrentInstance(), (Exception) e.getCause());
            }
        }

    }


    /**
     * <p>Perform the <code>prerender</code> callback on the specified
     * instance.</p>
     *
     * @param instance Bean instance on which to perform this callback
     */
    public void prerender(Object instance) {

        if (instance instanceof ViewController) {
            try {
                ((ViewController) instance).prerender();
            } catch (Exception e) {
                handleException(FacesContext.getCurrentInstance(), e);
            }
            return;
        }

        Method method = method(instance, Prerender.class);
        if (method != null) {
            try {
                method.invoke(instance, new Object[0]);
            } catch (IllegalAccessException e) {
                handleException(FacesContext.getCurrentInstance(), e);
            } catch (InvocationTargetException e) {
                handleException(FacesContext.getCurrentInstance(), (Exception) e.getCause());
            }
        }

    }


    /**
     * Checks the <code>instance</code> to determine if it is a
     * shale <code>ViewController</code>, <code>AbstractRequestBean</code>,
     * or has the <code>Destory</code> annotation.
     *
     * @param instance Bean instance on which to perform this callback
     * @return <code>true</code> if the instance implements has a <code>destroy</code> callback
     */

    public boolean isViewController(Object instance) {
        
        if (super.isViewController(instance)) {
            return true;
        }

        return (method(instance, Destroy.class) != null);
    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>The set of method annotations for callbacks of interest.</p>
     */
    private static final Class[] annotations =
    { Preprocess.class, Prerender.class, Destroy.class };



    /**
     * <p>Data structure to maintain information about annotated
     * methods.  In this map, the key is the Class being analyzed,
     * and the value is an inner map.  In the inner map, the key
     * is an Annotation class, and the value is the corresponding
     * Method instance.</p>
     */
    private transient Map<Class,Map<Class,Method>> maps =
      new HashMap<Class,Map<Class,Method>>();


    /**
     * <p>Return the <code>Method</code> to be called for the specified
     * annotation on the specified instance, if any.  If there is no such
     * method, return <code>null</code>.</p>
     *
     * @param instance Instance on which callbacks will be performed
     * @param annotation Annotation for which to return a method
     */
    private Method method(Object instance, Class annotation) {

        // Does the underlying class implement the View annotation?
        // If not, exit early
        Class clazz = instance.getClass();
        if (clazz.getAnnotation(View.class) == null) {
            return null;
        }

        synchronized (maps) {

            // If we have seen this Class already, simply return the
            // previously located Method (if any)
            Map<Class,Method> map = maps.get(clazz);
            if (map != null) {
                return map.get(annotation);
            }

            // Construct and cache a new Map identifying the
            // methods of interest for these callbacks
            map = new HashMap<Class,Method>();
            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getParameterTypes().length > 0) {
                    continue;
                }
                for (int j = 0; j < annotations.length; j++) {
                    if (methods[i].getAnnotation(annotations[j]) != null) {
                        map.put(annotations[j], methods[i]);
                    }
                }
            }
            maps.put(clazz, map);
            return map.get(annotation);

        }

    }


}
