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
 * $Id: ElementBuilder.java 471910 2006-11-06 22:44:56Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.ActionListenerBean;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.ConverterBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.clay.config.beans.ValidatorBean;
import org.apache.shale.clay.config.beans.ValueChangeListenerBean;
import org.apache.shale.clay.parser.Node;

/**
 * <p>This class handles building the {@link org.apache.shale.clay.config.beans.ElementBean}'s
 * from the html markup resembling the &lt;attributes&gt; node in the clay
 * DTD, http://shale.apache.org/dtds/clay-config_1_0.dtd.</p>
 */
public class ElementBuilder extends Builder {

    /**
     * <p>Common Logger utility class.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(ElementBuilder.class);
    }


    /**
     * <p>Returns the <code>jsfid</code> from the target HTML
     * {@link org.apache.shale.clay.parser.Node}.</p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        String jsfid = (String) node.getAttributes().get("jsfid");
        return jsfid;
    }

    /**
     * <p>Returns the <code>componentType</code> from the target HTML
     * {@link org.apache.shale.clay.parser.Node}.</p>
     *
     * @param node markup
     * @return component type
     */
    protected String getComponentType(Node node) {
        String componentType = (String) node.getAttributes().get("componentType");
        return componentType;
    }

    /**
     * <p>Adds a {@link org.apache.shale.clay.config.beans.ConverterBean}
     * to the <code>target</code> {@link org.apache.shale.clay.config.beans.ElementBean}
     * using the {@link org.apache.shale.clay.parser.Node} as the input source.</p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addConverter(Node node, ElementBean target) {
       ConverterBean targetConverter = new ConverterBean();

       String jsfid = getJsfid(node);
       targetConverter.setJsfid(jsfid);

       // resolve inheritance and attribute overrides
       realizeComponent(node, targetConverter);
       //attach to the target element
       target.addConverter(targetConverter);

    }


    /**
     * <p>Adds a {@link org.apache.shale.clay.config.beans.ValidatorBean}
     * to the <code>target</code> {@link org.apache.shale.clay.config.beans.ElementBean}
     * using the {@link org.apache.shale.clay.parser.Node} as the input source.</p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addValidator(Node node, ElementBean target) {
       ValidatorBean targetValidator = new ValidatorBean();

       String jsfid = getJsfid(node);
       targetValidator.setJsfid(jsfid);

       // resolve inheritance and attribute overrides
       realizeComponent(node, targetValidator);
       //attach to the target element
       target.addValidator(targetValidator);

    }

    /**
     * <p>Adds an {@link org.apache.shale.clay.config.beans.ActionListenerBean}
     * to the <code>target</code> {@link org.apache.shale.clay.config.beans.ElementBean}
     * using the {@link org.apache.shale.clay.parser.Node} as the input source.</p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addActionListener(Node node, ElementBean target) {
       ActionListenerBean targetActionListener = new ActionListenerBean();

       String jsfid = getJsfid(node);
       targetActionListener.setJsfid(jsfid);

       // resolve inheritance and attribute overrides
       realizeComponent(node, targetActionListener);
       //attach to the target element
       target.addActionListener(targetActionListener);

    }

    /**
     * <p>Adds a {@link org.apache.shale.clay.config.beans.ActionListenerBean}
     * to the <code>target</code> {@link org.apache.shale.clay.config.beans.ElementBean}
     * using the {@link org.apache.shale.clay.parser.Node} as the input source.</p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addValueChangeListener(Node node, ElementBean target) {
       ValueChangeListenerBean targetValueChangeListener = new ValueChangeListenerBean();

       String jsfid = getJsfid(node);
       targetValueChangeListener.setJsfid(jsfid);

       // resolve inheritance and attribute overrides
       realizeComponent(node, targetValueChangeListener);
       //attach to the target element
       target.addValueChangeListener(targetValueChangeListener);
    }


    /**
     * <p>Realizes the inheritance of the <code>target</code>
     * {@link org.apache.shale.clay.config.beans.ComponentBean} and
     * and then applies attributes that are optionally nested
     * under the <code>node</code>.</p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void realizeComponent(Node node, ComponentBean target) {
        // lookup the ConfigBean that handles the id
        ConfigBean config = ConfigBeanFactory.findConfig(target.getJsfid());

        try {
           //assign the parent
           config.assignParent(target);
           // resolve inheritance
           config.realizingInheritance(target);
        } catch (RuntimeException e) {
            log.error(e);
            throw new RuntimeException(
                    messages.getMessage("parser.unresolved",
                    new Object[] {node.getToken(), node.getToken().getRawText()}));
        }

        assignAttributes(node, target);

        //look for attributes
        Iterator ci = node.getChildren().iterator();
        while (ci.hasNext()) {
           Node child = (Node) ci.next();
           if (child.isWellFormed() && child.getName() != null
               && child.getName().equals("attributes")) {

                   addAttributes(child, target);
           }
        }

    }


    /**
     * <p>Looks for &lt;set/&gt; nodes within a &lt;attributes&gt; node and
     * converting them to {@link org.apache.shale.clay.config.beans.AttributeBean}'s
     * on the <code>target</code> {@link org.apache.shale.clay.config.beans.ComponentBean}.
     * </p>
     *
     * @param attributesNode markup
     * @param target child config bean
     */
    protected void addAttributes(Node attributesNode, ComponentBean target) {
        Iterator ci = attributesNode.getChildren().iterator();
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            if (child.isWellFormed() && child.getName() != null
                && child.getName().equals("set")) {

                String name = (String) child.getAttributes().get("name");
                String value = (String) child.getAttributes().get("value");
                String bindingType = (String) child.getAttributes().get("bindingType");

                AttributeBean attr = target.getAttribute(name);
                if (attr != null) {
                    createAttribute(attr, value, target);
                } else {
                    attr = new AttributeBean();
                    attr.setName(name);
                    attr.setValue(value);
                    attr.setBindingType(bindingType);
                    target.addAttribute(attr);
                }
            }
        }
    }


    /**
     * <p>Adds markup <code>symbols</code> to the <code>target</code>
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param symbolsNode markup
     * @param target child config bean
     */
    protected void addSymbols(Node symbolsNode, ElementBean target) {
        Iterator si = symbolsNode.getChildren().iterator();
        while (si.hasNext()) {
            Node child = (Node) si.next();
            if (child.isWellFormed() && child.getName() != null
                && child.getName().equals("set")) {

                String name = (String) child.getAttributes().get("name");
                String value = (String) child.getAttributes().get("value");

                if (name != null && name.length() > 0) {
                    SymbolBean symbol = new SymbolBean();
                    StringBuffer tmp = new StringBuffer(name);
                    if (tmp.charAt(0) != '@') {
                       tmp.insert(0, '@');
                    }

                    symbol.setName(tmp.toString());
                    symbol.setValue(value);
                    target.addSymbol(symbol);
                }
            }
        }
    }



    /**
     * <p>Handles converting markup resembling the &lt;element&gt; node
     * in the clay DTD, http://shale.apache.org/dtds/clay-config_1_0.dtd,
     * to the target {@link org.apache.shale.clay.config.beans.ElementBean}.</p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        super.encodeBegin(node, target, root);

        List deleteList = new ArrayList();
        Iterator ci = node.getChildren().iterator();
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            if (child.isWellFormed() && child.getName() != null) {
                if (child.getName().equals("attributes")) {
                    addAttributes(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("symbols")) {
                    addSymbols(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("converter")) {
                    addConverter(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("validator")) {
                    addValidator(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("actionListener")) {
                    addActionListener(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("valueChangeListener")) {
                    addValueChangeListener(child, target);
                    deleteList.add(child);
                }
            } else {
                if (child.isComment() || isNodeWhitespace(child)) {
                    // remove white space
                    deleteList.add(child);
                }
            }
        }

        ci = deleteList.iterator();
        while (ci.hasNext()) {
            node.getChildren().remove(ci.next());
        }

    }


    /**
     * <p>This method is overridden to look for a <code>renderId</code>
     * attribute in the {@link org.apache.shale.clay.parser.Node}.
     * If one exists, it is applied to the target
     * {@link org.apache.shale.clay.config.beans.ElementBean}. The
     * super class {@link Builder} generates a unique id by default.
     * The clay namespace HTML nodes can override the renderId to
     * allow overridding of nested elements.</p>
     *
     * @param node markup
     * @return config bean
     */
    public ElementBean createElement(Node node) {
        ElementBean target = super.createElement(node);
        String renderId = (String) node.getAttributes().get("renderId");
        if (renderId != null) {
           Integer id = null;
           try {
            id = Integer.valueOf(renderId);
           } catch (NumberFormatException e) {
               log.error(e);
               throw new RuntimeException(
                       messages.getMessage("parser.unresolved",
                       new Object[] {node.getToken(), node.getToken().getRawText()}));
           }
           if (id != null) {
              target.setRenderId(id.intValue());
           }
        }

        return target;
    }

    /**
     * <p>
     * This override returns <code>true</code> indicating that the from JSF
     * component can have children.
     * </p>
     *
     * @return <code>true</code>
     */
    public boolean isChildrenAllowed() {
        return true;
    }


    /**
     * <p>
     * This method resolves the <code>jsfid</code> attribute for an HTML
     * element to a component definition in the XML configuration files.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void assignNode(Node node, ElementBean target) {
        //the name attribute can not be used here because of the conflict with
        //the param tag.
        String id = (String) node.getAttributes().get("id");
        target.setId(id);

        // look to see if this node should be bound to a component
        if (target.getJsfid() != null) {
            // lookup the ConfigBean that handles the id
            ConfigBean config = ConfigBeanFactory.findConfig(target.getJsfid());
            // disconnect component type
            target.setComponentType(null);


            try {
               //assign the parent
               config.assignParent(target);
               // resolve inheritance
               config.realizingInheritance(target);
            } catch (RuntimeException e) {
                log.error(e);
                throw new RuntimeException(
                        messages.getMessage("parser.unresolved",
                        new Object[] {node.getToken(), node.getToken().getRawText()}));
            }

            // if the inheritance is broken, toggle back to the default
            if (target.getComponentType() == null) {
                target.setComponentType(this.getComponentType(node));
            }

        }

        // HTML attributes will override the declarative component
        assignAttributes(node, target);

    }


    /**
     * <p>Test the value of the node and returns <code>true</code> if
     * the value is only whitespace.</p>
     *
     * @param node markup node
     * @return <code>true</code> if value of the node is only whitespace
     */
    protected boolean isNodeWhitespace(Node node) {
        StringBuffer document = node.getToken().getDocument();
        for (int i = node.getToken().getBeginOffset();
             i < node.getToken().getEndOffset(); i++) {
           char c = document.charAt(i);
           if (!Character.isWhitespace(c)) {
               return false;
           }
        }
        return true;
    }

}
