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

package org.apache.shale.tiger.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.shale.tiger.managed.rules.ManagedBeansRuleSet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Parser utility for processing <code>faces-config.xml</code> resources.
 * Information parsed by each call to the <code>parse()</code> method is
 * merged with any previous information stored in the specified
 * {@link FacesConfigConfig} bean.</p>
 *
 * <p>To use this utility, instantiate a new instance and set the
 * <code>facesConfig</code>, <code>resource</code>, and <code>validating</code>
 * properties.  Then, call the <code>parse()</code> method.  You can parse
 * more than one resource by resetting the <code>resource</code> property
 * and calling <code>parse()</code> again.</p>
 */
public class FacesConfigParser {

    // -------------------------------------------------------- Static Constants


    /**
     * <p>Registration information for the DTD used to validate the specified
     * configuration resources.</p>
     */
    private static final String[] REGISTRATIONS =
    { "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.0//EN",
      "/org/apache/shale/tiger/resources/web-facesconfig_1_0.dtd",
      "-//Sun Microsystems, Inc.//DTD JavaServer Faces Config 1.1//EN",
      "/org/apache/shale/tiger/resources/web-facesconfig_1_1.dtd",
    };


    // -------------------------------------------------------------- Properties


    /**
     * <p>Configuration resource summarizing all of the configuration metadata
     * from resources that have been parsed so far.</p>
     */
    private FacesConfigConfig facesConfig = null;


    /**
     * <p>Return the {@link FacesConfigConfig} bean summarizing all of the
     * configuration metadata from resources that have been parsed so far.</p>
     */
    public FacesConfigConfig getFacesConfig() {
        return this.facesConfig;
    }


    /**
     * <p>Set the {@link FacesConfigConfig} bean that will summarize all of the
     * configuration metadata from parsed resources.</p>
     *
     * @param facesConfig New {@link FacesConfigConfig} instance to be populated
     */
    public void setFacesConfig(FacesConfigConfig facesConfig) {
        this.facesConfig = facesConfig;
    }


    /**
     * <p>The URL of the configuration resource to be parsed.</p>
     */
    private URL resource = null;


    /**
     * <p>Return the URL of the configuration resource to be parsed.</p>
     */
    public URL getResource() {
        return this.resource;
    }


    /**
     * <p>Set the URL of the configuration resource to be parsed.</p>
     *
     * @param resource The new configuration resource URL
     */
    public void setResource(URL resource) {
        this.resource = resource;
    }


    /**
     * <p>Flag indicating whether we should do a validating parse.</p>
     */
    private boolean validating = true;


    /**
     * <p>Return the validating parse flag.</p>
     */
    public boolean isValidating() {
        return this.validating;
    }


    /**
     * <p>Set the validating parse flag.</p>
     *
     * @param validating The new validating parse flag
     */
    public void setValidating(boolean validating) {
        this.validating = validating;
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Parse the configuration resource specified by the <code>resource</code>
     * property, storing resulting information in the configuration bean
     * specified by the <code>facesConfig</code> property.</p>
     *
     * @exception IOException if an input/output error occurs
     * @exception SAXException if an XML parsing error occurs
     */
    public void parse() throws IOException, SAXException {

        Digester digester = digester();
        digester.clear();
        digester.push(getFacesConfig());
        InputSource source = new InputSource(getResource().toExternalForm());
        InputStream stream = null;
        try {
            stream = getResource().openStream();
            source.setByteStream(stream);
            digester.parse(source);
        } catch (IOException e) {
            throw e;
        } catch (SAXException e) {
            throw e;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    ; // Fall through
                }
            }
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Configured <code>Digester</code> instance to be used (lazily
     * instantiated.</p>
     */
    private Digester digester = null;


    /**
     * <p>Return a fully configured <code>Digester</code> instance.</p>
     */
    private Digester digester() {

        // Return any previously created instance
        if (digester != null) {
            return digester;
        }

        // Create and configure a new Digester instance
        digester = new Digester();
        digester.setNamespaceAware(false);
        digester.setUseContextClassLoader(true);
        digester.setValidating(isValidating());

        // Register our local copy of DTDs we recognize
        for (int i = 0; i < REGISTRATIONS.length; i += 2) {
            URL url = this.getClass().getResource(REGISTRATIONS[i + 1]);
            digester.register(REGISTRATIONS[i], url.toString());
        }

        // Configure processing rules
        RuleSet ruleSet = null;
        ruleSet = new ManagedBeansRuleSet();
        ruleSet.addRuleInstances(digester);

        // Return the configured instance
        return digester;

    }


}
