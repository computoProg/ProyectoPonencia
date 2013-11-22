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
package org.apache.shale.clay.convert;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractViewControllerTestCase;
import org.apache.shale.util.ConverterHelper;

public class StringArrayConverterTestCase extends
        AbstractViewControllerTestCase {

    // Construct a new instance of this test case.
    public StringArrayConverterTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(StringArrayConverterTestCase.class));
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        Class clazz = Class.forName("[Ljava.lang.String;");
        
        facesContext.getApplication().addConverter(clazz,
                 "org.apache.shale.clay.convert.StringArrayConverter");
    }

    // test how Clay would use it to convert a string property into a target String[]
    public void testDefaults() {
        ConverterHelper helper = new ConverterHelper();
        String[] partialTriggers = {"one", "two", "three"};
        String asString = helper.asString(facesContext, String[].class, partialTriggers);
        
        assertNotNull(asString);
        assertEquals("one two three", asString);
        
        String[] asObject = (String[]) helper.asObject(facesContext, String[].class, asString);
        assertNotNull(asObject);
        
        assertEquals(partialTriggers .length, asObject.length);
        for (int i = 0; i < partialTriggers .length; i++) {
            assertEquals("item " + i + ":", partialTriggers[i], asObject[i]);
        }
        
    }
    
    public void testCustom() {
        StringArrayConverter converter = (StringArrayConverter) facesContext.getApplication()
                                                      .createConverter(String[].class);
        converter.setDelimiter(new Character('"'));
        converter.setSeparator(new Character(','));
        
        UIComponent dummy = new HtmlInputText();
        
        String[] partialTriggers  = {"one", "two", "three"};
        String asString = converter.getAsString(facesContext, dummy, partialTriggers);
        
        assertNotNull(asString);
        assertEquals("\"one\",\"two\",\"three\"", asString);
        
        String[] asObject = (String[]) converter.getAsObject(facesContext, dummy, asString);
        assertNotNull(asObject);
        
        assertEquals(partialTriggers.length, asObject.length);
        for (int i = 0; i < partialTriggers.length; i++) {
            assertEquals("item " + i + ":", partialTriggers[i], asObject[i]);
        }

    }

}
