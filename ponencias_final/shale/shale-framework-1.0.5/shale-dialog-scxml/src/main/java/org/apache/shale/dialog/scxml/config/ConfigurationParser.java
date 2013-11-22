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

package org.apache.shale.dialog.scxml.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.scxml.env.SimpleErrorHandler;
import org.apache.commons.scxml.io.SCXMLDigester;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.SCXML;
import org.apache.shale.dialog.scxml.Globals;
import org.apache.shale.dialog.scxml.action.RedirectAction;
import org.apache.shale.dialog.scxml.action.ViewAction;
import org.apache.shale.dialog.scxml.config.DialogMetadata.SCXMLAction;
import org.xml.sax.SAXException;

/**
 * <p>Configuration utility for parsing SCXML documents as resources for
 * defining dialogs.  This class has no dependencies on web tier APIs,
 * only on the Commons SCXML state machine engine library, and
 * on the parsing technology (Commons Digester) being used.</p>
 *
 * <p>The configuration for each Shale dialog exists as a standalone
 * SCXML document, with additional dialog "metadata" file(s)
 * that serve as the entry point for the Shale Dialog Manager.</p>
 *
 * <p>These dialog-config.xml file(s) look like this:
 * <pre>
 * &lt;dialogs&gt;
 *     &lt;dialog name="Foo" scxmlconfig="foo.scxml" /&gt;
 *     &lt;dialog name="Bar" scxmlconfig="bar.scxml"
 *             dataclassname="org.apache.shale.examples.Bar" /&gt;
 *     &lt;dialog name="Baz" scxmlconfig="baz.scxml" /&gt;
 *     &lt;!-- etc. --&gt;
 * &lt;/dialogs&gt;
 * </pre>
 * </p>
 *
 * <p>To use this utility, instantiate a new instance and set the
 * <code>dialogs</code>, <code>resource</code>, and <code>validating</code>
 * properties.  Then, call the <code>parse()</code> method.  You can parse
 * more than one resource by resetting the <code>resource</code>
 * property and calling <code>parse()</code> again.</p>
 *
 * @since 1.0.4
 */

public final class ConfigurationParser {


    // -------------------------------------------------------------- Properties

    /**
     * <p>Registration information for the DTD we will use to validate.</p>
     */
    private static final String[] REGISTRATIONS =
    { "-//Apache Software Foundation//DTD Shale SCXML Dialog Configuration 1.0//EN",
      "/org/apache/shale/dialog/scxml/dialog-scxml-config_1_0.dtd" };


    /**
     * <p><code>Map</code> of <code>Dialog</code> instances resulting
     * from parsing, keyed by dialog name.</p>
     */
    private Map dialogs = null;


    /**
     * <p>Return the <code>Map</code> of <code>Dialog</code> instances
     * into which parsed information will be stored, keyed by dialog
     * name.</p>
     *
     * @return Map of SCXML instances, keyed by logical dialog name
     */
    public Map getDialogs() {
        return this.dialogs;
    }


    /**
     * <p>Set the <code>Map</code> of <code>Dialog</code> instances
     * into which parsed information will be stored, keyed by dialog
     * name.</p>
     *
     * @param dialogs The new map
     */
    public void setDialogs(Map dialogs) {
        this.dialogs = dialogs;
    }


    /**
     * <p>The URL of the configuration resource to be parsed.</p>
     */
    private URL resource = null;


    /**
     * <p>Return the URL of the configuration resource to be parsed.</p>
     *
     * @return The resource URL
     */
    public URL getResource() {
        return this.resource;
    }


    /**
     * <p>Set the URL of the configuration resource to be parsed.</p>
     *
     * @param resource The new resource URL
     */
    public void setResource(URL resource) {
        this.resource = resource;
    }


    /**
     * <p>Flag indicating whether we should do a validating parse or not.</p>
     */
    private boolean validating = true;


    /**
     * <p>Return a flag indicating whether we will be doing a validating parse
     * or not.  Default value is <code>false</code>.</p>
     *
     * @return Whether the parse is validating
     */
    public boolean isValidating() {
        return this.validating;
    }


    /**
     * <p>Set a flag indicating whether we will be doing a validating parse
     * or not.</p>
     *
     * @param validating New flag value
     */
    public void setValidating(boolean validating) {
        this.validating = validating;
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Parse the configuration resource identified by the <code>resource</code>
     * property, storing resulting information in the <code>Map</code> specified
     * by the <code>dialogs</code> property.</p>
     *
     * @exception IOException if an input/output error occurs
     * @exception SAXException if an XML parsing error occurs
     */
    public void parse() throws IOException, SAXException {

        Map metadata = new HashMap();
        Digester digester = digester();
        digester.clear();
        digester.push(metadata);
        digester.parse(getResource());

        parseDialogs(metadata);
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return a fully configured <code>Digester</code> instance.</p>
     *
     * @return The configuration parser Digester instance
     */
    private Digester digester() {

        Digester digester = new Digester();

        // Configure global characteristics
        digester.setNamespaceAware(false);
        digester.setUseContextClassLoader(true);
        digester.setValidating(isValidating());

        // Register local copy of our DTDs
        for (int i = 0; i < REGISTRATIONS.length; i += 2) {
            URL url = this.getClass().getResource(REGISTRATIONS[i + 1]);
            digester.register(REGISTRATIONS[i], url);
        }

        // Configure processing rules

        // dialogs/dialog
        digester.addObjectCreate("dialogs/dialog", DialogMetadata.class);
        digester.addSetProperties("dialogs/dialog");
        digester.addRule("dialogs/dialog", new AddDialogMetadataRule());

        digester.addObjectCreate("dialogs/dialog/scxmlaction", SCXMLAction.class);
        digester.addSetProperties("dialogs/dialog/scxmlaction");
        digester.addSetNext("dialogs/dialog/scxmlaction", "addDialogAction");

        return digester;

    }


    /**
     * <p>Parse the SCXML documents in the dialog metadata, storing resulting
     * information as an entry in the <code>Map</code> specified by the
     * <code>dialogs</code> property.</p>
     *
     * @param metadata The metadata map
     * @throws IOException if an input/output error occurs
     * @throws SAXException if an XML parsing error occurs
     */
    private void parseDialogs(Map metadata) throws IOException, SAXException {

        Iterator iterator = metadata.entrySet().iterator();

        // Create a list of the custom Commons SCXML actions defined by the
        // Shale dialog Commons SCXML implementation
        List shaleDialogActions = new ArrayList();

        // <shale:redirect>
        CustomAction redirectAction =
            new CustomAction(Globals.CUSTOM_SCXML_ACTIONS_URI,
                "redirect", RedirectAction.class);
        shaleDialogActions.add(redirectAction);

        // <shale:view>
        CustomAction viewAction =
            new CustomAction(Globals.CUSTOM_SCXML_ACTIONS_URI,
                "view", ViewAction.class);
        shaleDialogActions.add(viewAction);

        // Class loader for app developer defined custom Commons SCXML actions
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ConfigurationParser.class.getClassLoader();
        }

        while (iterator.hasNext()) {

                Map.Entry entry = (Map.Entry) iterator.next();
                String name = (String) entry.getKey();
                DialogMetadata dMetadata = (DialogMetadata) entry.getValue();
                String scxmlconfig = dMetadata.getScxmlconfig();

                // The custom actions available to this dialog is the
                // summation of the ones defined by this Shale dialog module
                // and those defined by the app developer using the dialog
                // configuration file for this dialog
                List customDialogActions = new ArrayList();
                customDialogActions.addAll(shaleDialogActions);

                List devActions = dMetadata.getDialogActions();
                for (int i = 0; i < devActions.size(); i++) {
                    SCXMLAction scxmlAction = (SCXMLAction) devActions.get(i);
                    String actionname = scxmlAction.getName();
                    String uri = scxmlAction.getUri();
                    String actionFQCN = scxmlAction.getActionclassname();
                    if (actionname == null || uri == null || actionFQCN == null) {
                        // shouldn't happen if dialog-config is validated
                        throw new IllegalArgumentException("A custom Commons"
                            + " SCXML action (<scxmlaction> element) in the"
                            + " dialog configuration is missing the 'name',"
                            + " 'uri' or 'actionclassname'");
                    }
                    Class customActionClass = null;
                    try {
                        customActionClass = loader.loadClass(actionFQCN);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Cannot load "
                            + "custom Commons SCXML action class '"
                            + actionFQCN + "' for action with name '"
                            + actionname + "'");
                    }
                    CustomAction customAction = new CustomAction(uri,
                        actionname, customActionClass);
                    customDialogActions.add(customAction);
                }

                URL resource = new URL(getResource(), scxmlconfig);

                SCXML dialog = null;
                try {
                    // Parse document, with rules for custom actions in place
                    dialog = SCXMLDigester.digest(resource,
                        new SimpleErrorHandler(), customDialogActions);
                } catch (ModelException me) {
                    throw new SAXException(me.getMessage(), me);
                }

                dMetadata.setStateMachine(dialog);
                dialogs.put(name, dMetadata);

        }

    }


    // -------------------------------------------- Private Rule Implementations


    /**
     * <p>Custom <code>Digester</code> rule to add a dialog.</p>
     */
    static class AddDialogMetadataRule extends Rule {

        /**
         * Constructor.
         */
        public AddDialogMetadataRule() {
            super();
        }

        /**
         * {@inheritDoc}
         *
         * @see Rule#end(String,String)
         */
        public void end(String namespace, String name) throws Exception {

            DialogMetadata dialog = (DialogMetadata) getDigester().peek();
            Map map = (Map) getDigester().peek(1);
            map.put(dialog.getName(), dialog);

        }

    }

}

