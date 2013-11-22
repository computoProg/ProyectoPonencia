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
 * $Id: NodeTokenizer.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;

/**
 * <p>
 * Splits a document into tokens using the following delimiters "<>". The
 * tokens are represented by a starting and ending offset so a bunch of strings
 * are not created until the content is needed.
 * </p>
 */

public class NodeTokenizer {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", NodeTokenizer.class
            .getClassLoader());

    /**
     * <p>
     * Common logger utility instance.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(NodeTokenizer.class);
    }

    /**
     * <p>
     * The complete document being parsed.
     * </p>
     */
    private StringBuffer buffer = null;

    /**
     * <p>
     * Constructor with the complete document to parse.
     * </p>
     *
     * @param buffer document
     */
    public NodeTokenizer(StringBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     *
     * <p>
     * Inner class implementing the {@link Token} interface.
     * </p>
     *
     */
    private class TokenOffset implements Token {
        /**
         * <p>Starting offset of the token.</p>
         */
        private int beginOffset = 0;
        /**
         * <p>Ending offset of the token.</p>
         */
        private int endOffset = 0;
        /**
         * <p>Line number the token was found on.</p>
         */
        private int lineNumber = 0;
        /**
         * <p>Offset the begining line was found.</p>
         */
        private int lineBeginOffset = 0;

        /**
         * <p>
         * Constructor requires the begining and ending offset within the
         * document of the {@link Node}.
         * </p>
         *
         * @param beginOffset starting offset
         * @param endOffset ending offset
         * @param lineNumber line the token is found within the document
         * @param lineBeginOffset offset the line is in the document
         */
        public TokenOffset(int beginOffset, int endOffset, int lineNumber, int lineBeginOffset) {
            this.beginOffset = beginOffset;
            this.endOffset = endOffset;
            this.lineNumber = lineNumber;
            this.lineBeginOffset = lineBeginOffset;
        }

        /**
         * <p>
         * Returns the begining offset within the document for the {@link Node}.
         * </p>
         *
         * @return begining offset
         */
        public int getBeginOffset() {
            return beginOffset;
        }

        /**
         * <p>
         * Returns the ending offset within the document for the {@link Node}.</p>
         *
         * @return ending offset
         */
        public int getEndOffset() {
            return endOffset;
        }

        /**
         * <p>
         * Returns the complete document being parsed.
         * </p>
         *
         * @return document
         */
        public StringBuffer getDocument() {
            return buffer;
        }

        /**
         * <p>
         * Returns the raw representation of the {@link Node} within the
         * document identified by the {@link Token}.
         * </p>
         *
         * @return node text for beginOffset to endOffset
         */
        public String getRawText() {
            String pickel = null;
            try {
                pickel = buffer.substring(beginOffset, endOffset);
            } catch (RuntimeException e) {
                log.error(toString(), e);
                throw e;
            }
            return pickel;
        }

        /**
         * @return describes the objects state
         */
        public String toString() {
            return messages.getMessage("node.token.range",
                    new Object[] {
                new Integer(beginOffset),
                        new Integer(endOffset),
                        new Integer(lineNumber),
                        new Integer(lineBeginOffset)});
        }

        /**
         * <p>Returns the line number in the document that the node starts.</p>
         *
         * @return line number
         */
        public int getLineNumber() {
           return lineNumber;
        }

        /**
         * <p>Returns the line begining offset in the document that the node starts.</p>
         *
         * @return line begin offset within the document
         */
        public int getLineBeginOffset() {
           return lineBeginOffset;
        }

    }

    /**
     * <p>
     * This method is passed an empty <code>ArrayList</code> that should be
     * populated into {@link Token} offsets.
     * </p>
     *
     * @param tokenIndex all the document tokens
     */
    protected void index(ArrayList tokenIndex) {
        int s = 0;
        int lineNumber = 1;
        int lineBeginOffset = 0;

        if (log.isDebugEnabled()) {
            log.debug(messages.getMessage("node.document.size",
                    new Object[] { new Integer(buffer.length()) }));
        }

        for (int i = 0; i < buffer.length(); i++) {

            if (buffer.charAt(i) == '<') {
                if (i > s && s < i) {

                    TokenOffset offset = new TokenOffset(s, i, lineNumber, lineBeginOffset);
                    tokenIndex.add(offset);

                    if (log.isDebugEnabled()) {
                        log.debug(messages.getMessage("node.token.range",
                                new Object[] {
                            new Integer(offset.getBeginOffset()),
                                    new Integer(offset.getEndOffset()),
                                    new Integer(offset.getLineNumber()),
                                    new Integer(offset.getLineBeginOffset())}));
                    }

                }
                s = i;
            } else if (buffer.charAt(i) == '>') {
                if (i > s) {
                    TokenOffset offset = new TokenOffset(s, i + 1, lineNumber, lineBeginOffset);
                    tokenIndex.add(offset);

                    if (log.isDebugEnabled()) {
                        log.debug(messages.getMessage("node.token.range",
                                new Object[] {
                            new Integer(offset.getBeginOffset()),
                                    new Integer(offset.getEndOffset()),
                                    new Integer(offset.getLineNumber()),
                                    new Integer(offset.getLineBeginOffset())}));
                    }
                }
                s = i + 1;
            } else if (buffer.charAt(i) == '\n') {
                lineNumber++;
                lineBeginOffset = i;
            }

        }

        if ((buffer.length()) > s + 1) {
            TokenOffset offset = new TokenOffset(s, (buffer.length()), lineNumber, lineBeginOffset);
            tokenIndex.add(offset);

            if (log.isDebugEnabled()) {
                log.debug(messages.getMessage("node.token.range",
                        new Object[] {
                    new Integer(offset.getBeginOffset()),
                            new Integer(offset.getEndOffset()),
                            new Integer(offset.getLineNumber()),
                            new Integer(offset.getLineBeginOffset())}));
            }

        }

    }

    /**
     * <p>This inner class implements the <code>Iterator</code>
     * interface and is used to enumerate the {@link Token}
     * offset that define the document.  It's a decorator for
     * an <code>Iterator</code> of a internal collection of
     * node offsets.
     * </p>
     */
    private class TokenIterator implements Iterator {

        /**
         * <p>All the document tokens.</p>
         */
        private ArrayList tokenIndex = null;

        /**
         * <p>Current tokenIndex iterator.</p>
         */
        private Iterator ti = null;

        /**
         * <p>Constructor invokes the <code>index</code> method
         * passing an <code>ArrayList</code> to populate with
         * {@link Token} offsets.
         * </p>
         */
        public TokenIterator() {
            tokenIndex = new ArrayList();
            index(tokenIndex);
            ti = tokenIndex.iterator();
        }

        /**
         * <p>Retuns <code>true</code> if there are more {@link Token} node
         * offsets within the parsed document.
         * </p>
         *
         * @return <code>true</code> if there are more tokens
         */
        public boolean hasNext() {
            return ti.hasNext();
        }

        /**
         * <p>Returns the next {@link Token} in the document.</p>
         *
         * @return the next document token
         */
        public Object next() {
            TokenOffset offset = (TokenOffset) ti.next();
            return offset;
        }

        /**
         * <p>Not supported.</p>
         *
         * @deprecated
         */
        public void remove() {
            // NA
        }
    }

    /**
     * <p>Returns an implementation of the <code>Iterator</code>
     * interface to enumerate the nodes within the document.  Each
     * node is defined using a {@link Token} interface.
     * </p>
     *
     * @return TokenIterator
     */
    public Iterator iterator() {
        return new TokenIterator();
    }

}
