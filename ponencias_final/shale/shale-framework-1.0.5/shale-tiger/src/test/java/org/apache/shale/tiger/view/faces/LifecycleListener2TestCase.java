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

package org.apache.shale.tiger.view.faces;

import java.io.File;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseListener;
import javax.faces.render.Renderer;
import javax.faces.validator.Validator;
import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.tiger.config.FacesConfigConfig;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;
import org.apache.shale.tiger.managed.config.ManagedPropertyConfig;
import org.apache.shale.tiger.register.faces.PhaseListenerAdapter;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.tiger.view.faces.LifecycleListener</code>.</p>
 */
public class LifecycleListener2TestCase extends AbstractJsfTestCase {
    
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public LifecycleListener2TestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up our listener and bind it to context/session/request objects
        listener = new LifecycleListener();
        servletContext.addAttributeListener(listener);
        session.addAttributeListener(listener);
        request.addAttributeListener(listener);
        
        // Set up mock web application environment
        servletContext.addInitParameter("javax.faces.CONFIG_FILES",
                                        "/WEB-INF/test-config-0.xml," +
                                        "/WEB-INF/test-config-1.xml," +
                                        "/WEB-INF/test-config-2.xml");
        File root = new File(System.getProperty("basedir") + "/target/test-webapp");
        servletContext.setDocumentRoot(root);

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(LifecycleListener2TestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

//        callbacks = null;
        lifecycle = null;

        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    private LifecycleListener listener = null;


    // ------------------------------------------------------------ Test Methods


    // Test standard application bean
    public void testApplicationBean1() {

        ApplicationBean1 bean = new ApplicationBean1();
        assertEquals("", bean.log());
        servletContext.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        servletContext.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        servletContext.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


    // Test annotated application bean
    public void testApplicationBean2() {

        ApplicationBean2 bean = new ApplicationBean2();
        assertEquals("", bean.log());
        servletContext.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        servletContext.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        servletContext.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


    // Test basic operations -- can we initialize and destroy with no exceptions?
    public void testBasic() {

        // Create a ServletContextEvent we will pass to the event methods
        ServletContextEvent event = new ServletContextEvent(servletContext);

        // Initialize the servlet context listener
        listener.contextInitialized(event);

        // Finalize the servlet context listener
        listener.contextDestroyed(event);

    }


    // Test configuration of managed beans
    public void testManagedBeans() {

        // Create a ServletContextEvent we will pass to the event methods
        ServletContextEvent event = new ServletContextEvent(servletContext);

        // Initialize the servlet context listener
        listener.contextInitialized(event);

        // Check out the managed beans configuration information
        FacesConfigConfig fcConfig =
          (FacesConfigConfig) servletContext.getAttribute(LifecycleListener2.FACES_CONFIG_CONFIG);
        assertNotNull(config);
        Map<String,ManagedBeanConfig> mbMap = fcConfig.getManagedBeans();
        assertNotNull(mbMap);
        assertEquals(9, mbMap.size());

        ManagedPropertyConfig mpConfig = null;

        // Validate configuration of bean0
        ManagedBeanConfig bean0 = fcConfig.getManagedBean("bean0");
        assertNotNull(bean0);
        assertTrue(bean0 == mbMap.get("bean0"));
        assertEquals("bean0", bean0.getName());
        assertEquals("org.apache.shale.tiger.config.TestBean", bean0.getType());
        assertEquals("request", bean0.getScope());
        assertNull(bean0.getListEntries());
        assertNull(bean0.getMapEntries());
        Map<String,ManagedPropertyConfig> bean0Map = bean0.getProperties();
        assertNotNull(bean0Map);
        assertEquals(1, bean0Map.size());

        mpConfig = bean0.getProperty("stringProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean0Map.get("stringProperty"));
        assertEquals("stringProperty", mpConfig.getName());
        assertEquals("java.lang.String", mpConfig.getType());
        assertNull(mpConfig.getValue());
        assertTrue(mpConfig.isNullValue());

        // Validate configuration of bean1
        ManagedBeanConfig bean1 = fcConfig.getManagedBean("bean1");
        assertNotNull(bean1);
        assertTrue(bean1 == mbMap.get("bean1"));
        assertEquals("bean1", bean1.getName());
        assertEquals("org.apache.shale.tiger.config.TestBean", bean1.getType());
        assertEquals("session", bean1.getScope());
        assertNull(bean1.getListEntries());
        assertNull(bean1.getMapEntries());
        Map<String,ManagedPropertyConfig> bean1Map = bean1.getProperties();
        assertNotNull(bean1Map);
        assertEquals(3, bean1Map.size());

        mpConfig = bean1.getProperty("byteProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean1Map.get("byteProperty"));
        assertEquals("byteProperty", mpConfig.getName());
        assertEquals("byte", mpConfig.getType());
        assertEquals("11", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean1.getProperty("doubleProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean1Map.get("doubleProperty"));
        assertEquals("doubleProperty", mpConfig.getName());
        assertEquals("double", mpConfig.getType());
        assertEquals("222.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean1.getProperty("intProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean1Map.get("intProperty"));
        assertEquals("intProperty", mpConfig.getName());
        assertEquals("int", mpConfig.getType());
        assertEquals("44", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        // Validate configuration of bean2
        ManagedBeanConfig bean2 = fcConfig.getManagedBean("bean2");
        assertNotNull(bean2);
        assertTrue(bean2 == mbMap.get("bean2"));
        assertEquals("bean2", bean2.getName());
        assertEquals("org.apache.shale.tiger.config.TestBean", bean2.getType());
        assertEquals("request", bean2.getScope());
        assertNull(bean2.getListEntries());
        assertNull(bean2.getMapEntries());
        Map<String,ManagedPropertyConfig> bean2Map = bean2.getProperties();
        assertNotNull(bean2Map);
        assertEquals(8, bean2Map.size());

        mpConfig = bean2.getProperty("byteProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("byteProperty"));
        assertEquals("byteProperty", mpConfig.getName());
        assertEquals("byte", mpConfig.getType());
        assertEquals("-1", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("charProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("charProperty"));
        assertEquals("charProperty", mpConfig.getName());
        assertEquals("char", mpConfig.getType());
        assertEquals("z", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("doubleProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("doubleProperty"));
        assertEquals("doubleProperty", mpConfig.getName());
        assertEquals("double", mpConfig.getType());
        assertEquals("-2.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("floatProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("floatProperty"));
        assertEquals("floatProperty", mpConfig.getName());
        assertEquals("float", mpConfig.getType());
        assertEquals("-3.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("intProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("intProperty"));
        assertEquals("intProperty", mpConfig.getName());
        assertEquals("int", mpConfig.getType());
        assertEquals("-4", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("longProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("longProperty"));
        assertEquals("longProperty", mpConfig.getName());
        assertEquals("long", mpConfig.getType());
        assertEquals("-5", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("shortProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("shortProperty"));
        assertEquals("shortProperty", mpConfig.getName());
        assertEquals("short", mpConfig.getType());
        assertEquals("-6", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2.getProperty("stringProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2Map.get("stringProperty"));
        assertEquals("stringProperty", mpConfig.getName());
        assertEquals("java.lang.String", mpConfig.getType());
        assertEquals("Override The Annotation", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        // Validate configuration of bean2a
        ManagedBeanConfig bean2a = fcConfig.getManagedBean("bean2a");
        assertNotNull(bean2a);
        assertTrue(bean2a == mbMap.get("bean2a"));
        assertEquals("bean2a", bean2a.getName());
        assertEquals("org.apache.shale.tiger.config.TestBean2", bean2a.getType());
        assertEquals("application", bean2a.getScope());
        assertNull(bean2a.getListEntries());
        assertNull(bean2a.getMapEntries());
        Map<String,ManagedPropertyConfig> bean2aMap = bean2a.getProperties();
        assertNotNull(bean2aMap);
        assertEquals(9, bean2aMap.size());

        mpConfig = bean2a.getProperty("byteProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("byteProperty"));
        assertEquals("byteProperty", mpConfig.getName());
        assertEquals("byte", mpConfig.getType());
        assertEquals("-1", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("charProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("charProperty"));
        assertEquals("charProperty", mpConfig.getName());
        assertEquals("char", mpConfig.getType());
        assertEquals("z", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("doubleProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("doubleProperty"));
        assertEquals("doubleProperty", mpConfig.getName());
        assertEquals("double", mpConfig.getType());
        assertEquals("-2.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("floatProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("floatProperty"));
        assertEquals("floatProperty", mpConfig.getName());
        assertEquals("float", mpConfig.getType());
        assertEquals("-3.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("intProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("intProperty"));
        assertEquals("intProperty", mpConfig.getName());
        assertEquals("int", mpConfig.getType());
        assertEquals("-4", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("longProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("longProperty"));
        assertEquals("longProperty", mpConfig.getName());
        assertEquals("long", mpConfig.getType());
        assertEquals("-5", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("shortProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("shortProperty"));
        assertEquals("shortProperty", mpConfig.getName());
        assertEquals("short", mpConfig.getType());
        assertEquals("-6", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("stringProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("stringProperty"));
        assertEquals("stringProperty", mpConfig.getName());
        assertEquals("java.lang.String", mpConfig.getType());
        assertEquals("Annotated", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean2a.getProperty("xtraProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean2aMap.get("xtraProperty"));
        assertEquals("xtraProperty", mpConfig.getName());
        assertEquals("java.lang.String", mpConfig.getType());
        assertEquals("Xtra Override", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        // Validate configuration of bean3
        ManagedBeanConfig bean3 = fcConfig.getManagedBean("bean3");
        assertNotNull(bean3);
        assertTrue(bean3 == mbMap.get("bean3"));
        assertEquals("bean3", bean3.getName());
        assertEquals("org.apache.shale.tiger.config.TestBean3", bean3.getType());
        assertEquals("session", bean3.getScope());
        assertNull(bean3.getListEntries());
        assertNull(bean3.getMapEntries());
        Map<String,ManagedPropertyConfig> bean3Map = bean3.getProperties();
        assertNotNull(bean3Map);
        assertEquals(8, bean3Map.size());

        mpConfig = bean3.getProperty("byteProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("byteProperty"));
        assertEquals("byteProperty", mpConfig.getName());
        assertEquals("byte", mpConfig.getType());
        assertEquals("-1", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("charProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("charProperty"));
        assertEquals("charProperty", mpConfig.getName());
        assertEquals("char", mpConfig.getType());
        assertEquals("z", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("doubleProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("doubleProperty"));
        assertEquals("doubleProperty", mpConfig.getName());
        assertEquals("double", mpConfig.getType());
        assertEquals("-2.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("floatProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("floatProperty"));
        assertEquals("floatProperty", mpConfig.getName());
        assertEquals("float", mpConfig.getType());
        assertEquals("-3.0", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("intProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("intProperty"));
        assertEquals("intProperty", mpConfig.getName());
        assertEquals("int", mpConfig.getType());
        assertEquals("-4", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("longProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("longProperty"));
        assertEquals("longProperty", mpConfig.getName());
        assertEquals("long", mpConfig.getType());
        assertEquals("-5", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("shortProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("shortProperty"));
        assertEquals("shortProperty", mpConfig.getName());
        assertEquals("short", mpConfig.getType());
        assertEquals("-6", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        mpConfig = bean3.getProperty("stringProperty");
        assertNotNull(mpConfig);
        assertTrue(mpConfig == bean3Map.get("stringProperty"));
        assertEquals("stringProperty", mpConfig.getName());
        assertEquals("java.lang.String", mpConfig.getType());
        assertEquals("Annotated", mpConfig.getValue());
        assertTrue(!mpConfig.isNullValue());

        // Test existence of "org.apache.shale.TAG_UTILITY_BEAN" managed bean
        ManagedBeanConfig tagUtilityBean = fcConfig.getManagedBean("org.apache.shale.TAG_UTILITY_BEAN");
        assertNotNull(tagUtilityBean);

        // Finalize the servlet context listener
        listener.contextDestroyed(event);

    }


    // Test a prisine instance
    public void testPristine() {

        assertNotNull(listener);

    }


    // Test registration of JavaServer Faces objects
    public void testRegister() {

        // Create a ServletContextEvent we will pass to the event methods
        ServletContextEvent event = new ServletContextEvent(servletContext);

        // Initialize the servlet context listener
        listener.contextInitialized(event);

        // Check for the widgets that should have been registered
        UIComponent comp = application.createComponent("foo.MyComponent");
        assertNotNull(comp);
        Converter conv = application.createConverter("foo.MyConverter");
        assertNotNull(conv);
        Renderer rend = renderKit.getRenderer("foo.MyFamily", "foo.MyRenderer");
        assertNotNull(rend);
        Validator val = application.createValidator("foo.MyValidator");
        assertNotNull(val);

        // Check our lifecycle instance as well
        PhaseListener listeners[] = lifecycle.getPhaseListeners();
        assertNotNull(listeners);
        assertEquals(1, listeners.length);
        assertTrue(listeners[0] instanceof PhaseListenerAdapter);

        // Finalize the servlet context listener
        listener.contextDestroyed(event);

    }


    // Test standard request bean
    public void testRequestBean1() {

        RequestBean1 bean = new RequestBean1();
        assertEquals("", bean.log());
        request.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        request.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        request.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


    // Test annotated request bean
    public void testRequestBean2() {

        RequestBean2 bean = new RequestBean2();
        assertEquals("", bean.log());
        request.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        request.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        request.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


    // Test standard session bean
    public void testSessionBean1() {

        SessionBean1 bean = new SessionBean1();
        assertEquals("", bean.log());
        session.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        session.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        session.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


    // Test annotated session bean
    public void testSessionBean2() {

        SessionBean2 bean = new SessionBean2();
        assertEquals("", bean.log());
        session.setAttribute("bean", bean);
        assertEquals("init/", bean.log());
        session.setAttribute("bean", bean);
        assertEquals("init/destroy/init/", bean.log());
        session.removeAttribute("bean");
        assertEquals("init/destroy/init/destroy/", bean.log());

    }


}
