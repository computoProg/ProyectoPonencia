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

package org.apache.shale.dialog.scxml;

import org.apache.shale.dialog.scxml.impl.DefaultDialogStateMapper;

/**
 * <p>Manifest constants and well known event names for the
 * Apache Commons SCXML driven Shale dialog implementation.</p>
 *
 * @since 1.0.4
 */
public class Globals {


    /**
     * <p>Context initialization paramater name under which a comma separated
     * list of configuration resources to be parsed exists.</p>
     */
    public static final String CONFIGURATION =
            "org.apache.shale.dialog.scxml.CONFIGURATION";


    /**
     * <p>Application scope attribute under which a <code>Map</code> of
     * dialog definitions (keyed by dialog name) is stored. A dialog
     * definition is the Commons SCXML object model describing the dialog
     * state machine.</p>
     */
    public static final String DIALOGS =
            "org.apache.shale.dialog.scxml.DIALOGS";


    /**
     * <p>Application scope attribute under which the current
     * {@link DialogStateMapper} implementation resides. Defaults to
     * {@link DefaultDialogStateMapper}.</p>
     */
    public static final String STATE_MAPPER =
            "org$apache$shale$dialog$scxml$STATE_MAPPER";


    /**
     * <p>The name of the postback event that the state machines should
     * use to author transitions for, out of a "view" state.</p>
     */
    public static final String POSTBACK_EVENT =
            "faces.outcome";


    /**
     * <p>The variable that holds the postback outcome in the root context
     * of the state machine instance. Expressions within the SCXML document
     * can then make references to this variable.</p>
     */
    public static final String POSTBACK_OUTCOME =
            "outcome";


    /**
     * <p>The namespace URI for the custom Commons SCXML actions defined by
     * the Shale dialogs Commons SCXML implementation.</p>
     */
    public static final String CUSTOM_SCXML_ACTIONS_URI =
            "http://shale.apache.org/dialog-scxml";


    /**
     * <p>The key for saving dialog properties in the root context of the
     * underlying Commons SCXML state machine. SCXML documents describing
     * dialogs <em>must not</em> use this key as a &lt;var&gt; or &lt;data&gt;
     * name.</p>
     */
    public static final String DIALOG_PROPERTIES =
            "ReservedShaleDialogPropertiesKey";


}

