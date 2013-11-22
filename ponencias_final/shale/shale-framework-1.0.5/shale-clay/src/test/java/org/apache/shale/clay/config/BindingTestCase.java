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

package org.apache.shale.clay.config;

import java.io.StringWriter;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.IntegerConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;

public class BindingTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public BindingTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(BindingTestCase.class));
    }

    private HtmlCommandButton command = null;
    private HtmlInputText input = null;
    private Converter converter = null;
    private Validator validator = null;
    private ActionListener actionListener = null;
    private ValueChangeListener valueChangeListener = null;
    
    
    
    private Clay clay = null;
    protected void setUp() throws Exception {
        super.setUp();

        // done by the startup context listener
        loadConfigFiles(null, null);

        clay = (Clay) application
                .createComponent("org.apache.shale.clay.component.Clay");
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/binding.html");
        clay.setManagedBeanName("test");
        
        facesContext.getExternalContext().getSessionMap().put("test", this);

        // builds a buffer to write the page to
        StringWriter writer = new StringWriter();
        // create a buffered response writer
        ResponseWriter buffResponsewriter = facesContext.getRenderKit()
                .createResponseWriter(writer, null,
                        response.getCharacterEncoding());
        // push buffered writer to the faces context
        facesContext.setResponseWriter(buffResponsewriter);
        // start a document
        buffResponsewriter.startDocument();

        // build subtree
        clay.encodeBegin(facesContext);
    }

    public void testInputBindings() {
        HtmlInputText findInput = (HtmlInputText) findComponent(clay, "input");
        assertNotNull(findInput);
        assertTrue(getInput() == findInput);
        
        assertEquals(1, findInput.getValidators().length);
        assertTrue(getValidator() == findInput.getValidators()[0]);

        // test the property template override 
        assertEquals(100, ((LengthValidator) getValidator()).getMaximum());
        
        assertNotNull(findInput.getConverter());
        assertTrue(getConverter() == findInput.getConverter());
        
        assertEquals(1, findInput.getValueChangeListeners().length);
        assertTrue(getValueChangeListener() == findInput.getValueChangeListeners()[0]);      
    }
    
    public void testCommandBindings() {
        HtmlCommandButton findCommand = (HtmlCommandButton) findComponent(clay, "command");
        assertNotNull(findCommand);
        
        assertEquals(1, findCommand.getActionListeners().length);
        assertTrue(getActionListener() == findCommand.getActionListeners()[0]);
    }
    
    public ActionListener getActionListener() {
        if (actionListener == null) {
            actionListener = new ActionListener() {
               public void processAction(ActionEvent event) {
                   
               };  
            };
        }
        return actionListener;
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public HtmlCommandButton getCommand() {
        if (command == null) {
            command = new HtmlCommandButton();
        }
        return command;
    }

    public void setCommand(HtmlCommandButton command) {
        this.command = command;
    }

    public Converter getConverter() {
        if (converter == null) {
            converter = new IntegerConverter();    
        }
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public HtmlInputText getInput() {
        if (input == null) {
            input = new HtmlInputText();
        }
        return input;
    }

    public void setInput(HtmlInputText input) {
        this.input = input;
    }

    public Validator getValidator() {
        if (validator == null) {
            validator = new LengthValidator(5, 1);    
        }
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public ValueChangeListener getValueChangeListener() {
        if (valueChangeListener == null) {
            valueChangeListener = new ValueChangeListener() {
                public void processValueChange(ValueChangeEvent event) {
                    
                };
            };
        }
        return valueChangeListener;
    }

    public void setValueChangeListener(ValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    
    private UIComponent findComponent(UIComponent parent, String id) {
        if (parent == null) {
            return null;
        }

        if (parent.getId() != null && parent.getId().equals(id)) {
            return parent;
        } else {
            Iterator ci = parent.getChildren().iterator();
            while (ci.hasNext()) {
                UIComponent child = (UIComponent) ci.next();
                UIComponent target = findComponent(child, id);
                if (target != null) {
                    return target;
                }
            }
        }

        return null;
    }


}
