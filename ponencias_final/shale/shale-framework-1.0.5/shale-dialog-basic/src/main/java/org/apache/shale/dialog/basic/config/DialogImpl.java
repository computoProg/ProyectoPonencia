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
 * <p>{@link DialogImpl} is a basic implementation of {@link Dialog}.</p>
 *
 * @since 1.0.4
 */
public final class DialogImpl implements Dialog {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The class of a JavaBean to be instantiated as the initial
     * value of the <code>data</code> property of a newly instantiated
     * <code>DialogContext</code>.</p>
     */
    private Class dataClass = HashMap.class;


    /**
     * <p>Name of this {@link Dialog}.</p>
     */
    private String name = null;


    /**
     * <p>Name of the starting {@link State} for this {@link Dialog}.</p>
     */
    private String start = null;


    /**
     * <p>The {@link State}s owned by this {@link Dialog}, keyed by
     * identifier.</p>
     */
    private Map states = new HashMap();


    /**
     * <p>The global {@link Transition}s owned by this {@link Dialog}, keyed by
     * outcome.  Logic that performs transition management should first check
     * for a {@link Transition} associated with the origin {@link State}, then
     * consult the {@link Dialog} for a global definition.</p>
     */
    private Map transitions = new HashMap();


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the class of a JavaBean to be instantiated as the initial
     * value of the <code>data</code> property of a newly instantiated
     * <code>DialogContext</code>.</p>
     *
     * @return The JavaBean class whose instance becomes the <code>data</code>
     *         property of a new instance of this dialog
     */
    public Class getDataClass() {

        return this.dataClass;

    }


    /**
     * <p>Return the name of this {@link Dialog}.</p>
     *
     * @return The name of this {@link Dialog}
     */
    public String getName() {

        return this.name;

    }


    /**
     * <p>Return the name of the starting {@link State} for this
     * {@link Dialog}.</p>
     *
     * @return The starting state associated with this {@link Dialog}
     */
    public String getStart() {

        return this.start;

    }


    /**
     * <p>Return an <code>Iterator</code> over the names of {@link State}s
     * that are owned by this {@link Dialog}.  If there are no such
     * {@link State}s, an empty <code>Iterator</code> is returned.</p>
     *
     * @return An <code>Iterator</code> over all the {@link State}s in this
     *         {@link Dialog}
     */
    public Iterator getStateIds() {

        return this.states.keySet().iterator();

    }


    /**
     * <p>Return an <code>Iterator</code> over the logical outcomes of
     * global {@link Transition}s for this {@link Dialog}.  If there are
     * no such {@link Transition}s, an empty <code>Iterator</code> is
     * returned.</p>
     *
     * @return An <code>Iterator</code> over the logical outcomes of global
     *         {@link Transition}s for this {@link Dialog}
     */
    public Iterator getTransitionOutcomes() {

        return this.transitions.keySet().iterator();

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return the specified {@link State}, owned by this {@link Dialog},
     * if any.  Otherwise, return <code>null</code>.</p>
     *
     * @param id Identifier of the requested {@link State}
     * @return The {@link State} specified by the identifier, may be null
     */
    public State findState(String id) {

        return (State) states.get(id);

    }


    /**
     * <p>Return the global {@link Transition} for the specified logical outcome,
     * if any; otherwise, return <code>null</code>.</p>
     *
     * @param outcome Logical outcome for which to return a {@link Transition}
     * @return The global {@link Transition} for the specified logical outcome
     */
    public Transition findTransition(String outcome) {

        return (Transition) transitions.get(outcome);

    }


    /**
     * <p>Render a printable version of this instance.</p>
     *
     * @return A printable version of this instance
     */
    public String toString() {

        return "Dialog[name=" + this.name + ",start=" + this.start + "]";

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Add the specified {@link State} to the {@link State}s owned by
     * this {@link Dialog}.</p>
     *
     * @param state {@link State} to be added
     *
     * @exception IllegalArgumentException if there is already a {@link State}
     *  with the specified <code>id</code> owned by this {@link Dialog}
     */
    public void addState(State state) throws IllegalArgumentException {

        if (states.containsKey(state.getName())) {
            throw new IllegalArgumentException(state.getName());
        }
        states.put(state.getName(), state);
        if (state instanceof AbstractState) {
            ((AbstractState) state).setDialog(this);
        }

    }


    /**
     * <p>Add the specified {@link Transition} to the global {@link Transition}s
     * associated with this {@link Dialog}.</p>
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
     * <p>Return the data class name for the <code>data</code> property
     * of a newly instantiated <code>DialogContext</code>.</p>
     *
     * @return The fully qualified class name whose instance becomes the
     *         <code>data</code> property of a new instance of this dialog
     */
    public String getDataClassName() {

        return dataClass.getName();

    }


    /**
     * <p>Set the data class name for the <code>data</code> property
     * of a newly instantiated <code>DialogContext</code>.</p>
     *
     * @param dataClassName New data class name
     *
     * @exception Exception if the specified class name cannot be loaded
     */
    public void setDataClassName(String dataClassName) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = DialogImpl.class.getClassLoader();
        }
        try {
            this.dataClass = loader.loadClass(dataClassName);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }

    }


    /**
     * <p>Set the name of this {@link Dialog}.</p>
     *
     * @param name New name
     */
    public void setName(String name) {

        this.name = name;

    }


    /**
     * <p>Set the name of the starting {@link State} for this
     * {@link Dialog}.</p>
     *
     * @param start Name of the starting {@link State}
     */
    public void setStart(String start) {

        this.start = start;

    }


    /**
     * <p>Remove the specified {@link State} from the {@link State}s owned by
     * this {@link Dialog}, if it is currently registered.  Otherwise,
     * do nothing.</p>
     *
     * @param state {@link State} to be removed
     */
    public void removeState(State state) {

        states.remove(state.getName());
        if (state instanceof AbstractState) {
            ((AbstractState) state).setDialog(null);
        }

    }


    /**
     * <p>Remove the specified {@link Transition} from the global
     * {@link Transition}s associated with this {@link Dialog}, if it is
     * currently registered.  Otherwise, do nothing.</p>
     *
     * @param transition {@link Transition} to be removed
     */
    public void removeTransition(Transition transition) {

        transitions.remove(transition.getOutcome());

    }


}
