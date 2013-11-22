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
 * $Id: Node.java 468624 2006-10-28 03:13:24Z gvanmatre $
 */
package org.apache.shale.clay.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * <p>This class represents a node within a parsed document.</p>
 */
public class Node {

    /**
     * <p>Indicates the node is a comment or within a comment
     * block.</p>
     */
    private boolean isComment = false;

    /**
     * <p>Indicates the node is a starting node.</p>
     */
    private boolean isStart = false;

    /**
     * <p>Indicates the node is a ending node.</p>
     */
    private boolean isEnd = false;


    /**
     * <p>This flag indicates the node is a CDATA node.</p>
     */
    private boolean isCdata = false;

    /**
     * <p>This boolean flag has a <code>true</code> value if the
     * node has a starting and ending node.  Not all nodes will be
     * well formed.  Inline text or comments are not well formed.
     * </p>
     */
    private boolean isWellFormed = false;

    /**
     * <p>Qualified name.  This won't be used in HTML documents</p>
     */
    private String qname = null;

    /**
     * <p>The node name.</p>
     */
    private String name = null;

    /**
     * <p>Value pair tags within the body of the starting node.</p>
     */
    private Map attributes = new TreeMap();

    /**
     * <p>An object instance implementing the {@link Token} interface.
     * This object represents a starting and ending offset within the
     * document</p>
     */
    private Token token = null;

    /**
     * <p>Child nodes within the document.  These nodes will fall within
     * the beginning and ending node makers</p>
     */
    private List children = new ArrayList();

    /**
     * <p>All nodes but top-level nodes will have a parent node.  Because
     * the {@link Parser} can work through a incomplete and not well formed
     * HTML fragment, there may be several top-level nodes</p>
     */
    private Node parent = null;

    /**
     * <p>Returns <code>true</code> if the node has a beginning and ending
     * marker.</p>
     *
     * @return <code>true</code> if node is well-formed
     */
    public boolean isWellFormed() {
        return isWellFormed;
    }

    /**
     * <p>Sets a boolean flag that is <code>true</code> if the node has a
     * beginning and ending marker.</p>
     *
     * @param isWellFormed indicates if the node is well-formed
     */
    public void setWellFormed(boolean isWellFormed) {
        this.isWellFormed = isWellFormed;
    }

    /**
     * <p>Returns the parent of the node or <code>null</code> if the node
     * is a top-level/root node.</p>
     *
     * @return parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * <p>Sets the parent node.</p>
     *
     * @param parent nodes parent
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * <p>Returns a <code>List</code> of child nodes.</p>
     *
     * @return children of this node
     */
    public List getChildren() {
        return children;
    }

    /**
     * <p>Adds a child node to the <code>children</code> collection.</p>
     *
     * @param child added to this node
     */
    public void addChild(Node child) {
        child.setParent(this);
        children.add(child);
    }

    /**
     * <p>Overloaded constructor that requires a {@link Token} object
     * in the formal parameter.</p>
     *
     * @param token document token offset of this node
     */
    public Node(Token token) {
        this.token = token;
    }

    /**
     * <p>Returns an object that represents the starting and ending offsets
     * within the document that this node represents.</p>
     *
     * @return document offset token for this node
     */
    public Token getToken() {
        return token;
    }

    /**
     * <p>Returns a <code>true</code> value if this node is a ending marker.</p>
     *
     * @return <code>true</code> if this is a ending node
     */
    public boolean isEnd() {
        return isEnd;
    }

    /**
     * <p>Sets the flag indicating that this node is a ending marker.</p>
     *
     * @param isEnd indicates ending marker
     */
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * <p>Returns a Map collection of node attributes.</p>
     *
     * @return node attributes
     */
    public Map getAttributes() {
        return attributes;
    }

    /**
     * <p>Sets a Map collection of Node attributes.</p>
     *
     * @param attributes of the node
     */
    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    /**
     * <p>Returns <code>true</code> if the node is a beginning marker.</p>
     *
     * @return <code>true</code> if a beginning marker
     */
    public boolean isStart() {
        return isStart;
    }

    /**
     * <p>Sets a boolean flag indicating that the node is a beginning marker.</p>
     *
     * @param isStart beginning marker flag
     */
    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    /**
     * <p>Returns the node name.</p>
     *
     * @return name of the ndoe
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Sets the node name.</p>
     *
     * @param name of the node
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Returns the qualified node name.</p>
     *
     * @return the namespace prefix of the node
     */
    public String getQname() {
        return qname;
    }

    /**
     * <p>Sets the qualified node name.</p>
     *
     * @param qname namespace prefix of the node
     */
    public void setQname(String qname) {
        this.qname = qname;
    }

    /**
     * @return Describes the objects state
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("name=").append(name).append(" isStart=").append(isStart)
        .append(" isEnd=").append(isEnd).append(" isWellFormed=")
        .append(isWellFormed).append(" isComment=").append(isComment)
        .append(" isCdata=").append(isCdata)
        .append("\n").append(token).append("\n").append(attributes);
        return buff.toString();
    }

    /**
     * <p> Returns <code>true</code> if the node is
     * a comment; otherwise; the default is <code>false</code>.
     * </p>.
     *
     * @return <code>true</code> if node is a comment or in a comment
     */
    public boolean isComment() {
        return isComment;
    }


    /**
     * <p>Sets a boolean value that identifies this node as
     * being a comment.  This could be a starting, ending or
     * within the body.</p>
     *
     * @param isComment indicates node is or is in a comment block
     */
    public void setComment(boolean isComment) {
        this.isComment = isComment;
    }


    /**
     * <p> Returns <code>true</code> if the node is
     * a CDATA; otherwise; the default is <code>false</code>.
     * </p>.
     *
     * @return <code>true</code> if the node is or is in a CDATA block
     */
    public boolean isCdata() {
        return isCdata;
    }


    /**
     * <p>Sets a boolean value that identifies this node as
     * being a CDATA.  This could be a starting, ending or
     * within the body.</p>
     *
     * @param isCdata indicates the node is in a cdata block
     */
    public void setCdata(boolean isCdata) {
        this.isCdata = isCdata;
    }

    /**
     * <p>Finds matching nodes by <code>name</code> searching thru all the children.</p>
     *
     * @param name of the target node
     * @return list of nodes found by name
     */
    public List getNodesByName(String name) {
        List nodes = new ArrayList();
        findNodesByName(this, name, nodes);
        return nodes;
    }

    /**
     * <p>Recursively walks down the tree looking for nodes matching the <code>name</code>.</p>
     *
     * @param node markup
     * @param name searched argument
     * @param nodes nodes in the hierarchy matched by name
     */
    private void findNodesByName(Node node, String name, List nodes) {
        if (node.getName() != null && node.getName().equals(name)) {
            nodes.add(node);
        }

        Iterator ni = node.getChildren().iterator();
        while (ni.hasNext()) {
            Node child = (Node) ni.next();
            findNodesByName(child, name, nodes);
        }

    }

    /**
     * <p>Walks up the tree looking for a uri namespace matching the <code>prefix</code>.
     * A <code>null</code> prefix will search for the default uri namespace.</p>
     *
     * @param prefix node qname namespace prefix
     * @return url matching the namespace prefix
     */
    public String getNamespaceURI(String prefix) {
        StringBuffer attributeName = new StringBuffer("xmlns");
        if (prefix != null && prefix.length() > 0) {
            attributeName.append(":").append(prefix);
        }

        String uri = (String) attributes.get(attributeName.toString());

        if (uri != null) {
            return uri;
        }

        Node parent = getParent();
        while (parent != null) {
            uri = (String) parent.getAttributes().get(attributeName.toString());
            if (uri != null) {
                return uri;
            } else {
                parent = parent.getParent();
            }
        }


        return null;
    }

}
