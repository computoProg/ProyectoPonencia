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

package org.apache.shale.remoting.faces;

import javax.faces.application.ViewHandler;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.remoting.Constants;
import org.apache.shale.remoting.Mapping;
import org.apache.shale.remoting.Mappings;
import org.apache.shale.remoting.Mechanism;
import org.apache.shale.remoting.Processor;
import org.apache.shale.remoting.impl.ClassResourceProcessor;
import org.apache.shale.remoting.impl.FilteringProcessor;
import org.apache.shale.remoting.impl.MappingImpl;
import org.apache.shale.remoting.impl.MappingsImpl;
import org.apache.shale.remoting.impl.MethodBindingProcessor;
import org.apache.shale.remoting.impl.WebResourceProcessor;
import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>org.apache.shale.remoting.MappingsHelper</code>.
 * These tests focus on correct configuration, not on executable
 * functionality.</p>
 */
public class MappingsHelperTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MappingsHelperTestCase(String name) {
        super(name);
    }


    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        super.setUp();
        helper = new MappingsHelper();
        // mappings instance set after configuration, not here

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MappingsHelperTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        mappings = null;
        helper = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    private MappingsHelper helper = null;


    // An individual Mapping instance to be validated
    private Mapping mapping = null;


    // The mappings instance retrieved after configuration
    private Mappings mappings = null;


    // An individual Processor instance to be validated
    private Processor processor = null;


    // ------------------------------------------------------------ Test Methods


    // Test an invalid exclude pattern
    public void testInvalidExclude() {

        servletContext.addInitParameter(Constants.CLASS_RESOURCES_EXCLUDES,
                                        "*.class,/foo");
        try {
            mappings = helper.getMappings(facesContext);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test an invalid include pattern
    public void testInvalidInclude() {

        servletContext.addInitParameter(Constants.DYNAMIC_RESOURCES_INCLUDES,
                                        "/bar/*,*.x.y");
        try {
            mappings = helper.getMappings(facesContext);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test an invalid mechanism pattern
    public void testInvalidMechanism() {

        servletContext.addInitParameter(Constants.WEBAPP_RESOURCES_PARAM,
                                        "*/*:org.apache.shale.remoting.impl.MethodBindingProcessor");
        try {
            mappings = helper.getMappings(facesContext);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test a pristine instance with default configuration
    public void testPristine() {

        assertNotNull(helper);

        // Acquire the configured mappings instance
        mappings = helper.getMappings(facesContext);
        assertNotNull(mappings);

        // Validate the characteristics of the Mappings instance itself
        assertTrue(mappings instanceof MappingsImpl);
        assertEquals(ViewHandler.DEFAULT_SUFFIX, mappings.getExtension());
        assertEquals(3, mappings.getMappings().size());
        assertEquals(0, mappings.getPatterns().length);

        // Validate the "/static/*" Mapping instance for valid configuration
        mapping = mappings.getMapping("/static/*");
        assertNotNull(mapping);
        assertTrue(mapping instanceof MappingImpl);
        assertTrue(mappings == mapping.getMappings());
        assertEquals(Mechanism.CLASS_RESOURCE, mapping.getMechanism());
        assertEquals("/static/*", mapping.getPattern());
        processor = mapping.getProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof ClassResourceProcessor);
        assertEquals(Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT + ","
                     + Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getExcludes());
        assertEquals(Constants.CLASS_RESOURCES_INCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getIncludes());

        // Validate the "/dynamic/*" Mapping instance for valid configuration
        mapping = mappings.getMapping("/dynamic/*");
        assertNotNull(mapping);
        assertTrue(mapping instanceof MappingImpl);
        assertTrue(mappings == mapping.getMappings());
        assertEquals(Mechanism.DYNAMIC_RESOURCE, mapping.getMechanism());
        assertEquals("/dynamic/*", mapping.getPattern());
        processor = mapping.getProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof MethodBindingProcessor);
        assertEquals(Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT + ","
                     + Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getExcludes());
        assertEquals(Constants.DYNAMIC_RESOURCES_INCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getIncludes());

        // Validate the "/webapp/*" Mapping instance for valid configuration
        mapping = mappings.getMapping("/webapp/*");
        assertNotNull(mapping);
        assertTrue(mapping instanceof MappingImpl);
        assertTrue(mappings == mapping.getMappings());
        assertEquals(Mechanism.WEBAPP_RESOURCE, mapping.getMechanism());
        assertEquals("/webapp/*", mapping.getPattern());
        processor = mapping.getProcessor();
        assertNotNull(processor);
        assertTrue(processor instanceof WebResourceProcessor);
        assertEquals(Constants.WEBAPP_RESOURCES_EXCLUDES_DEFAULT + ","
                     + Constants.WEBAPP_RESOURCES_EXCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getExcludes());
        assertEquals(Constants.WEBAPP_RESOURCES_INCLUDES_DEFAULT,
                     ((FilteringProcessor) processor).getIncludes());

    }


    // --------------------------------------------------------- Support Methods



}
