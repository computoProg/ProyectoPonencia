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

package org.apache.shale.view.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Test case for <code>DefaultViewControllerMapper</code>.</p>
 */
public class DefaultViewControllerMapperTestCase extends TestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public DefaultViewControllerMapperTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        dvcm = new DefaultViewControllerMapper();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(DefaultViewControllerMapperTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        dvcm = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    DefaultViewControllerMapper dvcm = null;

    // The set of reserved managed bean names that should be treated specially
    String reserved[] = {
        "applicationScope", "cookie", "facesContext", "header", "headerValues",
        "initParam", "param", "paramValues", "requestScope", "sessionScope", "view",
    };

    // ------------------------------------------------------------ Test Methods


    // Test multiple directory level mappings
    public void testMultiple() {

        assertEquals("foo$bar", dvcm.mapViewId("/foo/bar.jsp"));
        assertEquals("foo$bar", dvcm.mapViewId("/foo/bar.xxx"));
        assertEquals("foo$bar", dvcm.mapViewId("foo/bar.jsp"));
        assertEquals("foo$bar", dvcm.mapViewId("foo/bar.xxx"));

    }


    // Test numeric prefix in logical name
    public void testNumeric() {

        // Positive ... should be prefixed
        assertEquals("_1$a", dvcm.mapViewId("/1/a.jsp"));
        assertEquals("_1$1$a", dvcm.mapViewId("/1/1/a.jsp"));

        // Negative ... should not be prefixed
        assertEquals("a$1$b", dvcm.mapViewId("/a/1/b.jsp"));

    }



    // Test a pristine instance
    public void testPristine() {

        ;

    }


    // Test mapping of reserved managed bean names
    public void testReserved() {

        for (int i = 0; i < reserved.length; i++) {
            assertEquals(reserved[i], 
                         "_" + reserved[i],
                         dvcm.mapViewId("/" + reserved[i] + ".jsp"));
        }

    }


    // Test single directory level mappings
    public void testSingle() {

        assertEquals("foo", dvcm.mapViewId("/foo.jsp"));
        assertEquals("foo", dvcm.mapViewId("/foo.xxx"));
        assertEquals("foo", dvcm.mapViewId("foo.jsp"));
        assertEquals("foo", dvcm.mapViewId("foo.xxx"));

    }


}
