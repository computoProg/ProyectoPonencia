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

package org.apache.shale.tiger.faces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.tiger.config.FacesConfigConfig;
import org.apache.shale.tiger.managed.Bean;
import org.apache.shale.tiger.managed.Value;
import org.apache.shale.tiger.managed.config.ListEntriesConfig;
import org.apache.shale.tiger.managed.config.ListEntryConfig;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;
import org.apache.shale.tiger.managed.config.ManagedPropertyConfig;
import org.apache.shale.tiger.managed.config.MapEntriesConfig;
import org.apache.shale.tiger.managed.config.MapEntryConfig;
import org.apache.shale.tiger.view.faces.LifecycleListener2;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.util.Messages;
import org.apache.shale.util.PropertyHelper;

/**
 * <p>Implementation of <code>VariableResolver</code> that delegates
 * to the original handler under these circumstances:</p>
 * <ul>
 * <li>There is bean already in existence with the specified name.</li>
 * <li>There is no managed beans definition for the specified name.</li>
 * </ul>
 *
 * <p>If control is not delegated, implement the standard functionality
 * for creating managed beans (this is necessary because JSF does not
 * expose any direct API to access these capabilities), with the following
 * additions:</p>
 * <ul>
 * <li>If the specified managed bean class includes the
 *     {@link Bean} annotation, the type and scope properties
 *     will have been preconfigured from the corresponding
 *     attribute values.  (These default settings can be
 *     overridden by specifying an actual managed bean element
 *     in a <code>faces-config.xml</code> resource.)</li>
 * <li>If a field of the specified bean class includes the
 *     {@link Value} annotation, the property initialization
 *     will be preconfigured to the literal or expression
 *     specified by the <code>value</code> attribute of the
 *     annotation.  (These default settings can be overridden
 *     by specifiying an actual managed bean element, and
 *     including a property definition that sets the value.</li>
 * </ul>
 *
 * <p>FIXME - Incomplete implemetnation of standard managed beans
 * functionality in the following areas:</p>
 * <ul>
 * <li>Partial support for list entries on managed beans and managed properties.
 *     It currently works for lists, but not for arrays.</li>
 * </ul>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - There is no <code>faces-config.xml</code>
 * resource that registers this variable resolver, since we could end up with
 * ordering issues with the standard Shale variable resolver.  Therefore, the
 * standard implementation will dynamically insert an instance of this
 * resolver (below the standard instance, but above the implementation
 * provided instance) in the standard decorator chain that is implemented
 * as the JavaServer Faces runtime parses configuration resources.</p>
 */
public class VariableResolverImpl extends VariableResolver {

    /**
     * <p>Create a new <code>VariableResolverImpl</code> wrapping the
     * specified <code>VariableResolver</code>.</p>
     *
     * @param original Original <code>VariableResolver</code> to wrap
     */
    public VariableResolverImpl(VariableResolver original) {

        this.original = original;

        if (log().isInfoEnabled()) {
            log().info(messages().getMessage("variable.resolver",
                                              new Object[] { original }));
        }

    }

    // ----------------------------------------------------- Instance Variables


    /**
     * <p>Helper bean for performing conversions.</p>
     */
    private ConverterHelper convHelper = new ConverterHelper();


    /**
     * <p>Log instance for this class.</p>
     */
    private transient Log log = null;


    /**
     * <p><code>Messages</code> instance for this class.</p>
     */
    private transient Messages messages = null;


    /**
     * <p>The original <code>VariableResolver</code> to which we should
     * delegate when necessary.</p>
     */
    private VariableResolver original = null;


    /**
     * <p>Helper bean for accessing properties.</p>
     */
    private PropertyHelper propHelper = new PropertyHelper();



    // ----------------------------------------------- VariableResolver Methods


    /**
     * <p>Resolve the specified variable name, creating and initializing
     * a new managed bean if necessary.</p>
     *
     * @param context <code>FacesContext</code> used to resolve variables
     * @param name Name of the variable to be resolved
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    public Object resolveVariable(FacesContext context,
            String name) throws EvaluationException {

        if (log().isDebugEnabled()) {
            log().debug("resolveVariable(" + name + ")");
        }

        // Is there an existing bean by this name?  If so, delegate
        Object value = context.getExternalContext().getRequestMap().get(name);
        if (value == null) {
            value = context.getExternalContext().getSessionMap().get(name);
        }
        if (value == null) {
            value = context.getExternalContext().getApplicationMap().get(name);
        }
        if (value != null) {
            if (log().isTraceEnabled()) {
                log().trace("resolveVariable(" + name + ") --> existing bean, so delegate");
            }
            return original.resolveVariable(context, name);
        }

        // Do we have a managed bean definition for this name?  If not, delegate
        FacesConfigConfig config = config(context);
        if (config == null) {
            if (log().isTraceEnabled()) {
                log().trace("resolveVariable(" + name + ") --> no FacesConfigConfig, so delegate");
            }
            return original.resolveVariable(context, name);
        }
        ManagedBeanConfig mb = config.getManagedBean(name);
        if (mb == null) {
            if (log().isTraceEnabled()) {
                log().trace("resolveVariable(" + name + ") --> no ManagedBeanConfig, so delegate");
            }
            return original.resolveVariable(context, name);
        }

        // Configure and return a new managed bean
        Object created = create(context, mb);
        return created;

    }


    // -------------------------------------------------------- Private Methods


    /**
     * <p>The {@link FacesConfigConfig} entry containing our managed bean
     * definitions, lazily instantiated.</p>
     */
    private FacesConfigConfig config = null;


    /**
     * <p>Return the {@link FacesConfigConfig} bean describing the
     * configuration information for this applicaiton, if any.</p>
     *
     * @param context FacesContext for the current request
     */
    private FacesConfigConfig config(FacesContext context) {

        if (config == null) {
            config = (FacesConfigConfig)
              context.getExternalContext().getApplicationMap().
              get(LifecycleListener2.FACES_CONFIG_CONFIG);
        }
        return config;

    }


    /**
     * <p>Create, configure, and return a new instance based on the
     * specified managed bean, after storing it in the configured
     * scope (if any).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mb ManagedBeanConfig describing the bean to be created
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private Object create(FacesContext context, ManagedBeanConfig mb)
        throws EvaluationException {

        if (log().isDebugEnabled()) {
            log().debug("create(" + mb.getName() + ")");
        }

        // Instantiate a new instance of the appropriate bean class
        Object instance = instance(context, mb);

        // Configure properties as necessary
        for (ManagedPropertyConfig mp : mb.getProperties().values()) {
            property(context, mb, mp, instance);
        }

        // Configure list entries as necessary
        ListEntriesConfig listEntries = mb.getListEntries();
        if (listEntries != null) {
            // FIXME - arrays are not yet supported
            if (!List.class.isAssignableFrom(instance.getClass())) {
                throw new EvaluationException(messages().
                    getMessage("list.list",
                               context.getViewRoot().getLocale(),
                               new Object[] { instance.getClass().getName() }));
            }
            list(context, listEntries, (List) instance);
        }

        // Configure map entries as necessary
        MapEntriesConfig mapEntries = mb.getMapEntries();
        if (mapEntries != null) {
            if (!Map.class.isAssignableFrom(instance.getClass())) {
                throw new EvaluationException(messages().
                    getMessage("map.map",
                               context.getViewRoot().getLocale(),
                               new Object[] { instance.getClass().getName() }));
            }
            map(context, mapEntries, (Map) instance);
        }

        // Place the bean into a scope, if necessary
        scope(context, mb, instance);

        return instance;

    }


    /**
     * <p>Create and return an instance of the class named by the
     * specified managed bean configuration bean.
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mb {@link ManagedBeanConfig} defining the bean to create
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private Object instance(FacesContext context, ManagedBeanConfig mb)
        throws EvaluationException {

        // Is there actually a class name available for us to use?
        if (mb.getType() == null) {
            throw new EvaluationException(messages().
                getMessage("variable.type",
                    context.getViewRoot().getLocale(),
                    new Object[] { mb.getName() }));
        }

        // Instantiate an instance of the appropriate class
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class clazz = null;
        Object instance = null;
        try {
            clazz = cl.loadClass(mb.getType());
            instance = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new EvaluationException(messages().
                getMessage("variable.class",
                           context.getViewRoot().getLocale(),
                           new Object[] { mb.getName(), mb.getType() }), e);
        } catch (IllegalAccessException e) {
            throw new EvaluationException(messages().
                getMessage("variable.access",
                           context.getViewRoot().getLocale(),
                           new Object[] { mb.getName(), mb.getType() }), e);
        } catch (InstantiationException e) {
            throw new EvaluationException(messages().
                getMessage("variable.instantiate",
                           context.getViewRoot().getLocale(),
                           new Object[] { mb.getName(), mb.getType() }), e);
        }

        return instance;

    }


    /**
     * <p>Populate the contents of the specified <code>List</code> from the
     * specified list entries configuration information.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param config {@link ListEntriesConfig} describing this list
     * @param list <code>List</code> instance to have entries appended
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private void list(FacesContext context, ListEntriesConfig config, List list) {

        // Determine the type to which list entries should be conveted, if any
        String valueType = config.getValueType();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class type = null;
        try {
            if (valueType != null) {
                type = cl.loadClass(valueType);
            }
        } catch (ClassNotFoundException e) {
            throw new EvaluationException(messages().
                getMessage("list.class",
                           context.getViewRoot().getLocale(),
                           new Object[] { valueType }), e);
        }

        // Add a list entry for each configuration element that is present
        for (ListEntryConfig entry : config.getEntries()) {
            if (entry.isNullValue()) {
                list.add(null);
            } else if (entry.isExpression()) {
                // Evaluate the specified value binding expression
                ValueBinding vb =
                    context.getApplication().createValueBinding(entry.getValue());
                list.add(vb.getValue(context));
            } else {
                if (type != null) {
                    list.add(convHelper.asObject(context, type, entry.getValue()));
                } else {
                    list.add(entry.getValue());
                }
            }
        }

    }


    /**
     * <p>Return the <code>Log</code> instance to be used for this class,
     * instantiating a new one if necessary.</p>
     */
    private Log log() {

        if (log == null) {
            log = LogFactory.getLog(VariableResolverImpl.class);
        }
        return log;

    }


    /**
     * <p>Populate the contents of the specified <code>Map</code> from the
     * specified map entries configuration information.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param config {@link MapEntriesConfig} describing this list
     * @param map <code>Map</code> instance to have entries appended
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private void map(FacesContext context, MapEntriesConfig config, Map map) {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        // Determine the type to which map keys should be conveted, if any
        String keyType = config.getKeyType();
        Class keyClass = null;
        try {
            if (keyType != null) {
                keyClass = cl.loadClass(keyType);
            }
        } catch (ClassNotFoundException e) {
            throw new EvaluationException(messages().
                getMessage("map.keyClass",
                           context.getViewRoot().getLocale(),
                           new Object[] { keyType }), e);
        }

        // Determine the type to which map values should be conveted, if any
        String valueType = config.getValueType();
        Class valueClass = null;
        try {
            if (valueType != null) {
                valueClass = cl.loadClass(valueType);
            }
        } catch (ClassNotFoundException e) {
            throw new EvaluationException(messages().
                getMessage("map.valueClass",
                           context.getViewRoot().getLocale(),
                           new Object[] { valueType }), e);
        }

        // Add a map key/value pair for each configuration element that is present
        for (MapEntryConfig entry : config.getEntries()) {
            Object key = null;
            if (keyClass != null) {
                key = convHelper.asObject(context, keyClass, entry.getKey());
            } else {
                key = entry.getKey();
            }
            if (entry.isNullValue()) {
                map.put(key, null);
            } else if (entry.isExpression()) {
                // Evaluate the specified value binding expression
                ValueBinding vb =
                    context.getApplication().createValueBinding(entry.getValue());
                map.put(key, vb.getValue(context));
            } else {
                if (valueClass != null) {
                    map.put(key, convHelper.asObject(context, valueClass, entry.getValue()));
                } else {
                    map.put(key, entry.getValue());
                }
            }
        }

    }


    /**
     * <p>Return the <code>Messages</code> instance to be used for this class,
     * instantiating a new one if necessary.</p>
     */
    private Messages messages() {

        if (messages == null) {
            messages = new Messages("org.apache.shale.tiger.faces.Bundle",
                                    Thread.currentThread().getContextClassLoader());
        }
        return messages;

    }


    /**
     * <p>Configure the specified property of the specified bean instance,
     * based on information from the specified managed property entry.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mb {@link ManagedBeanConfig} for the bean being scoped
     * @param mp {@link ManagedPropertyConfig} for the property being configured
     * @param instance Bean instance to be potentially placed in scope
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private void property(FacesContext context, ManagedBeanConfig mb,
                          ManagedPropertyConfig mp, Object instance)
        throws EvaluationException {

        // Configure list entries as necessary
        ListEntriesConfig listEntries = mp.getListEntries();
        if (listEntries != null) {

            // Acquire the value of the specified property from the
            // specified instance, if it exists
            Object property = null;
            try {
                property = propHelper.getValue(instance, mp.getName());
//                property = PropertyUtils.getProperty(instance, mp.getName());
            } catch (PropertyNotFoundException e) {
                ; // Fall through to creating our own list
            } catch (Exception e) {
                throw new EvaluationException(messages().
                    getMessage("list.get",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName()}), e);
            }
            if (property == null) {
                property = new ArrayList();
            }

            // FIXME - arrays are not yet supported
            if (!List.class.isAssignableFrom(property.getClass())) {
                throw new EvaluationException(messages().
                    getMessage("list.listProperty",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName(), property.getClass().getName() }));
            }

            // Accumulate the new values for the list
            list(context, listEntries, (List) property);

            // Store the value of the property
            try {
                propHelper.setValue(instance, mp.getName(), property);
//                BeanUtils.setProperty(instance, mp.getName(), property);
            } catch (Exception e) {
                throw new EvaluationException(messages().
                    getMessage("list.set",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName()}), e);
            }

            // We are through with this property, so return
            return;

        }

        // Configure map entries as necessary
        MapEntriesConfig mapEntries = mp.getMapEntries();
        if (mapEntries != null) {

            // Acquire the value of the specified property from the
            // specified instance, if it exists
            Object property = null;
            try {
                property = propHelper.getValue(instance, mp.getName());
//                property = PropertyUtils.getProperty(instance, mp.getName());
            } catch (PropertyNotFoundException e) {
                ; // Fall through to creating our own map
            } catch (Exception e) {
                throw new EvaluationException(messages().
                    getMessage("map.get",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName()}), e);
            }
            if (property == null) {
                property = new HashMap();
            }

            if (!Map.class.isAssignableFrom(property.getClass())) {
                throw new EvaluationException(messages().
                    getMessage("map.mapProperty",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName(), property.getClass().getName() }));
            }

            // Accumulate the new values for the list
            map(context, mapEntries, (Map) property);

            // Store the value of the property
            try {
                propHelper.setValue(instance, mp.getName(), property);
//                BeanUtils.setProperty(instance, mp.getName(), property);
            } catch (Exception e) {
                throw new EvaluationException(messages().
                    getMessage("map.set",
                               context.getViewRoot().getLocale(),
                               new Object[] { mp.getName(), mb.getName()}), e);
            }

            // We are through with this property, so return
            return;

        }

        // Does this managed property specify initialization?
        if ((mp.getValue() == null) && !mp.isNullValue()) {
            return;
        }

        // Acquire the value to be set (possibly requiring conversion)
        Object value = null;
        if (!mp.isNullValue()) {
            if (mp.isExpression()) {
                // Evaluate the specified value binding expression
                ValueBinding vb =
                    context.getApplication().createValueBinding(mp.getValue());
                value = vb.getValue(context);
            } else {
                // Grab the string literal version of the value to set
                value = mp.getValue();
            }
        }

        // Assign the acquired value to the specified bean property
        try {
            Class type = propHelper.getType(instance, mp.getName());
            if ((value != null) && (value instanceof String)) {
                value = convHelper.asObject(context, type, (String) value);
            }
            propHelper.setValue(instance, mp.getName(), value);
        } catch (Exception e) {
            throw new EvaluationException(messages().
                getMessage("variable.evaluate",
                           context.getViewRoot().getLocale(),
                           new Object[] { mb.getName(), mp.getName(), mp.getValue() }), e);
        }

    }


    /**
     * <p>Install the specified bean instance in the specified scope (if any).
     * </p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mb {@link ManagedBeanConfig} for the bean being scoped
     * @param instance Bean instance to be potentially placed in scope
     *
     * @exception EvaluationException if an evaluation error occurs
     */
    private void scope(FacesContext context, ManagedBeanConfig mb, Object instance)
        throws EvaluationException {

        if (log().isTraceEnabled()) {
            log().trace("Store bean " + mb.getName() + " in scope " + mb.getScope());
        }

        ExternalContext econtext = context.getExternalContext();
        String scope = mb.getScope();
        if ("request".equalsIgnoreCase(scope)) {
            econtext.getRequestMap().put(mb.getName(), instance);
        } else if ("session".equalsIgnoreCase(scope)) {
            econtext.getSessionMap().put(mb.getName(), instance);
        } else if ("application".equalsIgnoreCase(scope)) {
            econtext.getApplicationMap().put(mb.getName(), instance);
        }

    }


}
