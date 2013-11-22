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
 * $Id: Attributes.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.util.TreeMap;

/**
 * <p>Normalize the case of a String key to lower case making
 * the collection case insensitive.</p>
 */
public class Attributes extends TreeMap {

    /**
     * <p>Unique serial id.</p>
     */
    private static final long serialVersionUID = 3905244515647173938L;

    /**
     * <p>Make the key case insensitive.</p>
     *
     * @param key attribute key
     * @return <code>true</code> if key is contained
     */
    public boolean containsKey(Object key) {
        Object normKey = null;

        if (key != null && key instanceof String) {
           normKey = ((String) key).toLowerCase();
        } else {
           normKey = key;
        }

        return super.containsKey(normKey);
    }

    /**
     * <p>Make the key case insensitive.</p>
     *
     * @param key attribute name
     * @return value
     */
    public Object get(Object key) {
        Object normKey = null;
        if (key != null && key instanceof String) {
            normKey = ((String) key).toLowerCase();
        } else {
            normKey = key;
        }

        return super.get(normKey);
    }

    /**
     * <p>Make the key case insensitive.</p>
     *
     * @param key attribute name
     * @param value attribute value
     *
     * @return original value
     */
    public Object put(Object key, Object value) {
        Object normKey = null;
        if (key != null && key instanceof String) {
            normKey = ((String) key).toLowerCase();
        } else {
            normKey = key;
        }

        return super.put(normKey, value);
    }

}
