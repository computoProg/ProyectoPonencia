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

package org.apache.shale.remoting;

import java.util.List;

/**
 * <p>Configuration object used to manage the {@link Mapping} instances for
 * a particular web application.</p>
 */
public interface Mappings {


    /**
     * <p>Add the specified {@link Mapping} to the set of mappings for which
     * remoting services are supplied.</p>
     *
     * @param mapping The new {@link Mapping} to be added
     *
     * @exception IllegalStateException if there is an existing {@link Mapping}
     *  already defined for the specified <code>pattern</code>
     * @exception NullPointerException if <code>mapping</code> is <code>null</code>
     */
    public void addMapping(Mapping mapping);


    /**
     * <p>Return the extension that will replace the <code>FacesServlet</code>
     * extension pattern, if <code>FacesServlet</code> is extension mapped.</p>
     */
    public String getExtension();


    /**
     * <p>Return the {@link Mapping}, if any, for the specified matching
     * <code>pattern</code>.  If there is no such {@link Mapping}, return
     * <code>null</code> instead.</p>
     *
     * @param pattern Matching pattern for which to return a {@link Mapping}
     */
    public Mapping getMapping(String pattern);


    /**
     * <p>Return a <code>List</code> of all the currently defined {@link Mapping}s
     * for this application.  If there are no currently defined {@link Mapping}s,
     * an empty <code>List</code> is returned.</p>
     */
    public List getMappings();


    /**
     * <p>Return the zero-relative index, into the array returned by
     * <code>getPatterns()</code>, of the URL pattern to be used by default
     * when creating URLs for resources.</p>
     *
     * @since 1.0.4
     */
    public int getPatternIndex();


    /**
     * <p>Set the zero-relative index, into the array returned by
     * <code>getPatterns()</code>, of the URL pattern to be used by default
     * when creating URLs for resources.  If not called, the default value
     * will be zero.</p>
     *
     * @param patternIndex The new pattern index
     *
     * @since 1.0.4
     */
    public void setPatternIndex(int patternIndex);


    /**
     * <p>Return a list of URL patterns that this application has mapped to
     * <code>FacesServlet</code>.  This information is useful to renderers that
     * wish to dynamically calculate URLs that will be guaranteed to trigger
     * the JSF request processing lifecycle.</p>
     */
    public String[] getPatterns();


    /**
     * <p>Remove the specified {@link Mapping} from the set of mappings for which
     * remoting services are supplied, if it is currently included.</p>
     *
     * @param mapping The {@link Mapping} to be removed
     *
     * @exception NullPointerException if <code>mapping</code> is <code>null</code>
     */
    public void removeMapping(Mapping mapping);


    /**
     * <p>Set the extension that will replace the <code>FacesServlet</code>
     * extension pattern, if <code>FacesServlet</code> is extension mapped.</p>
     *
     * @param extension The new extension
     */
    public void setExtension(String extension);


    /**
     * <p>Set a list of URL patterns that this application has mapped to
     * <code>FacesServlet</code>.  If no patterns are known, this SHOULD
     * be set to a zero-length array, rather than <code>null</code>.</p>
     *
     * @param patterns The new list of patterns
     */
    public void setPatterns(String[] patterns);


}
