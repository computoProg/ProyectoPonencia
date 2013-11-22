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

package org.apache.shale.remoting;

import java.util.Locale;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.remoting.faces.ResponseFactory;
import org.apache.shale.remoting.impl.MappingImpl;
import org.apache.shale.remoting.impl.MappingsImpl;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockPrintWriter;

/**
 * <p>Unit tests for <code>org.apache.shale.remoting.XhtmlHelper</code>.</p>
 */
public class XhtmlHelperTestCase extends AbstractJsfTestCase {
    
    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public XhtmlHelperTestCase(String name) {
        super(name);
    }


    // ----------------------------------------------------------- Setup Methods


    // Set up instance variables for this test case.
    protected void setUp() throws Exception {

        super.setUp();
        helper = new XhtmlHelper();
        facesContext.setViewRoot(new UIViewRoot());
        mappings = new MappingsImpl();
        mappings.setExtension(".jsp");
        mappings.setPatterns(new String[] { "*.faces" });
        mapping = new MappingImpl();
        mapping.setMappings(mappings);
        mapping.setMechanism(Mechanism.CLASS_RESOURCE);
        mappings.addMapping(mapping);
        facesContext.getExternalContext().getApplicationMap().
                put(Constants.MAPPINGS_ATTR, mappings);
        facesContext.getViewRoot().setLocale(Locale.getDefault());
        request.setPathElements("/webapp", "/foo", null, null);

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(XhtmlHelperTestCase.class));

    }


    // Tear down instance variables for this test case.
    protected void tearDown() throws Exception {

        helper = null;
        mapping = null;
        mappings = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The helper instance to be tested
    private XhtmlHelper helper = null;


    // The mapping instance to be tested
    private MappingImpl mapping = null;


    // The mappings instance to be tested
    private MappingsImpl mappings = null;


    // -------------------------------------------------------- Static Variables


    // Prefix to the rendered JavaScript test string
    private static final String JAVASCRIPT_CONTENT =
      "<script type=\"text/javascript\" src=\"/webapp/dynamic/foo/bar.js.faces\"/>";


    // Prefix to the rendered Stylesheet test string
    private static final String STYLESHEET_CONTENT =
      "<link type=\"text/css\" rel=\"stylesheet\" href=\"/webapp/faces/foo/bar.css.dyn\"/>";


    // ------------------------------------------------------------ Test Methods


    // Test linking to a JavaScript resource
    public void testLinkJavascript() throws Exception {

        // Use extension mapping for FacesServlet and prefix mapping for the resource
        mappings.setPatterns(new String[] { "*.faces" });
        mapping.setPattern("/dynamic/*");

        // Perform the link request and check the results
        facesContext.setResponseWriter
          ((new ResponseFactory()).getResponseWriter(facesContext, "text/javascript"));
        helper.linkJavascript(facesContext, new UIOutput(),
                              facesContext.getResponseWriter(),
                              Mechanism.CLASS_RESOURCE, "/foo/bar.js");

        // Evaluate the results
        assertEquals("text/javascript", response.getContentType());
        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        char content[] = writer.content();
        assertNotNull(content);
        assertTrue(content.length > JAVASCRIPT_CONTENT.length());
        for (int i = 0; i < JAVASCRIPT_CONTENT.length(); i++) {
            assertEquals("Character at position " + i, JAVASCRIPT_CONTENT.charAt(i), content[i]);
        }


    }

    // Test a pristine instance
    public void testPristine() {

        ;

    }


    // Test linking to a stylesheet resource
    public void testLinkStylesheet() throws Exception {

        // Use prefix mapping for FacesServlet and extension mapping for the resource
        mappings.setPatterns(new String[] { "/faces/*" });
        mapping.setPattern("*.dyn");

        // Perform the link request and check the results
        facesContext.setResponseWriter
          ((new ResponseFactory()).getResponseWriter(facesContext, "text/css"));
        helper.linkStylesheet(facesContext, new UIOutput(),
                              facesContext.getResponseWriter(),
                              Mechanism.CLASS_RESOURCE, "/foo/bar.css");

        // Evaluate the results
        assertEquals("text/css", response.getContentType());
        MockPrintWriter writer = (MockPrintWriter) response.getWriter();
        char content[] = writer.content();
        assertNotNull(content);
        assertTrue(content.length > STYLESHEET_CONTENT.length());
        for (int i = 0; i < STYLESHEET_CONTENT.length(); i++) {
            assertEquals("Character at position " + i, STYLESHEET_CONTENT.charAt(i), content[i]);
        }

    }


}
