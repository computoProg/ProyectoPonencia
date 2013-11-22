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
 *
 * $Id: MyConverter.java 464373 2006-10-16 04:21:54Z rahul $
 */

package org.apache.shale.tiger.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apache.shale.tiger.register.FacesConverter;

/**
 * <p>Test converter for Shale Tiger unit tests.</p>
 */
@FacesConverter("foo.MyConverter")
public class MyConverter implements Converter {
    
    public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {
        throw new UnsupportedOperationException();
    }

    public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String string) {
        throw new UnsupportedOperationException();
    }

}
