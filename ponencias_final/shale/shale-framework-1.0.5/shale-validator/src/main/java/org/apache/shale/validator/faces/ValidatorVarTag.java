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

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;

/**
 * The tag class for the <code>s:validatorVar</code> tag, used to specify
 * parameters to specific validators of Commons Validator.
 * <p/>
 * $Id: ValidatorVarTag.java 465067 2006-10-17 21:45:17Z rahul $
 */
public class ValidatorVarTag extends TagSupport {

   /**
    * Serial version UID.
    */
   private static final long serialVersionUID = -7534191231970388638L;


   /**
    * The parent validator tag.
    */
   private ValidatorTag commonsValidatorTag = null;


   /**
    * The name of the var to pass to the validator.
    */
   private String name;


   /**
    * The value of the var to pass to the validator.
    */
   private String value;


   /**
    * Keeps track of the &lt;s:commonsValidator&gt; tag.
    * <p/>
    *
    * @param tag the parent tag of this tag instance.
    */
   public void setParent(Tag tag) {
      super.setParent(tag);
      if (tag instanceof ValidatorTag) {
         commonsValidatorTag = (ValidatorTag) tag;
      } else {
         throw new IllegalStateException(
               "This tag must be used inside a <s:commonsValidator> tag");
      }
   }


   /**
    * Pass the validation parameter to the commonsValidatorTag that immediately
    * surrounds this tag.
    *
    * @exception JspException if a JSP processing error occurs
    * @return Request to evaluate the page
    */
   public int doEndTag() throws JspException {
      commonsValidatorTag.addParam(name, value);
      return EVAL_PAGE;
   }


   //*************************************************************** Accessors


   /**
    * The var name.
    *
    * @param name The variable name
    */
   public void setName(String name) {
      this.name = name;
   }


   /**
    * The var value.
    *
    * @param value The variable value
    */
   public void setValue(String value) {
      this.value = value;
   }


}
