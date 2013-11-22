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
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.component.UIComponent;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.chain.Command;
import org.apache.shale.clay.component.chain.AbstractCommand;
import org.apache.shale.clay.component.chain.ClayContext;
import org.apache.shale.clay.component.chain.CreateComponentCommand;
import org.apache.shale.clay.component.chain.PropertyValueCommand;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;

// tests properties and symbol evaluation
public class SymbolsTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public SymbolsTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(SymbolsTestCase.class));

    }
 
    public void testNestedSymbolReplacement() {
        
        
        Map symbols = new TreeMap();
        
        ClayContext clayContext = new ClayContext();
        clayContext.setFacesContext(facesContext);
        clayContext.setSymbols(symbols);
        
        SymbolBean symbol = this.createSymbol("@a", "@b");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@b", "@c");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@c", "@d");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@d", "test");
        symbols.put(symbol.getName(), symbol);
        
        AbstractCommand.realizeSymbols(clayContext);
        
        symbol = (SymbolBean) symbols.get("@a");
        assertNotNull(symbol);
        assertEquals("test", symbol.getValue());
        
        symbol = (SymbolBean) symbols.get("@b");
        assertNotNull(symbol);
        assertEquals("test", symbol.getValue());
        
        symbol = (SymbolBean) symbols.get("@c");
        assertNotNull(symbol);
        assertEquals("test", symbol.getValue());
        
        symbol = (SymbolBean) symbols.get("@d");
        assertNotNull(symbol);
        assertEquals("test", symbol.getValue());
        
        
        symbols.clear();
        
        symbol = this.createSymbol("@a", "@b");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@b", "@a");
        symbols.put(symbol.getName(), symbol);
        
        AbstractCommand.realizeSymbols(clayContext);
        
        symbol = (SymbolBean) symbols.get("@a");
        assertNotNull(symbol);
        assertEquals("@a", symbol.getValue());
        
        symbol = (SymbolBean) symbols.get("@b");
        assertNotNull(symbol);
        assertEquals("@a", symbol.getValue());
        
        
        
        symbols.clear();
        
        symbol = this.createSymbol("@foo", "@xbeanx.@xpropertyx");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@xbeanx", "@a");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@xpropertyx", "@b");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@a", "foo");
        symbols.put(symbol.getName(), symbol);
        
        symbol = this.createSymbol("@b", "bar");
        symbols.put(symbol.getName(), symbol);
        
        AbstractCommand.realizeSymbols(clayContext);
        
        symbol = (SymbolBean) symbols.get("@foo");
        assertNotNull(symbol);
        assertEquals("foo.bar", symbol.getValue());
        
    }
    
    
    
    public void testGenericPropertyCommand () throws Exception {
        javax.faces.component.html.HtmlOutputText child = (javax.faces.component.html.HtmlOutputText) 
                                facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
       assertNotNull("javax.faces.HtmlOutputText", child);
       
       
       AttributeBean attr = new AttributeBean();
       attr.setName("value");
       attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
       attr.setValue("10");
       
       ComponentBean displayElement = new ComponentBean();
       displayElement.setJsfid("inputText");
       displayElement.setComponentType("javax.faces.HtmlOutputText");
       displayElement.setId("testId");
       displayElement.addAttribute(attr);
       
       assertNotNull("attribute case insensitive", displayElement.getAttribute("VaLue"));
       
       ClayContext clayContext = new ClayContext();
       clayContext.setFacesContext(facesContext);
       clayContext.setChild(child);
       clayContext.setAttribute(attr);
       clayContext.setDisplayElement(displayElement);
              
       Command command = new PropertyValueCommand();
       boolean isFinal = command.execute(clayContext);
       assertEquals("command finished", true, isFinal);       
       assertEquals("value = 10", child.getValue(), "10");

       
       child = (javax.faces.component.html.HtmlOutputText) 
                               facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
       assertNotNull("javax.faces.HtmlOutputText", child);
       clayContext.setChild(child);
       
       servletContext.setAttribute("goodYear", "1969");
       attr.setBindingType(AttributeBean.BINDING_TYPE_VALUE);
       attr.setValue("#{goodYear}");

       isFinal = command.execute(clayContext);
       assertEquals("command finished", true, isFinal);       
       assertEquals("value = 1969", "1969", child.getValue());
       
       child = (javax.faces.component.html.HtmlOutputText) 
                             facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
       assertNotNull("javax.faces.HtmlOutputText", child);
       clayContext.setChild(child);
       
       servletContext.setAttribute("ping", "pong");
       attr.setBindingType(AttributeBean.BINDING_TYPE_EARLY);
       attr.setValue("#{ping}");

       isFinal = command.execute(clayContext);
       assertEquals("command finished", true, isFinal);       
       assertEquals("value = pong", child.getValue(), "pong");


       child = (javax.faces.component.html.HtmlOutputText) 
                               facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
       assertNotNull("javax.faces.HtmlOutputText", child);
       clayContext.setChild(child);
       
       attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
       attr.setValue("#{forManfred}");

       isFinal = command.execute(clayContext);
       assertEquals("command finished", true, isFinal);       
       assertEquals("value = #{forManfred}", "#{forManfred}", child.getValue());

        
    }
   
    //factory method for creating a symbol bean
    private SymbolBean createSymbol(String name, String value) {
        SymbolBean symbol = new SymbolBean();
        symbol.setName(name);
        symbol.setValue(value);
        return symbol;
    }   
    
    // test symbolic property replacement
    public void testSymbolicProperties() throws Exception {
           javax.faces.component.html.HtmlOutputText child = (javax.faces.component.html.HtmlOutputText) 
                              facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);
           
           
           AttributeBean attr = new AttributeBean();
           attr.setName("value");
           attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
           attr.setValue("@value");  //symbolic attribute 
                      
           ComponentBean displayElement = new ComponentBean();
           displayElement.setJsfid("inputText");
           displayElement.setComponentType("javax.faces.HtmlOutputText");
           displayElement.setId("testId");
           displayElement.addAttribute(attr);
           displayElement.addSymbol(createSymbol("@value", "10"));
                      
           ClayContext clayContext = new ClayContext();
           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           clayContext.setSymbols(displayElement.getSymbols());
           
           Command command = new PropertyValueCommand();
           boolean isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = 10", "10", child.getValue());

           
           // test a symbol value of an el value
           child = (javax.faces.component.html.HtmlOutputText) 
                        facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);

           displayElement.addSymbol(createSymbol("@value", "#{value}"));
           attr.setBindingType(AttributeBean.BINDING_TYPE_EARLY);
           servletContext.setAttribute("value", "10");
          
           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           clayContext.setSymbols(displayElement.getSymbols());
           
           isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = 10", "10", child.getValue());

           
           // test a symbol value with a null value symbol replacement
           child = (javax.faces.component.html.HtmlOutputText) 
                 facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);

           displayElement.addSymbol(createSymbol("@value", null));
           attr.setBindingType(AttributeBean.BINDING_TYPE_EARLY);
          
           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           clayContext.setSymbols(displayElement.getSymbols());
           
           isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = null", null, child.getValue());


           // test a symbol value with an empty String value.  
           // this will evaluate to null since it is a symbol replacement.
           child = (javax.faces.component.html.HtmlOutputText) 
                     facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);

           displayElement.addSymbol(createSymbol("@value", ""));
           attr.setBindingType(AttributeBean.BINDING_TYPE_EARLY);
          
           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           clayContext.setSymbols(displayElement.getSymbols());
           
           isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = null", null, child.getValue());

           //no symbol replacement for a empty string - should return
           //an empty string.  This allows components like the selectItem
           //to create an empty select list pick.
           attr.setValue("");  //empty string           
           child = (javax.faces.component.html.HtmlOutputText) 
                        facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);

           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           
           isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = \"\"", "", child.getValue());

           //Case insensitive and reoccurring replacement
           attr.setValue("@TeSt1, @tEst1 never @test2; @test1, @teSt1 till ya @tesT3");  //test multiple symbols           
           child = (javax.faces.component.html.HtmlOutputText) 
                     facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
           assertNotNull("javax.faces.HtmlOutputText", child);

           displayElement.addSymbol(createSymbol("@test1", "rock"));
           displayElement.addSymbol(createSymbol("@test2", "stop"));
           displayElement.addSymbol(createSymbol("@test3", "drop"));

           clayContext.setFacesContext(facesContext);
           clayContext.setChild(child);
           clayContext.setAttribute(attr);
           clayContext.setDisplayElement(displayElement);
           // normally done in the AssignChildrenCommand
           clayContext.setSymbols(displayElement.getSymbols());
           
           isFinal = command.execute(clayContext);
           assertEquals("command finished", true, isFinal);       
           assertEquals("value = \"rock, rock never stop; rock, rock till ya drop\"", 
                   "rock, rock never stop; rock, rock till ya drop", child.getValue());

    }

    //test component creation using symbols for component id's 
    public void testCreateComponent()throws Exception {
        
        UIComponent parent = (UIComponent) 
              facesContext.getApplication().createComponent("javax.faces.NamingContainer"); 
        assertNotNull("javax.faces.NamingContainer", parent);
        parent.setId("parent");
        
        ComponentBean displayElement = new ComponentBean();
        displayElement.setJsfid("inputText");
        displayElement.setComponentType("javax.faces.HtmlOutputText");
        displayElement.setId("@wynn");
        displayElement.addSymbol(createSymbol("@wynn", "test"));
        
        ClayContext clayContext = new ClayContext();
        clayContext.setFacesContext(facesContext);
        clayContext.setParent(parent);
        clayContext.setDisplayElement(displayElement);
        clayContext.setSymbols(displayElement.getSymbols());
        clayContext.setJspIds(new TreeSet());
                
        Command command = new CreateComponentCommand();
        boolean isFinal = command.execute(clayContext);
        assertEquals("command finished", false, isFinal);
        
        UIComponent child = (UIComponent) clayContext.getChild();
        assertNotNull("child", child);
        
        assertEquals("id = test", "test", child.getId());
        
        
        //null component id symbol replacement
        parent = (UIComponent) 
             facesContext.getApplication().createComponent("javax.faces.NamingContainer"); 
        assertNotNull("javax.faces.NamingContainer", parent);
        parent.setId("parent");
        
        displayElement = new ComponentBean();
        displayElement.setJsfid("inputText");
        displayElement.setComponentType("javax.faces.HtmlOutputText");
        displayElement.setId("@wynn");
        displayElement.addSymbol(createSymbol("@wynn", null));
        
        clayContext = new ClayContext();
        clayContext.setFacesContext(facesContext);
        clayContext.setParent(parent);
        clayContext.setDisplayElement(displayElement);
        clayContext.setSymbols(displayElement.getSymbols());
        clayContext.setJspIds(new TreeSet());
                
        command = new CreateComponentCommand();
        try {
            isFinal = command.execute(clayContext);
            assertTrue("id replacement failed", false);
        } catch (RuntimeException e) {
            assertTrue("null component id", 
                    e.getMessage().startsWith("The component symbol substitution failed for id \"@wynn\""));    
        }
        
        //missing component id symbol replacement
        parent = (UIComponent) 
             facesContext.getApplication().createComponent("javax.faces.NamingContainer"); 
        assertNotNull("javax.faces.NamingContainer", parent);
        parent.setId("parent");
        
        displayElement = new ComponentBean();
        displayElement.setJsfid("inputText");
        displayElement.setComponentType("javax.faces.HtmlOutputText");
        displayElement.setId("@wynn");
        
        clayContext = new ClayContext();
        clayContext.setFacesContext(facesContext);
        clayContext.setParent(parent);
        clayContext.setDisplayElement(displayElement);
        clayContext.setSymbols(displayElement.getSymbols());
        clayContext.setJspIds(new TreeSet());
                
        command = new CreateComponentCommand();
        try {
            isFinal = command.execute(clayContext);
            assertTrue("id replacement failed", false);
        } catch (RuntimeException e) {
            assertTrue("missing component id", 
                    e.getMessage().startsWith("The component symbol substitution failed for id \"@wynn\""));    
        }

        
    }
    
 
    public void testSymbolDelimiters() throws Exception {

        //create a target component
        javax.faces.component.html.HtmlOutputText child = (javax.faces.component.html.HtmlOutputText) 
                           facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
        assertNotNull("javax.faces.HtmlOutputText", child);
        
        //setup some metadata
        AttributeBean attr = new AttributeBean();
        attr.setName("value");
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setValue("@[a]@[ab]");  //symbolic attribute 
                   
        ComponentBean displayElement = new ComponentBean();
        displayElement.setJsfid("inputText");
        displayElement.setComponentType("javax.faces.HtmlOutputText");
        displayElement.setId("testId");
        displayElement.addAttribute(attr);
        displayElement.addSymbol(createSymbol("@[ab]", "43"));
        displayElement.addSymbol(createSymbol("@[a]", "67"));
             
        ClayContext clayContext = new ClayContext();
        clayContext.setFacesContext(facesContext);
        clayContext.setChild(child);
        clayContext.setAttribute(attr);
        clayContext.setDisplayElement(displayElement);
        // normally done in the AssignChildrenCommand
        clayContext.setSymbols(displayElement.getSymbols());
                
        Command command = new PropertyValueCommand();
        boolean isFinal = command.execute(clayContext);
        assertEquals("command finished", true, isFinal);       
        assertEquals("value = 6743", "6743", child.getValue());      

    
        //create a target component
        child = (javax.faces.component.html.HtmlOutputText) 
                           facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
        assertNotNull("javax.faces.HtmlOutputText", child);

        attr.setValue("@{a}@{ab}");  //symbolic attribute 
        displayElement.addSymbol(createSymbol("@{ab}", "43"));
        displayElement.addSymbol(createSymbol("@{a}", "67"));

        clayContext.setChild(child);

        isFinal = command.execute(clayContext);
        assertEquals("command finished", true, isFinal);       
        assertEquals("value = 6743", "6743", child.getValue());      


        
        //create a target component
        child = (javax.faces.component.html.HtmlOutputText) 
                           facesContext.getApplication().createComponent("javax.faces.HtmlOutputText"); 
        assertNotNull("javax.faces.HtmlOutputText", child);

        attr.setValue("@(a)@(ab)");  //symbolic attribute 
        displayElement.addSymbol(createSymbol("@(ab)", "43"));
        displayElement.addSymbol(createSymbol("@(a)", "67"));

        clayContext.setChild(child);

        isFinal = command.execute(clayContext);
        assertEquals("command finished", true, isFinal);       
        assertEquals("value = 6743", "6743", child.getValue());      
        
        
    }
     
    //test symbol inheritance
    public void testSymbolInheritance() {

        //loads the default and the custom address config files
        loadConfigFile("/org/apache/shale/clay/config/address-config.xml");
          
        // test vertical inheritance
        ComponentBean bean = standardConfigBean.getElement("baseSymbolLabel");
        assertNotNull(bean);
        //look for a base symbol definition
        SymbolBean symbol = (SymbolBean) bean.getSymbols().get("@mystyle");
        assertNotNull(symbol);
        assertEquals("@mystyle == color:blue", "color:blue", symbol.getValue());

        // symbol1Label extends baseSymbolLabel
        bean = standardConfigBean.getElement("symbol1Label");
        assertNotNull(bean);
        //look for inherited symbol
        symbol = (SymbolBean) bean.getSymbols().get("@mystyle");
        assertNotNull(symbol);
        assertEquals("@mystyle == color:blue", "color:blue", symbol.getValue());

        // symbol2Label extends symbol1Label
        bean = standardConfigBean.getElement("symbol2Label");
        assertNotNull(bean);
        //look for an overridden symbol
        symbol = (SymbolBean) bean.getSymbols().get("@mystyle");
        assertNotNull(symbol);
        assertEquals("@mystyle == color:red", "color:red", symbol.getValue());

        
        //test nested/inner element inheritance
        bean = standardConfigBean.getElement("symbolPanel");
        assertNotNull(bean);
        
        assertEquals(bean.getChildren().size(), 2);
        Iterator ei = bean.getChildrenIterator();
        while (ei.hasNext()) {
            ElementBean ebean = (ElementBean) ei.next();
            if (ebean.getRenderId() == 1) {
                //look for inherited symbol
                symbol = (SymbolBean) ebean.getSymbols().get("@mystyle");
                assertNotNull(symbol);
                assertEquals("@mystyle == color:blue", "color:blue", symbol.getValue());                
            } else if (ebean.getRenderId() == 2) {
                //look for an overridden symbol
                symbol = (SymbolBean) ebean.getSymbols().get("@mystyle");
                assertNotNull(symbol);
                assertEquals("@mystyle == color:red", "color:red", symbol.getValue());
                
            }
        }
        
               
    }

    
}
