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
 * $Id: InnerComponentBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;

/**
 * <p>This class should be a base to all components that are contained
 * within a owing {@link ComponentBean}.
 */
public class InnerComponentBean extends ComponentBean implements Serializable {

    /**
     * <p>Unique id used in object serialization.</p>
     */
    private static final long serialVersionUID = 3257283630291301426L;

    /**
     *  <p>This method describes the inheritance relationship for a containing component.
     *  It works kind of like a java inner class in that the jsfid becomes the parent that
     *  the inner component should extend.
     *  </p>
     *
     * @return heritage client id
     */
    public StringBuffer getIsAClientId() {
        StringBuffer id = null;
        if (getIsAParent() != null) {
            id = getIsAParent().getIsAClientId();
            id.append((getHasAParent() != null ? getHasAParent().getJsfid() : "")).append("$");
        } else {
            id = new StringBuffer("UNKNOWN$");
        }

        id.append(getJsfid());
        return id;
    }
}
