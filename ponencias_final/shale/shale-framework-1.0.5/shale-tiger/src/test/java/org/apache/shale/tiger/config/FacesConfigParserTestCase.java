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

package org.apache.shale.tiger.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.shale.tiger.managed.config.ListEntriesConfig;
import org.apache.shale.tiger.managed.config.ListEntryConfig;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;
import org.apache.shale.tiger.managed.config.ManagedPropertyConfig;
import org.apache.shale.tiger.managed.config.MapEntriesConfig;
import org.apache.shale.tiger.managed.config.MapEntryConfig;

/**
 * <p>Test case for <code>org.apache.shale.tiger.digester.FacesConfigParser</code>.</p>
 */
public class FacesConfigParserTestCase extends TestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case
    public FacesConfigParserTestCase(String name) {
        super(name);
    }
    

    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        facesConfig = new FacesConfigConfig();
        parser = new FacesConfigParser();

    }


    // Return the tests included in this test case.
    public static Test suite() {
        return new TestSuite(FacesConfigParserTestCase.class);
    }


    // Tear down instance variables required by this test case
    protected void tearDown() throws Exception {

        facesConfig = null;
        parser = null;

    }


    // ------------------------------------------------------ Instance Variables


    // FacesConfigConfig instance representing our parsed metadata
    FacesConfigConfig facesConfig = null;


    // Parser instance to be tested
    FacesConfigParser parser = null;


    // ------------------------------------------------------------ Test Methods


    // Test parsing configuration resource that has lists in it
    public void testList() throws Exception {

        ManagedBeanConfig mb = null;
        ManagedPropertyConfig mp = null;
        ListEntriesConfig entries = null;
        ListEntryConfig entry = null;

        // Wire up our FacesConfigConfig instance
        parser.setFacesConfig(facesConfig);
        assertTrue(facesConfig == parser.getFacesConfig());

        // Make sure we do validating parses
        parser.setValidating(true);
        assertTrue(parser.isValidating());

        // Parse resource 4
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-4.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(4, facesConfig.getManagedBeans().size());

        // Validate bean "explicitSqlDateList"
        mb = facesConfig.getManagedBean("explicitIntegerList");
        assertNotNull(mb);
        assertEquals("explicitIntegerList", mb.getName());
        assertEquals("none", mb.getScope());
        assertEquals("java.util.Vector", mb.getType());
        assertNotNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(0, mb.getProperties().size());

        entries = mb.getListEntries();
        assertEquals("java.lang.Integer", entries.getValueType());
        assertEquals(4, entries.getEntries().size());
        entry = entries.getEntries().get(0);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("123", entry.getValue());
        entry = entries.getEntries().get(1);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("234", entry.getValue());
        entry = entries.getEntries().get(2);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(entry.isNullValue());
        assertNull(entry.getValue());
        entry = entries.getEntries().get(3);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("345", entry.getValue());

        // Validate bean "explicitStringList"
        mb = facesConfig.getManagedBean("explicitStringList");
        assertNotNull(mb);
        assertEquals("explicitStringList", mb.getName());
        assertEquals("request", mb.getScope());
        assertEquals("java.util.LinkedList", mb.getType());
        assertNotNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(0, mb.getProperties().size());

        entries = mb.getListEntries();
        assertEquals("java.lang.String", entries.getValueType());
        assertEquals(5, entries.getEntries().size());
        entry = entries.getEntries().get(0);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("foo", entry.getValue());
        entry = entries.getEntries().get(1);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("bar", entry.getValue());
        entry = entries.getEntries().get(2);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(entry.isNullValue());
        assertNull(entry.getValue());
        entry = entries.getEntries().get(3);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("baz", entry.getValue());
        entry = entries.getEntries().get(4);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("bop", entry.getValue());

        // Validate bean "implicitStringList"
        mb = facesConfig.getManagedBean("implicitStringList");
        assertNotNull(mb);
        assertEquals("implicitStringList", mb.getName());
        assertEquals("session", mb.getScope());
        assertEquals("java.util.ArrayList", mb.getType());
        assertNotNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(0, mb.getProperties().size());

        entries = mb.getListEntries();
        assertEquals(null, entries.getValueType());
        assertEquals(5, entries.getEntries().size());
        entry = entries.getEntries().get(0);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("bop", entry.getValue());
        entry = entries.getEntries().get(1);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(entry.isNullValue());
        assertNull(entry.getValue());
        entry = entries.getEntries().get(2);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("baz", entry.getValue());
        entry = entries.getEntries().get(3);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("bar", entry.getValue());
        entry = entries.getEntries().get(4);
        assertNotNull(entry);
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());
        assertEquals("foo", entry.getValue());

        // Validate bean "listPropertiesBean"
        mb = facesConfig.getManagedBean("listPropertiesBean");
        assertNotNull(mb);
        assertEquals("listPropertiesBean", mb.getName());
        assertEquals("application", mb.getScope());
        assertEquals("org.apache.shale.tiger.config.TestBean4", mb.getType());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(2, mb.getProperties().size());

        mp = mb.getProperty("emptyList");
        assertNotNull(mp);
        assertEquals("emptyList", mp.getName());
        assertNull(mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        assertNotNull(mp.getListEntries());
        assertEquals(5, mp.getListEntries().getEntries().size());

        mp = mb.getProperty("fullList");
        assertNotNull(mp);
        assertEquals("fullList", mp.getName());
        assertNull(mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        assertNotNull(mp.getListEntries());
        assertEquals(5, mp.getListEntries().getEntries().size());

    }


    // Test parsing configuration resource that has maps in it
    public void testMap() throws Exception {

        ManagedBeanConfig mb = null;
        ManagedPropertyConfig mp = null;
        MapEntriesConfig entries = null;
        MapEntryConfig entry = null;

        // Wire up our FacesConfigConfig instance
        parser.setFacesConfig(facesConfig);
        assertTrue(facesConfig == parser.getFacesConfig());

        // Make sure we do validating parses
        parser.setValidating(true);
        assertTrue(parser.isValidating());

        // Parse resource 5
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-5.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(2, facesConfig.getManagedBeans().size());

        // Validate bean "stringDateMap"
        mb = facesConfig.getManagedBean("stringIntegerMap");
        assertNotNull(mb);
        assertEquals("stringIntegerMap", mb.getName());
        assertEquals("none", mb.getScope());
        assertEquals("java.util.TreeMap", mb.getType());
        assertNull(mb.getListEntries());
        assertNotNull(mb.getMapEntries());
        assertEquals(0, mb.getProperties().size());

        entries = mb.getMapEntries();
        assertEquals("java.lang.String", entries.getKeyType());
        assertEquals("java.lang.Integer", entries.getValueType());
        assertEquals(4, entries.getEntries().size());

        entry = entries.getEntries().get(0);
        assertEquals("First", entry.getKey());
        assertEquals("123", entry.getValue());
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());

        entry = entries.getEntries().get(1);
        assertEquals("Second", entry.getKey());
        assertEquals("234", entry.getValue());
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());

        entry = entries.getEntries().get(2);
        assertEquals("Third", entry.getKey());
        assertNull(entry.getValue());
        assertTrue(!entry.isExpression());
        assertTrue(entry.isNullValue());

        entry = entries.getEntries().get(3);
        assertEquals("Fourth", entry.getKey());
        assertEquals("345", entry.getValue());
        assertTrue(!entry.isExpression());
        assertTrue(!entry.isNullValue());

        // Validate bean "mapPropertiesBean"
        mb = facesConfig.getManagedBean("mapPropertiesBean");
        assertNotNull(mb);
        assertEquals("mapPropertiesBean", mb.getName());
        assertEquals("application", mb.getScope());
        assertEquals("org.apache.shale.tiger.config.TestBean5", mb.getType());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(2, mb.getProperties().size());

        mp = mb.getProperty("emptyMap");
        assertNotNull(mp);
        assertEquals("emptyMap", mp.getName());
        assertNull(mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        assertNull(mp.getListEntries());
        assertNotNull(mp.getMapEntries());

        entries = mp.getMapEntries();
        assertNull(entries.getKeyType());
        assertNull(entries.getValueType());
        assertEquals(3, entries.getEntries().size());

        entry = entries.getEntries().get(0);
        assertTrue(!entry.isExpression());
        assertEquals("First Key", entry.getKey());
        assertEquals("First Value", entry.getValue());

        entry = entries.getEntries().get(1);
        assertTrue(!entry.isExpression());
        assertEquals("Second Key", entry.getKey());
        assertEquals("Second Value", entry.getValue());

        entry = entries.getEntries().get(2);
        assertTrue(!entry.isExpression());
        assertEquals("Third Key", entry.getKey());
        assertEquals("Third Value", entry.getValue());

        mp = mb.getProperty("fullMap");
        assertNotNull(mp);
        assertEquals("fullMap", mp.getName());
        assertNull(mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        assertNull(mp.getListEntries());
        assertNotNull(mp.getMapEntries());

        entries = mp.getMapEntries();
        assertNull(entries.getKeyType());
        assertNull(entries.getValueType());
        assertEquals(3, entries.getEntries().size());

        entry = entries.getEntries().get(0);
        assertTrue(!entry.isExpression());
        assertEquals("First Key", entry.getKey());
        assertEquals("First Value", entry.getValue());

        entry = entries.getEntries().get(1);
        assertTrue(!entry.isExpression());
        assertEquals("Second Key", entry.getKey());
        assertEquals("Second Value", entry.getValue());

        entry = entries.getEntries().get(2);
        assertTrue(!entry.isExpression());
        assertEquals("Third Key", entry.getKey());
        assertEquals("Third Value", entry.getValue());

    }


        // Test pristine instance of the parser
    public void testPristine() {

        assertNull(parser.getFacesConfig());
        assertNull(parser.getResource());
        assertTrue(parser.isValidating());

        assertEquals(0, facesConfig.getManagedBeans().size());
    }


    // Test a static parse of our configuration resources that should
    // reflect appropriate information merging, but should *not* reflect
    // any annotations on included classes
    public void testStatic() throws Exception {

        ManagedBeanConfig mb = null;
        ManagedPropertyConfig mp = null;

        // Wire up our FacesConfigConfig instance
        parser.setFacesConfig(facesConfig);
        assertTrue(facesConfig == parser.getFacesConfig());

        // Make sure we do validating parses
        parser.setValidating(true);
        assertTrue(parser.isValidating());

        // Parse resource 0
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-0.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(1, facesConfig.getManagedBeans().size());

        // Validate bean0 conditions after parsing resource 0
        mb = facesConfig.getManagedBean("bean0");
        assertNotNull(mb);
        assertEquals("bean0", mb.getName());
        assertEquals("request", mb.getScope());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(0, mb.getProperties().size());

        // Validate bean1 conditions after parsing resource 0
        assertNull(mb.getProperty("bean1"));

        // Validate bean2 conditions after parsing resource 0
        assertNull(mb.getProperty("bean2"));

        // Validate bean3 conditions after parsing resource 0
        assertNull(mb.getProperty("bean3"));

        // Parse resource 1
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-1.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(2, facesConfig.getManagedBeans().size());

        // Validate bean0 conditions after parsing resource 1
        mb = facesConfig.getManagedBean("bean0");
        assertNotNull(mb);
        assertEquals("bean0", mb.getName());
        assertEquals("request", mb.getScope());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(1, mb.getProperties().size());
        mp = mb.getProperty("stringProperty");
        assertNotNull(mp);
        assertNull(mp.getValue());
        assertTrue(mp.isNullValue());

        // Validate bean1 conditions after parsing resource 1
        mb = facesConfig.getManagedBean("bean1");
        assertNotNull(mb);
        assertEquals(3, mb.getProperties().size());
        mp = mb.getProperty("byteProperty");
        assertNotNull(mp);
        assertEquals("11", mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("doubleProperty");
        assertNotNull(mp);
        assertEquals("222.0", mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("intProperty");
        assertNotNull(mp);
        assertEquals("44", mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());

        // Validate bean2 conditions after parsing resource 1
        assertNull(facesConfig.getManagedBean("bean2"));

        // Validate bean3 conditions after parsing resource 1
        assertNull(facesConfig.getManagedBean("bean3"));

        // Parse resource 2
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-2.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(3, facesConfig.getManagedBeans().size());

        // Validate bean2 conditions after parsing resource 2
        mb = facesConfig.getManagedBean("bean2");
        assertNotNull(mb);
        assertEquals("bean2", mb.getName());
        assertEquals("request", mb.getScope());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(1, mb.getProperties().size());
        mp = mb.getProperty("stringProperty");
        assertNotNull(mp);
        assertEquals("java.lang.String", mp.getType());
        assertEquals("Override The Annotation", mp.getValue());
        assertTrue(!mp.isExpression());
        assertTrue(!mp.isNullValue());

        // Parse resource 3
        parser.setResource(this.getClass().getResource
                ("/org/apache/shale/tiger/config/test-config-3.xml"));
        assertNotNull(parser.getResource());
        parser.parse();
        assertEquals(4, facesConfig.getManagedBeans().size());

        // Validate bean3 conditions after parsing resource 3
        mb = facesConfig.getManagedBean("bean3");
        assertNotNull(mb);
        assertEquals("bean3", mb.getName());
        assertEquals("none", mb.getScope());
        assertNull(mb.getListEntries());
        assertNull(mb.getMapEntries());
        assertEquals(8, mb.getProperties().size());
        mp = mb.getProperty("byteProperty");
        assertNotNull(mp);
        assertEquals("byte", mp.getType());
        assertEquals("#{bean1.byteProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("charProperty");
        assertNotNull(mp);
        assertEquals("char", mp.getType());
        assertEquals("#{bean1.charProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("charProperty");
        assertNotNull(mp);
        assertEquals("char", mp.getType());
        assertEquals("#{bean1.charProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("doubleProperty");
        assertNotNull(mp);
        assertEquals("double", mp.getType());
        assertEquals("#{bean1.doubleProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("floatProperty");
        assertNotNull(mp);
        assertEquals("float", mp.getType());
        assertEquals("#{bean1.floatProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("intProperty");
        assertNotNull(mp);
        assertEquals("int", mp.getType());
        assertEquals("#{bean1.intProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("longProperty");
        assertNotNull(mp);
        assertEquals("long", mp.getType());
        assertEquals("#{bean1.longProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("shortProperty");
        assertNotNull(mp);
        assertEquals("short", mp.getType());
        assertEquals("#{bean1.shortProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());
        mp = mb.getProperty("stringProperty");
        assertNotNull(mp);
        assertEquals("java.lang.String", mp.getType());
        assertEquals("#{bean1.stringProperty}", mp.getValue());
        assertTrue(mp.isExpression());
        assertTrue(!mp.isNullValue());

    }


}
