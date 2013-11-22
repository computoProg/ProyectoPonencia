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

package org.apache.shale.application;

import org.apache.shale.application.faces.ShaleWebContext;

/**
 * <p>Command that filters incoming requests based on matching the remote
 * host name (or the remote address, if no remote host name is available)
 * against regular expression patterns that are configured on
 * this instance.  See {@link AbstractRegExpFilter} for details of the
 * matching algorithm.</p>
 *
 * <p><strong>USAGE NOTE:</strong> - This command will only be effective if
 * used before the regular filter chain is processed.  In other words, you
 * should invoke it as part of a <code>preprocess</code> chain in the
 * <code>shale</code> catalog.</p>
 *
 * $Id: RemoteHostFilter.java 464373 2006-10-16 04:21:54Z rahul $
 *
 * @see AbstractRegExpFilter
 */
public class RemoteHostFilter extends AbstractRegExpFilter {
    

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return the value to be tested against exclude and include patterns.
     * This will be the value of the <code>remoteHost</code> property (if any);
     * otherwise the value of the <code>remoteAddr</code> property.</p>
     *
     * @param context <code>Context</code> for the current request
     */
    protected String value(ShaleWebContext context) {

        String value = context.getRequest().getRemoteHost();
        if (value == null) {
            value = context.getRequest().getRemoteAddr();
        }
        return value;

    }


}
