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

import javax.faces.context.FacesContext;

/**
 * <p>Configuration element describing the mapping between a view identifier
 * URL pattern to a corresponding processor class.</p>
 */
public interface Mapping {


    /**
     * <p>Return the {@link Mappings} instance this {@link Mapping} instance
     * is associated with.</p>
     */
    public Mappings getMappings();


    /**
     * <p>Set the {@link Mappings} instance this {@link Mapping} instance
     * is associated with.</p>
     *
     * @param mappings The new {@link Mappings} instance
     */
    public void setMappings(Mappings mappings);


    /**
     * <p>Return a description of the mechanism used to return the response
     * from this processor.  This value <strong>may</strong> be interpreted,
     * for example, by a JavaServer Faces component that wishes to calculate
     * an appropriate URL for a component specific resource that is packaged
     * in a particular manner.</p>
     */
     public Mechanism getMechanism();


     /**
      * <p>Set the mechanism used by this mapping.</p>
      *
      * @param mechanism The new mechanism
      */
     public void setMechanism(Mechanism mechanism);


     /**
     * <p>Return the matching pattern for the view identifier for this
     * request, used to determine if this is the appropriate {@link Mapping}
     * for processing the current request or not.</p>
     */
     public String getPattern();


     /**
      * <p>Set the matching pattern used by this mapping.</p>
      *
      * @param pattern The new pattern
      */
     public void setPattern(String pattern);


     /**
      * <p>Return the {@link Processor} instance to be used to process
      * requests where the view identifier matches our <code>pattern</code>.</p>
      */
     public Processor getProcessor();


     /**
      * <p>Set the {@link Processor} instance used by this mapping.</p>
      *
      * @param processor The new {@link Processor} instance
      */
     public void setProcessor(Processor processor);


     /**
      * <p>Map the specified resource identifier to a complete URL that may
      * be used to request this resource from the server.</p>
      *
      * @param context <code>FacesContext</code> for the current request
      * @param resourceId Resource identifier to be mapped
      */
     public String mapResourceId(FacesContext context, String resourceId);


     /**
      * <p>If the specified view identifier matches the pattern specified by
      * this {@link Mapping}, return the corresponding resource identifier
      * that should be passed on to our {@link Processor}.  If the specified
      * view identifier does not match, return <code>null</code> instead.</p>
      *
      * @param context <code>FacesContext</code> for the current request
      */
     public String mapViewId(FacesContext context);


}
