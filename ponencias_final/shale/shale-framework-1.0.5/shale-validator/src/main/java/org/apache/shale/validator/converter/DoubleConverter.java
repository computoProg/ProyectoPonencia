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

import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import org.apache.commons.validator.routines.DoubleValidator;

/**
 * <p>JavaServer Faces <code>Converter</code> for Double (and int)
 * instances.  Parsing and formatting is controlled by the properties
 * that are set on a Converter instance.</p>
 */
public final class DoubleConverter extends AbstractConverter {
    

    // -------------------------------------------------------- Static Variables


    /**
     * <p>The Apache Commons Validator instance we will use to perform
     * our formatting and parsing.</p>
     */
    private static final DoubleValidator INSTANCE =
            DoubleValidator.getInstance();



    // -------------------------------------------------------------- Properties


    /**
     * <p>The <code>Locale</code> to apply to this conversion, if any.</p>
     */
    private Locale locale = null;


    /**
     * <p>Return the <code>Locale</code> to apply to this conversion, or
     * <code>null</code> to use the <code>Locale</code> for the current view.</p>
     */
    public Locale getLocale() {
        return this.locale;
    }


    /**
     * <p>Set the <code>Locale</code> to apply to this conversion, or
     * <code>null</code> to use the <code>Locale</code> for the current view.</p>
     *
     * @param locale The new Locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    /**
     * <p>The formatting pattern to apply to this conversion, if any.</p>
     */
    private String pattern = null;


    /**
     * <p>Return the <code>java.text.NumberFormat</code> pattern to apply
     * to this conversion, or <code>null</code> for using no pattern.</p>
     */
    public String getPattern() {
        return this.pattern;
    }


    /**
     * <p>Set the <code>java.text.NumberFormat</code> pattern to apply to
     * this conversion, or <code>null</code> for using no pattern.</p>
     *
     * @param pattern The new formatting pattern
     */
    public void setPattern(String pattern) {
        this.pattern = null;
    }


    // ------------------------------------------------------- Converter Methods


    /** {@inheritDoc} */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException(message(context, "common.null"));
        }

        try {
            if (locale == null) {
                return INSTANCE.validate(value, pattern, context.getViewRoot().getLocale());
            } else {
                return INSTANCE.validate(value, pattern, locale);
            }
        } catch (Exception e) {
            throw new ConverterException(e);
        }

    }


    /** {@inheritDoc} */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException(message(context, "common.null"));
        }

        try {
            if (locale == null) {
                return INSTANCE.format(value, pattern, context.getViewRoot().getLocale());
            } else {
                return INSTANCE.format(value, pattern, locale);
            }
        } catch (Exception e) {
            throw new ConverterException(e);
        }

    }



    // ----------------------------------------------------- StateHolder Methods


    /** {@inheritDoc} */
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        super.restoreState(context, values[0]);
        this.locale = (Locale) values[1];
        this.pattern = (String) values[2];
    }


    /** {@inheritDoc} */
    public Object saveState(FacesContext context) {
        Object[] values = new Object[3];
        values[0] = super.saveState(context);
        values[1] = this.locale;
        values[2] = this.pattern;
        return values;
    }


}
