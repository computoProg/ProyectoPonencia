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

package org.apache.shale.component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.faces.ShaleConstants;
import org.apache.shale.util.LoadBundle;
import org.apache.shale.util.Messages;
import org.apache.shale.util.TokenProcessor;

/**
 * <p>Component that renders a transaction token input field, and then
 * validates it on a subsequent form submit. The token component must
 * be the last input component child of the parent form to be processed.</p>
 *
 * $Id: Token.java 472288 2006-11-07 21:41:16Z rahul $
 */
public class Token extends UIInput {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Log instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(Token.class);


    /**
     * <p>The resource bundle <code>messageSummary</code> key.</p>
     */
    private static final String MESSAGE_SUMMARY_KEY = "token.summary.invalid";

    /**
     * <p>The resource bundle <code>messageDetail</code> key.</p>
     */
    private static final String MESSAGE_DETAIL_KEY = "token.detail.invalid";


    /**
     * <p>Local component attribute under which we store the token value
     * the first time it is generated.</p>
     */
    private static final String TOKEN_ATTRIBUTE_KEY = "org.apache.shale.Token.TOKEN_VALUE";


    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.resources.Bundle",
                   Token.class.getClassLoader());


    // ------------------------------------------------------------ Constructors

    /**
     * <p>A validation message summary override that can be used to change the default
     * validation message summary when the token verification fails.</p>
     */
    private String messageSummary = null;

    /**
     * <p>Returns the validation <code>messageSummary</code> used to create a
     * <code>FacesMessage.SEVERITY_ERROR</code>.</p>
     */
    public String getMessageSummary() {
        if (null != messageSummary) {
            return messageSummary;
        }
        ValueBinding valuebinding = getValueBinding("messageSummary");
        if (valuebinding != null) {
            return (String) valuebinding.getValue(getFacesContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Sets a <code>messageSummary</code> override used when reporting
     * a token verification failure.</p>
     *
     * @param message The new message summary
     */
    public void setMessageSummary(String message) {
       this.messageSummary = message;
    }


    /**
     * <p>A validation message detail override that can be used to change the default
     * validation message detail when the token verification fails.</p>
     */
    private String messageDetail = null;

    /**
     * <p>Returns the validation <code>messageDetail</code> used to create a
     * <code>FacesMessage.SEVERITY_ERROR</code>.</p>
     */
    public String getMessageDetail() {
        if (null != messageDetail) {
            return messageDetail;
        }
        ValueBinding valuebinding = getValueBinding("messageDetail");
        if (valuebinding != null) {
            return (String) valuebinding.getValue(getFacesContext());
        } else {
            return null;
        }
    }

    /**
     * <p>Sets a <code>messageDetail</code> override used when reporting
     * a token verification failure.</p>
     *
     * @param message The new message detail
     */
    public void setMessageDetail(String message) {
       this.messageDetail = message;
    }


    /**
     * <p>Create a default instance of this component.</p>
     */
    public Token() {
        setRendererType("org.apache.shale.Token");
    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the component family for this component.</p>
     */
    public String getFamily() {
        return "org.apache.shale.Token";
    }


    // --------------------------------------------------------- UIInput Methods


    /**
     * <p>Perform superclass validations, then ensure that the specified input
     * value is acceptable at this point in time.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public void validate(FacesContext context) {

        // If any of the other input components in this form triggered
        // validation errors, we do NOT want to validate the token component
        // here, because that would erase the saved token and prevent the
        // subsequent valid resubmit from succeeding.
        //
        // WARNING - for this test to be successful, the token component must
        // be the last input component child of the parent form to be
        // processed
        if (context.getMaximumSeverity() != null) {
            return;
        }

        super.validate(context);
        String token = (String) getValue();
        if (log.isDebugEnabled()) {
            log.debug("Validating token '" + token + "'");
        }
        TokenProcessor tp = getTokenProcessor(context);
        if (!tp.verify(context, token)) {
            if (log.isDebugEnabled()) {
                log.debug("  Validation failed!");
            }
            setValid(false);
            String summary = getErrorSummaryMessage(context);
            String detail = getErrorDetailMessage(context);
            FacesMessage message = new FacesMessage(summary, detail);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(getClientId(context), message);
        }

    }

    /**
     * <p>Returns the validation summary message.  The validation
     * <code>messageSummary</code> is evaluated in the following order:</p>
     * <ol>
     * <li>The <code>messageSummary</code> property on the {@link Token}
     *     component</li>
     * <li>A custom resource bundled registered in
     *     <code>faces-config.xml</code> using the message key of
     *     <strong>token.summary.invalid</strong>.
     * <p><blockquote><pre>
     *    &lt;application&gt;
     *       &lt;messageSummary-bundle&gt;org.acme.resources.Bundle&lt;/messageSummary-bundle&gt;
     *    &lt;/application&gt;
     *</pre></blockquote></p></li>
     * <li>The default will be taken from
     *     <strong>org.apache.shale.resources.Bundle</strong> packaged
     *     in the core Shale java archive.  The default message summary
     *     is "<strong>Invalid resubmit of the same form</strong>".</li>
     *</ol>
     *
     * @param context faces context
     * @return invalid token message
     */
    private String getErrorSummaryMessage(FacesContext context) {

        String msg = getMessageSummary();
        if (msg == null) {
            String bundleName = context.getApplication().getMessageBundle();
            if (bundleName != null) {
                LoadBundle loadBundle = new LoadBundle(bundleName);
                msg = (String) loadBundle.getMap().get(MESSAGE_SUMMARY_KEY);
            }
        }
        if (msg == null) {
            msg = messages.getMessage(MESSAGE_SUMMARY_KEY);
        }
        return msg;

    }


    /**
     * <p>Returns the validation detail message.  The validation
     * <code>messageDetail</code> is evaluated in the following order:</p>
     * <ol>
     * <li>The <code>messageDetail</code> property on the {@link Token}
     *     component</li>
     * <li>A custom resource bundled registered in
     *     <code>faces-config.xml</code> using the message key of
     *     <strong>token.detail.invalid</strong>.
     * <p><blockquote><pre>
     *    &lt;application&gt;
     *       &lt;messageSummary-bundle&gt;org.acme.resources.Bundle&lt;/messageSummary-bundle&gt;
     *    &lt;/application&gt;
     * </pre></blockquote></p></li>
     * <li>The default will be taken from
     *     <strong>org.apache.shale.resources.Bundle</strong>
     *     packaged in the core Shale java archive.</li>
     * <li>The default message detail is
     *     an empty string, ""</li>
     *</ol>
     *
     * @param context faces context
     * @return invalid token message
     */
    private String getErrorDetailMessage(FacesContext context) {

        String msg = getMessageDetail();
        if (msg == null) {
            String bundleName = context.getApplication().getMessageBundle();
            if (bundleName != null) {
                LoadBundle loadBundle = new LoadBundle(bundleName);
                msg = (String) loadBundle.getMap().get(MESSAGE_DETAIL_KEY);
            }
        }
        if (msg == null) {
            msg = messages.getMessage(MESSAGE_DETAIL_KEY);
        }
        return msg;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the transaction token value to be rendered for this occcurrence
     * of this component.  As a side effect, the transaction token value will
     * be saved for verification on a subsequent submit.</p>
     */
    public String getToken() {

        // Have we already generated a token value?  If so, use it
        String value = (String) getAttributes().get(TOKEN_ATTRIBUTE_KEY);
        if (value != null) {
             return value;
        }

        // Generate a new token value and cache it for reuse if the
        // current view is rerendered
        FacesContext context = FacesContext.getCurrentInstance();
        TokenProcessor tp = getTokenProcessor(context);
        String token = tp.generate(context);
        getAttributes().put(TOKEN_ATTRIBUTE_KEY, token);
        if (log.isDebugEnabled()) {
            log.debug("Generating token '" + token + "'");
        }
        return token;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Retrieve the {@link TokenProcessor} instance for this application,
     * creating and caching a new one if necessary.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
     private TokenProcessor getTokenProcessor(FacesContext context) {

         TokenProcessor tp = (TokenProcessor) context.getExternalContext().
           getApplicationMap().get(ShaleConstants.TOKEN_PROCESSOR);
         if (tp == null) {
             tp = new TokenProcessor();
             context.getExternalContext().
               getApplicationMap().put(ShaleConstants.TOKEN_PROCESSOR, tp);
         }
         return tp;

     }

    /**
     * <p>Restores the components state.</p>
     *
     * @param context FacesContext for the current request
     * @param obj State to be restored
     */
    public void restoreState(FacesContext context, Object obj) {
        Object[] aobj = (Object[]) obj;
        super.restoreState(context, aobj[0]);

        messageSummary = ((String) aobj[1]);
        messageDetail = ((String) aobj[2]);
    }

    /**
     * <p>Saves the components state.</p>
     *
     * @param context FacesContext for the current request
     */
    public Object saveState(FacesContext context) {
        Object[] aobj = new Object[3];
        aobj[0] = super.saveState(context);
        aobj[1] = messageSummary;
        aobj[2] = messageDetail;

        return aobj;
    }


}
