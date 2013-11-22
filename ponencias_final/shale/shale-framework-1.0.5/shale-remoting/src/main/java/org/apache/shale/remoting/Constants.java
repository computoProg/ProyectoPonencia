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

package org.apache.shale.remoting;

/**
 * <p>Manifest constants related to Shale Remoting support.</p>
 */
public final class Constants {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Private constructor to avoid instantiation.</p>
     */
    private Constants() { }


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly excluded.  If not specified, the value of constant
     * CLASS_RESOURCES_EXCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String CLASS_RESOURCES_EXCLUDES =
            "org.apache.shale.remoting.CLASS_RESOURCES_EXCLUDES";


    /**
     * <p>Default value for the CLASS_RESOURCES_EXCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The default exclude list
     * defined here will be prepended to any exclude list provided by the
     * application, with the result that it is not possible to configure
     * this processor to deliver resources matching these patterns.</p>
     *
     * @since 1.0.4
     */
    public static final String CLASS_RESOURCES_EXCLUDES_DEFAULT =
            "*.class,*.jsp,*.properties";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly included.  If not specified, the value of constant
     * CLASS_RESOURCES_INCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String CLASS_RESOURCES_INCLUDES =
            "org.apache.shale.remoting.CLASS_RESOURCES_INCLUDES";


    /**
     * <p>Default value for the CLASS_RESOURCES_INCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * @since 1.0.4
     */
    public static final String CLASS_RESOURCES_INCLUDES_DEFAULT =
            "*.css,*.gif,*.html,*.jpg,*.js,*.png,*.txt,*.xml";


    /**
     * <p>Context initialization parameter containing a comma-delimited list of
     * colon-delimited pairs, with each pair representing a URL matching pattern
     * (such as <code>/foo/*</code> or <code>*.foo</code>) and the fully qualified
     * class name of a {@link Processor} class to use for handling requests that
     * match the specified pattern.  If no such parameter is specified, the
     * default value (<code>/static/*:org.apache.shale.remoting.impl.ClassResourceProcessor</code>)
     * is used.  The {@link Mapping} instance for each pair will be configured
     * with <code>Mechanism.CLASS_RESOURCE</code>.</p>
     */
    public static final String CLASS_RESOURCES_PARAM =
            "org.apache.shale.remoting.CLASS_RESOURCES";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly excluded.  If not specified, the value of constant
     * DYNAMIC_RESOURCES_EXCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String DYNAMIC_RESOURCES_EXCLUDES =
            "org.apache.shale.remoting.DYNAMIC_RESOURCES_EXCLUDES";


    /**
     * <p>Default value for the DYNAMIC_RESOURCES_EXCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The default exclude list
     * defined here will be prepended to any exclude list provided by the
     * application, with the result that it is not possible to configure
     * this processor to deliver resources matching these patterns.</p>
     *
     * @since 1.0.4
     */
    public static final String DYNAMIC_RESOURCES_EXCLUDES_DEFAULT =
            "/application/*,/applicationScope/*,/facesContext/*,/request/*,/requestScope/*,/response/*,/session/*,/sessionScope/*,/view/*";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly included.  If not specified, the value of constant
     * DYNAMIC_RESOURCES_INCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String DYNAMIC_RESOURCES_INCLUDES =
            "org.apache.shale.remoting.DYNAMIC_RESOURCES_INCLUDES";


    /**
     * <p>Default value for the DYNAMIC_RESOURCES_INCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * @since 1.0.4
     */
    public static final String DYNAMIC_RESOURCES_INCLUDES_DEFAULT =
            null;


    /**
     * <p>Context initialization parameter containing a comma-delimited list of
     * colon-delimited pairs, with each pair representing a URL matching pattern
     * (such as <code>/foo/*</code> or <code>*.foo</code>) and the fully qualified
     * class name of a {@link Processor} class to use for handling requests that
     * match the specified pattern.  If no such parameter is specified, the
     * default value (<code>/dynamic/*:org.apache.shale.remoting.impl.MethodBindingProcessor</code>)
     * is used.  The {@link Mapping} instance for each pair will be configured
     * with <code>Mechanism.DYNAMIC_RESOURCE</code>.</p>
     */
    public static final String DYNAMIC_RESOURCES_PARAM =
            "org.apache.shale.remoting.DYNAMIC_RESOURCES";


    /**
     * <p>Context initialization parameter defining the name of the
     * Faces Servlet to be used for remoting requests.  If not specified,
     * the last (or only) servlet definition for a servlet whose class is
     * <code>javax.faces.webapp.FacesServlet</code> is used.</p>
     */
    public static final String FACES_SERVLET_NAME_PARAM =
            "org.apache.shale.remoting.FACES_SERVLET_NAME";


    /**
     * <p>Context initialization parameter defining the zero-relative index
     * of the <code>servlet-mapping</code>, for the specified or default
     * <code>FacesServlet</code> servlet name, to use when generating
     * URLs for resources.  If not specified, the default value is zero,
     * meaning that the first servlet mapping (in the order listed in
     * <code>web.xml</code>) will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String FACES_SERVLET_URL_PARAM =
            "org.apache.shale.remoting.FACES_SERVLET_MAPPING_INDEX";
    


    /**
     * <p>Context initialization parameter containing the fully qualified
     * class name of the {@link Mapping} implementation class to use.  If
     * not specified, <code>org.apache.shale.remoting.impl.MappingImpl</code>
     * is used.</p>
     */
    public static final String MAPPING_CLASS =
            "org.apache.shale.remoting.MAPPING_CLASS";


    /**
     * <p>Servlet context attribute under which the {@link Mappings} instance
     * for this web application will be stored.</p>
     */
    public static final String MAPPINGS_ATTR =
            "org.apache.shale.remoting.MAPPINGS";


    /**
     * <p>Context initialization parameter containing the fully qualified
     * class name of the {@link Mappings} implementation class to use.  If
     * not specified, <code>org.apache.shale.remoting.impl.MappingsImpl</code>
     * is used.</p>
     */
    public static final String MAPPINGS_CLASS =
            "org.apache.shale.remoting.MAPPINGS_CLASS";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly excluded.  If not specified, the value of constant
     * OTHER_RESOURCES_EXCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String OTHER_RESOURCES_EXCLUDES =
            "org.apache.shale.remoting.OTHER_RESOURCES_EXCLUDES";


    /**
     * <p>Default value for the OTHER_RESOURCES_EXCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The default exclude list
     * defined here will be prepended to any exclude list provided by the
     * application, with the result that it is not possible to configure
     * this processor to deliver resources matching these patterns.</p>
     *
     * @since 1.0.4
     */
    public static final String OTHER_RESOURCES_EXCLUDES_DEFAULT =
            "*.class,*.jsp,*.properties";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly included.  If not specified, the value of constant
     * OTHER_RESOURCES_INCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String OTHER_RESOURCES_INCLUDES =
            "org.apache.shale.remoting.OTHER_RESOURCES_INCLUDES";


    /**
     * <p>Default value for the OTHER_RESOURCES_INCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * @since 1.0.4
     */
    public static final String OTHER_RESOURCES_INCLUDES_DEFAULT =
            "*.css,*.gif,*.html,*.jpg,*.js,*.png,*.xml";


    /**
     * <p>Context initialization parameter containing a comma-delimited list of
     * colon-delimited pairs, with each pair representing a URL matching pattern
     * (such as <code>/foo/*</code> or <code>*.foo</code>) and the fully qualified
     * class name of a {@link Processor} class to use for handling requests that
     * match the specified pattern.  No defaults for this mechanism are defined.
     * The {@link Mapping} instance for each pair will be configured
     * with <code>Mechanism.OTHER_RESOURCE</code>.</p>
     */
    public static final String OTHER_RESOURCES_PARAM =
            "org.apache.shale.remoting.OTHER_RESOURCES";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly excluded.  If not specified, the value of constant
     * WEB_RESOURCES_EXCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String WEBAPP_RESOURCES_EXCLUDES =
            "org.apache.shale.remoting.WEB_RESOURCES_EXCLUDES";


    /**
     * <p>Default value for the WEB_RESOURCES_EXCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The default exclude list
     * defined here will be prepended to any exclude list provided by the
     * application, with the result that it is not possible to configure
     * this processor to deliver resources matching these patterns.</p>
     *
     * @since 1.0.4
     */
    public static final String WEBAPP_RESOURCES_EXCLUDES_DEFAULT =
            "*.class,*.jsp,*.properties";


    /**
     * <p>Context initialization parameter containing a comma-delimited list
     * of URL matching patterns for resource identifiers that will be
     * explicitly included.  If not specified, the value of constant
     * WEB_RESOURCES_INCLUDES_DEFAULT will be used.</p>
     *
     * @since 1.0.4
     */
    public static final String WEBAPP_RESOURCES_INCLUDES =
            "org.apache.shale.remoting.WEB_RESOURCES_INCLUDES";


    /**
     * <p>Default value for the WEB_RESOURCES_INCLUDES context initialization
     * parameter if no explicit value is specified.</p>
     *
     * @since 1.0.4
     */
    public static final String WEBAPP_RESOURCES_INCLUDES_DEFAULT =
            "*.css,*.gif,*.html,*.jpg,*.js,*.png,*.txt,*.xml";


    /**
     * <p>Context initialization parameter containing a comma-delimited list of
     * colon-delimited pairs, with each pair representing a URL matching pattern
     * (such as <code>/foo/*</code> or <code>*.foo</code>) and the fully qualified
     * class name of a {@link Processor} class to use for handling requests that
     * match the specified pattern.  If no such parameter is specified, the
     * default value (<code>/webapp/*:org.apache.shale.remoting.impl.MethodBindingProcessor</code>)
     * is used.  The {@link Mapping} instance for each pair will be configured
     * with <code>Mechanism.WEBAPP_RESOURCE</code>.</p>
     */
    public static final String WEBAPP_RESOURCES_PARAM =
            "org.apache.shale.remoting.WEBAPP_RESOURCES";


}
