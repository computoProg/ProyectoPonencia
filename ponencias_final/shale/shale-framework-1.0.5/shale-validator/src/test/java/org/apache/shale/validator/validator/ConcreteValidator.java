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

package org.apache.shale.validator.validator;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.shale.validator.util.ShaleValidatorAction;

/**
 * <p>Simple concrete implementation of <code>AbstractValidator</code>.</p>
 */
public class ConcreteValidator extends AbstractValidator {


    public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {

        ; // Do nothing

    }
    

    // Expose the following protected methods publicly for testing purposes


    public ShaleValidatorAction[] actions(FacesContext context, String type) {
        return super.actions(context, type);
    }



    public Object convert(FacesContext context, Object value, Class type) {
        return super.convert(context, value, type);
    }


    public String message(FacesContext context, String key) {
        return super.message(context, key);
    }



    public ValidatorResources resources(FacesContext context) {
        return super.resources(context);
    }



    public void validate(FacesContext context, UIComponent component,
                         Object value, String type, Map vars)
      throws ValidatorException {
        super.validate(context, component, value, type, vars);
    }


}
