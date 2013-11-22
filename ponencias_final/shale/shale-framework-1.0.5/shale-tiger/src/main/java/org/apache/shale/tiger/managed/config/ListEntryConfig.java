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
 * <p>Representation of the runtime relevant contents of a JavaServer Faces
 * <code>&lt;list-entry&gt;</code> configuration element.</p>
 */
public class ListEntryConfig implements NullValueHolder {

    /** Creates a new instance of ListEntryConfig. */
    public ListEntryConfig() {
    }

    /**
     * <p>Return <code>true</code> if the specified <code>value</code>
     * is a value binding expression, rather than a literal value.</p>
     */
    public boolean isExpression() {
        return (value != null) && value.startsWith("#{")
          && value.endsWith("}");
    }

    /**
     * Holds value of property value.
     */
    private String value;

    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public String getValue() {

        return this.value;
    }

    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) {

        this.value = value;
    }

    /**
     * Holds value of property nullValue.
     */
    private boolean nullValue;

    /**
     * Getter for property nullValue.
     * @return Value of property nullValue.
     */
    public boolean isNullValue() {

        return this.nullValue;
    }

    /**
     * Setter for property nullValue.
     * @param nullValue New value of property nullValue.
     */
    public void setNullValue(boolean nullValue) {

        this.nullValue = nullValue;
    }

}
