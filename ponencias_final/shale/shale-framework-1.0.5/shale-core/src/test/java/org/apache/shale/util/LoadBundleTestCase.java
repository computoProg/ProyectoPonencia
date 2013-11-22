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
import java.util.Map;

import javax.faces.context.FacesContext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>LoadBundle</code>.</p>
 */
public class LoadBundleTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public LoadBundleTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        lb = new LoadBundle("org.apache.shale.util.TestBundle");

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(LoadBundleTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        lb = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    LoadBundle lb = null;


    // ------------------------------------------------------------ Test Methods


    // Test access to the English values for this resource bundle
    public void testEngish() {

        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("en", "US"));
        Map map = lb.getMap();
        assertNotNull(map);
        assertEquals("English Key 1", map.get("key1"));
        assertEquals("English Key 2", map.get("key2"));

    }


    // Test access to the French values for this resource bundle
    public void testFrench() {

        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("fr", "FR"));
        Map map = lb.getMap();
        assertNotNull(map);
        assertEquals("French Key 1", map.get("key1"));
        assertEquals("French Key 2", map.get("key2"));

    }


    // Test a pristine instance
    public void testPristine() {

        assertEquals("org.apache.shale.util.TestBundle", lb.getBasename());

    }


}
