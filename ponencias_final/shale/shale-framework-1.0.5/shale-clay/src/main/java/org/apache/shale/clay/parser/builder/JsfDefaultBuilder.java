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
 * $Id: JsfDefaultBuilder.java 471910 2006-11-06 22:44:56Z gvanmatre $
 */
package org.apache.shale.clay.parser.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.shale.clay.config.beans.ActionListenerBean;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ConverterBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.clay.config.beans.ValidatorBean;
import org.apache.shale.clay.config.beans.ValueChangeListenerBean;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.util.Tags;

/**
 * <p>A generic builder that maps the markup node name to the jsfid.  The extends attribute
 * can be used to override the default mapping to provide meta-data inheritance.  The builder
 * handles child nodes common to JSF and shale components.</p>
 *
 */
public class JsfDefaultBuilder extends ElementBuilder {

    /**
     * <p>Utility class that helps evaluate binding expressions.</p>
     */
    private Tags tagsUtil = new Tags();

    /**
     * <p>Contains the namespace prefix that should be used to locate
     * clay configurations.  This allows the page to use any prefix
     * but still find the config element.</p>
     */
    private String prefix = null;

    /**
     * <p>Returns the namespace prefix that will be added to the
     * node name when resolving the clay config.</p>
     *
     * @return URI prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * <p>Sets the namespace preix that will override the template
     * nodeds qname.</p>
     *
     * @param prefix URI prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    /**
     * <p>Factory method that creates a {@link ElementBean} from a {@link Node}.</p>
     *
     * @param node markup
     * @return new config bean from the node
     */
    public ElementBean createElement(Node node) {
        ElementBean target = new ElementBean();
        target.setJsfid(getJsfid(node));
        target.setRenderId(getRenderId());

        return target;
    }

    /**
     * <p>Holds a cross reference of commonsValidator type to
     * a clay component config definition.</p>
     */
    private Map validatorsByType = null;
    {
      validatorsByType = new TreeMap();
      validatorsByType.put("required", "s:commonsValidatorRequired");
      validatorsByType.put("maxlength", "s:commonsValidatorMaxlength");
      validatorsByType.put("minlength", "s:commonsValidatorMinlength");
      validatorsByType.put("mask", "s:commonsValidatorMask");
      validatorsByType.put("byte", "s:commonsValidatorByte");
      validatorsByType.put("short", "s:commonsValidatorShort");
      validatorsByType.put("integer", "s:commonsValidatorInteger");
      validatorsByType.put("long", "s:commonsValidatorLong");
      validatorsByType.put("float", "s:commonsValidatorFloat");
      validatorsByType.put("double", "s:commonsValidatorDouble");
      validatorsByType.put("date", "s:commonsValidatorDate");
      validatorsByType.put("intRange", "s:commonsValidatorIntRange");
      validatorsByType.put("floatRange", "s:commonsValidatorFloatRange");
      validatorsByType.put("doubleRange", "s:commonsValidatorDoubleRange");
      validatorsByType.put("creditCard", "s:commonsValidatorCreditCard");
      validatorsByType.put("email", "s:commonsValidatorEmail");
      validatorsByType.put("url", "s:commonsValidatorUrl");
    }

    /**
     * @inheritDoc
     * @param node markup
     * @param target child config bean
     */
    protected void addConverter(Node node, ElementBean target) {
        ConverterBean targetConverter = new ConverterBean();

        //make sure we have the correct jsfid based on the namespace prefix
        Builder tmpBuilder = getBuilder(node);
        ElementBean tmp = tmpBuilder.createElement(node);

        String jsfid = tmp.getJsfid();
        targetConverter.setJsfid(jsfid);

        String converterId = (String) node.getAttributes().get("converterId");
        if (converterId != null) {
            targetConverter.setComponentType(tagsUtil.evalString(converterId));
        }

        // resolve inheritance and attribute overrides
        if (node.getAttributes().containsKey("extends") || !jsfid.equals("converter")) {
            realizeComponent(node, targetConverter);
        }

        //attach to the target element
        target.addConverter(targetConverter);

    }

    /**
     * <p>Looks for &lt;s:validatorVar/&gt; nodes within a &lt;s:commonsValidator&gt; node and
     * converting them to {@link org.apache.shale.clay.config.beans.AttributeBean}'s
     * on the <code>target</code> {@link org.apache.shale.clay.config.beans.ComponentBean}.
     * </p>
     *
     * @param attributesNode markup
     * @param target child config bean
     */
    protected void addValidatorVar(Node attributesNode, ComponentBean target) {
        Iterator ci = attributesNode.getChildren().iterator();
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            if (child.isWellFormed() && child.getName() != null
                && child.getName().equals("validatorVar")) {

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
     * @inheritDoc
     * @param node markup
     * @param target child config bean
     */
    protected void addValidator(Node node, ElementBean target) {
        ValidatorBean targetValidator = new ValidatorBean();

        //make sure we have the correct jsfid based on the namespace prefix
        Builder tmpBuilder = getBuilder(node);
        ElementBean tmp = tmpBuilder.createElement(node);

        String jsfid = tmp.getJsfid();
        targetValidator.setJsfid(jsfid);

        String validatorId = (String) node.getAttributes().get("validatorId");
        if (validatorId != null) {
            targetValidator.setComponentType(tagsUtil.evalString(validatorId));
        }

        // resolve inheritance and attribute overrides
        if (node.getAttributes().containsKey("extends") || !jsfid.equals("validator")) {
            realizeComponent(node, targetValidator);
        }

        //attach to the target element
        target.addValidator(targetValidator);

        if (node.getName().equals("commonsValidator")) {
            //You can use multiple commonsValidators per component.  The
            //jsfid is the key in the ComponentBean's validator set.
            //Override with the unique type attribute
            String type = (String) node.getAttributes().get("type");
            targetValidator.setJsfid((String) validatorsByType.get(type));

            //add support for the validatorVar JSP tag.
            addValidatorVar(node, targetValidator);
        }

    }

    /**
     * @inheritDoc
     * @param node markup
     * @param target child config bean
     */
    protected void addActionListener(Node node, ElementBean target) {
        ActionListenerBean targetActionListener = new ActionListenerBean();

        //make sure we have the correct jsfid based on the namespace prefix
        Builder tmpBuilder = getBuilder(node);
        ElementBean tmp = tmpBuilder.createElement(node);

        String jsfid = tmp.getJsfid();
        targetActionListener.setJsfid(jsfid);

        String type = (String) node.getAttributes().get("type");
        if (type != null) {
            targetActionListener.setComponentType(tagsUtil.evalString(type));
        }

        // resolve inheritance and attribute overrides
        if (node.getAttributes().containsKey("extends") || !jsfid.equals("actionListener")) {
            realizeComponent(node, targetActionListener);
        }
        //attach to the target element
        target.addActionListener(targetActionListener);

    }

    /**
     * @inheritDoc
     * @param node markup
     * @param target child config bean
     */
    protected void addValueChangeListener(Node node, ElementBean target) {
        ValueChangeListenerBean targetValueChangeListener = new ValueChangeListenerBean();

        //make sure we have the correct jsfid based on the namespace prefix
        Builder tmpBuilder = getBuilder(node);
        ElementBean tmp = tmpBuilder.createElement(node);

        String jsfid = tmp.getJsfid();
        targetValueChangeListener.setJsfid(jsfid);

        String type = (String) node.getAttributes().get("type");
        if (type != null) {
            targetValueChangeListener.setComponentType(tagsUtil.evalString(type));
        }

        // resolve inheritance and attribute overrides
        if (node.getAttributes().containsKey("extends") || !jsfid.equals("valueChangeListener")) {
            realizeComponent(node, targetValueChangeListener);
        }

        //attach to the target element
        target.addValueChangeListener(targetValueChangeListener);
    }

    /**
     * <p>Adds markup &lt;clay:symbol&gt; to the <code>target</code>
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addSymbol(Node node, ElementBean target) {
        String value = (String) node.getAttributes().get("value");
        String name = (String) node.getAttributes().get("name");
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

    /**
     * <p>Adds markup &lt;f:attribute&gt; to the <code>target</code>
     * {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addAttribute(Node node, ElementBean target) {
        String name = (String) target.getAttributes().get("name");
        String value = (String) target.getAttributes().get("value");

        AttributeBean attr = target.getAttribute(name);
        if (attr != null) {
            createAttribute(attr, value, target);
        } else {
            attr = new AttributeBean();
            attr.setName(name);
            attr.setValue(value);
            attr.setBindingType(AttributeBean.BINDING_TYPE_EARLY);
            target.addAttribute(attr);
        }
    }

    /**
     * <p>Adds markup &lt;f:facet&gt; to the <code>target</code>'s
     * child {@link org.apache.shale.clay.config.beans.ElementBean}.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     */
    protected void addFacet(Node node, ElementBean target) {
        String facetName = (String) node.getAttributes().get("name");
        Iterator ci = node.getChildren().iterator();
        // look for the first well-formed node.  only one under
        // a facet.  Call back on the current builder
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            if (child.isWellFormed()) {
                Builder childBuilder = getBuilder(child);
                ElementBean nextTarget = childBuilder.createElement(child);
                nextTarget.setFacetName(facetName);
                target.addChild(nextTarget);
                childBuilder.encode(child, nextTarget, nextTarget);
                break;
            }
        }
    }


    /**
     * <p>Build's a <code>target</code> {@link ElementBean} from a {@link Node}. The
     * following child nodes are handles outside of the <code>encodeChildren</code>
     * method: symbol, facet, attribute, convert, validate, actionListener,
     * and valueChangeListener.
     * </p>
     *
     * @param node markup
     * @param target child config bean
     * @param root parent config bean
     */
    protected void encodeBegin(Node node, ElementBean target, ComponentBean root) {
        assignNode(node, target);

        List deleteList = new ArrayList();
        Iterator ci = node.getChildren().iterator();
        next: while (ci.hasNext()) {
            Node child = (Node) ci.next();
            if (child.isWellFormed() && child.getName() != null) {
                if (child.getName().equals("symbol")) {
                    addSymbol(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("facet")) {
                    addFacet(child, target);
                    deleteList.add(child);
                } else if (child.getName().equals("attribute")) {
                    addAttribute(child, target);
                    deleteList.add(child);
                } else if (child.getName().startsWith("convert")) {
                    addConverter(child, target);
                    deleteList.add(child);
                } else if (child.getName().startsWith("validate")
                        || child.getName().startsWith("validator")
                        || child.getName().startsWith("commonsValidator")) {
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
                if (node.getName() != null && node.getName().equals("verbatim")) {
                    continue next;
                }
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
     * <p>Returns the <code>jsfid</code> from the {@link Node} The <code>extends</code>
     * attribute is giving the first order of evaluation.  If empty, the <code>node</code>'s
     * name is assigned to the jsfid.</p>
     *
     * @param node markup
     * @return jsfid
     */
    protected String getJsfid(Node node) {
        StringBuffer jsfid = new StringBuffer();
        if (node.getAttributes().containsKey("extends")) {
            jsfid.append(node.getAttributes().get("extends"));
        } else {
            jsfid.append(node.getName());

            //override the node prefix with the one
            //assigned to the uri.  the prefix will
            //match the config beans

            String prefix = getPrefix();
            if (prefix == null) {
                prefix = node.getQname();
            }
            if (prefix != null) {
                jsfid.insert(0, ':');
                jsfid.insert(0, prefix);
            }
        }


        return jsfid.toString();
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
