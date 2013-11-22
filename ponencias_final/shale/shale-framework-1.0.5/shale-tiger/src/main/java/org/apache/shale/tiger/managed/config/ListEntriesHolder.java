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

/**
 * <p>Marker interface for a configuration bean that supports a
 * <code>listEntries</code> property.</p>
 */
public interface ListEntriesHolder {

    /**
     * <p>Return the list entries for this instance.</p>
     */
    public ListEntriesConfig getListEntries();

    /**
     * <p>Set the list entries for this instance.</p>
     *
     * @param listEntries The new list entries
     */
    public void setListEntries(ListEntriesConfig listEntries);

}
