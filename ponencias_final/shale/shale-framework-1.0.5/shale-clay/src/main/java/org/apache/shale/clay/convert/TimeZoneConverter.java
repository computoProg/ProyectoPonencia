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
 * $Id: TimeZoneConverter.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.convert;

import java.util.TimeZone;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * <p>Converts a time zone id literal string into the
 * <code>TimeZone</code> and back to a string.</p>
 */
public class TimeZoneConverter implements Converter {

    /**
     * <p>Converts the string zone ID into a TimeZone object.</p>
     *
     * @param facesContext jsf faces context
     * @param component converter assigned
     * @param value to be converterd
     * @return strong typed value
     */
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {

        TimeZone zone = TimeZone.getTimeZone(value);
        return zone;
    }

    /**
     * <p>Converts a TimeZone object into the string zone id.</p>
     *
     * @param facesContext jsf faces context
     * @param component converter assigned
     * @param value to be converterd
     * @return string represention of the value
     */
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {

        TimeZone zone = (TimeZone) value;
        if (value != null) {
           return zone.getID();
        }

        return null;
    }

}
