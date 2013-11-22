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
 * <p>Overall configuration of an individual dialog.  During application
 * execution, this information is immutable (so that simultaneous execution
 * threads may be processing states or transitions through this dialog).
 * Therefore, access to configuration information is not synchronized.</p>
 *
 * <p>A {@link Dialog} is characterized by a set of named {@link State}s,
 * which are executed in an order that is determined by {@link Transition}s
 * between those {@link State}s.  Execution of a {@link Dialog} begins at
 * the {@link State} specified by the <code>start</code> property, and ends
 * when an {@link EndState} is executed.</p>
 *
 * <p>{@link Transition}s describe the rule that determines the name of the
 * next {@link State} to be executed, based upon a logical outcome returned
 * by the execution of a previous {@link State}.  {@link Transition}s
 * associated with a {@link Dialog} define dialog-wide rules for specified
 * outcomes, while {@link Transition}s associated with a particular
 * {@link State} can replace the global {@link Transition} that would normally
 * be selected with one specific to the executing {@link State}.</p>
 *
 * @since 1.0.4
 */

public interface Dialog {


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the class of a JavaBean to be instantiated as the initial
     * value of the <code>data</code> property of a newly instantiated
     * <code>DialogContext</code>.</p>
     *
     * <p>If not explicitly specified in the configuration metadata, this
     * should default to <code>java.util.HashMap</code> to provide convenient
     * name/value pair storage.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE</strong> - Because the <code>data</code>
     * object of <code>DialogContext</code> instances is stored in session
     * scope, the <code>Class</code> specified here should be serializable.</p>
     *
     * @return The JavaBean class whose instance becomes the <code>data</code>
     *         property of a new instance of this dialog
     */
    public Class getDataClass();


    /**
     * <p>Return the name of this {@link Dialog}.</p>
     *
     * @return The name of this {@link Dialog}
     */
    public String getName();


    /**
     * <p>Return the name of the starting {@link State} for this
     * {@link Dialog}.</p>
     *
     * @return The starting state associated with this {@link Dialog}
     */
    public String getStart();


    /**
     * <p>Return an <code>Iterator</code> over the names of {@link State}s
     * that are owned by this {@link Dialog}.  If there are no such
     * {@link State}s, an empty <code>Iterator</code> is returned.</p>
     *
     * @return An <code>Iterator</code> over all the {@link State}s in this
     *         {@link Dialog}
     */
    public Iterator getStateIds();


    /**
     * <p>Return an <code>Iterator</code> over the logical outcomes of
     * global {@link Transition}s for this {@link Dialog}.  If there are
     * no such {@link Transition}s, an empty <code>Iterator</code> is
     * returned.</p>
     *
     * @return An <code>Iterator</code> over the logical outcomes of global
     *         {@link Transition}s for this {@link Dialog}
     */
    public Iterator getTransitionOutcomes();


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the specified {@link State}, owned by this {@link Dialog},
     * if any.  Otherwise, return <code>null</code>.</p>
     *
     * @param id Identifier of the requested {@link State}
     * @return The {@link State} specified by the identifier, may be null
     */
    public State findState(String id);


    /**
     * <p>Return the global {@link Transition} for the specified logical outcome,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param outcome Logical outcome for which to return a {@link Transition}
     * @return The global {@link Transition} for the specified logical outcome
     */
    public Transition findTransition(String outcome);


}
