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
 * $Id: AbstractBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;

/**
 * <p>Abstract class that provides a <code>description</code> property
 * that is populated from the clay configuration file
 * when design time tool support is enabled.</p>
 *
 */
public abstract class AbstractBean implements Serializable {
    /**
     * <p>Metadata description provided in the clay configuration.</p>
     */
    private String description = null;

    /**
     * <p>Returns the <code>description</code> of the bean.</p>
     *
     * @return description of the config bean
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Sets the <code>description</code> of the bean.</p>
     *
     * @param description of the config bean
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
