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

package org.apache.shale.tiger.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Class-level annotation indicating that this class should be registered
 * with the current <code>Lifecycle</code> instance as a JavaServer Faces
 * <code>PhaseListener</code>.  If the implementation class actually
 * implements the <code>PhaseListener</code> interface, it will be
 * registered directly.  Otherwise, an adapter will be registered that
 * calls through to the methods specified by the {@link BeforePhase}
 * and/or {@link AfterPhase} annotations.</p>
 *
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FacesPhaseListener {


    /**
     * <p>Identifier for the phase that this listener is interested
     * in participating in.</p>
     */
    public enum PhaseId {
        ANY_PHASE,
        RESTORE_VIEW,
        APPLY_REQUEST_VALUES,
        PROCESS_VALIDATIONS,
        UPDATE_MODEL_VALUES,
        INVOKE_APPLICATION,
        RENDER_RESPONSE
    }


    /**
     * <p>Identifies the phase(s) that this listener is interested in being
     * notified about.  If not specified, PhaseId.ANY_PHASE is assumed.</p>
     */
    public PhaseId phaseId() default PhaseId.ANY_PHASE;


}
