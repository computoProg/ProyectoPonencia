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
import org.apache.shale.clay.config.beans.TemplateConfigBean;

// tests squeezing the component tree
public class OptimizeTreeTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public OptimizeTreeTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(OptimizeTreeTestCase.class));

    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    private ComponentBean createVerbatim(Class classz, String value)
            throws InstantiationException, IllegalAccessException {

        ComponentBean target = (ComponentBean) classz.newInstance();
        target.setJsfid("verbatim");
        target.setComponentType("javax.faces.HtmlOutputText");

        AttributeBean attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_VALUE);
        attr.setName("value");
        attr.setValue(value);
        target.addAttribute(attr);

        attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setName("escape");
        attr.setValue(Boolean.FALSE.toString());
        target.addAttribute(attr);

        attr = new AttributeBean();
        attr.setBindingType(AttributeBean.BINDING_TYPE_NONE);
        attr.setName("isTransient");
        attr.setValue(Boolean.TRUE.toString());
        target.addAttribute(attr);

        return target;
    }

    public void testRollup() throws Exception {

        ComponentBean root = createVerbatim(ComponentBean.class, "0");
        for (int i = 1; i < 10; i++) {
            ElementBean child = (ElementBean) createVerbatim(ElementBean.class,
                    String.valueOf(i));
            child.setRenderId(i);
            root.addChild(child);
        }

        ((TemplateConfigBean) htmlTemplateConfigBean).optimizeTree(root);

        assertEquals("#Children", 0, root.getChildren().size());

        AttributeBean attr = root.getAttribute("value");
        assertNotNull(attr);

        assertEquals("root value", "0123456789", attr.getValue());

    }

    public void testRollupNested() throws Exception {

        // root 0
        // + 1
        // + 2
        // + 3
        // + 4
        //   + 5
        //   + 6
        //   + 7
        //   + 8
        //   + 9
        ComponentBean root = createVerbatim(ComponentBean.class, "0");
        ElementBean lastChild = null;
        for (int i = 1; i < 5; i++) {
            lastChild = (ElementBean) createVerbatim(ElementBean.class, String
                    .valueOf(i));
            lastChild.setRenderId(i);
            root.addChild(lastChild);
        }

        for (int i = 5; i < 10; i++) {
            ElementBean child = (ElementBean) createVerbatim(ElementBean.class,
                    String.valueOf(i));
            child.setRenderId(i);
            lastChild.addChild(child);
        }

        ((TemplateConfigBean) htmlTemplateConfigBean).optimizeTree(root);

        assertEquals("#Children", 0, root.getChildren().size());

        AttributeBean attr = root.getAttribute("value");
        assertNotNull(attr);

        assertEquals("root value", "0123456789", attr.getValue());

    }

    public void testInterwoven() throws Exception {

        // 0
        // +1
        // +2
        // +3 not verbatim
        // +4
        // +5 not verbatim
        // +6
        // +7 not verbatim
        // +8
        // +9

        AttributeBean attr = null;
        ElementBean child = null;
        ComponentBean root = createVerbatim(ComponentBean.class, "0");
        // root is not a verbatim
        root.setJsfid("outputText");

        child = (ElementBean) createVerbatim(ElementBean.class, "1");
        child.setRenderId(1);
        root.addChild(child);
        

        child = (ElementBean) createVerbatim(ElementBean.class, "2");
        child.setRenderId(2);
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "3");
        child.setRenderId(3);
        // root is not a verbatim
        child.setJsfid("outputText");
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "4");
        child.setRenderId(4);
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "5");
        child.setRenderId(5);
        // root is not a verbatim
        child.setJsfid("outputText");
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "6");
        child.setRenderId(6);
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "7");
        child.setRenderId(7);
        // root is not a verbatim
        child.setJsfid("outputText");
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "8");
        child.setRenderId(8);
        root.addChild(child);

        child = (ElementBean) createVerbatim(ElementBean.class, "9");
        child.setRenderId(9);
        root.addChild(child);

        ((TemplateConfigBean) htmlTemplateConfigBean).optimizeTree(root);

        assertEquals("#Children", 7, root.getChildren().size());

        Iterator ci = root.getChildren().iterator();
        int i = 0;
        while (ci.hasNext()) {
            child = (ElementBean) ci.next();

            switch (++i) {
            case 1: {
                // first two nodes merged
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "12", attr.getValue());
                break;
            }
            case 2: {
                // non-verbatim
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "3", attr.getValue());
                break;
            }
            case 3: {
                // verbatim non-adjacent
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "4", attr.getValue());
                break;
            }
            case 4: {
                // non-verbatim
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "5", attr.getValue());
                break;
            }
            case 5: {
                // verbatim non-adjacent
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "6", attr.getValue());
                break;
            }
            case 6: {
                // non-verbatim
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "7", attr.getValue());
                break;
            }
            case 7: {
                // first two nodes merged
                attr = child.getAttribute("value");
                assertNotNull(attr);
                assertEquals("root value", "89", attr.getValue());
                break;
            }
            
            };
        }

    }

}
