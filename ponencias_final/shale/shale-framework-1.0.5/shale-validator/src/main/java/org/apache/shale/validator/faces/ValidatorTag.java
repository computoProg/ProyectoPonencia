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

package org.apache.shale.validator.faces;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.validator.Validator;
import javax.servlet.jsp.JspException;

import org.apache.shale.util.Tags;
import org.apache.shale.validator.CommonsValidator;

/**
 * The tag class for the <code>s:commonsValidator</code> tag.
 *
 * $Id: ValidatorTag.java 481413 2006-12-01 21:48:06Z rahul $
 */
public class ValidatorTag extends javax.faces.webapp.ValidatorTag  {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -5007063635477571688L;


    /**
     * <p>The <code>type</code> attribute.
     */
   private String type;


    /**
     * <p>The <code>min</code> attribute.
     */
   private String min;


    /**
     * <p>The <code>max</code> attribute.
     */
   private String max;


    /**
     * <p>The <code>minlength</code> attribute.
     */
   private String minlength;


    /**
     * <p>The <code>maxlength</code> attribute.
     */
   private String maxlength;


    /**
     * <p>The <code>datePatternStrict</code> attribute.
     */
   private String datePatternStrict;


    /**
     * <p>The <code>mask</code> attribute.
     */
   private String mask;


    /**
     * <p>The <code>message</code> attribute.
     */
   private String message;


    /**
     * <p>The <code>arg</code> attribute.
     */
   private String arg;


    /**
     * <p>The <code>client</code> attribute.
     */
   private String client;


    /**
     * <p>The <code>server</code> attribute.
     */
   private String server;


   /**
    * <p>The parameters to pass to the validator.</p>
    */
   private LinkedHashMap params;


    /**
     * <p>This constructor obtains a reference to the
     *    tag utility object, which is a JSF managed bean.
     */
   public ValidatorTag() {
      setValidatorId("org.apache.shale.CommonsValidator");
   }


    /**
     * <p>Setter method for the <code>type</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setType(String newValue) {
      type = newValue;
   }


    /**
     * <p>Setter method for the <code>min</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMin(String newValue) {
      min = newValue;
   }


    /**
     * <p>Setter method for the <code>max</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMax(String newValue) {
      max = newValue;
   }


    /**
     * <p>Setter method for the <code>minlength</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMinlength(String newValue) {
      minlength = newValue;
   }


    /**
     * <p>Setter method for the <code>maxlength</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMaxlength(String newValue) {
      maxlength = newValue;
   }


    /**
     * <p>Setter method for the <code>setDatePatternStrict</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setDatePatternStrict(String newValue) {
      datePatternStrict = newValue;
   }


    /**
     * <p>Setter method for the <code>mask</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMask(String newValue) {
      mask = newValue;
   }


    /**
     * <p>Setter method for the <code>message</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setMessage(String newValue) {
      message = newValue;
   }


    /**
     * <p>Setter method for the <code>arg</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setArg(String newValue) {
      arg = newValue;
   }


    /**
     * <p>Setter method for the <code>client</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setClient(String newValue) {
      client = newValue;
   }


    /**
     * <p>Setter method for the <code>server</code> attribute.
     *
     * @param newValue the new attribute value
     */
   public void setServer(String newValue) {
      server = newValue;
   }


   /**
    * <p>Adds a parameter to pass to the Commons Validator that will be
    * used.</p>
    *
    * @param name    the name of the parameter to pass to the validator.
    * @param value   the value of the parameter to pass to the validator.
    */
   public void addParam(String name, String value) {
      params.put(name, value);
   }

   /**
    * <p>Overridden to prevent the call to {@link #createValidator} from
    * occurring here.</p>
    *
    * @return EVAL_BODY_INCLUDE
    */
   public int doStartTag() throws JspException {
      // reset the params for each use of this tag
      params = new LinkedHashMap();
      return EVAL_BODY_INCLUDE;
   }


   /**
    * <p>Executes the code that was skipped in the <code>doStartTag()</code>
    * override.</p>
    *
    * @exception JspException if a JSP processing error occurs
    */
   public int doEndTag() throws JspException {
      super.doStartTag();
      return super.doEndTag();
   }


   /**
     * <p>Create a validator by calling <code>super.createValidator()</code>.
     *    This method initializes that validator with the tag's attribute
     *    values.
     *
     * @exception JspException if a JSP processing error occurs
     */
   public Validator createValidator() throws JspException {
      CommonsValidator validator = (CommonsValidator) super.createValidator();

      // parameters for specific validators
      if (min != null) {
          validator.setMin(min);
      }
      if (max != null) {
          validator.setMax(max);
      }
      if (minlength != null) {
          validator.setMinLength(minlength);
      }
      if (maxlength != null) {
          validator.setMaxLength(maxlength);
      }
      if (datePatternStrict != null) {
          validator.setDatePatternStrict(datePatternStrict);
      }
      if (mask != null) {
          validator.setMask(mask);
      }
      if (arg != null) {
          validator.setArg(arg);
      }

      Tags tagUtils = new Tags();

      // these properties require early binding
      validator.setType(tagUtils.evalString(type));
      validator.setMessage(tagUtils.evalString(message));
      validator.setClient(tagUtils.evalBoolean(client));
      validator.setServer(tagUtils.evalBoolean(server));

      tagUtils = null;

      // pass the parameters specified through <s:validatorVar>
      // if these vars contain binding expressions, they are
      // evaluated late
      if (params != null) {
         for (Iterator iterator = params.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry e = (Map.Entry) iterator.next();
            Object key = e.getKey();
            Object value = e.getValue();
            validator.getVars().put(key, value);
         }
      }

      return validator;
   }


    /**
     * <p>Sets all instance objects representing tag attribute values
      *    to <code>null</code>.
     */
   public void release() {
      type = null;
      params = null;
      min = null;
      max = null;
      minlength = null;
      maxlength = null;
      datePatternStrict = null;
      mask = null;
      message = null;
      arg = null;
      client = null;
      server = null;
   }

}
