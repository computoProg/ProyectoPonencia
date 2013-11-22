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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

/**
 * <p>Helper class to provide access to the properties of JavaBeans.  This
 * implementation is stateless and maintains no cached information, so
 * instances may be freely created and destroyed, with no side effects
 * (unless there are side effects from caching inside the JDK itself).</p>
 *
 * $Id: PropertyHelper.java 464373 2006-10-16 04:21:54Z rahul $
 *
 * @since 1.0.1
 */
public class PropertyHelper {


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the value of the specified property from the specified bean.</p>
     *
     * @param bean Bean whose property is to be retrieved
     * @param name Name of the property to get
     *
     * @exception PropertyNotFoundException if the bean class does not have
     *  a property with the specified name, or if the property is write only
     * @exception EvaluationException if an exception occurs while
     *  retrieving the property value
     */
    public Object getValue(Object bean, String name) {

        PropertyDescriptor descriptor = descriptor(bean, name);
        Method method = descriptor.getReadMethod();
        if (method == null) {
            throw new PropertyNotFoundException(name);
        }

        try {
            return method.invoke(bean, new Object[0]);
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getCause());
        } catch (Exception e) {
            throw new EvaluationException(e);
        }

    }


    /**
     * <p>Return <code>true</code> if the specified property is read only on
     * the underlying bean class.</p>
     *
     * @param bean Bean whose property is to be checked
     * @param name Name of the property to check
     *
     * @exception PropertyNotFoundException if the bean class does not have
     *  a property with the specified name, or if the property is write only
     * @exception EvaluationException if an exception occurs while
     *  checking the read only status
     */
    public boolean isReadOnly(Object bean, String name) {

        PropertyDescriptor descriptor = descriptor(bean, name);
        if (descriptor.getReadMethod() != null) {
            return descriptor.getWriteMethod() == null;
        }
        throw new PropertyNotFoundException(name);

    }


    /**
     * <p>Return the type of the specified property on the underlying bean
     * class.</p>
     *
     * @param bean Bean whose property is to be analyzed
     * @param name Name of the property to return the type of
     *
     * @exception PropertyNotFoundException if the bean class does not have
     *  a property with the specified name
     * @exception EvaluationException if an exception occurs while
     *  retrieving the property type
     */
    public Class getType(Object bean, String name) {

        PropertyDescriptor descriptor = descriptor(bean, name);
        return descriptor.getPropertyType();

    }


    /**
     * <p>Set the specified value on the specified property of the specified
     * bean.</p>
     *
     * @param bean Bean whose property is to be set
     * @param name Name of the property to be set
     * @param value New value for this property
     *
     * @exception PropertyNotFoundException if the bean class does not have
     *  a property with the specified name, or if the property is read only
     * @exception EvaluationException if an exception occurs while
     *  setting the property value
     */
    public void setValue(Object bean, String name, Object value) {

        PropertyDescriptor descriptor = descriptor(bean, name);
        Method method = descriptor.getWriteMethod();
        if (method == null) {
            throw new PropertyNotFoundException(name);
        }

        try {
            method.invoke(bean, new Object[] { value });
        } catch (InvocationTargetException e) {
            throw new EvaluationException(e.getCause());
        } catch (Exception e) {
            throw new EvaluationException(e);
        }

    }


    // ------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>PropertyDescriptor</code> for the specified
     * property of the specified class.</p>
     *
     * @param bean Bean whose property descriptor is to be returned
     * @param name Name of the property to retrieve a descrpitor for
     *
     * @exception PropertyNotFoundException if the bean class does not have
     *  a property with the specified name
     * @exception EvaluationException if an exception occurs introspecting
     *  the underlying bean class
     */
    private PropertyDescriptor descriptor(Object bean, String name) {

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            throw new EvaluationException(e);
        }

        PropertyDescriptor descriptors[] = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            descriptors = new PropertyDescriptor[0];
        }
        for (int i = 0; i < descriptors.length; i++) {
            if (name.equals(descriptors[i].getName())) {
                return descriptors[i];
            }
        }

        throw new PropertyNotFoundException(name);

    }


}
