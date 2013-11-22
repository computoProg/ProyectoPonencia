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
 * $Id: AttributeTokenizer.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;

/**
 * <p>
 * Tokenizes a portion of the document for attributes. The complete document is
 * passed by reference and new {@link Token} offsets are created for the name
 * and value of the discovered attributes.
 * </p>
 */

public class AttributeTokenizer {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", AttributeTokenizer.class
            .getClassLoader());

    /**
     * <p>
     * Common logging utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(AttributeTokenizer.class);
    }

    /**
     * <p>
     * Internal document buffer.
     * </p>
     */
    private StringBuffer buffer = null;

    /**
     * <p>
     * Beginning offset of the starting node.
     * </p>
     */
    private int beginOffset = 0;

    /**
     * <p>
     * Ending offset of the starting node.
     * </p>
     */
    private int endOffset = 0;

    /**
     * <p>Line number the target node is located in.</p>
     */
    private int lineNumber = 0;

    /**
     * <p>Line begining document offset where the target node is located.</p>
     */
    private int lineBeginOffset = 0;


    /**
     * <p>
     * Overloaded constructor that is passed the complete document and the
     * starting and ending offset of the node body within the document.
     * </p>
     *
     * @param buffer document
     * @param beginOffset start index of node body in the document
     * @param endOffset end index of node body in the document
     * @param lineNumber line number of the node within the document
     * @param lineBeginOffset index in the document that the line begins
     */
    public AttributeTokenizer(StringBuffer buffer, int beginOffset,
            int endOffset, int lineNumber, int lineBeginOffset) {
        this.buffer = buffer;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.lineBeginOffset = lineBeginOffset;
        this.lineNumber = lineNumber;

    }

    /**
     * <p>
     * Inner class implementing the {@link Token} interface. This class will
     * define an attribute's key and value offsets
     * </p>
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
         * @param beginOffset token start index
         * @param endOffset token end index
         */
        public TokenOffset(int beginOffset, int endOffset) {
            this.beginOffset = beginOffset;
            this.endOffset = endOffset;
        }

        /**
         * @return starting offset of the token in the document
         */
        public int getBeginOffset() {
            return beginOffset;
        }

        /**
         * @return ending offset of the token in the document
         */
        public int getEndOffset() {
            return endOffset;
        }

        /**
         * @return parsed document
         */
        public StringBuffer getDocument() {
            return buffer;
        }

        /**
         * @return token text between the beginOffset and endOffset
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
         * @return line number the token is found on within the document
         */
        public int getLineNumber() {
           return lineNumber;
        }

        /**
         * @return offset within the document that the token line is found
         */
        public int getLineBeginOffset() {
           return lineBeginOffset;
        }

        /**
         * @return description of the token
         */
        public String toString() {
            return messages.getMessage("node.token.range",
                    new Object[] {
                new Integer(beginOffset),
                        new Integer(endOffset),
                        new Integer(lineNumber),
                        new Integer(lineBeginOffset)});
        }

    }

    /**
     * <p>
     * This inner class implements the <code>Map.Entry</code> interfaces. It
     * holds a reference to the key and value parts of an attribute. Both the
     * key and value attributes are {@link Token} instances.
     * </p>
     */
    private class AttributeEntry implements Map.Entry {
        /**
         * <p>Token offset of the attribute key.</p>
         */
        private TokenOffset key = null;

        /**
         * <p>Token offset of the attribute value.</p>
         */
        private TokenOffset value = null;

        /**
         * <p>
         * Overloaded constructor is passed a {@link Token} for the key and
         * value attributes.
         * </p>
         *
         * @param key token key offset
         * @param value token value offset
         */
        public AttributeEntry(TokenOffset key, TokenOffset value) {
            this.key = key;
            this.value = value;
        }

        /**
         * <p>
         * Returns the attribute name {@link Token} offset.
         * </p>
         *
         * @return TokenOffset for the attribute key
         */
        public Object getKey() {
            return key;
        }

        /**
         * <p>
         * Returns the attribute value {@link Token} offset.
         * </p>
         *
         * @return TokenOffset of the attribute value
         */
        public Object getValue() {
            return value;
        }

        /**
         * <p>
         * Sets the attribute value {@link Token} offset.
         * </p>
         *
         * @param value TokenOffset value
         * @return value token offset
         */
        public Object setValue(Object value) {
            this.value = (TokenOffset) value;
            return value;
        }

        /**
         * @return description of the attribute
         */
        public String toString() {
            StringBuffer buff = new StringBuffer();
            TokenOffset key = (TokenOffset) getKey();
            TokenOffset value = (TokenOffset) getValue();

            buff.append("key: [").append((key != null ? key.getRawText() : null))
            .append("]\n").append("value: [")
            .append((value != null ? value.getRawText() : null))
            .append("]");

            return buff.toString();
        }
    }

    /**
     * <p>
     * The current offset within the <code>beginOffset</code> and
     * <code>endOffset</code> of the Node within the document.
     */
    private int currOffset = 0;

    /**
     * <p>
     * Builds an <code>ArrayList</code> of
     * {@link AttributeTokenizer.AttributeEntry} instances identifying
     * name and value pairs.
     * </p>
     *
     * @param tokenIndex populated attribute offset of a beging node body
     */
    protected synchronized void parse(ArrayList tokenIndex) {
        currOffset = beginOffset;

        if (log.isDebugEnabled()) {
            log.debug(messages.getMessage("attribute.range", new Object[] {
                new Integer(beginOffset), new Integer(endOffset) }));
        }

        while (currOffset < endOffset) {
            // skip leading spaces
            int startOffset = currOffset;
            while (Character.isWhitespace(buffer.charAt(currOffset))) {
                currOffset++;
            }

            if (log.isDebugEnabled()) {
                if (currOffset > startOffset) {
                    log.debug(messages
                            .getMessage("attribute.skip.space",
                            new Object[] { new Integer(currOffset
                                    - startOffset) }));
                }
            }

            // looks for the key value delimiter
            TokenOffset key = nextToken(currOffset, " ", "=", true);
            if (key == null) {
                break;
            }

            boolean skipValue = false;
            currOffset++;
            String delim = " "; // old school html color=red
            String otherDelim = "\"";
            if (currOffset < buffer.length()
                && buffer.charAt(currOffset) == '"') {

                // xmlish attribute
                delim = "\"";
                otherDelim = " ";
                currOffset++;
            } else if (currOffset < buffer.length() && currOffset > 0
                    && buffer.charAt(currOffset - 1) == ' ') {

                //attribute without value
                currOffset--;        //back up <option selected value=
                skipValue = true;
            }

            TokenOffset value = null;
            if (!skipValue) {   // no value part <option selected value=xxx>
               value = nextToken(currOffset, delim, otherDelim, false);
            }

            tokenIndex.add(new AttributeEntry(key, value));

            currOffset++;
            key = null;
            value = null;
        }

        if (log.isDebugEnabled()) {
            log.debug(messages.getMessage("attributes.total.found",
                    new Object[] { new Integer(tokenIndex.size()) }));
        }

    }

    /**
     * <p>
     * Returns the next {@link Token} given an <code>startOffset</code> and a
     * <code>endDelim</code>.
     * </p>
     *
     * @param startOffset begining offset in the document
     * @param endDelim primary token delimiter
     * @param otherDelim secondary token delimiter
     * @param isKey looking for an attribute name not a value
     * @return next token offset
     */
    protected TokenOffset nextToken(int startOffset, String endDelim, String otherDelim, boolean isKey) {
        //If isKey is true, we are looking for an attribute name with a endDelim or otherDelim.
        //Pick the one that comes first.

        //If isKey is false we are looking for an attribute value.  The endDelim is the best guess
        //and the otherDelim is the next best guess.
        if (isKey) {
            int offsetEnd = Math.min(buffer.indexOf(endDelim, startOffset), endOffset);
            int offsetOther = Math.min(buffer.indexOf(otherDelim, startOffset), endOffset);
            if (offsetEnd == -1) {
               currOffset = offsetOther;
            } else if (offsetOther == -1) {
               currOffset = offsetOther;
            } else {
               currOffset = Math.min(offsetEnd, offsetOther);
            }
        } else {
           currOffset = Math.min(buffer.indexOf(endDelim, startOffset), endOffset);
           // try another delimiter
           if (currOffset == -1) {
              currOffset = Math.min(buffer.indexOf(otherDelim, startOffset), endOffset);
           }
        }


        if (currOffset == -1) {
            currOffset = endOffset;
        }

        // look for the value delimiter or the end of the parse fragment,
        // whichever comes first
        if (currOffset > -1 && currOffset <= endOffset && startOffset < currOffset) {

            int e = currOffset;
            //forgive an attribute with Inconsistent delimiters, color=red"
            if (buffer.charAt(e - 1) == '"'
                || (Character.isWhitespace(buffer.charAt(e - 1))
                && buffer.charAt(e - 1) != ' ')) {
              --e;
            }


            TokenOffset value = new TokenOffset(startOffset, e);

            if (log.isDebugEnabled()) {
                log.debug(messages.getMessage("attribute.token.range",
                        new Object[] { new Integer(startOffset),
                                new Integer(e) }));
            }

            return value;
        }

        return null;
    }

    /**
     * <p>Inner class implementing the <code>Iterator</code>
     * interface. This class is a decorator of a <code>ArrayList</code>
     * of nodes.
     * </p>
     */
    private class TokenIterator implements Iterator {

        /**
         * <p>All the attribute entry tokens in the node body.</p>
         */
        private ArrayList tokenIndex = null;

        /**
         * <p>Internal <code>tokenIndex</code> iterator.</p>
         */
        private Iterator ti = null;

        /**
         * <p>Constructor parses the node body into a collection of
         * {@link AttributeTokenizer.AttributeEntry}.
         * </p>
         */
        public TokenIterator() {
            tokenIndex = new ArrayList();
            parse(tokenIndex);
            ti = tokenIndex.iterator();
        }

        /**
         * <p>Retuns <code>true</code> if there are more
         * {@link AttributeTokenizer.AttributeEntry} in the collection.
         * </p>
         *
         * @return <code>true</code> if there are more tokens
         */
        public boolean hasNext() {
            return ti.hasNext();
        }

        /**
         * <p>Retuns the next {@link AttributeTokenizer.AttributeEntry}
         * in the collection.
         * </p>
         *
         * @return returns the next token
         */
        public Object next() {
            Map.Entry attribute = (Map.Entry) ti.next();
            return attribute;
        }

        /**
         * <p>This method is not implemented.</p>
         *
         * @deprecated
         */
        public void remove() {
            // NA
        }
    }

    /**
     * <p>Returns an instance of an <code>Iterator</code> that
     * will enumerate attributes in the document where the attributes
     * are represented by a {@link AttributeTokenizer.AttributeEntry} instance.
     * </p>
     *
     * @return returns a {@link AttributeTokenizer.TokenIterator} iterator.
     */
    public Iterator iterator() {
        return new TokenIterator();
    }

}
