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

package org.apache.shale.validator.validator;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;

import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;
import org.apache.shale.validator.util.AbstractUtilities;
import org.apache.shale.validator.util.ShaleValidatorAction;

/**
 * <p>Test case for the utility methods provided by
 * <code>AbstractValidator</code>.  The protected <code>validate()</code>
 * method is not tested here, as it is presumed that this will be exercised
 * repeatedly by the test cases for the various concrete validators.</p>
 */
public class AbstractValidatorTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public AbstractValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        application.setMessageBundle("org.apache.shale.validator.TestBundle");
        facesContext.getViewRoot().setLocale(Locale.US);

        listener = new ValidatorLifecycleListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));

        validator = new ConcreteValidator();

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        validator = null;

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Default resource bundle for error message lookup
     */
    protected static final ResourceBundle defaultBundle =
      ResourceBundle.getBundle(AbstractUtilities.DEFAULT_RESOURCE_BUNDLE, Locale.US);


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>ValidatorLifecycleListener used to load configuration resources</p>
     */
    protected ValidatorLifecycleListener listener = null;


    /**
     * <p>Validator instance under test.</p>
     */
    protected ConcreteValidator validator = null;


    // ------------------------------------------------- Individual Test Methods


    // Test retrieving actions arrays
    public void testActions() {

        ShaleValidatorAction actions[] = null;

        actions = validator.actions(facesContext, "double");
        assertNotNull(actions);
        assertEquals(1, actions.length);

        actions = validator.actions(facesContext, "doubleRange");
        assertNotNull(actions);
        assertEquals(2, actions.length);

        actions = validator.actions(facesContext, "intRange");
        assertNotNull(actions);
        assertEquals(2, actions.length);
        assertEquals("integer", actions[0].getAction().getName());
        assertEquals("intRange", actions[1].getAction().getName());

        actions = validator.actions(facesContext, "???undefined???");
        assertNull(actions);

    }


    // Test type conversions via registered converters
    public void testConvert() {

        ; // FIXME - add some tests for conversions

    }


    // Test retrieving messages
    public void testMessage() {

        String message = null;

        // Check a message that should be overridden by the application
        message = validator.message(facesContext, "Long.unconverted");
        assertNotNull(message);
        assertEquals("Custom Long Error Message", message);

        // Check a message that should be grabbed from the default resources
        message = validator.message(facesContext, "Integer.unconverted");
        assertNotNull(message);
        assertEquals(defaultBundle.getString("Integer.unconverted"), message);

        // Check a message that should be undefined
        message = validator.message(facesContext, "???undefined???");
        assertNotNull(message);

    }


    // Test retrieving resources
    public void testResources() {

        ValidatorAction action = null;

        ValidatorResources resources = validator.resources(facesContext);
        assertNotNull(resources);

        action = resources.getValidatorAction("integer");
        assertNotNull(action);

        action = resources.getValidatorAction("intRange");
        assertNotNull(action);

        action = resources.getValidatorAction("???undefined");
        assertNull(action);

    }


}
