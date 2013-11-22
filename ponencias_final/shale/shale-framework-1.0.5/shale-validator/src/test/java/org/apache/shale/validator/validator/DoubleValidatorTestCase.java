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

import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContextEvent;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;

/**
 * <p>Test case for <code>DoubleValidator</code>.</p>
 */
public class DoubleValidatorTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public DoubleValidatorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        facesContext.getViewRoot().setLocale(Locale.US);

        listener = new ValidatorLifecycleListener();
        listener.contextInitialized(new ServletContextEvent(servletContext));

        form = new UIForm();
        form.setId("form");
        facesContext.getViewRoot().getChildren().add(form);

        input = new UIInput();
        input.setId("input");
        form.getChildren().add(input);

        validator = new DoubleValidator();
        input.addValidator(validator);

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(DoubleValidatorTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        validator = null;
        input = null;
        form = null;

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The form component for our input form.</p>
     */
    private UIForm form = null;


    /**
     * <p>The text field component for our input form.</p>
     */
    private UIInput input = null;


    /**
     * <p>ValidatorLifecycleListener used to load configuration resources</p>
     */
    private ValidatorLifecycleListener listener = null;


    /**
     * <p>Validator instance under test.</p>
     */
    private DoubleValidator validator = null;


    // ------------------------------------------------- Individual Test Methods


    /**
     * <p>Tests for invalid input with no range limits.</p>
     */
    public void testInvalidInput() {

        // NOTE - null and zero-length string are irrelevant inputs, because
        // JSF will not call validators in that scenario

        try {
            validator.validate(facesContext, input, "abc");
            fail("Should have thrown ValidatorException");
        } catch (ValidatorException e) {
            ; // Expected result
//            System.err.println("a: " + e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a maximum range value has been specified
     * with an invalid value.</p>
     */
    public void testInvalidMaximum() {

        validator.setMaximum(123);
        try {
            validator.validate(facesContext, input, new Double(234));
            fail("Should have thrown ValidatorException");
        } catch (ValidatorException e) {
            ; // Expected result
//            System.err.print("234: " + e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a minimum range value has been specified
     * with an invalid value.</p>
     */
    public void testInvalidMinimum() {

        validator.setMinimum(234);
        try {
            validator.validate(facesContext, input, new Double(123));
            fail("Should have thrown ValidatorException");
        } catch (ValidatorException e) {
            ; // Expected result
//            System.err.print("123: " + e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a minimum and minimum range value has been specified
     * with an invalid value.</p>
     */
    public void testInvalidRange() {

        validator.setMinimum(0);
        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(-1));
            fail("Should have thrown ValidatorException");
        } catch (ValidatorException e) {
            ; // Expected result
//            System.err.println("-1: " + e.getFacesMessage().getSummary());
        }

        validator.setMinimum(0);
        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(235));
            fail("Should have thrown ValidatorException");
        } catch (ValidatorException e) {
            ; // Expected result
//            System.err.println("-1: " + e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Tests for valid input with no range limits.</p>
     */
    public void testValidInput() {

        // NOTE - null and zero-length string are irrelevant inputs, because
        // JSF will not call validators in that scenario

        try {
            validator.validate(facesContext, input, new Double(0));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        try {
            validator.validate(facesContext, input, new Double(123));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        try {
            validator.validate(facesContext, input, new Double(-456));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a maximum range value has been specified
     * with a valid value.</p>
     */
    public void testValidMaximum() {

        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(123));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(234));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a minimum range value has been specified
     * with a valid value.</p>
     */
    public void testValidMinimum() {

        validator.setMinimum(0);
        try {
            validator.validate(facesContext, input, new Double(0));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        validator.setMinimum(0);
        try {
            validator.validate(facesContext, input, new Double(123));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

    }


    /**
     * <p>Test cases where a minimum and minimum range value has been specified
     * with a valid value.</p>
     */
    public void testValidRange() {

        validator.setMinimum(0);
        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(0));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        validator.setMinimum(0);
        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(123));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

        validator.setMinimum(0);
        validator.setMaximum(234);
        try {
            validator.validate(facesContext, input, new Double(234));
        } catch (ValidatorException e) {
            fail("Should not have thrown ValidatorException: " +
                    e.getFacesMessage().getSummary());
        }

    }


}
