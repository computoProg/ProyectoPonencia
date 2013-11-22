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
 * $Id: AttributeBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;


/**
 * <p>Represents a value for a component property or a tag attribute.
 * Instances of this class will be placed in the {@link ComponentBean}
 * <code>attributes</code> collection.</p>
 */
public class AttributeBean extends SymbolBean implements Serializable {

    /**
     * <p>Unique id used by the <code>Serializable</code> interface.</p>
     */
    private static final long serialVersionUID = 3102689599088266442L;

    /**
     * <p>Mnemonic the signifies the a method binding expression.</p>
     */
    public static final String BINDING_TYPE_METHOD = "MB";

    /**
     * <p>Mnemonic that signifies a value binding expression.</p>
     */
    public static final String BINDING_TYPE_VALUE = "VB";

    /**
     * <p>This code means that if the attribute value contains an expression,
     * it will be evalutated before populating the component.</p>
     */
    public static final String BINDING_TYPE_EARLY = "Early";

    /**
     * <p>This constant indicates that the attribute value will be passed as its
     * literal value and no attempt to evaluate a contained expression will
     * be preformed.</p>
     */
    public static final String BINDING_TYPE_NONE = "None";

    /**
     * <p>The parent meta component that contains this attribute in its
     * attributes collection.
     * </p>
     */
    private ComponentBean hasAParent = null;

    /**
     * <p>A meta component reference that this object is inherited from.</p>
     */
    private AttributeBean isAParent = null;

    /**
     * <p>This property represents the type of binding allowed
     * for this attribute by the component.</p>
     */
    private String bindingType = null;

    /**
     * <p>A boolean flag that the meta component inheritance has been
     * resolved for this object instance.
     * </p>
     */
    private boolean isInheritanceFinal = false;

    /**
     * @return named value list that represents the object's state
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("name=\"").append(getName()).append("\" value=\"").append(getValue())
            .append("\" bindingType=\"").append(bindingType).append("\"");
        return buff.toString();
    }

    /**
     * <p>Returns the parent component containing this object instance.</p>
     *
     * @return composition parent
     */
    public ComponentBean getHasAParent() {
        return hasAParent;
    }

    /**
     * <p>Sets the parent component that contains this object instance.</p>
     *
     * @param bean the composition parent
     */
    public void setHasAParent(ComponentBean bean) {
        hasAParent = bean;
    }

    /**
     * <p>Returns a parent component that this component extends in terms of
     * a meta relationship.</p>
     *
     * @return returns the inheritance parent
     */
    public AttributeBean getIsAParent() {
        return isAParent;
    }

    /**
     * <p>Sets a parent component that this component extends in terms of
     * a meta relationship.</p>
     *
     * @param bean the inheritance parent
     */
    public void setIsAParent(AttributeBean bean) {
        isAParent = bean;
    }

    /**
     * <p>Returns a String that indicates the binding type for the attribute.  The
     * valid values include ("VB", "MB", "None", "Early").</p>
     *
     * @return binding type enumeration
     */
    public String getBindingType() {
       return bindingType;
    }

    /**
     * <p>Sets a String that indicates the binding type for the attribute.  The
     * valid values include ("VB", "MB", "None", "Early").</p>
     *
     * @param bindingType enumeration
     */
    public void setBindingType(String bindingType) {
        if (bindingType != null
           && (bindingType.equals(BINDING_TYPE_METHOD)
           || bindingType.equals(BINDING_TYPE_VALUE)
           || bindingType.equals(BINDING_TYPE_EARLY)
           || bindingType.equals(BINDING_TYPE_NONE))) {

            this.bindingType = bindingType;
        } else {
            this.bindingType = null;
        }
    }

    /**
     * <p>Returns boolean that indicates the meta inheritance relationships have
     * been resolved.</p>
     *
     * @return <code>true</code> if inheritance has been resolved
     */
    public boolean isInheritanceFinal() {
        return isInheritanceFinal;
    }

    /**
     * <p>Sets a boolean that indicates the meta inheritance relationships have
     * been resolved.</p>
     *
     * @param b <code>true</code> if inheritance has been resolved
     */
    public void setInheritanceFinal(boolean b) {
        isInheritanceFinal = b;
    }

}


