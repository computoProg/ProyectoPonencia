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
 *
 * $Id: MyComponent.java 372303 2006-01-25 20:09:24Z craigmcc $
 */

package org.apache.shale.tiger.faces;

import javax.faces.event.PhaseEvent;
import org.apache.shale.tiger.register.AfterPhase;
import org.apache.shale.tiger.register.BeforePhase;
import org.apache.shale.tiger.register.FacesPhaseListener;
import org.apache.shale.tiger.register.FacesPhaseListener.PhaseId;

/**
 * <p>Test phase listener for Shale Tiger unit tests.</p>
 */
@FacesPhaseListener(phaseId=PhaseId.ANY_PHASE)
public class MyPhaseListener {
    

    @BeforePhase
    public void myBeforePhase(PhaseEvent event) {
        ;
    }


    @AfterPhase
    public void myAfterPhase(PhaseEvent event) {
        ;
    }


}
