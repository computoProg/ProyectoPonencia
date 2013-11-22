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

package org.apache.shale.dialog.basic.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.shale.dialog.basic.model.Dialog;
import org.apache.shale.dialog.basic.model.State;
import org.apache.shale.dialog.basic.model.Transition;

/**
 * <p>Abstract base class for {@link State} definitions.  Each state is owned
 * by exactly one owning {@link Dialog}.  While an application
 * is running, {@link Dialog} (and the constituent {@link State}s and
 * other configuration information) is immutable, so that it may be
 * shared by multiple simultaneous paths of execution.</p>
 *
 * @since 1.0.4
 */

public abstract class AbstractState implements State {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link Dialog} that owns this {@link State} instance.</p>
     */
    private Dialog dialog = null;


    /**
     * <p>The identifier of this {@link State}, which must be unique among
     * all {@link State}s within the owning {@link Dialog}.</p>
     */
    private String name = null;


    /**
     * <p>The {@link Transition}s owned by this {@link State}, keyed by
     * outcome.  <strong>FIXME</strong> - a different strategy will be needed
     * if {@link Transition}s become more complex and manage their own
     * criteria.</p>
     */
    private Map transitions = new HashMap();


    // -------------------------------------------------------------- Properties


    /**
     * {@inheritDoc}
     */
    public Dialog getDialog() {

        return this.dialog;

    }


    /**
     * {@inheritDoc}
     */
    public String getName() {

        return this.name;

    }


    /**
     * {@inheritDoc}
     */
    public Iterator getTransitionOutcomes() {

        return this.transitions.keySet().iterator();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the {@link Transition} for the specified logical outcome,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param outcome Logical outcome for which to return a {@link Transition}
     * @return The matching {@link Transition}
     */
    public Transition findTransition(String outcome) {

        return (Transition) transitions.get(outcome);

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Add the specified {@link Transition} to the {@link Transition}s owned
     * by this {@link State}.</p>
     *
     * @param transition {@link Transition} to be added
     *
     * @exception IllegalArgumentException if the specified {@link Transition}
     *  cannot be added to this {@link State}
     */
    public void addTransition(Transition transition) throws IllegalArgumentException {

        // FIXME - addTransition() - ignore duplicate outcomes for now
        transitions.put(transition.getOutcome(), transition);

    }


    /**
     * <p>Remove the specified {@link Transition} from the {@link Transition}s
     * owned by this {@link State}, if it is currently registered.  Otherwise,
     * do nothing.</p>
     *
     * @param transition {@link Transition} to be removed
     */
    public void removeTransition(Transition transition) {

        transitions.remove(transition.getOutcome());

    }


    /**
     * <p>Set the {@link Dialog} that owns this {@link State}.</p>
     *
     * @param dialog New owning {@link Dialog}
     */
    public void setDialog(Dialog dialog) {

        this.dialog = dialog;

    }



    /**
     * <p>Set the identifier of this {@link State}, which must be unique
     * among the {@link State}s owned by the same {@link Dialog}.</p>
     *
     * @param name New identifier
     */
    public void setName(String name) {

        this.name = name;

    }


}
