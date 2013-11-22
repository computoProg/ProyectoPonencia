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

import org.apache.shale.remoting.logger.Logger;

/**
 * <p>Default {@link Logger} implementation that delegates to Apache Commons
 * Logging (if present), or JDK <code>java.util.Logging</code> (if present),
 * or simply writes to <code>System.out</code>.</p>
 */
public class DefaultLogger extends AbstractLogger {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new DefaultLogger instance, deciding which
     * {@link Logger} implementation, if any, we should delegate to.</p>
     */
    public DefaultLogger() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        // Delegate to Apache Commons Logging if possible
        try {
            Class clazz = loader.loadClass("org.apache.shale.remoting.logger.CommonsLogger");
            this.logger = (Logger) clazz.newInstance();
            return;
        } catch (Exception e) {
            ; // Fall through
        }

        // Delegate to JDK java.util.logging Logging if possible
        try {
            Class clazz = loader.loadClass("org.apache.shale.remoting.logger.JdkLogger");
            this.logger = (Logger) clazz.newInstance();
            return;
        } catch (Exception e) {
            ; // Fall through
        }

        // We will do the logging stuff ourself

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link Logger} instance we should delegate to (if any).</p>
     */
    protected Logger logger = null;


    // ------------------------------------------------------ Condition Checking


    /** {@inheritDoc} */
    public boolean isTraceEnabled(String name) {
        if (logger != null) {
            return logger.isTraceEnabled(name);
        }
        return false;
    }


    /** {@inheritDoc} */
    public boolean isDebugEnabled(String name) {
        if (logger != null) {
            return logger.isDebugEnabled(name);
        }
        return false;
    }


    /** {@inheritDoc} */
    public boolean isInfoEnabled(String name) {
        if (logger != null) {
            return logger.isInfoEnabled(name);
        }
        return false;
    }


    /** {@inheritDoc} */
    public boolean isWarnEnabled(String name) {
        if (logger != null) {
            return logger.isWarnEnabled(name);
        }
        return false;
    }


    /** {@inheritDoc} */
    public boolean isErrorEnabled(String name) {
        if (logger != null) {
            return logger.isErrorEnabled(name);
        }
        return false;
    }


    /** {@inheritDoc} */
    public boolean isFatalEnabled(String name) {
        if (logger != null) {
            return logger.isFatalEnabled(name);
        }
        return false;
    }


    // ------------------------------------------------------------- Log Methods


    /** {@inheritDoc} */
    public void trace(String name, String message,
                      Throwable exception, Object[] params) {
        if (logger != null) {
            trace(name, message, exception, params);
            return;
        }
        System.out.println("TRACE: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


    /** {@inheritDoc} */
    public void debug(String name, String message,
                      Throwable exception, Object[] params) {
        if (logger != null) {
            debug(name, message, exception, params);
            return;
        }
        System.out.println("DEBUG: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


    /** {@inheritDoc} */
    public void info(String name, String message,
                     Throwable exception, Object[] params) {
        if (logger != null) {
            info(name, message, exception, params);
            return;
        }
        System.out.println("INFO: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


    /** {@inheritDoc} */
    public void warn(String name, String message,
                     Throwable exception, Object[] params) {
        if (logger != null) {
            warn(name, message, exception, params);
            return;
        }
        System.out.println("WARN: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


    /** {@inheritDoc} */
    public void error(String name, String message,
                      Throwable exception, Object[] params) {
        if (logger != null) {
            error(name, message, exception, params);
            return;
        }
        System.out.println("ERROR: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


    /** {@inheritDoc} */
    public void fatal(String name, String message,
                      Throwable exception, Object[] params) {
        if (logger != null) {
            fatal(name, message, exception, params);
            return;
        }
        System.out.println("FATAL: " + message(message, params));
        if (exception != null) {
            exception.printStackTrace(System.out);
        }
    }


}
