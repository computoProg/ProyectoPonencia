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

import org.apache.shale.view.AbstractSessionBean;

/**
 * <p>Test bean that subclasses <code>AbstractSessionBean</code> so we can
 * test pass-through calls to "regular" bean instances.</p>
 */
public class SessionBean1 extends AbstractSessionBean {
    

    private static final long serialVersionUID = 7979738341537126448L;
    private StringBuffer sb = new StringBuffer();


    // -------------------------------------------------------- Callback Methods


    public void init() {
        sb.append("init/");
    }


    public void destroy() {
        sb.append("destroy/");
    }


    // ---------------------------------------------------------- Public Methods


    public String log() {
        return sb.toString();
    }


}
