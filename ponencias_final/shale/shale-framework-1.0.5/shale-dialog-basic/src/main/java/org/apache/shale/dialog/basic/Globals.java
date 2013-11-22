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

package org.apache.shale.dialog.basic;

import org.apache.shale.dialog.basic.model.Dialog;

/**
 * <p>Manifest constants for the basic dialog state manager implementation.</p>
 *
 * @since 1.0.4
 */
public class Globals {


    /**
     * <p>Context initialization paramater name under which a comma separated
     * list of configuration resources to be parsed exists.</p>
     */
    public static final String CONFIGURATION =
            "org.apache.shale.dialog.basic.CONFIGURATION";


    /**
     * <p>Application scope attribute under which a <code>Map</code> of
     * {@link Dialog} definitions (keyed by dialog name) is stored.</p>
     */
    public static final String DIALOGS =
            "org.apache.shale.dialog.basic.DIALOGS";


    /**
     * <p>Context initialization parameter name under which a strategy name
     * for dealing with saving and restoring dialog context state information
     * is specified.  Valid values are:</p>
     * <ul>
     * <li><code>none</code> - No extra information is stored (default).</li>
     * <li><code>top</code> - Information from the top-most {@link Position}
     *     on the stack, plus enough information to detect crossing a subdialog
     *     boundary.  Such a case will cause an exception to be thrown.</li>
     * <li><code>stack</code> - The entire stack of {@link Position}s, including
     *     the corresponding data objects.<li>
     * </ul>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - The feature set supported by
     * this context initialization parameter is experimental, and no guarantees
     * of backwards compatibility in future versions should be assumed.</p>
     */
    public static final String STRATEGY =
            "org.apache.shale.dialog.basic.STRATEGY";


}
