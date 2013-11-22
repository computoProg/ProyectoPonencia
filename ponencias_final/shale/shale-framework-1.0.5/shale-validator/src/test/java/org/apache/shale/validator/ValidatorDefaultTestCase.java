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

package org.apache.shale.validator;

import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.validator.ValidatorAction;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;

/**
 * <p>Test case for <code>CommonsValidator</code>.</p>
 */
public class ValidatorDefaultTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ValidatorDefaultTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        servletContext.addInitParameter
            (Globals.VALIDATOR_RULES,
             Globals.DEFAULT_VALIDATOR_RULES +
             ", /org/apache/shale/validator/custom-rules.xml");
        listener = new ValidatorLifecycleListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ValidatorDefaultTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // ValidatorLifecycleListener used to load configuration resources
    ValidatorLifecycleListener listener = null;


    // ------------------------------------------------------------ Test Methods


    // Test access to the 'required' validation rule with default configuration
    public void testRequired() {

        ValidatorAction va = CommonsValidator.getValidatorAction( "required" );
        assertNotNull(va);
        assertEquals("required",va.getName());
        assertEquals("org.apache.shale.validator.CommonsValidator",
                va.getClassname());
        assertEquals("isSupplied",va.getMethod());

    }

}
