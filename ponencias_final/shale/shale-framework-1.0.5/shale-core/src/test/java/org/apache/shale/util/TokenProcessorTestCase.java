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

package org.apache.shale.util;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ResponseWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.component.Token;
import org.apache.shale.renderer.TokenRenderer;
import org.apache.shale.test.base.AbstractJsfTestCase;

/**
 * <p>Test case for <code>TokenProcessor</code>.</p>
 */
public class TokenProcessorTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public TokenProcessorTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();

        // Set up the instance we will be testing
        tp = new TokenProcessor();
        
        //register the Token component
        application.addComponent("org.apache.shale.Token", "org.apache.shale.component.Token");
        facesContext.getRenderKit().addRenderer("org.apache.shale.Token", 
                "org.apache.shale.Token",
                new TokenRenderer());
    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(TokenProcessorTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        tp = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // The instance to be tested
    TokenProcessor tp = null;


    // ------------------------------------------------------------ Test Methods


    // Test generation of multiple tokens and the ability to validate them
    public void testMultiple() {

        Set set = new HashSet();

        // Create a bunch of tokens
        for (int i = 0; i < 1000; i++) {
            String token = null;
            while (true) {
                token = tp.generate(facesContext);
                if (!set.contains(token)) {
                    break;
                }
            }
            set.add(token);
        }

        // Ensure that we can verify all of them
        Iterator tokens = set.iterator();
        while (tokens.hasNext()) {
            String token = (String) tokens.next();
            assertTrue("Cannot verify token '" + token + "'", tp.verify(facesContext, token));
        }
        
    }


    // Test a pristine instance
    public void testPristine() {

        assertNotNull(tp);

    }

    public void testMessagePropertyOverride() throws Exception {

       final String SUMMARY_MESSAGE = "This page is dirty.  Evil browser back button."; 
       final String DETAIL_MESSAGE = "This page has been mark as having durability of a single submit and this contract has been violated by the submission of a dirty page.";

       Token token = (Token) facesContext.getApplication().createComponent("org.apache.shale.Token");
       assertNotNull(token);
       
       UIViewRoot root = facesContext.getViewRoot();
       assertNotNull(root);
       
       // add token to the component tree
       root.getChildren().add(root.getChildCount(), token);
             
       token.setId("messagePropertyOverride");
       token.setMessageSummary(SUMMARY_MESSAGE);
       token.setMessageDetail(DETAIL_MESSAGE);
       
       StringBuffer htmlSnippet = encode(token);       
       // check rendered markup
       String id = getAttribute(htmlSnippet, "id");
       assertEquals("id", token.getClientId(facesContext), id);

       String name = getAttribute(htmlSnippet, "name");
       assertEquals("id", token.getClientId(facesContext), name);

       String type = getAttribute(htmlSnippet, "type");
       assertEquals("id", "hidden", type);

       String value = getAttribute(htmlSnippet, "value");
       assertNotNull("value", value);
       
       // simulate form post on dirty page
       Map map = facesContext.getExternalContext().getRequestParameterMap();
       map.put(id, value);
       
       // simulate apply values
       token.decode(facesContext);
       assertEquals("value", value, token.getSubmittedValue());
        
       // simulate validation and invalidates token
       token.validate(facesContext);
       
       checkNoMessages(token);
       
       // simulate double post
       token.validate(facesContext);
       
       // check for the custom message
       checkMessage(SUMMARY_MESSAGE, DETAIL_MESSAGE, token);
       
    }
        
    public void testMessageFacesConfigBundleOverride() throws Exception {
        
        // simulate a faces config resource bundle override
        application.setDefaultLocale(new Locale("en", "US"));
        application.setMessageBundle("org.apache.shale.util.TestBundle");

        //value of the "token.invalid" key in the "org.apache.shale.util.TestBundle"
        final String SUMMARY_MESSAGE = "Invalid resubmit of the same form is bad news Lucy Luo"; 
        final String DETAIL_MESSAGE = "This page has been mark as having durability of a single submit and this contract has been violated by the submission of a dirty page.";
                
        Token token = (Token) facesContext.getApplication().createComponent("org.apache.shale.Token");
        assertNotNull(token);
        
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        // add token to the component tree
        root.getChildren().add(root.getChildCount(), token);
              
        token.setId("messageFacesConfigBundleOverride");
        token.setMessageSummary(null);  // no message property override
        token.setMessageDetail(null);   // no message property override
        
        
        StringBuffer htmlSnippet = encode(token);       
        // check rendered markup
        String id = getAttribute(htmlSnippet, "id");
        assertEquals("id", token.getClientId(facesContext), id);

        String name = getAttribute(htmlSnippet, "name");
        assertEquals("id", token.getClientId(facesContext), name);

        String type = getAttribute(htmlSnippet, "type");
        assertEquals("id", "hidden", type);

        String value = getAttribute(htmlSnippet, "value");
        assertNotNull("value", value);
        
        // simulate form post on dirty page
        Map map = facesContext.getExternalContext().getRequestParameterMap();
        map.put(id, value);
        
        // simulate apply values
        token.decode(facesContext);
        assertEquals("value", value, token.getSubmittedValue());
         
        // simulate validation and invalidates token
        token.validate(facesContext);
        
        checkNoMessages(token);
        
        // simulate double post
        token.validate(facesContext);
        
        // check for the custom message
        checkMessage(SUMMARY_MESSAGE, DETAIL_MESSAGE, token);
        
     }

    /**
     * <p>Token message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.resources.Bundle",
                   TokenProcessorTestCase.class.getClassLoader());

    public void testMessageDefault() throws Exception {
        
        final String SUMMARY_MESSAGE = messages.getMessage("token.summary.invalid");
        final String DETAIL_MESSAGE = messages.getMessage("token.detail.invalid");;
                
        Token token = (Token) facesContext.getApplication().createComponent("org.apache.shale.Token");
        assertNotNull(token);
        
        UIViewRoot root = facesContext.getViewRoot();
        assertNotNull(root);
        
        // add token to the component tree
        root.getChildren().add(root.getChildCount(), token);
              
        token.setId("messageDefault");
        token.setMessageSummary(null);  // no message property override
        
        StringBuffer htmlSnippet = encode(token);       
        // check rendered markup
        String id = getAttribute(htmlSnippet, "id");
        assertEquals("id", token.getClientId(facesContext), id);

        String name = getAttribute(htmlSnippet, "name");
        assertEquals("id", token.getClientId(facesContext), name);

        String type = getAttribute(htmlSnippet, "type");
        assertEquals("id", "hidden", type);

        String value = getAttribute(htmlSnippet, "value");
        assertNotNull("value", value);
        
        // simulate form post on dirty page
        Map map = facesContext.getExternalContext().getRequestParameterMap();
        map.put(id, value);
        
        // simulate apply values
        token.decode(facesContext);
        assertEquals("value", value, token.getSubmittedValue());
         
        // simulate validation and invalidates token
        token.validate(facesContext);
        
        checkNoMessages(token);
        
        // simulate double post
        token.validate(facesContext);
        
        // check for the custom message
        checkMessage(SUMMARY_MESSAGE, DETAIL_MESSAGE, token);
        
     }
    
    
    
    //looks for an html attribute in a markup snippet
    private String getAttribute(StringBuffer htmlSnippet, String attributeName) {
        String sarg = attributeName + "=\"";
        int s = htmlSnippet.indexOf(sarg);
        String value = null;
        if (s > -1) {
           s += sarg.length();
           int e = htmlSnippet.indexOf("\"", s);
           if (e > -1 && e > s) {
              value = htmlSnippet.substring(s, e);
           }
           
        }
        return value;
    }
    
    //looks for an error message added in validation 
    private void checkMessage(String expectedSummary, String expectedDetail, UIInput component) {
        String id = component.getClientId(facesContext);
        Iterator mi = facesContext.getMessages(id);
        boolean hasMessage = false;
        while (mi.hasNext()) {
           FacesMessage message = (FacesMessage) mi.next();    
           String summary = message.getSummary();
           String detail = message.getDetail();
           assertEquals(expectedSummary, summary);
           assertEquals(expectedDetail, detail);
           hasMessage = true;
        }
        
        assertTrue(id + " has messages", hasMessage);   
    }
    
    //checks to make sure there are no error messages after validation
    private void checkNoMessages(UIInput component) {
        String id = component.getClientId(facesContext);
        Iterator mi = facesContext.getMessages(id);
        boolean hasMessage = false;
        while (mi.hasNext()) {
           hasMessage = true;
        }
        
        assertFalse(id + " has messages", hasMessage);   
       
    }

    
    //render and return the results
    private StringBuffer encode(UIComponent component) throws Exception {
        //builds a buffer to write the page to
        StringWriter writer = new StringWriter();
        //create a buffered response writer
        ResponseWriter buffResponsewriter = facesContext.getRenderKit()
                        .createResponseWriter(writer, null, response.getCharacterEncoding());
        //push buffered writer to the faces context
        facesContext.setResponseWriter(buffResponsewriter);
        //start a document
        buffResponsewriter.startDocument();
        
        //render HTML
        component.encodeBegin(facesContext);
        component.encodeChildren(facesContext);
        component.encodeEnd(facesContext);
        
        //end the document
        buffResponsewriter.endDocument();
        
        return writer.getBuffer();
    }

    
    
}
