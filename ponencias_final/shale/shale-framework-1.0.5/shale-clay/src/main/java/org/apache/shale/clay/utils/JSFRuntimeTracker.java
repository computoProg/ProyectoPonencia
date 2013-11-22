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
 * $Id: JSFRuntimeTracker.java 465411 2006-10-18 23:06:50Z gvanmatre $
 */

package org.apache.shale.clay.utils;

/**
 * <p>This utility class is used to determine the
 * JSF runtime that will take presidency.</p>
 */
public final class JSFRuntimeTracker {

    /**
     * <p>Utility Class hides the default constructor.</p>
     */
    private JSFRuntimeTracker() {
        super();
    }

    /**
     * <p>This enumeration indicates the JSF RI 1.1.x runtime is installed.</p>
     */
    public static final int RI_1_1 = 0;

    /**
     * <p>This enumeration indicates the MyFaces 1.1.x runtime is installed.</p>
     */
    public static final int MYFACES_1_1 = 1;

    /**
     * <p>This enumeration indicates the JSF RI 1.2.x runtime is installed.</p>
     */
    public static final int RI_1_2 = 2;

    /**
     * <p>Captures the active runtime once determined.</p>
     */
    private static int activeRuntime = -1;

    /**
     * <p>This method will try to load some key classes in the various runtimes
     * to determine the active runtime.  The enumerations are as follows:
     * <code>RI_1_1</code>, <code>MYFACES_1_1</code>, <code>RI_1_2</code>.</p>
     *
     * @return an enumeration that indicates the active JSF runtime
     */
    public static int getJsfRuntime() {
        if (activeRuntime > 0) {
            return activeRuntime;
        }
        try {
            // JSF RI 1.2
            Class.forName("com.sun.faces.config.JSFVersionTracker");
            activeRuntime = RI_1_2;
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("org.apache.myfaces.application.jsp.JspViewHandlerImpl");
                activeRuntime = MYFACES_1_1;  //myfaces 1.1.x
            } catch (ClassNotFoundException e2) {
                activeRuntime = RI_1_1; //JSF RI 1.1
            }
        }

        return activeRuntime;
    }
}
