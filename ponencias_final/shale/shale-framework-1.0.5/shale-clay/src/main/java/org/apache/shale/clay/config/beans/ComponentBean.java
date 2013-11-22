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
 * $Id: ComponentBean.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;

/**
 * <p>This is the base class of most of the metadata that is used by the
 * {@link org.apache.shale.clay.component.Clay} component to build
 * a component subtree.
 *
 * <dl>
 * <dt> There are three sources that populate this object:
 * <dd> {@link org.apache.shale.clay.config.ClayXmlParser} - from XML config files
 * <dd> {@link org.apache.shale.clay.parser.builder.Builder} - extending classes
 * <dd> {@link org.apache.shale.clay.component.Clay} - <code>shapeValidator</code>
 *      a <code>validator</code> style of event method binding
 * </dl>
 * </p>
 */
public class ComponentBean extends AbstractBean implements Comparable, Serializable {

    /**
     * <p>Unique id used by the Serializable interface.</p>
     */
    private static final long serialVersionUID = 3907217039524312373L;

    /**
     * <p>Common Logging utility class.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(ComponentBean.class);
    }

    /**
     * <p>A class scoped unique sequence counter.</p>
     */
    private static long uniqueSequence = -1;

    /**
     * @return the next <code>uniqueSequence</code>
     */
    private synchronized long generateId() {
       return ++uniqueSequence;
    }

    /**
     * <p>The config beans unique sequence.  The value is
     * populated from a call to <code>generateId</code>.</p>
     */
    private long jspId = -1;

    /**
     * <p>Returns a unique id that will stick to the config bean.
     * It represents its compositional relationship within the subtree.
     * This is clay's version of the <code>JspIdConsumer</code>
     * in JSP 2.1.</p>
     *
     * @return unique id for a view element
     */
    public long getJspId() {
       if (jspId == -1) {
           jspId = generateId();
       }

       return jspId;
    }

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", ComponentBean.class
            .getClassLoader());

    /**
     * <p>Unique id that points to component meta information.</p>
     */
    private String jsfid = null;

    /**
     * <p>This value pair collection is used to set the property values for JSF object
     * implementing <code>UIComponent, Validator, ValueChangeListener, ActionListener
     * and Converter</code>.
     * </p>
     */
    private Map attributes = new Attributes();

    /**
     * <p>An object reference that shows a generalization relationship through the
     * metadata.  The <code>extends</code> attribute will hold the <code>jsfid</code>
     * of the parent this instance extends.
     * </p>
     */
    private ComponentBean isAParent = null;

    /**
     * <p>An object reference that shows a composition relationship.  This reference
     * will point to the parent that holds this object instance in one of it's collections.
     *</p>
     */
    private ComponentBean hasAParent = null;

    /**
     * <p>The <code>componentType</code> relates to a JSF component type used use to instantiate
     * the component using abstract factories.  For component's like <code>ActionListener and
     * ValueChangeListener</code>, that are not registered in the faces configuration file, the
     * <code>componentType</code> is the fully qualified class name.
     * </p>
     */
    private String componentType = null;

    /**
     * <p>The <code>jsfid</code> of the meta component parent.</p>
     */
    private String extendsElementId = null;

    /**
     * <p>Child meta components that form composition under another meta
     * component instance.  Each instance in this set will be a instance
     * of {@link ElementBean} and uniquely identified by <code>renderId</code>
     * </p>
     */
    private Collection children = new TreeSet();

    /**
     *  <p>Boolean flag indicates the meta inheritance of this component
     *  has been resolved.</p>
     */
    private boolean isInheritanceFinal = false;

    /**
     *  <p>Reference to an associated {@link ComponentBean} that is an instance of
     *  {@link ConverterBean} and is used to instantiate a JSF <code>Converter</code>.
     *  </p>
     */
    private ComponentBean converter = null;

    /**
     *  <p>Reference to a set of associated {@link ComponentBean} that is an instance of
     *  {@link ValidatorBean} and is used to instantiate a JSF <code>Validator</code>.
     *  </p>
     */
    private TreeSet validators = new TreeSet();

    /**
     *  <p>Reference to a set of associated {@link ComponentBean} that is an instance of
     *  {@link ValueChangeListenerBean} and is used to instantiate a JSF <code>ValueChangeListener</code>.
     *  </p>
     */
    private TreeSet valueChangeListeners = new TreeSet();

    /**
     *  <p>Reference to a set of associated {@link ComponentBean} that is an instance of
     *  {@link ActionListenerBean} and is used to instantiate a JSF <code>ActionListener</code>.
     *  </p>
     */
    private TreeSet actionListeners = new TreeSet();

    /**
     * <p>This attribute used when defining the template style of page composition where the
     * body of the HTML element is rendered by the component ignoring the HTML.
     */
    private String allowBody = null;

    /**
     * <p>Use this property to add the component to the parent's facet collection rather than
     * the default children collection.</p>
     */
    private String facetName = null;

    /**
     * <p>The replacement symbol table for the component meta-data.</p>
     */
    private Map symbols = new Attributes();

    /**
     * <p>This property only applies when using the {@link org.apache.shale.clay.component.Clay}
     * template features.  A <code>true</code> value is returned if the HTML child nodes under
     * the node that this meta component is bound to should be rendered; otherwise, a <code>"false"</code>
     * value is returned indicating the child nodes should be ignored.
     * </p>
     *
     * @return literal string "true" if allow body is on
     */
    public String getAllowBody() {
        return allowBody;
    }

    /**
     * <p>This property only applies when using the {@link org.apache.shale.clay.component.Clay}
     * template features.  Sets a Boolean string value that indicating if the child HTML nodes
     * under the node that this component is bound to should render or ignore its child nodes.
     * </p>
     *
     * @param allowBody indicates how the child markup nodes are processed
     *
     */
    public void setAllowBody(String allowBody) {
        this.allowBody = allowBody;
    }

    /**
     * <p>Returns a boolean representation of the <code>allowBody</code> property.
     * The default is <code>true</code></p>
     *
     * @return <code>true</code> if allowBody has a literal string value of "true"
     */
    public boolean getIsBodyAllowed() {
       boolean f = true;
       try {
           if (allowBody != null) {
               f = Boolean.valueOf(allowBody).booleanValue();
           }
       } catch (Exception e) {
           f = true;
       }

       return f;
    }

  /**
   * <p>Returns the facet name that will be used as the identifier when adding the
   * component to the parent facets collection.</p>
   *
   * @return facetName
   */
   public String getFacetName() {
      return facetName;
   }

   /**
    * <p>Sets the facet name that will be used as the identifier when adding the
    * component to the parent facets collection.</p>
    *
    * @param facetName component grouping
    */
   public void setFacetName(String facetName) {
      this.facetName = facetName;
   }

   /**
    * @return a value list of the object's state
    */
   public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("jsfid=\"").append(jsfid).append(
                "\" componentType=\"").append(componentType).append(
                "\" extends=\"").append(extendsElementId).append("\"")
                .append(" allowBody=\"").append(allowBody)
                .append("\" ").append("facetName=\"").append(facetName)
                .append("\"");

        return buff.toString();
    }

    /**
     * <p>Returns the component type that is used to instantiate the associated
     * JSF component.<p>
     *
     * @return component type
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * <p>Returns the <code>jsfid</code> of the meta component that this
     * instance inherits from.<p>
     *
     * @return extended jsfid
     */
    public String getExtends() {
        return extendsElementId;
    }

    /**
     * <p>Sets the component type uses by abstract factories to instantiate
     * associated JSF resources.</p>
     *
     * @param componentType used to create a JSF resource
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * <p>Sets the <code>jsfid</code> of the meta component that this meta
     * component inherits from.
     * </p>
     *
     * @param extendsElementId extending jsfid
     */
    public void setExtends(String extendsElementId) {
        this.extendsElementId = extendsElementId;
    }

    /**
     * <p>Returns a <code>Iterator</code> to the <code>children</code> set.
     * Each item in the set is uniquely identified by its
     * <code>renderId</code> property and an instance of {@link ElementBean}.
     * </p>
     *
     * @return iterator for the children collection
     */
    public Iterator getChildrenIterator() {

        return children.iterator();
    }

    /**
     *<p>Returns a set of children that are instances of {@link ElementBean}.
     *</p>
     *
     * @return children collection
     */
    public Collection getChildren() {
        return children;
    }

    /**
     * <p>Merges two sets of children {@link org.apache.shale.clay.config.beans.ElementBean}. Items in the source
     * collection will replace those in the target with the same <code>renderId</code>
     * </p>
     *
     * @param collection of child components
     */
    public void setChildren(Collection collection) {
        children.addAll(collection);
    }

    /**
     * <p>Adds a child {@link org.apache.shale.clay.config.beans.ElementBean} to the <code>children</code> set and
     * fixes up the composition parent relationship.</p>
     *
     * @param obj element bean added as a child
     */
    public void addChild(ElementBean obj) {
        if (obj.getJsfid() != null) {
            obj.setHasAParent(this);
            children.add(obj);
        } else {
            log.error(messages.getMessage("missing.jsfid.error", new Object[] {"ElementBean.jsfid", getJsfid()}));
        }
    }

    /**
     * <p>This <code>Comparable</code> implementation makes the
     * <code>jsfid</code> attribute the unique identifier for the object in a
     * set.</p>
     *
     * @param obj target object to compare to
     * @return weighted value based on the jsfid property
     */
    public int compareTo(Object obj) {
        return ((ComponentBean) obj).getJsfid().compareTo(
                getJsfid());
    }

    /**
     * <p>Gets a meta converter bean used to instantiate a jsf <code>Converter</code>.</p>
     *
     * @return converter assigned to the component
     */
    public ComponentBean getConverter() {
        return converter;
    }

    /**
     * <p>Adds a {@link ConverterBean} and assigns the composition parent.</p>
     *
     * @param bean converter assigned to this component
     */
    public void addConverter(ConverterBean bean) {
        if (bean.getJsfid() != null) {
            bean.setHasAParent(this);
            converter = bean;
        } else {
            log.error(messages.getMessage("missing.jsfid.error", new Object[] {"ConverterBean.jsfid", getJsfid()}));
        }
    }

    /**
     * <p>Returns a <code>Collection</code> of meta validators used to create jsf <code>Validator</code>
     * object instances.
     *
     * @return collection of validators
     */
    public Collection getValidators() {
        return validators;
    }

    /**
     * <p>Adds a collection of {@link ValidatorBean} to the <code>validator</code> set.  Each
     * instance is uniquely identified in the collection by the <code>jsfid</code>.
     * </p>
     *
     * @param collection of validators added to the component
     */
    public void setValidators(Collection collection) {
        validators.addAll(collection);
    }

    /**
     * <p>Returns a <code>Iterator</code> to the <code>validator</code> set.  Each
     * object will be an instance of {@link ValidatorBean}.
     * </p>
     *
     * @return Iterator of the components validators collection
     */
    public Iterator getValidatorIterator() {

        return validators.iterator();
    }

    /**
     * <p>Adds a {@link ValidatorBean} and assigns the composition parent.</p>
     *
     * @param bean validator to add to the component
     */
    public void addValidator(ValidatorBean bean) {
        if (bean.getJsfid() != null) {
            validators.add(bean);
        } else {
            log.error(messages.getMessage("missing.jsfid.error",
                    new Object[] {"ValidatorBean.jsfid", getJsfid()}));
        }
    }

    /**
     * <p>Returns a <code>Iterator</code> to the <code>valueChangeListeners</code> set.
     * Each {@link ValueChangeListenerBean} in the collection is uniquely identified
     * by <code>jsfid</code>.
     *</p>
     *
     * @return collection of value change listeners
     */
    public Collection getValueChangeListeners() {
        return valueChangeListeners;
    }

    /**
     * <p>Merges a collection of {@link org.apache.shale.clay.config.beans.ValueChangeListenerBean} where items in the
     * source collection with the same <code>jsfid</code> will override items
     * in the target set with the same identifier.
     * </p>
     *
     * @param collection of value change listeners added to the component
     */
    public void setValueChangeListeners(Collection collection) {
        valueChangeListeners.addAll(collection);
    }

    /**
     * <p>Returns a <code>Iterator</code> for the <code>valueChangeListener</code> set
     * of {@link ValueChangeListenerBean}.<p>
     *
     * @return iterator for the collection of value change listeners
     */
    public Iterator getValueChangeListenerIterator() {
        return valueChangeListeners.iterator();
    }

    /**
     * <p>Adds a {@link ValueChangeListenerBean} to the set where each instance is
     * uniquely identified by <code>jsfid</code>.</p>
     *
     * @param bean value change listener added to the components collection of listeners
     */
    public void addValueChangeListener(ValueChangeListenerBean bean) {
        if (bean.getJsfid() != null) {
            valueChangeListeners.add(bean);
        } else {
            log.error(messages.getMessage("missing.jsfid.error",
                    new Object[] {"ValueChangeListenerBean.jsfid", getJsfid()}));
        }
    }

    /**
     * <p>Returns a <code>Collection</code> of {@link ActionListenerBean}. </p>
     *
     * @return collection of the component's action listeners
     */
    public Collection getActionListeners() {
        return actionListeners;
    }

    /**
     * <p>Merges two collections where items in the source collection will override
     * those in the target collection of {@link ActionListenerBean} by the <code>jsfid</code>
     * property.</p>
     *
     * @param collection of action listeners added to the components set
     */
    public void setActionListeners(Collection collection) {
        actionListeners.addAll(collection);
    }

    /**
     * <p>Returns an <code>Iterator</code> for the <code>actionListeners</code> set of
     * {@link ActionListenerBean}.
     * </p>
     *
     * @return iterator of the component's action listeners set
     */
    public Iterator getActionListenerIterator() {

        return actionListeners.iterator();
    }

    /**
     * <p>Adds an {@link ActionListenerBean} to the <code>actionListeners</code> set.  Each
     * instance is uniquely identified by the <code>jsfid</code> property.
     * </p>
     *
     * @param bean action listener added to the component
     */
    public void addActionListener(ActionListenerBean bean) {
        if (bean.getJsfid() != null) {
            actionListeners.add(bean);
        } else {
            log.error(messages.getMessage("missing.jsfid.error",
              new Object[] {"ActionListenerBean.jsfid", getJsfid()}));
        }
    }

    /**
     * <p>This inner class provides implementation for an <code>Iterator</code> handeling
     * {@link AttributeBean} objects in the <code>attributes</code> collection.
     * </p>
     *
     * @return iterator for the components attributes Map
     */
    public Iterator getAttributeIterator() {

        return new Iterator() {
            /**
             * <p>Graps a <code>Iterator</code> instance of the attributes entry set</p>
             */
            private Iterator entrySet = getAttributes().entrySet().iterator();
            /**
             * <p>Decorates the <code>Iterator</code> of the private <code>entrySet</code></p>
             */
            public boolean hasNext() {
                return entrySet.hasNext();
            }
            /**
             * <p>Returns the next {@link AttributeBean} in the Map collection</p>
             */
            public Object next() {
                Map.Entry e = (Map.Entry) entrySet.next();
                return e.getValue();
            }

            /**
             * <p>This method is not applicable for this class and has an empty
             * method body but has to be implemented to realize the <code>Iterator</code>
             * interface.
             * </p>
             */
            public void remove() {
            }
        };

    }

    /**
     * <p>Returns a {@link AttributeBean} by the classes <code>name</code> property.</p>
     *
     * @param key attribute name
     * @return attribute bean for the key
     */
    public AttributeBean getAttribute(String key) {
        return (AttributeBean) getAttributes().get(key);
    }

    /**
     * <p>Adds a {@link AttributeBean} to the <code>attributes</code> Map collection where
     * the <code>name</code> property is the key identifier in the value pair relationship.
     * </p>
     *
     * @param obj attribute bean added to the attributes Map
     */
    public void addAttribute(AttributeBean obj) {
        if (obj.getName() != null) {
            obj.setHasAParent(this);
            attributes.put(obj.getName(), obj);
        } else {
            log.error(messages.getMessage("missing.jsfid.error", new Object[] {"AttributeBean.jsfid", getJsfid()}));
        }

    }

    /**
     * <p>Returns the a <code>Map</code> collection of {@link AttributeBean} objects.</p>
     *
     * @return attributes map
     */
    public Map getAttributes() {

        return attributes;
    }

    /**
     * <p>Returns the unique meta component identifier.</p>
     *
     * @return jsfid
     */
    public String getJsfid() {
        return jsfid;
    }

    /**
     * <p>Merges a set of {@link AttributeBean} where items in the source
     * collection override items in the target collection by the object's
     * <code>jsfid</code> property.
     * </p>
     *
     * @param map of attributes to be merged
     */
    public void setAttributes(Map map) {
        attributes.putAll(map);
    }

    /**
     * <p>Sets the unique meta component identifier.</p>
     *
     * @param jsfid identifier
     */
    public void setJsfid(String jsfid) {
        this.jsfid = jsfid;
    }

    /**
     * <p>Returns the parent component that aggregates this object.</p>
     *
     * @return composition parent
     */
    public ComponentBean getHasAParent() {
        return hasAParent;
    }

    /**
     * <p>Returns the parent component that generalizes this object.</p>
     *
     * @return inheritance parent
     */
    public ComponentBean getIsAParent() {
        return isAParent;
    }

    /**
     * <p>Sets the parent that owns this component.</p>
     *
     * @param bean composition parent
     */
    public void setHasAParent(ComponentBean bean) {
        hasAParent = bean;
    }

    /**
     * <p>Sets the component that this instance extends.</p>
     *
     * @param bean inheritance parent
     */
    public void setIsAParent(ComponentBean bean) {
        isAParent = bean;
    }

    /**
     * <p>Returns a xpath like string describing how this component
     * fits into the overall composition.</p>
     *
     * @return composition client id
     */
    public StringBuffer getHasAClientId() {
        StringBuffer id = null;

        if (getHasAParent() != null) {
            id = getHasAParent().getHasAClientId();
        } else {
            id = new StringBuffer();
        }

        id.append("/").append(getJsfid());

        return id;
    }

    /**
     * <p>Returns an xpath like string that describes the heritage
     * of this component.</p>
     *
     * @return inheritance client id
     */
    public StringBuffer getIsAClientId() {
        StringBuffer id = new StringBuffer();

        ComponentBean parent = getIsAParent();
        while (parent != null) {
            id.insert(0, parent.getJsfid() + (id.length() > 0 ? ":" : ""));
            parent = parent.getIsAParent();
        }
        parent = null;

        return id;
    }

    /**
     * <p>Returns a boolean flag indicating that the meta inheritances
     * has been resolved.</p>
     *
     * @return <code>true</code> if inheritance has been resolved
     */
    public boolean isInheritanceFinal() {
        return isInheritanceFinal;
    }

    /**
     * <p>Sets a boolean flag indicating that the meta inheritances
     * have been resolved.</p>
     *
     * @param b <code>true</code> if inheritance has been resolved
     */
    public void setInheritanceFinal(boolean b) {
        isInheritanceFinal = b;
    }

    /**
     * <p>Returns the identifier that will populate the JSF <code>UIComponent.id</code>
     *  property and is used to name the component within the tree.
     * </p>
     *
     * @return component's id
     */
    public String getId() {
        AttributeBean attr = (AttributeBean) attributes.get("id");
        if (attr != null) {
           return attr.getValue();
        }

        return null;
    }

    /**
     * <p>Sets the identifier that is used to populate the JSF <code>UIComponent.id</code>
     * property. </p>
     *
     * @param id component's identifier
     */
    public void setId(String id) {
        AttributeBean attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setValue(id);
        attr.setName("id");
        addAttribute(attr);
    }

    /**
     * <p>Adds a symbol identified by the
     * {@link SymbolBean} to the symbols collection.</p>
     *
     * @param symbol added to the symbols Map
     */
    public void addSymbol(SymbolBean symbol) {
       if (symbol.getName() != null && symbol.getName().length() > 0) {
            StringBuffer buff = new StringBuffer(symbol.getName());
            if (buff.charAt(0) != '@') {
               buff.insert(0, '@');
               symbol.setName(buff.toString());
            }

            symbols.put(symbol.getName(), symbol);
        }
    }

    /**
     * <p>Returns the replacement symbols assigned to the component.
     * The key value represents the literal replacement string.
     * The value Map property represents target {@link SymbolBean}.</p>
     *
     * @return map of symbols
     */
    public Map getSymbols() {
       return symbols;
    }

    /**
     * <p>Returns a {@link SymbolBean} from the <code>symbols</code>
     * Map by <code>name</code>.  Prepends a '@' character to the
     * <code>name</code> if it doesn't exist.</p>
     *
     * @param name of the symbol
     * @return symbol bean identified by name
     */
    public SymbolBean getSymbol(String name) {
        StringBuffer tmp = new StringBuffer(name);
        if (tmp.charAt(0) != '@') {
            tmp.insert(0, '@');
        }

        SymbolBean symbol = (SymbolBean) symbols.get(tmp.toString());

        return symbol;
    }

}

