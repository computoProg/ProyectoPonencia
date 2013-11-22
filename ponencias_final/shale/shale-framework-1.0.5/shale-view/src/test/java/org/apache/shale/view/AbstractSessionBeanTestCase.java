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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.view.AbstractSessionBean</code>.</p>
 */
public class AbstractSessionBeanTestCase extends AbstractJsfTestCase {
    
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public AbstractSessionBeanTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        listener = new SessionAttributeListener();
        session.addAttributeListener(listener);
        session.addAttributeListener(new LifecycleListener());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(AbstractSessionBeanTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener = null;
        SessionAttributeListener.clear();
        ConcreteSessionBean.clear();
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    SessionAttributeListener listener = null;


    // ------------------------------------------------------------ Test Methods



    // Test a prisine instance
    public void testPristine() {

        assertEquals("", SessionAttributeListener.record());

    }


    // Test processing an attribute
    public void testProcess() {

        assertEquals("", SessionAttributeListener.record());
        ConcreteSessionBean bean = new ConcreteSessionBean();
        assertEquals("", ConcreteSessionBean.record());

        session.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteSessionBean)//",
                     SessionAttributeListener.record());
        assertEquals("init()//",
                     ConcreteSessionBean.record());

        session.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteSessionBean)//" +
                     "attributeReplaced(bean,ConcreteSessionBean)//",
                     SessionAttributeListener.record());
        assertEquals("init()//destroy()//init()//",
                     ConcreteSessionBean.record());

        session.removeAttribute("bean");
        assertEquals("attributeAdded(bean,ConcreteSessionBean)//" +
                     "attributeReplaced(bean,ConcreteSessionBean)//" +
                     "attributeRemoved(bean,ConcreteSessionBean)//",
                     SessionAttributeListener.record());
        assertEquals("init()//destroy()//init()//destroy()//",
                     ConcreteSessionBean.record());

    }


}
