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

package org.apache.shale.util;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>Messages</code>.</p>
 */
public class MessagesTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MessagesTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        m = new Messages("org.apache.shale.util.TestBundle");

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MessagesTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        m = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    Messages m = null;


    // ------------------------------------------------------------ Test Methods


    // Test access to the English values for this resource bundle
    public void testEngish() {

        Locale l = new Locale("en", "US");
        assertEquals("English Key 1", m.getMessage("key1", l));
        assertEquals("English Key 2", m.getMessage("key2", l));

    }


    // Test access to the French values for this resource bundle
    public void testFrench() {

        Locale l = new Locale("fr", "FR");
        assertEquals("French Key 1", m.getMessage("key1", l));
        assertEquals("French Key 2", m.getMessage("key2", l));

    }


    // Test a pristine instance
    public void testPristine() {

        assertEquals("org.apache.shale.util.TestBundle", m.getName());

    }


}
