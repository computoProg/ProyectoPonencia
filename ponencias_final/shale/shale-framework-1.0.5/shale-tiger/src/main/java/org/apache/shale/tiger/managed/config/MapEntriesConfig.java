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

package org.apache.shale.tiger.managed.config;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Representation of the runtime relevant contents of a JavaServer Faces
 * <code>&lt;map-entries&gt;</code> configuration element.</p>
 */
public class MapEntriesConfig {

    /** Creates a new instance of MapEntriesConfig. */
    public MapEntriesConfig() {
    }

    /**
     * Holds value of property keyType.
     */
    private String keyType;

    /**
     * Getter for property keyType.
     * @return Value of property keyType.
     */
    public String getKeyType() {

        return this.keyType;
    }

    /**
     * Setter for property keyType.
     * @param keyType New value of property keyType.
     */
    public void setKeyType(String keyType) {

        this.keyType = keyType;
    }

    /**
     * Holds value of property valueType.
     */
    private String valueType;

    /**
     * Getter for property valueType.
     * @return Value of property valueType.
     */
    public String getValueType() {

        return this.valueType;
    }

    /**
     * Setter for property valueType.
     * @param valueType New value of property valueType.
     */
    public void setValueType(String valueType) {

        this.valueType = valueType;
    }


    /**
     * <p><code>List</code> of map entry configurations for this
     * map entries configuration.</p>
     */
    private List<MapEntryConfig> entries = new ArrayList<MapEntryConfig>();


    /**
     * <p>Add a map entry configuration to the list for this
     * map entries configuration.</p>
     *
     * @param entry The map entry configuration to add
     */
    public void addEntry(MapEntryConfig entry) {
        this.entries.add(entry);
    }


    /**
     * <p>Return the list of map entry configurations for this
     * map entries configuration.</p>
     */
    public List<MapEntryConfig> getEntries() {
        return this.entries;
    }


    /**
     * <p>REturn the map entry configuration for the specified key, if any;
     * otherwise, return <code>null</code>.</p>
     *
     * @param key Key for which to return a map entry configuration
     */
    public MapEntryConfig getEntry(String key) {
        for (MapEntryConfig entry : entries) {
            if (key.equals(entry.getKey())) {
                return entry;
            }
        }
        return null;
    }


}
