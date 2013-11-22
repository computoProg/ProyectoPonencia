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
 * $Id: Builder.java 467434 2006-10-24 18:48:48Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.Token;
import org.apache.shale.util.Messages;

/**
 * <p>
 * The abstract document node converter handles building meta components,
 * {@link org.apache.shale.clay.config.beans.ComponentBean}, for a parsed html
 * document fragment. The {@link org.apache.shale.clay.parser.Parser} loads the
 * HTML document into a tree structure of
 * {@link org.apache.shale.clay.parser.Node}. Each node in the parsed document
 * tree is mapped to a <code>Builder</code> by a a subclass of
 * {@link org.apache.shale.clay.parser.builder.chain.BuilderRuleContext}.
 * </p>
 */
public abstract class Builder {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    protected static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", Builder.class
            .getClassLoader());

    /**
     * <p>
     * Common Logger utility class.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(Builder.class);
    }

    /**
     * <p>
     * This method returns <code>true</code> if the faces component that it
     * builds allows children but the default is <code>false</code>.
     * </p>
     *
     * @return <code>false</code>
     */
    public boolean isChildrenAllowed() {
        return false;
    }

    /**
     * <p>
     * Returns a <code>jsfid</code> for the component.
     * </p>
     *
     * @param node markup
     * @return jsfid
     */
    protected abstract String getJsfid(Node node);

    /**
     * <p>
     * Returns the faces component type registered in the faces configuration or the
     * fully qualified class name for <code>ActionListeners</code> and
     * <code>ValueChangeListeners</code>.
     * </p>
     *
     * @param node markup
     * @return component type
     */
    protected abstract String getComponentType(Node node);

    /**
     * <p>
     * An integer value used to order {@link ElementBean}'s in the
     * <code>children</code> collection of a {@link ComponentBean}. When read
     * for the configuration files by the
     * {@link org.apache.shale.clay.config.ClayConfigureListener}, the
     * <code>renderId</code> is the <strong>"signature"</strong> when
     * resolving inheritance and overrides. Used here, the component structure
     * must take on the form of the HTML document.
     * </p>
     */
    private static int renderId = 0;

    /**
     * <p>
     * Returns the next generated <code>renderId</code>.
     * <p>
     *
     * @return unique id
     */
    protected synchronized int getRenderId() {
        return renderId++;
    }

    /**
     * <p>
     * Returns <code>true</code> if the builder handles converting the node's
     * children or <code>false</code> if it's handled by the parent. The
     * <code>isBodyAllowed</code> attribute can also
     * be used to override the default option. This flag allows the HTML to be
     * mocked up but the body ignored when converted into a
     * {@link ComponentBean} graph.
     * <p>
     *
     * @param node markup
     * @param target child config bean
     * @return <code>false</code> if the node's children should be ignored
     */
    protected boolean getBuildNodeBody(Node node, ElementBean target) {
        return (target != null && !target.getIsBodyAllowed());
    }

    /**
     * <p>
     * Called to being building a {@link ElementBean} from a {@link Node}. This
     * follows the JSF pattern used by the Components and Renders.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target,
            ComponentBean root) {

        assignNode(node, target);
    }

    /**
     * <p>
     * Factory method that creates a {@link AttributeBean} from the <code>original</code>
     * replacing the <code>value</code>.
     * </p>
     *
     * @param original attribute being cloned
     * @param value attribute property value override
     * @param target owner of the attribute
     * @return cloned original with the value overridden
     */
    protected AttributeBean createAttribute(AttributeBean original, String value, ComponentBean target) {
        AttributeBean attr = new AttributeBean();
        attr.setName(original.getName());
        attr.setValue(value);
        attr.setBindingType(original.getBindingType());
        target.addAttribute(attr);
        return attr;
    }




    /**
     * <p>
     * Recursively builds the clay meta component data from the parse
     * document of {@link Node}'s. A similar design pattern found in JSF.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeChildren(Node node, ElementBean target,
            ComponentBean root) {

        if (!getBuildNodeBody(node, target)) {
            Iterator ci = node.getChildren().iterator();
            while (ci.hasNext()) {
                Node child = (Node) ci.next();
                Builder childRenderer = getBuilder(child);

                ElementBean targetChild = childRenderer.createElement(child);
                root.addChild(targetChild);
                //if the child component allows children, pass it to the render as
                // the root, otherwise, add the child to the current root
                if (childRenderer.isChildrenAllowed()) {
                    childRenderer.encode(child, targetChild, targetChild);
                } else {
                    childRenderer.encode(child, targetChild, root);
                }
            }
        }

    }

    /**
     * <p>
     * This call is invoked for any final processing.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeEnd(Node node, ElementBean target,
            ComponentBean root) {

    }

    /**
     * <p>
     * Factory method that creates a {@link ElementBean} from a {@link Node}.
     * </p>
     *
     * @param node markup
     * @return target config bean from the markup node
     */
    public ElementBean createElement(Node node) {

        ElementBean target = new ElementBean();
        target.setJsfid(getJsfid(node));
        if (!node.isComment() && node.isStart()) {
            String jsfid = (String) node.getAttributes().get("jsfid");
            if (jsfid != null) {
               target.setJsfid(jsfid);
            }
        }
        target.setComponentType(getComponentType(node));
        target.setRenderId(getRenderId());

        return target;
    }

    /**
     * <p>
     * The call that begins the conversion of a {@link Node} to a
     * {@link ComponentBean}. Each element in the html document will be
     * converted into a Clay meta component.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    public void encode(Node node, ElementBean target, ComponentBean root) {

        if (log.isDebugEnabled()) {
            log.debug(messages.getMessage("encode.begin", new Object[] {node}));
        }

        encodeBegin(node, target, root);
        encodeChildren(node, target, root);
        encodeEnd(node, target, root);

        if (log.isDebugEnabled()) {
            log.debug(messages.getMessage("encode.end", new Object[] {node}));
        }

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
        String id = (String) node.getAttributes().get("id");
        if (id == null) {
            id = (String) node.getAttributes().get("name");
        }

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
     * <p>
     * This method applies the HTML attribute overrides to the declarative
     * component, an object representation of the XML configuration.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void assignAttributes(Node node, ComponentBean target) {
        // override with html attributes

        Iterator ai = node.getAttributes().entrySet().iterator();
        next: while (ai.hasNext()) {
            Map.Entry e = (Map.Entry) ai.next();

            if (e.getKey().equals("id")
                || e.getKey().equals("jsfid")
                || e.getKey().equals("allowbody")
                || e.getKey().equals("facetname")) {

                continue next;
            }

            AttributeBean original = null;
            Token valueToken = (Token) e.getValue();
            if (valueToken != null) {
                original = target.getAttribute((String) e.getKey());
                if (original != null) {
                    createAttribute(original, valueToken.getRawText(), target);
                } else  {
                    //any token that is not an attribute in the target becomes a symbol
                    StringBuffer identifier = new StringBuffer((String) e.getKey());
                    identifier.insert(0, '@');
                    SymbolBean symbol = new SymbolBean();
                    symbol.setName(identifier.toString());
                    symbol.setValue(valueToken.getRawText());
                    target.addSymbol(symbol);
                }
            }
        }

        if (node.getAttributes().containsKey("allowbody")) {
           target.setAllowBody((String) node.getAttributes().get("allowbody"));
        }

        if (node.getAttributes().containsKey("facetname")) {
            target.setFacetName((String) node.getAttributes().get("facetname"));
        }


    }

    /**
     * <p>Returns the {@link org.apache.shale.clay.parser.builder.Builder} that
     * is assigned the task of converting the html node to a corresponding component
     * metadata used to construct a JSF resource.</p>
     *
     * @param node markup node
     * @return builder that maps markup to config beans
     */
    public Builder getBuilder(Node node) {
        return BuilderFactory.getRenderer(node);
    }

}
