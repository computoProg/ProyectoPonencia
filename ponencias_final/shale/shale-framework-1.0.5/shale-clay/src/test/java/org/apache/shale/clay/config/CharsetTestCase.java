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
package org.apache.shale.clay.config;

import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.Parser;

// test logic to handle encoding types in document templates
public class CharsetTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public CharsetTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(CharsetTestCase.class));

    }

    //family, renderer type, Myfaces Impl, Sun RI Impl
    protected String[][] RENDERERS = {
            {"javax.faces.Output", "javax.faces.Text", 
             "org.apache.myfaces.renderkit.html.HtmlTextRenderer", 
             "com.sun.faces.renderkit.html_basic.TextRenderer",},        
            {"javax.faces.Input", "javax.faces.Text", 
             "org.apache.myfaces.renderkit.html.HtmlTextRenderer", 
             "com.sun.faces.renderkit.html_basic.TextRenderer"},
            {"javax.faces.Output", "javax.faces.Label", 
             "org.apache.myfaces.renderkit.html.HtmlLabelRenderer",
             "com.sun.faces.renderkit.html_basic.LabelRenderer"}
            
    };
    
    protected void setUp() throws Exception {
        super.setUp();
                        
        //loads the RI or myfaces renderers from the RENDERERS static array.
        for (int i = 0; i < RENDERERS.length; i++) {
            
            Renderer renderer = null;
            renderer: for (int j = 2; j < 4; j++) {
                try {
                    Class clazz = Class.forName(RENDERERS[i][j]);
                    if (clazz != null) {
                        renderer = (Renderer) clazz.newInstance();
                        if (renderer != null) {
                            //System.out.println(RENDERERS[i][j]);
                            break renderer;
                        }
                    }
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }
            }
            
            if (renderer != null) {
                facesContext.getRenderKit().addRenderer(RENDERERS[i][0], RENDERERS[i][1],
                        renderer);
            }
        }        
        
    }

    public void testFindCharacterEncoding() throws Exception {
        //done by the startup context listener
        loadConfigFiles(null, null);
          
        ClayTemplateParser parser = new ClayTemplateParser();
        parser.setConfig(htmlTemplateConfigBean);

        
        URL noOverrideURL = facesContext.getExternalContext().getResource("/org/apache/shale/clay/config/comment.html");
        String enc = parser.getCharacterEncoding(noOverrideURL);
        assertEquals("VM default", System.getProperty("file.encoding"), enc);
        
        servletContext.addInitParameter(Globals.CLAY_HTML_CHARSET, "UTF-16");
        enc = parser.getCharacterEncoding(noOverrideURL);
        assertEquals("web.xml override", "UTF-16", enc);

        servletContext.addInitParameter(Globals.CLAY_HTML_CHARSET, "BOGUS");
        enc = parser.getCharacterEncoding(noOverrideURL);
        assertEquals("web.xml bogus override", System.getProperty("file.encoding"), enc);
            
        URL overrideURL = facesContext.getExternalContext().getResource("/org/apache/shale/clay/config/some-utf-8.html");
        enc = parser.getCharacterEncoding(overrideURL);
        assertEquals("template override", "UTF-8", enc);

    }
    
    
    public void testUtf8() throws Exception{
        
        //done by the startup context listener
        loadConfigFiles(null, null);
        response.setCharacterEncoding("UTF-8");
        
        
        Clay clay = (Clay) application.createComponent("org.apache.shale.clay.component.Clay");    
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/some-utf-8.html");
        clay.setManagedBeanName("test");
                
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
        clay.encodeBegin(facesContext);
        clay.encodeChildren(facesContext);
        clay.encodeEnd(facesContext);
        
        //end the document
        buffResponsewriter.endDocument();
        
        Parser p = new Parser();
        List nodes = p.parse(writer.getBuffer());
        assertEquals("2 root node", 2, nodes.size());

        Node paragraph = (Node) nodes.get(1);
        assertNotNull(paragraph);
        
        // contact rendered output into a buffer
        StringBuffer snippet = concatText(paragraph);
        assertNotNull(snippet);
        
        int i = snippet.indexOf("\u0119");
        assertTrue("polish language char \u0119", i > -1);        
                
        i = snippet.indexOf("\u00f3");
        assertTrue("polish language char \u00f3", i > -1);        
                
        i = snippet.indexOf("\u0107");
        assertTrue("polish language char \u0107", i > -1);        
        
        i = snippet.indexOf("\u017a");
        assertTrue("polish language char \u017a", i > -1);        

        i = snippet.indexOf("\u017c");
        assertTrue("polish language char \u017c", i > -1);        

        i = snippet.indexOf("\u015b");
        assertTrue("polish language char \u015b", i > -1);        

        i = snippet.indexOf("\u0105");
        assertTrue("polish language char \u0105", i > -1);        

        i = snippet.indexOf("\u221e");
        assertTrue("infinity char \u221e", i > -1);        
        
        i = snippet.indexOf("\u03c6");
        assertTrue("small greek letter Phi char \u03c6", i > -1);        

        i = snippet.indexOf("\u03c6");
        assertTrue("integral sign char \u03c6", i > -1);        

        writer.close();
        
    } 
    
    private StringBuffer concatText(Node node) {
        
        StringBuffer buff = new StringBuffer();
        buff.append(node.getToken().getRawText());
        Iterator ci = node.getChildren().iterator();
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            buff.append(child.getToken().getRawText());    
        } 
        
        return buff;
    }
    
           

}
