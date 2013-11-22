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
 * $Id: PageNotFoundException.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

/**
 * <p>This is an unchecked exception used to identify that
 * a {@link org.apache.shale.clay.component.Clay} template
 * could not be found.  The exception captures the requested
 * resource to be compared to the view root.  This is done
 * by the {@link org.apache.shale.clay.faces.ClayViewHandler}
 * in the <code>renderView</code> method.  If the missing
 * template and the <code>viewId</code> are the same, a
 * HTTP 404 status code is sent to the client.  If the
 * missing template resource is nested in the page composition,
 * the standard 500 status code will be returned.
 *</p>
 */
public class PageNotFoundException extends RuntimeException {

    /**
     * <p>Unique serial is use in object serialization.</p>
     */
    private static final long serialVersionUID = 3258689897039672375L;
    /**
     * <p>The requested resource.</p>
     */
    private String resource = null;

    /**
     * <p>Overloaded constructor requires an error message
     * and the missing resource.</p>
     *
     * @param message  error message
     * @param resource missing resource
     */
    public PageNotFoundException(String message, String resource) {
       super(message);
       this.resource = resource;
    }

    /**
     * <p>Returns the missing resource.</p>
     *
     * @return uri of the requested page
     */
    public String getResource() {
        return resource;
    }


}
