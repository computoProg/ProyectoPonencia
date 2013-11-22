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

package org.apache.shale.tiger.view.faces;

import org.apache.shale.tiger.view.Application;
import org.apache.shale.tiger.view.Destroy;
import org.apache.shale.tiger.view.Init;

/**
 * <p>Test bean that does not subclass <code>AbstractApplicationBean</code>,
 * but uses corresponding annotations, so that we can test callback events.</p>
 */
@Application
public class ApplicationBean2 {
    

    private StringBuffer sb = new StringBuffer();


    // -------------------------------------------------------- Callback Methods


    @Init
    public void doInit() {
        sb.append("init/");
    }


    @Destroy
    public void doDestroy() {
        sb.append("destroy/");
    }


    // ---------------------------------------------------------- Public Methods


    public String log() {
        return sb.toString();
    }


}
