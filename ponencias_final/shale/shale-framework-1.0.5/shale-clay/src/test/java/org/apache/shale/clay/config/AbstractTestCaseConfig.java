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
package org.apache.shale.clay.config;

import java.io.File;

import org.apache.shale.clay.config.beans.ComponentConfigBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.TemplateComponentConfigBean;
import org.apache.shale.clay.config.beans.TemplateConfigBean;
import org.apache.shale.test.base.AbstractViewControllerTestCase;

// package scope base class that loads factory definitions
public abstract class AbstractTestCaseConfig extends AbstractViewControllerTestCase {

    // Construct a new instance of this test case.
    public AbstractTestCaseConfig(String name) {
        super(name);
    }


    protected ConfigBean standardConfigBean = null;
    protected ConfigBean htmlTemplateConfigBean = null;
    protected ConfigBean xmlTemplateConfigBean = null;

    //COMPONENTS[0] = jsfid, COMPONENTS[1] = componentType, COMPONENT[2] = className
    protected static final Object[] COMPONENTS = {new String[] {"token", "org.apache.shale.Token", "org.apache.shale.component.Token"},
                          new String[] {"outputText", "javax.faces.HtmlOutputText", "javax.faces.component.html.HtmlOutputText"}, 
                          new String[] {"selectItem", "javax.faces.SelectItem", "javax.faces.component.UISelectItem"},
                          new String[] {"selectItems", "javax.faces.SelectItems", "javax.faces.component.UISelectItems"},
                          new String[] {"selectOneRadio", "javax.faces.HtmlSelectOneRadio", "javax.faces.component.html.HtmlSelectOneRadio"},
                          new String[] {"selectOneMenu", "javax.faces.HtmlSelectOneMenu", "javax.faces.component.html.HtmlSelectOneMenu"},
                          new String[] {"selectManyMenu", "javax.faces.HtmlSelectManyMenu", "javax.faces.component.html.HtmlSelectManyMenu"},
                          new String[] {"manySelectCheckbox", "javax.faces.HtmlSelectManyCheckbox", "javax.faces.component.html.HtmlSelectManyCheckbox"},
                          new String[] {"selectBooleanCheckbox", "javax.faces.HtmlSelectBooleanCheckbox", "javax.faces.component.html.HtmlSelectBooleanCheckbox"},
                          new String[] {"panelGroup", "javax.faces.HtmlPanelGroup", "javax.faces.component.html.HtmlPanelGroup"},
                          new String[] {"outputLink", "javax.faces.HtmlOutputLink", "javax.faces.component.html.HtmlOutputLink"},
                          new String[] {"outputLabel", "javax.faces.HtmlOutputLabel", "javax.faces.component.html.HtmlOutputLabel"},
                          new String[] {"inputTextarea", "javax.faces.HtmlInputTextarea","javax.faces.component.html.HtmlInputTextarea"},
                          new String[] {"inputSecret", "javax.faces.HtmlInputSecret","javax.faces.component.html.HtmlInputSecret"},
                          new String[] {"inputText", "javax.faces.HtmlInputText", "javax.faces.component.html.HtmlInputText"},
                          new String[] {"image", "javax.faces.HtmlGraphicImage", "javax.faces.component.html.HtmlGraphicImage"},
                          new String[] {"form", "javax.faces.HtmlForm", "javax.faces.component.html.HtmlForm"},
                          new String[] {"dataTable", "javax.faces.HtmlDataTable", "javax.faces.component.html.HtmlDataTable"},
                          new String[] {"commandLink", "javax.faces.HtmlCommandLink", "javax.faces.component.html.HtmlCommandLink"},
                          new String[] {"column", "javax.faces.Column", "javax.faces.component.UIColumn"},
                          new String[] {"inputHidden", "javax.faces.HtmlInputHidden", "javax.faces.component.html.HtmlInputHidden"},
                          new String[] {"outputFormat", "javax.faces.HtmlOutputFormat", "javax.faces.component.html.HtmlOutputFormat"},
                          new String[] {"messages", "javax.faces.HtmlMessages", "javax.faces.component.html.HtmlMessages"},
                          new String[] {"message", "javax.faces.HtmlMessage", "javax.faces.component.html.HtmlMessage"},
                          new String[] {"commandButton", "javax.faces.HtmlCommandButton", "javax.faces.component.html.HtmlCommandButton"},
                          new String[] {"panelGrid", "javax.faces.HtmlPanelGrid", "javax.faces.component.html.HtmlPanelGrid"},
                          new String[] {"namingContainer", "javax.faces.NamingContainer", "javax.faces.component.UINamingContainer"},
                          new String[] {"clay", "org.apache.shale.clay.component.Clay", "org.apache.shale.clay.component.Clay"}    
    };

    // load the mock component config data
    protected void loadComponents() {
       for (int i = 0; i < COMPONENTS.length; i++) {
          application.addComponent(((String[])COMPONENTS[i])[1], ((String[])COMPONENTS[i])[2]);
       }
    }
    
    //CONVERTERS[0] = jsfid, CONVERTERS[1] = componentType, CONVERTERS[2] = className
    protected static final Object[] CONVERTERS = {
               new String[] {"integerConverter", "javax.faces.Integer", "javax.faces.convert.IntegerConverter"},
               new String[] {"dateTimeConverter", "javax.faces.DateTime", "javax.faces.convert.DateTimeConverter"}
    };    
    
    // load the mock converter config data
    protected void loadConverters() {
        for (int i = 0; i < CONVERTERS.length; i++) {
           application.addConverter(((String[])CONVERTERS[i])[1], ((String[])CONVERTERS[i])[2]);
        }
     }
    
    //VALIDATORS[0] = jsfid, VALIDATORS[1] = componentType, VALIDATORS[2] = className
    public static final Object[] VALIDATORS = {
               new String[] {"longRangeValidator","javax.faces.LongRange", "javax.faces.validator.LongRangeValidator"}
    };     

    // load the mock validator config
    protected void loadValidators() {
        for (int i = 0; i < VALIDATORS.length; i++) {
           application.addValidator(((String[])VALIDATORS[i])[1], ((String[])VALIDATORS[i])[2]);
        }
    }

    // setup the testcase   
    protected void setUp() throws Exception {
        super.setUp();

        // Configure document root for tests
        String documentRoot = System.getProperty("documentRoot");
        if (documentRoot == null) {
            documentRoot = System.getProperty("user.dir") + "\\target\\test-classes";           
        }
        servletContext.setDocumentRoot(new File(documentRoot));
        
        

        //load the mock config data
        loadComponents();
        loadConverters();
        loadValidators();

        // sets the default html template suffix ".html"
        servletContext.addInitParameter(Globals.CLAY_HTML_TEMPLATE_SUFFIX, ".html");

        // sets the default html template suffix ".xml"
        servletContext.addInitParameter(Globals.CLAY_XML_TEMPLATE_SUFFIX, ".xml");
       
        // creates the component metadata container from the xml config files
        standardConfigBean = new ComponentConfigBean();
        // creates a container that builds the component metadata from an HTML  
        // template configuration.
        htmlTemplateConfigBean = new TemplateConfigBean();
        // full xml view support
        xmlTemplateConfigBean = new TemplateComponentConfigBean();

        
        // register with the factory
        ConfigBeanFactory.register(standardConfigBean);
        ConfigBeanFactory.register(htmlTemplateConfigBean);
        ConfigBeanFactory.register(xmlTemplateConfigBean);

    }

    protected void tearDown() throws Exception {
        super.tearDown();

        // deregister
        ConfigBeanFactory.destroy();
        standardConfigBean = null;
        htmlTemplateConfigBean = null;
        xmlTemplateConfigBean = null;
    }

    // loads the config files
    protected void loadConfigFile(String commonConfigFiles) {
        loadConfigFiles(commonConfigFiles, null);
    }
    
    // loads the config files
    protected void loadConfigFiles(String commonConfigFiles, String fullXmlConfigFiles) {
        // this would be done in the ClayConfigureListener
        
        if (commonConfigFiles !=  null)
           servletContext.addInitParameter(Globals.CLAY_COMMON_CONFIG_FILES, commonConfigFiles);  
        
        if (fullXmlConfigFiles != null) {
           servletContext.addInitParameter(Globals.CLAY_FULLXML_CONFIG_FILES, fullXmlConfigFiles);  
        }
        
        standardConfigBean.init(servletContext);
        htmlTemplateConfigBean.init(servletContext);
        xmlTemplateConfigBean.init(servletContext);

    }

       
}
