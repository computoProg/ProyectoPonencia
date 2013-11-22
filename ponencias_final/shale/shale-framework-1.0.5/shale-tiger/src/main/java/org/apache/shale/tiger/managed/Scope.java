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

package org.apache.shale.tiger.managed;

/**
 * <p>Enumeration describing the valid values for the <code>scope</code>
 * attribute of a {@link Bean} annotation.</p>
 */
public enum Scope {

    /**
     * <p>A created bean should not be stored in any scope.</p>
     */
    NONE,

    /**
     * <p>A created bean should be stored in request scope.</p>
     */
    REQUEST,

    /**
     * <p>A created bean should be stored in session scope.</p>
     */
    SESSION,

    /**
     * <p>A created bean should be stored in application scope.</p>
     */
    APPLICATION

}
