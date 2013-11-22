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

package org.apache.shale.tiger.managed.rules;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.shale.tiger.config.FacesConfigConfig;

/**
 * <p>Digester <code>RuleSet</code> for parsing managed bean declarations
 * from a JavaServer Faces configuration file.</p>
 *
 * <p><strong>ASSERTION</strong> - The <code>Digester</code> stack is
 * preinitialized with an instance of {@link FacesConfigConfig} prior
 * to parsing being initiated.</p>
 */
public class ManagedBeansRuleSet extends RuleSetBase {

    /** Creates a new instance of ManagedBeansRuleSet. */
    public ManagedBeansRuleSet() {
    }


    /**
     * <p>Add the required <code>Rule</code> instances to parse
     * managed bean declaration metadata.</p>
     *
     * @param digester Digester instance to which rules should be added
     */
    public void addRuleInstances(Digester digester) {

        digester.addRule
                ("faces-config/managed-bean",
                 new ManagedBeanRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-bean-name",
                 "setName", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/managed-bean-class",
                 "setType", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/managed-bean-scope",
                 "setScope", 0);

        digester.addRule
                ("faces-config/managed-bean/managed-property",
                 new ManagedPropertyRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/property-name",
                 "setName", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/property-class",
                 "setType", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/value",
                 "setValue", 0);
        digester.addRule
                ("faces-config/managed-bean/managed-property/null-value",
                 new NullValueRule());

        digester.addRule
                ("faces-config/managed-bean/list-entries",
                 new ListEntriesRule());
        digester.addCallMethod
                ("faces-config/managed-bean/list-entries/value-class",
                 "setValueType", 0);
        digester.addRule
                ("faces-config/managed-bean/list-entries/null-value",
                 new ListEntryRule());
        digester.addRule
                ("faces-config/managed-bean/list-entries/null-value",
                 new NullValueRule());
        digester.addRule
                ("faces-config/managed-bean/list-entries/value",
                 new ListEntryRule());
        digester.addCallMethod
                ("faces-config/managed-bean/list-entries/value",
                 "setValue", 0);

        digester.addRule
                ("faces-config/managed-bean/managed-property/list-entries",
                 new ListEntriesRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/list-entries/value-class",
                 "setValueType", 0);
        digester.addRule
                ("faces-config/managed-bean/managed-property/list-entries/null-value",
                 new ListEntryRule());
        digester.addRule
                ("faces-config/managed-bean/managed-property/list-entries/null-value",
                 new NullValueRule());
        digester.addRule
                ("faces-config/managed-bean/managed-property/list-entries/value",
                 new ListEntryRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/list-entries/value",
                 "setValue", 0);

        digester.addRule
                ("faces-config/managed-bean/map-entries",
                 new MapEntriesRule());
        digester.addCallMethod
                ("faces-config/managed-bean/map-entries/key-class",
                 "setKeyType", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/map-entries/value-class",
                 "setValueType", 0);
        digester.addRule
                ("faces-config/managed-bean/map-entries/map-entry",
                 new MapEntryRule());
        digester.addCallMethod
                ("faces-config/managed-bean/map-entries/map-entry/key",
                 "setKey", 0);
        digester.addRule
                ("faces-config/managed-bean/map-entries/map-entry/null-value",
                 new NullValueRule());
        digester.addCallMethod
                ("faces-config/managed-bean/map-entries/map-entry/value",
                 "setValue", 0);

        digester.addRule
                ("faces-config/managed-bean/managed-property/map-entries",
                 new MapEntriesRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/map-entries/key-class",
                 "setKeyType", 0);
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/map-entries/value-class",
                 "setValueType", 0);
        digester.addRule
                ("faces-config/managed-bean/managed-property/map-entries/map-entry",
                 new MapEntryRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/map-entries/map-entry/key",
                 "setKey", 0);
        digester.addRule
                ("faces-config/managed-bean/managed-property/map-entries/map-entry/null-value",
                 new NullValueRule());
        digester.addCallMethod
                ("faces-config/managed-bean/managed-property/map-entries/map-entry/value",
                 "setValue", 0);

    }


}
