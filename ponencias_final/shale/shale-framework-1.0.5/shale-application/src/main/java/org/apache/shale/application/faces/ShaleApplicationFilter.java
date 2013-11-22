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

package org.apache.shale.application.faces;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogBase;
import org.apache.commons.chain.web.WebContext;
import org.apache.commons.chain.web.servlet.ServletWebContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.util.Messages;


/**
 * <p>{@link ShaleApplicationFilter} is a <code>Filter</code> implementation
 * that invokes the required <em>Application Controller</em> functionality on
 * every request.
 * In addition, it performs overall application startup and shutdown
 * operations on behalf of the framework.</p>
 *
 * <p>The detailed processing to be performed for each request is configured
 * by a <code>Command</code> or <code>Chain</code> defined using the "Chain of
 * Resposibility" design pattern, as implemented by the Commons Chain package.
 * There must exist a <code>Catalog</code> named <code>shale</code>, which
 * contains a <code>Command</code> named <code>standard</code>, that defines
 * the processing to be performed.</p>
 *
 * <p>At any point, one of the <code>Command</code>s being executed may choose
 * to complete the response itself (such as to perform an HTTP redirect),
 * instead of allowing processing to continue.  To indicate this choice, the
 * <code>Command</code> should follow the standard Commons Chain convention of
 * returning <code>true</code>.  If you want processing to continue, return
 * <code>false</code> instead.</p>
 *
 * <p>The default implementation of the standard command processing chain
 * performs the following tasks:</p>
 * <ul>
 * <li>Invoke a command named <code>preprocess</code> (in the <code>shale</code>
 *     catalog), if it exists.  This is where you should insert commands to be
 *     executed <strong>before</strong> {@link ShaleApplicationFilter} passes the
 *     request on to the next filter or servlet.</li>
 * <li>Execute the remainder of the filter chain for this request.</li>
 * <li>Invokes a command named <code>postprocess</code> (in the <code>shale</code>
 *     catalog), if it exists.  This is where you should insert commands to be
 *     executed <strong>after</strong> control returns from the invoked filter or
 *     servlet.  Note that it is no longer possible, at this point, to replace
 *     the response content produced by the filter or servlet -- that should
 *     be done in a preprocess step.</li>
 * </ul>
 *
 * <p><strong>NOTE</strong> - Configuration of the <code>shale</code> catalog,
 * and the commands it contains, may be performed in any manner you desire.
 * One convenient mechanism is to use the <code>ChainListener</code> class
 * that is included in the Commons Chain package.  If you do not reconfigure it
 * differently, the <code>standard</code> command (in the <code>shale</code>
 * catalog) will be configured according to the embedded resource
 * <code>org/apache/shale/faces/shale-config.xml</code> in the JAR file
 * containing the core Shale runtime environment, which executes the default
 * request processing described above.</p>
 *
 * $Id: ShaleApplicationFilter.java 464373 2006-10-16 04:21:54Z rahul $
 */

public class ShaleApplicationFilter implements Filter {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The name of the Commons Chain <code>Catalog</code> to use.</p>
     */
    public static final String CATALOG_NAME = "shale";


    /**
     * <p>The name of the <code>Command</code> to execute during
     * application shutdown.</p>
     */
    public static final String COMMAND_DESTROY = "destroy";


    /**
     * <p>The name of the <code>Command</code> to execute during
     * application startup.</p>
     */
    public static final String COMMAND_INIT = "init";


    /**
     * <p>The name of the <code>Command</code> to execute before
     * the application logic itself is invoked.</p>
     */
    public static final String COMMAND_PREPROCESS = "preprocess";


    /**
     * <p>The name of the <code>Command</code> to execute after
     * the application logic itself is invoked.</p>
     */
    public static final String COMMAND_POSTPROCESS = "postprocess";


    /**
     * <p>The request scope attribute key under which the <code>Context</code>
     * object used for this chain of command request to be stored, in addition
     * to it being passed in to the command chains.</p>
     */
    public static final String CONTEXT_ATTR =
      "org.apache.shale.CONTEXT_ATTR";


    /**
     * <p>The name of the internal resource containing our default
     * configuration of the default command.</p>
     */
    public static final String RESOURCE_NAME =
      "org/apache/shale/application/faces/shale-config.xml";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Chain of Responsibility <code>Catalog</code> we will be using.</p>
     */
    private Catalog catalog = null;


    /**
     * <p>The <code>ServletContext</code> instance for our web application.</p>
     */
    private ServletContext context = null;


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private transient Log log = null;


    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages =
      new Messages("org.apache.shale.resources.Bundle",
                   ShaleApplicationFilter.class.getClassLoader());


    // ---------------------------------------------------------- Filter Methods


    /**
     * <p>Perform application shutdown finalization as necessary.</p>
     */
    public void destroy() {

        if (log().isInfoEnabled()) {
            log().info(messages.getMessage("filter.finalizing"));
        }

        // Execute the "destroy" command in the "shale" catalog (if any)
        Command command = catalog.getCommand(COMMAND_DESTROY);
        if (command != null) {
            WebContext webContext = new ServletWebContext(context, null, null);
            try {
                command.execute(webContext);
            } catch (Exception e) {
                if (log().isErrorEnabled()) {
                    log().error(messages.getMessage("filter.destroyException"), e);
                }
            }
        }

        // Clean up JavaServer Faces integration linkages
        context = null;
        catalog = null;

        // Clean up subordinate libraries as needed
        CatalogFactory.clear();
        cleanup();
        LogFactory.release(Thread.currentThread().getContextClassLoader());

    }


    /**
     * <p>Perform per-request application controler functionality.</p>
     *
     * @param request The request we are processing
     * @param response The response we are creating
     * @param chain The filter chain for this request
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception is thrown
     */
    public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {

      // Define local variables we will need
      Command command = null;
      boolean result = false;

      // Construct and store a new Context for this request
      ShaleWebContext context =
        new ShaleWebContext(this.context,
          (HttpServletRequest) request,
          (HttpServletResponse) response);
      request.setAttribute(CONTEXT_ATTR, context);

      // Invoke the "preprocess" command (if any is defined).  If this
      // command returns true, the response has been completed already
      // so we do NOT invoke the actual application.
      command = catalog.getCommand(COMMAND_PREPROCESS);
      if (command != null) {
          try {
              result = command.execute(context);
          } catch (IOException e) {
              throw e;
          } catch (ServletException e) {
              throw e;
          } catch (Exception e) {
              throw new ServletException(e);
          }
          if (result) {
              // Clean up the stored request attribute
              request.removeAttribute(CONTEXT_ATTR);
              // Bypass calling the remainder of the application
              return;
          }
      }

      // Invoke the remainder of the processing for this request
      // (which will typically be the JSF controller servlet)
      chain.doFilter(request, response);

      // Invoke the "postprocess" command (if any is defined).
      command = catalog.getCommand(COMMAND_POSTPROCESS);
      if (command != null) {
          try {
              command.execute(context);
          } catch (IOException e) {
              throw e;
          } catch (ServletException e) {
              throw e;
          } catch (Exception e) {
              throw new ServletException(e);
          }
      }

      // Clean up the stored request attribute
      request.removeAttribute(CONTEXT_ATTR);

    }


    /**
     * <p>Perform application startup intiialization as necessary.</p>
     *
     * @param config <code>FilterConfig</code> for this filter
     *
     * @exception ServletException if a servlet exception is thrown
     */
    public void init(FilterConfig config) throws ServletException {

        if (log().isInfoEnabled()) {
            log().info(messages.getMessage("filter.initializing"));
        }

        context = config.getServletContext();

        // Look up the "shale" catalog and ensure "standard" is defined
        try {
            catalog = getCatalog();
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // Execute the "init" command in the "shale" catalog (if any)
        Command command = catalog.getCommand(COMMAND_INIT);
        if (command != null) {
            WebContext webContext = new ServletWebContext(context, null, null);
            try {
                command.execute(webContext);
            } catch (Exception e) {
                if (log().isErrorEnabled()) {
                    log().error(messages.getMessage("filter.initException"), e);
                }
                throw new ServletException(e);
            }
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Clean up the Commons BeanUtils library if it has been loaded.</p>
     */
    private void cleanup() {

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = ShaleApplicationFilter.class.getClassLoader();
            }
            Class clazz = loader.loadClass("org.apache.commons.beanutils.PropertyUtils");
            Method method = clazz.getMethod("clearDescriptors", (Class[]) null);
            method.invoke(null, (Object[]) null);
        } catch (Exception e) {
            ; // Swallow and ignore any exceptions
        }

    }



    /**
     * <p>Return the "shale" catalog with a "standard" command, configuring the
     * default version of this command if necessary.</p>
     *
     * @exception Exception if a resource parsing exception occurs
     */
    private Catalog getCatalog() throws Exception {

        // Look up the "shale" catalog, returning any existing instance
        // if it is fully configured
        Catalog catalog = CatalogFactory.getInstance().getCatalog(CATALOG_NAME);
        if ((catalog != null) &&
            (catalog.getCommand(COMMAND_INIT) != null) &&
            (catalog.getCommand(COMMAND_DESTROY) != null)) {
            return catalog;
        }

        // Create a new catalog (if necessary)
        if (catalog == null) {
            if (log().isDebugEnabled()) {
                log().debug(messages.getMessage("filter.creatingCatalog",
                                                new Object[] { CATALOG_NAME }));
            }
            catalog = new CatalogBase();
            CatalogFactory.getInstance().addCatalog(CATALOG_NAME, catalog);
        }

        // Configure this catalog based on our default resource
        if (log().isDebugEnabled()) {
            log().debug(messages.getMessage("filter.parsingResource",
                                            new Object[] { RESOURCE_NAME }));
        }
        ConfigParser parser = new ConfigParser();
        URL url = this.getClass().getClassLoader().getResource(RESOURCE_NAME);
        if (url == null) {
            throw new IllegalArgumentException(RESOURCE_NAME);
        }
        parser.parse(url);

        return catalog;

    }


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(ShaleApplicationFilter.class);
        }
        return log;

    }


}
