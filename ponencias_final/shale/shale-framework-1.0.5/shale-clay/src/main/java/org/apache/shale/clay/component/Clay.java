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
 * $Id: Clay.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.component;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.component.chain.AbstractCommand;
import org.apache.shale.clay.component.chain.ClayContext;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.util.Messages;

/**
 * This component grafts a subview onto the current JSF view.
 *
 */
public class Clay extends UIComponentBase {

    /**
     * <p>
     * Commons logger utility class instance.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(Clay.class);
    }

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", Clay.class.getClassLoader());


    /**
     * <p>Holds the symbol table for the component.</p>
     */
    private Map symbols = new TreeMap();

    /**
     * <p>Returns the symbol table for the component.</p>
     *
     * @return Map of {@link org.apache.shale.clay.config.beans.SymbolBean}
     */
    public Map getSymbols() {
        return symbols;
    }

    /**
     * <p>
     * Loads the chains config using the resource defined by config file
     * <code>Globals.CLAY_RESOURCE_NAME</code>. These chain commands are used
     * to build the clay component subtree from a root {@link ComponentBean}
     * obtained for a {@link ConfigBean} instance from the
     * {@link ConfigBeanFactory}.
     * </p>
     *
     * @return {@link Catalog} instance of a catalog defined by catalog
     *         <code>Globals.CLAY_CATALOG_NAME</code>
     * @exception Exception loading catalog
     */
    protected Catalog getCatalog() throws Exception {

        // Look up the "shale" catalog, creating one if necessary
        Catalog catalog = CatalogFactory.getInstance().getCatalog(
                Globals.CLAY_CATALOG_NAME);
        if (catalog == null) {

            ConfigParser parser = new ConfigParser();
            URL url = this.getClass().getClassLoader().getResource(
                    Globals.CLAY_RESOURCE_NAME);
            if (url == null) {
                throw new IllegalArgumentException(Globals.CLAY_RESOURCE_NAME);
            }
            parser.parse(url);

            catalog = CatalogFactory.getInstance().getCatalog(
                    Globals.CLAY_CATALOG_NAME);

        }

        return catalog;

    }

    /**
     * <p>
     * Unique identifier that is used to define a simple or complex component.
     * The suffix of this identifier will be used to determine if the component
     * metadata will be loaded from a HTML style of document or a XML defining
     * document
     * </p>
     */
    private String jsfid = null;


    /**
     * <p>
     * A <code>validator</code> style of method event that is fired when
     * building the sub component tree using the <code>jsfid</code> as the key
     * defining the component metadata.
     * </p>
     */
    private String shapeValidator = null;

    /**
     * <p>
     * The root of the metadata used to build the component subtree.
     * </p>
     */
    private ComponentBean displayElementRoot = null;

    /**
     * <p>
     * Returns the unique identifier used to build the component subtree.
     * </p>
     *
     * @return jsfid
     */
    public String getJsfid() {
        return jsfid;
    }

    /**
     * <p>
     * Sets the unique identifier used to build the component subtree.
     * </p>
     *
     * @param jsfid root identifier of the sub tree
     */
    public void setJsfid(String jsfid) {
        this.jsfid = jsfid;
    }


    /**
     * <p>
     * Sets the unique identifier used to build the component subtree.
     * This property is not accessible from the JSP tag.
     * </p>
     *
     * @param jsfid alias to the jsfid property
     */
    public void setClayJsfid(String jsfid) {
        this.jsfid = jsfid;
    }

    /**
     * <p>
     * Returns the unique identifier used to build the component subtree.
     * This property is not accessible from the JSP tag.
     * </p>
     *
     * @return jsfid alias
     */
    public String getClayJsfid() {
       return jsfid;
    }


    /**
     * <p>
     * Retuns the <code>validator</code> signature event that is invoked when
     * the component metadata is retrieved.
     * </p>
     *
     * @return method binding expression
     */
    public String getShapeValidator() {
        return shapeValidator;
    }

    /**
     * <p>
     * Sets the <code>validator</code> signature event that is invoked when
     * the component metadata is retrieved.
     * </p>
     *
     * @param loadShapeAction method binding expression
     */
    public void setShapeValidator(String loadShapeAction) {
        this.shapeValidator = loadShapeAction;
    }

    /**
     * <p>
     * Returns the logical bean name that replaces any occurance of
     * "@managed-bean-name" within a binding expression.
     * </p>
     *
     * @return managed bean name symbol
     */
    public String getManagedBeanName() {
        SymbolBean symbol = (SymbolBean) symbols.get(Globals.MANAGED_BEAN_MNEMONIC);
        return ((symbol != null) ? symbol.getValue() : null);
    }

    /**
     * <p>
     * Sets the logical bean name that replaces any occurrences of
     * "@managed-bean-name" within a binding expression.
     * </p>
     *
     * @param mbeanMnemonic managed bean symbol
     */
    public void setManagedBeanName(String mbeanMnemonic) {
        SymbolBean symbol = new SymbolBean();
        symbol.setName(Globals.MANAGED_BEAN_MNEMONIC);
        symbol.setValue(mbeanMnemonic);
        symbols.put(symbol.getName(), symbol);
    }

    /**
     * <p>
     * Returns the root metadata component that is used to add to the component
     * tree. It locates the {@link ComponentBean} using the <code>jsfid</code>
     * attribute as the key. A call to the {@link ConfigBeanFactory} locates the
     * correct {@link ConfigBean} used to find the {@link ComponentBean}. </p>
     *
     * @return root config bean used to define the subtree
     */
    protected ComponentBean getRootElement() {

        ConfigBean config = ConfigBeanFactory.findConfig(getJsfid());

        if (config == null) {
            throw new NullPointerException(messages
                    .getMessage("clay.config.notloaded"));
        }

        // find the top-level display element associated with the subtree
        ComponentBean b = config.getElement(getJsfid());
        if (b == null) {
            throw new NullPointerException(messages.getMessage(
                    "clay.jsfid.notfound", new Object[] { getJsfid() }));
        }

        return b;
    }

    /**
     * <p>
     * This is where the clay component loads the root {@link ComponentBean} by
     * calling the
     * <code>getRootElement()</code> method or while invoking the <code>shapeValidator</code> callback event.
     * The subtree is created using the <code>Globals.ADD_COMPONENT_COMMAND_NAME</code> from the
     * <code>Globals.CLAY_CATALOG_NAME</code> in the <code>Globals.CLAY_RESOURCE_NAME</code>.
     *
     * @param context faces context
     * @exception IOException render error
     */
    public void encodeBegin(FacesContext context) throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeBegin(FacesContext)");
        }

        //I added a more descriptive message to the null pointer exception.
        //The challenge here is that the symbol replacement happens after the
        //HTML node has been thrown out.  Look to see if the clayJsfid can be
        //found in the forming ComponentBean.
        if (getJsfid() == null) {
            String attrClayJsfid = "null";
            ComponentBean displayElement = (ComponentBean) getAttributes().get(Globals.CLAY_RESERVED_ATTRIBUTE);
            AttributeBean attr = displayElement.getAttribute("clayJsfid");
            if (displayElement != null && (attr != null)) {
                attrClayJsfid = attr.getValue();
            }
            throw new NullPointerException(messages.getMessage("clay.jsfid.null",
                  new Object[] { attrClayJsfid}));
        }

        if (getDisplayElementRoot() == null) {
            if (!getJsfid().equals(Globals.RUNTIME_ELEMENT_ID)) {
                displayElementRoot = getRootElement();
            } else {
                displayElementRoot = new ComponentBean();
                displayElementRoot.setComponentType("javax.faces.HtmlOutputText");
                displayElementRoot.setJsfid(getJsfid());

            }

            // this callback will give a hook to override
            // or alter the root display element by
            // using the getter and setter for the displayElementRoot
            if (this.getShapeValidator() != null) {

                ClayContext clayContext = new ClayContext();

                Map symbolTable = new TreeMap();
                symbolTable.putAll(getSymbols());
                clayContext.setSymbols(symbolTable);
                //evaluate nested symbols
                AbstractCommand.realizeSymbols(clayContext);

                //resolve the literal "@managed-bean-name"
                String expr = AbstractCommand.replaceMnemonic(clayContext, getShapeValidator());
                Class[] methodSignature = {
                        javax.faces.context.FacesContext.class,
                        javax.faces.component.UIComponent.class,
                        java.lang.Object.class };

                MethodBinding binding = getFacesContext().getApplication()
                    .createMethodBinding(expr, methodSignature);

                if (log.isDebugEnabled()) {
                    log
                    .debug(messages
                            .getMessage("clay.invoke.shapeValidator"));
                }

                Object[] args = { context, this, displayElementRoot };
                binding.invoke(context, args);

            }
        }

        // Recursively build the subtree and fixup references. The root
        // of the subview will be assigned to the child private property.

        ClayContext clayContext = new ClayContext();
        clayContext.setChild(null);
        clayContext.setDisplayElement(getDisplayElementRoot());
        clayContext.setJsfid(getJsfid());
        clayContext.setFacesContext(getFacesContext());
        clayContext.setJspIds(new TreeSet());

        Map symbolTable = new TreeMap();
        symbolTable.putAll(getDisplayElementRoot().getSymbols());
        symbolTable.putAll(getSymbols());
        clayContext.setSymbols(symbolTable);

        //evaluate nested symbols
        AbstractCommand.realizeSymbols(clayContext);

        clayContext.setRootElement(getDisplayElementRoot());
        clayContext.setParent(this);

        Catalog catalog = null;
        try {
            catalog = getCatalog();
        } catch (Exception e) {
            log.error(e);
        }
        Command command = catalog
                 .getCommand(Globals.ADD_COMPONENT_COMMAND_NAME);

        try {
            command.execute(clayContext);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

    }

    /**
     * <p>
     * Recursively invokes the rendering of the sub component tree.
     * </p>
     *
     * @param child faces component
     * @param context faces context
     * @exception IOException render error
     */
    protected void recursiveRenderChildren(UIComponent child,
            FacesContext context) throws IOException {

        if (!child.getRendersChildren() || child == this) {
            Iterator ci = child.getChildren().iterator();
            while (ci.hasNext()) {
                UIComponent c = (UIComponent) ci.next();
                if (c.isRendered()) {
                    c.encodeBegin(context);

                    if (!c.getRendersChildren()) {
                        recursiveRenderChildren(c, context);
                    } else {
                        c.encodeChildren(context);
                    }

                    c.encodeEnd(context);
                    c = null;
                }
            }
        } else {
            // let the component handle iterating over the children
            child.encodeChildren(context);
        }
    }

    /**
     * <p>
     * Called by JSF, this method delegates to <code>recursiveRenderChildren</code>.
     * </p>
     *
     * @param context faces context
     * @exception IOException render error
     */
    public void encodeChildren(FacesContext context) throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeChildren(FacesContext)");
        }

        recursiveRenderChildren(this, context);

    }

    /**
     * <p>
     * Called by JSF, this method simply emits a logging statement
     * if tracing is enabled.
     * </p>
     *
     * @param context faces context
     * @exception IOException render error
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("encodeEnd(FacesContext)");
        }

    }

    /**
     * <p>
     * Returns the root {@link ComponentBean} used to build the clay subtree
     * component.
     * </p>
     *
     * @return root config bean
     */
    protected ComponentBean getDisplayElementRoot() {
        return displayElementRoot;
    }

    /**
     * <p>
     * Sets the root {@link ComponentBean} used to build the clay subtree
     * component.
     * </p>
     *
     * @param displayElementRoot root config bean
     */
    protected void setDisplayElementRoot(ComponentBean displayElementRoot) {
        this.displayElementRoot = displayElementRoot;
    }

    /**
     * <p>
     * Restores a component's state.
     * </p>
     *
     * @param context faces context
     * @param obj component state
     */
    public void restoreState(FacesContext context, Object obj) {

        final int superState = 0;
        final int jsfidState = 1;
        final int shapeValidatorState = 2;
        final int displayElementState = 3;
        final int symbolsState = 4;

        Object[] aobj = (Object[]) obj;
        super.restoreState(context, aobj[superState]);

        jsfid = ((String) aobj[jsfidState]);
        shapeValidator = ((String) aobj[shapeValidatorState]);
        displayElementRoot = ((ComponentBean) aobj[displayElementState]);
        symbols = ((Map) aobj[symbolsState]);

    }

    /**
     * <p>
     * Saves a component's state.
     * </p>
     *
     * @see javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext)
     * @param context faces context
     * @return subtree state
     */
    public Object saveState(FacesContext context) {
        final int superState = 0;
        final int jsfidState = 1;
        final int shapeValidatorState = 2;
        final int displayElementState = 3;
        final int symbolsState = 4;
        final int size = 5;

        Object[] aobj = new Object[size];
        aobj[superState] = super.saveState(context);
        aobj[jsfidState] = jsfid;
        aobj[shapeValidatorState] = shapeValidator;
        aobj[displayElementState] = displayElementRoot;
        aobj[symbolsState] = symbols;

        return aobj;
    }

    /**
     * <p>Returns <code>true</code> indicating that this
     * component renders it's children. That means JSF will
     * invoke <code>encodeChildren()</code> method.</p>
     *
     * @return <code>true</code>
     */
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * @return Returns the component's family.
     */
    public String getFamily() {
        return "org.apache.shale.clay";
    }


}
