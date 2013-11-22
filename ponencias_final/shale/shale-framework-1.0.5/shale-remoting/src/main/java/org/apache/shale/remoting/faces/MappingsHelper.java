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

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Constants;
import org.apache.shale.remoting.Mapping;
import org.apache.shale.remoting.Mappings;
import org.apache.shale.remoting.Mechanism;
import org.apache.shale.remoting.Processor;
import org.apache.shale.remoting.impl.FilteringProcessor;
import org.apache.shale.remoting.impl.MappingImpl;
import org.apache.shale.remoting.impl.MappingsImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Helper bean for accessing the {@link Mappings} instance for this
 * application, creating it the first time if necessary.</p>
 *
 * @since 1.0.1
 */
public class MappingsHelper {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p><code>ResourceBundle</code> containing our localized messages.</p>
     */
    private ResourceBundle bundle =
            ResourceBundle.getBundle("org.apache.shale.remoting.Bundle");


    /**
     * <p>Log instance for this class.</p>
     */
    private transient Log log = null;


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the {@link Mappings} instance for this web application,
     * creating it if necessary.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public Mappings getMappings(FacesContext context) {

        Mappings mappings = (Mappings)
            context.getExternalContext().getApplicationMap().
                get(Constants.MAPPINGS_ATTR);
        if (mappings == null) {
            mappings = createMappings(context);
            context.getExternalContext().getApplicationMap().
                    put(Constants.MAPPINGS_ATTR, mappings);
        }
        return mappings;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Configure {@link Mapping} instances on the specified {@link Mappings}
     * instance, for the specified context initialization parameter.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param mappings {@link Mappings} instance being configured
     * @param paramName Context initialization parameter name to process
     * @param excludesName Context initialization parameter containing our
     *  exclude patterns
     * @param excludesDefault Default exclude patterns if none are configured
     * @param includesName Context initialization parameter containing our
     *  include patterns
     * @param includesDefault Default include patterns if none are configured
     * @param mechanism {@link Mechanism} to configure on created instances
     * @param defaultValue Default value (if any) if not specified
     *
     * @exception FacesException if a new Mapping instance cannot be created
     *  or configured
     */
    private void configureMappings(FacesContext context, Mappings mappings,
                                   String paramName,
                                   String excludesName, String excludesDefault,
                                   String includesName, String includesDefault,
                                   Mechanism mechanism, String defaultValue) {

        // Identify the Mapping implementation class to be used
        Class clazz = MappingImpl.class;
        String mappingClass =
          context.getExternalContext().getInitParameter(Constants.MAPPING_CLASS);
        if (mappingClass != null) {
            try {
                clazz = loadClass(mappingClass);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

        // Acquire the context initialization parameter value (or default it)
        String paramValue = context.getExternalContext().getInitParameter(paramName);
        if (paramValue == null) {
            paramValue = defaultValue;
        }
        if (paramValue == null) {
            return;
        }

        // Configure new Mapping instances for each specified pattern:classname pair
        while (true) {
            paramValue = paramValue.trim();
            if (paramValue.length() == 0) {
                break;
            }
            String pair = null;
            int comma = paramValue.indexOf(',');
            if (comma >= 0) {
                pair = paramValue.substring(0, comma).trim();
                paramValue = paramValue.substring(comma + 1);
            } else {
                pair = paramValue.trim();
                paramValue = "";
            }
            int colon = pair.indexOf(':');
            if (colon < 0) {
                throw new IllegalArgumentException(pair);
            }
            String pattern = pair.substring(0, colon).trim();
            String processorClass = pair.substring(colon + 1).trim();
            if (log().isInfoEnabled()) {
                log().info(bundle.getString("mapping.configure"));
                log().info(pattern + ":" + processorClass);
            }
            Class processorClazz = null;
            try {
                processorClazz = loadClass(processorClass);
            } catch (Exception e) {
                throw new FacesException(e);
            }
            try {
                Mapping mapping = (Mapping) clazz.newInstance();
                mapping.setMappings(mappings);
                mapping.setMechanism(mechanism);
                mapping.setPattern(pattern);
                Processor processor = (Processor) processorClazz.newInstance();
                if (processor instanceof FilteringProcessor) {
                    String excludesPatterns =
                      context.getExternalContext().getInitParameter(excludesName);
                    if (excludesPatterns == null) {
                        excludesPatterns = excludesDefault;
                    }
                    ((FilteringProcessor) processor).setExcludes(excludesPatterns);
                    String includesPatterns =
                      context.getExternalContext().getInitParameter(includesName);
                    if (includesPatterns == null) {
                        includesPatterns = includesDefault;
                    }
                    ((FilteringProcessor) processor).setIncludes(includesPatterns);
                }
                mapping.setProcessor(processor);
                mappings.addMapping(mapping);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

    }


    /**
     * <p>Create and configure a {@link Mappings} instance based on the relevant
     * context initialization parameters for this web application.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     *
     * @exception FacesException if a new Mappings instance cannot be created
     *  or configured
     */
    private Mappings createMappings(FacesContext context) {

        // Instantiate a Mappings instance to configure
        Mappings mappings = null;
        String mappingsClass = MappingsImpl.class.getName();
        String mappingsClassParam =
          context.getExternalContext().getInitParameter(Constants.MAPPINGS_CLASS);
        if (mappingsClassParam != null) {
            mappingsClass = mappingsClassParam;
        }
        Class clazz = null;
        try {
            if (log().isInfoEnabled()) {
                log().info(bundle.getString("mappings.configure"));
                log().info(mappingsClass);
            }
            clazz = loadClass(mappingsClass);
        } catch (Exception e) {
            throw new FacesException(e);
        }
        try {
            mappings = (Mappings) clazz.newInstance();
        } catch (Exception e) {
            throw new FacesException(e);
        }

        // Configure the Mapping instances for this Mappings instance
        configureMappings(context, mappings, Constants.CLASS_RESOURCES_PARAM,
                          Constants.CLASS_RESOURCES_EXCLUDES,
                          Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT,
                          Constants.CLASS_RESOURCES_INCLUDES,
                          Constants.CLASS_RESOURCES_INCLUDES_DEFAULT,
                          Mechanism.CLASS_RESOURCE,
                          "/static/*:org.apache.shale.remoting.impl.ClassResourceProcessor");
        configureMappings(context, mappings, Constants.DYNAMIC_RESOURCES_PARAM,
                          Constants.DYNAMIC_RESOURCES_EXCLUDES,
                          Constants.DYNAMIC_RESOURCES_EXCLUDES_DEFAULT,
                          Constants.DYNAMIC_RESOURCES_INCLUDES,
                          Constants.DYNAMIC_RESOURCES_INCLUDES_DEFAULT,
                          Mechanism.DYNAMIC_RESOURCE,
                          "/dynamic/*:org.apache.shale.remoting.impl.MethodBindingProcessor");
        configureMappings(context, mappings, Constants.OTHER_RESOURCES_PARAM,
                          Constants.OTHER_RESOURCES_EXCLUDES,
                          Constants.OTHER_RESOURCES_EXCLUDES_DEFAULT,
                          Constants.OTHER_RESOURCES_INCLUDES,
                          Constants.OTHER_RESOURCES_INCLUDES_DEFAULT,
                          Mechanism.OTHER_RESOURCE,
                          null);
        configureMappings(context, mappings, Constants.WEBAPP_RESOURCES_PARAM,
                          Constants.WEBAPP_RESOURCES_EXCLUDES,
                          Constants.WEBAPP_RESOURCES_EXCLUDES_DEFAULT,
                          Constants.WEBAPP_RESOURCES_INCLUDES,
                          Constants.WEBAPP_RESOURCES_INCLUDES_DEFAULT,
                          Mechanism.WEBAPP_RESOURCE,
                          "/webapp/*:org.apache.shale.remoting.impl.WebResourceProcessor");

        // Calculate and set the replacement extension, to be used
        // if FacesServlet is extension mapped
        String extension = context.getExternalContext().
                getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
        if (extension == null) {
            extension = ViewHandler.DEFAULT_SUFFIX;
        }
        mappings.setExtension(extension);

        // Calculate and set the URL patterns that FacesServlet is mapped with
        // FIXME - hard coded to "*.faces" for now
        String[] patterns = patterns(context);
        if (log().isTraceEnabled()) {
            for (int i = 0; i < patterns.length; i++) {
                log().trace("FacesServlet is mapped with URL pattern '" + patterns[i] + "'");
            }
        }
        mappings.setPatterns(patterns);

        // Calculate the index of the pattern to use by default
        int patternIndex = 0;
        String patternIndexString =
          context.getExternalContext().getInitParameter(Constants.FACES_SERVLET_URL_PARAM);
        if (patternIndexString != null) {
            patternIndex = Integer.parseInt(patternIndexString.trim());
        }
        if (patternIndex >= patterns.length) {
            log.warn("FacesServlet pattern index of " + patternIndex
                     + " does not match any specified pattern");
        }
        mappings.setPatternIndex(patternIndex);

        // Return the configured Mappings instance
        return mappings;

    }


    /**
     * <p>Load the specified class from the web application class loader
     * (if possible).</p>
     *
     * @param name Fully qualified class name
     *
     * @exception ClassNotFoundException if the specified class cannot
     *  be loaded
     */
    private Class loadClass(String name) throws ClassNotFoundException {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        return cl.loadClass(name);

    }


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(MappingsHelper.class);
        }
        return log;

    }


    /**
     * <p>Return an array of URL patterns that <code>FacesServlet</code> is
     * mapped to for this application.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private String[] patterns(FacesContext context) {

        Document document = null;
        InputStream stream = null;

        try {

            // Acquire a URL for /WEB-INF/web.xml (if any)
            Object ctxt = context.getExternalContext().getContext();
            Method method =
                    ctxt.getClass().getMethod("getResource",
                                              new Class[] { String.class });
            URL url = (URL) method.invoke(ctxt, new Object[] { "/WEB-INF/web.xml" });
            if (url == null) {
                if (log().isTraceEnabled()) {
                    log().trace("No /WEB-INF/web.xml resource available, returning empty list");
                }
                return new String[0];
            }

            // Parse this resource into a DOM tree
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            stream = url.openStream();
            document = db.parse(stream);

        } catch (Exception e) {

            if (log().isErrorEnabled()) {
                log().error(bundle.getString("mappings.parseWebXml"), e);
            }
            return new String[0];

        } finally {

            if (stream != null) {
                try { stream.close(); } catch (Exception e) { ; }
            }

        }

        // Identify the servlet name of the JavaServer Faces controller servlet
        String name =
                context.getExternalContext().getInitParameter(Constants.FACES_SERVLET_NAME_PARAM);
        if (null == name) {
            NodeList servletNodes = document.getElementsByTagName("servlet");
            for (int i = 0; i < servletNodes.getLength(); i++) {
                Node servletNode = servletNodes.item(i);
                String servletName = null;
                String servletClass = null;
                NodeList kids = servletNode.getChildNodes();
                for (int j = 0; j < kids.getLength(); j++) {
                    Node kid = kids.item(j);
                    if ("servlet-name".equals(kid.getNodeName())) {
                        servletName = text(kid);
                    } else if ("servlet-class".equals(kid.getNodeName())) {
                        servletClass = text(kid);
                    }
                }
                if ("javax.faces.webapp.FacesServlet".equals(servletClass)) {
                    name = servletName;
                }
            }
        }

        // Identify the URL patterns to which this servlet is mapped
        List list = new ArrayList();
        NodeList mappingNodes = document.getElementsByTagName("servlet-mapping");
        for (int i = 0; i < mappingNodes.getLength(); i++) {
            Node mappingNode = mappingNodes.item(i);
            String servletName = null;
            String urlPattern = null;
            NodeList kids = mappingNode.getChildNodes();
            for (int j = 0; j < kids.getLength(); j++) {
                Node kid = kids.item(j);
                if ("servlet-name".equals(kid.getNodeName())) {
                    servletName = text(kid);
                } else if ("url-pattern".equals(kid.getNodeName())) {
                    urlPattern = text(kid);
                }
            }
            if (name.equals(servletName)) {
                list.add(urlPattern);
            }
        }

        // Return the resulting list
        return (String[]) list.toArray(new String[list.size()]);

    }


    /**
     * <p>Return the text content inside the specified node.</p>
     *
     * @param node Node from which text is to be extracted
     */
    private String text(Node node) {

        NodeList kids = node.getChildNodes();
        for (int k = 0; k < kids.getLength(); k++) {
            Node kid = kids.item(k);
            if ("#text".equals(kid.getNodeName())) {
                return kid.getNodeValue().trim();
            }
        }
        return "";

    }


}
