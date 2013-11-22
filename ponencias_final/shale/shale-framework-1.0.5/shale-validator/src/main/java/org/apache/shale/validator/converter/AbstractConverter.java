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
import javax.faces.convert.Converter;
import org.apache.shale.validator.util.AbstractUtilities;

/**
 * <p>Abstract base class for converters that use Apache Commons Validator
 * as their foundation.</p>
 */
public abstract class AbstractConverter extends AbstractUtilities
  implements Converter {
    

    // ------------------------------------------------------------ Constructors


    // ------------------------------------------------------ Manifest Constants


    // -------------------------------------------------------------- Properties


    // ------------------------------------------------------- Converter Methods


    /**
     * <p>Convert the specified string value, associated with the specified
     * <code>UIComponent</code>, into a corresponding model data object.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> associated with the
     *  value to be converted
     * @param value String value to be converted
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is <code>null</code>
     */
    public abstract Object getAsObject(FacesContext context, UIComponent component,
                                       String value);


    /**
     * <p>Convert the specified model data object, associated with the
     * specified <code>UIComponent</code>, into a string suitable for rendering.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param component <code>UIComponent</code> associated with the
     *  model data object to be converted
     * @param value Model data object to be converted
     *
     * @exception ConverterException if conversion cannot be successfully
     *  performed
     * @exception NullPointerException if <code>context</code> or
     *  <code>component</code> is <code>null</code>
     */
    public abstract String getAsString(FacesContext context, UIComponent component,
                                       Object value);


    // ------------------------------------------------------- Protected Methods


}
