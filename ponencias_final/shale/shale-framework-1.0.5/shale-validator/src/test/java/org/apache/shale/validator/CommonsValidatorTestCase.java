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

package org.apache.shale.validator;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContextEvent;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.validator.ValidatorAction;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;
import org.apache.shale.validator.faces.ValidatorScript;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.util.Messages;


/**
 * <p>Test case for <code>CommonsValidator</code>.</p>
 */
public class CommonsValidatorTestCase extends AbstractJsfTestCase {

    /**
     * <p>Validator message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.validator.messages",
                   CommonsValidatorTestCase.class.getClassLoader());
    
    protected static final Object[] COMPONENTS = {
        new String[] {"javax.faces.NamingContainer", "javax.faces.component.UINamingContainer"},
        new String[] {"javax.faces.HtmlInputText", "javax.faces.component.html.HtmlInputText"},
        new String[] {"org.apache.shale.ValidatorScript", "org.apache.shale.validator.faces.ValidatorScript"},
        new String[] {"javax.faces.HtmlForm", "javax.faces.component.html.HtmlForm"},

    };

    // load the mock component config data
    protected void loadComponents() {
       for (int i = 0; i < COMPONENTS.length; i++) {
          application.addComponent(((String[])COMPONENTS[i])[0], ((String[])COMPONENTS[i])[1]);
       }
    }

      
    protected static final Object[] CONVERTERS = {
               new String[] {"javax.faces.DateTime", "javax.faces.convert.DateTimeConverter"}
    };    
    
    protected void loadConverters() {
        for (int i = 0; i < CONVERTERS.length; i++) {
           application.addConverter(((String[])CONVERTERS[i])[0], ((String[])CONVERTERS[i])[1]);
        }
    }
    
    public static final Object[] VALIDATORS = {
        new String[] {"org.apache.shale.CommonsValidator", "org.apache.shale.validator.CommonsValidator"}
    };     

    protected void loadValidators() {
        for (int i = 0; i < VALIDATORS.length; i++) {
           application.addValidator(((String[])VALIDATORS[i])[0], ((String[])VALIDATORS[i])[1]);
        }
    }
   
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public CommonsValidatorTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Instance Variables


    // ValidatorLifecycleListener used to load configuration resources
    ValidatorLifecycleListener listener = null;


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        servletContext.addInitParameter
            (Globals.VALIDATOR_RULES,
             Globals.DEFAULT_VALIDATOR_RULES +
             ", /org/apache/shale/validator/custom-rules.xml");
        listener = new ValidatorLifecycleListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));
        
        loadComponents();
        loadConverters();
        loadValidators();
        
        facesContext.getViewRoot().setLocale(new Locale("de", "DE"));
    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(CommonsValidatorTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // ------------------------------------------------------------ Test Methods


    // Test access to the 'required' validation rule
    public void testRequired() {
        
        ValidatorAction va = CommonsValidator.getValidatorAction("required");
        assertNotNull(va);
        assertEquals("required", va.getName());
        assertEquals("org.apache.shale.validator.CommonsValidator",
                va.getClassname());
        assertEquals("isSupplied",va.getMethod());
        

    }

    
    //test validation rule
    public static boolean isValid(String value) {
        if (value != null && value.equals("blue") ||
            value.equals("red"))
           return true;
        else
           return false;
    }

    //test validation rule
    public static boolean isValid(String value, String colors) {
       StringTokenizer tokenizer = new StringTokenizer(colors, ",");
       
       while (tokenizer.hasMoreTokens()) {
          String color = tokenizer.nextToken().trim();
          if (value.equals(color))
             return true;
       }
    
       return false;
    }

    
    
    // Test access to a custom validation rule
    public void testCustom1() {

        application.setMessageBundle("org.apache.shale.validator.messages");

        
        ValidatorAction va = CommonsValidator.getValidatorAction("testRule1");
        assertNotNull(va);
        assertEquals("testRule1", va.getName());
        assertEquals("org.apache.shale.validator.CommonsValidatorTestCase",
                va.getClassname());
        assertEquals("isValid", va.getMethod());
        
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("testRule1", form1);
        component1.setRequired(true);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "testRule1");
        
        //set the value
        component1.setSubmittedValue("blue");
        
        // invoke component validation
        component1.validate(facesContext);
            
        // check for a error message
        checkMessages(component1);
       
    }

    
    // Test access to a custom validation rule
    public void testCustom2() {
       
        ValidatorAction va = CommonsValidator.getValidatorAction("testRule2");
        assertNotNull(va);
        assertEquals("testRule2", va.getName());
        assertEquals("org.apache.shale.validator.CommonsValidatorTestCase",
                va.getClassname());
        assertEquals("isValid", va.getMethod());
        
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("testRule2", form1);
        component1.setRequired(true);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "testRule2");

        // <s:commonsValidator type="testRule2" message="{0} must be one of the following: {2}." arg="Favorite Color">
        //    <s:validatorVar name="enumerations" value="black, yellow, red"/>
        // </s:commonsValidator>
        validator1.getVars().put("enumerations", "black, yellow, red");
        validator1.setMessage("{0} must be one of the following: {2}.");
        validator1.setArg("Favorite Color");
        
        //set the value
        component1.setSubmittedValue("blue");
        
        // invoke component validation
        component1.validate(facesContext);
            
        // check for a error message
        checkMessage("Favorite Color must be one of the following: black, yellow, red.", component1);
       
    }

    
    // test the required rule with two forms
    public void testValidateRequired() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("requiredField", form1);
        component1.setRequired(true);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "required");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("requiredField", form2);
        component2.setRequired(true);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "required");
       
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("   ");
        // invoke component validation
        component1.validate(facesContext);
                        
        
        ValidatorAction va = CommonsValidator.getValidatorAction("required");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(), 
                facesContext.getViewRoot().getLocale(), 
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_required()",
           "test2_required()",
           "validateRequired(form)",
           "function validateRequired(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    

    // test the maxlength rule with two forms
    public void testValidateMaxlength() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("maxlength", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "maxlength");
        validator1.setMaxLength(new Integer(5).toString());

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("maxlength", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "maxlength");
        validator2.setMaxLength(new Integer(5).toString());
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("0123456789");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("maxlength");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId(),
                validator1.getVars().get(CommonsValidator.MAX_LENGTH_VARNAME)});

        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_maxlength()",
           "test2_maxlength()",
           "validateMaxLength(form)",
           "function validateMaxLength(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }

    
    // test the minlength rule with two forms
    public void testValidateMinlength() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("minlength", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "minlength");
        validator1.setMinLength(new Integer(5).toString());

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("minlength", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "minlength");
        validator2.setMinLength(new Integer(5).toString());
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("0");
        // invoke component validation
        component1.validate(facesContext);

        ValidatorAction va = CommonsValidator.getValidatorAction("minlength");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId(),
                validator1.getVars().get(CommonsValidator.MIN_LENGTH_VARNAME)});

        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_minlength()",
           "test2_minlength()",
           "validateMinLength(form)",
           "function validateMinLength(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }

    
    // test the mask rule with two forms
    public void testValidateMask() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("mask", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "mask");
        validator1.setMask("^[a-zA-Z]*$");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("mask", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "mask");
        validator2.setMask("^[a-zA-Z]*$");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("123Test");
        // invoke component validation
        component1.validate(facesContext);
                      
        ValidatorAction va = CommonsValidator.getValidatorAction("mask");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_mask()",
           "test2_mask()",
           "validateMask(form)",
           "function validateMask(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    
  
    // test the byte rule with two forms
    public void testValidateByte() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("byte", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "byte");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("byte", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "byte");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("129");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("byte");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_ByteValidations()",
           "test2_ByteValidations()",
           "validateByte(form)",
           "function validateByte(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
   

    // test the short rule with two forms
    public void testValidateShort() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("short", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "short");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("short", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "short");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("32768");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("short");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_ShortValidations()",
           "test2_ShortValidations()",
           "validateShort(form)",
           "function validateShort(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
   
    // test the integer rule with two forms
    public void testValidateInteger() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("integer", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "integer");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("integer", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "integer");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("2147483648");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("integer");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_IntegerValidations()",
           "test2_IntegerValidations()",
           "validateInteger(form)",
           "function validateInteger(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    

    // test the float rule with two forms
    public void testValidateFloat() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("float", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "float");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("float", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "float");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("ABCD");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("float");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_FloatValidations()",
           "test2_FloatValidations()",
           "validateFloat(form)",
           "function validateFloat(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }


    // test the double rule with two forms
    public void testValidateDouble() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("double", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "double");
        validator1.setClient(Boolean.FALSE);

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("double", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "double");
        validator2.setClient(Boolean.FALSE);
        
        
        //set the value
        component1.setSubmittedValue("ABCD");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("double");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
        
    }

    // test the long rule with two forms
    public void testValidateLong() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("long", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "long");
        validator1.setClient(Boolean.FALSE);

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("long", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "long");
        validator2.setClient(Boolean.FALSE);
        
        
        //set the value
        component1.setSubmittedValue("99999999999999999999999999999999999999999999999999999999");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("long");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
        
    }
    
    
    
    
    // test the date rule with two forms
    public void testValidateDate() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("date", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "date");
        validator1.setDatePatternStrict("MM/dd/yyyy");

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("date", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "date");
        validator2.setDatePatternStrict("MM/dd/yyyy");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("04/31/2006");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("date");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_DateValidations()",
           "test2_DateValidations()",
           "validateDate(form)",
           "function validateDate(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    
    // test the intRange rule with two forms
    public void testValidateIntRange() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("intRange", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "intRange");
        validator1.setMax(new Integer(5).toString());
        validator1.setMin(new Integer(1).toString());

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("intRange", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "intRange");
        validator2.setMax(new Double(5).toString());
        validator2.setMin(new Double(1).toString());
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("6");
        // invoke component validation
        component1.validate(facesContext);
                        
        // check for a error message
        String min = (String) validator1.getVars().get(CommonsValidator.MIN_VARNAME);
        String max = (String) validator1.getVars().get(CommonsValidator.MAX_VARNAME);
        
        ValidatorAction va = CommonsValidator.getValidatorAction("intRange");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId(), min, max});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_intRange()",
           "test2_intRange()",
           "validateIntRange(form)",
           "function validateIntRange(form)",
           "function jcv_retrieveFormName(form)",
           "test1_IntegerValidations()",
           "test2_IntegerValidations()"

        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }

 
    // test the floatRange rule with two forms
    public void testValidateFloatRange() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("floatRange", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "floatRange");
        validator1.setMax(new Double(5).toString());
        validator1.setMin(new Double(1).toString());

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("floatRange", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "floatRange");
        validator2.setMax(new Double(5).toString());
        validator2.setMin(new Double(1).toString());
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("6");
        // invoke component validation
        component1.validate(facesContext);
                        
        // check for a error message
        String min = (String) validator1.getVars().get(CommonsValidator.MIN_VARNAME);
        String max = (String) validator1.getVars().get(CommonsValidator.MAX_VARNAME); 
        ValidatorAction va = CommonsValidator.getValidatorAction("floatRange");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId(), min, max});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_floatRange()",
           "test2_floatRange()",
           "validateFloatRange(form)",
           "function validateFloatRange(form)",
           "function jcv_retrieveFormName(form)",
           "test1_FloatValidations()",
           "test2_FloatValidations()"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    
    // test the doubleRange rule with two forms
    public void testValidateDoubleRange() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("doubleRange", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "doubleRange");
        validator1.setMax(new Double(5).toString());
        validator1.setMin(new Double(1).toString());

       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("doubleRange", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "doubleRange");
        validator2.setMax(new Double(5).toString());
        validator2.setMin(new Double(1).toString());
                
        //set the value
        component1.setSubmittedValue("6");
        // invoke component validation
        component1.validate(facesContext);
                        
        // check for a error message
        // check for a error message
        String min = (String) validator1.getVars().get(CommonsValidator.MIN_VARNAME);
        String max = (String) validator1.getVars().get(CommonsValidator.MAX_VARNAME); 

        ValidatorAction va = CommonsValidator.getValidatorAction("doubleRange");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId(), min, max});
        
        // check for a error message
        checkMessage(actualMsg, component1);
    }

    

    // test the creditCard rule with two forms
    public void testValidateCreditCard() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("creditCard", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "creditCard");
       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("creditCard", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "creditCard");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("11111111111111111111");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("creditCard");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_creditCard()",
           "test2_creditCard()",
           "validateCreditCard(form)",
           "function validateCreditCard(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    
    
    // test the email rule with two forms
    public void testValidateEmail() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);

        UINamingContainer namingContainer = (UINamingContainer) application.createComponent("javax.faces.NamingContainer");
        
        namingContainer.setId(root.createUniqueId());
        root.getChildren().add(root.getChildCount(), namingContainer);

        //create a form 1
        UIComponent form1 = this.createForm("test1", namingContainer);
        
        
        //create a dummy component 1
        HtmlInputText component1 = createInputText("email", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "email");
       
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("email", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "email");
        
        //create a script component
        ValidatorScript script = createValidatorScript(root);
        
        //set the value
        component1.setSubmittedValue("xyz@abc");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("email");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
        // render the javascript for the form
        StringBuffer htmlSnippet = encode(script);
        
        // search tokens to test for in the javascript
        String[] searchTokens = {
           "test1_email()",
           "test2_email()",
           "validateEmail(form)",
           "function validateEmail(form)",
           "function jcv_retrieveFormName(form)"
        };
        
        //look for all search tokens
        checkScript(htmlSnippet, searchTokens);
        
        //System.out.println(htmlSnippet.toString());
    }
    

    // test the url rule with two forms
    public void testValidateUrl() throws Exception {
        
        // find the view root
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        //create a form 1
        UIComponent form1 = this.createForm("test1", root);
         
        //create a dummy component 1
        HtmlInputText component1 = createInputText("url", form1);
               
        //create a required field/server rule 
        CommonsValidator validator1 = createValidator(component1, "url");
        validator1.setClient(Boolean.FALSE);
        
        //create a form 2
        UIComponent form2 = this.createForm("test2", root);

        //create a dummy component 2
        HtmlInputText component2 = createInputText("url", form2);
               
        //create a required field/server rule 
        CommonsValidator validator2 = createValidator(component2, "url");
        validator2.setClient(Boolean.FALSE);
                
        //set the value
        component1.setSubmittedValue("xyz://www.wyantdata.com");
        // invoke component validation
        component1.validate(facesContext);
                        
        ValidatorAction va = CommonsValidator.getValidatorAction("url");
        assertNotNull("validator action", va);
        String actualMsg = messages.getMessage(va.getMsg(),
                facesContext.getViewRoot().getLocale(),
                new Object[] {component1.getId()});
        
        // check for a error message
        checkMessage(actualMsg, component1);
          
    }

    
    private void checkScript(StringBuffer htmlSnippet, String[] searchTokens) {
        for (int i = 0; i < searchTokens.length; i++) {
           Assert.assertTrue("Not Found: " + searchTokens[i], (htmlSnippet.indexOf(searchTokens[i]) > -1));    
        }
    }
    
    private HtmlForm createForm(String id, UIComponent root) {
        HtmlForm component = (HtmlForm) facesContext.getApplication().createComponent("javax.faces.HtmlForm");
        assertNotNull(component);
        
        component.setId(id);
        component.getAttributes().put("onsubmit", "return validateForm(this);");
        
        root.getChildren().add(root.getChildCount(), component);        
        return component;            
    }
    
    
    private HtmlInputText createInputText(String id, UIComponent parent) {
        HtmlInputText component = (HtmlInputText) facesContext.getApplication().createComponent("javax.faces.HtmlInputText");
        assertNotNull(component);

        component.setId(id); 
        parent.getChildren().add(parent.getChildCount(), component);
        
        return component;
    }
    
   
    private ValidatorScript createValidatorScript(UIViewRoot root) {
        //create a script component
        ValidatorScript script = (ValidatorScript) facesContext.getApplication().createComponent("org.apache.shale.ValidatorScript");
        assertNotNull(script);
        script.setId(root.createUniqueId());
        script.setFunctionName("validateForm");
        
        root.getChildren().add(root.getChildCount(), script);

        return script;
    }
    
    
    
    private CommonsValidator createValidator(UIInput parent, String type) {
        //create a required field/server rule
        CommonsValidator validator = (CommonsValidator) facesContext.getApplication().createValidator("org.apache.shale.CommonsValidator");
        assertNotNull(validator);
        
        validator.setType(type);
        validator.setArg(parent.getId());
        validator.setServer(Boolean.TRUE);
        validator.setClient(Boolean.TRUE);
        parent.addValidator(validator);
        
        return validator;
    }
    
  
    private void checkMessages(UIInput component) {
        String id = component.getClientId(facesContext);
        Iterator mi = facesContext.getMessages(id);
        boolean hasMessage = false;
        while (mi.hasNext()) {
           hasMessage = true;
        }
        
        assertFalse(id + " has messages", hasMessage);   
       
    }
    
    
    private void checkMessage(String expected, UIInput component) {
        String id = component.getClientId(facesContext);
        Iterator mi = facesContext.getMessages(id);
        boolean hasMessage = false;
        while (mi.hasNext()) {
           FacesMessage message = (FacesMessage) mi.next();    
           assertEquals(expected, message.getDetail());
           hasMessage = true;
        }
        
        assertTrue(id + " has messages", hasMessage);   
    }
    
    
    
    //render and return the results
    private StringBuffer encode(UIComponent component) throws Exception {
        //builds a buffer to write the page to
        StringWriter writer = new StringWriter();
        //create a buffered response writer
        ResponseWriter buffResponsewriter = facesContext.getRenderKit()
                        .createResponseWriter(writer, null, response.getCharacterEncoding());
        //push buffered writer to the faces context
        facesContext.setResponseWriter(buffResponsewriter);
        //start a document
        buffResponsewriter.startDocument();
        
        //render HTML
        component.encodeBegin(facesContext);
        component.encodeChildren(facesContext);
        component.encodeEnd(facesContext);
        
        //end the document
        buffResponsewriter.endDocument();
        
        return writer.getBuffer();
    }
    

}
