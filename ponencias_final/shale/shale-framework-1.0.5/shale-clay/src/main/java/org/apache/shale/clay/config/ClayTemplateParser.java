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
 * $Id: ClayTemplateParser.java 467434 2006-10-24 18:48:48Z gvanmatre $
 */
package org.apache.shale.clay.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ComponentConfigBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.TemplateConfigBean;
import org.apache.shale.clay.parser.AttributeTokenizer;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.Parser;
import org.apache.shale.clay.parser.Token;
import org.apache.shale.clay.parser.builder.Builder;
import org.apache.shale.clay.parser.builder.BuilderFactory;
import org.apache.shale.util.Messages;
import org.xml.sax.SAXException;

/**
 * <p>
 * This class is responsible for loading an HTML template into a graph of
 * {@link ComponentBean}'s that represents a JSF component tree. It is used by
 * {@link org.apache.shale.clay.config.beans.TemplateConfigBean}, a subclass of
 * {@link org.apache.shale.clay.config.beans.ComponentConfigBean}.
 * </p>
 */
public class ClayTemplateParser implements ClayConfigParser {

    /**
     * <p>
     * Commons logging utility object static instance.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory
                .getLog(org.apache.shale.clay.config.ClayXmlParser.class);
    }

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", ClayConfigureListener.class
                    .getClassLoader());

    /**
     * <p>
     * Object pool for HTML template configuration files.
     * </p>
     */
    private ConfigBean config = null;

    /**
     * <p>
     * Sets an object pool for HTML template configuration files.
     * </p>
     *
     * @param config
     *            handler
     */
    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    /**
     * <p>
     * Returns an object pool for HTML template configuration files.
     * </p>
     *
     * @return config handler
     */
    public ConfigBean getConfig() {
        return config;
    }

    /**
     * <p>
     * Loads the <code>templateURL</code> identified by the
     * <code>templateName</code> into a graph of {@link ComponentBean}'s.
     * </p>
     *
     * @param templateURL
     *            template file
     * @param templateName
     *            jsfid
     * @exception SAXException
     *                XML parse error
     * @exception IOException
     *                XML parse error
     */
    public void loadConfigFile(URL templateURL, String templateName)
            throws IOException, SAXException {

        ((ComponentConfigBean) config).addChild(generateElement(templateURL,
                templateName));
    }

    /**
     * <p>
     * Loads the template file and parses it into a composition of metadata
     * that's used by the {@link org.apache.shale.clay.component.Clay}
     * component. This metadata is used to construct a JSF subtree within target
     * view.
     * </p>
     *
     * @param templateURL
     *            template file
     * @param templateName
     *            jsfid
     * @return config bean graph holding the template file
     * @exception IOException
     *                loading template file
     */
    protected ComponentBean generateElement(URL templateURL, String templateName)
            throws IOException {

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("loading.template",
                    new Object[] { templateName }));
        }

        ComponentBean root = new ComponentBean();
        root.setJsfid(templateName);
        root.setComponentType("javax.faces.HtmlOutputText");

        // generate the document

        StringBuffer buffer = loadTemplate(templateURL);

        List roots = new Parser().parse(buffer);
        Iterator ri = roots.iterator();
        while (ri.hasNext()) {
            Node node = (Node) ri.next();
            Builder renderer = getBuilder(node);
            ElementBean child = renderer.createElement(node);

            root.addChild(child);
            if (renderer.isChildrenAllowed()) {
                renderer.encode(node, child, child);
            } else {
                renderer.encode(node, child, root);
            }
        }

        roots.clear();
        roots = null;
        buffer.setLength(0);
        buffer = null;
        ri = null;

        // verify there is not a duplicate component id within a naming
        // container.
        config.checkTree(root);

        // compress the tree merging adjacent verbatim nodes
        if (config instanceof TemplateConfigBean) {
            ((TemplateConfigBean) config).optimizeTree(root);
        }

        return root;
    }

    /**
     * <p>Loads the template file respecting the encoding type.
     * The file encoding type is determined by calling
     * the <code>getCharacterEncoding()</code> method.
     * </p>
     *
     * @param templateURL target template to load
     * @return content of the template
     * @throws IOException error loading the template
     */
    public StringBuffer loadTemplate(URL templateURL) throws IOException {

        StringBuffer buff = new StringBuffer();
        BufferedReader in = null;
        String enc = getCharacterEncoding(templateURL);

        if (log.isDebugEnabled()) {
           log.debug(messages.getMessage("template.encoding",
               new Object[] { enc, templateURL.getFile() }));
        }

        try {

            in = new BufferedReader(new InputStreamReader(templateURL.openStream(), enc));
            while (in.ready()) {
               buff.append(in.readLine()).append("\n");
            }

        } catch (IOException e) {
            log.error(messages.getMessage("loading.template.exception",
                    new Object[] { templateURL.getFile() }), e);
            throw e;
        } finally {
           if (in != null) {
              in.close();
           }
        }

        return buff;

    }


    /**
     * <p>Returns the encoding type used to open the <code>templateURL</code>.
     * The template encoding type is resolved using three overrides.  The first
     * step is to look in the target template for a comment token that defines
     * the charset. The first 512 chars of the <code>templateURL</code> are read
     * and scanned for a special comment token.<br/></br/>
     *
     * For example: &lt;-- ### clay:page charset="UTF-8" /### --><br/><br/>
     *
     * If the Clay page directive is not found, the next override is an
     * initialization parameter in the web.xml.  The value of this parameter
     * is a global override for all templates.<br/><br/>
     *
     * For example:<br/>
     * &lt;context-param&gt;<br/>
     * &nbsp;&nbsp;&lt;param-name&gt;org.apache.shale.clay.HTML_TEMPLATE_CHARSET
     * &lt;/param-name&gt;<br/>
     * &nbsp;&nbsp;&lt;param-value&gt;UTF-8&lt;/param-value&gt;<br/>
     * &lt;/context-param&gt;<br/><br/>
     *
     * Otherwise, the defaut is the VM's "<code>file.encoding</code>"
     * system parameter.
     *
     * @param templateURL template URL
     * @return charset encoding used to read the template
     * @throws IOException unable to read the template document
     */
    public String getCharacterEncoding(URL templateURL) throws IOException {

        InputStreamReader in = null;
        StringWriter snippet = new StringWriter();
        char[] chars = new char[512];
        String enc = null;


        try {
            // read in the top of the template
            in = new InputStreamReader(templateURL.openStream());
            int n = in.read(chars);
            snippet.write(chars, 0, n);

            // look for the comment page directive containing the charset
            int s = snippet.getBuffer().indexOf(Parser.START_CHARSET_TOKEN);
            if (s > -1) {
               int e = snippet.getBuffer().indexOf(Parser.END_CHARSET_TOKEN, s);
               AttributeTokenizer tokenizer = new AttributeTokenizer(snippet.getBuffer(), s, e, 1, 0);
               Iterator ti = tokenizer.iterator();
               while (ti.hasNext()) {
                   Map.Entry attribute = (Map.Entry) ti.next();
                   Token key = (Token) attribute.getKey();
                   //check the attribute name, we are only interested
                   //in "charset"
                   if (key != null && key.getRawText() != null
                       && key.getRawText().equalsIgnoreCase("charset")) {
                       Token value = (Token) attribute.getValue();

                       //look for the value of the charset attribute
                       if (value != null && value.getRawText() != null) {
                          // if it is supported, use the value for the encoding
                          if (Charset.isSupported(value.getRawText())) {
                              enc = value.getRawText();
                          } else {
                              log.error(messages.getMessage("template.encoding.notsupported",
                                      new Object[] { value.getRawText() }));
                          }
                       }
                   }
               }
            }


        } finally {
            if (in != null) {
               in.close();
            }
            if (snippet != null) {
               snippet.close();
            }
        }

        if (enc == null) {
            enc = getConfig().getServletContext().getInitParameter(Globals.CLAY_HTML_CHARSET);
            if (enc != null) {
               if (!Charset.isSupported(enc)) {
                  log.error(messages.getMessage("template.encoding.notsupported",
                           new Object[] { enc }));
                  enc = System.getProperty("file.encoding");
               }
            } else {
               enc = System.getProperty("file.encoding");
            }
        }

        return enc;
    }

    /**
     * <p>Returns the {@link org.apache.shale.clay.parser.builder.Builder} that
     * is assigned the task of converting the html node to a corresponding component
     * metadata used to construct a JSF resource.</p>
     *
     * @param node markup node
     * @return builder that maps markup to config beans
     */
    public Builder getBuilder(Node node) {
        return BuilderFactory.getRenderer(node);
    }

}
