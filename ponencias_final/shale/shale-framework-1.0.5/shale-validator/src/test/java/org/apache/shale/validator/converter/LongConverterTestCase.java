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
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletContextEvent;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.validator.faces.ValidatorLifecycleListener;

/**
 * <p>Test case for <code>LongConverter</code>.</p>
 */
public class LongConverterTestCase extends AbstractJsfTestCase {


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public LongConverterTestCase(String name) {
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

        converter = new LongConverter();
        input.setConverter(converter);

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        converter = null;
        input = null;
        form = null;

        listener.contextDestroyed(new ServletContextEvent(servletContext));
        listener = null;

        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    // Valid object values to be tested
    private static final Long[] VALID_OBJECTS =
    { new Long(Long.MIN_VALUE),
      new Long(-123456789),
      new Long(-123456),
      new Long(-123),
      new Long(0),
      new Long(123),
      new Long(123456),
      new Long(123456789),
      new Long(Long.MAX_VALUE),
    };


    // Valid string values to be tested
    private static final String[] VALID_STRINGS =
    { String.valueOf(Long.MIN_VALUE),
      "-123456789",
      "-123456",
      "-123",
      "0",
      "123",
      "123456",
      "123456789",
      String.valueOf(Long.MAX_VALUE),
    };



    // Valid string values with grouping (Locale.GERMANY)
    private static final String[] VALID_STRINGS_DE =
    { "-123.456.789",
      "-123.456",
      "123.456",
      "123.456.789",
    };


    // Valid string values with grouping (Locale.US)
    private static final String[] VALID_STRINGS_US =
    { "-123,456,789",
      "-123,456",
      "123,456",
      "123,456,789",
    };


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Converter instance under test.</p>
     */
    private LongConverter converter = null;


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


    // ------------------------------------------------- Individual Test Methods


    /**
     * <p>Tests for invalid Object input.</p>
     */
    public void testInvalidObject() {


    }


    /**
     * <p>Tests for valid Object input.</p>
     */
    public void testValidObject() {

        for (int i = 0; i < VALID_OBJECTS.length; i++) {
            Long value = VALID_OBJECTS[i];
            try {
                String string = converter.getAsString(facesContext, input, value);
                assertEquals("Long value " + value
                             + " equals String value " + string,
                  value.longValue(), Long.valueOf(strip(string)).longValue());
            } catch (ConverterException e) {
                fail("Should not have thrown ConverterException for value " + value);
            }
        }

    }


    /**
     * <p>Tests for invalid String input.</p>
     */
    public void testInvalidString() {


    }


    /**
     * <p>Tests for valid String input.</p>
     */
    public void testValidString() {

        for (int i = 0; i < VALID_STRINGS.length; i++) {
            String string = VALID_STRINGS[i];
            try {
                Object value = converter.getAsObject(facesContext, input, string);
                assertTrue(value instanceof Long);
                assertEquals("String value " + string
                             + " equals Long value " + value,
                             Long.parseLong(string),
                             ((Long) value).longValue());
            } catch (ConverterException e) {
                fail("Should not have thrown ConverterException for value " + string);
            }
        }

    }


    /**
     * <p>Tests for valid String input with grouping (Locale.GERMANY).</p>
     */
    public void testValidStringDE() {

        facesContext.getViewRoot().setLocale(Locale.GERMANY);
        for (int i = 0; i < VALID_STRINGS_DE.length; i++) {
            String string = VALID_STRINGS_DE[i];
            Object value = converter.getAsObject(facesContext, input, string);
            assertTrue(value instanceof Long);
            assertEquals("String value " + string
                         + " equals Long value " + value,
                         Long.parseLong(strip(string)),
                         ((Long) value).longValue());
        }

    }


    /**
     * <p>Tests for valid String input with grouping (Locale.US).</p>
     */
    public void testValidStringUS() {

        facesContext.getViewRoot().setLocale(Locale.US);
        for (int i = 0; i < VALID_STRINGS_US.length; i++) {
            String string = VALID_STRINGS_US[i];
            Object value = converter.getAsObject(facesContext, input, string);
            assertTrue(value instanceof Long);
            assertEquals("String value " + string
                         + " equals Long value " + value,
                         Long.parseLong(strip(string)),
                         ((Long) value).longValue());
        }

    }


    private String strip(String value) {
        char grouping =
          Locale.US == facesContext.getViewRoot().getLocale() ? ',' : '.';
        while (true) {
            int index = value.indexOf(grouping);
            if (index < 0) {
                break;
            }
            value = value.substring(0, index) + value.substring(index + 1);
        }
        return value;
    }


}
