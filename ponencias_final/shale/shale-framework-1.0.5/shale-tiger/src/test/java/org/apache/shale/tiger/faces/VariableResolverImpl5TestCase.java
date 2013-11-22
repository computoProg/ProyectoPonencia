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

package org.apache.shale.tiger.faces;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.tiger.config.TestBean5;
import org.apache.shale.tiger.view.faces.LifecycleListener2;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.tiger.faces.VariableResolverImpl</code>
 * when processing resource <code>/WEB-INF/test-config-5.xml</code>.</p>
 */
public class VariableResolverImpl5TestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case
    public VariableResolverImpl5TestCase(String name) {
        super(name);
    }
    

    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        // Set up mock web application environment
        super.setUp();
        servletContext.addInitParameter("javax.faces.CONFIG_FILES",
                                        "/WEB-INF/test-config-5.xml");
        File root = new File(System.getProperty("basedir") + "/target/test-webapp");
        servletContext.setDocumentRoot(root);

        // Process our configuration information
        listener = new LifecycleListener2();
        listener.contextInitialized(new ServletContextEvent(servletContext));

        // Create resolver instance to be tested
        // (Force NPEs on delegation use cases by default)
        resolver = new VariableResolverImpl(null);

    }


    // Return the tests included in this test case.
    public static Test suite() {
        return new TestSuite(VariableResolverImpl5TestCase.class);
    }


    // Tear down instance variables required by this test case
    protected void tearDown() throws Exception {

        // Release tested instances
        resolver = null;

        // Finalize our context listener
        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        // Tear down the mock web application environment
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // LifecycleListener instance to be tested
    LifecycleListener listener = null;

    // VariableResolverImpl instance to be tested
    VariableResolverImpl resolver = null;


    // ------------------------------------------------------------ Test Methods


    // Test creating bean "stringIntegerMap"
    public void testStringIntegerMap() {

        Object instance = resolver.resolveVariable(facesContext, "stringIntegerMap");
        assertNotNull(instance);
        assertTrue(instance instanceof TreeMap);
        Map map = (Map) instance;
        assertEquals(4, map.size());

        assertEquals(new Integer(123), map.get("First"));
        assertEquals(new Integer(234), map.get("Second"));
        assertNull(map.get("Third"));
        assertEquals(new Integer(345), map.get("Fourth"));

    }


    // Test creating bean "listPropertiesBean"
    public void testMapPropertiesBean() {

        Object instance = resolver.resolveVariable(facesContext, "mapPropertiesBean");
        assertNotNull(instance);
        assertTrue(instance instanceof TestBean5);
        TestBean5 bean = (TestBean5) instance;

        Map emptyMap = bean.getEmptyMap();
        assertNotNull(emptyMap);
        assertTrue(emptyMap instanceof HashMap);
        assertEquals(3, emptyMap.size());

        Map fullMap = bean.getFullMap();
        assertNotNull(fullMap);
        assertTrue(fullMap instanceof LinkedHashMap);
        assertEquals(4, fullMap.size());

    }

}
