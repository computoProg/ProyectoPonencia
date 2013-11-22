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

package org.apache.shale.dialog.basic.model;

/**
 * <p>A {@link SubdialogState} represents the execution of a separate
 * {@link Dialog}, after which processing proceeds within the current
 * {@link Dialog} based upon the logical outcome returned by the
 * {@link EndState} of the subordinate dialog.</p>
 *
 * @since 1.0.4
 */

public interface SubdialogState extends State {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the name of the subordinate dialog to be executed
     * by this state.</p>
     *
     * @return The name of the subdialog to be executed
     */
    public String getDialogName();


}
