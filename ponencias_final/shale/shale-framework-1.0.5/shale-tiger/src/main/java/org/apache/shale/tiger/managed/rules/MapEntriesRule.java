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
import org.apache.shale.tiger.managed.config.MapEntriesConfig;
import org.apache.shale.tiger.managed.config.MapEntriesHolder;
import org.xml.sax.Attributes;

/**
 * <p>Digester rule for processing a <code>&lt;map-entries&gt;</code>
 * element.</p>
 */
public class MapEntriesRule extends Rule {

    /** Creates a new instance of MapEntriesRule. */
    public MapEntriesRule() {
    }

    /** <p>Fully qualified class name of our configuration element bean.</p> */
    private static final String CLASS_NAME =
            "org.apache.shale.tiger.managed.config.MapEntriesConfig";

    /**
     * <p>Create a new {@link MapEntriesConfig} and push it on to the
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
     * <p>Pop the {@link MapEntriesConfig} instance from the stack,
     * and either add it or merge it with parent information.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     *
     * @exception IllegalStateException if the popped object is not
     *  of the correct type
     * @exception Exception if a different error occurs
 */
    public void end(String namespace, String name) throws Exception {

        MapEntriesConfig config = (MapEntriesConfig) digester.pop();
        MapEntriesHolder parent = (MapEntriesHolder) digester.peek();
        MapEntriesConfig previous = (MapEntriesConfig) parent.getMapEntries();
        if (previous == null) {
            parent.setMapEntries(config);
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
    static void merge(MapEntriesConfig config, MapEntriesConfig previous) {

        if (config.getKeyType() != null) {
            previous.setKeyType(config.getKeyType());
        }
        if (config.getValueType() != null) {
            previous.setValueType(config.getValueType());
        }
        previous.getEntries().addAll(config.getEntries());

    }


}
