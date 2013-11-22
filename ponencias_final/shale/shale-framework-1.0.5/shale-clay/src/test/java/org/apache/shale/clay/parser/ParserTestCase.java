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

package org.apache.shale.clay.parser;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class ParserTestCase extends TestCase {

    /**
     * <p>
     * Tests to see if we can parse a document fragment that has multiple root
     * nodes
     * </p>
     */
    public void testManyRootNodes() {
        Parser p = new Parser();
        StringBuffer doc1 = new StringBuffer();

        doc1.append("<p>").append(
                "<input type=text size=10 maxsize=10 id=username>").append(
                "<input type=text size=10 maxsize=10 id=password>").append(
                "</p>").append("<p>").append("This is a test.  Just a test")
                .append("</p>").append("<p></p>");

        List nodes1 = p.parse(doc1);
        assertTrue("Has 3 root nodes", nodes1.size() == 3);

    }

    /**
     * <p>
     * Test a couple comment block scenarios
     * </p>
     */
    public void testCommentBlocks() {
        Parser p = new Parser();
        StringBuffer doc1 = new StringBuffer();

        doc1.append("<p><!-- self contained comment -->").append(
                "<!--<input type=text size=10 maxsize=10 id=username>").append(
                "<input type=text size=10 maxsize=10 id=password>-->").append(
                "</p>").append("<!--This is a test.  Just a test-->").append(
                "<!--<p>Testing <b>123</b></p> -->");

        List nodes1 = p.parse(doc1);
        assertTrue("Has 3 root nodes", nodes1.size() == 3);

        Node node = (Node) nodes1.get(0);
        assertTrue("first paragraph has 2 node", node.getChildren().size() == 2);

        node = (Node) node.getChildren().get(1);
        assertTrue("second comment block has 3 nodes",
                node.getChildren().size() == 3);

        node = (Node) nodes1.get(2);
        assertTrue("third comment has 7 child nodes", node.getChildren()
                .size() == 7);


        // truncate the buffer
        doc1.setLength(0);
        doc1
                .append("<p>")
                .append(
                        "<!--</p><b><tr></table> we should have a single root with 5 children -->")
                .append("</p>");

        nodes1 = p.parse(doc1);
        assertTrue("root has 1 child nodes", nodes1.size() == 1);

        node = (Node) nodes1.get(0);
        assertTrue("paragraph has 1 child node", node.getChildren().size() == 1);

        node = (Node) node.getChildren().get(0);
        assertTrue("comment has 5 child nodes", node.getChildren().size() == 5);

        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!--A Comment-->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());

        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!-->-->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());

        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!--<-->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());
   
        
        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!-- <a attr=\"> -->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());
        
        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!-- <a attr=\" size=4 class=\"test\"> -->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());


        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!-- <a attr=> -->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());

        
        // truncate the buffer
        doc1.setLength(0);
        doc1.append("<!--- </ -->");
        
        nodes1 = p.parse(doc1);
        assertTrue("one root node", nodes1.size() == 1);
        
        node = (Node) nodes1.get(0);
        assertTrue("node is a valid comment", node.isComment());
        
    }

    /**
     * <p>
     * Tests case insensitivity in parsing the document.
     * </p>
     */
    public void testCaseInsensitivity() {
        Parser p = new Parser();
        StringBuffer doc1 = new StringBuffer();

        doc1.append("<body>").append("<p>").append(
                "<input type=text size=10 maxsize=10 id=username>").append(
                "<input type=text size=10 maxsize=10 id=password>").append(
                "<textarea id=memo>testing 123</textarea>").append("</p>")
                .append("</body>");

        List nodes1 = p.parse(doc1);
        assertTrue("Lower case HTML document", nodes1.size() == 1);

        StringBuffer doc2 = new StringBuffer();

        doc2.append("<BODY>").append("<p>").append(
                "<input TYPE=text size=10 maxSize=10 Id=username>").append(
                "<input typE=text size=10 MAXSIZE=10 id=password>").append(
                "<textarea id=memo>testing 123</TEXTAREA>").append("</P>")
                .append("</body>");

        List nodes2 = p.parse(doc2);
        assertTrue("Mixed case HTML document", nodes2.size() == 1);

        compareTrees(nodes1, nodes2);

    }

    /**
     * <p>
     * Tests the parsing to make sure that self terminated nodes are handled the
     * same as well-formed self terminating nodes.
     * </p>
     */
    public void testSelfTerminating() {
        Parser p = new Parser();
        StringBuffer doc1 = new StringBuffer();

        doc1
                .append("<head>")
                .append("<title>Shale Rocks</title>")
                .append(
                        "<style type=\"text/css\" media=\"all\"><!-- @import \"common.css\"; --></style>")
                .append(
                        "<style type=\"text/css\" media=\"all\"><!-- @import \"content.css\"; --></style>")
                .append(
                        "<script type=\"text/javascript\" src=\"common.js\"></script>")
                .append(
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">")
                .append("<META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\">")
                .append("<META HTTP-EQUIV=\"Expires\" CONTENT=\"-1\">")
                .append(
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">")
                .append("</head>");

        List nodes1 = p.parse(doc1);
        assertTrue("Lazy HTML has 1 node", nodes1.size() == 1);

        StringBuffer doc2 = new StringBuffer();

        doc2
                .append("<head>")
                .append("<title>Shale Rocks</title>")
                .append(
                        "<style type=\"text/css\" media=\"all\"><!-- @import \"common.css\"; --></style>")
                .append(
                        "<style type=\"text/css\" media=\"all\"><!-- @import \"content.css\"; --></style>")
                .append(
                        "<script type=\"text/javascript\" src=\"common.js\"></script>")
                .append(
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\"/>")
                .append("<META HTTP-EQUIV=\"PRAGMA\" CONTENT=\"NO-CACHE\"/>")
                .append("<META HTTP-EQUIV=\"Expires\" CONTENT=\"-1\"/>")
                .append(
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"/>")
                .append("</head>");

        List nodes2 = p.parse(doc2);
        assertTrue("Well-formed HTML has 1 node", nodes2.size() == 1);

        compareTrees(nodes1, nodes2);

    }

    /**
     * <p>
     * Tests to make sure that the parser handles the HTML tags that can have
     * optional ending tags the same that it would a document that was
     * well-formed
     * </p>
     */
    public void testOptionalEnding() {
        Parser p = new Parser();

        StringBuffer doc1 = new StringBuffer();

        // lazy html
        doc1
                .append("<table>")
                .append("<tr><th>Test<th>Test")
                .append("<tr><td>")
                .append("<ol><li>1<li>2<li>3</ol>")
                .append("<tr><td>")
                .append(
                        "<select><option value=1>1<option value=1>2<option value=1>3</select>")
                .append("</table>");

        List nodes1 = p.parse(doc1);
        assertTrue("Lazy HTML has 1 node", nodes1.size() == 1);

        // good html
        StringBuffer doc2 = new StringBuffer();
        doc2
                .append("<table>")
                .append("<tr><th>Test</th><th>Test</th></tr>")
                .append("<tr><td>")
                .append("<ol><li>1</li><li>2</li><li>3</li></ol>")
                .append("</td></tr>")
                .append("<tr><td>")
                .append(
                        "<select><option value=1>1</option><option value=1>2</option><option value=1>3</option></select>")
                .append("</td></tr>").append("</table>");

        List nodes2 = p.parse(doc2);
        assertTrue("Well-formed HTML has 1 node", nodes2.size() == 1);

        compareTrees(nodes1, nodes2);

    }

    /**
     * <p>Check for an ending tag without a begining.</p>
     *
     */
    public void testUnmatchedNonOptionalEnding() {

        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<html>\n")   
           .append("<head/>\n")
           .append("<body>\n")
           .append("</p>\n")
           .append("</body>\n")
           .append("</html>");
        
        try {
           List nodes = p.parse(doc);
           assertTrue("Failed unmatched non-optional ending tag test", false);
        } catch (Exception e) {
           assertTrue("Failed unmatched non-optional ending tag test", e.getMessage().startsWith("Unmatched ending non-optional token"));
        }

    }

    /**
     * <p>Check for a begining tag that was not terminated.</p>
     *
     */
    public void testUnmatchedBegining() {

        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<html>\n")   
           .append("<head/>\n")
           .append("<body>\n")
           .append("<p>\n")
           .append("</body>\n")
           .append("</html>");
        
        try {
           List nodes = p.parse(doc);
           assertTrue("Failed unmatched begining", false);
        } catch (Exception e) {
           assertTrue("Failed unmatched begining test", e.getMessage().startsWith("Unmatched begining token"));
        }

    }

    
    /**
     * Checks to see if nested tables parse correctly
     */
    public void testNestedTables() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc
                .append("<table>")
                .append("<tr><td><table><tr><td></td></tr></table></td>")
                .append(
                        "<td><span jsfid=\"tabs\"><ul id=\"menu\"><li id=\"nav\"><a href=\"#\">Tab 1</a></li><li id=\"nav-sel\"><a href=\"#\">Tab 2</a></li><li id=\"nav\"><a href=\"#\">Tab 3</a></li></ul></span>")
                .append("</td></tr>")
                .append("</table>")
                .append("<div id=\"contents\">")
                .append("<table border=\"0\">")
                .append("<tr><td rowspan=\"3\">")
                .append("<span jsfid=\"contactTable\">")
                .append("<table class=\"contacts\">")
                .append("<tr class=\"contactsHeader\">              ")
                .append("<td>")
                .append("Contacts")
                .append("</td>")
                .append("</tr>    ")
                .append("<tr class=\"contactsRow1\">")
                .append("<td>")
                .append("<a href=\"#\">ABC Company</a>")
                .append("</td>")
                .append("</tr>")
                .append("<tr class=\"contactsRow2\">")
                .append("<td>")
                .append("<a href=\"#\">XYZ Company</a>")
                .append("</td>")
                .append("</tr>")
                .append("</table>")
                .append("</span>")
                .append("</td></tr>")
                .append("<tr><td>")
                .append("<table>")
                .append(
                        "<tr><td><label style=\"color:#99CC66\" jsfid=\"contactNameLabel\">Name:</label></td><td><input jsfid=\"contactName\" type=text size=40 maxlength=\"50\"/></td><td><span style=\"color:red\" jsfid=\"contactNameMessage\">Mock Error Name</span></td></tr></table></td></tr>")
                .append("</table>").append("</div>");

        List nodes = p.parse(doc);
        assertTrue("Well-formed nested table has 1 root node",
                nodes.size() == 2);

    }

    /**
     * <p>
     * Aserts that two trees of parsed HTML have the same number children and
     * the same attributes. Verifies that the structure is the same
     * </p>
     */
    protected void compareTrees(List tree1, List tree2) {

        boolean isSame = (tree1 == null && tree2 == null)
                || (tree1.size() == tree2.size());

        assertTrue("Trees have same # children", isSame);
        if (tree1 != null && tree2 != null) {
            for (int i = 0; i < tree1.size(); i++) {
                Node node1 = (Node) tree1.get(i);
                Node node2 = (Node) tree2.get(i);

                isSame = false;
                if (node1 != null && node2 != null) {
                    if (node1.getName() == null && node2.getName() == null)
                        isSame = true;
                    else
                        isSame = (node1.getName() != null
                                && node2.getName() != null && node1.getName()
                                .equalsIgnoreCase(node2.getName()));

                    assertTrue("Nodes names are equal", isSame);

                    isSame = (node1.getAttributes().size() == node2
                            .getAttributes().size());
                    assertTrue("Nodes have same # attributes", isSame);
                    Iterator ki = node1.getAttributes().keySet().iterator();
                    while (ki.hasNext()) {
                        String key = (String) ki.next();
                        String value1 = (String) node1.getAttributes().get(key);
                        String value2 = (String) node2.getAttributes().get(key);
                        isSame = value1.equalsIgnoreCase(value2);
                        assertTrue("Nodes have same attribute value", isSame);

                    }
                    compareTrees(node1.getChildren(), node2.getChildren());

                }
            }

        }
    }
    
    /**
     * <p>Test attribute parsing.</p>
     */
    public void testAttributes() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<input type=text size=\"10\" name=date>");
   
        List nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);

        Node node = (Node) nodes.get(0);

        String value = (String) node.getAttributes().get("type");
        assertEquals("Attribute type", "text", value);
        
        value = (String) node.getAttributes().get("size");
        assertEquals("Attribute size", "10", value);
        
        value = (String) node.getAttributes().get("name");
        assertEquals("Attribute name", "date", value);

        //test linespan with bogus whitespace
        doc.setLength(0);
        doc.append("<input \n type=text \t\r \nsize=\"10\" \nname=date\r>");
   
        nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);

        node = (Node) nodes.get(0);

        value = (String) node.getAttributes().get("type");
        assertEquals("Attribute type", "text", value);
        
        value = (String) node.getAttributes().get("size");
        assertEquals("Attribute size", "10", value);
        
        value = (String) node.getAttributes().get("name");
        assertEquals("Attribute name", "date", value);

       
        //test bogus attribute with missing delimiter
        doc.setLength(0);
        doc.append("<input type=\"text \" size=\"10 name=date>");

        nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);
        
        node = (Node) nodes.get(0);

        value = (String) node.getAttributes().get("type");
        assertEquals("Attribute type ", "text ", value);

        value = (String) node.getAttributes().get("size");
        assertEquals("Attribute size", "10", value);

        value = (String) node.getAttributes().get("name");
        assertEquals("Attribute name", "date", value);

      
        //test bogus attribute with missing delimiter
        doc.setLength(0);
        doc.append("<input type=radio\" name=date checked>");

        nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);
        
        node = (Node) nodes.get(0);

        value = (String) node.getAttributes().get("type");
        assertEquals("Attribute type", "radio", value);

        value = (String) node.getAttributes().get("name");
        assertEquals("Attribute name", "date", value);
        
        assertTrue("Attribute checked exists", node.getAttributes().containsKey("checked"));
        value = (String) node.getAttributes().get("checked");
        assertNull("Attribute checked", value);


        doc.setLength(0);
        doc.append("<option selected value=\"\"/>");

        nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);
        
        node = (Node) nodes.get(0);

        assertTrue("Attribute selected exists", node.getAttributes().containsKey("selected"));
        value = (String) node.getAttributes().get("selected");
        assertNull("Attribute selected", value);
        
        assertTrue("Attribute selected exists", node.getAttributes().containsKey("value"));
        value = (String) node.getAttributes().get("value");
        assertNull("Attribute value", value);

        //test tabbed attributes
        doc.setLength(0);
        doc.append("<a\thref=\"http://www.acme.com\">\nAcme Company</a>");
   
        nodes = p.parse(doc);
        assertTrue("1 root node", nodes.size() == 1);

        node = (Node) nodes.get(0);
        assertTrue("Node is well-formed", node.isWellFormed());

        
        value = (String) node.getAttributes().get("href");
        assertEquals("Attribute href",  "http://www.acme.com", value);   
            
    }
    
    public void testTable() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();

        doc.append("<table>")
           .append("<tr><th></th><th></tr>")
           .append("<tr><td></td><td></tr>")
           .append("</table>");
        
        List nodes = p.parse(doc);
        assertEquals("table, 1 root node", nodes.size(), 1);   
        
        Node table = (Node) nodes.get(0);
        assertNotNull("table", table);
        
        List trs = table.getChildren();
        assertEquals("trs, 2 root nodes", 2, trs.size());   
        
        Node tr = (Node) trs.get(0);
        assertNotNull("tr row 1", tr);
        
        List ths = tr.getChildren();
        assertEquals("ths, 2 root nodes", 2, ths.size());           
        
        tr = (Node) trs.get(1);
        assertNotNull("tr row 2", tr);
        
        List tds = tr.getChildren();
        assertEquals("tds, 2 root nodes", 2, tds.size());           
        
        
    }
    
    public void testJSPTag() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        
        doc.append("<%@ taglib uri=\"/tags/struts-html\" prefix=\"html\" %>")
           .append("<%@ taglib uri=\"/tags/struts-bean\" prefix=\"bean\" %>")
           .append("<%@ page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" session=\"true\" %>")
           .append("<%@ page errorPage=\"../error.jsp\" %>")
           .append("<table>")
           .append("<tr><td>")
           .append("<bean:message key=\"msg.username\"/><td><html:text property=\"userName\" size=\"10\" maxlength=\"10\"/>")
           .append("<tr><td>")
           .append("<bean:message key=\"msg.password\"/><td><html:text property=\"password\" size=\"10\" maxlength=10/><br>")
           .append("</table>");
        
        List nodes = p.parse(doc);
        assertTrue("Well-formed JSP, 5 root nodes", nodes.size() == 5);
        
        Node root = (Node) nodes.get(4);
        assertNotNull("table node not null", root);

        Node tr = (Node) root.getChildren().get(0);
        assertNotNull("tr node not null", tr);

        Node td = (Node) tr.getChildren().get(0);
        assertNotNull("td node not null", td);

        Node message = (Node) td.getChildren().get(0);
        assertNotNull("bean:message node not null", message);
        
        assertEquals("node.name", "message", message.getName());
        assertEquals("node.qname", "bean", message.getQname());
        assertEquals("node.attributes.key", "msg.username", message.getAttributes().get("key"));
       
        td = (Node) tr.getChildren().get(1);
        assertNotNull("td node not null", td);

        Node text = (Node) td.getChildren().get(0);
        assertNotNull("bean:message node not null", text);
        
        assertEquals("node.name", "text", text.getName());
        assertEquals("node.qname", "html", text.getQname());
        assertEquals("node.attributes.property", "userName", text.getAttributes().get("property"));
        assertEquals("node.attributes.size", "10", text.getAttributes().get("size"));
        assertEquals("node.attributes.maxlength", "10", text.getAttributes().get("maxlength"));  

    }
    
    //parse document ending in text
    public void testPlainText() {
   
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        
        doc.append("<br/>test");

        List nodes = p.parse(doc);
        assertTrue("Plain text, 2 node", nodes.size() == 2);

        Node root = (Node) nodes.get(1);
        assertNotNull("last node", root);

        assertEquals("node raw text", "test", root.getToken().getRawText());


        doc.setLength(0);
        doc.append("This is a test.");

        nodes = p.parse(doc);
        assertTrue("Plain text, 1 node", nodes.size() == 1);

        root = (Node) nodes.get(0);
        assertNotNull("first node", root);

        assertEquals("node raw text", doc.toString(), root.getToken().getRawText());

        
    }
    
    
    /**
     * <p>Test parsing a DOCTYPE Tag.</p>
     */
    public void testDoctype() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" ")
           .append("\"http://www.w3.org/TR/html4/strict.dtd\">")
           .append("<HTML>")
           .append("<HEAD>")
           .append("<TITLE>My first HTML document</TITLE>")
           .append("</HEAD>")
           .append("<BODY>")
           .append("<P>Hello world!</p>")
           .append("</BODY>")
           .append("</HTML>");


        List nodes = p.parse(doc);
        assertTrue("Well-formed DOCTYPE, 2 root nodes",
                nodes.size() == 2);

    }
    
    
    public void testNameSpace() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
           .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" ") 
           .append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
           .append("<html xmlns=\"http://www.w3.org/1999/xhtml\" ")
           .append("xmlns:clay=\"http://shale.apache.org/xml/clay\" ")
           .append("xml:lang=\"en\" ")  
           .append("lang=\"en\">")
           .append("<head>")
           .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>")
           .append("<title>Test</title>")
           .append("</head>")
           .append("<body>")
           .append("<clay:component jsfid=\"outputText\" value=\"testing\"/>")
           .append("<div xmlns:clay=\"http://shale.apache.org/dtds/shale-clay\">")
           .append("<clay:component jsfid=\"outputText\" value=\"testing\"/>")  
           .append("</div>")
           .append("</body>")
           .append("</html>");
        
        
        List roots = p.parse(doc);
        assertEquals(3, roots.size());
        Node root = (Node) roots.get(2);
        assertNotNull(root);
        
        List nodes = root.getNodesByName("component");
        assertEquals(2, nodes.size());
        
        Node clayComponent = (Node) nodes.get(0);
        assertEquals("prefix", "clay", clayComponent.getQname());
        
        // clay namespace
        String uri = clayComponent.getNamespaceURI(clayComponent.getQname());
        assertEquals("uri", "http://shale.apache.org/xml/clay", uri);
        
        // default namespace
        uri = clayComponent.getNamespaceURI(null);
        assertEquals("uri", "http://www.w3.org/1999/xhtml", uri);

        
        clayComponent = (Node) nodes.get(1);
        assertEquals("prefix", "clay", clayComponent.getQname());
        
        // clay namespace
        uri = clayComponent.getNamespaceURI(clayComponent.getQname());
        assertEquals("uri", "http://shale.apache.org/dtds/shale-clay", uri);
        
        // default namespace
        uri = clayComponent.getNamespaceURI(null);
        assertEquals("uri", "http://www.w3.org/1999/xhtml", uri);
        
    }
    
    
    public void testParseCDATA() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<jsp:text\njsp:id=\"9\"\n><![CDATA[\n<html>\n]]>\n</jsp:text>");
        doc.append("<jsp:text\njsp:id=\"10\"\n><![CDATA[\n<head>\n]]>\n</jsp:text>");
        doc.append("<jsp:text\njsp:id=\"11\"\n><![CDATA[\n</title>\n]]>\n</jsp:text>");
        doc.append("<jsp:text\njsp:id=\"12\"\n><![CDATA[\n<!-- </title> -->\n]]>\n</jsp:text>");
        
        List roots = p.parse(doc);
        assertEquals(4, roots.size());
        
        for (int i = 0; i < roots.size(); i++) {
           Node node = (Node) roots.get(i);  
           assertEquals("jsp", node.getQname());
           assertEquals("text", node.getName());
           
           String id = (String) node.getAttributes().get("jsp:id");
           assertEquals(String.valueOf(i + 9), id);
           
           
           
        }
                
    }
    
    
    public void testRemoveBlock() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<!-- ### clay:remove ### -->")
           .append("<html>")
           .append("<div class=\"content\">")
           .append("<!-- ### /clay:remove ### -->")
           .append("<!--")
           .append("    this is the body content to be placed")
           .append("    in the basic layout HTML template defined elsewhere")
           .append("-->")
           .append("<form>")
           .append("<input type=\"text\" value=\"#{contentBean.inputText}\"/>")
           .append("<input type=\"submit\" action=\"#{contentBean.doSubmit}\"/>")
           .append("</form>")
           .append("<!-- ### clay:remove ### -->")
           .append("</div>")
           .append("<!--   this is the basic layout to be removed when converting to an include -->")
           .append("</html>")
           .append("<!-- ### /clay:remove ### -->");     
        
        List roots = p.parse(doc);
        assertNotNull(roots);
        
        assertEquals("root nodes", 2, roots.size());
        
        Node comment = (Node) roots.get(0);
        
        assertEquals("isComment", true, comment.isComment());
        
        Node form = (Node) roots.get(1);
        
        assertEquals("form well-formed", true, form.isWellFormed());
        assertEquals("form name", "form", form.getName());
        assertEquals("form children", 2, form.getChildren().size());
        
        
    }

    
    public void testRemoveCharset() {
        Parser p = new Parser();
        StringBuffer doc = new StringBuffer();
        doc.append("<!-- ### clay:page charset=\"UTF-8\" ### -->")
           .append("<html></html>");

        List roots = p.parse(doc);
        assertNotNull(roots);
        
        //charset comment should be removed
        assertEquals("root nodes", 1, roots.size());

        Node root = (Node) roots.get(0); 
        assertEquals("root node", "html", root.getName());

    }
    

}
