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
package org.apache.shale.clay.convert;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.shale.util.Messages;

/**
 * <p>Converts a <code>String</code> to a <code>String[]</code> or
 * vise versa.  This converter is targeted at supporting the
 * myfaces trinidad partialTriggers component properties.
 * The default element <code>delimiter</code> is none and the default
 * element <code>separator</code> is a space.</p>
 */
public class StringArrayConverter implements Converter {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", StringArrayConverter.class
                    .getClassLoader());

    /**
     * <p>Begining and ending element delimiter.  The default
     * is <code>null</code> meaning no delimiter.</p>
     */
    private Character delimiter = null;

    /**
     * <p>The element seperator.  The default is a space.</p>
     */
    private Character separator = new Character(' ');

    /**
     * @return the element begining and ending delimiter
     */
    public Character getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter element begining and ending delimiter char
     */
    public void setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return the character used to delimit elements
     */
    public Character getSeparator() {
        return separator;
    }

    /**
     * @param separator the character used to delimit elements
     */
    public void setSeparator(Character separator) {
        this.separator = separator;
    }


    /**
     * <p>Converts the <code>value</code> into a string array using
     * the element <code>delimiter</code> and element <code>separator</code>.</p>
     *
     * @param facesContext faces context
     * @param component value holder component
     * @param value source value to converter to a String[]
     * @return the target type is a <code>String[]</code>
     */
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {

        List elements = new ArrayList();
        StringBuffer buff = new StringBuffer(value != null ? value : "");
        boolean isBlockOn = false;
        for (int i = buff.length() - 1; i > -1; i--) {
            if (delimiter != null
                    && buff.charAt(i) == delimiter.charValue()) {

                if (!isBlockOn) {
                    buff.deleteCharAt(i);
                } else {
                    elements.add(0, buff.substring(i + 1));
                    buff.delete(i, buff.length());
                }
                isBlockOn = !isBlockOn;
            } else if (separator != null
                    && buff.charAt(i) == separator.charValue()
                    && !isBlockOn) {

                if (i + 1 < buff.length()) {
                   elements.add(0, buff.substring(i + 1));
                }
                buff.delete(i, buff.length());
            }
        }

        if (buff.length() > 0) {
            elements.add(0, buff.toString());
        }

        String[] values = new String[elements.size()];
        elements.toArray(values);

        return values;
    }

    /**
     * <p>Converts a string array into a value delimited string.  The <code>delimiter</code>
     * and <code>separator</code> properties are used to distinguish each element.</p>
     *
     * @param facesContext faces context
     * @param component value owning component
     * @param value source String array that is converter into a tokenized String
     * @return delimited string holding all values of the source string array
     */
    public String getAsString(FacesContext facesContext, UIComponent component,
            Object value) {

        if (value == null) {
            return "";
        }

        if (value instanceof String) {
            return (String) value;
        }

        if (!(value instanceof String[])) {
            throw new ConverterException(messages
                    .getMessage("string.array.converter.invalid.type"));
        }
        StringBuffer buff = new StringBuffer();
        String[] values = (String[]) value;
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                buff.append(separator);
            }

            buff.append(delimiter != null ? delimiter.toString() : "")
                 .append(values[i])
                 .append(delimiter != null ? delimiter.toString() : "");
        }
        return buff.toString();

    }

}
