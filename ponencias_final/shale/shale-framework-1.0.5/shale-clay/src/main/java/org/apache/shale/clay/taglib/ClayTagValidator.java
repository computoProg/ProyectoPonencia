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

/*
 * $Id: ClayTagValidator.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.taglib;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.PageData;
import javax.servlet.jsp.tagext.TagLibraryValidator;
import javax.servlet.jsp.tagext.ValidationMessage;

import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.Parser;
import org.apache.shale.util.Messages;

/**
 * <p>Validates the JSP page for the clay namespace,
 * "http://shale.apache.org/shale/clay-plugin".  This tag
 * validator checks to make sure that there are not any nested
 * tags under the {@link ClayTag} with the exception of the
 * {@link SymbolTag}.</p>
 */
public class ClayTagValidator extends TagLibraryValidator {

    /**
     * <p>URI Namespace for the clay tag lib.</p>
     */
    private static final String CLAY_URI_NAMESPACE = "http://shale.apache.org/clay";

    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages = new Messages("org.apache.shale.clay.Bundle",
            ClayTagValidator.class.getClassLoader());

    /**
     * <p>Loads the <code>page</code> content into a <code>StringBuffer</code>.</p>
     *
     * @param page tag page data
     * @return document in a string buffer
     * @exception IOException error reading page data
     */
    protected StringBuffer loadTemplate(PageData page) throws IOException {


        StringBuffer buff = new StringBuffer();
        InputStream inputStream = page.getInputStream();

            int c = 0;
            done: while (true) {
                c = inputStream.read();
                if (c > -1) {
                    buff.append((char) c);
                } else {
                    break done;
                }

            }

            return buff;

    }

    /**
     * <p>Creates a <code>ValidationMessage</code> for a {@link ClayTag} containing
     * a child of anything other than the {@link SymbolTag}.</p>
     *
     * @param prefix qname
     * @param clayNode markup
     * @param childNode child markup
     *
     * @return message for the JSP compiler
     */
    private ValidationMessage getMessage(String prefix, Node clayNode, Node childNode) {
       Object[] args = {clayNode.getToken().getRawText(),
                        childNode.getToken().getRawText(),
                        prefix};
       String jspid = (String) childNode.getAttributes().get("jsp:id");
       String message = messages.getMessage("invalid.nested.tag", args);
       return new ValidationMessage(jspid, message);
    }


    /**
     * <p>Checks the child nodes of the <code>clayNode</code> verifying that
     * only the symbol node is present.</p>
     *
     * @param prefix qname
     * @param clayNode markup
     * @param messages error messages
     */
    private void checkForInvalidNestedTags(String prefix, Node clayNode, List messages) {
        List children = clayNode.getChildren();
        next: for (int i = 0; i < children.size(); i++) {
            Node child = (Node) children.get(i);
            if ((!child.isComment() && !child.isCdata()) && child.isWellFormed()) {
                if (child.getQname() != null && child.getName() != null) {

                    if (child.getQname().equals("jsp") && child.getName().equals("text")) {
                        continue next;
                    }  else if (!child.getName().equals("symbol") || !prefix.equals(child.getQname())) {
                        messages.add(getMessage(prefix, clayNode, child));
                    }
                }

            }
        }
    }

    /**
     * <p>Recursively walks the parsed document looking for clay component nodes.  The children
     * are checked to make sure the symbol tag is the only valid child tag.</p>
     *
     * @param prefix qname
     * @param node markup
     * @param messages list of errors
     */
    private void validateClayTags(String prefix, Node node, List messages) {
       if ((!node.isComment() && !node.isCdata()) && node.isWellFormed()
           && node.getName() != null && node.getName().equals("clay")
           && node.getQname() != null && node.getQname().equals(prefix)) {

           checkForInvalidNestedTags(prefix, node, messages);
           return;
       }

       List children = node.getChildren();
       for (int i = 0; i < children.size(); i++) {
          Node child = (Node) children.get(i);
          validateClayTags(prefix, child, messages);
       }

    }

    /**
     * <p>Validates the page for a directive with a uri of
     * "<strong>http://shale.apache.org/shale/clay-plugin</strong>".
     *
     * @param prefix namespace
     * @param uri namespace
     * @param page normalized jsp page into XML
     * @return error messages
     */
    public ValidationMessage[] validate(String prefix, String uri, PageData page) {
        List messages = new ArrayList();

        if (uri != null && CLAY_URI_NAMESPACE.equals(uri)) {
            try {
                StringBuffer buff = loadTemplate(page);
                Parser p = new Parser();
                List roots = p.parse(buff);

                for (int i = 0;  i < roots.size(); i++) {
                    Node node = (Node) roots.get(i);
                    validateClayTags(prefix, node, messages);
                }

            } catch (IOException e) {
                messages.add(new ValidationMessage(null, e.getMessage()));
            }
        }

        if (messages.isEmpty()) {
            return null;
        } else {
            ValidationMessage[] validationMessages = new ValidationMessage[messages.size()];
            messages.toArray(validationMessages);
            return validationMessages;
        }

    }

}
