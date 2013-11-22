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
 * $Id: ComponentConfigBean.java 511459 2007-02-25 06:38:52Z gvanmatre $
 */
package org.apache.shale.clay.config.beans;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.ClayConfigParser;
import org.apache.shale.clay.config.ClayXmlParser;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.util.Messages;
import org.xml.sax.SAXException;

/**
 * <p>This class is kind of the metadata object pool for configuration data
 *  loaded from XML files on startup in the {@link org.apache.shale.clay.config.ClayConfigureListener}
 *  by the {@link org.apache.shale.clay.config.ClayXmlParser}.  An instance of this
 *  class will be registered with the {@link ConfigBeanFactory}.
 *  </p>
 */
public class ComponentConfigBean implements ConfigBean {

    /**
     * <p>Commons logger.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(ComponentConfigBean.class);
    }

    /**
     * <p>Uses the digester to load the configuration files
     * into a object graph cached in <code>displayElements</code>.
     * </p>
     */
    protected ClayConfigParser parser = null;

    /**
     * <p>This parameter is initialized from the <code>init</code>
     * method from the <code>org.apache.shale.clay.AUTO_RELOAD_CONFIG_FILES</code> init
     * parameter in the web.xml.  The default value is <code>true</code>
     * which will trigger reloading the files when a change has occurred.
     * </p>
     */
    protected boolean isWatchDogOn = true;


    /**
     * <p>Map of {@link WatchDog} that watches the configuration files looking for changes.
     * The configuration files are defined by the {@link ConfigBean.ConfigDefinition} top level
     * interface.</p>
     */
    protected Map watchDogs = null;

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    protected static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", ComponentConfigBean.class
            .getClassLoader());

    /**
     *  <p>The suffixes used to identify that a jsfid is a template style of
     *  composition.  If it has a matching suffix, it will be handled
     *  by the {@link TemplateConfigBean} or {@link TemplateComponentConfigBean};
     *  Otherwise, it's handled by {@link ComponentConfigBean}.</p>
     */
    protected String[] suffixes = null;

    /**
     * <p>Reference to the <code>ServletContext</code>.</p>
     */
    protected transient ServletContext context = null;


    /**
     * <p>Flag that indicates the current mode is design time.
     * In design time mode, the descriptions in the clay
     * configuration files will populate the <code>description</code>
     * property of the target {@link AbstractBean}.</p>
     */
    private boolean isDesigntime = false;

    /**
     * <p>Returns <code>true</code> if the current mode
     * is design time.</p>
     *
     * @return <code>true</code> if design time
     */
    public boolean isDesigntime() {
       return isDesigntime;
    }

    /**
     * <p>Sets the design time to somthing other than
     * the default <code>false</code> value.</p>
     *
     * @param isDesigntime load config descriptions
     */
    public void setDesigntime(boolean isDesigntime) {
       this.isDesigntime = isDesigntime;
    }


    /**
     * <p>Initialization method that is passed the <code>ServletContext</code>
     * as a parameter.  Loads the <code>sufixes</code> for the ServletContext
     * initialization parameter.
     * </p>
     *
     * @param context servlet context
     */
    public  void init(ServletContext context) {
        this.context = context;

        if (suffixes == null) {
            suffixes = new String[2];

            suffixes[0] = context.getInitParameter(
                    Globals.CLAY_HTML_TEMPLATE_SUFFIX);
            if (suffixes[0] == null) {
                suffixes[0] = Globals.CLAY_DEFAULT_HTML_TEMPLATE_SUFFIX;
            }

            suffixes[1] = context.getInitParameter(
                    Globals.CLAY_XML_TEMPLATE_SUFFIX);
            if (suffixes[1] == null) {
                suffixes[1] = Globals.CLAY_DEFAULT_XML_TEMPLATE_SUFFIX;
            }

        }

        String autoReloadClayFiles = context.getInitParameter(Globals.AUTO_RELOAD_CLAY_FILES);
        if (autoReloadClayFiles != null) {
            try {
                isWatchDogOn = Boolean.valueOf(autoReloadClayFiles).booleanValue();
            } catch (RuntimeException e) {
                isWatchDogOn = false;
            }

        }

        //loads the config files
        loadConfigFiles();
    }


    /**
     * <p>Loads the {@link org.apache.shale.clay.component.Clay} configration files
     * into the <code>displayElements</code> Map.  The files are defined by the
     * <code>clay-template-suffix</code> initialization parameter in the web deployment
     * descriptor.  The default configuration file "META-INF/view-config.xml" is always
     * loaded from the shale-clay java archive.</p>
     */
    protected void loadConfigFiles() {

        parser = new ClayXmlParser();
        parser.setConfig(this);

        // grab the default config file
        StringBuffer configFiles = new StringBuffer(
                Globals.DEFAULT_CLAY_CONFIG_FILE);

        // a comma delimited value list of config files
        String param = context.getInitParameter(Globals.CLAY_CONFIG_FILES);

        // add the default config file
        if (param != null && param.trim().length() > 0) {
            configFiles.append(", ").append(param);

            log.warn(messages.getMessage("config.deprecated.param",
                     new Object[] {Globals.CLAY_CONFIG_FILES,
                                   Globals.CLAY_COMMON_CONFIG_FILES}));
        }

        // a comma delimited value list of config files
        param = context.getInitParameter(Globals.CLAY_COMMON_CONFIG_FILES);
        if (param != null && param.trim().length() > 0) {
            configFiles.append(", ").append(param);
        }

        // pass the config bean to the parser
        parser.setConfig(this);

        // map holding the resource watchers
        watchDogs = Collections.synchronizedMap(new TreeMap());

        // create the watch dog with a list of config files to look for changes
        WatchDog watchDog = new WatchDog(getConfigDefinitions(configFiles.toString()),
                Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);

        // adds the watcher to a map identified by name
        watchDogs.put(watchDog.getName(), watchDog);

        // loads the config files
        watchDog.refresh(true);

        param = null;
        configFiles = null;

    }

    /**
     * <p>Passed a comma delimited list of configuration files, this method returns
     * an array of {@link ConfigBean.ConfigDefinition} defining the files.</p>
     *
     * @param configFiles comma seperated list of config files
     * @return config definitions for the files
     */
    protected ConfigBean.ConfigDefinition[] getConfigDefinitions(String configFiles) {

        List urls = new ArrayList();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        if (classloader == null) {
            classloader = this.getClass().getClassLoader();
        }

        // convert a tokenized list of configuration files into an array of urls
        StringTokenizer tokenizer = new StringTokenizer(configFiles, ", ");
        while (tokenizer.hasMoreTokens()) {
            StringBuffer configFile = new StringBuffer(tokenizer.nextToken().trim());

            //look for a classpath prefix.
            int i = configFile.indexOf(Globals.CLASSPATH_PREFIX);
            if (i > -1) {
               configFile.delete(0, i + Globals.CLASSPATH_PREFIX.length());
            }

            try {
                if (i > -1) {
                   for (Enumeration ui = classloader.getResources(configFile.toString());
                         ui.hasMoreElements();) {
                       urls.add(ui.nextElement());
                   }
                } else {
                   URL url = context.getResource(configFile.toString());
                   if (url == null) {
                      throw new PageNotFoundException(messages.getMessage("file.notfound",
                              new Object[] {configFile.toString()}), configFile.toString());
                   }
                   urls.add(url);
                }
            } catch (IOException e) {
                log.error(e);
            }

            configFile = null;
        }
        tokenizer = null;
        classloader = null;

        ConfigBean.ConfigDefinition[] configDefs = new ConfigBean.ConfigDefinition[urls.size()];

        for (int i = 0; i < urls.size(); i++) {
            configDefs[i] = new XmlConfigDef((URL) urls.get(i));
        }

        return configDefs;
    }

    /**
     * <p>Returns the web container ServletContext.</p>
     *
     * @return servlet context
     */
    public ServletContext getServletContext() {
        return context;
    }

    /**
     * <p>Collection holding all the top-level components defined in the XML
     * config files.</p>
     */
    protected Map displayElements = null;

    /**
     * <p>Constructor initializes the <code>displayElements</code>
     * collection.</p>
     */
    public ComponentConfigBean() {
        final int size = 1000;
        displayElements = Collections.synchronizedMap(new HashMap(size));
    }

    /**
     * <p>Factory method that returns a top-level {link ComponentBean} with a
     *  matching <code>jsfid</code> or <code>null</code> if not found.
     * </p>
     *
     * @param jsfid id of component definition
     * @return component definition for the jsfid
     */
    public ComponentBean getElement(String jsfid) {
        ComponentBean element = null;
            element = (ComponentBean) displayElements.get(jsfid);
        return element;
    }

    /**
     * <p>Adds a {link ComponentBean} to the <code>displayElement</code> map collection using
     * the <code>jsfid</code> as the key.</p>
     *
     * @param obj component bean added to the map of elements
     */
    public void addChild(ComponentBean obj) {
        if (obj.getJsfid() != null) {
            displayElements.put(obj.getJsfid(), obj);
        } else {
            log.error(messages.getMessage("missing.jsfid.error",
                    new Object[] {"ComponentBean.jsfid", "ComponentConfigBean"}));
        }

    }

    /**
     * <p>This method is called on startup to resolve the meta inheritance relationships for
     * each top-level components in the <code>displayElements</code> collection.  There are
     * three steps, find parents, check for circular relationships, and realize the relationships.
     * </p>
     */
    public void resolveInheritance() {

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("resolve.inheritance.begin"));
        }

        // fixup heritage links
        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("finding.parents"));
        }

        Iterator di = displayElements.entrySet().iterator();
        while (di.hasNext()) {
            Map.Entry e = (Map.Entry) di.next();
            ComponentBean b = (ComponentBean) e.getValue();

            try {
                assignParent(b);
            } catch (RuntimeException e1) {
                log.error(messages.getMessage("finding.parents.exception"), e1);
                throw e1;
            }

            b = null;
            e = null;
        }
        di = null;

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("checking.inheritance"));
        }

        // Check for circular inheritance
        di = displayElements.entrySet().iterator();
        while (di.hasNext()) {
            Map.Entry e = (Map.Entry) di.next();
            ComponentBean b = (ComponentBean) e.getValue();

            try {
                checkCircularInheritance(b);
            } catch (RuntimeException e1) {
                log.error(
                        messages.getMessage("checking.inheritance.exception"),
                        e1);
                throw e1;
            }

            b = null;
            e = null;
        }
        di = null;

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("realizing.inheritance"));
        }

        // now realizing inheritance
        di = displayElements.entrySet().iterator();
        while (di.hasNext()) {
            Map.Entry e = (Map.Entry) di.next();
            ComponentBean b = (ComponentBean) e.getValue();

            try {
                realizingInheritance(b);
            } catch (RuntimeException e1) {
                log.error(messages.getMessage("realizing.inheritance.exception"), e1);
                throw e1;
            }

            //check to make sure that there is not a duplicate component id
            //within the same naming container.
            checkTree(b);

            b = null;
            e = null;
        }
        di = null;

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("resolve.inheritance.end"));
        }

    }

    /**
     * <p>Returns the root metadata component that is used to add to the component
     * tree.  This method might be overridden to broaden the scope to search for
     * components outside of the <code>displayElement</code> cache.</p>
     *
     * @param jsfid id of a component bean
     * @return component bean
     */
    protected ComponentBean getTopLevelElement(String jsfid) {

        // find the top-level display element associated with the subtree
        ComponentBean b = (ComponentBean) displayElements.get(jsfid);
        if (b == null) {
            throw new NullPointerException(messages.getMessage(
                    "jsfid.notfound", new Object[] { jsfid }));
        }

        return b;
    }


    /**
     * <p>Called to assign the IsA parent to the {@link ComponentBean}
     * using the <code>extends</code> attribute.
     * </p>
     *
     * @param b component bean needing isa parent assigned
     */
    public synchronized void assignParent(ComponentBean b) {


        if (b instanceof InnerComponentBean) {

            // these elements are like inner classes
            // set the extends to the element so the
            // parent can be generically resolved
            if (b.getJsfid() != null) {
                b.setExtends(b.getJsfid());
            }
        }

        // look for a meta inheritance property
        if (b.getExtends() != null) {

            // assign the parent to a top-level display element
            b.setIsAParent(getTopLevelElement(b.getExtends()));
        }

        // resolve inheritance of nested components
        Iterator ci = b.getChildrenIterator();
        while (ci.hasNext()) {
            assignParent((ComponentBean) ci.next());
        }

        // resolve inheritance of converter
        if (b.getConverter() != null) {
            assignParent(b.getConverter());
        }

        // resolve inheritance of validators
        Iterator vi = b.getValidatorIterator();
        while (vi.hasNext()) {
            assignParent((ComponentBean) vi.next());
        }

        // resolve inheritance of value change listeners
        vi = b.getValueChangeListenerIterator();
        while (vi.hasNext()) {
            assignParent((ComponentBean) vi.next());
        }
        vi = null;

        // resolve inheritance of action listeners
        vi = b.getActionListenerIterator();
        while (vi.hasNext()) {
            assignParent((ComponentBean) vi.next());
        }
        vi = null;


    }

    /**
     * <p>This overload handles fixing up {@link AttributeBean}
     * inheritance.</p>
     *
     * @param a attribute needing inheritance resolved
     */
    protected void realizingInheritance(AttributeBean a) {

        // look to see if the inheritance has
        // already been resolved
        if (a.isInheritanceFinal()) {
            return;
        }

        // if no parent, nothing to do
        if (a.getIsAParent() == null) {
            return;
        }

        // traverse up to the parent and work down
        realizingInheritance(a.getIsAParent());

        // inherit attribute value
        if (a.getValue() == null) {
            a.setValue(a.getIsAParent().getValue());
        }

        // inherit late binding type
        if (a.getBindingType() == null) {
            a.setBindingType(a.getIsAParent().getBindingType());
        }

        if (a.getDescription() == null) {
            a.setDescription(a.getIsAParent().getDescription());
        }

        // set final indicator
        a.setInheritanceFinal(true);

    }

    /**
     * <p>This method is passed a {@link ComponentBean} and is
     * recursively called for each contained component.  It fixes up
     * the meta inheritance relationships.
     * </p>
     *
     * @param b component bean needing inheritance realized
     */
    public void realizingInheritance(ComponentBean b) {

        // look at the final indicator to determine
        // if inheritance has already been resolved
        if (b.isInheritanceFinal()) {
            return;
        }

        // does the node have a parent
        if (b.getIsAParent() != null) {

            // resolve the parents inheritance and
            // work down
            realizingInheritance(b.getIsAParent());

            // inherit component type
            if (b.getComponentType() == null) {
                b.setComponentType(b.getIsAParent().getComponentType());
            }

            if (b.getAllowBody() == null) {
                b.setAllowBody(b.getIsAParent().getAllowBody());
            }

            if (b.getFacetName() == null) {
                b.setFacetName(b.getIsAParent().getFacetName());
            }

            if (b.getDescription() == null) {
                b.setDescription(b.getIsAParent().getDescription());
            }

            // inherit parents attributes
            Iterator pi = b.getIsAParent().getAttributeIterator();
            while (pi.hasNext()) {
                AttributeBean a = (AttributeBean) pi.next();
                if (a != null) {
                    // if the parent has and attribute the child doesn't
                    // then the child inherits it
                    if ((a.getName() != null)
                        && (!b.getAttributes().containsKey(a.getName()))) {
                        b.getAttributes().put(a.getName(), a);
                    } else if (
                            (a.getName() != null)
                            && (b.getAttributes().containsKey(a.getName()))) {
                        // if the parent has an attribute, let the child
                        // attribute inherit properties of the attribute
                        AttributeBean ca =
                                (AttributeBean) b.getAttributes().get(a.getName());
                        ca.setIsAParent(a);
                        realizingInheritance(ca);
                    }

                }
                a = null;
            }
            pi = null;

            //inherit symbols
            pi = b.getIsAParent().getSymbols().entrySet().iterator();
            while (pi.hasNext()) {
               Map.Entry e = (Map.Entry) pi.next();
               if (!b.getSymbols().containsKey(e.getKey())) {
                  b.getSymbols().put(e.getKey(), e.getValue());
               }
            }

            //inherit elements from the parent.  elements are identified/ordered by
            //the renderid in the set.
            TreeSet tmp = new TreeSet();
            // get the parents children/elements
            Iterator ci = b.getIsAParent().getChildrenIterator();
            while (ci.hasNext()) {
                ComponentBean c = (ComponentBean) ci.next();
                // the parent of a element can only be a display element
                // and a display element must have or inherit a component type
                if (c.getComponentType() == null) {
                    throw new NullPointerException(messages.getMessage("missing.componentType.exception",
                            new Object[] {c}));
                }

                // if the child node doesn't contain the parent's
                // element identified by renderId then add it to a
                // temp set
                if (!b.getChildren().contains(c)) {
                    tmp.add(c);
                }

                c = null;
            }

            // merge the delta of parent elements not found in the
            // child's element set into the child's element set
            b.setChildren(tmp);
            tmp = null;
            ci = null;

        }

        // at this point, the component id should be defined or inherited
        if (b.getComponentType() == null) {
            throw new NullPointerException(messages.getMessage("missing.componentType.exception",
                    new Object[] {b}));
        }

        // iterate thru the inherited set of elements resolving
        // their inheritance
        Iterator ci = b.getChildren().iterator();
        while (ci.hasNext()) {
            ComponentBean c = (ComponentBean) ci.next();
            realizingInheritance(c);
            if (c.getComponentType() == null) {
                throw new NullPointerException(messages.getMessage("missing.componentType.exception",
                        new Object[] {c}));
            }
            c = null;
        }
        ci = null;

        // inherit converter from parent
        if (b.getConverter() == null && b.getIsAParent() != null
                && b.getIsAParent().getConverter() != null) {
            b.addConverter((ConverterBean) b.getIsAParent().getConverter());
        }

        // resolve the inheritance of a nested converter
        if (b.getConverter() != null) {
            realizingInheritance(b.getConverter());
        }

        // inheritance of all parent validators
        if (b.getIsAParent() != null) {
           Iterator vi = b.getIsAParent().getValidatorIterator();
           while (vi.hasNext()) {
               ComponentBean c = (ComponentBean) vi.next();
               // check to make sure the child doesn't have one
               if (!b.getValidators().contains(c)) {
                  b.addValidator((ValidatorBean) c);
               }
               c = null;
           }
           vi = null;
        }

        // resovle inheritance of all nested validators
        Iterator vi = b.getValidatorIterator();
        while (vi.hasNext()) {
            ComponentBean c = (ComponentBean) vi.next();
            realizingInheritance(c);
            c = null;
        }
        vi = null;

        // inheritance of all value change listeners
        if (b.getIsAParent() != null) {
           vi = b.getIsAParent().getValueChangeListenerIterator();
           while (vi.hasNext()) {
              ComponentBean c = (ComponentBean) vi.next();
              if (!b.getValueChangeListeners().contains(c)) {
                 b.addValueChangeListener((ValueChangeListenerBean) c);
              }
              c = null;
           }
           vi = null;
        }

        // resolve inheritance of all nested value change listeners
        vi = b.getValueChangeListenerIterator();
        while (vi.hasNext()) {
            ComponentBean c = (ComponentBean) vi.next();
            realizingInheritance(c);
            c = null;
        }
        vi = null;

        // inheritance of all action listeners
        if (b.getIsAParent() != null) {
           vi = b.getIsAParent().getActionListenerIterator();
           while (vi.hasNext()) {
              ComponentBean c = (ComponentBean) vi.next();
              if (!b.getActionListeners().contains(c)) {
                 b.addActionListener((ActionListenerBean) c);
              }
              c = null;
           }
           vi = null;
        }

        // resolve inheritance of all nested action listeners
        vi = b.getActionListenerIterator();
        while (vi.hasNext()) {
            ComponentBean c = (ComponentBean) vi.next();
            realizingInheritance(c);
            c = null;
            ci = null;
        }
        vi = null;

        // toggle on the final flag
        b.setInheritanceFinal(true);
    }

    /**
     * <p>Walks up the isA parent chain looking for circular
     * relationships.  It returns a Stack of {@link ComponentBean}
     * documenting the heritage.  A runtime exception is thrown if
     * a circular relationship is found.
     * </p>
     *
     * @param b component bean having inheritance checked
     * @return inheritance stack
     */
    protected Stack getGeneralizations(ComponentBean b) {
        Stack heritage = new Stack();
        if (!(b instanceof InnerComponentBean)) {
            heritage.push(b);
        }

        ComponentBean node = b.getIsAParent();
        while (node != null) {
            if (!heritage.contains(node)) {
                heritage.push(node);
            } else {
                // construct a error message from the heritage stack
                heritage.push(node);
                throw new RuntimeException(messages.getMessage("circular.inheritance.exception",
                        new Object[] {describeRelationships(heritage)}));
            }
            node = node.getIsAParent();
        }

        return heritage;
    }

    /**
     * <p>Walks up the hasA parent chain looking for circular
     * relationships.  It returns a Stack of {@link ComponentBean}
     * documenting the composition.  A runtime exception is thrown if
     * a circular relationship is found.
     * </p>
     *
     * @param b component bean having composition checked
     * @return stack of parents
     */
    protected Stack getAssociations(ComponentBean b) {
        Stack relationships = new Stack();
        ComponentBean node = b.getHasAParent();
        while (node != null) {
            if (!relationships.contains(node)) {
                relationships.push(node);
            } else {
                relationships.push(node);

                throw new RuntimeException(messages.getMessage("circular.composition.exception",
                        new Object[] {describeRelationships(relationships)}));
            }
            node = node.getIsAParent();
        }
        return relationships;
    }

    /**
     * <p>Returns a StringBuffer with an xpath like expression of
     * <code>jsfid</code> that describes the Stack of {@link ComponentBean}.
     * </p>
     *
     * @param heritage stack of relationships to report on
     * @return description of the stack
     */
    protected StringBuffer describeRelationships(Stack heritage) {
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < heritage.size(); i++) {
            ComponentBean node = (ComponentBean) heritage.get(i);
            if (i > 0) {
                msg.insert(0, "/");
            }
            msg.insert(0, node.getJsfid());
        }
        return msg;
    }

    /**
     * <p>Passed a {@link ComponentBean}, the method looks for several
     * types of circular inheritances.  It's recursively called for all
     * contained components, children, validators, actionListeners,
     * valueChangeListeners and Converter. A runtime exception is
     * thrown if a invalid relationship is found.
     * </p>
     *
     * @param b component bean to check
     */
    protected void checkCircularInheritance(ComponentBean b) {

        Stack associations = getAssociations(b);
        Stack generalizations = getGeneralizations(b);

        if ((b.getHasAParent() != null)
          && (b.getIsAParent() != null)
          && (b.getHasAParent() == b.getIsAParent())) {

            throw new RuntimeException(messages.getMessage("circular.child.parent.same.exception",
                    new Object[] {describeRelationships(generalizations), describeRelationships(associations) }));
        }

        if ((b.getHasAParent() != null)
        && generalizations.contains(b.getHasAParent())) {

            throw new RuntimeException(messages.getMessage("circular.child.extends.same.parent.exception",
                    new Object[] {describeRelationships(generalizations),
                            describeRelationships(getGeneralizations(b.getHasAParent()))}));
        }

        associations.clear();
        generalizations.clear();

        associations = null;
        generalizations = null;

        Iterator ci = b.getChildrenIterator();
        while (ci.hasNext()) {
            checkCircularInheritance((ComponentBean) ci.next());
        }
        ci = null;

        if (b.getConverter() != null) {
            checkCircularInheritance(b.getConverter());
        }

        Iterator vi = b.getValidatorIterator();
        while (vi.hasNext()) {
            checkCircularInheritance((ComponentBean) vi.next());
        }

        vi = b.getValueChangeListenerIterator();
        while (vi.hasNext()) {
            checkCircularInheritance((ComponentBean) vi.next());
        }

        vi = b.getActionListenerIterator();
        while (vi.hasNext()) {
            checkCircularInheritance((ComponentBean) vi.next());
        }

        vi = null;

    }

    /**
     * <p>Recursively called to unassign isA and hasA parent
     * relationships.
     * </p>
     *
     * @param b component bean
     */
    protected void unassignParent(ComponentBean b) {

        // play nicely and clean up your mess
        if (b.getIsAParent() != null) {
            unassignParent(b.getIsAParent());
        }
        b.setHasAParent(null);

        Iterator ai = b.getAttributeIterator();
        while (ai.hasNext()) {
            AttributeBean a = (AttributeBean) ai.next();
            a.setHasAParent(null);
            a.setIsAParent(null);
            a = null;
        }
        ai = null;

        Iterator ci = b.getChildrenIterator();
        while (ci.hasNext()) {
            unassignParent((ComponentBean) ci.next());
        }
        b.getChildren().clear();

        if (b.getConverter() != null) {
            unassignParent(b.getConverter());
        }
        b.addConverter(null);

        Iterator vi = b.getValidatorIterator();
        while (vi.hasNext()) {
            unassignParent((ComponentBean) vi.next());
        }
        b.getValidators().clear();

        vi = b.getValueChangeListenerIterator();
        while (vi.hasNext()) {
            unassignParent((ComponentBean) vi.next());
        }
        b.getValueChangeListeners().clear();

        vi = b.getActionListenerIterator();
        while (vi.hasNext()) {
            unassignParent((ComponentBean) vi.next());
        }
        b.getValueChangeListeners().clear();

        vi = null;

    }


    /**
     * <p>Cleans up before a group of files are reloaded.</p>
     *
     * @param watchDogName group name for a group of config files or templates
     */
    protected void clear(String watchDogName) {
        Iterator di = displayElements.entrySet().iterator();
        while (di.hasNext()) {
            Map.Entry e = (Map.Entry) di.next();
            ComponentBean b = (ComponentBean) e.getValue();

            try {
                unassignParent(b);
            } catch (RuntimeException e1) {
                // log.error(e1);
            }

            b = null;
            e = null;
        }
        di = null;

        displayElements.clear();
    }

    /**
     * <p>The destroy method is invoked to clean up resources.  By
     * dereferencing the complex graph of display elements
     * </p>
     */
    public void destroy() {
        clear(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);
        context = null;

        if (parser != null) {
            parser.setConfig(null);
        }
        parser = null;

        if (watchDogs != null) {
            Iterator wi = watchDogs.entrySet().iterator();
            while (wi.hasNext()) {
                Map.Entry e = (Map.Entry) wi.next();
                WatchDog watchDog = (WatchDog) e.getValue();
                if (watchDog != null) {
                    watchDog.destroy();
                }
            }
            watchDogs.clear();
            watchDogs = null;
        }

    }

    /**
     * <p>Called by the {@link ConfigBeanFactory} to determine if this
     * instance of {@link ConfigBean} can handle finding the {@link ConfigBean}
     * from the <code>jsfid</code>.
     *
     * @param id jsfid
     * @return <code>true</code> if the jsfid can be handled
     */
    public boolean validMoniker(String id) {

        for (int i = 0; i < suffixes.length; i++) {
           if (id.endsWith(suffixes[i])) {
               return false;
           }
        }

        return true;
    }

    /**
     * <p>Implementation of the Comparable interface. The <code>weight</code>
     * is used to determine the ordering of the registered {@link ConfigBean}
     * objects within the {@link ConfigBeanFactory}.
     * </p>
     *
     * @param config object to compare to
     * @return compares the weight of two config handlers
     */
    public int compareTo(Object config) {

        ConfigBean compConfig = (ConfigBean) config;
        if (getWeight() > compConfig.getWeight()) {
            return 1;
        } else if (getWeight() > compConfig.getWeight()) {
            return -1;
        } else {
            return 0;
        }

    }
    /**
     * <p>The weight is an attempt to make a plug-able system for
     * registering {@link ConfigBean} objects with the {@link ConfigBeanFactory}.
     * A custom implementation could be registered for a different composition
     * technique adding or overriding an existing implementation.
     * </p>
     *
     * @return <code>0</code>
     */
    public int getWeight() {
        return 0;
    }

    /**
     * <p>This class defines a single configration file that is watched for
     * changes.  In addition to the <code>URL</code> passed to the overloaded
     * constructor, the <code>lastModifed</code> date is kept as a state
     * variable.</p>
     */
    protected class XmlConfigDef implements ConfigBean.ConfigDefinition {
        /**
         * <p>The location of the config file.</p>
         */
        private URL configUrl = null;

        /**
         * <p>Date the last time the file was modified as a <code>long</code>.</p>
         */
        private long lastModified = 0;

        /**
         * <p>Overloaded constructor that requires the target config <code>URL</code>.
         *
         * @param configUrl file to load
         */
        public XmlConfigDef(URL configUrl) {
           this.configUrl = configUrl;
        }

        /**
         * <p>Returns the target configuration file url.</p>
         *
         * @return file to load
         */
        public URL getConfigUrl() {
           return configUrl;
        }

        /**
         * <p>Returns the last time the target configuration file was modified.</p>
         *
         * @return last modified timestamp of the config file
         */
        public long getLastModified() {
           return lastModified;
        }

        /**
         * <p>Sets the last time the target configuration file was modified.</p>
         *
         * @param lastModified last time the file was changed
         */
        public void setLastModified(long lastModified) {
           this.lastModified = lastModified;
        }
     }

    /**
     * <p>This inner class watches for changes in a array of {@link ConfigBean.ConfigDefinition}'s.
     * This collection defines the configuration files that the {@link org.apache.shale.clay.component.Clay}
     * component uses.</p>
     */
    protected class WatchDog {

        /**
         * <p>Name assigned to the resource watcher.</p>
         */
        private String name = null;

        /**
         * <p>Returns the name of the resource watcher.</p>
         *
         * @return the watched resource
         */
        public String getName() {
           return name;
        }

        /**
         * <p>Array of config file definitions.</p>
         */
        private ConfigBean.ConfigDefinition[] configDefs = null;

        /**
         * <p>Array of connections used to determine that the file has changed.</p>
         */
        private URLConnection[] connections = null;

        /**
         * <p>Overloaded constructor that is passed the configuration file
         * definitions as a parameter.  The name associated with the resource
         * watcher is also passed as a parameter.</p>
         *
         * @param configDefs files in the watch group
         * @param name the watch group name
         */
        public WatchDog(ConfigBean.ConfigDefinition[] configDefs, String name) {
            this.configDefs = configDefs;
            this.name = name;
        }

        /**
         * <p>This method is invoked to dereference the private
         * array of config file definitions.</p>
         */
        public void destroy() {
            close();
            for (int i = 0; i < configDefs.length; i++) {
                configDefs[i] = null;
            }

            configDefs = null;
        }

        /**
         * <p>Loads an array of <code>URLConnection</code> corresponding to the
         * <code>configDefs</code>'s.</p>
         */
        private void open() {

            if (connections != null) {
                close();
            }

            connections = new URLConnection[configDefs.length];
            for (int i = 0; i < configDefs.length; i++) {

                try {
                    connections[i] = configDefs[i].getConfigUrl()
                            .openConnection();
                } catch (IOException e) {
                    log.error(messages.getMessage("parser.load.error",
                            new Object[] { configDefs[i].getConfigUrl()
                                    .getPath() }), e);
                }
            }
        }

        /**
         * <p>Performs some extra cleanup on the open array of
         * <code>connections</code>.</p>
         */
        private void close() {
            if (connections == null) {
                return;
            }

            for (int i = 0; i < connections.length; i++) {
                connections[i] = null;
            }

            connections = null;
        }

        /**
         * <p>Iterates over the open <code>connections</code> looking
         * for files that have changed.  A <code>true</code> value is
         * returned if one of the <code>configDefs</code> has been
         * modified since last loaded.</p>
         *
         * @return <code>true</code> if the file has been modified
         */
        private boolean isDirty() {
            for (int i = 0; i < configDefs.length; i++) {
                if (configDefs[i].getLastModified() < connections[i]
                        .getLastModified()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * <p>This method is the watch dog timmer.  It's invoked to determine
         * if any of the files have changed since the last time they were loaded.
         * If a change has occured on any of the <code>configDefs</code> or
         * the <code>forceReload</code> param is <code>true</code>, all the
         * files are reloaded and the last modified date is reset in the
         * {@link ConfigBean.ConfigDefinition}. A <code>true</code> value is
         * returned if the files were refreshed.
         * </p>
         *
         * @param forceReload reload the group of config files
         * @return <code>true</code> if the group was reloaded
         */
        public synchronized boolean refresh(boolean forceReload) {

            boolean wasDirty = false;

            int i = 0;
            try {
                open();
                if (forceReload || isDirty()) {
                    wasDirty = true;
                    clear(getName());
                    for (i = 0; i < configDefs.length; i++) {

                        if (log.isInfoEnabled()) {
                            log.info(messages.getMessage("parser.load.file",
                                    new Object[] { configDefs[i].getConfigUrl()
                                            .getPath() }));
                        }

                        try {

                            configDefs[i].setLastModified(connections[i]
                                    .getLastModified());
                            parser.loadConfigFile(connections[i].getURL(), getName());

                        } catch (IOException e) {
                            log.error(messages.getMessage("parser.load.error",
                                    new Object[] { configDefs[i].getConfigUrl()
                                            .getPath() }), e);
                        } catch (SAXException e) {
                            log.error(messages.getMessage("parser.load.error",
                                    new Object[] { configDefs[i].getConfigUrl()
                                            .getPath() }), e);
                        }
                    }

                    resolveInheritance();
                }
            } finally {
                close();
            }

            return wasDirty;
        }

    }

    /**
     * <p>This method should be called from key points in the application to invoke
     * automatic reloading of the configuration files if they have been modified since
     * last reloaded.  If the <code>forceReload</code> flag is <code>true</code> the files are
     * reloaded.  A <code>true</code> return value indicates the config files
     * were reloaded.</p>
     *
     * @param forceReload reload the files
     * @return files were reloaded
     */
    public boolean refresh(boolean forceReload) {

        boolean wasDirty = false;

        WatchDog watchDog = (WatchDog) watchDogs.get(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);

        // is auto watch turned off
        if (!ComponentConfigBean.this.isWatchDogOn || watchDog == null) {
           return wasDirty;
        }

        wasDirty = watchDog.refresh(forceReload);

        watchDog = null;

        return wasDirty;
    }


    /**
     * <p>A static string array of faces component types that are naming
     * containers.</p>
     */
    protected static final String[] NAMING_CONTAINER_TYPES = {
        "javax.faces.HtmlForm",
        "javax.faces.HtmlDataTable",
        "org.apache.shale.view.Subview",
        "javax.faces.NamingContainer"};

    /**
     * <p>Checks the <code>componentType</code> against the <code>NAMING_CONTAINER_TYPES</code>
     * list to determine if it is a naming container. Component id's must be unique within a
     * naming container.  Returns a <code>true</code> value if the <code>componentType</code>
     * is a naming container; otherwise, returns <code>false</code>.</p>
     *
     * @param componentType type of the component
     * @return <code>true</code> if the component type is a naming comtainer
     */
    protected boolean isNamingContainer(String componentType) {
       boolean flag = false;
       for (int i = 0; i < NAMING_CONTAINER_TYPES.length; i++) {
          if (NAMING_CONTAINER_TYPES[i].equals(componentType)) {
             flag = true;
             break;
          }
       }

       return flag;
    }


    /**
     * <p>Recursively walks the tree of component metadata verifying
     * there is not a duplicate component id within a naming container.
     * A root {@link ComponentBean} is passed as a single parameter.
     * The overloaded <code>checkTree(List, ComponentBean)</code> is
     * invoked to process components under a naming container.</p>
     *
     * @param b root of the component tree
     */
     public void checkTree(ComponentBean b) {
        if (log.isDebugEnabled()) {
           log.debug(messages.getMessage("check.tree", new Object[] {b.getComponentType()}));
        }

        List componentIds = new ArrayList();
        checkTree(componentIds, b);
        componentIds.clear();
        componentIds = null;
     }



     /**
      * <p>Verifies there is not a duplicate component id within a naming container.
      * A list of accumulating <code>componentIds</code> and a
      * root {@link ComponentBean} is passed as parameters.  A runtime
      * exception is thrown if a duplicate id is encountered.</p>
      *
      * @param componentIds list of component id's in the naming container
      * @param b parent component bean
      */
      protected void checkTree(List componentIds, ComponentBean b) {

          //check fo duplicate component id's
          String id = b.getId();
          if (id != null && (id.indexOf('@') == -1)) {
              if (componentIds.contains(id)) {
                  throw new NullPointerException(messages.getMessage("duplicate.componentid.exception",
                          new Object[] {id, b}));
              } else {
                  componentIds.add(id);
              }
          }

          Iterator ci = b.getChildrenIterator();
          while (ci.hasNext()) {
             ComponentBean c = (ComponentBean) ci.next();
             if (isNamingContainer(c.getComponentType())) {
                checkTree(c);
             } else {
                checkTree(componentIds, c);
             }
          }

      }

}
