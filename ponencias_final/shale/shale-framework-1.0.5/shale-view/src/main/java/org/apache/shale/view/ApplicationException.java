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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Runtime exception encapsulating a <code>List</code> of exceptions that
 * have occurred during the request processing lifecycle.</p>
 *
 * @since 1.0.3
 */
public class ApplicationException extends RuntimeException {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2180307410483666456L;


    /**
     * <p>Construct an exception with no message.</p>
     */
    public ApplicationException() {
        super();
    }



    /**
     * <p>Construct an exception with the specified message.</p>
     *
     * @param message The exception message
     */
    public ApplicationException(String message) {
        super(message);
    }


    /**
     * <p>Construct an exception with the specified message and cause.</p>
     *
     * @param message The exception message
     * @param cause The root cause
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.exceptions = new ArrayList(1);
        this.exceptions.add(cause);
    }


    /**
     * <p>Construct an exception with the specified cause.</p>
     *
     * @param cause The root cause
     */
    public ApplicationException(Throwable cause) {
        super(cause);
        this.exceptions = new ArrayList(1);
        this.exceptions.add(cause);
    }


    /**
     * <p>Construct an exception with the specified <code>List</code> of
     * causes.  The first exception in the list will be logged as the
     * formal cause of this exception.</p>
     *
     * @param exceptions List of exceptions that have been thrown
     */
    public ApplicationException(List exceptions) {
        super((Exception) exceptions.get(0));
        this.exceptions = exceptions;
    }


    // ------------------------------------------------------- Public Properties


    /**
     * <p><code>List</code> of exceptions that are the cumulative cause of
     * this exception.</p>
     */
    private List exceptions;


    /**
     * <p>Return a <code>List</code> of exceptoins that are the cumulative
     * cause of this exception.</p>
     */
    public List getExceptions() {
        return this.exceptions;
    }


}
