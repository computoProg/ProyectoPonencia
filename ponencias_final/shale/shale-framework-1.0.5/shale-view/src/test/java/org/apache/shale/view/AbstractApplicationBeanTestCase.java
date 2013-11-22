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

import javax.servlet.ServletContextAttributeListener;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.view.AbstractApplicationBean</code>.</p>
 */
public class AbstractApplicationBeanTestCase extends AbstractJsfTestCase {
    
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public AbstractApplicationBeanTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        listener = new ApplicationAttributeListener();
        servletContext.addAttributeListener(listener);
        servletContext.addAttributeListener(new LifecycleListener());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(AbstractApplicationBeanTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener = null;
        ApplicationAttributeListener.clear();
        ConcreteApplicationBean.clear();
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    ServletContextAttributeListener listener = null;


    // ------------------------------------------------------------ Test Methods



    // Test a prisine instance
    public void testPristine() {

        assertEquals("", ApplicationAttributeListener.record());

    }


    // Test processing an attribute
    public void testProcess() {

        assertEquals("", ApplicationAttributeListener.record());
        ConcreteApplicationBean bean = new ConcreteApplicationBean();
        assertEquals("", ConcreteApplicationBean.record());

        servletContext.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteApplicationBean)//",
                     ApplicationAttributeListener.record());
        assertEquals("init()//",
                     ConcreteApplicationBean.record());

        servletContext.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteApplicationBean)//" +
                     "attributeReplaced(bean,ConcreteApplicationBean)//",
                     ApplicationAttributeListener.record());
        assertEquals("init()//destroy()//init()//",
                     ConcreteApplicationBean.record());

        servletContext.removeAttribute("bean");
        assertEquals("attributeAdded(bean,ConcreteApplicationBean)//" +
                     "attributeReplaced(bean,ConcreteApplicationBean)//" +
                     "attributeRemoved(bean,ConcreteApplicationBean)//",
                     ApplicationAttributeListener.record());
        assertEquals("init()//destroy()//init()//destroy()//",
                     ConcreteApplicationBean.record());

    }


}
