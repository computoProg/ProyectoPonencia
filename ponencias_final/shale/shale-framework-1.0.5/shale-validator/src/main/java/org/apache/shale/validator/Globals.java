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

package org.apache.shale.validator;

/**
 * <p>Manifest constants that are global to the Validator Integration
 * implementation.</p>
 */

public class Globals {


    /**
     * <p>Location of the default Validator configuration file.
     * The rules in this file will be loaded if no
     * "org.apache.shale.validator.VALIDATOR_RULES"
     * context initialization parameter is supplied</p>
     */
    public static final String DEFAULT_VALIDATOR_RULES =
      "/org/apache/shale/validator/validator-rules.xml";


    /**
     * <p>Application scope attribute under which we store a <code>Map</code>
     * of arrays of <code>ShaleValidatorAction</code> instances, keyed by the
     * logical name of the Commons Validator action to be executed.</p>
     */
    public static final String VALIDATOR_ACTIONS =
      "org.apache.shale.validator.ACTIONS";


    /**
     * <p>Application scope attribute under which we store the Commons
     * Validator resources that have been configured.</p>
     */
    public static final String VALIDATOR_RESOURCES =
      "org.apache.shale.validator.RESOURCES";


    /**
     * <p>Context initialization parameter used to specify a comma delimited
     * list of context relative resource paths to resources containing our
     * validator configuration information.</p>
     */
    public static final String VALIDATOR_RULES =
      "org.apache.shale.validator.VALIDATOR_RULES";


}
