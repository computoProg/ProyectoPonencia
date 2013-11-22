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
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;

public class ImplicitMappingTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public ImplicitMappingTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ImplicitMappingTestCase.class));
    }

    private Clay clay = null;

    protected void setUp() throws Exception {
        super.setUp();

        // done by the startup context listener
        loadConfigFiles(null, null);

        clay = (Clay) application
                .createComponent("org.apache.shale.clay.component.Clay");
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/implicit.html");
        clay.setManagedBeanName("test");

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

    public void testForm() throws Exception {

        // form
        UIComponent widget = findComponent(clay, "logonForm");
        assertNotNull("UIForm", widget);
        assertTrue("UIForm", widget instanceof UIForm);
    }

    public void testLabel() {
        // label
        UIComponent widget = findComponent(clay, "usernameLabel");
        assertNotNull("HtmlOutputLabel", widget);
        assertTrue("HtmlOutputLabel", widget instanceof HtmlOutputLabel);
        assertEquals("for", "username", widget.getAttributes().get("for"));

    }

    public void testInputText() {
        // input text
        UIComponent widget = findComponent(clay, "username");
        assertNotNull("HtmlInputText", widget);
        assertTrue("HtmlInputText", widget instanceof HtmlInputText);
        assertEquals("size", "16", widget.getAttributes().get("size")
                .toString());

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.username}", exp);
    }

    public void testSubmit() {
        // submit
        UIComponent widget = findComponent(clay, "submit");
        assertNotNull("HtmlCommandButton", widget);
        assertTrue("HtmlCommandButton", widget instanceof HtmlCommandButton);
        assertEquals("value", "submit", widget.getAttributes().get("value")
                .toString());

        MethodBinding mb = ((HtmlCommandButton) widget).getAction();
        assertNotNull("MethodBinding", mb);
        String exp = mb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("action", "#{test.save}", exp);

    }

    public void testAnchor() {
        UIComponent widget = findComponent(clay, "shaleSite");
        assertNotNull("HtmlOutputLink", widget);
        assertTrue("HtmlOutputLink", widget instanceof HtmlOutputLink);
        assertEquals("value", "http://shale.apache.org/", widget
                .getAttributes().get("value"));
        assertEquals("target", "_blank", widget.getAttributes().get("target"));

    }

    public void testAnchorOverride() {
        UIComponent widget = findComponent(clay, "shaleSiteOverride");
        assertNotNull("HtmlCommandLink", widget);
        assertTrue("HtmlCommandLink", widget instanceof HtmlCommandLink);

        MethodBinding mb = ((HtmlCommandLink) widget).getAction();
        assertNotNull("MethodBinding", mb);
        String exp = mb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("action", "#{test.save}", exp);

        assertNull("value", widget.getAttributes().get("value"));
        
        assertEquals("1 child", 1, widget.getChildCount());
    }

    public void testCheckBox() {
        UIComponent widget = findComponent(clay, "checkbox");
        assertNotNull("HtmlSelectBooleanCheckbox", widget);
        assertTrue("HtmlSelectBooleanCheckbox",
                widget instanceof HtmlSelectBooleanCheckbox);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.shaleRocks}", exp);

    }

    public void testRadio() {
        UIComponent widget = findComponent(clay, "radio");
        assertNotNull("HtmlSelectOneRadio", widget);
        assertTrue("HtmlSelectOneRadio", widget instanceof HtmlSelectOneRadio);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.rockType}", exp);

    }

    public void testSelectOne() {
        UIComponent widget = findComponent(clay, "states");
        assertNotNull("HtmlSelectOneMenu", widget);
        assertTrue("HtmlSelectOneMenu", widget instanceof HtmlSelectOneMenu);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.state}", exp);

    }

    public void testOption() {
        UIComponent widget = findComponent(clay, "alabama");
        assertNotNull("UISelectItem", widget);
        assertTrue("UISelectItem", widget instanceof UISelectItem);
        assertEquals("itemLabel", "Alabama", widget.getAttributes().get(
                "itemLabel"));
        assertEquals("itemValue", "AL", widget.getAttributes().get("itemValue"));
    }

    public void testSelectMany() {
        UIComponent widget = findComponent(clay, "multiStates");
        assertNotNull("HtmlSelectManyMenu", widget);
        assertTrue("HtmlSelectManyMenu", widget instanceof HtmlSelectManyMenu);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.state}", exp);

    }

    public void testOptions() {
        UIComponent widget = findComponent(clay, "stateOptions");
        assertNotNull("UISelectItems", widget);
        assertTrue("UISelectItems", widget instanceof UISelectItems);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.states}", exp);

    }

    public void testTextArea() {
        UIComponent widget = findComponent(clay, "textArea");
        assertNotNull("HtmlInputTextarea", widget);
        assertTrue("HtmlInputTextarea", widget instanceof HtmlInputTextarea);

        ValueBinding vb = widget.getValueBinding("value");
        assertNotNull("ValueBinding", vb);
        String exp = vb.getExpressionString();
        assertNotNull("Expression String", exp);
        assertEquals("value", "#{test.textarea}", exp);

        assertEquals("rows", "10", widget.getAttributes().get("rows")
                .toString());

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
