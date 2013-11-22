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

package org.apache.shale.dialog.basic.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.shale.dialog.basic.model.Dialog;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>Configuration utility for parsing configuration resources for
 * defining dialogs.  This class has no dependencies on web tier APIs,
 * only on the parsing technology (Commons Digester) being used.</p>
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


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Registration information for the DTD we will use to validate.</p>
     */
    private static final String[] REGISTRATIONS =
    { "-//Apache Software Foundation//DTD Shale Dialog Configuration 1.0//EN",
      "/org/apache/shale/dialog/dialog-config_1_0.dtd",
      "-//Apache Software Foundation//DTD Shale Dialog Configuration 1.1//EN",
      "/org/apache/shale/dialog/dialog-config_1_1.dtd" };


    // -------------------------------------------------------------- Properties


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
     * @return The map of available {@link Dialog}s, keyed by name
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
     * @return The URL of the dialogs configuration resource
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
     * or not.  Default value is <code>true</code>.</p>
     *
     * @return Whether the parse is a validating one
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

        Digester digester = digester();
        digester.clear();
        digester.push(getDialogs());
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
     * <p>Return a fully configured <code>Digester</code> instance.</p>
     *
     * @return The fully configured {@link Digester} instance for parsing
     *         the dialog configuration resource
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
            digester.register(REGISTRATIONS[i], url.toString());
        }

        // Configure processing rules

        // dialogs/dialog
        digester.addObjectCreate("dialogs/dialog", "className", DialogImpl.class);
        digester.addSetProperties("dialogs/dialog");
        digester.addSetProperty("dialogs/dialog/property", "name", "value");
        digester.addRule("dialogs/dialog", new AddDialogRule());

        // dialogs/dialog/action
        digester.addObjectCreate("dialogs/dialog/action", "className",
                                 ActionStateImpl.class);
        digester.addSetProperties("dialogs/dialog/action");
        digester.addSetProperty("dialogs/dialog/action/property", "name", "value");
        digester.addSetNext("dialogs/dialog/action",
                            "addState", "org.apache.shale.dialog.basic.model.State");

        // dialogs/dialog/action/transition
        digester.addObjectCreate("dialogs/dialog/action/transition", "className",
                                 TransitionImpl.class);
        digester.addSetProperties("dialogs/dialog/action/transition");
        digester.addSetProperty("dialogs/dialog/action/transition/property", "name", "value");
        digester.addSetNext("dialogs/dialog/action/transition",
                            "addTransition", "org.apache.shale.dialog.basic.model.Transition");

        // dialogs/dialog/end
        digester.addObjectCreate("dialogs/dialog/end", "className",
                                 EndStateImpl.class);
        digester.addSetProperties("dialogs/dialog/end");
        digester.addSetProperty("dialogs/dialog/end/property", "name", "value");
        digester.addSetNext("dialogs/dialog/end",
                            "addState", "org.apache.shale.dialog.basic.model.State");

        // dialogs/dialog/end/transition
        digester.addObjectCreate("dialogs/dialog/end/transition", "className",
                                 TransitionImpl.class);
        digester.addSetProperties("dialogs/dialog/end/transition");
        digester.addSetProperty("dialogs/dialog/end/transition/property", "name", "value");
        digester.addSetNext("dialogs/dialog/end/transition",
                            "addTransition", "org.apache.shale.dialog.basic.model.Transition");

        // dialogs/dialog/subdialog
        digester.addObjectCreate("dialogs/dialog/subdialog", "className",
                                 SubdialogStateImpl.class);
        digester.addSetProperties("dialogs/dialog/subdialog");
        digester.addSetProperty("dialogs/dialog/subdialog/property", "name", "value");
        digester.addSetNext("dialogs/dialog/subdialog",
                            "addState", "org.apache.shale.dialog.basic.model.State");

        // dialogs/dialog/subdialog/transition
        digester.addObjectCreate("dialogs/dialog/subdialog/transition", "className",
                                 TransitionImpl.class);
        digester.addSetProperties("dialogs/dialog/subdialog/transition");
        digester.addSetProperty("dialogs/dialog/subdialog/transition/property", "name", "value");
        digester.addSetNext("dialogs/dialog/subdialog/transition",
                            "addTransition", "org.apache.shale.dialog.basic.model.Transition");

        // dialogs/dialog/transition
        digester.addObjectCreate("dialogs/dialog/transition", "className",
                                 TransitionImpl.class);
        digester.addSetProperties("dialogs/dialog/transition");
        digester.addSetProperty("dialogs/dialog/transition/property", "name", "value");
        digester.addSetNext("dialogs/dialog/transition",
                            "addTransition", "org.apache.shale.dialog.basic.model.Transition");

        // dialogs/dialog/view
        digester.addObjectCreate("dialogs/dialog/view", "className",
                                 ViewStateImpl.class);
        digester.addSetProperties("dialogs/dialog/view");
        digester.addSetProperty("dialogs/dialog/view/property", "name", "value");
        digester.addSetNext("dialogs/dialog/view",
                            "addState", "org.apache.shale.dialog.basic.model.State");

        // dialogs/dialog/view/transition
        digester.addObjectCreate("dialogs/dialog/view/transition", "className",
                                 TransitionImpl.class);
        digester.addSetProperties("dialogs/dialog/view/transition");
        digester.addSetProperty("dialogs/dialog/view/transition/property", "name", "value");
        digester.addSetNext("dialogs/dialog/view/transition",
                            "addTransition", "org.apache.shale.dialog.basic.model.Transition");

        return digester;

    }


    // -------------------------------------------- Private Rule Implementations


    /**
     * <p>Custom <code>Digester</code> rule to add a dialog.</p>
     */
    static class AddDialogRule extends Rule {

        /**
         * <p>Process the "end" event for this rule.</p>
         *
         * @param namespace XML namespace of this element
         * @param name Name of this element
         *
         * @exception Exception if a processing exception occurs
         */
        public void end(String namespace, String name) throws Exception {

            Dialog dialog = (Dialog) getDigester().peek();
            Map map = (Map) getDigester().peek(1);
            map.put(dialog.getName(), dialog);

        }

    }


}
