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
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.Parser;

// test processing comment blocks
public class CommentTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public CommentTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(CommentTestCase.class));

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

    //Tests the processing of a comment in a HTML template
    public void testComment() throws Exception{
        
        //done by the startup context listener
        loadConfigFiles(null, null);
        
        
        Clay clay = (Clay) application.createComponent("org.apache.shale.clay.component.Clay");    
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/comment.html");
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
        assertEquals("1 root node", 1, nodes.size());

        Node comment = findComment((Node) nodes.get(0));
        assertNotNull("comment found", comment);       
        assertTrue("is a comment", comment.isComment());
        
        StringBuffer buff = concatCommentText(comment);      
        assertEquals("<!-- <span jsfid=\"outputText\"/> -->", buff.toString());
        
        writer.close();
        
    } 
    
    //Tests the handling of a comment in a nested template.
    public void testNested() throws Exception {
        loadConfigFiles(null, null);
        
        Clay clay = (Clay) application.createComponent("org.apache.shale.clay.component.Clay");    
        clay.setId("test");
        clay.setJsfid("/org/apache/shale/clay/config/layout.html");
        clay.setManagedBeanName("test");
        
        //the clayJsfid in the layout.html is a early EL "#{viewId}"
        request.setAttribute("viewId", "/org/apache/shale/clay/config/comment.html");
        
        //builds a buffer to write the page to
        StringWriter writer = new StringWriter();
        //create a buffered response writer
        ResponseWriter buffResponsewriter = facesContext.getRenderKit()
        .createResponseWriter(writer, null, response.getCharacterEncoding());
        //push buffered writer to the faces context
        facesContext.setResponseWriter(buffResponsewriter);
        //start a document
        buffResponsewriter.startDocument();
        
        clay.encodeBegin(facesContext);
        clay.encodeChildren(facesContext);
        clay.encodeEnd(facesContext);
        
        //end the document
        buffResponsewriter.endDocument();
        
        Parser p = new Parser();
        List nodes = p.parse(writer.getBuffer());
        assertEquals("1 root node", 1, nodes.size());
        
        Node comment = findComment((Node) nodes.get(0));
        assertNotNull("comment found", comment);       
        assertTrue("is a comment", comment.isComment());
        
        StringBuffer buff = concatCommentText(comment);      
        assertEquals("<!-- <span jsfid=\"outputText\"/> -->", buff.toString());
      
        writer.close();
    }
    
    //Recursively traverse the parsed HTML document tree returning 
    //the first comment node.
    private Node findComment(Node node) {
        if (node.isComment())
           return node;
        
        Iterator ci = node.getChildren().iterator();
        while (ci.hasNext()) {
           Node child = (Node) ci.next();
           Node comment = findComment(child);
           if (comment != null)
              return comment;     
        }
        
        return null;
        
    }
    
    //Concatenate all of the parsed tokens in the comment 
    //into a single StringBuffer
    private StringBuffer concatCommentText(Node comment) {
        assertTrue("is comment node", comment.isComment());
        
        StringBuffer buff = new StringBuffer();
        buff.append(comment.getToken().getRawText());
        Iterator ci = comment.getChildren().iterator();
        while (ci.hasNext()) {
            Node child = (Node) ci.next();
            buff.append(child.getToken().getRawText());    
        } 
        
        return buff;
    }
    

}
