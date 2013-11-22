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
 * <code>&lt;list-entries&gt;</code> configuration element.</p>
 */
public class ListEntriesConfig {

    /** Creates a new instance of ListEntriesConfig. */
    public ListEntriesConfig() {
    }

    /**
     * Holds value of property valueType.
     */
    private String valueType;

    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public String getValueType() {

        return this.valueType;
    }

    /**
     * Setter for property type.
     * @param valueType New value of property type.
     */
    public void setValueType(String valueType) {

        this.valueType = valueType;
    }


    /**
     * <p>List of entries for configuring this list.</p>
     */
    private List<ListEntryConfig> entries = new ArrayList<ListEntryConfig>();


    /**
     * <p>Add a new entry to the entries for this list.</p>
     *
     * @param entry Entry to be added
     */
    public void addEntry(ListEntryConfig entry) {
        this.entries.add(entry);
    }


    /**
     * <p>Return the entries for configuring this list.</p>
     */
    public List<ListEntryConfig> getEntries() {
        return this.entries;
    }


}
