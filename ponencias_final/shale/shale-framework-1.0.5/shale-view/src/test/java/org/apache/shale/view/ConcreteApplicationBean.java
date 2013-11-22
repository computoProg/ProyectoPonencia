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

package org.apache.shale.view;

/**
 * <p>Concrete implementation of {@link AbstractApplicationBean}.</p>
 */
public class ConcreteApplicationBean extends AbstractApplicationBean {
    
    /** Creates a new instance of ConcreteApplicationBean */
    public ConcreteApplicationBean() {
    }
    

    // ---------------------------------------------------------- Public Methods


    public void init() {
        record("init()");
    }


    public void destroy() {
        record("destroy()");
    }


    public String toString() {
        return "ConcreteApplicationBean";
    }


    // --------------------------------------------------------- Support Methods


    private static String record = "";


    public static void clear() {
        record = "";
    }


    public static String record() {
        return record;
    }


    private static void record(String message) {
        record += message + "//";
    }

}
