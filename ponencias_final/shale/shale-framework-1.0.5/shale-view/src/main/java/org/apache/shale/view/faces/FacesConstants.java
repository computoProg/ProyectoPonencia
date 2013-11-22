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

package org.apache.shale.view.faces;

import org.apache.shale.view.ViewController;


/**
 * <p>Manifest constants related to Shale view support, which are specific
 * to the internal implementation and are thus should not be directly
 * referenced by applications.  Because they are predominantly required only
 * within the current package, the constants are declared package private
 * unless outside access is necessary.</p>
 */
public final class FacesConstants {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Private constructor to avoid instantiation.</p>
     */
    private FacesConstants() { }


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Request scope attribute under which a <code>java.util.List</code>
     * of exceptions accumulated during the current request processing lifecycle
     * are accumulated.  If there is no such <code>List</code> present, then
     * no exceptions have been accumulated for the current request.</p>
     */
    public static final String EXCEPTIONS_LIST =
      "org$apache$shale$view$EXCEPTIONS_LIST";


    /**
     * <p>Application scope attribute under which the
     * {@link ViewControllerCallbacks} instance for this application
     * is stored.</p>
     */
    public static final String VIEW_CALLBACKS =
      "org$apache$shale$view$VIEW_CALLBACKS";


    /**
     * <p>Request scope attribute under which a <code>List</code>
     * containing all {@link ViewController}s that have been initialized
     * for the current request are stored.</p>
     */
    public static final String VIEWS_INITIALIZED =
      "org$apache$shale$view$VIEWS_INITIALIZED";


    /**
     * <p>Request scope attribute under which a <code>Boolean.TRUE</code>
     * flag is stored if this request is a postback.</p>
     */
    public static final String VIEW_POSTBACK =
      "org$apache$shale$view$VIEW_POSTBACK";


    /**
     * <p>Request scope attribute under which the managed bean name of the
     * {@link ViewController} for the view that will actually be rendered
     * (if any) is stored.</p>
     */
    static final String VIEW_NAME_RENDERED =
      "org$apache$shale$view$VIEW_NAME_RENDERED";


}
