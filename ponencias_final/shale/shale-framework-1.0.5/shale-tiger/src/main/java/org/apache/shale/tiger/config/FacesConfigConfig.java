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

import java.util.HashMap;
import java.util.Map;
import org.apache.shale.tiger.managed.config.ManagedBeanConfig;

/**
 * <p>Configuration bean representing the entire contents of zero or more
 * <code>faces-config.xml</code> resources.</p>
 */
public class FacesConfigConfig {

    /** Creates a new instance of FacesConfigConfig. */
    public FacesConfigConfig() {
    }


    /**
     * <p>Map of {@link ManagedBeanConfig} elements for all defined
     * managed bean configurations, keyed by managed bean name.</p>
     */
    private Map<String,ManagedBeanConfig> managedBeans =
            new HashMap<String,ManagedBeanConfig>();


    /**
     * <p>Add a new managed bean configuration element.</p>
     *
     * @param config {@link ManagedBeanConfig} element to be added
     */
    public void addManagedBean(ManagedBeanConfig config) {
        this.managedBeans.put(config.getName(), config);
    }


    /**
     * <p>Return the {@link ManagedBeanConfig} element for the specified
     * managed bean name, if any; otherwise, return <code>null</code>.</p>
     *
     * @param name Name of the managed bean configuration to return
     */
    public ManagedBeanConfig getManagedBean(String name) {
        return this.managedBeans.get(name);
    }


    /**
     * <p>Return a map of defined {@link ManagedBeanConfig} elements,
     * keyed by managed bean name.</p>
     */
    public Map<String,ManagedBeanConfig> getManagedBeans() {
        return this.managedBeans;
    }


}
