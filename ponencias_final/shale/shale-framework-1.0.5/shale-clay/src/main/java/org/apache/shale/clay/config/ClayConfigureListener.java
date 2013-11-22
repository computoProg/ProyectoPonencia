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
 * $Id: ClayConfigureListener.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.beans.ComponentConfigBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.TemplateComponentConfigBean;
import org.apache.shale.clay.config.beans.TemplateConfigBean;
import org.apache.shale.util.Messages;

/**
 * <p>This context listener is responsible for loading the clay
 * configuration files on application startup.  It is registered
 * in our tag library descriptor, so no further registration is
 * required.  The configuration files are defined by a context
 * initialization parameter named <code>Globals.CLAY_CONFIG_FILES</code>.
 * </p>
 *
 * <p>After the data is loaded, it will be accessible using the factory class
 * {@link org.apache.shale.clay.config.beans.ConfigBeanFactory}</p>
 */

public class ClayConfigureListener implements ServletContextListener {

    /**
     * <p>Static instanc reference to the common logger utility class.</p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(ClayConfigureListener.class);
    }

    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages = new Messages("org.apache.shale.clay.Bundle",
            ClayConfigureListener.class.getClassLoader());

    /**
     * <p>Loads the configuration files on startup into an instance of
     * {@link org.apache.shale.clay.config.beans.ComponentConfigBean}.
     * and registers it with the factory {@link ConfigBeanFactory}.  The HTML template style of
     * {@link org.apache.shale.clay.component.Clay} configuration bean
     * {@link org.apache.shale.clay.config.beans.TemplateConfigBean} and the XML template style of configuration
     * bean is {@link org.apache.shale.clay.config.beans.TemplateComponentConfigBean}.  The are all registered
     * with the factory {@link ConfigBeanFactory}.  All configuration bean pools implement the
     * {@link org.apache.shale.clay.config.beans.ConfigBean} interface.
     * </p>
     * @param event servlet context
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("config.load.begin"));
        }

        try {

            // load xml config
            ConfigBean config = new ComponentConfigBean();
            config.init(event.getServletContext());
            ConfigBeanFactory.register(config);

            // load HTML template config
            config = new TemplateConfigBean();
            config.init(event.getServletContext());
            ConfigBeanFactory.register(config);

            // load XML template config
            config = new TemplateComponentConfigBean();
            config.init(event.getServletContext());
            ConfigBeanFactory.register(config);



        } catch (RuntimeException e) {
            log.error(messages.getMessage("config.load.error"), e);
            throw e;
        }

        if (log.isInfoEnabled()) {
            log.info(messages.getMessage("config.load.done"));
        }

    }

    /**
     * <p>Tear down the factories and clean house.</p>
     * @param event servlet context
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {

        if (log.isInfoEnabled()) {
            log.debug(messages.getMessage("config.destroy"));
        }

        ConfigBeanFactory.destroy();
        LogFactory.release(Thread.currentThread().getContextClassLoader());

    }

}
