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
 * $Id: Parser.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;

/**
 * <p>Parses the document into a tree of nodes using the
 * {@link NodeTokenizer}. Nodes are defined by a token or
 * offset range in the document, {@link Token}.  Attributes in beginning
 * nodes are also parsed into token offsets by the {@link AttributeTokenizer}.
 * <br><br>
 * A document tree is built representing nodes in the target document.  The
 * document can be a HTML fragment that is not well-formed or an XML
 * fragment of a XHTML document.
 * </p>
 *
 */
public class Parser {

    /**
     * <p>Common logging utility instance.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(Parser.class);
    }

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", NodeTokenizer.class
            .getClassLoader());


    /**
     * <p>This inner class is a subclass of a <code>TreeMap</code>.
     * This wrapper handles the attribute key and value parts represented
     * as a {@link Token} offsets.  The value part of the attribute will be
     * represented by a offset until is accessed using the <code>get</code>
     * method to avoid creating a bunch of <code>String</code> instances.
     * </p>
     *
     */
    private class Attributes extends TreeMap implements Map {

        /**
         * <p>Unique serial id.</p>
         */
        private static final long serialVersionUID = 3906654111096190000L;

        /**
         * <p> Returns the value of the attribute using a offset
         * range within the parsed document.  The parameter <code>key</code>
         * value is converted into a case neutral value.
         * </p>
         *
         * @param key attribute name
         * @return attribute value
         */
        public Object get(Object key) {
            String tmp = (String) key;
            if (tmp != null) {
                tmp = tmp.toLowerCase();
            }

            Token e = (Token) super.get(tmp);
            return (e != null) ? e.getRawText() : null;
        }

        /**
         *  <p>This method is overridden and not implemented.  The
         *  <code>add</code> method should be used for this specific
         *  implementation.  The <code>value</code> attribute's internal
         *  type realizes {@link Token}, but the <code>get</code> method
         *  will return a <code>String</code> object.
         *  </p>
         *
         *   @deprecated
         *   @param key not supported
         *   @param value not supported
         *   @return not supported
         */
        public Object put(Object key, Object value) {
            // NA
            return null;
        }

        /**
         * <p>Adds a attribute to the collection.  The attribute is
         * represented by two {@link Token} object for its key and
         * value parts.
         * </p>
         *
         * @param e token to be added
         *
         */
        public void add(Map.Entry e) {

            String key = ((Token) e.getKey()).getRawText();
            if (key != null) {
                key = key.toLowerCase();
            }

            super.put(key, e.getValue());
        }

        /**
         * <p>This method is overridden to convert the key into a neutral
         * case so that the <code>Map</code> access method will be case
         * insensitive.</p>
         *
         * @param key attribute name
         * @return <code>true</code> if attribute exists
         */
        public boolean containsKey(Object key) {
            String tmp = (String) key;
            if (tmp != null) {
                tmp = tmp.toLowerCase();
            }

            return super.containsKey(tmp);
        }



    }

    /**
     * <p>This array of HTML tags can have optional ending tags.</p>
     */
    private static final String[] OPTIONAL_ENDING_TAG  = {"TR", "TH", "TD", "LI", "DT", "DD", "LH", "OPTION"};
    /**
     * <p>This array of parent tags is cross referenced by the
     * <code>OPTIONAL_ENDING_TAG</code> array.</p>
     */
    private static final String[][] TAG_PARENTS = {{"TABLE", "TBODY"}, {"TR"}, {"TR"},
        {"OL", "UL"}, {"DL"}, {"DL"}, {"DL"}, {"SELECT"}};

    /**
     * <p>
     * Determines if a HTML nodeName is a type of tag that can optionally have a
     * ending tag.
     * </p>
     *
     * @param nodeName the name of the html node
     * @return <code>true</code> if the nodeName is in the
     *   <code>OPTIONAL-ENDING_TAG</code> array; otherwise, <code>false</code> is returned
     */
    protected boolean isOptionalEndingTag(String nodeName) {
        if (nodeName != null) {
            for (int i = 0; i < OPTIONAL_ENDING_TAG.length; i++) {
                if (OPTIONAL_ENDING_TAG[i].equalsIgnoreCase(nodeName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * <p>
     * Checks to see if a optional ending tag has a valid parent. This is use to
     * detect a implicit ending tag
     * </p>
     *
     * @param nodeName of the optional ending tag
     * @param parentNodeName name of the parent
     * @return <code>true</code> if the parentNodeName is a valid parent for
     *   the nodeName; otherwise, a <code>false</code> value is returned
     */
    protected boolean isValidOptionalEndingTagParent(String nodeName,
            String parentNodeName) {
        if (nodeName != null && parentNodeName != null) {
            for (int i = 0; i < OPTIONAL_ENDING_TAG.length; i++) {
                if (OPTIONAL_ENDING_TAG[i].equalsIgnoreCase(nodeName)) {
                    for (int j = 0; j < TAG_PARENTS[i].length; j++) {
                        if (TAG_PARENTS[i][j].equalsIgnoreCase(parentNodeName)) {
                            return true;
                        }
                    }
                    break;
                }
            }
        }
        return false;
    }

    /**
     * @param current top of the stack
     * @param node ending node
     * @return begining node
     */
    protected Node findBeginingNode(Node current, Node node) {

        pop: while (true) {
            if (current == null) {
                break pop;
            }

            if (isNodeNameEqual(current, node)) {

                // isWellFormed indicates a beginning tag and ending tag
                // was found
                current.setWellFormed(true);

                // nodes are equal, make the parent of the
                // begin tag the current node
                current = current.getParent();

                break pop;
            }

            if (isOptionalEndingTag(current.getName())) {
                current.setWellFormed(true);
            }

            if (current.getParent() == null) {
                throw new RuntimeException(
                        messages.getMessage("parser.unmatched.endtoken",
                                new Object[] {node.getToken(), node.getToken().getRawText()}));
            }

            current = current.getParent();

        }

    return current;

    }

    /**
     * <p>Starting remove block delimiter.  It must be a self contained commment.<p>
     */
    private static final String BEGIN_REMOVE_TOKEN = "<!-- ### clay:remove ### -->";

    /**
     * <p>Ending remove block delimiter.</p>
     */
    private static final String END_REMOVE_TOKEN = "<!-- ### /clay:remove ### -->";

    /**
     * <p>The start of the comment token used to override the template
     * encoding type.</p>
     */
    public static final String START_CHARSET_TOKEN = "<!-- ### clay:page ";

    /**
     * <p>The end of the comment token used to override the template
     * encoding type.</p>
     */
    public static final String END_CHARSET_TOKEN = "/### -->";

    /**
     * <p>
     * Parse a document fragment into graphs of {@link Node}. The resulting
     * type is a list because the fragment might not be well-formed.
     * </p>
     *
     * @param document input source
     * @return collection of {@link Node}
     */
    public List parse(StringBuffer document) {

        boolean isWithinRemoveBlock = false;
        Node root = new Node(null);
        Node current = root;
        current.setName("namingContainer");
        root.setWellFormed(true);

        NodeTokenizer t = new NodeTokenizer(document);
        Iterator i = t.iterator();
        next: while (i.hasNext()) {
            Token token = (Token) i.next();
            Node node = buildNode(token);

            // self contained comment matching the begin/end remove token delimiter
            // skip all tokens within the remove block
            if (node.isComment() && node.isStart() && node.isEnd()) {

                //ignore start page charset token if not in a remove comment block
                if (!isWithinRemoveBlock && node.getToken().getRawText().startsWith(START_CHARSET_TOKEN)) {
                   continue next;
                }

                if (isWithinRemoveBlock && node.getToken().getRawText().equals(END_REMOVE_TOKEN)) {

                   isWithinRemoveBlock = false;
                   continue next;

                } else if (node.getToken().getRawText().equals(BEGIN_REMOVE_TOKEN)) {

                  isWithinRemoveBlock = true;
                  continue next;

               } else if (isWithinRemoveBlock) {
                  continue next;
               }
            } else if (isWithinRemoveBlock) {
                continue next;
            }


            //play forward on comments making all nodes child nodes until a
            //ending comment is hit
            if ((node.isComment() || node.isCdata()) && node.isStart()) {

                // capture the type of block since you can have comments in a cdata block
                boolean isCommentBlock = node.isComment();
                boolean isCdataBlock = node.isCdata();

                //not self contained comment
                if (!node.isEnd()) {

                    trash: while (i.hasNext()) {
                        token = (Token) i.next();
                        Node bodyNode = buildNode(token);
                        //if a ending node and the block matches
                        if (((bodyNode.isComment() && isCommentBlock)
                                || (bodyNode.isCdata() && isCdataBlock)) && bodyNode.isEnd()) {
                            node.addChild(bodyNode);
                            node.setEnd(true);
                            node.setWellFormed(true);
                            break trash;
                        } else {
                            //force all nodes to be comment or cdata within a block
                            node.setComment(isCommentBlock);
                            node.setCdata(isCdataBlock);
                            node.setWellFormed(true);
                            node.addChild(bodyNode);
                        }
                    } // end while

                }

                current.addChild(node);
                continue next;

            }


            if (!node.isStart() && node.isEnd()) {

                current = findBeginingNode(current, node);

            } else if (node.isStart() && !node.isEnd()) {

                // this is to handle an option tag without an ending option tag.
                // they just liked to write conditional code back then, what the
                // heck!
                // <select>
                // <option value=why>old school html
                // <option value=whyo>crazy man
                // </select>
                //


                if (isOptionalEndingTag(current.getName())
                        && current.isStart() && !current.isEnd()
                        && current.getParent() != null
                        && isValidOptionalEndingTagParent(node.getName(), current.getParent().getName())) {

                    current.setWellFormed(true);
                    current.getParent().addChild(node);
                    current = node;

                } else {
                    // the current node is a optional and the new node is it's parent
                    // simulate having ending nodes

                    if (isOptionalEndingTag(node.getName())
                            && isValidOptionalEndingTagParent(current.getName(), node.getName())) {

                        current = this.findBeginingNode(current, node);
                        current.addChild(node);
                        current = node;

                    } else {

                        // adding a new node to the current making it current
                        current.addChild(node);
                        current = node;
                    }
                }
            } else {
                if (current != null) {
                    current.addChild(node);
                } else {
                    current = node;
                }
            }

        }

        t = null;
        i = null;

        simpleWellFormedCheck(root);

        return root.getChildren();

    }

    /**
     * <p>A simple check to make sure that all nodes have been terminated including
     * tags with optional ending tags.</p>
     *
     * @param node root markup
     */
    private void simpleWellFormedCheck(Node node) {
        if (node.getName() != null && !node.isWellFormed()) {
            throw new RuntimeException(
                    messages.getMessage("parser.unmatched.begintoken",
                            new Object[] {node.getToken(), node.getToken().getRawText()}));
        }

        if (!node.isComment() && !node.isCdata()) {
            Iterator ci = node.getChildren().iterator();
            while (ci.hasNext()) {
                simpleWellFormedCheck((Node) (ci.next()));
            }
        }
    }

    /**
     * <p>Compares two {@link Node} instances by <code>name</code>.
     * This method is used to match a beginning tag with an ending tag
     * while building the document stack.  Returns <code>true</code> if
     * the node <code>name</code> properties are the same.
     * </p>
     *
     * @param node1 first node
     * @param node2 secnod node
     * @return <code>true</code> if they are the same
     *
     */
    protected boolean isNodeNameEqual(Node node1, Node node2) {
        boolean f = false;

        if (node1 != null && node2 != null) {
            if (node1.getName() != null && node2.getName() != null) {
                if (node1.getName().equalsIgnoreCase(node2.getName())) {
                    if (node1.getQname() == null && node2.getQname() == null) {
                        f = true;
                    } else if (node1.getQname() != null
                            && node2.getQname() != null
                            && node1.getQname().equalsIgnoreCase(node2.getQname())) {
                        f = true;
                    }
                }
            }
        }

        if (log.isDebugEnabled()) {
            StringBuffer msg = new StringBuffer();
            msg.append("matching nodes (").append(node1.getName()).append(
                    (f ? "==" : "!=")).append(node2.getName()).append(")");
            log.debug(msg.toString());
        }
        return f;
    }

    /**
     * <p>Table of self terminating Html tags.</p>
     */
    private static final String[] SELF_TERMINATING = {"META", "LINK", "HR",
        "BASEFONT", "IMG", "PARAM", "BR", "AREA", "INPUT", "ISINDEX",
    "BASE"};

    /**
     * <p>
     * Checks to see if the nodeName is within the <code>SELF_TERMINATING</code>
     * table of values.
     * </p>
     *
     * @param nodeName to check for self termination
     * @return <code>true</code> if is self terminating otherwise
     *   <code>false</code>
     */
    protected boolean isSelfTerminating(String nodeName) {

        if (nodeName != null) {
            for (int i = 0; i < SELF_TERMINATING.length; i++) {
                if (SELF_TERMINATING[i].equalsIgnoreCase(nodeName)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * <p>This is a factory method that builds a {@link Node} from a
     * {@link Token}.
     * </p>
     *
     * @param token node offset in the document
     * @return node that describes the structure of the token
     */
    protected Node buildNode(Token token) {

        Node node = new Node(token);

        discoverNodeShape(node);
        discoverNodeName(node);
        discoverNodeAttributes(node);
        discoverNodeOverrides(node);

        return node;
    }


    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate an ending {@link Token}.</p>
     */
    private static final Rule[] END_TAG_RULES = {new Rule('<', true, 0, true),
        new Rule('/', true, 1, true),
        new Rule('>', false, -1, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate self terminating {@link Token}.</p>
     */
    private static final Rule[] SELF_TERM_TAG_RULES = {new Rule('<', true, 0, true),
        new Rule('/', false, -2, true),
        new Rule('>', false, -1, true)};
    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate self contained comment {@link Token}.</p>
     */
    private static final Rule[] SELF_CONTAINED_COMMENT_RULES = {new Rule('<', true, 0, true),
        new Rule('!', true, 1, true),
        new Rule('-', true, 2, true),
        new Rule('-', true, 3, true),
        new Rule('>', false, -1, true),
        new Rule('-', false, -2, true),
        new Rule('-', false, -3, true)};


    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate self contained CDATA {@link Token}.</p>
     */
    private static final Rule[] SELF_CONTAINED_CDATA_RULES = {new Rule('<', true, 0, true),
        new Rule('!', true, 1, true),
        new Rule('[', true, 2, true),
        new Rule('C', true, 3, true),
        new Rule('D', true, 4, true),
        new Rule('A', true, 5, true),
        new Rule('T', true, 6, true),
        new Rule('A', true, 7, true),
        new Rule('[', true, 8, true),
        new Rule('>', false, -1, true),
        new Rule(']', false, -2, true),
        new Rule(']', false, -3, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate a begin CDATA {@link Token}.</p>
     */
    public static final Rule[] BEGIN_CDATA_RULES = {new Rule('<', true, 0, true),
        new Rule('!', true, 1, true),
        new Rule('[', true, 2, true),
        new Rule('C', true, 3, true),
        new Rule('D', true, 4, true),
        new Rule('A', true, 5, true),
        new Rule('T', true, 6, true),
        new Rule('A', true, 7, true),
        new Rule('[', true, 8, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate an end CDATA {@link Token}.</p>
     */
    public static final Rule[] END_CDATA_RULES = {new Rule('>', false, -1, true),
        new Rule(']', false, -2, true),
        new Rule(']', false, -3, true)};


    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate a begin comment {@link Token}.</p>
     */
    public static final Rule[] BEGIN_COMMENT_TAG_RULES = {new Rule('<', true, 0, true),
        new Rule('!', true, 1, true),
        new Rule('-', true, 2, true),
        new Rule('-', true, 3, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate an end comment {@link Token}.</p>
     */
    public static final Rule[] END_COMMENT_TAG_RULES = {new Rule('>', false, -1, true),
        new Rule('-', false, -2, true),
        new Rule('-', false, -3, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate document type {@link Token}.</p>
     */
    public static final Rule[] DOCTYPE_TAG_RULES = {new Rule('<', true, 0, true),
        new Rule('!', true, 1, true),
        new Rule('>', false, -1, true)};

    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate a begining {@link Token}.</p>
     */
    public static final Rule[] BEGIN_TAG_RULES = {new Rule('<', true, 0, true),
        new Rule('-', true, 1, false),
        new Rule('/', true, 1, false),
        new Rule('?', true, 1, false),
        new Rule('%', true, 1, false),
        new Rule('>', false, -1, true)};


    /**
     * <p>Declare an array of {@link Parser.Rule}s that validate JSP block {@link Token}.</p>
     */
    private static final Rule[] JSP_RULES = {new Rule('<', true, 0, true),
        new Rule('%', true, 1, true),
        new Rule('>', false, -1, true),
        new Rule('%', false, -2, true)};


    /**
     * <p>Declare an array of {@link Parser.Shape}s further defined by {@link Parser.Rule}s
     *  that are used to determine the type of {@link Node} the {@link Token} defines.</p>
     */
    private static final Shape[] NODE_SHAPES = {
        new Shape(true, true, false, true, SELF_CONTAINED_CDATA_RULES),
        new Shape(true, false, false, true, BEGIN_CDATA_RULES),
        new Shape(false, true, false, true, END_CDATA_RULES),
        new Shape(false, true, false, false, END_TAG_RULES),
        new Shape(true, true, false, false, SELF_TERM_TAG_RULES),
        new Shape(true, true, true, false, SELF_CONTAINED_COMMENT_RULES),
        new Shape(true, false, true, false, BEGIN_COMMENT_TAG_RULES),
        new Shape(false, true, true, false, END_COMMENT_TAG_RULES),
        new Shape(true, true, true, false, DOCTYPE_TAG_RULES),
        new Shape(true, false, false, false, BEGIN_TAG_RULES),
        new Shape(true, true, true, false, JSP_RULES)};


    /**
     * <p>Determine if the {@link Node} is a starting, ending, or body text
     * tag. The array of {@link Parser.Shape}s are used to determine the type of
     * {@link Node} the {@link Token} representes.</p>
     *
     * @param node target node
     */
    protected void discoverNodeShape(Node node) {
        Token token = node.getToken();

        nextShape: for (int i = 0; i < NODE_SHAPES.length; i++) {

            int maxBeginOffset = 0;
            int minEndOffset = Integer.MAX_VALUE;

            Shape shape = NODE_SHAPES[i];

            Rule[] rules = shape.getRules();
            for (int j = 0; j < rules.length; j++) {

                // use the begin or end token offset
                int n = (rules[j].isBegin ? token.getBeginOffset() : token.getEndOffset()) + rules[j].getOffset();

                if (rules[j].isBegin) {
                    maxBeginOffset = Math.max(n, maxBeginOffset);
                } else {
                    minEndOffset = Math.min(n, minEndOffset);
                }


                // if out of document range, look for the next shape
                if (n > token.getDocument().length() || n < 0) {
                    continue nextShape;
                }

                // check the operator
                boolean match = false;
                if (rules[j].isEqual) {
                    match = (token.getDocument().charAt(n) ==  rules[j].getMnemonic());
                } else {
                    match = (token.getDocument().charAt(n) !=  rules[j].getMnemonic());
                }

                if (!match) {
                    continue nextShape;
                }
            }

            //make sure the compared token delimiters don't overlap <!-->
            if (minEndOffset <= maxBeginOffset) {
                continue nextShape;
            }

            node.setStart(shape.isStart());
            node.setEnd(shape.isEnd());
            node.setComment(shape.isComment());
            node.setCdata(shape.isCdata);

            break nextShape;
        }

    }

    /**
     * <p>Extracts the node name from the {@link Token} if the {@link Node}
     * is a starting or ending tag.</p>
     *
     * @param node target
     */
    protected void discoverNodeName(Node node) {
        Token token = node.getToken();

        if (node.isStart() || node.isEnd()) {
            // comments are treated special because and ending comment may will not
            // have a node name <!-- <input > -->
            if (node.isComment()) {

                node.setName("--");

            } else if (node.isCdata()) {

                node.setName("[CDATA[");

            } else {
                // find the node name delimiter
                //int e = token.getDocument().indexOf(" ", token.getBeginOffset() + 2);

                //calc end of token body
                int etb = (node.isStart() && node.isEnd()) ? (token.getEndOffset() - 2)
                        : (token.getEndOffset() - 1);

                // find the start of the node attribute body
                int s = (!node.isStart() && node.isEnd()) ? token.getBeginOffset() + 2
                        : token.getBeginOffset() + 1;

                //look for the first whitespace
                int e = -1;
                indexOf: for (int i = s; i < etb; i++) {
                    if (Character.isWhitespace(token.getDocument().charAt(i))) {
                        e = i;
                        break indexOf;
                    }
                }

                // end of token is the end of body
                if (e == -1) {
                    e = etb;
                }

                // return the full node name
                String nodeName = token.getDocument().substring(s, e);
                // separate the namespace
                e = nodeName.indexOf(':');
                if (e > -1) {
                    node.setQname(nodeName.substring(0, e));
                }
                node.setName(nodeName.substring(e + 1));
            }

        }

    }

    /**
     * <p>If the {@link Node} is a starting tag and not a comment,
     * use the {@link AttributeTokenizer} to realize the node attributes.</p>
     *
     * @param node target
     */
    protected void discoverNodeAttributes(Node node) {
        Token token = node.getToken();
        Attributes attributes = this.new Attributes();
        node.setAttributes(attributes);

        // look for attribute in a beginning tag only
        if (node.isStart() && (!node.isComment() && !node.isCdata())) {

            int e = (node.isStart() && node.isEnd()) ? (token.getEndOffset() - 2)
                    : (token.getEndOffset() - 1);

            int s = -1;
            indexOf: for (int i = token.getBeginOffset() + 2; i < e; i++) {
                if (Character.isWhitespace(token.getDocument().charAt(i))) {
                    s = i;
                    break indexOf;
                }
            }

            if (s > -1 && s < e) {

                // find the tokens and load them into the attributes map
                AttributeTokenizer tokenizer = new AttributeTokenizer(token
                        .getDocument(), s, e, token.getLineNumber(), token.getLineBeginOffset());
                Iterator at = tokenizer.iterator();
                while (at.hasNext()) {
                    Map.Entry attribute = (Map.Entry) at.next();
                    attributes.add(attribute);
                }
            }

        }

    }

    /**
     * <p>Explicitly sets the <code>isEnd</code> {@link Node} property to <code>true</code> for
     * self terminating tags.  Sets the {@link Node}'s <code>isWellFormed</code> property
     * to <code>true</code> if the <code>isStart</code> and <code>isEnd</code>
     * {@link Node} properties are <code>true</code>.</p>
     *
     * @param node target
     */
    protected void discoverNodeOverrides(Node node) {
        //look for self terminating tags
        if (node.isStart() && isSelfTerminating(node.getName())) {
            node.setEnd(true);
        }

        // begin and end tag found on a self terminating node <xxx/>
        if (node.isStart() && node.isEnd()) {
            node.setWellFormed(true);
        }

    }

    /**
     * <p>Defines a parsing {@link Parser.Rule} used to determine
     * the {@link Parser.Shape} of a {@link Node}.</p>
     */
    static class Rule {
        /**
         * <p>The target char to check for in the {@link Token} document.</p>
         */
        private char mnemonic = ' ';

        /**
         * <p>A boolen flag that indicates if the <code>offset</code> is from
         * the begining of the {@link Token} offset or the ending offset.</p>
         */
        private boolean isBegin = false;

        /**
         * <p>The offset from the start or end of the {@link Token} that the
         * <code>mnemonic</code> should be found.</p>
         */
        private int offset = 0;
        /**
         * <p>A boolean value that determines the relational operator used
         * to compare the <code>mnemonic</code> to the {@link Token} begin
         * or ending offset plus the {@link Parser.Rule} offset.  If the value
         * is <code>true</code> the equals operator is used; otherwise,
         * the not equals operator is used in the comparison.</p>
         */
        private boolean isEqual = false;

        /**
         * <p>Overloaded constructor for the immutable object.</p>
         * @param mnemonic character looked for in the token
         * @param isBegin boolean that determines if the begining or ending of the Token is used
         * @param offset the offset from the begin or ending Token
         * @param isEqual boolean that determines if the = or != operator is used to check the mnemonic
         */
        public Rule(char mnemonic, boolean isBegin, int offset, boolean isEqual) {
            this.mnemonic = mnemonic;
            this.isBegin = isBegin;
            this.offset = offset;
            this.isEqual = isEqual;
        }
        /**
         * <p>Returns the character looked for in the {@link Token}.</p>
         *
         * @return searched for token
         */
        public char getMnemonic() {
            return mnemonic;
        }
        /**
         * <p>Returns <code>true</code> if the <code>mnemonic</code> is at the
         * begin or end of the token plus the <code>offset</code>.</p>
         *
         * @return <code>true</code> search from the start
         */
        public boolean isBegin() {
            return isBegin;
        }
        /**
         * <p>Returns a positive or negative offset from the begin or ending
         * {@link Token} offset withing the document.</p>
         *
         * @return offset for the begining or ending of the token
         */
        public int getOffset() {
            return offset;
        }

        /**
         * <p>Returns <code>true</code> if the equal relational operator is
         * used for the <code>mnemonic</code> comparison; otherwise the not
         * equal operator is used.</p>
         *
         * @return use relational operator
         */
        public boolean isEqual() {
            return isEqual;
        }
    }

    /**
     * <p>This class defines the shape of the {@link Node} by characterizing
     * if the {@link Token} is a begin, end or comment tag.</p>
     */
    static class Shape {

        /**
         * <p>If <code>true</code> it indicates a starting node.</p>
         */
        private boolean isStart = false;

        /**
         * <p>If <code>true</code> it indicates an ending node.</p>
         */
        private boolean isEnd = false;

        /**
         * <p>If <code>true</code> it indicates a comment node.</p>
         */
        private boolean isComment = false;


        /**
         * <p>If <code>true</code> it indicates a CDATA node.</p>
         */
        private boolean isCdata = false;


        /**
         * <p>An array of {@link Parser.Rule}s used to determine if the
         * {@link Node} matches the {@link Parser.Shape}.</p>
         */
        private Rule[] rules = null;

        /**
         * <p>Overloaded constructor used to instantiate the immutable object.</p>
         *
         * @param isStart starting node
         * @param isEnd ending node
         * @param isComment comment node
         * @param isCdata cdata node
         * @param rules define the node
         */
        public Shape(boolean isStart, boolean isEnd, boolean isComment, boolean isCdata, Rule[] rules) {
            this.isStart = isStart;
            this.isEnd = isEnd;
            this.isComment = isComment;
            this.isCdata = isCdata;
            this.rules = rules;
        }

        /**
         * <p>Returns <code>true</code> if the {@link Token} is a starting tag.</p>
         *
         * @return is a starting tag
         */
        public boolean isStart() {
            return isStart;
        }
        /**
         * <p>Returns <code>true</code> if the {@link Token} is an ending tag.</p>
         *
         * @return is a ending tag
         */
        public boolean isEnd() {
            return isEnd;
        }
        /**
         * <p>Returns <code>true</code> if the {@link Token} is a comment tag.</p>
         *
         * @return is a comment
         */
        public boolean isComment() {
            return isComment;
        }
        /**
         * <p>Returns <code>true</code> if the {@link Token} is a CDATA tag.</p>
         *
         * @return is a cdata
         */
        public boolean isCdata() {
            return isCdata;
        }

        /**
         * <p>Returns the {@link Parser.Rule}s that define the <code>isStart</code>,
         * <code>isEnd</code> and <code>isComment</code> characteristics.</p>
         *
         * @return rules defining the type of node
         */
        public Rule[] getRules() {
            return rules;
        }
    }

}
