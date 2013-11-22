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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>JavaBean class with map properties for testing.</p>
 */
public class TestBean5 {
    
    /** Creates a new instance of TestBean */
    public TestBean5() {
    }

    private Map emptyMap = null;
    public Map getEmptyMap() { return this.emptyMap; }
    public void setEmptyMap(Map emptyMap) { this.emptyMap = emptyMap; }

    private Map fullMap = new LinkedHashMap();
    {
        fullMap.put("Initial Key", "Initial Value");
    }
    public Map getFullMap() { return this.fullMap; }
    public void setFullMap(Map fullMap) { this.fullMap = fullMap; }

}
