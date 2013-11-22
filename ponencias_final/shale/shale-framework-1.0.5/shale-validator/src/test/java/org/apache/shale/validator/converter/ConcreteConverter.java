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

package org.apache.shale.validator.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>Simple concrete implementation of <code>AbstractConverter</code>.</p>
 */
public class ConcreteConverter extends AbstractConverter {


    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {
        return value;
    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }


    // Expose the following protected methods publicly for testing purposes


    public String message(FacesContext context, String key) {
        return super.message(context, key);
    }



}
