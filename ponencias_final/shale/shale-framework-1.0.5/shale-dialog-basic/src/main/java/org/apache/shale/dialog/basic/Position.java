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

package org.apache.shale.dialog.basic;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.shale.dialog.basic.model.Dialog;
import org.apache.shale.dialog.basic.model.State;

/**
 * <p>JavaBean that represents the current {@link State} within
 * a {@link Dialog}.</p>
 */
class Position implements Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -1600336297791202073L;


    /**
     * <p>Construct a new {@link Position} representing the specified
     * {@link State} for the specified {@link Dialog}, and associated
     * with the specified <code>data</code> instance.</p>
     *
     * @param dialog {@link Dialog} to be recorded
     * @param state {@link State} to be recorded
     * @param data Data instance to be recorded
     */
    Position(Dialog dialog, State state, java.lang.Object data) {
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog cannot be null");
        }
        setDialog(dialog);
        setState(state);
        setData(data);
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The data instance for the dialog execution represeented by this
     * {@link Position}.</p>
     */
    private Object data = null;


    /**
     * <p>The {@link Dialog} within which this {@link Position} is reported.
     * This value is transient, and may need to be regenerated.</p>
     */
    private transient Dialog dialog = null;


    /**
     * <p>The name of the {@link Dialog} within which this {@link Position}
     * is reported.</p>
     */
    private String dialogName = null;


    /**
     * <p>The {@link State} that represents the current position within the
     * {@link Dialog} that is being executed.  This value is transient, and
     * may need to be regenerated.</p>
     */
    private transient State state = null;


    /**
     * <p>The name of the {@link State} that represents the current position
     * within the {@link Dialog} that is being executed.</p>
     */
    private String stateName = null;


    // ------------------------------------------------------ Package Properties


    /**
     * <p>Return the data object associated with this dialog execution.</p>
     *
     * @return The associated data object for this dialog instance
     */
    Object getData() {
        return this.data;
    }


    /**
     * <p>Set the data object associated with this dialog execution.</p>
     *
     * @param data The new data object
     */
    void setData(Object data) {
        this.data = data;
    }


    /**
     * <p>Return the {@link Dialog} whose execution is tracked by this
     * {@link Position}.</p>
     *
     * @return The {@link Dialog} being tracked by this {@link Position}
     *         instance
     */
    Dialog getDialog() {
        if (this.dialog != null) {
            return this.dialog;
        }
        Map map = (Map)
          FacesContext.getCurrentInstance().getExternalContext().
          getApplicationMap().get(Globals.DIALOGS);
        this.dialog = (Dialog) map.get(this.dialogName);
        return this.dialog;
    }


    /**
     * <p>Set the {@link Dialog} whose execution is being tracked by this
     * {@link Position}.</p>
     *
     * @param dialog The {@link Dialog} instance being tracked
     */
    private void setDialog(Dialog dialog) {
        this.dialog = dialog;
        this.dialogName = dialog.getName();
    }


    /**
     * <p>Return the {@link State} representing the current execution position
     * within the owning {@link Dialog}.</p>
     *
     * @return The current {@link State} within the owning {@link Dialog}
     */
    State getState() {
        if (this.state == null) {
            Dialog dialog = getDialog();
            if (dialog == null) {
                return null;
            }
            this.state = dialog.findState(this.stateName);
        }
        return this.state;
    }


    /**
     * <p>Set the {@link State} representing the current execution position
     * within the owning {@link Dialog}.</p>
     *
     * @param state The new {@link State}
     */
    void setState(State state) {
        if (state == null) {
            this.state = null;
            this.stateName = null;
        } else {
            this.state = state;
            this.stateName = state.getName();
        }
    }


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Return a String representation of this object.</p>
     *
     * @return The String representation of this {@link Position} instance
     */
    public String toString() {
        if (getState() == null) {
            return "Position[dialog=" + getDialog().getName()
            + ",data=" + getData() + "]";
        } else {
            return "Position[dialog=" + getDialog().getName()
                   + ",state=" + getState().getName()
                   + ",data=" + getData() + "]";
        }
    }


}
