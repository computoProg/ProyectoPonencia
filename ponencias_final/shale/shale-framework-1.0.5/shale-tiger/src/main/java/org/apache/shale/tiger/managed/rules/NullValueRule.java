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

import org.apache.commons.digester.Rule;
import org.apache.shale.tiger.managed.config.NullValueHolder;
import org.xml.sax.Attributes;

/**
 * <p>Digester rule for processing a <code>&lt;null-value&gt;</code>
 * element.</p>
 */
public class NullValueRule extends Rule {

    /** Creates a new instance of NullValueRule. */
    public NullValueRule() {
    }

    /**
     * <p>Set the <code>nullValue</code> property on the containing
     * <code>NullValueHolder</code>.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     * @param attributes Attribute list of the matching element
     *
     * @exception Exception if a parsing error occurs
     */
    public void begin(String namespace, String name,
                      Attributes attributes) throws Exception {

        NullValueHolder config = (NullValueHolder) digester.peek();
        config.setNullValue(true);

    }


    /**
     * <p>No body processing for this element.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     *
     * @throws Exception if a parsing error occurs
     */
    public void body(String namespace, String name) throws Exception {
    }


    /**
     * <p>No end processing for this element.</p>
     *
     * @param namespace Namespace URI of the matching element
     * @param name Local name of the matching element
     *
     * @exception IllegalStateException if the popped object is not
     *  of the correct type
     * @exception Exception if a different error occurs
     */
    public void end(String namespace, String name) throws Exception {
    }


}
