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

package org.apache.shale.remoting.impl;

import javax.servlet.http.HttpServletResponse;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.remoting.Constants;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockServletOutputStream;

/**
 * <p>Test case for <code>org.apache.shale.remoting.impl.ClassResourceProcessor</code>.</p>
 */
public class ClassResourceProcessorTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ClassResourceProcessorTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Manifest Constants


    private static final String INVALID_RESOURCE_ID =
            "/org/apache/shale/remoting/impl/MissingData.txt";

    private static final String SENSITIVE_RESOURCE_ID =
            "/org/apache/shale/remoting/Bundle.properties";

    private static final String VALID_RESOURCE_ID =
            "/org/apache/shale/remoting/impl/TestData.txt";

    private static final String VALID_RESOURCE_CONTENT =
            "This is a test.  It is only a test."; // Not including line delimiters!

    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        threadClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        super.setUp();
        servletContext.addMimeType("txt", "text/x-plain");
        processor = new ClassResourceProcessor();
        processor.setExcludes(Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT);
        processor.setIncludes(Constants.CLASS_RESOURCES_INCLUDES_DEFAULT);

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ClassResourceProcessorTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        processor = null;
        super.tearDown();
        Thread.currentThread().setContextClassLoader(threadClassLoader);

    }


    // ------------------------------------------------------ Instance Variables


    // The Processor instance to be tested
    private ClassResourceProcessor processor = null;


    // The cached thread context class loader
    private ClassLoader threadClassLoader = null;


    // ------------------------------------------------------------ Test Methods


    // Test an invalid resource
    public void testInvalidResource() throws Exception {

        processor.process(facesContext, INVALID_RESOURCE_ID);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals(INVALID_RESOURCE_ID, response.getMessage());

    }


    // Test mapping of resource identifiers to URLs
    public void testMapping() {

        assertNotNull(processor.getResourceURL(facesContext, VALID_RESOURCE_ID));
        assertNull(processor.getResourceURL(facesContext, INVALID_RESOURCE_ID));

    }


    // Test attempt to execute an expression for an excluded pattern
    public void testPatternExcluded() throws Exception {

        processor.setExcludes("*.txt");
        processor.process(facesContext, VALID_RESOURCE_ID);
        assertEquals(404, response.getStatus());

    }

    // Test attempt to execute an expression for an included pattern
    // that is not sensitive (i.e. included on the default exclude list)
    public void testPatternIncludedInensitive() throws Exception {

        processor.setExcludes(null);
        processor.setIncludes("*.txt");
        processor.process(facesContext, VALID_RESOURCE_ID);
        assertEquals(200, response.getStatus());

    }

    // Test attempt to execute an expression for an included pattern
    // that is sensitive (i.e. included on the default exclude list)
    public void testPatternIncludedSensitive() throws Exception {

        processor.setExcludes(null);
        processor.setIncludes("*.properties");
        processor.process(facesContext, SENSITIVE_RESOURCE_ID);
        assertEquals(404, response.getStatus());

    }

    // Test attempt to execute an expression for a mixed exclude/include case
    public void testPatternMixed() throws Exception {

        processor.setExcludes("*.properties");
        processor.setIncludes("*.txt");
        processor.process(facesContext, VALID_RESOURCE_ID);
        assertEquals(200, response.getStatus());

    }


    // Test attempt to access an existing sensitive resource that should be
    // blocked by the default configuration
    public void testPatternSensitive() throws Exception {

        assertEquals(Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT + ","
                     + Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT,
                     processor.getExcludes());
        assertEquals(Constants.CLASS_RESOURCES_INCLUDES_DEFAULT, processor.getIncludes());
        processor.process(facesContext, SENSITIVE_RESOURCE_ID);
        assertEquals(404, response.getStatus());

    }


    // Test a pristine instance of the Processor to be tested
    public void testPristine() {

        assertNotNull(processor);
        assertEquals("text/x-plain", servletContext.getMimeType(VALID_RESOURCE_ID));
        assertEquals("text/x-plain", servletContext.getMimeType(INVALID_RESOURCE_ID));

    }


    // Test a valid resource that has not been modified
    public void testNotModifiedResource() throws Exception {

        long timestamp = processor.getLastModified();
        request.addDateHeader("If-Modified-Since", timestamp);
        processor.process(facesContext, VALID_RESOURCE_ID);
        assertEquals(HttpServletResponse.SC_NOT_MODIFIED, response.getStatus());

    }


    // Test a valid resource
    public void testValidResource() throws Exception {

        processor.process(facesContext, VALID_RESOURCE_ID);
        assertEquals("text/x-plain", response.getContentType());
        MockServletOutputStream stream =
          (MockServletOutputStream) response.getOutputStream();
        assertNotNull(stream);
        assertTrue(stream.size() > VALID_RESOURCE_CONTENT.length());
        byte content[] = stream.content();
        for (int i = 0; i < VALID_RESOURCE_CONTENT.length(); i++) {
            byte b = (byte) ((int) VALID_RESOURCE_CONTENT.charAt(i));
            assertEquals("Byte at position " + i, b, content[i]);
        }
        

    }

    // --------------------------------------------------------- Support Methods



}
