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

package org.apache.shale.validator.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentTag;
import org.apache.shale.faces.ShaleConstants;

/**
 * The tag class for the <code>s:validatorScript</code> tag.
 *
 * $Id: ValidatorScriptTag.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class ValidatorScriptTag extends UIComponentTag {


    /**
     * <p>The function name for validating the enclosing form.</p>
     */
   private String functionName;


    /**
     * <p>A Utility object to aid in developing JSF tags. See
     *    <code>org.apache.shale.util.Tags</code> for more
     *    information.</p>
     */
   private org.apache.shale.util.Tags tagUtils = null;


    /**
     * <p>This constructor retrieves a managed bean
     *    that has utility methods for implementing JSP
     *    custom tags for JSF components.</p>
     */
    public ValidatorScriptTag() {
        FacesContext context = FacesContext.getCurrentInstance();
        tagUtils = (org.apache.shale.util.Tags) context
                     .getApplication()
                     .getVariableResolver()
                     .resolveVariable(context, ShaleConstants.TAG_UTILITY_BEAN);
    }


    /**
     * <p>Setter method for the function name.</p>
     *
     * @param newValue The new value for the function name.
     */
    public void setFunctionName(String newValue) {
        functionName = newValue;
    }


    /**
     * <p>Sets properties for the component.</p>
     *
     * @param component The component whose properties we're setting
     */
    public void setProperties(UIComponent component) {
        super.setProperties(component);
        tagUtils.setString(component, "functionName",
          functionName);
    }


    /**
     * <p>Sets the <code>functionName</code> property to null.</p>
     */
    public void release() {
        functionName = null;
    }


    /**
     * @return Returns the renderer type, which is null.
     */
    public String getRendererType() {
        return null;
    }


    /**
     * @return Returns the component type, which is
     *    <code>org.apache.shale.ValidatorScript</code>.
     */
    public String getComponentType() {
        return "org.apache.shale.ValidatorScript";
    }


}
