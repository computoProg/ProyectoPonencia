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

package org.apache.shale.taglib;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.shale.component.Token;

/**
 * <p>JSP custom tag for the {@link Token} component.</p>
 *
 * $Id: TokenTag.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class TokenTag extends UIComponentTag {


    /**
     * <p>Pushs the tag attributes to the {@link Token}'s
     * component properties.</p>
     *
     * @param component Component for which to set properties
     */
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        FacesContext context = getFacesContext();

        if (messageSummary != null) {
            if (this.isValueReference(messageSummary)) { // needs to be non-static
                ValueBinding vb = context.getApplication().createValueBinding(messageSummary);
                component.setValueBinding("messageSummary", vb);
            } else {
                component.getAttributes().put("messageSummary", messageSummary);
            }
        }

        if (messageDetail != null) {
            if (this.isValueReference(messageDetail)) { // needs to be non-static
                ValueBinding vb = context.getApplication().createValueBinding(messageDetail);
                component.setValueBinding("messageDetail", vb);
            } else {
                component.getAttributes().put("messageDetail", messageDetail);
            }
        }

    }


    /**
     * <p>Return the required component type.</p>
     */
    public String getComponentType() {
        return "org.apache.shale.Token";
    }


    /**
     * <p>Return the required renderer type.</p>
     */
    public String getRendererType() {
        return "org.apache.shale.Token";
    }

    /**
     * <p>A validation messageSummary override that can be used to change the default
     * validation messageSummary when the token verification fails.</p>
     */
    private String messageSummary = null;

    /**
     * <p>Sets a <code>messageSummary</code> override used when reporting
     * a {@link org.apache.shale.component.Token} verification failure.</p>
     *
     * @param message The new message summary
     */
    public void setMessageSummary(String message) {
       this.messageSummary = message;
    }

    /**
     * <p>Returns a <code>messageSummary</code> override used when reporting
     * a {@link org.apache.shale.component.Token} verification failure.</p>
     */
    public String getMessageSummary() {
       return messageSummary;
    }


    /**
     * <p>A validation messageDetail override that can be used to change the default
     * validation messageDetail when the token verification fails.</p>
     */
    private String messageDetail = null;

    /**
     * <p>Sets a <code>messageDetail</code> override used when reporting
     * a {@link org.apache.shale.component.Token} verification failure.</p>
     *
     * @param message The new message detail
     */
    public void setMessageDetail(String message) {
       this.messageDetail = message;
    }

    /**
     * <p>Returns a <code>messageDetail</code> override used when reporting
     * a {@link org.apache.shale.component.Token} verification failure.</p>
     */
    public String getMessageDetail() {
       return messageDetail;
    }


}
