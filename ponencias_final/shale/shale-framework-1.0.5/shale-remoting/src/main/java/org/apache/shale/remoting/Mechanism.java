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

/**
 * <p>Typesafe enumeration of the legal values that may be specified for the
 * <code>mechanism</code> property of a {@link Mapping}.  Note that these values
 *  are advisory only -- the framework has no concept of what technique is
 *  actually employed to translate the incomfing view identifier into content
 *  to be supplied as a response.</p>
 */
public final class Mechanism {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Private constructor to disable creation of new instances.</p>
     *
     * @param description Description of this mechanism
     */
    private Mechanism(String description) {

        this.description = description;

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The description of this mechanism.</p>
     */
    private String description = null;


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return a string representation of this mechansim.</p>
     */
    public String toString() {

        return this.description;

    }




    // -------------------------------------------------------- Create Instances


    /**
     * <p>{@link Mechanism} indicating that the processor should serve a static
     * resource from the classpath of this web application.</p>
     */
    public static final Mechanism CLASS_RESOURCE = new Mechanism("CLASS_RESOURCE");


    /**
     * <p>{@link Mechanism} indicating that the processor should serve a static
     * resource from the web application archive of this web application.</p>
     */
    public static final Mechanism WEBAPP_RESOURCE = new Mechanism("WEBAPP_RESOURCE");


    /**
     * <p>{@link Mechanism} indicating that the processor should dynamically
     * calculate the content type, and contents, of the response to be
     * created.</p>
     */
    public static final Mechanism DYNAMIC_RESOURCE = new Mechanism("DYNAMIC_RESOURCE");


    /**
     * <p>{@link Mechanism} indicating that an unspecified mechanism will be
     * used to provide the content for the response.</p>
     */
    public static final Mechanism OTHER_RESOURCE = new Mechanism("OTHER_RESOURCE");


}
