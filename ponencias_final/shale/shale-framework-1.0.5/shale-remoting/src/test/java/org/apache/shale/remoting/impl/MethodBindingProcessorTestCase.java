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

import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.remoting.Constants;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockPrintWriter;
import org.apache.shale.test.mock.MockServletOutputStream;

/**
 * <p>Test case for <code>org.apache.shale.remoting.impl.MethodBindingProcessor</code>.</p>
 */
public class MethodBindingProcessorTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MethodBindingProcessorTestCase(String name) {
        super(name);
    }


    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        super.setUp();
        processor = new MethodBindingProcessor();
        processor.setExcludes(Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT);
        processor.setIncludes(Constants.DYNAMIC_RESOURCES_INCLUDES_DEFAULT);
        servletContext.setAttribute("business", new MethodBindingProcessorBusinessObject());

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MethodBindingProcessorTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        servletContext.removeAttribute("business");
        processor = null;
        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    // Resource identifiers that should be rejected by the default
    // include/exclude rules
    private static final String[] DEFAULT_EXCLUDES =
    { "/applicationScope/clear",
      "/requestScope/clear",
      "/response/flushBuffer",
      "/response/getOutputStream",
      "/response/getWriter",
      "/response/reset",
      "/response/resetBuffer",
      "/sessionScope/clear",
    };


    // ------------------------------------------------------ Instance Variables


    // The Processor instance to be tested
    private MethodBindingProcessor processor = null;


    // ------------------------------------------------------------ Test Methods


    // Test attempt to execute a resource id on the implicitly rejected list
    public void testImplicitExclude() throws Exception {

        for (int i = 0; i < DEFAULT_EXCLUDES.length; i++) {
            try {
                processor.process(facesContext, DEFAULT_EXCLUDES[i]);
                assertEquals("Should return 404 for '" + DEFAULT_EXCLUDES[i] + "'",
                             404, response.getStatus());
            } catch (Exception e) {
                fail("Should have returned 404 for '" + DEFAULT_EXCLUDES[i] +
                     "' instead of exception " + e.getMessage());
            }
        }

    }


    // Test attempt to execute an expression with an invalid bean name
    public void testInvalidBean() throws Exception {

        try {
            processor.process(facesContext, "/invalid/directStream");
            fail("Should have thrown EvaluationException");
        } catch (EvaluationException e) {
            ; // Expected result
        }

    }


    // Test attempt to execute an expression with an invalid method name
    public void testInvalidMethod() throws Exception {
        
        try {
            processor.process(facesContext, "/business/invalidMethod");
            fail("Should have thrown MethodNotFoundException");
        } catch (MethodNotFoundException e) {
            ; // Expected result
        }

    }


    // Test mapping of resource identifiers to expressions
    public void testMapping() {

        assertEquals("#{business.directStream}",
                     processor.mapResourceId(facesContext, "/business/directStream").getExpressionString());
        assertEquals("#{business.directWriter}",
                     processor.mapResourceId(facesContext, "/business/directWriter").getExpressionString());
        assertEquals("#{business.indirectStream}",
                     processor.mapResourceId(facesContext, "/business/indirectStream").getExpressionString());
        assertEquals("#{business.indirectWriter}",
                     processor.mapResourceId(facesContext, "/business/indirectWriter").getExpressionString());

    }


    // Test attempt to execute an expression for an excluded pattern
    public void testPatternExcluded() throws Exception {

        processor.setExcludes("/business/*");
        processor.process(facesContext, "/business/directWriter");
        assertEquals(404, response.getStatus());

    }


    // Test attempt to execute an expression for an included pattern
    public void testPatternIncluded() throws Exception {

        processor.setIncludes("/business/*");
        processor.process(facesContext, "/business/directWriter");
        assertEquals(200, response.getStatus());

    }


    // Test attempt to execute an expression for a mixed exclude/include case
    public void testPatternMixed() throws Exception {

        processor.setExcludes("/bar/*");
        processor.setIncludes("/business/*");
        processor.process(facesContext, "/business/directWriter");
        assertEquals(200, response.getStatus());

    }


    // Test a pristine instance of the Processor to be tested
    public void testPristine() {

        assertNotNull(processor);
        assertNotNull(servletContext.getAttribute("business"));
        assertNull(servletContext.getAttribute("invalid"));

    }


    // Test output sent directly to the servlet response ServletOutputStream
    public void testDirectStream() throws Exception {

        processor.process(facesContext, "/business/directStream");
        assertEquals("application/x-binary", response.getContentType());
        MockServletOutputStream stream =
          (MockServletOutputStream) response.getOutputStream();
        assertNotNull(stream);
        assertEquals(10, stream.size());
        byte content[] = stream.content();
        for (int i = 0; i < 10; i++) {
            assertEquals("Byte at position " + i, (byte) i, content[i]);
        }
        assertTrue(facesContext.getResponseComplete());

    }


    // Test output sent directly to the servlet response PrintWriter
    public void testDirectWriter() throws Exception {

        processor.process(facesContext, "/business/directWriter");
        assertEquals("text/x-plain", response.getContentType());
        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        assertNotNull(writer);
        assertEquals(10, writer.size());
        char content[] = writer.content();
        for (int i = 0; i < 10; i++) {
            assertEquals("Character at position " + i, (char) ('a' + i), content[i]);
        }
        assertTrue(facesContext.getResponseComplete());

    }


    // Test output sent indirectly to the servlet or portlet response stream
    public void testIndirectStream() throws Exception {

        processor.process(facesContext, "/business/indirectStream");
        assertEquals("application/x-binary", response.getContentType());
        MockServletOutputStream stream =
          (MockServletOutputStream) response.getOutputStream();
        assertNotNull(stream);
        assertEquals(10, stream.size());
        byte content[] = stream.content();
        for (int i = 0; i < 10; i++) {
            assertEquals("Byte at position " + i, (byte) i, content[i]);
        }
        assertTrue(facesContext.getResponseComplete());

    }


    // Test output sent indirectly to the servlet or portlet response writer
    public void testIndirectWriter() throws Exception {

        processor.process(facesContext, "/business/indirectWriter");
        assertEquals("text/x-plain", response.getContentType());
        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        assertNotNull(writer);
        assertEquals(10, writer.size());
        char content[] = writer.content();
        for (int i = 0; i < 10; i++) {
            assertEquals("Character at position " + i, (char) ('a' + i), content[i]);
        }
        assertTrue(facesContext.getResponseComplete());

    }


    // --------------------------------------------------------- Support Methods



}
