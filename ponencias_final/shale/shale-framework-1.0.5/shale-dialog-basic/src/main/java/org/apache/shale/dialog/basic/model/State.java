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

import java.util.Iterator;

/**
 * <p>A {@link State} is an executable entity, within the scope of an
 * owning {@link Dialog}.  Execution of a {@link State} returns a logical
 * outcome (represented as a String), which is used to select the next
 * {@link State} to be executed, via a {@link Transition}.</p>
 *
 * <p>Specialized subinterfaces of {@link State} are defined for the
 * standard execution entity types that are supported, including:</p>
 * <ul>
 * <li>{@link ActionState} - Execution of an action method (typically
 *     delegating behavior to appropriate business logic).</li>
 * <li>{@link SubdialogState} - Execution of a separate {@link Dialog},
 *     with continuation based on the logical outcome returned by the
 *     ending {@link State} within the subordinate dialog.</li>
 * <li>{@link ViewState} - Execution of the rendering of a JavaServer
 *     Faces <code>view</code>, and returning the logical outcome returned
 *     by the action method that processes the subsequent submit.</li>
 * <li>{@link EndState} - Specialized {@link ViewState} that also marks
 *     the end of execution of this {@link Dialog}.</li>
 * </ul>
 *
 * @since 1.0.4
 */

public interface State {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link Dialog} that owns this {@link State}.</p>
     *
     * @return The {@link Dialog} this {@link State} belongs to
     */
    public Dialog getDialog();


    /**
     * <p>Return the identifier of this {@link State}, which must be unique
     * among the {@link State}s owned by the same {@link Dialog}.</p>
     *
     * @return The identifier for this {@link State}
     */
    public String getName();


    /**
     * <p>Return an <code>Iterator</code> over the logical outcomes of
     * local {@link Transition}s for this {@link State}.  If there are
     * no such {@link Transition}s, an empty <code>Iterator</code> is
     * returned.</p>
     *
     * @return An {@link Iterator} over the logical outcomes of local
     *         {@link Transition}s for this {@link State}
     */
    public Iterator getTransitionOutcomes();


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the {@link Transition} for the specified logical outcome,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param outcome Logical outcome for which to return a {@link Transition}
     * @return The {@link Transition} for the specified outcome, may be null
     */
    public Transition findTransition(String outcome);


}
