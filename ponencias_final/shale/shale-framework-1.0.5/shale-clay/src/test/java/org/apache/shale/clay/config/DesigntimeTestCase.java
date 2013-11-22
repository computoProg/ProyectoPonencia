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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ComponentConfigBean;
import org.apache.shale.clay.config.beans.SymbolBean;

public class DesigntimeTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public DesigntimeTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(DesigntimeTestCase.class));

    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDesigntimeOn() {
        ((ComponentConfigBean) standardConfigBean).setDesigntime(true);       
        loadConfigFiles(null, null);
        
        ComponentBean bean = standardConfigBean.getElement("clay");
        assertNotNull(bean);
        
        String description = bean.getDescription();
        assertNotNull(description);
        
        assertTrue(description.startsWith("This component builds a sub component tree and attaches"));
        
        AttributeBean attr = bean.getAttribute("managedBeanName");
        assertNotNull(attr);
        
        description = attr.getDescription();
        assertEquals("A symbol that is used to alias the bound backing bean.", description);

        
        bean = standardConfigBean.getElement("baseHtml");
        assertNotNull(bean);
        
        description = bean.getDescription();
        assertNotNull(description);
        
        assertTrue(description.startsWith("Abstract base component definition"));
        
        SymbolBean symbol = (SymbolBean) bean.getSymbols().get("@class");
        assertNotNull(symbol);
        
        description = symbol.getDescription();
        assertNotNull(description);
        
        assertEquals("The default value of the styleClass attribute.", description);
        
        
    }

    
    public void testDesigntimeOff() {
        ((ComponentConfigBean) standardConfigBean).setDesigntime(false);       
        loadConfigFiles(null, null);
        
        ComponentBean bean = standardConfigBean.getElement("clay");
        assertNotNull(bean);
        
        String description = bean.getDescription();
        assertNull(description);
               
        AttributeBean attr = bean.getAttribute("managedBeanName");
        assertNotNull(attr);
        
        description = attr.getDescription();
        assertNull(description);

        bean = standardConfigBean.getElement("baseHtml");
        assertNotNull(bean);
        
        description = bean.getDescription();
        assertNull(description);

        SymbolBean symbol = (SymbolBean) bean.getSymbols().get("@class");
        assertNotNull(symbol);
        
        description = symbol.getDescription();
        assertNull(description);
        
    }


    public void testDesigntimeOnInheritance() {
        ((ComponentConfigBean) standardConfigBean).setDesigntime(true);       
        loadConfigFiles(null, null);

        ComponentBean bean1 = standardConfigBean.getElement("f:converter");
        assertNotNull(bean1);
        
        assertNotNull(bean1.getDescription());
        
        ComponentBean bean2 = standardConfigBean.getElement(bean1.getExtends());
        assertNotNull(bean2);

        assertNotNull(bean2.getDescription());
        assertEquals(bean2.getDescription(), bean1.getDescription());

    }     
        
    
    public void testSerializable() throws Exception {
        // test the object serialization of the metadata beans
        
        ((ComponentConfigBean) standardConfigBean).setDesigntime(true);       
        loadConfigFiles(null, null);

        ComponentBean live = standardConfigBean.getElement("h:outputText");
        assertNotNull(live);
       
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(os);
        out.writeObject(live);
        out.close();
        
       
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectInputStream in = new ObjectInputStream(is);
        
        ComponentBean memorex = (ComponentBean) in.readObject();
        in.close();
        
        assertNotNull(memorex);
        
        assertEquals(live.getDescription(), memorex.getDescription());
        assertEquals(live.getJsfid(), memorex.getJsfid());
        assertEquals(live.getAttribute("styleClass").getValue(), memorex.getAttribute("styleClass").getValue());
        assertEquals(live.getSymbol("class").getValue(), memorex.getSymbol("class").getValue());
        
    }
}
