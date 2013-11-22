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

import java.util.List;
import java.util.Vector;

/**
 * <p>JavaBean class with list properties for testing.</p>
 */
public class TestBean4 {
    
    /** Creates a new instance of TestBean */
    public TestBean4() {
    }

    private List emptyList = null;
    public List getEmptyList() { return this.emptyList; }
    public void setEmptyList(List emptyList) { this.emptyList = emptyList; }

    private List fullList = new Vector();
    {
        fullList.add("First");
        fullList.add("Second");
    }
    public List getFullList() { return this.fullList; }
    public void setFullList(List fullList) { this.fullList = fullList; }


}
