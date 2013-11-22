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

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.component.Clay;
import org.apache.shale.test.mock.MockRenderKit;

public class AssignViewRootTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public AssignViewRootTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {
        return (new TestSuite(AssignViewRootTestCase.class));
    }

    private Clay clay = null;

    protected void setUp() throws Exception {
        super.setUp();

        loadConfigFiles(null, null);

    }

    public void testAssign1() throws Exception {        

        RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit("MY_KIT1", renderKit);

        buildSubtree("/org/apache/shale/clay/config/viewroot1.html");
        
        String renderKitId = facesContext.getViewRoot().getRenderKitId();
        assertEquals("renderKitId", "MY_KIT1", renderKitId);
        
        Locale locale = facesContext.getViewRoot().getLocale();
        assertEquals("locale", "ja_JP", locale.toString());

        UIComponent input = findComponent(clay, "input1");
        assertNotNull("child 1 assigned", input);

        input = findComponent(clay, "input2");
        assertNotNull("child 2 assigned", input);

    }

    public void testAssign2() throws Exception {        
  
        RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit("MY_KIT2", renderKit);

        buildSubtree("/org/apache/shale/clay/config/viewroot2.html");
        
        String renderKitId = facesContext.getViewRoot().getRenderKitId();
        assertEquals("renderKitId", "MY_KIT2", renderKitId);
        
        Locale locale = facesContext.getViewRoot().getLocale();
        assertEquals("locale", "pt_PT", locale.toString());

        UIComponent input = findComponent(clay, "input1");
        assertNotNull("child 1 assigned", input);

        input = findComponent(clay, "input2");
        assertNotNull("child 2 assigned", input);

    }

    public void testAssign3() throws Exception {        

        RenderKitFactory renderKitFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit("MY_KIT3", renderKit);

        buildSubtree("/org/apache/shale/clay/config/viewroot3.html");
        
        String renderKitId = facesContext.getViewRoot().getRenderKitId();
        assertEquals("renderKitId", "MY_KIT3", renderKitId);
        
        Locale locale = facesContext.getViewRoot().getLocale();
        assertEquals("locale", "de", locale.toString());

        UIComponent input = findComponent(clay, "input1");
        assertNotNull("child 1 assigned", input);

        input = findComponent(clay, "input2");
        assertNotNull("child 2 assigned", input);

    }

    private void buildSubtree(String jsfid) throws Exception {
        clay = (Clay) application.createComponent("org.apache.shale.clay.component.Clay");
        clay.setId("test");
        clay.setJsfid(jsfid);
        clay.setManagedBeanName("test");
        facesContext.getViewRoot().getChildren().add(0, clay);

        //      builds a buffer to write the page to
        StringWriter writer = new StringWriter();
        //      create a buffered response writer
        ResponseWriter buffResponsewriter = facesContext.getRenderKit()
            .createResponseWriter(writer, null,
            response.getCharacterEncoding());
        //      push buffered writer to the faces context
        facesContext.setResponseWriter(buffResponsewriter);
        //      start a document
        buffResponsewriter.startDocument();

        //      build subtree
        clay.encodeBegin(facesContext);

    }
    
    private UIComponent findComponent(UIComponent parent, String id) {
        if (parent == null) {
            return null;
        }

        if (parent.getId() != null && parent.getId().equals(id)) {
            return parent;
        } else {
            Iterator ci = parent.getChildren().iterator();
            while (ci.hasNext()) {
                UIComponent child = (UIComponent) ci.next();
                UIComponent target = findComponent(child, id);
                if (target != null) {
                    return target;
                }
            }
        }

        return null;
    }



}
