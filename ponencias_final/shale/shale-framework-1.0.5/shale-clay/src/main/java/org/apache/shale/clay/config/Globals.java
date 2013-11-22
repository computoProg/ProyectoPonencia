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
 * $Id: Globals.java 473102 2006-11-09 22:26:21Z gvanmatre $
 */
package org.apache.shale.clay.config;

/**
 * <p>A {@link Globals} contains static constants used throughout the clay
 * plug-in.
 * </p>
 */
public class Globals {

    /**
     *<p>EL properties will replaced the literal value of this constant with the
     * mapped ViewController bean before the expression is evaluated.
     *</p>
     */
    public static final String MANAGED_BEAN_MNEMONIC = "@managed-bean-name";

    /**
     *<p>Name of the initialization param in the web deployment descriptor
     * containing additional registered config files.  The list of files
     * should be comma delimited and the path relative to the context root.
     * <br/>
     * The configuration files should conform to the clay-config_x_x.dtd
     *</p>
     * @deprecated use CLAY_COMMON_CONFIG_FILES
     */
    public static final String CLAY_CONFIG_FILES = "clay-config-files";

    /**
     *<p>Name of the initialization parameter in the web deployment descriptor
     *containing additional registered configuration files.  These files represent
     *common component definitions that are not full view XML templates.   Common
     *component definitions should have jsfid's that don't have a ".xml" or
     *".html" suffix.  The list of files should be comma delimited and the path
     *relative to the context root.
     *<br>
     *The configuration files should conform to the clay-config_x_x.dtd.
     *</p>
     */
    public static final String CLAY_COMMON_CONFIG_FILES = "org.apache.shale.clay.COMMON_CONFIG_FILES";

    /**
     *<p>Name of the initialization parameter in the web deployment descriptor
     *containing additional registered configuration files.  These files represent
     *common component definitions that are full view XML templates.
     *The list of files should be comma delimited and the path relative to the context root.
     *<br/>
     *The configuration files should conform to the clay-config_x_x.dtd.
     *</p>
     */
    public static final String CLAY_FULLXML_CONFIG_FILES = "org.apache.shale.clay.FULLXML_CONFIG_FILES";

    /**
     * <p>The name of the initialization parameter defined in the
     * web deployment descriptor that's value will define a suffix
     * identifier used by the {@link org.apache.shale.clay.config.beans.TemplateConfigBean}.
     * This suffix identifies a jsfid that uses HTML to define page composition.
     * </p>
     */
    public static final String CLAY_HTML_TEMPLATE_SUFFIX = "org.apache.shale.clay.HTML_TEMPLATE_SUFFIX";

    /**
     * <p>The name of the initialization parameter defined in the
     * web deployment descriptor that's value will define a suffix
     * identifier used by the {@link org.apache.shale.clay.config.beans.TemplateComponentConfigBean}.
     * This suffix identifies a jsfid that uses full XML views to define page composition.
     * </p>
     */
    public static final String CLAY_XML_TEMPLATE_SUFFIX = "org.apache.shale.clay.XML_TEMPLATE_SUFFIX";

    /**
     * <p>The name of the initialization parameter defined in the
     * web deployment descriptor that's value will determine if the
     * configuration files defined by <code>CLAY_CONFIG_FILES</code>
     * will be watched for changes.  The default value is <code>true</code>
     * meaning that the config files will be automatically reloaded if
     * a change occures.
     * </p>
     */
    public static final String AUTO_RELOAD_CLAY_FILES = "org.apache.shale.clay.AUTO_RELOAD_CONFIG_FILES";

    /**
     * <p>The default full html view template suffix identifier if one is not specified.
     * Override using the CLAY_HTML_TEMPLATE_SUFFIX web init param parameter.</p>
     */
    public static final String CLAY_DEFAULT_HTML_TEMPLATE_SUFFIX = ".html";

    /**
     * <p>The default full XML template view suffix identifier if one is not specified.
     * Override using the CLAY_HTML_TEMPLATE_SUFFIX web init param parameter.</p>
     */

    public static final String CLAY_DEFAULT_XML_TEMPLATE_SUFFIX = ".xml";

    /**
     * <p>Configuration file prefix, "classpath*:", denotes that the resource
     * should be loaded from the class path versus the context root.</p>
     */
    public static final String CLASSPATH_PREFIX = "classpath*:";

    /**
     * <p>The name of the initializtion parameter in the web deployment descriptor that
     * defines the default charset for all html templates.  If not specified, the
     * "<code>file.encoding</code>" system parameter is the default.  The charset can
     * be overridden for each template file using a special comment directive,
     * <code>"&lt;!-- ### clay:page charset="UTF-8" /### --&gt;"</code>.</p>
     */
    public static final String CLAY_HTML_CHARSET = "org.apache.shale.clay.HTML_TEMPLATE_CHARSET";

    /**
     * <p>The default subview configuration file containing the base supported
     * components.
     *</p>
     */
    public static final String DEFAULT_CLAY_CONFIG_FILE = CLASSPATH_PREFIX + "META-INF/clay-config.xml";

    /**
     * <p>The literal string used to identify that a clay subtree should be
     * built at runtime.  This value "RUNTIME" will be placed in the jsfid
     * attribute.  The subtree construction should be build by providing implementation
     * for the "shapeValidator" component event.
     * </p>
     */
    public static final String RUNTIME_ELEMENT_ID = "RUNTIME";

    /**
     * <p>The default catalog name used to configure the chain
     * workflow for building the clay subtree from a object
     * graph of metadata.
     *</p>
     */
    public static final String CLAY_CATALOG_NAME = "clay";

    /**
     * <p>The customization catalog name used to configure the chain
     * workflow for building the clay subtree from a object
     * graph of metadata.
     *</p>
     */
    public static final String CLAY_CUSTOMIZATION_CATALOG_NAME = "clayCustomization";

    /**
     * <p>Chain subchain command invoked by the {@link org.apache.shale.clay.component.chain.AssignPropertiesCommand}
     * to assign the attributes to the components.  There are five subcommands
     * under this chain that handle the associated attributes.
     * </p>
     */
    public static final String SET_ATTRIBUTE_COMMAND_NAME = "setAttribute";

    /**
     * <p>The command name used to add a component.  There are several subchains
     * that will be invoked.  This command is invoked recursively  for each child
     * component.
     * </p>
     */
    public static final String ADD_COMPONENT_COMMAND_NAME = "addComponent";

    /**
     * <p>This command is invoked to add a converter to a component implementing
     * the {@link javax.faces.component.ValueHolder} interface.
     * </p>
     */
    public static final String ADD_CONVERTER_COMMAND_NAME = "addConverter";

    /**
     * <p>This command is invoked to add a validator to a component
     * implementing the {@link javax.faces.component.EditableValueHolder}
     * interface.</p>
     */
    public static final String ADD_VALIDATOR_COMMAND_NAME = "addValidator";

    /**
     * <p>This command is invoked to add a valueChangeListener to a component that
     * implements the {@link javax.faces.component.EditableValueHolder} interface.</p>
     */
    public static final String ADD_VALUE_CHANGE_LISTENER_COMMAND_NAME = "addValueChangeListener";

    /**
     * <p>This command is invoked to add a actionListener to a component that
     * implements the {@link javax.faces.component.ActionSource} interface.
     *</p>
     */
    public static final String ADD_ACTION_LISTENER_COMMAND_NAME = "addActionListener";

    /**
     * <p>The catalog name used by {@link org.apache.shale.clay.parser.builder.BuilderFactory}
     *  to define the rules that map an HTML element to a {@link org.apache.shale.clay.parser.builder.Builder}.
     *  </p>
     */
    public static final String BUILDER_CATALOG_NAME = "builder";

    /**
     * <p> Command name in the BUILDER_CATALOG_NAME used to invoke a command
     * chain of rule commands that locates a {@link org.apache.shale.clay.parser.builder.Builder}
     * for the default namespace.
     * </p>
     */
    public static final String FIND_DEFAULT_BUILDER_COMMAND_NAME = "default-namespace";

    /**
     * <p> Command name in the BUILDER_CATALOG_NAME used to invoke a command
     * chain of rule commands that locates a {@link org.apache.shale.clay.parser.builder.Builder}
     * for a unknown namespace.
     * </p>
     */
    public static final String FIND_UNKNOWN_BUILDER_COMMAND_NAME = "unknown-namespace";

    /**
     * <p>The config file used by {@link org.apache.shale.clay.component.Clay} component to
     * load the ADD_COMPONENT_COMMAND_NAME from the CLAY_CATALOG_NAME.
     * </p>
     */
    public static final String CLAY_RESOURCE_NAME = "org/apache/shale/clay/component/chain/shale-clay-config.xml";

    /**
     * <p> The config file used by the {@link org.apache.shale.clay.parser.builder.BuilderFactory}
     * to assemble the rule finder chain.
     * </p>
     */
    public static final String BUILDER_RESOURCE_NAME =
        "org/apache/shale/clay/parser/builder/chain/shale-builder-config.xml";

    /**
     * <p>The Map key used by the {@link org.apache.shale.clay.config.beans.ComponentConfigBean} to identify the
     * {@link org.apache.shale.clay.config.beans.ComponentConfigBean$WatchDog}
     * for the XML configuration files.</p>
     */
    public static final String DEFAULT_COMPONENT_CONFIG_WATCHDOG = "ComponentConfigBean$WatchDog";

    /**
     * <p>The key value used to cache the original view suffix in request scope for full clay views.</p>
     */
    public static final String CLAY_FULL_VIEW_SUFFIX = "org.apache.shale.clay.suffix";

    /**
     * <p>The key value used as a reqest scope indicator to determine a view has been restored.</p>
     */
    public static final String CLAY_FULL_VIEW_RESTORE_IND = "org.apache.shale.clay.forward";

    /**
     * <p>The clay component attribute name that will capture the ComponentBean representing the root
     * of the subtree.  The root is capture for enhanced error reporting.</p>
     */
    public static final String CLAY_RESERVED_ATTRIBUTE = "org.apache.shale.clay";

    /**
     * <p>The request scope key that will be populated with the unknow namespace when parsing an
     * HTML document.  Custom builders can be registed by the namespace uri by the commons chains.
     * <br/><br/>For example:<br/>
     * &lt;catalog name="clayCustomization" &gt;<br/>
     * &nbsp;&lt;chain name="http://www.acme.com/jsf/mywidgets"&gt;<br/>
     * &nbsp;&nbsp;&lt;command  className="org.apache.shale.clay.parser.builder.chain.JsfDefaultBuilderRule"
     *  prefix="w"/&gt;<br/>
     * &nbsp;&lt;/chain&gt;<br/>
     *
     */
    public static final String CLAY_CUSTOM_BUILDER_XMLNS = "XMLNS";

    /**
     * <p>The key value used to store clay's version of the <code>jspid</code>.
     * This custom id is added to the <code>UIComponent</code>'s attributes
     * map.</p>
     */
    public static final String CLAY_JSPID_ATTRIBUTE = "org.apache.shale.clay.jspid";
}

