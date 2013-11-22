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

package org.apache.shale.faces;

import org.apache.shale.util.TokenProcessor;

/**
 * <p>{@link ShaleConstants} are manifest constants defining global identifiers shared across
 * the internal implementation of the controller.  These constants
 * need not be referenced by applications built on the framework.</p>
 *
 * $Id: ShaleConstants.java 464373 2006-10-16 04:21:54Z rahul $
 */
public final class ShaleConstants {

    /**
     * <p>Private constructor so that this class cannot be instantiated.</p>
     */
    private ShaleConstants() { }


    /**
     * <p>Application scope attribute that's an instance of
     * <code>org.apache.shale.util.Tags</code>. This managed
     * bean is defined in <code>faces-config.xml</code> and
     * is used by custom tags.
     *
     */
    public static final String TAG_UTILITY_BEAN =
      "org.apache.shale.TAG_UTILITY_BEAN";


    /**
     * <p>Appication scope attribute key under which the
     * {@link TokenProcessor} instance for this application is stored.</p>
     */
    public static final String TOKEN_PROCESSOR =
      "org.apache.shale.TOKEN_PROCESSOR";


    /**
     * <p>Session scope attribute key under which we keep a <code>Set</code>
     * containing the valid transaction tokens for this session.</p>
     */
    public static final String TOKENS = "org.apache.shale.TOKENS";


}
