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
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockPrintWriter;

/**
 * <p>Test case for <code>org.apache.shale.remoting.impl.ClassResourceProcessor</code>.</p>
 */
public class ChainProcessorTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public ChainProcessorTestCase(String name) {
        super(name);
    }


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The valid content (up to the line delimiter) returned for the
     * <code>testProcess()</code> test.</p>
     */
    private static final String PROCESS_CONTENT =
            "This is a test.  It is only a test.";


    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        super.setUp();
        processor = new ChainProcessor();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ChainProcessorTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        processor = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The Processor instance to be tested
    private ChainProcessor processor = null;


    // ------------------------------------------------------------ Test Methods


    // Test the createContext() method
    public void testCreateContext() {

        Context context = processor.createContext(facesContext, "/foo/bar");
        assertNotNull(context);
        assertTrue(context instanceof ChainContext);
        assertTrue(facesContext == context.get("facesContext"));
        assertTrue(facesContext == ((ChainContext) context).getFacesContext());

    }


    // Test the mapCatalog() method
    public void testMapCataog() {

        assertEquals("remoting", processor.mapCatalog(facesContext, "/foo/bar"));
        assertEquals("remoting", processor.mapCatalog(facesContext, "/baz/bop"));

    }


    // Test the mapCommand() method
    public void testMapCommand() {

        assertEquals("foo.bar", processor.mapCommand(facesContext, "/foo/bar"));
        assertEquals("baz.bop", processor.mapCommand(facesContext, "/baz/bop"));

    }


    // Test the situation where the mapped catalog is missing
    public void testMissingCatalog() throws Exception {

        processor.process(facesContext, "/foo/bar");
        assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);
        assertEquals(response.getMessage(), "/foo/bar");

    }


    // Test the situation where the mapped command is missing
    public void testMissingCommand() throws Exception {

        CatalogFactory.getInstance().addCatalog("remoting", new CatalogBase());
        processor.process(facesContext, "/foo/bar");
        assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);
        assertEquals(response.getMessage(), "/foo/bar");

    }


    // Test a pristine instance
    public void testPristine() throws Exception {

        assertNotNull(processor);

    }


    // Test processing of a configured command
    public void testProcess() throws Exception {

        CatalogFactory.getInstance().addCatalog("remoting", new CatalogBase());
        CatalogFactory.getInstance().getCatalog("remoting").
                addCommand("foo.bar", new ChainProcessorCommand());
        processor.process(facesContext, "/foo/bar");
        assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
        assertEquals("text/x-plain", response.getContentType());
        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        char content[] = writer.content();
        assertNotNull(content);
        assertTrue(content.length > PROCESS_CONTENT.length());
        for (int i = 0; i < PROCESS_CONTENT.length(); i++) {
            assertEquals("Character at position " + i, PROCESS_CONTENT.charAt(i), content[i]);
        }

    }


    // Test the sendNotFound() method
    public void testSendNotFound() throws Exception {

        processor.sendNotFound(facesContext, "/foo/bar");
        assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);
        assertEquals(response.getMessage(), "/foo/bar");

    }


    // Test the sendServerError() method
    public void testServerError() throws Exception {

        processor.sendServerError(facesContext, "/foo/bar", new NullPointerException());
        assertEquals(response.getStatus(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals(response.getMessage(), "/foo/bar");

    }




    // --------------------------------------------------------- Support Methods



}
