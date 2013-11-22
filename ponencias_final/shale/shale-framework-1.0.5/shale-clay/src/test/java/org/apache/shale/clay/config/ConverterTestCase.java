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
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.BooleanConverter;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;

public class ConverterTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public ConverterTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ConverterTestCase.class));
    }

    private Clay clay = null;

    protected void setUp() throws Exception {
        super.setUp();

        loadConfigFiles("/org/apache/shale/clay/config/converter-config.xml",
                null);

        // register one of the standard converters
        facesContext.getApplication().addConverter("javax.faces.Boolean",
                "javax.faces.convert.BooleanConverter");

        clay = (Clay) application
                .createComponent("org.apache.shale.clay.component.Clay");
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/converter.html");
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

    public void testConverterComponentType() {
        HtmlOutputLabel label = (HtmlOutputLabel) findComponent(clay, "testlabel1");
        assertNotNull(label);
        
        assertNotNull(label.getConverter());
        assertTrue(label.getConverter() instanceof BooleanConverter);
    }

    public void testConverterIdOverride() {
        HtmlOutputLabel label = (HtmlOutputLabel) findComponent(clay, "testlabel2");
        assertNotNull(label);
        
        assertNotNull(label.getConverter());
        assertTrue(label.getConverter() instanceof BooleanConverter);
 
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
