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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Var;
import org.apache.shale.util.Tags;
import org.apache.shale.validator.CommonsValidator;

/**
 * <p>A JSF component that encodes JavaScript for
 *    all client-side validations specified in the same
 *    JSP page (with <code>s:commonsValidator</code>.
 *
 * $Id: ValidatorScript.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class ValidatorScript extends UIComponentBase {


    // -------------------------------------------------------- Private Variables


    /**
     * <p>The value of the <code>functionName</code> property.</p>
     */
    private String functionName;


    /**
     * <p>A map of validators, representing all of the Commons Validators
     *    attached to components in the current component hierarchy.
     *    The keys of the map are validator type names. The values are
     *    maps from IDs to CommonsValidator objects.</p>
     */
    private Map validators = new LinkedHashMap();


    /**
     * <p>Holds vars loaded with EL that are evaluated at render time.
     * The key into the map is the component's client id.</p>
     */
    private Map validatorVars = new LinkedHashMap();


    /**
     * <p>The component renders itself; therefore, this
     *    method returns null.</p>
     *
     * @return <code>null</code> component handles renderering
     */
    public String getRendererType() {
        return null;
    }


    /**
     * <p>Returns the component's family. In this case,
     *    the component is not associated with a family,
     *    so this method returns null.</p>
     *
     * @return component's family
     */
    public String getFamily() {
        return null;
    }


    /**
     * <p>Return the value of the <code>functionName</code> property.</p>
     *
     * @return callback function from the form's onsubmit.
     */
    public String getFunctionName() {

        if (this.functionName != null) {
            return functionName;
        }
        ValueBinding _vb = getValueBinding("functionName");
        if (_vb != null) {
            return (String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }

    }


    /**
     * <p>Set the value of the <code>functionName</code> property.</p>
     *
     * @param functionName The new function name
     */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }


    /**
     * <p>Restore the state of this component.</p>
     *
     * @param context FacesContext for the current request
     * @param state State to be restored
     */
    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.functionName = (String) values[1];

    }


    /**
     * <p>Save the state of this component.</p>
     *
     * @param context FacesContext for the current request
     * @return component's state
     */
    public Object saveState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = this.functionName;
        return values;

    }


    /**
     * <p>Restructures the validator hierarchy grouping by
     * form name, type and id.</p>
     *
     * @return validators grouped by form, type and clientId
     */
    private Map getValidatorsGroupByFormName() {

        Map formValidators = new LinkedHashMap();

        Iterator vi = validators.entrySet().iterator();
        while (vi.hasNext()) {

            Map.Entry typeEntry = (Map.Entry) vi.next();
            Map typeMap = (Map) typeEntry.getValue();

            String type = (String) typeEntry.getKey();

            Iterator ti = typeMap.entrySet().iterator();
            while (ti.hasNext()) {

                Map.Entry idEntry = (Map.Entry) ti.next();
                String id = (String) idEntry.getKey();
                CommonsValidator v = (CommonsValidator) idEntry.getValue();
                String formName = v.getFormName();

                Map formTypeMap = (Map) formValidators.get(formName);
                if (formTypeMap == null) {
                    formTypeMap = new LinkedHashMap();
                    formValidators.put(formName, formTypeMap);
                }

                Map formTypeIdMap = (Map) formTypeMap.get(type);
                if (formTypeIdMap == null) {
                    formTypeIdMap = new LinkedHashMap();
                    formTypeMap.put(type, formTypeIdMap);
                }

                formTypeIdMap.put(id, v);
            }

        }

        return formValidators;

    }


    /**
     * <p>Registers a validator according to type and id.</p>
     *
     * @param type The type of the validator
     * @param id The validator's identifier
     * @param v The Commons validator associated with the id and type
     */
    private void addValidator(String type, String id, CommonsValidator v) {
        // look for validators organized by type
        Map map = (Map) validators.get(type);
        if (map == null) {
            map = new LinkedHashMap();
            validators.put(type, map);
        }
        if (id != null) {
             map.put(id, v);
        }
   }


    /**
     * <p>Recursively finds all Commons validators for the all of the
     *    components in a component hierarchy and adds them to a map.</p>
     * <p>If a validator's type is required, this method sets the
     *    associated component's required property to true. This is
     *    necessary because JSF does not validate empty fields unless
     *    a component's required property is true.</p>
     *
     * @param c The component at the root of the component tree
     * @param context The FacesContext for this request
     */
   private void findCommonsValidators(UIComponent c, FacesContext context) {
      if (c instanceof EditableValueHolder && c.isRendered()) {
         EditableValueHolder h = (EditableValueHolder) c;
         javax.faces.validator.Validator[] vs = h.getValidators();
         for (int i = 0; i < vs.length; i++) {
            if (vs[i] instanceof CommonsValidator) {
               CommonsValidator v = (CommonsValidator) vs[i];
               v.setFormName(findForm(context, c));
               if (Boolean.TRUE.equals(v.getClient())) {

                   //look for the clientId set
                   Map clientIds = (Map) c.getAttributes().get(ValidatorInputRenderer.VALIDATOR_CLIENTIDS_ATTR);
                   if (clientIds != null) {

                      validatorVars.putAll(clientIds);
                      Iterator ci = clientIds.entrySet().iterator();
                      while (ci.hasNext()) {
                         Map.Entry e = (Map.Entry) ci.next();

                         String id = (String) e.getKey();
                         addValidator(v.getType(), id, v);

                         ValidatorAction action = v.getValidatorAction();
                         List list = action.getDependencyList();
                         Iterator iter = list.iterator();
                         while (iter.hasNext()) {
                             String type = (String) iter.next();
                             addValidator(type, id, v);
                         }

                      }

                   } else {
                       //otherwise just try using the client id
                       String id = c.getClientId(context);
                       addValidator(v.getType(), id, v);

                       ValidatorAction action = v.getValidatorAction();
                       List list = action.getDependencyList();
                       Iterator iter = list.iterator();
                       while (iter.hasNext()) {
                           String type = (String) iter.next();
                           addValidator(type, id, v);
                       }
                   }

               }
               if (Boolean.TRUE.equals(v.getServer())) {
                  // Fields with empty values are not validated, so
                  // we force the issue here by setting the component's
                  // required attribute to true.

                  if ("required".equals(v.getType())) {
                     h.setRequired(true);
                  }
               }
            }
         }
      }

      Iterator childrenIterator = c.getFacetsAndChildren();
      while (childrenIterator.hasNext()) {
         UIComponent child = (UIComponent) childrenIterator.next();
         findCommonsValidators(child, context);
      }
      childrenIterator = null;
   }


    /**
     * <p>Write the start of the script for client-side validation.</p>
     *
     * @param writer A response writer
     *
     * @exception IOException if an input/output error occurs
     */
   private void writeScriptStart(ResponseWriter writer) throws IOException {
      writer.startElement("script", this);
      writer.writeAttribute("type", "text/javascript", null);
      writer.writeAttribute("language", "Javascript1.1", null);
      writer.write("\n");
    }


    /**
     * <p>Write the end of the script for client-side validation.</p>
     *
     * @param writer A response writer
     *
     * @exception IOException if an input/output error occurs
     */
   private void writeScriptEnd(ResponseWriter writer) throws IOException {
      writer.write("\n");
      writer.endElement("script");
   }


    /**
     * <p>Returns the name of the JavaScript function, specified in
     *    the JSP page (presumably), that validates this JSP page's form.</p>
     *
     * @param writer A response writer
     * @param context The FacesContext for this request
     *
     * @exception IOException if an input/output error occurs
     */
   private void writeValidationFunctions(ResponseWriter writer,
      FacesContext context) throws IOException {

      StringBuffer buff = new StringBuffer();
      buff.append("var bCancel = false;\n")
          .append("function ")
          .append(getAttributes().get("functionName").toString()).append("(form) {\n")
          .append("\tvar bValid = true;\n")
          .append("\tvar sFormName = jcv_retrieveFormName(form);\n");

      Map formValidators = getValidatorsGroupByFormName();
      Iterator formIter = formValidators.entrySet().iterator();
      while (formIter.hasNext()) {
          Map.Entry typeEntry = (Map.Entry) formIter.next();
          String formName = (String) typeEntry.getKey();
          Map formTypeValidators = (Map) typeEntry.getValue();

          // for each validator type, write callback

          buff.append("\tif ((bValid && !bCancel && (\"")
              .append(formName)
              .append("\" == sFormName))) {\n")
              .append("\t\tbValid = (");


          Iterator iter = getTypesOrderedByDependencies(formTypeValidators.keySet()).iterator();
          boolean first = true;
          while (iter.hasNext()) {
              String type = (String) iter.next();
              ValidatorAction a = CommonsValidator.getValidatorAction(type);

              buff.append((!first ? " && " : ""))
                  .append(a.getJsFunctionName())
                  .append("(form)");
              first = false;

              writer.write("function ");
              StringBuffer callback = new StringBuffer();

              // most of the type the callback function is based on the form name and
              // type but for some rules require special names
              String fnameMnemonic = CommonsValidator.getJsCallbackMnemonic(type);

              callback.append(formName).append('_').append(fnameMnemonic);
              writer.write(callback.toString());
              writer.write("() { \n");
              // for each field validated by this type, add configuration object
              Map map = (Map) formTypeValidators.get(type);
              Iterator iter2 = map.keySet().iterator();
              int k = 0;
              while (iter2.hasNext()) {
                  String id = (String) iter2.next();
                  CommonsValidator v = (CommonsValidator) map.get(id);
                  writer.write("this[" + k + "] = ");
                  k++;
                  writeJavaScriptParams(writer, context, id, v);
                  writer.write(";\n");
              }
              writer.write("\t}\n");
          }

          buff.append(");\n\t\n}");

      }
      formValidators.clear();

      // write out the form function
      buff.append("\n\treturn bValid;\n")
          .append("}\n");

      writer.write(buff.toString());

      // resolve dependencies for a complete types list

      List types = new ArrayList(validators.keySet());
      types.add("includeJavaScriptUtilities");

      Iterator iter = types.iterator();
      while (iter.hasNext()) {
         String type = (String) iter.next();
         ValidatorAction a = CommonsValidator.getValidatorAction(type);
         writer.write(a.getJavascript());
         writer.write("\n");
      }

      types.clear();

   }


   /**
    * <p>Backslash-escapes the following characters from the input string:
    * &quot;, &apos;, \, \r, \n.</p>
    *
    * <p>This method escapes characters that will result in an invalid
    * Javascript statement within the validator Javascript.</p>
    *
    * @param str The string to escape.
    * @return The string <code>s</code> with each instance of a double quote,
    *         single quote, backslash, carriage-return, or line feed escaped
    *         with a leading backslash.
    */
   private String escapeJavascript(String str) {
       if (str == null) {
           return null;
       }

       int length = str.length();

       if (length == 0) {
           return str;
       }

       // guess at how many chars we'll be adding...
       StringBuffer out = new StringBuffer(length + 4);

       // run through the string escaping sensitive chars
       for (int i = 0; i < length; i++) {
           char c = str.charAt(i);

           if ((c == '"') || (c == '\'') || (c == '\\') || (c == '\n')
                   || (c == '\r')) {
               out.append('\\');
           }

           out.append(c);
       }

       return out.toString();
   }



   /**
    * <p>Returns an array of validator types organized by dependencies.</p>
    *
    * @param typeSet The type set to be processed
    * @return array of validator types ordered by dependencies
    */
   private List getTypesOrderedByDependencies(Set typeSet) {

       List tmpList = new ArrayList(typeSet);

       ordered: for (int i = 0; i < tmpList.size(); i++) {
           boolean swap = false;
           for (int j = 0; j < tmpList.size(); j++) {
               String type = (String) tmpList.get(j);
               ValidatorAction a = CommonsValidator.getValidatorAction(type);

               List dependencies  = a.getDependencyList();
               if (dependencies != null && dependencies.size() > 0) {
                   int max = -1;
                   for (int n = 0; n < dependencies.size(); n++) {
                       max = Math.max(max, tmpList.indexOf(dependencies.get(n)));
                   }
                   if (max > j) {
                       String tmp = (String) tmpList.get(j);
                       tmpList.remove(j);
                       tmpList.add(max, tmp);
                       swap = true;
                       j = max;
                   }
               }

           }
           if (!swap) {
             break ordered;
           }
       }

       return tmpList;

   }

    /**
     * <p>Writes the JavaScript parameters for the client-side
     *    validation code.</p>
     *
     * @param writer A response writer
     * @param context The FacesContext for this request
     * @param id The clientId of the owning component
     * @param v The Commons validator
     *
     * @exception IOException if an input/output error occurs
     */
   public void writeJavaScriptParams(ResponseWriter writer,
      FacesContext context, String id, CommonsValidator v) throws IOException {

      Map localVars = null;
      // look for var's evaluated at render time.  This is for client
      // side JavaScript validation
      if (validatorVars != null && validatorVars.containsKey(id)) {
          Map typeVars = (Map) validatorVars.get(id);
          if (typeVars != null && typeVars.containsKey(v.getType())) {
              localVars = (Map) typeVars.get(v.getType());
          }
      }

      Tags tagUtils = new Tags();
      ValidatorAction validatorAction = v.getValidatorAction();
      writer.write("new Array(\"");
      writer.write(id);
      writer.write("\", \"");
      writer.write(v.getErrorMessage(context, validatorAction, localVars));
      writer.write("\", new Function(\"x\", \"return {");

      Iterator vi = v.getVars().entrySet().iterator();

      boolean first = true;

      // vars captured at render time and are the result of
      // EL.
      Map idVars = (Map) validatorVars.get(id);

      next: while (vi.hasNext()) {
         Map.Entry e = (Map.Entry) vi.next();

         Object value = e.getValue();

         // look for a render override by clientId/type
         if (idVars != null && idVars.containsKey(v.getType())) {

             Map typeVars = (Map) idVars.get(v.getType());
             // look for a render override by clientId/type/name
             if (typeVars != null && typeVars.containsKey(e.getKey())) {
                value = typeVars.get(e.getKey());
             }
         } else {
            if (value != null && value instanceof String
                && isValueReference((String) e.getValue())) {

                value = tagUtils.eval((String) e.getValue());
            }
         }

         if (value == null) {
            continue next;
         }
         String name = (String) e.getKey();
         if (!first) {
             writer.write(",");
         } else {
             first = false;
         }
         writer.write(name);
         writer.write(":");

         String jsType = v.getVarType(name);
         // Ugh...mask validator doesn't construct RegExp
         if (jsType.equals(Var.JSTYPE_REGEXP)) {
             writer.write("/");
         } else {
             writer.write("'");
         }

         writer.write(escapeJavascript(value.toString()));

         if (jsType.equals(Var.JSTYPE_REGEXP)) {
             writer.write("/");
         } else {
             writer.write("'");
         }
      }
      writer.write("}[x];\"))");
   }


   /**
    * <p>Traverses up the tree looking for the owning form.
    * Returns the parent <code>UIForm</code> or empty string
    * if one is not found.</p>
    *
    * @param context FacesContext for the current request
    * @param component <code>UIForm</code> parent of the component.
    * @return client id of the parent form component
    */
   public String findForm(FacesContext context, UIComponent component) {

       UIComponent parent = component.getParent();
       if (parent != null) {
          if (parent instanceof UIForm) {
             return parent.getClientId(context).replace(UINamingContainer.SEPARATOR_CHAR, '_');
          } else {
             return findForm(context, parent);
          }
       }

       return "";
   }


    /**
     * <p>Begin encoding for this component. This method
     *    finds all Commons validators attached to components
     *    in the current component hierarchy and writes out
     *    JavaScript code to invoke those validators, in turn.</p>
     *
     * @param context The FacesContext for this request
     *
     * @exception IOException if an input/output error occurs
     */
   public void encodeBegin(FacesContext context) throws IOException {
      ResponseWriter writer = context.getResponseWriter();

      validators.clear();
      findCommonsValidators(context.getViewRoot(), context);

      writeScriptStart(writer);
      writeValidationFunctions(writer, context);
      writeScriptEnd(writer);
   }


    /**
     * <p>Return true if the specified string contains an EL expression.</p>
     * 
     * <p>This is taken almost verbatim from {@link javax.faces.webapp.UIComponentTag}
     * in order to remove JSP dependencies from the renderers.</p>
     *
     * @param value String to be checked for being an expression
     */
    private boolean isValueReference(String value) {

        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");
        if (start < 0) {
            return false;
        }

        int end = value.lastIndexOf('}');
        return (end >= 0) && (start < end);
    }


}
