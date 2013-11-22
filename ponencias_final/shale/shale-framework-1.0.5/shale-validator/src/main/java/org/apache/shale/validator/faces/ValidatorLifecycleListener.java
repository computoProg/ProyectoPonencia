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

package org.apache.shale.validator.faces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.FacesException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.shale.validator.Globals;
import org.apache.shale.validator.util.ShaleValidatorAction;
import org.xml.sax.SAXException;

/**
 * <p>ServletContextListener that loads validator configuration resources
 * at application startup, and cleans up the libraries we depend on at
 * application shutdown.</p>
 */
public class ValidatorLifecycleListener implements ServletContextListener {
    

    // -------------------------------------------------------- Static Variables


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Log</code> instance we will use for this listener.</p>
     */
    private Log log = LogFactory.getLog(ValidatorLifecycleListener.class);


    // ------------------------------------------ ServletContextListener Methods


    /**
     * <p>Process an application shutdown event.</p>
     *
     * @param event Shutdown event to be processed
     */
    public void contextDestroyed(ServletContextEvent event) {

        if (log.isInfoEnabled()) {
            log.info("Finalizing Validator Integration");
        }

        // Clean up our cached configuration information
        event.getServletContext().removeAttribute(Globals.VALIDATOR_ACTIONS);
        event.getServletContext().removeAttribute(Globals.VALIDATOR_RESOURCES);

        // Clean up dependency libraries we have used
        PropertyUtils.clearDescriptors(); // Used by Commons Validator via Digester
        LogFactory.release(Thread.currentThread().getContextClassLoader());
        log = null;

    }


    /**
     * <p>Process an application startup event.</p>
     *
     * @param event Startup event to be processed
     */
    public void contextInitialized(ServletContextEvent event) {

        if (log.isInfoEnabled()) {
            log.info("Initializing Validator Integration");
        }

        // Configure and cache the validator resources for this application
        ServletContext context = event.getServletContext();
        ValidatorResources resources = null;
        try {
            resources = validatorResources(context);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException e) {
            throw new FacesException(e);
        } catch (SAXException e) {
            throw new FacesException(e);
        }
        context.setAttribute(Globals.VALIDATOR_RESOURCES, resources);

        // Configure and cache precalculated arrays of validator actions
        // to be performed at runtime
        Map actions = validatorActions(resources);
        context.setAttribute(Globals.VALIDATOR_ACTIONS, actions);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Configure precalcualted lists of validator actions that will be
     * used to perform server side validation processing at runtime.</p>
     *
     * @param resources <code>ValidatorResources</code> for this application
     *
     * @exception IllegalArgumentException if the configuration resources
     *  specify invalid validator types, or classes or methods that
     *  cannot be loaded
     */
    private Map validatorActions(ValidatorResources resources) {

        // Use the validator resources to precalculate a map of
        // ShaleValidatorAction wrappers for the Commons Validator
        // ValidatorAction configuration information
        Map map = new HashMap();
        Iterator entries = resources.getValidatorActions().entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if ("includeJavaScriptUtilities".equals(entry.getKey())) {
                continue;
            }
            map.put((String) entry.getKey(),
                    new ShaleValidatorAction(resources,
                                             (ValidatorAction) entry.getValue()));
        }

        // Next, create arrays of the ShaleValidatorAction instances to be
        // processed for each validator type, taking into account the
        // configured dependencies that must also be tested
        ShaleValidatorAction[] result = null;
        Map results = new HashMap();
        List list = null;
        Iterator actions = map.entrySet().iterator();
        while (actions.hasNext()) {
            Map.Entry action = (Map.Entry) actions.next();
            list =
              ((ShaleValidatorAction) action.getValue()).getAction().getDependencyList();
            if (list == null) {
                list = new ArrayList(0);
            }
            result = new ShaleValidatorAction[list.size() + 1];
            for (int i = 0; i < list.size(); i++) {
                result[i] = (ShaleValidatorAction) map.get((String) list.get(i));
                if (result[i] == null) {
                    throw new IllegalArgumentException((String) action.getKey());
                }
            }
            result[result.length - 1] = (ShaleValidatorAction) action.getValue();
            results.put((String) action.getKey(), result);
        }

        // Return the map of created arrays
        return results;

    }


    /**
     * <p>Configure the validator resources to be used by this application,
     * by reading the list of resources configured on the context init
     * parameter named by <code>Globals.VALIDATOR_RULES</code> (if any),
     * followed by reading the default resource named by
     * <code>Globals.DEFAULT_VALIDATOR_RULES</code> (if it has not already
     * been processed).</p>
     *
     * @param context <code>ServletContext</code> for this application
     *
     * @exception IllegalArgumentException if a specified resource cannot
     *  be located, or if a malformed URL is constructed from the
     *  specified resource name
     * @exception IOException if an input/output error occurs while
     *  processing the specified configuration resources
     * @exception SAXException if an XML parsing error occurs while
     *  processing the specified configuration resources
     */
    private ValidatorResources validatorResources(ServletContext context)
      throws IOException, SAXException {

        // Process the explicitly configured resources (if any)
        List urls = new ArrayList();
        URL url = null;
        boolean didDefault = false;
        String pathnames = context.getInitParameter(Globals.VALIDATOR_RULES);
        if (pathnames != null) {
            pathnames = pathnames.trim();
            while (pathnames.length() > 0) {

                // Identify the next resource pathname to be processed
                int comma = pathnames.indexOf(',');
                String pathname = null;
                if (comma >= 0) {
                    pathname = pathnames.substring(0, comma).trim();
                    pathnames = pathnames.substring(comma + 1);
                } else {
                    pathname = pathnames.trim();
                    pathnames = "";
                }
                if (pathname.length() < 1) {
                    break;
                }

                // Add the corresponding URL to our list
                try {
                    url = context.getResource(pathname);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("MalformedURLException:"
                        + " The URL '" + pathname
                        + "' specified as a validator rules resource is malformed.");
                }
                if (url == null) {
                    url = ValidatorLifecycleListener.class.getResource(pathname);
                }
                if (url == null) {
                    throw new IllegalArgumentException(pathname);
                }
                urls.add(url);
                if (Globals.DEFAULT_VALIDATOR_RULES.equals(pathname)) {
                    didDefault = true;
                }

            }
        }

        // Process the default configuration resources (if not already loaded)
        if (!didDefault) {
            try {
                url = context.getResource(Globals.DEFAULT_VALIDATOR_RULES);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("MalformedURLException:"
                        + " The URL '" + Globals.DEFAULT_VALIDATOR_RULES
                        + "' specified as a validator rules resource is malformed.");
            }
            if (url == null) {
                url = ValidatorLifecycleListener.class.getResource(Globals.DEFAULT_VALIDATOR_RULES);
            }
            if (url == null) {
                throw new IllegalArgumentException(Globals.DEFAULT_VALIDATOR_RULES);
            }
            urls.add(url);
        }

        // Until <http://issues.apache.org/jira/browse/VALIDATOR-209>
        // is addressed, we must convert the URLs we have gathered back into
        // Strings and hope no information is lost in the process
        String[] array = new String[urls.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = ((URL) urls.get(i)).toExternalForm();
        }
        
        // Construct and return a new ValidatorResources instance
        return new ValidatorResources(array);

    }


}
