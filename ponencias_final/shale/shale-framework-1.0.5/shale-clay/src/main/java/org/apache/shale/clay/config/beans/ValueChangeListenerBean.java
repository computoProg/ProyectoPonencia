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
 * $Id: ValueChangeListenerBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;

/**
 * This meta component will be used to construct a JSF <code>ValueChangeListener</code>.  It is
 * a subclass of {@link ComponentBean} where this class acts like an enumerated type.
 */
public class ValueChangeListenerBean extends InnerComponentBean implements Serializable {

    /**
     * <p>Unique serialization id.</p>
     */
    private static final long serialVersionUID = 3256718472791536436L;


    /**
     * <p>Override the handling of the {@link ComponentBean}'s id property.
     * The <code>id</code> is not stored in the <code>attributes</code>
     * collection.</p>
     */
    private String id = null;

    /**
     * <p>Returns the {@link ComponentBean}'s <code>id</code>.</p>
     *
     * @return component id
     */
    public String getId() {
        return id;
    }


    /**
     * <p>Sets the {@link ComponentBean}'s <code>id</code>.</p>
     *
     * @param id component id
     */
    public void setId(String id) {
        this.id = id;
    }

}
