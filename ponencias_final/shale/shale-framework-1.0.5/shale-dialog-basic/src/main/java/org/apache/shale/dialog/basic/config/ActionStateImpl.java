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

import org.apache.shale.dialog.basic.model.ActionState;


/**
 * <p>{@link ActionStateImpl} is a basic implementation of
 * {@link ActionState}.</p>
 *
 * @since 1.0.4
 */

public final class ActionStateImpl extends AbstractState implements ActionState {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Method binding expression specifying the method to be invoked
     * when this {@link State} is entered.</p>
     */
    private String method = null;


    // -------------------------------------------------------------- Properties


    /**
     * {@inheritDoc}
     */
    public String getMethod() {

        return this.method;

    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Render a printable version of this instance.</p>
     *
     * @return The printable version of this instance
     */
    public String toString() {

        return "ActionState[dialog=" +
               ((getDialog() != null) ? getDialog().getName() : "<null>") +
               ",name=" + getName() +
               ",method=" + getMethod() +
               "]";

    }


    // --------------------------------------------------- Configuration Methods


    /**
     * <p>Set the method binding expression specifying the method to be
     * invoked when this {@link State} is entered.</p>
     *
     * @param method The new method binding expression
     */
    public void setMethod(String method) {

        this.method = method;

    }


}
