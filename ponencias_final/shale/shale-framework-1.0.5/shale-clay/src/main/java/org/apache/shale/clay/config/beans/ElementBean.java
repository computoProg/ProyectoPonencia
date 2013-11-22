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
 * $Id: ElementBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.Serializable;

/**
 * <p>This bean represents the composition of a complex component.  The renderId
 * uniquely sequences it within a top-level {@link ComponentBean}.</p>
 */

public class ElementBean extends InnerComponentBean implements Comparable, Serializable {

    /**
     * <p>Unique id used by the Serializable interface.</p>
     */
    private static final long serialVersionUID = 3690760596346123828L;

    /**
     * <p>An integer id that is used to order a nested component within it's
     * child collection.  This id is also used a the "signature" when resolving
     * inheritance.
     * </p>
     */
    private int renderId = 0;

    /**
     * @return a named value list of the object's state
     */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("renderId=\"").append(renderId).append("\" ").append(super.toString());
        return buff.toString();
    }

    /**
     * <p>Returns an integer id that is used to order within the {@link ComponentBean} children
     * set.</p>
     *
     * @return the render id
     */
    public int getRenderId() {
        return renderId;
    }

    /**
     * <p>Sets an integer id that is used to order within the {@link ComponentBean} children
     * set.</p>
     *
     * @param i render id
     */
    public void setRenderId(int i) {
        renderId = i;
    }

    /**
     * <p>This is an override of the {@link ComponentBean} making the
     * <code>renderId</code> the ordering identifier instead of the
     * <code>jsfid</code>.</p>
     *
     * @param obj object to compare
     * @return weight of the comparison
     */
    public int compareTo(Object obj) {
        ElementBean item = (ElementBean) obj;
        if (item.renderId < renderId) {
            return 1;
        } else if (item.renderId > renderId) {
            return -1;
        } else {
            return 0;
        }
    }

}

