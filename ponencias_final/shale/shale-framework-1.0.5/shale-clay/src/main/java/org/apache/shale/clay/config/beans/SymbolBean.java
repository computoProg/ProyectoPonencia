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

/*
 * $Id: SymbolBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;


/**
 * <p>A symbol represents a variable replaced in a
 * JSF binding expression.  Within the expression
 * symbols are identified by the '@' prefix.</p>
 *
 */
public class SymbolBean extends AbstractBean implements Serializable,
        Comparable {

    /**
     * <p>Unique serialization id.</p>
     */
    private static final long serialVersionUID = -584466364674399355L;

    /**
     * <p>Name of the symbol in the target JSF object property.</p>
     */
    private String name = null;

    /**
     * <p>Value of the named symbol in the target JSF object property.</p>
     */
    private String value = null;


    /**
     * <p>Returns a name corresponding to an associated JSF object property.</p>
     *
     * @return symbol name
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Returns the value of the attribute that can be a literal or a
     * expression.</p>
     *
     * @return symbol value
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Sets the name of the attribute.</p>
     *
     * @param name symbol name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Sets the value of the attribute.</p>
     *
     * @param value symbol value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p>This implementation of the <code>Comparable</code> interface makes
     * the <code>name</code> property the compared key.</p>
     *
     * @param obj object to compare
     * @return weighted value that describes this object compared with the obj
     */
    public int compareTo(Object obj) {
        SymbolBean item = (SymbolBean) obj;

        return item.getName().compareTo(getName());
    }


    /**
     * @return describes the state of the symbol
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("name=\"").append(name).append("\" value=\"").append(value)
            .append("\"");
        return buff.toString();
    }

}
