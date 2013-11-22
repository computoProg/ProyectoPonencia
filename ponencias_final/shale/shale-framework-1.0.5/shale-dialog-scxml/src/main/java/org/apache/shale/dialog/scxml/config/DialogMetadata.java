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

package org.apache.shale.dialog.scxml.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.scxml.model.SCXML;


/**
 * <p>Bean encapsulating metadata for a Shale dialog when using Commons SCXML
 * to drive the underlying state machine.</p>
 *
 * <p>This includes:
 *  <ul>
 *   <li>The logical name of this dialog.</li>
 *   <li>The document location where Commons SCXML can find the SCXML document
 *       describing this dialog.</li>
 *   <li>The Commons SCXML object model for this dialog obtained by parsing
 *       the above document.</li>
 *   <li>The dialog data class name, an instance of which will be set as
 *       dialog data once an instance of this dialog is started.</li>
 *   <li>A list of Commons SCXML custom actions, that will be defined for this
 *       dialog.</li>
 *  </ul>
 * </p>
 *
 *  <p><strong>WARNING</strong> - These classes should <strong>ONLY</strong>
 *  be used by the dialog framework infrastructure.  They are
 *  <strong>NOT</strong> meant to be used by the application.</p>
 *
 * @since 1.0.4
 *
 * $Id: DialogMetadata.java 491384 2006-12-31 04:43:49Z rahul $
 */
public class DialogMetadata implements Serializable {

    //---------------------------------------- SCXML dialog metadata variables

        /**
         * Serial version UID.
         */
        private static final long serialVersionUID = -4399162240006113135L;

        /**
         * <p>Default FQCN for dialog data.</p>
         */
        private static final String DEFAULT_DIALOG_DATA_CLASS_NAME = "java.util.HashMap";


        /**
         * The dialog name.
         */
        private String name;

        /**
         * The location where the SCXML document for this dialog resides.
         */
        private String scxmlconfig;


        /**
         * The Commons SCXML object model describing the state machine for this dialog.
         */
        private SCXML stateMachine;


        /**
         * The FQCN of the dialog data.
         */
        private String dataclassname;


        /**
         * The custom Commons SCXML actions for this dialog.
         */
        private List dialogActions;


        //---------------------------------------- Constructors

        /**
         * Constructor.
         */
        public DialogMetadata() {
            this.dataclassname = DEFAULT_DIALOG_DATA_CLASS_NAME;
            this.dialogActions = new ArrayList();
        }


        //---------------------------------------- Public methods

        /**
         * Get the dialog name.
         *
         * @return Returns the dialog name.
         */
        public String getName() {
                return name;
        }


        /**
         * Set the dialog name.
         *
         * @param name The dialog name.
         */
        public void setName(String name) {
            this.name = name;
        }


        /**
         * Get the data class FQN.
         *
         * @return Returns the dataclassname.
         */
        public String getDataclassname() {
                return dataclassname;
        }


        /**
         * Set the data class FQN.
         *
         * @param dataclassname The data class FQN.
         */
        public void setDataclassname(String dataclassname) {
            if (dataclassname != null && dataclassname.trim().length() > 0) {
                this.dataclassname = dataclassname;
            }
        }


        /**
         * Get the location where the SCXML document for this dialog resides.
         *
         * @return Returns the scxmlconfig.
         */
        public String getScxmlconfig() {
                return scxmlconfig;
        }


        /**
         * Set the location where the SCXML document for this dialog resides.
         *
         * @param scxmlconfig The SCXML dialog configuration file location.
         */
        public void setScxmlconfig(String scxmlconfig) {
            this.scxmlconfig = scxmlconfig;
        }


        /**
         * Get the Commons SCXML object model describing the state machine
         * for this dialog.
         *
         * @return Returns the stateMachine.
         */
        public SCXML getStateMachine() {
                return stateMachine;
        }


        /**
         * Set the Commons SCXML object model describing the state machine
         * for this dialog.
         *
         * @param stateMachine The stateMachine to set.
         */
        public void setStateMachine(SCXML stateMachine) {
                this.stateMachine = stateMachine;
        }

        /**
         * Add this Commons SCXML custom action for this specific dialog.
         *
         * @param scxmlAction The SCXMLAction to be added.
         */
        public void addDialogAction(SCXMLAction scxmlAction) {
                dialogActions.add(scxmlAction);
        }


        /**
         * Get the list of dialog actions defined for this specific dialog.
         *
         * @return Returns the list of DialogActions.
         */
        public List getDialogActions() {
                return dialogActions;
        }


        /**
         * A POJO representing the bits of a custom Commons SCXML action used
         * in a Shale dialog.
         *
         */
        public static class SCXMLAction implements Serializable {

            /**
             * Serial version UID.
             */
            private static final long serialVersionUID = 1L;

            //------------------------------------------- Properties
            /**
             * The action name.
             */
            private String name;

            /**
             * The action URI.
             */
            private String uri;

            /**
             * The action class FQN.
             */
            private String actionclassname;

            //------------------------------------------- Constructor

            /**
             * Constructor.
             */
            public SCXMLAction() {
                super();
            }

            //-------------------------------------------- Public methods

            /**
             * Get the action name.
             *
             * @return The action name.
             */
            public String getName() {
                return name;
            }

            /**
             * Set the action name.
             *
             * @param name The action name.
             */
            public void setName(String name) {
                this.name = name;
            }

            /**
             * Get the action URI.
             *
             * @return The action URI.
             */
            public String getUri() {
                return uri;
            }

            /**
             * Set the action URI.
             *
             * @param uri The action URI.
             */
            public void setUri(String uri) {
                this.uri = uri;
            }

            /**
             * Get the action class FQN.
             *
             * @return The action class FQN.
             */
            public String getActionclassname() {
                return actionclassname;
            }

            /**
             * Set the action class FQN.
             *
             * @param actionclassname The action class FQN.
             */
            public void setActionclassname(String actionclassname) {
                this.actionclassname = actionclassname;
            }

        }

}
