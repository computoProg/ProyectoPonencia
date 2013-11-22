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

package org.apache.shale.dialog.scxml;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.dialog.scxml.config.ConfigurationParser;

/**
 * <p>ServletContextListener that loads dialog configuration resources
 * at application startup, and cleans up the libraries we depend on at
 * application shutdown.</p>
 *
 * $Id: SCXMLLifecycleListener.java 476713 2006-11-19 05:10:57Z rahul $
 *
 * @since 1.0.4
 */
public class SCXMLLifecycleListener implements ServletContextListener {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The default configuration resource we will process (if present),
     * even if it is not explicitly configured.</p>
     */
    private static final String DEFAULT_CONFIGURATION_RESOURCE =
            "/WEB-INF/dialog-config.xml";


    /**
     * <p>Resource name for dialog configuration resource(s) embedded in
     * JAR files inside the application.</p>
     */
    private static final String EMBEDDED_CONFIGURATION_RESOURCE =
            "META-INF/dialog-config.xml";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Log</code> instance we will use for this listener.</p>
     */
    private Log log = LogFactory.getLog(SCXMLLifecycleListener.class);


    // ------------------------------------------ ServletContextListener Methods


    /**
     * <p>Process the application shutdown event, cleanup resources.</p>
     *
     * @param event Shutdown event to be processed
     */
    public void contextDestroyed(ServletContextEvent event) {

        if (log.isInfoEnabled()) {
            log.info("Finalizing Dialog SCXML Implementation");
        }

        // Clean up our cache of dialog configuration information
        event.getServletContext().removeAttribute(Globals.DIALOGS);

        // Clean up dependency libraries we have used
        PropertyUtils.clearDescriptors();
        LogFactory.release(Thread.currentThread().getContextClassLoader());
        log = null;

    }


    /**
     * <p>Process the application startup event, parse dialog
     * configurations.</p>
     *
     * @param event Startup event to be processed
     */
    public void contextInitialized(ServletContextEvent event) {

        if (log.isInfoEnabled()) {
            log.info("Initializing Dialog SCXML Implementation");
        }

        // Set up to parse our specified configuration resources and cache the results
        boolean didDefault = false;
        ConfigurationParser parser = new ConfigurationParser();
        parser.setDialogs(new HashMap());

        // Parse implicitly specified resources embedded in JAR files
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = this.getClass().getClassLoader();
        }
        try {
            Enumeration resources = loader.getResources(EMBEDDED_CONFIGURATION_RESOURCE);
            while (resources.hasMoreElements()) {
                URL resource = (URL) resources.nextElement();
                if (log.isDebugEnabled()) {
                    log.debug("Parsing configuration resource '"
                            + resource + "'");
                }
                parser.setResource(resource);
                parser.parse();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Parse explicitly specified resources
        String resources =
          event.getServletContext().getInitParameter(Globals.CONFIGURATION);
        if (resources == null) {
            resources = "";
        }
        int comma = 0;
        String path = null;
        try {
            while (true) {
                comma = resources.indexOf(",");
                if (comma < 0) {
                    path = resources.trim();
                    resources = "";
                } else {
                    path = resources.substring(0, comma).trim();
                    resources = resources.substring(comma + 1);
                }
                if (path.length() < 1) {
                    break;
                }
                URL resource = event.getServletContext().getResource(path);
                if (log.isDebugEnabled()) {
                    log.debug("Parsing configuration resource '"
                            + resource + "'");
                }
                parser.setResource(resource);
                parser.parse();
                if (DEFAULT_CONFIGURATION_RESOURCE.equals(path)) {
                    didDefault = true;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Parse the default configuration resource if it exists and has not
        // already been parsed
        if (!didDefault) {
            try {
                URL resource =
                  event.getServletContext().getResource(DEFAULT_CONFIGURATION_RESOURCE);
                if (resource != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Parsing configuration resource '"
                                + resource + "'");
                    }
                    parser.setResource(resource);
                    parser.parse();
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        // Cache the results in application scope
        Map dialogs = parser.getDialogs();

        if (dialogs.size() == 0) {
            if (log.isWarnEnabled()) {
                log.warn("No dialog configuration information present.  No default configuration found at: "
                    + DEFAULT_CONFIGURATION_RESOURCE + ".  No embedded configuration found at: "
                    + EMBEDDED_CONFIGURATION_RESOURCE);
            }
        }
        event.getServletContext().setAttribute(Globals.DIALOGS, dialogs);

    }


}
