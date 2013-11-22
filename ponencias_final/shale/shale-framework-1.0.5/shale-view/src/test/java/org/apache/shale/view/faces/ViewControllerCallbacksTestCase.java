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

package org.apache.shale.view.faces;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Test case for <code>org.apache.shale.view.faces.ViewControllerCallbacks</code>.</p>
 */
public class ViewControllerCallbacksTestCase extends TestCase {
    
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ViewControllerCallbacksTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        callbacks = new ViewControllerCallbacks();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ViewControllerCallbacksTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        callbacks = null;

    }


    // ------------------------------------------------------ Instance Variables


    private ViewControllerCallbacks callbacks = null;


    // ------------------------------------------------------------ Test Methods



    // Test a prisine instance
    public void testPristine() {

        ;

    }


    // Test calling all of the appropriate methods in order
    public void testViewControllerCallbacks() {

        TestViewController tvc = new TestViewController();
        callbacks.preprocess(tvc);
        callbacks.prerender(tvc);
        assertEquals("preprocess/prerender/",
                     tvc.log());

        assertTrue(callbacks.isViewController(tvc));
    }


}
