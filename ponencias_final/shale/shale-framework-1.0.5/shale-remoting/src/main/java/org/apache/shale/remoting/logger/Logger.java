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

package org.apache.shale.remoting.logger;

/**
 * <p>Adapter to logging systems that intends to make Commons Logging
 * optional on deployments running on JDK 1.4 or later.</p>
 */
public interface Logger {
    

    // ------------------------------------------------------ Condition Checking


    /**
     * <p>Is trace level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isTraceEnabled(String name);


    /**
     * <p>Is debug level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isDebugEnabled(String name);


    /**
     * <p>Is info level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isInfoEnabled(String name);


    /**
     * <p>Is warning level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isWarnEnabled(String name);


    /**
     * <p>Is error level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isErrorEnabled(String name);


    /**
     * <p>Is fatal level debugging enabled on the specified logger?</p>
     *
     * @param name Name of the logger to check
     */
    public boolean isFatalEnabled(String name);


    // ------------------------------------------------------------- Log Methods


    /**
     * <p>Log a message at trace severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void trace(String name, String message,
                      Throwable exception, Object[] params);


    /**
     * <p>Log a message at debug severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void debug(String name, String message,
                      Throwable exception, Object[] params);


    /**
     * <p>Log a message at info severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void info(String name, String message,
                     Throwable exception, Object[] params);


    /**
     * <p>Log a message at warning severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void warn(String name, String message,
                     Throwable exception, Object[] params);


    /**
     * <p>Log a message at error severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void error(String name, String message,
                      Throwable exception, Object[] params);


    /**
     * <p>Log a message at fatal severity.</p>
     *
     * @param name Name of the logger to use
     * @param message Text message to record (treated as a message format if
     *  the <code>params</code> argument is not null)
     * @param exception Exception to report, or <code>null</code> for none
     * @param params Message4 format replacement parameters, or
     *  <code>null</code> for none
     */
    public void fatal(String name, String message,
                      Throwable exception, Object[] params);


}
