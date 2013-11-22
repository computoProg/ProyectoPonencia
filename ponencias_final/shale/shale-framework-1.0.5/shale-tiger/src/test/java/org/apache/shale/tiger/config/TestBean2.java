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

import org.apache.shale.tiger.managed.Bean;
import org.apache.shale.tiger.managed.Property;
import org.apache.shale.tiger.managed.Scope;
import org.apache.shale.tiger.managed.Value;

/**
 * <p>JavaBean class for testing.</p>
 */
@Bean(name="bean2a", scope=Scope.APPLICATION)
public class TestBean2 extends TestBean {
    
    /** Creates a new instance of TestBean2 */
    public TestBean2() {
    }

    /**
     * Holds value of property xtraProperty.
     */
    @Value("Xtra Override")
    private String xtraProperty = "Xtra";

    /**
     * Getter for property xtraProperty.
     * @return Value of property xtraProperty.
     */
    public String getXtraProperty() {

        return this.xtraProperty;
    }

    /**
     * Setter for property xtraProperty.
     * @param xtraProperty New value of property xtraProperty.
     */
    public void setXtraProperty(String xtraProperty) {

        this.xtraProperty = xtraProperty;
    }

}
