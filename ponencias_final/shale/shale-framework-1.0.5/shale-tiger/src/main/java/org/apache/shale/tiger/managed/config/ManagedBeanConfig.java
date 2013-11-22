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

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Representation of the runtime relevant contents of a JavaServer Faces
 * <code>&lt;managed-bean&gt;</code> configuration element.</p>
 */
public class ManagedBeanConfig implements ListEntriesHolder, MapEntriesHolder {

    /** Creates a new instance of ManagedBeanConfig. */
    public ManagedBeanConfig() {
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {

        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Holds value of property type.
     */
    private String type;

    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public String getType() {

        return this.type;
    }

    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Holds value of property scope.
     */
    private String scope;

    /**
     * Getter for property scope.
     * @return Value of property scope.
     */
    public String getScope() {

        return this.scope;
    }

    /**
     * Setter for property scope.
     * @param scope New value of property scope.
     */
    public void setScope(String scope) {

        this.scope = scope;
    }

    /**
     * <p>Map of managed properties associated with this managed bean,
     * keyed by property name.</p>
     */
    private Map<String,ManagedPropertyConfig> properties =
      new HashMap<String,ManagedPropertyConfig>();


    /**
     * <p>Add the specified managed property to the set that is associated
     * with this managed bean, replacing any previous entry.</p>
     *
     * @param property The managed property to add
     */
    public void addProperty(ManagedPropertyConfig property) {
        properties.put(property.getName(), property);
    }


    /**
     * <p>Return the list of managed properties associated with this
     * managed bean.</p>
     */
    public Map<String,ManagedPropertyConfig> getProperties() {
        return this.properties;
    }


    /**
     * <p>Return the managed property (associated with this managed bean)
     * with the specified name, if any; otherwise, return null.</p>
     *
     * @param name Name of the managed property to return
     */
    public ManagedPropertyConfig getProperty(String name) {
        return properties.get(name);
    }

    /**
     * Holds value of property listEntries.
     */
    private ListEntriesConfig listEntries;

    /**
     * Getter for property listEntries.
     * @return Value of property listEntries.
     */
    public ListEntriesConfig getListEntries() {

        return this.listEntries;
    }

    /**
     * Setter for property listEntries.
     * @param listEntries New value of property listEntries.
     */
    public void setListEntries(ListEntriesConfig listEntries) {

        this.listEntries = listEntries;
    }

    /**
     * Holds value of property mapEntries.
     */
    private MapEntriesConfig mapEntries;

    /**
     * Getter for property mapEntries.
     * @return Value of property mapEntries.
     */
    public MapEntriesConfig getMapEntries() {

        return this.mapEntries;
    }

    /**
     * Setter for property mapEntries.
     * @param mapEntries New value of property mapEntries.
     */
    public void setMapEntries(MapEntriesConfig mapEntries) {

        this.mapEntries = mapEntries;
    }


}
