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

package org.apache.shale.remoting.impl;

import javax.faces.context.ResponseWriter;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.remoting.faces.ResponseFactory;

/**
 * <p>Test <code>Command</code> implementation for
 * <code>ChainProcessorTestCase</code>.</p>
 */

public class ChainProcessorCommand implements Command {
    
    
    public boolean execute(Context context) throws Exception {

        ChainContext ccontext = (ChainContext) context;
        ResponseWriter writer =
            (new ResponseFactory()).getResponseWriter
                (ccontext.getFacesContext(), "text/x-plain");
        writer.writeText("This is a test.  It is only a test.\n", null);
        return false;

    }


}
