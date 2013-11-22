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
import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.tiger.config.FacesConfigConfig;
import org.apache.shale.tiger.config.TestBean;
import org.apache.shale.tiger.view.faces.LifecycleListener2;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.tiger.faces.VariableResolverImpl</code>.</p>
 */
public class VariableResolverImplTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case
    public VariableResolverImplTestCase(String name) {
        super(name);
    }
    

    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        // Set up mock web application environment
        super.setUp();
        servletContext.addInitParameter("javax.faces.CONFIG_FILES",
                                        "/WEB-INF/test-config-0.xml," +
                                        "/WEB-INF/test-config-1.xml," +
                                        "/WEB-INF/test-config-2.xml," +
                                        "/WEB-INF/test-config-3.xml");
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
        return new TestSuite(VariableResolverImplTestCase.class);
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


    // Test creating "bean0"
    public void testBean0() {

        TestBean bean = (TestBean) resolver.resolveVariable(facesContext, "bean0");
        assertNotNull(bean);
        assertEquals((byte) 1, bean.getByteProperty());
        assertEquals('a', bean.getCharProperty());
        assertEquals((double) 2.0, bean.getDoubleProperty());
        assertEquals((float) 3.0, bean.getFloatProperty());
        assertEquals(4, bean.getIntProperty());
        assertEquals((long) 5, bean.getLongProperty());
        assertEquals((short) 6, bean.getShortProperty());
        assertNull(bean.getStringProperty()); // Overridden in test-config-1.xml

    }


    // Test creating "bean1"
    public void testBean1() {

        TestBean bean = (TestBean) resolver.resolveVariable(facesContext, "bean1");
        assertNotNull(bean);
        assertEquals((byte) 11, bean.getByteProperty()); // Configured
        assertEquals('a', bean.getCharProperty()); // Defaulted
        assertEquals((double) 222.0, bean.getDoubleProperty()); // Configured and overridden
        assertEquals((float) 3.0, bean.getFloatProperty()); // Defaulted
        assertEquals(44, bean.getIntProperty()); // Configured
        assertEquals((long) 5, bean.getLongProperty()); // Defaulted
        assertEquals((short) 6, bean.getShortProperty()); // Defaulted
        assertEquals("String", bean.getStringProperty()); // Defaulted

    }


    // Test creating "bean2"
    public void testBean2() {

        TestBean bean = (TestBean) resolver.resolveVariable(facesContext, "bean2");
        assertNotNull(bean);
        assertEquals((byte) -1, bean.getByteProperty()); // Annotated
        assertEquals('z', bean.getCharProperty()); // Annotated
        assertEquals((double) -2.0, bean.getDoubleProperty()); // Annotated
        assertEquals((float) -3.0, bean.getFloatProperty()); // Annotated
        assertEquals(-4, bean.getIntProperty()); // Annotated
        assertEquals((long) -5, bean.getLongProperty()); // Annotated
        assertEquals((short) -6, bean.getShortProperty()); // Annotated
        assertEquals("Override The Annotation", bean.getStringProperty()); // Annotated and overridden

    }


    // Test creating "bean3"
    public void testBean3() {

        // We need to hook into the rest of the expression evaluation framework,
        // since we will be evaluating expressions to initialize property values
        resolver = new VariableResolverImpl(application.getVariableResolver());
        application.setVariableResolver(resolver);

        // The configuration metadata for "bean3" sets *all* of the properties
        // of the new bean to match the corresponding property values for "bean1".
        // Therefore, all of the assertions below should match the corresponding
        // assertions from testBean1().
        TestBean bean = (TestBean) resolver.resolveVariable(facesContext, "bean3");
        assertNotNull(bean);
        assertEquals((byte) 11, bean.getByteProperty()); // Configured
        assertEquals('a', bean.getCharProperty()); // Defaulted
        assertEquals((double) 222.0, bean.getDoubleProperty()); // Configured and overridden
        assertEquals((float) 3.0, bean.getFloatProperty()); // Defaulted
        assertEquals(44, bean.getIntProperty()); // Configured
        assertEquals((long) 5, bean.getLongProperty()); // Defaulted
        assertEquals((short) 6, bean.getShortProperty()); // Defaulted
        assertEquals("String", bean.getStringProperty()); // Defaulted

    }


    // Test creating existing bean name (delegating)
    public void testExistingDelegating() {

        resolver = new VariableResolverImpl(application.getVariableResolver());
        application.setVariableResolver(resolver);
        externalContext.getRequestMap().put("existing", "This is an existing object");
        Object instance = resolver.resolveVariable(facesContext, "existing");
        assertNotNull(instance);
        assertTrue(instance instanceof String);
        assertEquals("This is an existing object", instance);

    }


    // Test creating existing bean name (plain)
    public void testExistingPlain() {

        externalContext.getRequestMap().put("existing", "This is an existing object");
        Object instance = null;
        try {
            instance = resolver.resolveVariable(facesContext, "existing");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

    }


    // Test pristine instance of the resolver
    public void testPristine() {

        assertNotNull(resolver);
        assertNotNull(application.getVariableResolver());
        FacesConfigConfig config = (FacesConfigConfig)
            externalContext.getApplicationMap().
            get(LifecycleListener2.FACES_CONFIG_CONFIG);
        assertNotNull(config);
        assertEquals(9, config.getManagedBeans().size());

    }


    // Test creating unknown bean name (delegating)
    public void testUnknownDelegating() {

        resolver = new VariableResolverImpl(application.getVariableResolver());
        application.setVariableResolver(resolver);
        Object instance = resolver.resolveVariable(facesContext, "unknown");
        assertNull(instance);

    }


    // Test creating unknown bean name (plain)
    public void testUnknownPlain() {

        Object instance = null;
        try {
            instance = resolver.resolveVariable(facesContext, "unknown");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

    }


}
