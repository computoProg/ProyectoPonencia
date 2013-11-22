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

package org.apache.shale.validator.converter;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletContextEvent;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;
import org.apache.shale.validator.util.AbstractUtilities;

/**
 * <p>Test case for the utility methods provided by
 * <code>AbstractConverter</code>.</p>
 */
public class AbstractConverterTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public AbstractConverterTestCase(String name) {
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

        converter = new ConcreteConverter();

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        converter = null;

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
     * <p>Converter instance under test.</p>
     */
    protected ConcreteConverter converter = null;


    // ------------------------------------------------- Individual Test Methods


    // Test retrieving messages
    public void testMessage() {

        String message = null;

        // Check a message that should be overridden by the application
        message = converter.message(facesContext, "Long.unconverted");
        assertNotNull(message);
        assertEquals("Custom Long Error Message", message);

        // Check a message that should be grabbed from the default resources
        message = converter.message(facesContext, "Integer.unconverted");
        assertNotNull(message);
        assertEquals(defaultBundle.getString("Integer.unconverted"), message);

        // Check a message that should be undefined
        message = converter.message(facesContext, "???undefined???");
        assertNotNull(message);

    }


}
