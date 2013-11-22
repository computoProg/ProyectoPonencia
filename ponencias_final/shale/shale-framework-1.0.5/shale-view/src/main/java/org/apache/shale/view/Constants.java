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
 * <p>Manifest constants related to Shale view support that are relevant
 * to applications using Shale.</p>
 */
public final class Constants {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Private constructor to avoid instantiation.</p>
     */
    private Constants() { }


    // ------------------------------------------------------ Manifest Constants



    /**
     * <p>Name of the context initialization parameter that defines the
     * context-relative path we should dispatch to, at the end of
     * <em>Invoke Application</em> phase of the request processing lifecycle,
     * if any application exceptions have been cached by the default
     * {@link ExceptionHandler} instance.  If no value is specified, the
     * default behavioer is to proceed to rendering for the view that the
     * application has navigated to.</p>
     *
     * <p><strong>NOTE</strong> - in order to be processed correctly by the
     * container, the specified path should be a context relative URL
     * that beings with a '/' character.</p>
     *
     * @since 1.0.3
     */
    public static final String EXCEPTION_DISPATCH_PATH =
      "org.apache.shale.view.EXCEPTION_DISPATCH_PATH";



    /**
     * <p>Application scope attribute under which the
     * {@link ExceptionHandler} for handling exceptions is stored.</p>
     */
    public static final String EXCEPTION_HANDLER =
      "org$apache$shale$view$EXCEPTION_HANDLER";


    /**
     * <p>Name of the context initialization parameter that defines the
     * fully qualified class name of the {@link ViewControllerMapper} to be
     * used is stored.  If not present, the default value is
     * <code>org.apache.shale.view.DefaultViewControllerMapper</code>.</p>
     *
     * @deprecated Replace the application scoped managed bean stored with
     *  key Constants.VIEW_MAPPER instead
     */
    public static final String VIEW_CONTROLLER_MAPPER =
      "org.apache.shale.view.VIEW_CONTROLLER_MAPPER";


    /**
     * <p>Application scope attribute under which the
     * {@link ViewControllerMapper} for translating view identifiers
     * to class names of the corresponding {@link ViewController}
     * is stored.</p>
     */
    public static final String VIEW_MAPPER =
      "org$apache$shale$view$VIEW_MAPPER";


}
