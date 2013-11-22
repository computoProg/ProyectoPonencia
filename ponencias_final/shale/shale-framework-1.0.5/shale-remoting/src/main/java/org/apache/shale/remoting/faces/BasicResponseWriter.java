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

package org.apache.shale.remoting.faces;

import java.io.IOException;
import java.io.Writer;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

/**
 * <p>Basic implementation of <code>javax.faces.context.ResponseWriter</code>
 * for use when a content type other than <code>text/html</code> is desired
 * (such as <code>text/xml</code>).</p>
 */
public class BasicResponseWriter extends ResponseWriter {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new instance configured with the specified properties.</p>
     *
     * @param writer <code>Writer</code> to be wrapped
     * @param contentType Content type of this response
     * @param characterEncoding Character encoding of thie response
     **/
    public BasicResponseWriter(Writer writer, String contentType, String characterEncoding) {
        this.writer = writer;
        this.contentType = contentType;
        this.characterEncoding = characterEncoding;
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The character encoding of the response we are creating.</p>
     */
    private String characterEncoding = null;


    /**
     * <p>The content type of the response we are creating.</p>
     */
    private String contentType = "text/html";


    /**
     * <p>Flag indicating that an element has been started.</p>
     */
    private boolean open = false;


    /**
     * <p>The <code>Writer</code> we are wrapping.</p>
     */
    private Writer writer = null;


    // ----------------------------------------------------- Mock Object Methods



    // -------------------------------------------------- ResponseWriter Methods


    /** {@inheritDoc} */
    public ResponseWriter cloneWithWriter(Writer writer) {
        return new BasicResponseWriter(writer, contentType, characterEncoding);
    }


    /** {@inheritDoc} */
    public void endDocument() throws IOException {
        finish();
        writer.flush();
    }


    /** {@inheritDoc} */
    public void endElement(String name) throws IOException {
        if (open) {
            writer.write("/");
            finish();
        } else {
            writer.write("</");
            writer.write(name);
            writer.write(">");
        }
    }


    /** {@inheritDoc} */
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }


    /** {@inheritDoc} */
    public String getContentType() {
        return this.contentType;
    }


    /** {@inheritDoc} */
    public void flush() throws IOException {
        finish();
    }


    /** {@inheritDoc} */
    public void startDocument() throws IOException {
        // Do nothing
    }


    /** {@inheritDoc} */
    public void startElement(String name, UIComponent component) throws IOException {
        if (name == null) {
            throw new NullPointerException();
        }
        finish();
        writer.write('<');
        writer.write(name);
        open = true;
    }


    /** {@inheritDoc} */
    public void writeAttribute(String name, Object value, String property) throws IOException {
        if ((name == null) || (value == null)) {
            throw new NullPointerException();
        }
        if (!open) {
            throw new IllegalStateException();
        }
        writer.write(" ");
        writer.write(name);
        writer.write("=\"");
        if (value instanceof String) {
            string((String) value);
        } else {
            string(value.toString());
        }
        writer.write("\"");
    }


    /** {@inheritDoc} */
    public void writeComment(Object comment) throws IOException {
        if (comment == null) {
            throw new NullPointerException();
        }
        finish();
        writer.write("<!-- ");
        if (comment instanceof String) {
            writer.write((String) comment);
        } else {
            writer.write(comment.toString());
        }
        writer.write(" -->");
    }


    /** {@inheritDoc} */
    public void writeText(Object text, String property) throws IOException {
        if (text == null) {
            throw new NullPointerException();
        }
        finish();
        if (text instanceof String) {
            string((String) text);
        } else {
            string(text.toString());
        }
    }


    /** {@inheritDoc} */
    public void writeText(char[] text, int off, int len) throws IOException {
        if (text == null) {
            throw new NullPointerException();
        }
        if ((off < 0) || (off > text.length) || (len < 0) || (len > text.length)) {
            throw new IndexOutOfBoundsException();
        }
        finish();
        string(text, off, len);
    }


    /** {@inheritDoc} */
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        if ((name == null) || (value == null)) {
            throw new NullPointerException();
        }
        if (!open) {
            throw new IllegalStateException();
        }
        writer.write(" ");
        writer.write(name);
        writer.write("=\"");
        if (value instanceof String) {
            string((String) value);
        } else {
            string(value.toString());
        }
        writer.write("\"");
    }


    // ---------------------------------------------------------- Writer Methods


    /** {@inheritDoc} */
    public void close() throws IOException {
        finish();
        writer.close();
    }


    /** {@inheritDoc} */
    public void write(char[] cbuf, int off, int len) throws IOException {
        finish();
        writer.write(cbuf, off, len);
    }


    // --------------------------------------------------------- Support Methods


    /**
     * <p>Write the specified character, filtering if necessary.</p>
     *
     * @param ch Character to be written
     *
     * @exception IOException if an input/output error occurs
     */
    private void character(char ch) throws IOException {

        if (ch <= 0xff) {
            // In single byte characters, replace only the five
            // characters for which well-known entities exist in XML
            if (ch == 0x22) {
                writer.write("&quot;");
            } else if (ch == 0x26) {
                writer.write("&amp;");
            } else if (ch == 0x27) {
                writer.write("&apos;");
            } else if (ch == 0x3C) {
                writer.write("&lt;");
            } else if (ch == 0X3E) {
                writer.write("&gt;");
            } else {
                writer.write(ch);
            }
        } else {
            if (substitution()) {
                numeric(writer, ch);
            } else {
                writer.write(ch);
            }
        }

    }


    /**
     * <p>Close any element that is currently open.</p>
     *
     * @exception IOException if an input/output error occurs
     */
    private void finish() throws IOException {

        if (open) {
            writer.write(">");
            open = false;
        }

    }


    /**
     * <p>Write a numeric character reference for specified character
     * to the specfied writer.</p>
     *
     * @param writer Writer we are writing to
     * @param ch Character to be translated and appended
     *
     * @exception IOException if an input/output error occurs
     */
    private void numeric(Writer writer, char ch) throws IOException {

        writer.write("&#");
        writer.write(String.valueOf((int) ch));
        writer.write(";");

    }


    /**
     * <p>Write the specified characters (after performing suitable
     * replacement of characters by corresponding entities).</p>
     *
     * @param text Character array containing text to be written
     * @param off Starting offset (zero relative)
     * @param len Number of characters to be written
     *
     * @exception IOException if an input/output error occurs
     */
    private void string(char[] text, int off, int len) throws IOException {

        // Process the specified characters
        for (int i = off; i < (off + len); i++) {
            character(text[i]);
        }

    }


    /**
     * <p>Write the specified string (after performing suitable
     * replacement of characters by corresponding entities).</p>
     *
     * @param s String to be filtered and written
     *
     * @exception IOException if an input/output error occurs
     */
    private void string(String s) throws IOException {

        for (int i = 0; i < s.length(); i++) {
            character(s.charAt(i));
        }

    }


    /**
     * <p>Return true if entity substitution should be performed on double
     * byte character values.</p>
     */
    private boolean substitution() {

        return !("UTF-8".equals(characterEncoding) || "UTF-16".equals(characterEncoding));

    }


}
