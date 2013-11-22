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

package org.apache.shale.clay.utils;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.component.UIComponent;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.test.base.AbstractViewControllerTestCase;

public class ClayAmalgamTestCase extends AbstractViewControllerTestCase {

    private ClayAmalgam clayAmalgam = null;
        
    protected void setUp() throws Exception {
        super.setUp();
        
        // Configure document root for tests
        String documentRoot = System.getProperty("documentRoot");
        if (documentRoot == null) {
        	documentRoot = System.getProperty("user.dir") + "\\target\\test-classes";            
        }
        servletContext.setDocumentRoot(new File(documentRoot));

        clayAmalgam = new ClayAmalgam();
        application.addComponent("org.apache.shale.clay.component.Clay", "org.apache.shale.clay.component.Clay"); 
    }
 
    // Construct a new instance of this test case.
    public ClayAmalgamTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(ClayAmalgamTestCase.class));
    }

    //test the import file into a outputText component
    public void testClayImport() {
       //test excapeXml=true
       Map requestParams = new TreeMap();
       //context root import
       requestParams.put("url", "/org/apache/shale/clay/utils/hello.html");
       externalContext.setRequestParameterMap(requestParams);
       
       ComponentBean displayElement = new ComponentBean();
       displayElement.setId("clayImport");
       displayElement.setComponentType("org.apache.shale.clay.component.Clay");
       displayElement.setJsfid("clayImport");
        
       UIComponent component = application.createComponent("org.apache.shale.clay.component.Clay");     
       component.setId("clayImport");
       component.getAttributes().put("url","#{param.url}");
       component.getAttributes().put("escapeXml", Boolean.TRUE.toString());
       
       clayAmalgam.clayImport(facesContext, component, displayElement);
       
       assertEquals("jsfid", "outputText", displayElement.getJsfid());
       assertEquals("componentType", "javax.faces.HtmlOutputText", displayElement.getComponentType());
       
       AttributeBean attr = displayElement.getAttribute("escape");
       assertNotNull("escape", attr);
       assertEquals("escape", Boolean.FALSE.toString(), attr.getValue());
       
       attr = displayElement.getAttribute("isTransient");
       assertNotNull("isTransient", attr);
       assertEquals("isTransient", Boolean.TRUE.toString(), attr.getValue());
       
       attr = displayElement.getAttribute("value");
       assertNotNull("value", attr);
       assertEquals("value", "&lt;html&gt;&lt;head&gt;&lt;/head&gt;&lt;body bgcolor=\"blue\"&gt;Hello World&lt;/body&gt;&lt;/html&gt;", attr.getValue());

       //test excapeXml=false
       component.getAttributes().put("escapeXml", Boolean.FALSE.toString());
       displayElement = new ComponentBean();
       displayElement.setId("clayImport");
       displayElement.setComponentType("org.apache.shale.clay.component.Clay");
       displayElement.setJsfid("clayImport");
      
       clayAmalgam.clayImport(facesContext, component, displayElement);
       
       assertEquals("jsfid", "outputText", displayElement.getJsfid());
       assertEquals("componentType", "javax.faces.HtmlOutputText", displayElement.getComponentType());
       
       attr = displayElement.getAttribute("escape");
       assertNotNull("escape", attr);
       assertEquals("escape", Boolean.FALSE.toString(), attr.getValue());
       
       attr = displayElement.getAttribute("isTransient");
       assertNotNull("isTransient", attr);
       assertEquals("isTransient", Boolean.TRUE.toString(), attr.getValue());
       
       attr = displayElement.getAttribute("value");
       assertNotNull("value", attr);
       assertEquals("value", "<html><head></head><body bgcolor=\"blue\">Hello World</body></html>", attr.getValue());

       
       //test classpath file import
       requestParams = new TreeMap();
       requestParams.put("url", "classpath*:/org/apache/shale/clay/utils/hello.html");
       externalContext.setRequestParameterMap(requestParams);
       
       displayElement = new ComponentBean();
       displayElement.setId("clayImport");
       displayElement.setComponentType("org.apache.shale.clay.component.Clay");
       displayElement.setJsfid("clayImport");
        
       component = application.createComponent("org.apache.shale.clay.component.Clay");     
       component.setId("clayImport");
       component.getAttributes().put("url","#{param.url}");
       component.getAttributes().put("escapeXml", Boolean.TRUE.toString());
       
       clayAmalgam.clayImport(facesContext, component, displayElement);
       attr = displayElement.getAttribute("value");
       assertNotNull("value", attr);
       assertEquals("value", "", attr.getValue());

       
       //test no file found
       requestParams = new TreeMap();
       requestParams.put("url", "/org/apache/shale/clay/utils/notfound.html");
       externalContext.setRequestParameterMap(requestParams);
       
       displayElement = new ComponentBean();
       displayElement.setId("clayImport");
       displayElement.setComponentType("org.apache.shale.clay.component.Clay");
       displayElement.setJsfid("clayImport");
        
       component = application.createComponent("org.apache.shale.clay.component.Clay");     
       component.setId("clayImport");
       component.getAttributes().put("url","#{param.url}");
       component.getAttributes().put("escapeXml", Boolean.TRUE.toString());
       
       clayAmalgam.clayImport(facesContext, component, displayElement);
       attr = displayElement.getAttribute("value");
       assertNotNull("value", attr);
       assertEquals("value", "", attr.getValue());

       
    }
    
    //convert an escaped value into a un-escaped outputText
    public void testClayOut() {
        ComponentBean displayElement = new ComponentBean();
        displayElement.setId("clayOut");
        displayElement.setComponentType("org.apache.shale.clay.component.Clay");
        displayElement.setJsfid("clayOut");

        UIComponent component = application.createComponent("org.apache.shale.clay.component.Clay");     
        component.setId("clayOut");
        component.getAttributes().put("value","&lt;html&gt;&lt;head&gt;&lt;/head&gt;&lt;body bgcolor=\"blue\"&gt;Hello World&lt;/body&gt;&lt;/html&gt;");
        component.getAttributes().put("escapeXml", Boolean.FALSE.toString());
        
   
        clayAmalgam.clayOut(facesContext, component, displayElement);
        
        assertEquals("jsfid", "outputText", displayElement.getJsfid());
        assertEquals("componentType", "javax.faces.HtmlOutputText", displayElement.getComponentType());
        
        AttributeBean attr = displayElement.getAttribute("escape");
        assertNotNull("escape", attr);
        assertEquals("escape", Boolean.FALSE.toString(), attr.getValue());
        
        attr = displayElement.getAttribute("isTransient");
        assertNotNull("isTransient", attr);
        assertEquals("isTransient", Boolean.TRUE.toString(), attr.getValue());
        
        attr = displayElement.getAttribute("value");
        assertNotNull("value", attr);
        assertEquals("value", "<html><head></head><body bgcolor=\"blue\">Hello World</body></html>", attr.getValue());
          
    }
    
}
