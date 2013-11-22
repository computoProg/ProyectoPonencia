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
 * $Id: ClayXmlParser.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.ComponentConfigBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.util.Messages;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * <p>This class loads the configuration files defining page fragments and
 * caches a graph of beans in application scope. The location of the default
 * configuration file is located at <code>Globals.DEFAULT_CLAY_CONFIG_FILE</code>.
 * A comma value list of names can be supplied as a  initialization parameter in
 * the web deployment descriptor using the parameter name
 * <code>Globals.CLAY_CONFIG_FILES</code>.
 * </p>
 */


public class ClayXmlParser  implements ClayConfigParser {

    /**
     * <p>The {@link ComponentConfigBean} is the container holding all of the
     * component metadata definitions read for the configuration files.
     * </p>
     */
    private ConfigBean config = null;

    /**
     * <p>The <code>Digester</code> makes short work of materalizing a XML document
     * into an object graph using simple binding rules.
     * </p>
     */
    private Digester digester;

    /**
     * <p>Commons logging utility object static instance.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(org.apache.shale.clay.config.ClayXmlParser.class);
    }

    /**
     * @return config {@link ConfigBean} instance of the component metadata container
     */
    public ConfigBean getConfig() {
        return config;
    }
    /**
     * @param config {@link ConfigBean} instance of the component metadata container
     */
    public void setConfig(ConfigBean config) {
        this.config = config;
    }

    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages = new Messages("org.apache.shale.clay.Bundle",
            ClayConfigureListener.class.getClassLoader());

    /**
     * <p>A static array of local DTD's to validate the digested documents against when not
     * connected to the internet.
     * </p>
     */
    private Object[] registrations = {
        new String[] {"-//Apache Software Foundation//DTD Shale Clay View Configuration 1.0//EN",
                      "/META-INF/clay-config_1_0.dtd"}};

    /**
     * <p>Loads a configuration file from a <code>url</code>.  The
     * input stream is identifed by the <code>watchDogName</code>.</p>
     *
     * @param configURL url of the target configuration file
     * @param watchDogName an id used to group files that need to be reloaded together
     * @throws IOException raised by the digester processing the configUrl
     * @throws SAXException raised by the digester processing the XML config file
     */
    public void loadConfigFile(URL configURL, String watchDogName) throws IOException, SAXException {

        if (digester == null) {
            digester = new Digester();
            digester.setLogger(log);
            digester.setValidating(true);
            digester.setUseContextClassLoader(true);

            // Register our local copy of the DTDs that we can find
            for (int i = 0; i < registrations.length; i++) {
                URL url = this.getClass().getResource(((String[]) registrations[i])[1]);
                if (url != null) {
                    digester.register(((String[]) registrations[i])[0], url.toString());
                }
            }

            configureRules();
            digester.push(config);
        } else {
            digester.clear();
            digester.push(config);
        }

        InputStream in = null;
        InputSource inputSource = null;
        try {
            in = configURL.openStream();
            inputSource = new InputSource(configURL.toExternalForm());
            inputSource.setByteStream(in);
            digester.parse(inputSource);
        } finally {
           if (in != null) {
              in.close();
           }
        }
        inputSource = null;

    }

        /**
         * <p>This method is called once to register the object binding rules with
         * the <code>Digester</code> instance.
         *</p>
         */
        protected void configureRules() {

            if (log.isInfoEnabled()) {
                log.info(messages.getMessage("parser.load.rules"));
            }

            if (getConfig() instanceof ComponentConfigBean) {
                if (((ComponentConfigBean) getConfig()).isDesigntime()) {
                    digester.addBeanPropertySetter("*/description", "description");
                }
            }

            digester.addObjectCreate(
                    "*/attributes/set",
                    org.apache.shale.clay.config.beans.AttributeBean.class);
            digester.addSetProperties("*/attributes/set");
            digester.addSetNext(
                    "*/attributes/set",
                    "addAttribute",
                    "org.apache.shale.clay.config.beans.AttributeBean");

            digester.addObjectCreate(
                    "*/symbols/set",
                    org.apache.shale.clay.config.beans.SymbolBean.class);
            digester.addSetProperties("*/symbols/set");
            digester.addSetNext(
                    "*/symbols/set",
                    "addSymbol",
                    "org.apache.shale.clay.config.beans.SymbolBean");

            digester.addObjectCreate(
                    "*/valueChangeListener",
                    org.apache.shale.clay.config.beans.ValueChangeListenerBean.class);
            digester.addSetProperties(
                    "*/valueChangeListener");
            digester.addSetNext(
                    "*/valueChangeListener",
                    "addValueChangeListener",
                    "org.apache.shale.clay.config.beans.ValueChangeListenerBean");

            digester.addObjectCreate(
                    "*/actionListener",
                    org.apache.shale.clay.config.beans.ActionListenerBean.class);
            digester.addSetProperties("*/actionListener");
            digester.addSetNext(
                    "*/actionListener",
                    "addActionListener",
                    "org.apache.shale.clay.config.beans.ActionListenerBean");

            digester.addObjectCreate(
                    "*/validator",
                    org.apache.shale.clay.config.beans.ValidatorBean.class);
            digester.addSetProperties("*/validator");
            digester.addSetNext(
                    "*/validator",
                    "addValidator",
                    "org.apache.shale.clay.config.beans.ValidatorBean");

            digester.addObjectCreate(
                    "*/converter",
                    org.apache.shale.clay.config.beans.ConverterBean.class);
            digester.addSetProperties("*/converter");
            digester.addSetNext(
                    "*/converter",
                    "addConverter",
                    "org.apache.shale.clay.config.beans.ConverterBean");

            digester.addObjectCreate(
                    "*/element",
                    org.apache.shale.clay.config.beans.ElementBean.class);
            digester.addSetProperties("*/element");
            digester.addSetNext(
                    "*/element",
                    "addChild",
                    "org.apache.shale.clay.config.beans.ElementBean");

            digester.addObjectCreate(
                    "view/component",
                    org.apache.shale.clay.config.beans.ComponentBean.class);
            digester.addSetProperties("view/component");
            digester.addSetNext(
                    "view/component",
                    "addChild",
                    "org.apache.shale.clay.config.beans.ComponentBean");

        }
}

