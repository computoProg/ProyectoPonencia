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

package org.apache.shale.util;

import java.io.Serializable;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 * <p>A utility class for JSF tags. An instance of this class is
 *    specified as an application-scoped managed bean in <code>
 *    faces-config.xml</code>, under the name
 *    <code>org.apache.shale.TAG_UTILITY_BEAN</code>.
 *    The <code>org.apache.shale.taglib.CommonsValidatorTag</code>
 *    uses that managed bean.</p>
 * <p>Nearly all of the methods of this class set a component attribute or
 *    create a value- or method-binding, given a String representation
 *    of the value. The methods check to see whether the value is a value
 *    reference (ie. <code>#{...}</code>); if so, the methods set value
 *    bindings or method bindings, as appropriate, for the component.</p>
 *
 * $Id: Tags.java 482453 2006-12-05 02:10:40Z rahul $
 */
public class Tags {


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>String</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     */
    public void setString(UIComponent component, String attributeName,
            String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, attributeValue);
        }
    }


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>Integer</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     */
    public void setInteger(UIComponent component,
            String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName,
                    new Integer(attributeValue));
        }
    }


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>Double</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     */
    public void setDouble(UIComponent component,
            String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName,
                    new Double(attributeValue));
        }
    }


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>Boolean</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     */
    public void setBoolean(UIComponent component,
            String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName,
                    new Boolean(attributeValue));
        }
    }


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>ValueBinding</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     */
    public void setValueBinding(UIComponent component, String attributeName,
            String attributeValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueBinding vb = app.createValueBinding(attributeValue);
        component.setValueBinding(attributeName, vb);
    }


    /**
     * <p>Sets the component's <code>ActionListener</code>
     *    by setting a method binding with the given attribute
     *    value.</p>
     *
     * @param component The JSF component
     * @param attributeValue The value of the attribute
     */
    public void setActionListener(UIComponent component, String attributeValue) {
        setMethodBinding(component, "actionListener", attributeValue,
                new Class[] { ActionEvent.class });
    }


    /**
     * <p>Sets the component's <code>ValueChangeListener</code>
     *    by setting a method binding with the given attribute
     *    value.</p>
     *
     * @param component The JSF component
     * @param attributeValue The value of the attribute
     */
    public void setValueChangeListener(UIComponent component,
            String attributeValue) {
        setMethodBinding(component, "valueChangeListener", attributeValue,
                new Class[] { ValueChangeEvent.class });
    }


    /**
     * <p>Sets the component's <code>Validator</code>
     *    by setting a method binding with the given attribute
     *    value.</p>
     *
     * @param component The JSF component
     * @param attributeValue The value of the attribute
     */
    public void setValidator(UIComponent component,
            String attributeValue) {
        setMethodBinding(component, "validator", attributeValue,
                new Class[] { FacesContext.class, UIComponent.class, Object.class });
    }


    /**
     * <p>Sets the component's <code>Action</code>
     *    by setting a method binding with the given attribute
     *    value.</p>
     *
     * @param component The JSF component
     * @param attributeValue The value of the attribute
     */
    public void setAction(UIComponent component, String attributeValue) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            setMethodBinding(component, "action", attributeValue,
                    new Class[] {});
        } else {
            MethodBinding mb = new ActionMethodBinding(attributeValue);
            component.getAttributes().put("action", mb);
        }
    }


    /**
     * <p>Sets the component's <code>attributeName</code> with the
     *    given <code>attributeValue</code> as a <code>MethodBinding</code>.</p>
     *
     * <p>If the <code>attributeValue</code> is <code>null</code>, do
     *    nothing. If <code>attributeValue</code> is a value reference,
     *    set a value binding for the component. Otherwise, store the
     *    <code>attributeValue</code> in the component's attribute map.</p>
     *
     * @param component The JSF component
     * @param attributeName The name of the attribute to set
     * @param attributeValue The value of the attribute
     * @param paramTypes Signature for method parameters
     */
    public void setMethodBinding(UIComponent component, String attributeName,
            String attributeValue, Class[] paramTypes) {
        if (attributeValue == null) {
            return;
        }
        if (isValueReference(attributeValue)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            MethodBinding mb = app.createMethodBinding(attributeValue, paramTypes);
            component.getAttributes().put(attributeName, mb);
        }
    }


    /**
     * <p>Evaluate the <code>expression</code>. If it's a value reference,
     *    get the reference's value.
     *    Otherwise, return the <code>expression</code>.</p>
     *
     * @param expression The expression
     */
    public Object eval(String expression) {
        if (expression == null) {
            return null;
        }
        if (isValueReference(expression)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            return app.createValueBinding(expression).getValue(context);
        } else {
            return expression;
        }
    }


    /**
     * <p>Evaluate the <code>expression</code>. If it's a value reference,
     *    get the reference's value as a String.
     *    Otherwise, return the <code>expression</code>.</p>
     *
     * @param expression The expression
     */
    public String evalString(String expression) {
        if (expression == null) {
            return null;
        }
        if (isValueReference(expression)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            return "" + app.createValueBinding(expression).getValue(context);
        } else {
            return expression;
        }
    }


    /**
     * <p>Evaluate the <code>expression</code> and return an <code>Integer</code>.</p>
     *
     * @param expression The expression
     */
    public Integer evalInteger(String expression) {
        if (expression == null) {
            return null;
        }
        if (isValueReference(expression)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            Object r = app.createValueBinding(expression).getValue(context);
            if (r == null) {
                return null;
            } else if (r instanceof Integer) {
                return (Integer) r;
            } else {
                return new Integer(r.toString());
            }
        } else {
            return new Integer(expression);
        }
    }


    /**
     * <p>Evaluate the <code>expression</code> and return a <code>Double</code>.</p>
     *
     * @param expression The expression
     */
    public Double evalDouble(String expression) {
        if (expression == null) {
            return null;
        }
        if (isValueReference(expression)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            Object r = app.createValueBinding(expression).getValue(context);
            if (r == null) {
                return null;
            } else if (r instanceof Double) {
                return (Double) r;
            } else {
                return new Double(r.toString());
            }
        } else {
            return new Double(expression);
        }
    }


    /**
     * <p>Evaluate the <code>expression</code> and return a <code>Boolean</code>.</p>
     *
     * @param expression The expression
     */
    public Boolean evalBoolean(String expression) {
        if (expression == null) {
            return null;
        }
        if (isValueReference(expression)) {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            Object r = app.createValueBinding(expression).getValue(context);
            if (r == null) {
                return null;
            } else if (r instanceof Boolean) {
                return (Boolean) r;
            } else {
                return new Boolean(r.toString());
            }
        } else {
            return new Boolean(expression);
        }
    }


    /**
     * <p>A method binding whose result is a canned string.</p>
     *
     */
    private static class ActionMethodBinding
            extends MethodBinding implements Serializable {

        private static final long serialVersionUID = -8155887531790127241L;
        private String result;

        public ActionMethodBinding(String result) { this.result = result; }
        public Object invoke(FacesContext context, Object params[]) {
            return result;
        }
        public String getExpressionString() { return result; }
        public Class getType(FacesContext context) { return String.class; }
    }


    /**
     * <p>Return true if the specified string contains an EL expression.</p>
     * 
     * <p>This is taken almost verbatim from {@link javax.faces.webapp.UIComponentTag}
     * in order to remove JSP dependencies from the renderers.</p>
     *
     * @param value String to be checked for being an expression
     */
    private boolean isValueReference(String value) {

        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");
        if (start < 0) {
            return false;
        }

        int end = value.lastIndexOf('}');
        return (end >= 0) && (start < end);
    }


}
