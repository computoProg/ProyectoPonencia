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

package org.apache.shale.view;

/**
 * <p>{@link ViewControllerMapper} is an interface describing a pluggable
 * mechanism to map between a JavaServer Faces <em>view identifier</em>
 * and the name of a corresponding <em>managed bean</em> that serves as
 * the backing bean for this view.  If the managed bean class implements
 * {@link ViewController}, the services described in that interface will
 * be provided by Shale.</p>
 *
 * $Id: ViewControllerMapper.java 464373 2006-10-16 04:21:54Z rahul $
 */

public interface ViewControllerMapper {

    /**
     * <p>Return the name of the managed bean that serves as the backing
     * bean for the specified <code>view identifier</code>.  If no such
     * managed bean name can be determined, return <code>null</code>.</p>
     *
     * @param viewId View identifier for which to identify a backing bean
     */
    public String mapViewId(String viewId);


}
