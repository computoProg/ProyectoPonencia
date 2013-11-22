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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.SymbolBean;

public class LimitedJspxTestCase extends AbstractTestCaseConfig {

    // Construct a new instance of this test case.
    public LimitedJspxTestCase(String name) {
        super(name);
    }

    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(LimitedJspxTestCase.class));
    }

    public void testJpx() {
        loadConfigFile(null);

        ComponentBean document = htmlTemplateConfigBean.getElement("/org/apache/shale/clay/config/limited-jspx.html");
        assertNotNull(document);

        assertEquals("void component count", 2, countVoidComponents(document));

        List clayIncludes = findClayJspxInclude(document);
        assertEquals("includes", 2, clayIncludes.size());

        Iterator ci = clayIncludes.iterator();
        while (ci.hasNext()) {
            ComponentBean clayInclude = (ComponentBean) ci.next();
            if (clayInclude.getSymbol("@file") != null) {

                SymbolBean symbol = clayInclude.getSymbol("@file");
                assertNotNull("file symbol", symbol);
                assertEquals("/org/apache/shale/clay/config/address1.html", symbol.getValue());

                AttributeBean attr = clayInclude.getAttribute("clayJsfid");
                assertNotNull("clayJsfid", attr);
                assertEquals("@file", attr.getValue());


            } else if (clayInclude.getSymbol("@page") != null) {

                SymbolBean symbol = clayInclude.getSymbol("@page");
                assertNotNull("file symbol", symbol);
                assertEquals("/org/apache/shale/clay/config/address1.html", symbol.getValue());

                AttributeBean attr = clayInclude.getAttribute("clayJsfid");
                assertNotNull("clayJsfid", attr);
                assertEquals("@page", attr.getValue());

                for (int i = 0; i < 5; i++) {
                    symbol = clayInclude.getSymbol("@symbol" + i);
                    assertNotNull("@symbol" + i, symbol);
                    assertEquals("value" + i, symbol.getValue());
                }

            } else {
                assertFalse("invalid include mapping", true);
            }
        }

    }

    // counts the number of void components in the tree
    public int countVoidComponents(ComponentBean bean) {
        int voidcnt = 0;

        if (bean.getJsfid().equals("void")) {
            voidcnt++;
        }

        Iterator ci = bean.getChildren().iterator();
        while (ci.hasNext()) {
            ComponentBean child = (ComponentBean) ci.next();
            voidcnt += countVoidComponents(child);
        } 

        return voidcnt;
    }

    // find beans with a jsfid equal to "clay"
    public List findClayJspxInclude(ComponentBean bean) {
        List beans = new ArrayList();

        if (bean.getJsfid().equals("clay")) {
            beans.add(bean);
        }
        Iterator ci = bean.getChildren().iterator();
        while (ci.hasNext()) {
            beans.addAll(findClayJspxInclude((ComponentBean) ci.next()));
        }        

        return beans;
    }
}
