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

package org.apache.shale.view;

import java.util.Iterator;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>AbstractFacesBean</code>.</p>
 */
public class ConcreteFacesBeanTestCase extends AbstractJsfTestCase {
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ConcreteFacesBeanTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        bean = new ConcreteFacesBean();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ConcreteFacesBeanTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        bean = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    AbstractFacesBean bean = null;

    
    // ------------------------------------------------------------ Test Methods


    // Test access to application scope attributes
    public void testApplicationMap() {

        servletContext.setAttribute("foo", "bar");
        Map map = bean.getApplicationMap();
        assertTrue(map.containsKey("foo"));
        assertTrue(map.containsValue("bar"));
        assertEquals("bar", map.get("foo"));
        map.put("baz", "bop");
        assertEquals("bop", servletContext.getAttribute("baz"));
        map.remove("foo");
        assertNull(servletContext.getAttribute("foo"));

    }


    // Test the getBean() method
    public void testGetBean() {

        servletContext.setAttribute("foo1", "bar1");
        request.setAttribute("foo2", "bar2");
        session.setAttribute("foo3", "bar3");
        assertEquals("bar1", bean.getBean("foo1"));
        assertEquals("bar2", bean.getBean("foo2"));
        assertEquals("bar3", bean.getBean("foo3"));

    }


    // Test the getValue() method
    public void testGetValue() {

        servletContext.setAttribute("foo", new ConcreteFacesBean("bar"));
        assertEquals("bar", bean.getValue("#{foo.id}"));

    }


    private Severity messagesAttachedSeverity[] = {
        FacesMessage.SEVERITY_ERROR,
        FacesMessage.SEVERITY_ERROR,
        FacesMessage.SEVERITY_ERROR,
        FacesMessage.SEVERITY_ERROR,
        FacesMessage.SEVERITY_FATAL,
        FacesMessage.SEVERITY_FATAL,
        FacesMessage.SEVERITY_FATAL,
        FacesMessage.SEVERITY_INFO,
        FacesMessage.SEVERITY_INFO,
        FacesMessage.SEVERITY_WARN,
    };

    private String messagesAttachedSummary[] = {
        "Error 0", "Error 1", "Error 2", "Error 3",
        "Fatal 0", "Fatal 1", "Fatal 2",
        "Info 0", "Info 1",
        "Warn 0",
    };


    // Test messages attached to a particular component
    public void testMessagesAttached() {

        UIViewRoot root = new UIViewRoot();
        root.setId("root");
        facesContext.setViewRoot(root);
        bean.error(root, "Error 0");
        bean.error(root, "Error 1");
        bean.error(root, "Error 2");
        bean.error(root, "Error 3");
        bean.fatal(root, "Fatal 0");
        bean.fatal(root, "Fatal 1");
        bean.fatal(root, "Fatal 2");
        bean.info(root, "Info 0");
        bean.info(root, "Info 1");
        bean.warn(root, "Warn 0");
        checkMessages(facesContext.getMessages(root.getClientId(facesContext)),
                      messagesAttachedSeverity,
                      messagesAttachedSummary);

    }


    private Severity messagesUnattachedSeverity[] = {
        FacesMessage.SEVERITY_ERROR,
        FacesMessage.SEVERITY_FATAL,
        FacesMessage.SEVERITY_FATAL,
        FacesMessage.SEVERITY_INFO,
        FacesMessage.SEVERITY_INFO,
        FacesMessage.SEVERITY_INFO,
        FacesMessage.SEVERITY_WARN,
        FacesMessage.SEVERITY_WARN,
        FacesMessage.SEVERITY_WARN,
        FacesMessage.SEVERITY_WARN,
    };

    private String messagesUnattachedSummary[] = {
        "Error 0",
        "Fatal 0", "Fatal 1",
        "Info 0", "Info 1", "Info 2",
        "Warn 0", "Warn 1", "Warn 2", "Warn 3",
    };


    // Test messages not attached to any particular component
    public void testMessagesUnattached() {

        bean.error("Error 0");
        bean.fatal("Fatal 0");
        bean.fatal("Fatal 1");
        bean.info("Info 0");
        bean.info("Info 1");
        bean.info("Info 2");
        bean.warn("Warn 0");
        bean.warn("Warn 1");
        bean.warn("Warn 2");
        bean.warn("Warn 3");
        checkMessages(facesContext.getMessages(null),
                      messagesUnattachedSeverity,
                      messagesUnattachedSummary);

    }


    // Test pristine instance
    public void testPristine() {

        assertNotNull(bean.getApplication());
        assertNotNull(bean.getApplicationMap());
        assertNotNull(bean.getExternalContext());
        assertNotNull(bean.getFacesContext());
        assertNotNull(bean.getLifecycle());
        assertNotNull(bean.getRequestMap());
        assertNotNull(bean.getSessionMap());

    }


    // Test access to request scope attributes
    public void testRequestMap() {

        request.setAttribute("foo", "bar");
        Map map = bean.getRequestMap();
        assertTrue(map.containsKey("foo"));
        assertTrue(map.containsValue("bar"));
        assertEquals("bar", map.get("foo"));
        map.put("baz", "bop");
        assertEquals("bop", request.getAttribute("baz"));
        map.remove("foo");
        assertNull(request.getAttribute("foo"));

    }


    // Test access to session scope attributes
    public void testSessionMap() {

        session.setAttribute("foo", "bar");
        Map map = bean.getSessionMap();
        assertTrue(map.containsKey("foo"));
        assertTrue(map.containsValue("bar"));
        assertEquals("bar", map.get("foo"));
        map.put("baz", "bop");
        assertEquals("bop", session.getAttribute("baz"));
        map.remove("foo");
        assertNull(session.getAttribute("foo"));

    }


    // Test the setBean()method
    public void testSetBean() {

        bean.setBean("foo", "bar");
        assertEquals("bar", bean.getBean("foo"));
        assertEquals("bar", request.getAttribute("foo"));

    }


    // Test the setValue() method
    public void testSetValue() {

        servletContext.setAttribute("foo", new ConcreteFacesBean("bar"));
        bean.setValue("#{foo.id}", "baz");
        assertEquals("baz", bean.getValue("#{foo.id}"));
        assertEquals("baz", ((ConcreteFacesBean) servletContext.getAttribute("foo")).getId());

    }


    // --------------------------------------------------------- Support Methods


    // Check the queued messages for correct severity and summary messages
    private void checkMessages(Iterator messages, Severity severity[], String summary[]) {
        int n = 0;
        while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            assertEquals(summary[n] + " severity", severity[n], message.getSeverity());
            assertEquals(summary[n] + " summary", summary[n], message.getSummary());
            n++;
        }
    }


}
