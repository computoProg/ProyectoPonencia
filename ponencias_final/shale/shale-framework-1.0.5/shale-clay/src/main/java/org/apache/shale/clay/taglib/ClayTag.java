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
 * $Id: ClayTag.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;

import org.apache.shale.clay.component.Clay;

/**
 * <p>JSP Tag for the {@link org.apache.shale.clay.component.Clay} component.</p>
 *
 */
public class ClayTag extends UIComponentBodyTag {

    /**
     * <p>Represents the meta-component to build the subtree on.</p>
     */
    private String jsfid = null;

    /**
     * <p>The name of the managed bean instance in the faces configuration file that
     * should be bound to this view composition.  The literal "@managed-bean-name"
     * in the component metadata will be replaced with this value before the
     * binding of the expression is created.
     * </p>
     */
    private String managedBeanName = null;

    /**
     * <p>A method event that conforms to the standard <code>validator</code>
     * attribute.  This event will get fired before the sub component tree is
     * build.
     * </p>
     */
    private String shapeValidator = null;

    /**
     * <p>Returns the expression literal defining the validator event method
     *  binding.
     *  </p>
     *
     * @return method binding expression
     */
    public String getShapeValidator() {
        return shapeValidator;
    }

    /**
     * <p>Sets the expression literal defining the validator event method binding.
     * </p>
     *
     * @param shapeValidator <code>String</code> validator binding expression
     */
    public void setShapeValidator(String shapeValidator) {
        this.shapeValidator = shapeValidator;
    }


    /**
     * <p>Gets the display Element identifier to be rendered.</p>
     *
     * @return jsfid
     */
    public String getJsfid() {
        return jsfid;
    }

    /**
     * <p>Sets the identifier defining component metadata.
     * </p>
     *
     * @param jsfid <code>String</code> sets the component element to be rendered
     */
    public void setJsfid(String jsfid) {
        this.jsfid = jsfid;
    }

    /**
     * <p>Gets the name of the managed bean that is dynamically resolved
     * using a token replacement before binding the value expression.
     * If not explicitly set, it will default to the literal <code>UNKNOWN</code>
     * </p>
     *
     * @return managed bean name symbol
     */
    public String getManagedBeanName() {
        if (managedBeanName == null || managedBeanName.length() == 0) {
            return "UNKNOWN";
        }

        return managedBeanName;
    }

    /**
     * <p> Name of the managed bean that is dynamically resolved. The literal
     * string "managed-bean-name" is replaced with the value of this
     * property.
     * </p>
     *
     * @param managedBeanName <code>String</code>
     */
    public void setManagedBeanName(String managedBeanName) {
        this.managedBeanName = managedBeanName;
    }


    /**
     * <p> Returns the logical component name registered in the faces config
     * for the {@link org.apache.shale.clay.component.Clay} component.
     * </p>
     *
     * @return component type
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType() {
        return "org.apache.shale.clay.component.Clay";
    }

    /**
     * <p>Returns the render registered for the component.  The
     * {@link org.apache.shale.clay.component.Clay} component doesn't
     * have a render because the component invokes the rendering of
     * it's children.
     * </p>
     *
     * @return <code>null</code>
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType() {
        return null;
    }

    /**
     *  <p>This method is invoked by the super and its purpose it
     *  to push tag attributes to corresponding component property
     *  values.
     *  </p>
     *
     * @param component {@link UIComponent} instance of {@link org.apache.shale.clay.component.Clay}
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        Clay c = (Clay) component;
        FacesContext facesContext = getFacesContext();

        if (getJsfid() != null) {

            c.setJsfid(getJsfid());

        }


        if (getManagedBeanName() != null) {
            c.setManagedBeanName(getManagedBeanName());

            if (isValueReference(getManagedBeanName())) {
                ValueBinding valueBinding = facesContext.getApplication().createValueBinding(getManagedBeanName());
                Object value = valueBinding.getValue(facesContext);
                c.setManagedBeanName((value != null) ? value.toString() : null);

            } else {
                c.setManagedBeanName(getManagedBeanName());
            }

        }


        if (getShapeValidator() != null) {
            c.setShapeValidator(getShapeValidator());
        }

    }
}
