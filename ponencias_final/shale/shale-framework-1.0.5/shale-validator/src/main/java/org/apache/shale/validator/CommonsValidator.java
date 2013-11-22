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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Var;
import org.apache.shale.util.ConverterHelper;
import org.apache.shale.util.Messages;
import org.apache.shale.util.Tags;
import org.apache.shale.validator.faces.ValidatorInputRenderer;

/**
 * <p>This is a JavaServer Faces <code>Validator</code> that uses
 * Apache Commons Validator to perform validation, either on the
 * client side, the server side, or both.</p>
 *
 *  <p>The current implementation is dependent on version 1.3 of Commons Validator.
 * Some new conventions have been adopted for registering a validation rule in
 * the validator's XML configuration file.  In the action framework, validation
 * was suited for declaring rules associated with a form.  This worked well since
 * generally a single action had the responsibility of handling all the posted
 * form data at once.</p>
 *
 * <p>However, JSF takes a component based approach.  Each component has the
 * responsibility of managing its posted data and rendering its state.   In the
 * component based world, it is easier to associate configuration values at
 * that level versus what works best for Struts actions.</p>
 *
 * <p>In an effort to reuse as much of Commons Validator and provide a method of
 * registering new rules, a new convention was adopted for declaring validation
 * rules.</p>
 *
 * <pre>
 *    &lt;global&gt;
 *      &lt;validator name="mask"
 *                classname="org.apache.commons.validator.GenericValidator"
 *                method="matchRegexp"
 *                methodParams="java.lang.String,java.lang.String"
 *                msg="errors.invalid"
 *                jsFunctionName="validateMask"
 *                jsFunction="org.apache.commons.validator.javascript.validateMask"
 *                depends=""/&gt;
 *    &lt;/global&gt;
 * </pre>
 *
 * <p>The rules declaration is the same but an added form is required to capture extra
 * configuration information.  The form is associated with the validation rule using a
 * naming convention.  The prefix of the form name is "org.apache.shale.validator.XXXX"
 * where "XXXX" is the validation rule name.</p>
 *
 * <pre>
 *   &lt;formset&gt;
 *     &lt;form name="org.apache.shale.validator.mask"&gt;
 * </pre>
 *
 *
 * <p>The form is followed by a field and the property attribute of the form has the
 * same value as the rule name.</p>
 *
 * <pre>
 *           &lt;field property="mask"&gt;
 * </pre>
 *
 * <p>Within the field definition, arg's are used to define the parameters in order
 * for message substitution and method argument value resolution.  There are two reserved
 * name values for the arg node used to define messages and parameters.</p>
 *
 * <pre>
 *               &lt;arg position="0" name="message" key="arg" resource="false"/&gt;
 *               &lt;arg position="1" name="message" key="mask" resource="false"/&gt;
 *               &lt;arg position="2" name="message" key="submittedValue" resource="false"/&gt;
 *
 *               &lt;arg position="0" name="parameter" key="submittedValue" resource="false"/&gt;
 *               &lt;arg position="1" name="parameter" key="mask" resource="false"/&gt;
 * </pre>
 *
 * <p>The "message" name arguments defines the possible <code>MessageFormat</code> parameter substitution
 * where the "position" corresponds to the substitution parameter.</p>
 *
 * <pre>
 *    errors.invalid={0} is invalid.
 * </pre>
 *
 * <p>The "parameter" arguments define the variable names that hold values for the target validatior method
 * identified by the validator rule name.  The comma delimited class types in the "methodParms" value list
 * correspond to the parameters by position.</p>
 *
 * <pre>
 *   methodParams="java.lang.String,java.lang.String"
 * </pre>
 *
 * <p>The var node is also used to explicitly define a JavaScript variable type.  If not
 * defined, the default is "string".  The var-value is ignored because its captured by
 * the shale commons validator instance.</p>
 *
 * <pre>
 *               &lt;var&gt;
 *                  &lt;var-name&gt;mask&lt;/var-name&gt;
 *                  &lt;var-value&gt;&lt;/var-value&gt;
 *                  &lt;var-jstype&gt;regexp&lt;/var-jstype&gt;
 *               &lt;/var&gt;
 * </pre>
 *
 * $Id: CommonsValidator.java 562606 2007-08-03 22:21:50Z rahul $
 */

public class CommonsValidator implements Validator, Serializable {


    // -------------------------------------------------------- Static Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -1783130706547542798L;

    /**
     * <p>Map of standard types: boolean, byte, char, etc.
     *    and their corresponding classes.</p>
     */
    private static Map standardTypes;


    /**
     * <p>Localized messages for this class.</p>
     */
    private static final Messages messages =
        new Messages("org.apache.shale.validator.resources.Bundle");


    /**
     * <p>Log instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog("org.apache.shale.validator");


    /**
     * <p>The name of the parent form used for javascript.</p>
     */
    private String formName = null;

    /**
     * <p>Returns the parent's form name.</p>
     *
     * @return form name the validator is contained in
     */
    public String getFormName() {
       return formName;
    }

    /**
     * <p>Sets the validator's owning form name.</p>
     *
     * @param formName The new form name
     */
    public void setFormName(String formName) {
       this.formName = formName;
    }


    // -------------------------------------------------------- Instance Variables

    /**
     * <p>Validator type.</p>
     */
    private String type;

    /**
     * <p>Enable client-side validation?</p>
     */
    private Boolean client;


    /**
     * <p>Enable server-side validation?</p>
     */
    private Boolean server;


    /**
     * <p>The <code>validate</code> method uses this
     *    as the text of an error message it stores on the
     *    FacesContext when validation fails.</p>
     */
    private String message;


    /**
     * <p>Parameters for the specific Commons Validator to be used.</p>
     */
    private Map vars = new HashMap();

    /**
     * <p>Returns a <code>Map</code> of variables that can be passed to a
     * commons validator method or used to create a parameterized error
     * message.  Several of the public properties are contained within
     * the <code>vars</code> collection.  These include: arg, min, max,
     * minlength, maxlength, mask, datePatternStrict.</p>
     *
     * @return A value paired collection of variables used to invoke a
     * validator method.
     */
    public Map getVars() {
       return vars;
    }


    // -------------------------------------------------------- Transient Variables



    // ----------------------------------------------------- Property Accessors


    /**
     * <p>The setter method for the <code>type</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>type</code> property.
     */
   public void setType(String newValue) {
       type = newValue;
   }


    /**
     * <p>The getter method for the <code>type</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     *  @return validation rule to apply
     */
   public String getType() {
       return type;
   }


    /**
     * <p>The setter method for the <code>client</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>client</code> property.
     */
   public void setClient(Boolean newValue) {
       client = newValue;
   }


    /**
     * <p>The getter method for the <code>client</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @return <code>true</code> if using JavaScript validation
     */
   public Boolean getClient() {
       return client;
   }


    /**
     * <p>The setter method for the <code>server</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>server</code> property.
     */
   public void setServer(Boolean newValue) {
       server = newValue;
   }


    /**
     * <p>The getter method for the <code>server</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @return <code>true</code> if using server side validation
     */
   public Boolean getServer() {
       return server;
   }


    /**
     * <p>The setter method for the <code>message</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>message</code> property.
     */
   public void setMessage(String newValue) {
       message = newValue;
   }


    /**
     * <p>The getter method for the <code>message</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @return validation message override
     */
   public String getMessage() {
       return message;
   }

   /**
    * <p>Name of the <code>arg</code> property in the <code>vars</code> Map.</p>
    */
   private static final String ARG_VARNAME = "arg";

    /**
     * <p>The setter method for the <code>arg</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>arg</code> property.
     */
   public void setArg(String newValue) {
       vars.put(ARG_VARNAME, newValue);
   }


   /**
    * <p>Name of the <code>min</code> property in the <code>vars</code> Map.</p>
    */
   public static final String MIN_VARNAME = "min";

    /**
     * <p>The setter method for the <code>min</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>min</code> property.
     */
   public void setMin(String newValue) {
       vars.put(MIN_VARNAME, newValue);
   }

   /**
    * <p>Name of the <code>max</code> property in the <code>vars</code> Map.</p>
    */
   public static final String MAX_VARNAME = "max";

    /**
     * <p>The setter method for the <code>max</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>max</code> property.
     */
   public void setMax(String newValue) {
       vars.put(MAX_VARNAME, newValue);
   }


   /**
    * <p>Name of the <code>minLength</code> property in the <code>vars</code> Map.</p>
    */
   public static final String MIN_LENGTH_VARNAME = "minlength";

   /**
     * <p>The setter method for the <code>minlength</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>minlength</code> property.
     */
   public void setMinLength(String newValue) { vars.put(MIN_LENGTH_VARNAME,  newValue); }


   /**
    * <p>Name of the <code>maxLength</code> property in the <code>vars</code> Map.</p>
    */
   public static final String MAX_LENGTH_VARNAME = "maxlength";

    /**
     * <p>The setter method for the <code>maxlength</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>maxlength</code> property.
     */
   public void setMaxLength(String newValue) {
       vars.put(MAX_LENGTH_VARNAME, newValue);
   }

   /**
    * <p>Name of the <code>mask</code> property in the <code>vars</code> Map.</p>
    */
   public static final String MASK_VARNAME = "mask";

    /**
     * <p>The setter method for the <code>mask</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>mask</code> property.
     */
   public void setMask(String newValue) {
       vars.put(MASK_VARNAME, newValue);
   }


   /**
    * <p>Name of the <code>datePatternStrict</code> in the <code>vars</code> Map.</p>
    */
   public static final String DATE_PATTERN_STRICT_VARNAME = "datePatternStrict";


    /**
     * <p>The setter method for the <code>datePatternStrict</code> property. This property is
     *    passed through to the Commons Validator.</p>
     *
     * @param newValue The new value for the <code>datePatternStrict</code> property.
     */
   public void setDatePatternStrict(String newValue) {
      vars.put(DATE_PATTERN_STRICT_VARNAME, newValue);
   }


    /**
     * <p>Return the validator resources that were configured and cached
     * at application startup time.</p>
     */
    private static ValidatorResources getValidatorResources() {

       FacesContext context = FacesContext.getCurrentInstance();
       ExternalContext external = context.getExternalContext();
       Map applicationMap = external.getApplicationMap();
       return (ValidatorResources) applicationMap.get(Globals.VALIDATOR_RESOURCES);

    }


    /**
     * <p>Name of the <code>message</code> property in the <code>vars</code> Map.</p>
     */
    private static final String MESSAGE_ARG_NAME = "message";

    /**
     * <p>Returns an array of values for message parameter replacement
     * arguments.  The list and ordering is determined by a form
     * registered in the common validators XML.  The form name
     * and the fields property is tied to the validation rule name
     * by convention.  The the <code>arg</code> name attribute is
     * assumed to be "message" for message argument grouping.</p>
     *
     * @param ruleName name of the validation rule
     * @param localVars snapshot of EL vars captured at rendering time
     *        and used by the script collector
     * @return array of objects used to fill the message
     */
    protected Object[] getMessageArgs(String ruleName, Map localVars) {

       Tags tagUtils = new Tags();
       Arg[] templateArgs = getArgs(MESSAGE_ARG_NAME, ruleName);
       assert templateArgs != null;

       Object[] target = new Object[templateArgs.length];

       for (int i = 0; i < templateArgs.length; i++) {
          Object value = vars.get(templateArgs[i].getKey());

          // look for a local var override
          if (localVars != null && localVars.containsKey(templateArgs[i].getKey())) {
             value = localVars.get(templateArgs[i].getKey());
          } else if (value != null && value instanceof String) {
              // if a String, check for a value binding expression
              value = tagUtils.eval((String) value);
          }
          target[i] = value;
       }

       return target;
    }

    /**
     * <p>Returns an array of class types corresponding to the the
     * target validation rules method signature.  The params are
     * configured by the <code>validator</code>'s <code>methodParams</code>
     * attribute.</p>
     *
     * @param validationAction the validators configuration bean populated from the XML file.
     * @return an array of class types for the formal parameter list.
     * @throws ClassNotFoundException validation rule class not found
     */
    protected Class[] loadMethodParamClasses(ValidatorAction validationAction) throws ClassNotFoundException {

        List tmp = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(validationAction.getMethodParams(), ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.length() > 0) {
                tmp.add(token);
            }
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        assert classLoader != null;


        Class[] parameterClasses = new Class[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            String className = (String) tmp.get(i);
            if (standardTypes.containsKey(className)) {
                parameterClasses[i] = (Class) standardTypes.get(className);
            } else {
                parameterClasses[i] = classLoader.loadClass(className);
            }
        }

        return parameterClasses;
    }


    /**
     * <p>The args name used to define the parameter values for
     * a validation rule.</p>
     */
    private static final String PARAMETER_ARG_NAME = "parameter";

    /**
     * <p>Returns an array of parameter names in the target validator's
     * method.  The parameter names are defined in the validators configuration
     * file under a form and field that correspond to the target rule.
     * The name attribute of the nested <code>arg</code> is assumed to be
     * "parameter".
     *
     * @param name the name of the target validation rule
     * @return array of formal parameter names
     */
    public String[] getMethodParamNames(String name) {
        Arg[] templateArgs = getArgs(PARAMETER_ARG_NAME, name);
        assert templateArgs != null;

        String[] target = new String[templateArgs.length];

        for (int i = 0; i < templateArgs.length; i++) {
           target[i] = templateArgs[i].getKey();
        }

        return target;
    }

    private static final String FORM_NAME_PREFIX = "org.apache.shale.validator.";

    /**
     * <p>Returns validator <code>Arg</code> beans from the configuration file.
     * The <code>form</code> and <code>field</code> nodes that contain the target
     * arguments have a naming convention to a validation rule.  This convention
     * was adopted for JSF validators because the <code>var</code>'s of the defining
     * validator is defined by the JSF validator object and not in the config file.
     * The JSF implementation doesn't have a concept of defining global form field
     * characteristics outside of the associated JSF component.  But, we needed
     * a place to explicitly declare parameter names and message arguments.<p>
     *
     * @param name the name of the <code>arg</code> name attribute.
     * @param ruleName the name of the validator rule
     * @return an array of validator <code>Arg</code> beans.
     */
    protected static Arg[] getArgs(String name, String ruleName) {

        StringBuffer formName = new StringBuffer(FORM_NAME_PREFIX);
        formName.append(ruleName);

        Form formDef = getValidatorResources().getForm(Locale.getDefault(), formName.toString());
        assert formDef != null;

        Field field = formDef.getField(ruleName);
        assert field != null;

        Arg[] templateArgs = field.getArgs(name);

        int max = -1;
        for (int i = templateArgs.length - 1; i > -1; i--) {
           if (templateArgs[i] != null) {
              max = i;
              break;
           }
        }
        if (max == -1) {
           return new Arg[0];
        } else if (max < templateArgs.length - 1) {
           Arg[] tmp = new Arg[max + 1];
           System.arraycopy(templateArgs, 0, tmp, 0, max + 1);
           templateArgs = tmp;
        }
        return templateArgs;
    }


    /**
     * <p>Returns the JavaScript type for a <code>var</code> collection
     * item.  The default is <code>Var.JSTYPE_STRING</code>.  The type
     * can be overridden by adding a <code>var</code> node to the
     * <code>field</code> node in the validator configuration file.
     * The <code>var-value</code> is ignored but a value is required
     * for well-formness.  The <code>var-jstype</code>
     * and <code>var-name</code> are the only values used.
     * The <code>form</code> and the <code>field</code> the <code>var</code>
     * is nested under has an association with the <code>type</code>.
     * </p>
     * @param varName The name of the target variable
     * @return The JavaScript variable type ("string", "int", "regexp")
     */
    public String getVarType(String varName) {
        StringBuffer formName = new StringBuffer(FORM_NAME_PREFIX);
        formName.append(getType());

        Form formDef = getValidatorResources().getForm(Locale.getDefault(), formName.toString());
        assert formDef != null;

        Field field = formDef.getField(getType());
        assert field != null;

        String jsType = Var.JSTYPE_STRING; // default type
        Var var = field.getVar(varName);

        if (var != null && var.getJsType() != null) {
           jsType = var.getJsType();
        }

        return jsType;
    }


    /**
     * <p>Assumed argument name that captures the javascript
     * validation rule callback.</p>
     */
    private static final String JSCALLBACK_ARG_NAME = "jscallback";


    /**
     * <p>Returns a mnemonic used to build the commons validator
     * javascript call back.  This method is invoked from the
     * {@link org.apache.shale.component.ValidatorScript} component.
     * The routine looks for an <code>arg</code> with a name of
     * <code>jscallback</code> under a form name and field
     * property corresponding to the rule.  If there is not
     * a matching arg found, the <code>ruleName</code> is
     * returned as the default.
     * </p>
     * @param ruleName name of the target rule to invoke
     * @return code used to create the javacript call back function
     */
    public static String getJsCallbackMnemonic(String ruleName) {
        Arg[] args = getArgs(JSCALLBACK_ARG_NAME, ruleName);
        if (args == null || args.length == 0) {
           return ruleName;
        } else {
           return args[0].getKey();
        }
    }

    /**
     * <p>Loads an array of method parameter values corresponding to the
     * formal parameter of the target validator's method.<p>
     * @param context faces context
     * @param validatorAction <code>ValidatorAction</code> configuration bean.
     * @param methodParamClasses <code>Class[]</code> of the parameters of the target method.
     * @param localVars snapshot of EL variables at rendering time; used by client side
     * @return An array of object valuse for each method parameter.
     */
    protected Object[] loadMethodParamValues(FacesContext context, ValidatorAction validatorAction, Class[] methodParamClasses, Map localVars) {
        Tags tagUtils = new Tags();

        String[] paramNames = getMethodParamNames(validatorAction.getName());
        assert paramNames != null;

        Object[] target = new Object[paramNames.length];
        assert paramNames.length == methodParamClasses.length;

        for (int i = 0; i < paramNames.length; i++) {
            Object obj = null;
            if (localVars != null && localVars.containsKey(paramNames[i])) {
                obj = localVars.get(paramNames[i]);   
            }
            if (obj == null) {
                obj = vars.get(paramNames[i]);
            }
            if (obj != null && obj instanceof String) {
                obj = tagUtils.eval((String) obj);
            }
            target[i] = convert(context, obj, methodParamClasses[i]);
        }

        return target;
     }


    /**
     * <p>Returns the Commons validator action that's appropriate
     *    for the validator with the given <code>name</code>.</p>
     *
     * @param name The name of the validator
     * @return Validator rules config bean
     */
   public static ValidatorAction getValidatorAction(String name) {
      return getValidatorResources().getValidatorAction(name);
   }
   /**
    * <p>Returns the commons validator action associated with
    * the <code>type</code> attribute.</p>
    *
    * @return Validator rules config bean
    */
   public ValidatorAction getValidatorAction() {
       return getValidatorResources().getValidatorAction(getType());
   }

   /**
    * <p>For a given commons validator rule, returns an array of
    * rule names that are dependent of the <code>name</code>.
    * Rule dependencies will be first in the returned array.
    * </p>
    * @param name target validator rule
    * @return array of all dependent rules for the target name
    */
   protected String[] getDependencies(String name) {

       ValidatorAction action = getValidatorAction(name);
       assert action != null;

       List dependencies = action.getDependencyList();
       String[] types = new String[dependencies.size() + 1];

       for (int i = 0; i < dependencies.size(); i++) {
           types[i] = (String) dependencies.get(i);
       }
       types[types.length - 1] = name;

       return types;
   }

   /**
    * <p>Name used to store the component's submitted value in the
    * <code>vars</code> Map.</p>
    */
   private static final String SUBMITTED_VALUE_VARNAME = "submittedValue";

    /**
     * <p>This <code>validate</code> method is called by JSF to verify
     *    the component to which the validator is attached.</p>
     *
     * @param context The faces context
     * @param component The component to validate
     * @param value the component's submitted value after the converter applied.
     */
   public void validate(FacesContext context, UIComponent component, Object value) {

       if (Boolean.FALSE.equals(getServer())) {
           return;
       }
       String[] types = getDependencies(getType());
       for (int j = 0; j < types.length; j++) {
           ValidatorAction validatorAction = CommonsValidator.getValidatorAction(types[j]);

           try {
               if (component instanceof EditableValueHolder) {
                  vars.put(SUBMITTED_VALUE_VARNAME, ((EditableValueHolder) component).getSubmittedValue());    
               } else {
                  vars.put(SUBMITTED_VALUE_VARNAME, value);
               }

               Map localVars = null;
               // A map that captures information about a component that might contain state for commons
               // validators properties.  The map is organized by a hierarchy "clientId/validatorType/vars".
               // It is captured at render time.
               Map ids = (Map) component.getAttributes().get(ValidatorInputRenderer.VALIDATOR_CLIENTIDS_ATTR);
               if (ids != null) {
                   String clientId = component.getClientId(context);
                   Map validatorVars = (Map) ids.get(clientId);
                   if (validatorVars != null) {
                      localVars = (Map) validatorVars.get(getType()); 
                   }
               }

               Class validatorClass = loadValidatorClass(validatorAction);
               Class[] paramClasses = this.loadMethodParamClasses(validatorAction);
               Object[] paramValues = this.loadMethodParamValues(context, validatorAction, paramClasses, localVars);
               Method validatorMethod = this.loadValidatorMethod(validatorAction, validatorClass,  paramClasses);
               Object validator = null;
               if (!Modifier.isStatic(validatorMethod.getModifiers())) {
                   validator = validatorClass.newInstance();
               }
               Boolean r = (Boolean) validatorMethod.invoke(validator, paramValues);
               
               if (r.equals(Boolean.FALSE)) {
                   throw new ValidatorException(new FacesMessage(
                           FacesMessage.SEVERITY_ERROR,
                           getErrorMessage(context, validatorAction, localVars), null));
               }
           } catch (IllegalArgumentException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } catch (ClassNotFoundException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } catch (InstantiationException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } catch (IllegalAccessException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } catch (NoSuchMethodException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } catch (InvocationTargetException e) {
               throw new RuntimeException(messages.getMessage("commonsValidator.intException",
                       new Object[] {getType(), component.getId()}), e);
           } finally {
               vars.remove(SUBMITTED_VALUE_VARNAME);
           }

       }
   }


   /**
    * <p>Loads the commons validator class containing the target rule.</p>
    * @param validatorAction the validator rules config bean
    * @return the class having the target validation method
    *
    * @throws ClassNotFoundException if the specified class cannot be found
    * @throws InstantiationException if a new instance cannot be instantiated
    * @throws IllegalAccessException if there is no public constructor
    */
   protected Class loadValidatorClass(ValidatorAction validatorAction)
     throws ClassNotFoundException, InstantiationException, IllegalAccessException {

       ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
       if (classLoader == null) {
           classLoader = this.getClass().getClassLoader();
       }
       assert classLoader != null;

       //find the target class having the validator rule
       Class validatorClass = classLoader.loadClass(validatorAction.getClassname());

       return validatorClass;
   }

   /**
    * <p>Loads the <code>Method</code> of the <code>validatorClass</code> having
    * using definitions from the <code>validatorAction</code> bean.
    * </p>
    * @param validatorAction The config info bean of the target rule.
    * @param validatorClass The class having the validation method.
    * @param methodParamClasses The method formal parameter class signature.
    * @return target commons validator method to invoke.
    * @throws NoSuchMethodException if the specified method cannot be found
    */
   protected Method loadValidatorMethod(ValidatorAction validatorAction,
           Class validatorClass,
           Class[] methodParamClasses) throws NoSuchMethodException {
       //find the method on the target validator rule class
       Method validatorMethod = validatorClass.getMethod(validatorAction.getMethod(), methodParamClasses);
       return validatorMethod;
   }


    /**
     * <p>Retrieves an error message, using the validator's message combined
     *    with the errant value.</p>
     * @param context the all knowing faces conext object.
     * @param validatorAction config bean defining the commons validator rule.
     * @param localVars snapshot of EL variables at rendering time; used by client side
     * @return the message after parameter substitution.
     */
   public String getErrorMessage(FacesContext context, ValidatorAction validatorAction, Map localVars) {
      final String DEFAULT_BUNDLE_NAME = "org.apache.shale.validator.messages";

      Locale locale = context.getViewRoot().getLocale();
      String msg = getMessage();
      if (msg == null) {
         String msgkey = validatorAction.getMsg();
         ClassLoader loader = Thread.currentThread().getContextClassLoader();
         if (loader == null) { loader = getClass().getClassLoader(); }
         Application app = context.getApplication();
         String appBundleName = app.getMessageBundle();
         if (appBundleName != null) {
            ResourceBundle bundle
               = ResourceBundle.getBundle(appBundleName, locale, loader);
            if (bundle != null) {
               try {
                  msg = bundle.getString(msgkey);
               } catch (MissingResourceException ex) {
                   ; // Ignore this
               }
            }
         }
         if (msg == null) {
            ResourceBundle bundle
               = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME, locale, loader);
            if (bundle != null) {
               try {
                  msg = bundle.getString(msgkey);
               } catch (MissingResourceException ex) {
                   log.error(messages.getMessage("commonsValidator.msgerror",
                       new Object[] {msgkey}), ex);
                   throw ex;
               }
            }
         }
      }
      Object[] params = getMessageArgs(validatorAction.getName(), localVars);
      msg = new MessageFormat(msg, locale).format(params);
      return msg;
   }


    // ----------------------------------------------------- Static Initialization

    /**
     * <p>A utility method that converts an object to an instance
     *    of a given class, such as converting <code>"true"</code>
     *    for example, into <code>Boolean.TRUE</code>.</p>
     * <p>If the component passed to this method is an instance of
     *    <code>EditableValueHolder</code> and the object's class is
     *    <code>String</code>, this method returns the component's
     *     submitted value, without converting it to a string. The
     *     <code>component</code> parameter can be <code>null</code>.
     *  </p>
     *
     * @param context faces context
     * @param obj The object to convert
     * @param cl The type of object to convert to
     * @return target object type
     */
   private static Object convert(FacesContext context, Object obj, Class cl) {

      ConverterHelper converterHelper = new ConverterHelper();
      // correct target type
      if (cl.isInstance(obj)) {
          return obj;
      }
      // target type is String
      if (cl == String.class) {
          if (obj instanceof String) {
             return obj;
          } else {
             return converterHelper.asString(context, obj.getClass(), obj);
          }
      }
      
      if (obj instanceof String) {   
          // String to object
          return converterHelper.asObject(context, cl, (String) obj);  
      } else {
          //Object to String
          String source = converterHelper.asString(context, obj.getClass(), obj);
          // String to Object
          return converterHelper.asObject(context, cl, source);
      }

   }


    /**
     * <p>A utility method that returns <code>true</code> if
     *    the supplied string has a length greater than zero.
     * </p>
     *
     * @param str The string
     * @return <code>true</code> if not an empty String
     */
   // these two methods are referenced in validator-utils.xml
   public static boolean isSupplied(String str) {
      return str.trim().length() > 0;
   }


    /**
     * <p>A utility method that returns <code>true</code> if
     *    the supplied string represents a date.</p>
     *
     * @param d The string representation of the date.
     * @param datePatternStrict Commons validator property
     * @return <code>true</code> if <code>d</code> is a date
     */
   public static boolean isDate(String d, String datePatternStrict) {
      return GenericValidator.isDate(d, datePatternStrict, true);
   }


    // ----------------------------------------------------- Static Initialization


    /**
     * <p>Standard types for conversions</p>
     */
   static {
      standardTypes = new HashMap();
      standardTypes.put("boolean", boolean.class);
      standardTypes.put("byte", byte.class);
      standardTypes.put("char", char.class);
      standardTypes.put("double", double.class);
      standardTypes.put("float", float.class);
      standardTypes.put("int", int.class);
      standardTypes.put("long", long.class);
      standardTypes.put("short", short.class);
      standardTypes.put("java.lang.String", String.class);
   }

}
