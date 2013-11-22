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
 * $Id: ClayContext.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.chain.impl.ContextBase;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

/**
 * <p>This context is passed to all the commands in this package
 * used to create the sub component tree for the clay component.
 * The use of the context object promotes a reusable logic
 * fragments that are loosely coupled and kind of snap togather.
 * </p>
 */
public class ClayContext extends ContextBase {

    /**
     * <p>Unique serialization id.</p>
     */
    private static final long serialVersionUID = 3618132372818901298L;

    /**
     * <p>Symbol table that holds literal strings that
     * will be replaced within the value of an attribute.</p>
     */
    private Map symbols = null;

    /**
     * <p>Returns a Map containing replacement symbols
     * within meta-component attributes.</p>
     *
     * @return map of {@link org.apache.shale.clay.config.beans.SymbolBean}
     */
    public Map getSymbols() {
        if (symbols == null) {
           symbols = new TreeMap();
        }

        return symbols;
    }

    /**
     * <p>Sets a Map containing replacement symbols
     * within meta-component attributes.</p>
     *
     * @param symbols map of {@link org.apache.shale.clay.config.beans.SymbolBean}
     */
    public void setSymbols(Map symbols) {
        this.symbols = symbols;
    }


    /**
     * <p>Unique identifier for a component metadata definition.</p>
     */
    private String jsfid = null;

    /**
     * <p>Returns the unique identifier for a component metadata definition.</p>
     *
     * @return jsfid
     */
    public String getJsfid() {
        return jsfid;
    }

    /**
     * <p>Sets the unique identifier for a component metadata definition.</p>
     *
     * @param jsfid unique component identifier
     */
    public void setJsfid(String jsfid) {
        this.jsfid = jsfid;
    }

    /**
     * <p>A <strong>child</strong> component is a faces Component, Validator,
     * Listener or Converter that has a parent.  Because a {@link org.apache.shale.clay.component.Clay} component
     * is nested within a JSF/JSP page, it will should always have a parent.
     * </p>
     */
    private Object child = null;

    /**
     * <b>Returns a child component that can be a UIComponent, Validator,
     * Listener or Converter.
     * </b>
     *
     * @return child component
     */
    public Object getChild() {
        return child;
    }

    /**
     * <p>Sets a child component that can be a UIComponent, Validator,
     * Listener or Converter.
     * </p>
     *
     * @param child component
     */
    public void setChild(Object child) {
        this.child = child;
    }

    /**
     * <p>The root of a {@link org.apache.shale.clay.component.Clay} component defined by a
     * {@link org.apache.shale.clay.taglib.ClayTag} has a base metadata object of type
     * {@link org.apache.shale.clay.config.beans.ComponentBean}.  It can be defined in an XML
     * file or dynamically built from a HTML fragment or defined
     * at runtime.</p>
     */
    private ComponentBean rootElement = null;

    /**
     * <p>Returns the root config object used to construct the
     * {@link org.apache.shale.clay.component.Clay} component subtree.
     * </p>
     *
     * @return config bean
     */
    public ComponentBean getRootElement() {
        return rootElement;
    }

    /**
     * <p>Sets the root config object used to construct the
     * {@link org.apache.shale.clay.component.Clay} component subtree.
     * </p>
     *
     * @param rootElement parent config bean
     */
    public void setRootElement(ComponentBean rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * <p>An attribute that is the current index in the
     * <code>attributes</code> collection of the {@link org.apache.shale.clay.config.beans.ComponentBean}
     * object. There are five <code>Command</code> object that use the
     * <code>attribute</code> property in the context.
     * <dl>
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyActionCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyActionListenerCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValidatorCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValueChangeListenerCommand}
     * <dd>{@link org.apache.shale.clay.component.chain.PropertyValueCommand}
     * </dl>
     *
     */
    private AttributeBean attribute = null;

    /**
     * <p>Sets the current attribute for each in
     * {@link org.apache.shale.clay.component.chain.AssignPropertiesCommand}.
     * </p>
     *
     * @param attribute current attribute bean
     */
    public void setAttribute(AttributeBean attribute) {
        this.attribute = attribute;
    }

    /**
     * <p>Gets the current attribute for each in
     * {@link org.apache.shale.clay.component.chain.AssignPropertiesCommand}.
     * </p>
     *
     * @return current attribute bean
     */
    public AttributeBean getAttribute() {
        return attribute;
    }

    /**
     * <p>Represents the current component metadata used to build
     * an associated JSF resource.</p>
     */
    private ComponentBean displayElement = null;

    /**
     * <p>Returns the current component metadata used to build
     * a JSF resource.
     * </p>
     *
     * @return current config bean
     */
    public ComponentBean getDisplayElement() {
        return displayElement;
    }

    /**
     * <p>Sets the current component metadata used to build
     * a JSF resource.
     * </p>
     *
     * @param displayElement config bean
     */
    public void setDisplayElement(ComponentBean displayElement) {
        this.displayElement = displayElement;
    }

    /**
     * <p>Reference to the current {@link FacesContext}.</p>
     */
    private FacesContext facesContext = null;

    /**
     * <p>Returns the current faces Context.</p>
     *
     * @return faces context
     */
    public FacesContext getFacesContext() {
        return facesContext;
    }

    /**
     * <p>Sets the current faces Context.</p>
     *
     * @param facesContext jsf context
     */
    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     * <p>The <strong>parent</strong> of the <strong>child</strong>
     * component.  The <strong>parent</strong> can be something other
     * than a subclass of <code>UIComponent</code>.
     * </p>
     */
    private UIComponent parent = null;

    /**
     * <p>Returns the <strong>parent</strong> of the <strong>child</strong>.</p>
     *
     * @return current parent component
     */
    public UIComponent getParent() {
        return parent;
    }

    /**
     * <p>Sets the <strong>parent</strong> of the <strong>child</strong>.</p>
     *
     * @param parent current
     */
    public void setParent(UIComponent parent) {
        this.parent = parent;
    }

    /**
     * <p>Index at which a new component must be added in the list of childs.</p>
     */
    private int childIndex = 0;

    /**
     * <p>Returns the index at which a new component must be added in the list
     * of childs.</p>
     *
     * @return current child index
     */
    public int getChildIndex() {
        return childIndex;
    }

    /**
     * <p>Sets the index at which a new component must be added in the list
     * of childs.</p>
     *
     * @param index current child index
     */
    public void setChildIndex(int index) {
        childIndex = index;
    }

    /**
     * <p>Each <code>displayElement</code> is given a unique id.  This
     * set represents each {@link ComponentBean} used in the page
     * composition.</p>
     */
    private TreeSet jspIds = null;

    /**
     * @return the set that represents each <code>displayElement</code> used
     * to build the target JSF sub componennt tree
     */
    public TreeSet getJspIds() {
        return jspIds;
    }

    /**
     * @param graph the set that represents each <code>displayElement</code> used
     * to build the target JSF sub componennt tree
     */
    public void setJspIds(TreeSet graph) {
        jspIds = graph;
    }
}
