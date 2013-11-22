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

package org.apache.shale.dialog.scxml.impl;

import javax.faces.context.FacesContext;

import org.apache.shale.dialog.scxml.DialogStateMapper;

/**
 * <p>Default {@link DialogStateMapper} implementation. The dialog state
 * identifier itself is used as the JavaServer Faces
 * <code>view identifier</code>.</p>
 *
 * @since 1.0.4
 *
 * $Id: DefaultDialogStateMapper.java 469656 2006-10-31 21:18:18Z rahul $
 */
public class DefaultDialogStateMapper implements DialogStateMapper {

    /**
     * <p>The default implementation is an identity transform which
     * returns the dialog state ID as the next JavaServer Faces
     * <code>view identifier</code>.</p>
     *
     * @param dialogName The logical name of the dialog this state belongs to
     * @param stateId The state identifier for the current dialog state
     * @param context The current {@link FacesContext}
     * @return The JavaServer Faces <code>view identifier</code> that should
     *         be rendered
     */
    public String mapStateId(String dialogName, String stateId,
            FacesContext context) {

        // the identity transform
        return stateId;

    }

}

