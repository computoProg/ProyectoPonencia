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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * <p>Implementation of {@link Logger} that interacts with the standard
 * <code>java.util.logging</code> APIs.</p>
 */
public final class JdkLogger extends AbstractLogger {
    

    // ------------------------------------------------------ Condition Checking


    /** {@inheritDoc} */
    public boolean isTraceEnabled(String name) {
        return logger(name).isLoggable(Level.FINEST);
    }


    /** {@inheritDoc} */
    public boolean isDebugEnabled(String name) {
        return logger(name).isLoggable(Level.FINE);
    }


    /** {@inheritDoc} */
    public boolean isInfoEnabled(String name) {
        return logger(name).isLoggable(Level.INFO);
    }


    /** {@inheritDoc} */
    public boolean isWarnEnabled(String name) {
        return logger(name).isLoggable(Level.WARNING);
    }


    /** {@inheritDoc} */
    public boolean isErrorEnabled(String name) {
        return logger(name).isLoggable(Level.SEVERE);
    }


    /** {@inheritDoc} */
    public boolean isFatalEnabled(String name) {
        return logger(name).isLoggable(Level.SEVERE);
    }


    // ------------------------------------------------------------- Log Methods


    /** {@inheritDoc} */
    public void trace(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.FINEST, message(message, params));
        } else {
            logger(name).log(Level.FINEST, message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void debug(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.FINE, message(message, params));
        } else {
            logger(name).log(Level.FINE, message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void info(String name, String message,
                     Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.INFO, message(message, params));
        } else {
            logger(name).log(Level.INFO, message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void warn(String name, String message,
                     Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.WARNING, message(message, params));
        } else {
            logger(name).log(Level.WARNING, message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void error(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.SEVERE, message(message, params));
        } else {
            logger(name).log(Level.SEVERE, message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void fatal(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).log(Level.SEVERE, message(message, params));
        } else {
            logger(name).log(Level.SEVERE, message(message, params), exception);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Map of <code>Logger</code> instances, keyed by name.</p>
     */
    private Map loggers = new HashMap();


    /**
     * <p>Return a <code>Logger</code> instance for the specified
     * logger name, creating a new one if necessary.</p>
     *
     * @param name Name of the requested logger
     */
    private synchronized Logger logger(String name) {

        Logger logger = (Logger) loggers.get(name);
        if (logger == null) {
            logger = LogManager.getLogManager().getLogger(name);
            loggers.put(name, logger);
        }
        return logger;

    }


}
