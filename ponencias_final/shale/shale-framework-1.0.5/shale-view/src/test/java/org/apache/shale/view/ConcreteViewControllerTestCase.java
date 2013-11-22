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

/**
 * <p>Test case for <code>AbstractViewController</code>.</p>
 */
public class ConcreteViewControllerTestCase extends ConcreteFacesBeanTestCase {
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ConcreteViewControllerTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        bean = new ConcreteViewController();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ConcreteViewControllerTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        bean = null;
        super.tearDown();

    }


    // ------------------------------------------------------------ Test Methods


    // Test access to the postBack property
    public void testPostBack() {

        ConcreteViewController cvc = (ConcreteViewController) bean;
        assertTrue(!cvc.isPostBack());
        cvc.setPostBack(true);
        assertTrue(cvc.isPostBack());

    }


}
