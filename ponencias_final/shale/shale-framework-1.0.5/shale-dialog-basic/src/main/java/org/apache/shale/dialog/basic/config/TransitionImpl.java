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

import org.apache.shale.dialog.basic.model.Transition;

/**
 * <p>{@link TransitionImpl} is a basic implementation of
 * {@link Transition}.</p>
 *
 * @since 1.0.4
 */

public final class TransitionImpl implements Transition {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The logical outcome used to select this {@link Transition}.</p>
     */
    private String outcome = null;


    /**
     * <p>The identifier of the target {@link State} for this
     * {@link Transition}.</p>
     */
    private String target = null;


    // -------------------------------------------------------------- Properties


    /**
     * {@inheritDoc}
     */
    public String getOutcome() {

        return this.outcome;

    }


    /**
     * {@inheritDoc}
     */
    public String getTarget() {

        return this.target;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Render a printable version of this instance.</p>
     *
     * @return The printable version of this instance
     */
    public String toString() {

        return "Transition[outcome=" + this.outcome +
               ",target=" + this.target + "]";

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Set the logical outcome used to select this {@link Transition}.</p>
     *
     * @param outcome New logical outcome
     */
    public void setOutcome(String outcome) {

        this.outcome = outcome;

    }


    /**
     * <p>Set the target {@link State} identifier for this
     * {@link Transition}.</p>
     *
     * @param target New target identifier
     */
    public void setTarget(String target) {

        this.target = target;

    }


}
