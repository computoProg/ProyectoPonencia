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

package org.apache.shale.tiger.managed.rules;

import org.apache.commons.digester.Rule;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;
import org.apache.shale.tiger.managed.config.ManagedPropertyConfig;
import org.xml.sax.Attributes;

/**
 * <p>Digester rule for processing a <code>&lt;managed-property&gt;</code>
 * element.</p>
 */
public class ManagedPropertyRule extends Rule {

    /** Creates a new instance of ManagedPropertyRule. */
    public ManagedPropertyRule() {
    }

    /** <p>Fully qualified class name of our configuration element bean.</p> */
    private static final String CLASS_NAME =
            "org.apache.shale.tiger.managed.config.ManagedPropertyConfig";

    /**
     * <p>Create a new {@link ManagedPropertyConfig} and push it on to the
     * Digester stack.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     * @param attributes Attribute list of the matching element
     *
     * @exception Exception if a parsing error occurs
     */
    public void begin(String namespace, String name,
                      Attributes attributes) throws Exception {

        Class clazz = digester.getClassLoader().loadClass(CLASS_NAME);
        digester.push(clazz.newInstance());

    }


    /**
     * <p>No body processing for this element.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     *
     * @throws Exception if a parsing error occurs
     */
    public void body(String namespace, String name) throws Exception {
    }


    /**
     * <p>Pop the {@link ManagedPropertyConfig} instance from the stack,
     * and either add it or merge it with parent information.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     *
     * @exception IllegalStateException if the popped object is not
     *  of the correct type
     * @exception Exception if any other error occurs
     */
    public void end(String namespace, String name) throws Exception {

        ManagedPropertyConfig config = (ManagedPropertyConfig) digester.pop();
        ManagedBeanConfig parent = (ManagedBeanConfig) digester.peek();
        ManagedPropertyConfig previous = parent.getProperty(config.getName());
        if (previous == null) {
            parent.addProperty(config);
        } else {
            merge(config, previous);
        }

    }


    /**
     * <p>Merge properties from <code>config</code> into
     * <code>previous</code>.</p>
     *
     * @param config Newly constructed bean
     * @param previous Previous bean to merge into
     */
    static void merge(ManagedPropertyConfig config, ManagedPropertyConfig previous) {

        if (config.getType() != null) {
            previous.setType(config.getType());
        }
        if (config.getValue() != null) {
            previous.setValue(config.getValue());
        }
        if (config.isNullValue()) {
            previous.setNullValue(true);
        }
        if (config.getListEntries() != null) {
            if (previous.getListEntries() != null) {
                ListEntriesRule.merge(config.getListEntries(), previous.getListEntries());
            } else {
                previous.setListEntries(config.getListEntries());
            }
        }
        if (config.getMapEntries() != null) {
            if (previous.getMapEntries() != null) {
                MapEntriesRule.merge(config.getMapEntries(), previous.getMapEntries());
            } else {
                previous.setMapEntries(config.getMapEntries());
            }
        }

    }


}
