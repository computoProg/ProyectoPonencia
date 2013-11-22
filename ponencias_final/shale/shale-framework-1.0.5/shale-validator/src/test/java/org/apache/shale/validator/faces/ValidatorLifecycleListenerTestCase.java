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

package org.apache.shale.validator.faces;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.Globals;
import org.apache.shale.validator.util.ShaleValidatorAction;

/**
 * <p>Test case for the initializations performed by
 * <code>ValidatorLifecycleListener</code> at application startup.</p>
 */
public class ValidatorLifecycleListenerTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ValidatorLifecycleListenerTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        listener = new ValidatorLifecycleListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ValidatorLifecycleListenerTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The number of ShaleVariableAction entries that should be present
     * for each logical type, keyed by the type name.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The contents of this map
     * will need to be adjusted whenever changes are made to the preregistered
     * validators in <code>validator-rules.xml</code>.</p>
     */
    private static final Map ACTION_COUNTS = new HashMap();
    static {
        ACTION_COUNTS.put("byte", new Integer(1));
        ACTION_COUNTS.put("creditCard", new Integer(1));
        ACTION_COUNTS.put("date", new Integer(1));
        ACTION_COUNTS.put("double", new Integer(1));
        ACTION_COUNTS.put("doubleRange", new Integer(2));
        ACTION_COUNTS.put("email", new Integer(1));
        ACTION_COUNTS.put("float", new Integer(1));
        ACTION_COUNTS.put("floatRange", new Integer(2));
        ACTION_COUNTS.put("integer", new Integer(1));
        ACTION_COUNTS.put("intRange", new Integer(2));
        ACTION_COUNTS.put("long", new Integer(1));
        ACTION_COUNTS.put("mask", new Integer(1));
        ACTION_COUNTS.put("maxlength", new Integer(1));
        ACTION_COUNTS.put("minlength", new Integer(1));
        ACTION_COUNTS.put("required", new Integer(1));
        ACTION_COUNTS.put("short", new Integer(1));
        ACTION_COUNTS.put("url", new Integer(1));
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>ValidatorLifecycleListener used to load configuration resources</p>
     */
    ValidatorLifecycleListener listener = null;


    // ------------------------------------------------- Individual Test Methods


    // Test whether the precalculated validator actions includes the data
    // that it should
    public void testActions() {

        // Acquire the configuration data we will need
        ValidatorResources resources = (ValidatorResources)
          servletContext.getAttribute(Globals.VALIDATOR_RESOURCES);
        Map vamap = resources.getValidatorActions();
        Map actions = (Map)
          servletContext.getAttribute(Globals.VALIDATOR_ACTIONS);

        // The precalculated map will not include an entry for
        // "includeJavaScriptUtilities" because it is not a real validation type
        assertEquals(vamap.size() - 1, actions.size());

        // Verify the number of actions for each precalculated validator
        // This also indirectly checks that all entries that should be
        // precalculated are actually present
        Iterator entries = ACTION_COUNTS.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            ValidatorAction va = resources.getValidatorAction(key);
            assertTrue("ValidatorAction " + key + " is present", va != null);
            ShaleValidatorAction[] action = (ShaleValidatorAction[])
              actions.get(key);
            assertTrue("ShaleValidatorAction[] " + key + " is present", action != null);
            assertEquals("ShaleValidatorAction[] " + key + " has correct size",
                         value.intValue(), action.length);
            if (action.length > 1) {
                assertTrue("First two actions for " + key + " are not identical",
                           action[0] != action[1]);
            }
        }

    }


    // Test a pristine instance of the listener
    public void testPristine() {

        assertNotNull(listener);

        ValidatorResources resources = (ValidatorResources)
          servletContext.getAttribute(Globals.VALIDATOR_RESOURCES);
        assertNotNull(resources);
        Map vamap = resources.getValidatorActions();

        Map actions = (Map)
          servletContext.getAttribute(Globals.VALIDATOR_ACTIONS);
        assertNotNull(actions);

    }


    // --------------------------------------------------------- Private Methods


}
