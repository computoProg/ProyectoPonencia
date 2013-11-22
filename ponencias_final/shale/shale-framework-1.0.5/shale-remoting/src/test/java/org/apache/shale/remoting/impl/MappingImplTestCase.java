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

import java.util.Locale;
import javax.faces.component.UIViewRoot;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.remoting.Constants;
import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>org.apache.shale.remoting.impl.MappingImpl</code>.</p>
 */
public class MappingImplTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MappingImplTestCase(String name) {
        super(name);
    }


    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        super.setUp();
        facesContext.setViewRoot(new UIViewRoot());
        mappings = new MappingsImpl();
        mappings.setExtension(".jsp");
        mappings.setPatterns(new String[] { "*.faces" });
        mapping = new MappingImpl();
        mapping.setMappings(mappings);
        mappings.addMapping(mapping);
        facesContext.getExternalContext().getApplicationMap().
                put(Constants.MAPPINGS_ATTR, mappings);
        facesContext.getViewRoot().setLocale(Locale.getDefault());
        request.setPathElements("/webapp", "/foo", null, null);

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MappingImplTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        mapping = null;
        mappings = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    private MappingImpl mapping = null;


    // The parent instance we must configure for testing
    private MappingsImpl mappings = null;


    // ------------------------------------------------------------ Test Methods


    // Test extension mapping
    public void testExtension() {

        mapping.setPattern("*.foo");
        facesContext.getViewRoot().setViewId("foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId(".foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo.bar");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/bar.foo");
        assertEquals("/bar", mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/bar/baz.foo");
        assertEquals("/bar/baz", mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo/bar.foo" + mappings.getExtension());
        assertEquals("/foo/bar", mapping.mapViewId(facesContext));

    }


    // Test extension mapping workaround for JSF RI bug that leaves
    // "*.faces" on the view id after Restore View phase
    public void testExtensionExtra() {

        mapping.setPattern("*.bop");
        facesContext.getViewRoot().setViewId("/foo/bar.bop" + mappings.getPatterns()[0].substring(1));
        assertEquals("/foo/bar", mapping.mapViewId(facesContext));

    }


    // Test invalid patterns
    public void testInvalid() {

        try {
            mapping.setPattern("");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern("foo");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern("/foo");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern("/foo/");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern(".foo");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern("*.foo.");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

        try {
            mapping.setPattern("*.foo.bar");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // Test prefix mapping
    public void testPrefix() {

        mapping.setPattern("/foo/*");
        facesContext.getViewRoot().setViewId("foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/bar");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/bar/foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo");
        assertNull(mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo/bar");
        assertEquals("/bar", mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo/bar.baz");
        assertEquals("/bar.baz", mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo/bar/baz");
        assertEquals("/bar/baz", mapping.mapViewId(facesContext));
        facesContext.getViewRoot().setViewId("/foo/bar/baz.bop");
        assertEquals("/bar/baz.bop", mapping.mapViewId(facesContext));

    }


    // Test a pristine instance
    public void testPristine() {

        assertNull(mapping.getMechanism());
        assertNull(mapping.getPattern());
        assertNull(mapping.getProcessor());

    }


    // Test mapping resource identifiers to view identifiers
    public void testResourceIdentifiers() throws Exception {

        String resourceId = "/foo/bar.baz";

        // FacesServlet=prefix Mapping=prefix
        mappings.setPatterns(new String[] { "/faces/*" });
        mapping.setPattern("/bop/*");
        assertEquals("/webapp/faces/bop/foo/bar.baz",
                     mapping.mapResourceId(facesContext, resourceId));

        // FacesServlet=prefix Mapping=extension
        mappings.setPatterns(new String[] { "/faces/*" });
        mapping.setPattern("*.bop");
        assertEquals("/webapp/faces/foo/bar.baz.bop",
                     mapping.mapResourceId(facesContext, resourceId));

        // FacesServlet=extension Mapping=prefix
        mappings.setPatterns(new String[] { "*.faces" });
        mapping.setPattern("/bop/*");
        assertEquals("/webapp/bop/foo/bar.baz.faces",
                     mapping.mapResourceId(facesContext, resourceId));

        // FacesServlet=extension Mapping=prefix
        mappings.setPatterns(new String[] { "*.faces" });
        mapping.setPattern("*.bop");
        assertEquals("/webapp/foo/bar.baz.bop.faces",
                     mapping.mapResourceId(facesContext, resourceId));

    }


    // --------------------------------------------------------- Support Methods



}
