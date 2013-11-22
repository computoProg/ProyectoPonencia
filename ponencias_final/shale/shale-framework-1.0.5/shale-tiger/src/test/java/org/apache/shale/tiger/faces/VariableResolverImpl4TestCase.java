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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.tiger.config.TestBean4;
import org.apache.shale.tiger.view.faces.LifecycleListener2;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.tiger.faces.VariableResolverImpl</code>
 * when processing resource <code>/WEB-INF/test-config-4.xml</code>.</p>
 */
public class VariableResolverImpl4TestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case
    public VariableResolverImpl4TestCase(String name) {
        super(name);
    }
    

    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        // Set up mock web application environment
        super.setUp();
        servletContext.addInitParameter("javax.faces.CONFIG_FILES",
                                        "/WEB-INF/test-config-4.xml");
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
        return new TestSuite(VariableResolverImpl4TestCase.class);
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


    // Test creating bean "explicitIntegerList"
    public void testExplicitIntegerList() {

        Object instance = resolver.resolveVariable(facesContext, "explicitIntegerList");
        assertNotNull(instance);
        assertTrue(instance instanceof Vector);
        List list = (List) instance;
        assertEquals(4, list.size());

        assertEquals(new Integer(123), list.get(0));
        assertEquals(new Integer(234), list.get(1));
        assertNull(list.get(2));
        assertEquals(new Integer(345), list.get(3));

    }


    // Test creating bean "explicitStringList"
    public void testExplicitStringList() {

        Object instance = resolver.resolveVariable(facesContext, "explicitStringList");
        assertNotNull(instance);
        assertTrue(instance instanceof LinkedList);
        List list = (List) instance;
        assertEquals(5, list.size());

        assertEquals("foo", list.get(0));
        assertEquals("bar", list.get(1));
        assertNull(list.get(2));
        assertEquals("baz", list.get(3));
        assertEquals("bop", list.get(4));

    }


    // Test creating bean "implicitStringList"
    public void testImplicitStringList() {

        Object instance = resolver.resolveVariable(facesContext, "implicitStringList");
        assertNotNull(instance);
        assertTrue(instance instanceof ArrayList);
        List list = (List) instance;
        assertEquals(5, list.size());

        assertEquals("bop", list.get(0));
        assertNull(list.get(1));
        assertEquals("baz", list.get(2));
        assertEquals("bar", list.get(3));
        assertEquals("foo", list.get(4));

    }


    // Test creating bean "listPropertiesBean"
    public void testListPropertiesBean() {

        Object instance = resolver.resolveVariable(facesContext, "listPropertiesBean");
        assertNotNull(instance);
        assertTrue(instance instanceof TestBean4);
        TestBean4 bean = (TestBean4) instance;

        List emptyList = bean.getEmptyList();
        assertNotNull(emptyList);
        assertTrue(emptyList instanceof ArrayList);
        assertEquals(5, emptyList.size());

        List fullList = bean.getFullList();
        assertNotNull(fullList);
        assertTrue(fullList instanceof Vector);
        assertEquals(7, fullList.size());

    }


}
