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
 * $Id: PropertyValueCommand.java 473039 2006-11-09 19:26:41Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.util.PropertyHelper;

/**
 * <p>
 * This is the catch all <code>Command</code> to handle all attributes that
 * are not an "action", "actionListener", "validator" and "valueChangeListener".
 * This <code>Command</code> is invoked from the
 * {@link AssignPropertiesCommand} chain.
 * <p>
 */
public class PropertyValueCommand extends AbstractCommand implements Command {

    /**
     * <p>Helper class to provide access to the properties of JavaBeans.</p>
     */
    private PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * <p>Helper class to provide access to by-type JavaServer Faces
     * <code>Converter</code> capabilities.</p>
     */
    private ConverterHelper converterHelper = new ConverterHelper();

    /**
     * <p>Sets a property value on the target component.  If the data
     * type of the target bean property is not a String and the property
     * value is a String, the data type is converted to the target type
     * using the <code>ConverterHelper</code>.  The value is applied to
     * the component using <code>PropertyHelper</code>.</p>
     *
     * @param context faces context
     * @param target object with the property
     * @param propertyName name of the target property
     * @param propertyValue value of the target property
     */
    private void setProperty(FacesContext context, Object target, String propertyName, Object propertyValue) {
        Class classz = null;
        StringBuffer actualPropertyName = new StringBuffer(propertyName);

        try {
            classz = propertyHelper.getType(target, actualPropertyName.toString());
        } catch (PropertyNotFoundException e) {
            if (propertyName.length() > "isX".length() && propertyName.startsWith("is")) {
               actualPropertyName.delete(0, 2);
               actualPropertyName.setCharAt(0, Character.toLowerCase(actualPropertyName.charAt(0)));

               classz = propertyHelper.getType(target, actualPropertyName.toString());
            } else {
               throw e;
            }

        }

        if (classz != Object.class && classz != propertyValue.getClass()
            && propertyValue.getClass() == String.class) {

            Object targetValue = converterHelper.asObject(context, classz, (String) propertyValue);
            propertyHelper.setValue(target, actualPropertyName.toString(), targetValue);
        } else {
            propertyHelper.setValue(target, actualPropertyName.toString(), propertyValue);
        }

    }


    /**
     * <p>
     * Looks at the {@link AttributeBean} on the {@link ClayContext} to see
     * if the value is a binding EL.  If it is not it just updates the component
     * with the value.  If the attribute is a value binding expression, then a
     * <code>ValueBinding</code> is created.  If the attribute uses early binding
     * the <code>ValueBinding</code> is executed and result applied to the component.
     * Otherwise, the binding expression is applied to the component in a prepared state.
     * A <code>true</code> value is always returned because this is the default handler.
     * </p>
     *
     * @param context common chains
     * @return <code>true</code> if the chain is complete
     * @exception Exception propagated up to the top of the chain
     */
    public boolean execute(Context context) throws Exception {

        boolean isFinal = true;

        ClayContext clayContext = (ClayContext) context;
        if (clayContext == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.clayContext"));
        }
        AttributeBean attributeBean = clayContext.getAttribute();
        if (attributeBean == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.attributeBean"));
        }
        Object child = clayContext.getChild();
        if (child == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.childComponent"));
        }
        ComponentBean displayElement = clayContext.getDisplayElement();
        if (displayElement == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.componentBean"));
        }
        FacesContext facesContext = clayContext.getFacesContext();
        if (facesContext == null) {
            throw new NullPointerException(getMessages().getMessage("clay.null.facesContext"));
        }

        // don't try to set the binding attribute of anything but a component
        if (attributeBean.getName().equals("binding")
                && !(child instanceof UIComponent)) {
            return isFinal;

        }
        // skip trying to set the converterId on a converter
        if (attributeBean.getName().equals("converterId")
                && child instanceof Converter) {
            return isFinal;
        }
        // replace all symbols returning the target attribute value
        String expr = replaceMnemonic(clayContext);

        //exit if null or empty string
        if (expr == null) {
           return isFinal;
        }


        String bindingType = attributeBean.getBindingType();
        if (bindingType == null) {
            bindingType = AttributeBean.BINDING_TYPE_NONE;
        }

        //contains expression language
        boolean isEL = isValueReference(expr);
        //use value binding
        boolean isVB = ((bindingType.equals(AttributeBean.BINDING_TYPE_VALUE))
                        && (child instanceof UIComponent));
        //use early binding
        boolean isEarly = bindingType.equals(AttributeBean.BINDING_TYPE_EARLY);

        if (isEL && isVB) {
           getTagUtils().setValueBinding((UIComponent) child, attributeBean.getName(), expr);
        } else if (isEL && isEarly) {
            ValueBinding vb = facesContext.getApplication().createValueBinding(expr);
            Object value = vb.getValue(facesContext);
            try {
                setProperty(facesContext, child, attributeBean.getName(), value);
            } catch (Exception e) {
                 if (child instanceof UIComponent) {
                   ((UIComponent) child).getAttributes().put(attributeBean.getName(), expr);
                 } else {
                    throw e;
                 }
             }
        } else  {
            try {
               setProperty(facesContext, child, attributeBean.getName(), expr);
            } catch (Exception e) {
                if (child instanceof UIComponent) {
                  ((UIComponent) child).getAttributes().put(attributeBean.getName(), expr);
                } else {
                   if (child.getClass().getName().equals("org.apache.shale.validator.CommonsValidator")) {
                      Map vars = (Map) propertyHelper.getValue(child, "vars");
                      //vars collection is like the components attributes
                      //native support for shale components
                      vars.put(attributeBean.getName(), expr);
                   } else {
                      throw e;
                   }
                }
            }
        }

        return isFinal;
    }

}
