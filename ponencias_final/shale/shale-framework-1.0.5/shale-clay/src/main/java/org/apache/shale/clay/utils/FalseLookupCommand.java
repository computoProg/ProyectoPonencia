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

/*
 * $Id: FalseLookupCommand.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.utils;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.LookupCommand;

/**
 * Very similar to the LookupCommand of commons chain, but the execute method
 * return always false to continue the processing.
 * @see org.apache.commons.chain.generic.LookupCommand
 */
public class FalseLookupCommand extends LookupCommand {

    /**
     * @param context commons chains
     * @return always <code>false</code>
     * @throws Exception from nested command execution
     */
    public boolean execute(Context context) throws Exception {
        super.execute(context);
        return false;
    }
}
