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

import javax.faces.context.FacesContext;

/**
 * <p>{@link DialogStateMapper} is an interface describing a pluggable
 * mechanism to map between the state ID within a SCXML dialog definition
 * and the corresponding JavaServer Faces <em>view identifier</em>
 * that should be rendered when the dialog state machine comes to rest in
 * that state.</p>
 *
 * <p>By default, an identity transform is applied i.e. the
 * JavaServer Faces <code>view identifier</code> used is the same as
 * the dialog state identifier.</p>
 *
 * <p>The default behavior can be changed by replacing the application
 * scoped bean at key {@link Globals#STATE_MAPPER}. The replacement
 * must implement {@link DialogStateMapper}.</p>
 *
 * @since 1.0.4
 *
 * $Id: DialogStateMapper.java 469656 2006-10-31 21:18:18Z rahul $
 */
public interface DialogStateMapper {

    /**
     * <p>Return the JavaServer Faces <code>view identifier</code> that
     * corresponds to current dialog state. The current {@link FacesContext}
     * instance is also available so developers can consult pertinent
     * information such as user role, current locale, user agent making
     * the request etc. to map the state identifier to the view
     * identifier, if needed.</p>
     *
     * @param dialogName The logical name of the dialog this state belongs to
     * @param stateId The state identifier for the current dialog state
     * @param context The current {@link FacesContext}
     * @return The JavaServer Faces <code>view identifier</code> that should
     *         be rendered
     */
    public String mapStateId(String dialogName, String stateId,
                            FacesContext context);

}

