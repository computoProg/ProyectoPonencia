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

import javax.servlet.ServletRequestAttributeListener;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.view.faces.LifecycleListener;

/**
 * <p>Test case for <code>org.apache.shale.view.AbstractRequestBean</code>.</p>
 */
public class AbstractRequestBeanTestCase extends AbstractJsfTestCase {
    
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public AbstractRequestBeanTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        listener = new RequestAttributeListener();
        request.addAttributeListener(listener);
        request.addAttributeListener(new LifecycleListener());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(AbstractRequestBeanTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener = null;
        RequestAttributeListener.clear();
        ConcreteRequestBean.clear();
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    ServletRequestAttributeListener listener = null;


    // ------------------------------------------------------------ Test Methods



    // Test a prisine instance
    public void testPristine() {

        assertEquals("", RequestAttributeListener.record());

    }


    // Test processing an attribute
    public void testProcess() {

        assertEquals("", RequestAttributeListener.record());
        ConcreteRequestBean bean = new ConcreteRequestBean();
        assertEquals("", ConcreteRequestBean.record());

        request.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteRequestBean)//",
                     RequestAttributeListener.record());
        assertEquals("init()//",
                     ConcreteRequestBean.record());

        request.setAttribute("bean", bean);
        assertEquals("attributeAdded(bean,ConcreteRequestBean)//" +
                     "attributeReplaced(bean,ConcreteRequestBean)//",
                     RequestAttributeListener.record());
        assertEquals("init()//destroy()//init()//",
                     ConcreteRequestBean.record());

        request.removeAttribute("bean");
        assertEquals("attributeAdded(bean,ConcreteRequestBean)//" +
                     "attributeReplaced(bean,ConcreteRequestBean)//" +
                     "attributeRemoved(bean,ConcreteRequestBean)//",
                     RequestAttributeListener.record());
        assertEquals("init()//destroy()//init()//destroy()//",
                     ConcreteRequestBean.record());

    }


}
