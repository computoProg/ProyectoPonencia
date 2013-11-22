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

import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.chain.web.ChainListener;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;

// tests registering builders in an alternate namespace
public class ConfigAltNsTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public ConfigAltNsTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ConfigAltNsTestCase.class));

    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
    }
    
    public void testLoadBuilderNamespace() throws Exception {
    	
    	//contains a widget in the xmlns:acme="http://www.acme.org/widgets" namespace
        loadConfigFiles("/org/apache/shale/clay/config/altns-config.xml", null);
        
        // simulate invoking the common chains context listener
        servletContext.addInitParameter("org.apache.commons.chain.CONFIG_WEB_RESOURCE", "/org/apache/shale/clay/config/chain-config.xml");        
        ServletContextListener chainsListener = new ChainListener();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);
        chainsListener.contextInitialized(servletContextEvent);
        
        // find the config bean definition in the commons XML config file
        ComponentBean configBean = standardConfigBean.getElement("acme:city");
        assertNotNull("acme:city exists", configBean);
        assertEquals("id", "city", configBean.getId());
        
        AttributeBean size = configBean.getAttribute("size");
        assertNotNull("size", size);
        assertEquals("size", "20", size.getValue());
        
        AttributeBean value = configBean.getAttribute("value");
        assertNotNull("value", value);
        assertEquals("value", "#{@managed-bean-name.city}", value.getValue());
        
        AttributeBean maxlength = configBean.getAttribute("maxlength");
        assertNotNull("maxlength", maxlength);
        assertEquals("maxlength", "30", maxlength.getValue());
        
        AttributeBean required = configBean.getAttribute("required");
        assertNotNull("required", required);
        assertEquals("required", "true", required.getValue());
       

        // load a template that points/extends the acme:city widget
        ComponentBean templateRoot = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/altns.html");
        assertNotNull("/org/apache/shale/clay/config/altns.html",  templateRoot);
        
        // config bean after extend by the html template
        ComponentBean cityConfigBean = findForId(configBean.getId(), templateRoot);
        assertNotNull("city from html template", cityConfigBean);
        
        // inherit from acme:city
        assertEquals("id", "city", cityConfigBean.getId());
        
        // html override
        size = cityConfigBean.getAttribute("size");
        assertNotNull("size", size);
        assertEquals("size", "10", size.getValue());
        
        // inherit from acme:city
        value = cityConfigBean.getAttribute("value");
        assertNotNull("value", value);
        assertEquals("value", "#{@managed-bean-name.city}", value.getValue());
        
        // html override
        maxlength = cityConfigBean.getAttribute("maxlength");
        assertNotNull("maxlength", maxlength);
        assertEquals("maxlength", "100", maxlength.getValue());
        
        // html override
        required = cityConfigBean.getAttribute("required");
        assertNotNull("required", required);
        assertEquals("required", "false", required.getValue());
        
        // just because we can
        chainsListener.contextDestroyed(servletContextEvent);
    }
    
    private ComponentBean findForId(String id, ComponentBean root) {
        if (root.getId() != null && root.getId().equals(id)) {
           return root;
        }
        Iterator ci = root.getChildren().iterator();
        while (ci.hasNext()) {
           ComponentBean child = (ComponentBean) ci.next();
           ComponentBean target = findForId(id, child);
           if (target != null) {
              return target;
           }
        }
        
        return null;
    }

}
