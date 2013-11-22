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

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * <p>Helper class to provide access to by-type JavaServer Faces
 * <code>Converter</code> capabilities.  This implementation is stateless
 * and maintains no cached information, so instances may be freely created
 * and destroyed with no side effects.</p>
 *
 * $Id: ConverterHelper.java 560569 2007-07-28 16:56:48Z gvanmatre $
 *
 * @since 1.0.1
 */
public class ConverterHelper {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Messages for this class.</p>
     */
    private static Messages messages =
            new Messages("org.apache.shale.resources.Bundle",
                         ConverterHelper.class.getClassLoader());


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Use the registered by-type <code>Converter</code> to convert the
     * specified String value to a corresponding Object value.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param type Type to which this value should be converted
     *  (used to select the appropriate by-type converter)
     * @param value Value to be converted
     *
     * @exception ConverterException if a conversion error occurs
     */
    public Object asObject(FacesContext context, Class type, String value) {

        if (String.class == type) {
            return value;
        }

        UIViewRoot root = context.getViewRoot();
        if (root == null) {
            root = new UIViewRoot();
        }
        
        return converter(context, type).getAsObject(context, root, value);

    }


    /**
     * <p>Use the registered by-type <code>Converter</code> to convert the
     * specified Object value to a corresponding String value.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param type Type from which this value should be converted
     *  (used to select the appropriate by-type converter)
     * @param value Value to be converted
     *
     * @exception ConverterException if a conversion error occurs
     */
    public String asString(FacesContext context, Class type, Object value) {

        if (value == null) {
            return null;
        } else if ((String.class == type) && (value instanceof String)) {
            return (String) value;
        }
        UIViewRoot root = context.getViewRoot();
        if (root == null) {
            root = new UIViewRoot();
        }

        return converter(context, type).getAsString(context, root, value);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return an appropriate <code>Converter</code> instance for the
     * specified type.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param type Type for which to retrieve a <code>Converter</code>
     *
     * @exception ConverterException if no <code>Converter</code> has been
     *  registered for the specified type
     */
    private Converter converter(FacesContext context, Class type) {

        if (type == null) {
            throw new ConverterException(messages.getMessage("convHelper.missing",
                                                             context.getViewRoot().getLocale()));
        }

        Converter converter = null;
        try {
            converter = context.getApplication().createConverter(type);
        } catch (FacesException e) {
            throw new ConverterException(e);
        }
        if (converter == null) {
            throw new ConverterException(messages.getMessage("convHelper.noConverter",
                                                             context.getViewRoot().getLocale(),
                                                             new Object[] { type.getName() }));
        }
        return converter;

    }


}
