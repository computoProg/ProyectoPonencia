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

package org.apache.shale.validator.tag;

import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.ConverterTag;
import javax.faces.webapp.UIComponentTag;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.validator.converter.IntegerConverter;

/**
 * <p>JSP custom action for <code>IntegerConverter</code>.</p>
 */
public final class IntegerConverterTag extends ConverterTag {
    
    
    // -------------------------------------------------------- Static Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;


    /**
     * <p>Helper object for performing conversions.</p>
     */
    private static final ConverterHelper HELPER =
            new ConverterHelper();


    // -------------------------------------------------------------- Properties


    private String locale = null;
    public void setLocale(String locale) {
        this.locale = locale;
    }


    private String message = null;
    public void setMessage(String message) {
        this.message = message;
    }


    private String pattern = null;
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    // ------------------------------------------------------------- Tag Methods


    /**
     * <p>Release resources allocated for this instance.</p>
     */
    public void release() {

        locale = null;
        message = null;
        pattern = null;

        super.release();

    }


    // ---------------------------------------------------- ConverterTag Methods


    /**
     * <p>Create and return a new <code>Converter</code> instance to be
     * registered on our corresponding <code>UIComponent</code>.</p>
     */
    protected Converter createConverter() {

        FacesContext context = FacesContext.getCurrentInstance();
        IntegerConverter converter = new IntegerConverter();

        if (locale != null) {
            Locale value = null;
            if (UIComponentTag.isValueReference(locale)) {
                ValueBinding vb = context.getApplication().createValueBinding(locale);
                value = (Locale) vb.getValue(context);
            } else {
                value = (Locale) HELPER.asObject(context, Locale.class, locale);
            }
            converter.setLocale(value);
        }

        if (pattern != null) {
            String value = null;
            if (UIComponentTag.isValueReference(message)) {
                ValueBinding vb = context.getApplication().createValueBinding(pattern);
                value = (String) vb.getValue(context);
            } else {
                value = pattern;
            }
            converter.setPattern(value);
        }

        return converter;

    }


}
