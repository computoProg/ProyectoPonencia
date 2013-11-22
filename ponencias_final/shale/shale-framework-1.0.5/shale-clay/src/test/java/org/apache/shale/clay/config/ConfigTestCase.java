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

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;

// Tests that the clay component metadata can be loaded and inheritance resolved.
public class ConfigTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public ConfigTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ConfigTestCase.class));

    }


    public void testLoadConfigFile() {

        // loads the default config from the META-INF jar folder
        loadConfigFile("");
        
        //verify the components where loaded
        for (int i = 0; i < COMPONENTS.length; i++) {
            ComponentBean bean = standardConfigBean.getElement(((String[])COMPONENTS[i])[0]);
            assertNotNull("component", bean);
            assertEquals("component.jsfid",((String[])COMPONENTS[i])[0],  bean.getJsfid()); 
            assertEquals("component.componentType", ((String[])COMPONENTS[i])[1], bean.getComponentType());
        }

        //verify converters where loaded
        for (int i = 0; i < CONVERTERS.length; i++) {
            ComponentBean bean = standardConfigBean.getElement(((String[])CONVERTERS[i])[0]);
            assertNotNull("converter", bean);
            assertEquals("converter.jsfid", ((String[])CONVERTERS[i])[0], bean.getJsfid()); 
            assertEquals("converter.componentType", ((String[])CONVERTERS[i])[1], bean.getComponentType());
        }

        //verify validators where loaded 
        for (int i = 0; i < VALIDATORS.length; i++) {
            ComponentBean bean = standardConfigBean.getElement(((String[])VALIDATORS[i])[0]);
            assertNotNull("validator", bean);
            assertEquals("validator.jsfid", ((String[])VALIDATORS[i])[0], bean.getJsfid()); 
            assertEquals("validator.componentType", ((String[])VALIDATORS[i])[1], bean.getComponentType());
        }

    }

    //data that is used to verify the inheritance is working from the XML file.
    //jsfid || id, componentType, facetName, allowBody, attributes[], 
    //aggregates (converters, children, validators, actionListeners, valueChangeListeners)
    protected static final Object[] CUSTOM_XML_COMPONENTS =  {
             new Object[] {"street1Label","javax.faces.HtmlOutputLabel", null, "false", 
                                new Object[] {new String[] {"style", "color:blue"}, 
                                              new String[] {"value", "Street 1:"},
                                              new String[] {"for", "street1"}}
              , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
             },
             new Object[] {"street1","javax.faces.HtmlOutputText", null, null, 
                     new Object[] {new String[] {"size", "35"}, 
                                   new String[] {"maxlength", "50"},
                                   new String[] {"value", "#{@managed-bean-name.address1}"},  
                                   new String[] {"required", "true"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
             },  
             new Object[] {"street1Message","javax.faces.HtmlMessage", null, "false", 
                     new Object[] {new String[] {"style", "color:red"}, 
                                   new String[] {"for", "street1"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
             },   
             new Object[] {"street2Label","javax.faces.HtmlOutputLabel", null, "false", 
                     new Object[] {new String[] {"style", "color:blue"}, 
                                   new String[] {"value", "Street 2:"},
                                   new String[] {"for", "street2"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
             },
             new Object[] {"street2","javax.faces.HtmlInputText", null, null, 
                     new Object[] {new String[] {"size", "35"}, 
                        new String[] {"maxlength", "50"},
                        new String[] {"value", "#{@managed-bean-name.address2}"},  
                        new String[] {"required", "true"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
             },  
            new Object[] {"street2Message","javax.faces.HtmlMessage", null, "false", 
                     new Object[] {new String[] {"style", "color:red"}, 
                        new String[] {"for", "street2"}}
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                                  
            },  
            new Object[] {"zip","javax.faces.HtmlInputText", null, null, 
                     new Object[] {new String[] {"size", "5"}, 
                        new String[] {"maxlength", "9"},
                        new String[] {"value", "#{@managed-bean-name.zip}"},  
                        new String[] {"valueChangeListener", "#{@managed-bean-name.zipValueChange}"}}
            , new Integer[] {new Integer(1), new Integer(0), new Integer(1), new Integer(0), new Integer(1)}                      
            }, 
            new Object[] {"integerConverter","javax.faces.Integer", null, null, 
                     new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
            },  
            new Object[] {"longRangeValidator","javax.faces.LongRange", null, null, 
                     new Object[] {new String[] {"minimum", "80000"}, 
                        new String[] {"maximum", "80125"}}
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
            },
            new Object[] {"testValueChangeListener","org.apache.shale.clay.config.TestValueChangeListener", null, null, 
                     new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                                         
            },
            new Object[] {"saveCommand","javax.faces.HtmlCommandButton", null, null, 
                     new Object[] {new String[] {"value", "Save"}, 
                        new String[] {"action", "#{@managed-bean-name.save}"},
                        new String[] {"actionListener", "#{@managed-bean-name.saveAction}"}}  
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(1), new Integer(0)}                      
             },
            new Object[] {"testActionListener","org.apache.shale.clay.config.TestActionListener", null, null, 
                     new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}                      
            }  
    };

    
    //Data that is used to verify the inheritance is working from the HTML file.
    //jsfid || id, componentType, facetName, allowBody, attributes[]
    //aggregates (converters, children, validators, actionListeners, valueChangeListeners)
    protected static final Object[] CUSTOM_HTML_COMPONENTS =  {
             new Object[] {"street1Label","javax.faces.HtmlOutputLabel", null, "false", 
                                new Object[] {new String[] {"style", "color:blue"}, 
                                              new String[] {"value", "Street 1:"},
                                              new String[] {"for", "street1"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
             },
             new Object[] {"street1","javax.faces.HtmlInputText", null, null, 
                     new Object[] {new String[] {"size", "45"}, 
                                   new String[] {"maxlength", "50"},
                                   new String[] {"value", "#{@managed-bean-name.address1}"},  
                                   new String[] {"required", "true"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
             },  
             new Object[] {"street1Message","javax.faces.HtmlMessage", null, "false", 
                     new Object[] {new String[] {"style", "color:red"}, 
                                   new String[] {"for", "street1"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
             },   
             new Object[] {"street2Label","javax.faces.HtmlOutputLabel", null, "false", 
                     new Object[] {new String[] {"style", "color:blue"}, 
                                   new String[] {"value", "Street 2:"},
                                   new String[] {"for", "street2"}}
             , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },
            new Object[] {"street2","javax.faces.HtmlInputText", null, null, 
                     new Object[] {new String[] {"size", "45"}, 
                        new String[] {"maxlength", "50"},
                        new String[] {"value", "#{@managed-bean-name.address2}"},  
                        new String[] {"required", "true"}}
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },  
            new Object[] {"street2Message","javax.faces.HtmlMessage", null, "false", 
                     new Object[] {new String[] {"style", "color:red"}, 
                        new String[] {"for", "street2"}}
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },  
            new Object[] {"zip","javax.faces.HtmlInputText", null, null, 
                     new Object[] {new String[] {"size", "9"}, 
                        new String[] {"maxlength", "9"},
                        new String[] {"value", "#{@managed-bean-name.zip}"},  
                        new String[] {"valueChangeListener", "#{@managed-bean-name.zipValueChange}"}}
            , new Integer[] {new Integer(1), new Integer(0), new Integer(1), new Integer(0), new Integer(1)}
            }, 
            new Object[] {"integerConverter","javax.faces.Integer", null, null, 
                     new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },  
            new Object[] {"longRangeValidator","javax.faces.LongRange", null, null, 
                     new Object[] {new String[] {"minimum", "80000"}, 
                        new String[] {"maximum", "80125"}}
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },
            new Object[] {"testValueChangeListener","org.apache.shale.clay.config.TestValueChangeListener", null, null, 
                     new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            },
            new Object[] {"saveCommand","javax.faces.HtmlCommandButton", null, null, 
                     new Object[] {new String[] {"value", "Save"}, 
                        new String[] {"action", "#{@managed-bean-name.save}"},
                        new String[] {"actionListener", "#{@managed-bean-name.saveAction}"}}  
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(1), new Integer(0)}
            },
            new Object[] {"testActionListener","org.apache.shale.clay.config.TestActionListener", null, null, 
              new Object[0]
            , new Integer[] {new Integer(0), new Integer(0), new Integer(0), new Integer(0), new Integer(0)}
            }  
    };
    
    
    
    // loads the xml config files and validates a sample of 
    // components state to the know good state.
    public void testCustomConfigFile() {

        //loads the default and the custom address config files
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");
       
        ComponentBean bean = standardConfigBean.getElement("agentAddressForm");
        assertNotNull(bean);
        
        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_XML_COMPONENTS, true);    
        }
    }
    
    
    // loads the HTML document fragment into a graph of
    // shale meta component data and validates the select
    // sample to the known good state
    public void testLoadHTMLFile() {

        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");

        ComponentBean bean = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address2.html"); 
        assertNotNull(bean);

        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_HTML_COMPONENTS, true);    
        } 
        
    }

    
    // loads the XHTML document fragment into a graph of
    // shale meta component data and validates the select
    // sample to the known good state
    public void testLoadXHTMLNamespaceFile1() {

        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");

        ComponentBean bean = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address1.html"); 
        assertNotNull(bean);

        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_HTML_COMPONENTS, true);    
        } 
        
    }

    // loads the XHTML document fragment into a graph of
    // shale meta component data and validates the select
    // sample to the known good state
    public void testLoadXHTMLNamespaceFile3() {

        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");

        ComponentBean bean = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address3.html"); 
        assertNotNull(bean);

        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_HTML_COMPONENTS, true);    
        } 
        
    }
    
    public void testLoadXHTMLJsfHtmlCore4() {

        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");

        ComponentBean bean = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address4.html"); 
        assertNotNull(bean);

        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_HTML_COMPONENTS, true);    
        } 
        
    }

    public void testLoadXHTMLJsfHtmlCore5() {

        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");

        ComponentBean bean = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address5.html"); 
        assertNotNull(bean);

        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ElementBean child = (ElementBean) ci.next();
           //look for a component that we have setup to test
           checkComponent(child, CUSTOM_HTML_COMPONENTS, false);    
        } 
        
    }

    
    
    //test a full xml view including a html template (on-demand)
    public void testLoadXMLFileOnDemand() {
        
        //loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");
        
        ComponentBean bean = xmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/address.xml"); 
        assertNotNull(bean);
        
        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
            ElementBean child = (ElementBean) ci.next();
            
            AttributeBean attr = null;
            if (child.getId().equals("htmlBegin")) {
                attr = child.getAttribute("value");
                assertEquals("<html>", attr.getValue());
            } else if (child.getId().equals("htmlEnd")) {
                attr = child.getAttribute("value");
                assertEquals("</html>", attr.getValue());
            } else if (child.getId().equals("header")) {
                attr = child.getAttribute("value");
                assertEquals("<head><title>Testing</title></head>", attr.getValue());
            } else if (child.getId().equals("bodyBegin")) {
                attr = child.getAttribute("value");
                assertEquals("<body>", attr.getValue());
            } else if (child.getId().equals("bodyEnd")) {
                attr = child.getAttribute("value");
                assertEquals("</body>", attr.getValue());
            } else if (child.getId().equals("content")) {
                //look for a component that we have setup to test
                checkComponent(child, CUSTOM_HTML_COMPONENTS, true); 
            }
        } 
    }
   
    
    //test a full xml view centralized config loaded on startup that 
    //includes a html template (global template file)
    public void testLoadXMLFileGlobal() {
        
        // loads the default and the custom address config file.
        // loads a centralized full view config file.   
        loadConfigFiles("/org/apache/shale/clay/config/address-config.xml", 
                        "/org/apache/shale/clay/config/address-fullxml.xml");
        
        //loaded on startup, this call will just retrieve a loaded definitions
        //the jsfid doesn't match the name of the config file, a requirement
        //for on-demand full xml views
        ComponentBean bean = xmlTemplateConfigBean.getElement("/address2.xml"); 
        assertNotNull(bean);
        
        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
            ElementBean child = (ElementBean) ci.next();
            
            AttributeBean attr = null;
            if (child.getId().equals("htmlBegin")) {
                attr = child.getAttribute("value");
                assertEquals("<html>", attr.getValue());
            } else if (child.getId().equals("htmlEnd")) {
                attr = child.getAttribute("value");
                assertEquals("</html>", attr.getValue());
            } else if (child.getId().equals("header")) {
                attr = child.getAttribute("value");
                assertEquals("<head><title>Testing</title></head>", attr.getValue());
            } else if (child.getId().equals("bodyBegin")) {
                attr = child.getAttribute("value");
                assertEquals("<body>", attr.getValue());
            } else if (child.getId().equals("bodyEnd")) {
                attr = child.getAttribute("value");
                assertEquals("</body>", attr.getValue());
            } else if (child.getId().equals("content")) {
                //look for a component that we have setup to test
                checkComponent(child, CUSTOM_HTML_COMPONENTS, true); 
            }
        } 
        
    }
    
 
    // checks a meta components realized state against the assumed values
    protected void checkComponent(ComponentBean bean, Object[] knownGoodStates, boolean useJsfid) {
       
       String sarg = null;
       int indx = -1;
       if (useJsfid)
          sarg = bean.getJsfid();
       else
          sarg = bean.getId();
          
       indx = indexOf(sarg, knownGoodStates);
             
       int converterCnt = -1;
       int childrenCnt = -1;
       int validatorCnt = -1;
       int actionListenerCnt = -1;
       int valueChangeListenerCnt = -1;
       
       
       if (indx > -1) {
            Object[] compData = (Object[]) knownGoodStates[indx];
            
            
            if (useJsfid) {
               String jsfid = (String) compData[0];
               assertEquals("check.jsfid for" + sarg, jsfid, bean.getJsfid());
            } else {
                String id = (String) compData[0];
                assertEquals("check.id for " + sarg, id, bean.getId());                
            }

            String componentType = (String) compData[1];
            assertEquals("check.componentType for " + sarg, componentType, bean.getComponentType());

            String facetName = (String) compData[2];
            assertEquals("check.facetName for " + sarg, facetName, bean.getFacetName());

            String allowBody = (String) compData[3];
            assertEquals("check.allowBody for " + sarg, allowBody, bean.getAllowBody());

            Object[] attributes = (Object[]) compData[4];
            for (int i = 0; i < attributes.length; i++) {
                String[] valuepair = (String[]) attributes[i];

                AttributeBean attr = bean.getAttribute(valuepair[0]);
                assertNotNull("check.attribute for " + sarg, attr);
                //System.out.println("\t" + attr.toString());
                assertEquals("check.attribute.value for " + sarg, valuepair[1], attr.getValue());
            }
            
            // the know aggregate object counts
            Integer[] aggregateCnts = (Integer[]) compData[5];
            converterCnt = aggregateCnts[0].intValue();
            childrenCnt = aggregateCnts[1].intValue();
            validatorCnt = aggregateCnts[2].intValue();
            actionListenerCnt = aggregateCnts[3].intValue();
            valueChangeListenerCnt = aggregateCnts[4].intValue();

        }
       
        if (bean.getConverter() !=  null) {
            if (converterCnt > -1)
               assertEquals("check.converter.cnt for " + sarg, 1, converterCnt);
            
            checkComponent(bean.getConverter(), knownGoodStates, useJsfid);    
        }
        
        if (childrenCnt > -1) {
           assertEquals("check.children.cnt for " + sarg, childrenCnt, bean.getChildren().size());    
        }
        
        Iterator ci = bean.getChildrenIterator();
        while (ci.hasNext()) {
           ComponentBean child = (ComponentBean) ci.next();
           checkComponent(child, knownGoodStates, useJsfid);
        }

        if (validatorCnt > -1) {
            assertEquals("check.validator.cnt for " + sarg, validatorCnt, bean.getValidators().size());    
        }
        
        ci = bean.getValidatorIterator();
        while (ci.hasNext()) {
           ComponentBean child = (ComponentBean) ci.next();
           checkComponent(child, knownGoodStates, useJsfid);
        }

        if (actionListenerCnt > -1) {
            assertEquals("check.actionListener.cnt for " + sarg, actionListenerCnt, bean.getActionListeners().size());    
        }
        
        ci = bean.getActionListenerIterator();
        while (ci.hasNext()) {
           ComponentBean child = (ComponentBean) ci.next();
           checkComponent(child, knownGoodStates, useJsfid);
        }

        if (valueChangeListenerCnt > -1) {
            assertEquals("check.valueChangeListener.cnt for " + sarg, valueChangeListenerCnt, bean.getValueChangeListeners().size());    
        }
               
        ci = bean.getValueChangeListenerIterator();
        while (ci.hasNext()) {
           ComponentBean child = (ComponentBean) ci.next();
           checkComponent(child, knownGoodStates, useJsfid);
        }

    }
    
    // looks to see if there is a state check for the components
    protected int indexOf(String jsfid, Object[] knownGoodStates) {
         for (int i = 0; i < knownGoodStates.length; i++) {
             Object[] compData = (Object[]) knownGoodStates[i];
             String id = (String) compData[0];
             if (id.equals(jsfid)) {
                return i;   
             }
         }
        return -1;
    }
   
        
    // test duplicate id check
    public void testDuplicateComponentIds() {
        
        // loads the default and the custom address config file
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");
        
        try {
            ComponentBean bean = htmlTemplateConfigBean.getElement("org/apache/shale/clay/config/duplicate1.html");
            assertTrue("Duplicate component check", false);
        } catch (RuntimeException e) {
            assertTrue(
                    "Duplicate component check",
                    e.getMessage().startsWith("A duplicate component id (street1) was found within the same naming container"));
        }
        
        
        ComponentBean bean = htmlTemplateConfigBean.getElement("org/apache/shale/clay/config/duplicate2.html");
        assertNotNull("Duplicate component check", bean);
        
        
    } 
       

    
}
