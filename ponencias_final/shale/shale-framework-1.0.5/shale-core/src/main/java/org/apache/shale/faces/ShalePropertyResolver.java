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

package org.apache.shale.faces;

import java.beans.Beans;
import java.util.Map;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.shale.util.LoadBundle;

/**
 * <p>Shale-specific PropertyResolver for evaluating JavaServer Faces
 * value binding and method binding expressions.  The following special
 * processing is performed, based on recognizing the type of the base
 * object:</p>
 * <ul>
 * <li><strong>javax.naming.Context</strong> - Treats the property "name"
 *     as a String (or <code>javax.naming.Name</code>) suitable for passing
 *     to the <code>lookup()</code> method of the <code>Context</code>.
 *     The Context has no way to describe whether it is read only or not,
 *     so <code>isReadOnly()</code> returns <code>false</code>.</li>
 * <li><strong>org.apache.shale.util.LoadBundle</strong> - Special handling
 *     as follows, based on the requested property name:
 *     <ul>
 *     <li><code>map</code> - Delegates to the original resolver's handling
 *         of the <code>map</code> property.  This is for backwards compatibility
 *         with applications depending on this behavior from the 1.0.0
 *         version of the class.</li>
 *     <li>Any other property is considered to be a resource bundle key, which
 *         will be used to look up the corresponding value from the underlying
 *         resource bundle, using the <code>Locale</code> from the current
 *         view for selecting the appropriate translation.</li>
 *     </ul></li>
 * </ul>
 * <p>All other evaluations are delegated to the previous implementation
 * that was passed to our constructor.</p>
 *
 * $Id: ShalePropertyResolver.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class ShalePropertyResolver extends PropertyResolver {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Construct a new {@link ShalePropertyResolver} instance.</p>
     *
     *
     * @param original Original resolver to delegate to.
     */
    public ShalePropertyResolver(PropertyResolver original) {

        this.original = original;

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The original <code>PropertyResolver</code> passed to our constructor.</p>
     */
    private PropertyResolver original = null;


    // ------------------------------------------------ PropertyResolver Methods


    /**
     * <p>For a base object of type <code>Context</code>, look up and return
     * the named object corresponding to the specified property name from
     * this <code>Context</code>.</p>
     *
     * <p>(Since 1.0.1) For a base object of type <code>LoadBundle</code>,
     * treat the property expression as follows:</p>
     * <ul>
     * <li>If the property name is <code>map</code>, call the corresponding
     *     property getter and return that value.</li>
     * <li>Otherwise, treat the property name as a message key, and look up
     *     and return the corresponding value from the <code>Map</code> that
     *     is returned by the <code>getMap()</code> call.</li>
     * </ul>
     *
     * @param base Base object from which to return a property
     * @param property Property to be returned
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public Object getValue(Object base, Object property)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            Context context = (Context) base;
            try {
                if (property instanceof Name) {
                    return context.lookup((Name) property);
                } else {
                    return context.lookup(property.toString());
                }
            } catch (NameNotFoundException e) {
                // Mimic standard JSF/JSP behavior when base is a Map
                // by returning null
                return null;
            } catch (NamingException e) {
                throw new EvaluationException(e);
            }
        } else if (base instanceof LoadBundle && !"basename".equals(property)) {
            Map map = ((LoadBundle) base).getMap();
            if ("map".equals(property)) {
                return map;
            } else {
                return map.get(property);
            }
        } else {
            return original.getValue(base, property);
        }

    }


    /**
     * <p>For a base object of type <code>Context</code>, replace any previous
     * binding for the named object corresponding to the specified property
     * name into this <code>Context</code>.</p>
     *
     * <p>(Since 1.0.1) For a base object of type <code>LoadBundle</code>,
     * throw an exception since all properties of this object are read only.</p>
     *
     * @param base Base object in which to store a property
     * @param property Property to be stored
     * @param value Value to be stored
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public void setValue(Object base, Object property, Object value) {

        if (base instanceof Context) {
            Context context = (Context) base;
            try {
                // Mimic standard JSF/JSP behavior when base is a Map
                // by calling rebind() instead of bind()
                if (property instanceof Name) {
                    context.rebind((Name) property, value);
                } else {
                    context.rebind(property.toString(), value);
                }
            } catch (NamingException e) {
                throw new EvaluationException(e);
            }
        } else if (base instanceof LoadBundle && !"basename".equals(property)) {
            throw new PropertyNotFoundException("" + value);
        } else {
            original.setValue(base, property, value);
        }

    }


    /**
     * <p>For a <code>Context</code> base object, arbitrarily return
     * <code>false</code> because we cannot determine if a <code>Context</code>
     * is read only or not.</p>
     *
     * <p>(Since 1.0.1) For a <code>LoadBundle</code> base object,
     * return <code>true</code> because all pseudo-properties of
     * this bundle are considered to be read only.</p>
     *
     * @param base Base object from which to return read only state
     * @param property Property to be checked
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public boolean isReadOnly(Object base, Object property)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            // Mimic standard JSF/JSP behavior when base is a Map
            // by returning false if we cannot tell any better
            return false;
        } else if (base instanceof LoadBundle && !"basename".equals(property)) {
            // All properties of this object are considered read only
            return true;
        } else {
            return original.isReadOnly(base, property);
        }

    }


    /**
     * <p>For a <code>Context</code>, look up and return the type of the
     * named object corresponding to the specified property name from this
     * <code>Context</code>.</p>
     *
     * <p>(Since 1.0.1) For a <code>LoadBundle</code>, look up and return
     * the corresponding object type at runtime, or return <code>Object</code>
     * for the type to be looked up at design time.</p>
     *
     * @param base Base object from which to return a property type
     * @param property Property whose type is to be returned
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public Class getType(Object base, Object property)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            Context context = (Context) base;
            Object value;
            try {
                if (property instanceof Name) {
                    value = context.lookup((Name) property);
                } else {
                    value = context.lookup(property.toString());
                }
            } catch (NameNotFoundException e) {
                // Mimic standard JSF/JSP behavior when base is a Map
                // by returning null
                return null;
            } catch (NamingException e) {
                throw new EvaluationException(e);
            }
            if (value == null) {
                return null;
            } else {
                return value.getClass();
            }
        } else if (base instanceof LoadBundle && !"basename".equals(property)) {
            LoadBundle lb = (LoadBundle) base;
            if ("map".equals(property)) {
                return Map.class;
            } else if (Beans.isDesignTime()) {
                return Object.class;
            } else {
                Object value = lb.getMap().get(property);
                if (value != null) {
                    return value.getClass();
                } else {
                    return null;
                }
            }
        } else {
            return original.getType(base, property);
        }

    }


    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to return a property
     * @param index Index to be returned
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public Object getValue(Object base, int index)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            return getValue(base, "" + index);
        } else if (base instanceof LoadBundle && !"basename".equals(base)) {
            return getValue(base, "" + index);
        } else {
            return original.getValue(base, index);
        }

    }


    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object into which to store a property
     * @param index Index to be stored
     * @param value Value to be stored
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public void setValue(Object base, int index, Object value)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            setValue(base, "" + index, value);
        } else if (base instanceof LoadBundle) {
            setValue(base, "" + index, value);
        } else {
            original.setValue(base, index, value);
        }

    }


    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to check a property
     * @param index Index to be checked
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public boolean isReadOnly(Object base, int index)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            return isReadOnly(base, "" + index);
        } else if (base instanceof LoadBundle) {
            return isReadOnly(base, "" + index);
        } else {
            return original.isReadOnly(base, index);
        }

    }


    /**
     * <p>Convert an index into a corresponding string, and delegate.</p>
     *
     * @param base Base object from which to return a property type
     * @param index Index whose type is to be returned
     *
     * @exception EvaluationException if an evaluation error occurs
     * @exception PropertyNotFoundException if there is no such named
     *  object in this context
     */
    public Class getType(Object base, int index)
      throws EvaluationException, PropertyNotFoundException {

        if (base instanceof Context) {
            return getType(base, "" + index);
        } else if (base instanceof LoadBundle) {
            return getType(base, "" + index);
        } else {
            return original.getType(base, index);
        }

    }


}
