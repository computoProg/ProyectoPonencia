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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Implementation of {@link Logger} that interacts with the Apache
 * Commons Logging APIs.</p>
 */
public final class CommonsLogger extends AbstractLogger {
    

    // ------------------------------------------------------ Condition Checking


    /** {@inheritDoc} */
    public boolean isTraceEnabled(String name) {
        return logger(name).isTraceEnabled();
    }


    /** {@inheritDoc} */
    public boolean isDebugEnabled(String name) {
        return logger(name).isDebugEnabled();
    }


    /** {@inheritDoc} */
    public boolean isInfoEnabled(String name) {
        return logger(name).isInfoEnabled();
    }


    /** {@inheritDoc} */
    public boolean isWarnEnabled(String name) {
        return logger(name).isWarnEnabled();
    }


    /** {@inheritDoc} */
    public boolean isErrorEnabled(String name) {
        return logger(name).isErrorEnabled();
    }


    /** {@inheritDoc} */
    public boolean isFatalEnabled(String name) {
        return logger(name).isFatalEnabled();
    }


    // ------------------------------------------------------------- Log Methods


    /** {@inheritDoc} */
    public void trace(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).trace(message(message, params));
        } else {
            logger(name).trace(message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void debug(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).debug(message(message, params));
        } else {
            logger(name).debug(message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void info(String name, String message,
                     Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).info(message(message, params));
        } else {
            logger(name).info(message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void warn(String name, String message,
                     Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).warn(message(message, params));
        } else {
            logger(name).warn(message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void error(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).error(message(message, params));
        } else {
            logger(name).error(message(message, params), exception);
        }
    }


    /** {@inheritDoc} */
    public void fatal(String name, String message,
                      Throwable exception, Object[] params) {
        if (exception == null) {
            logger(name).fatal(message(message, params));
        } else {
            logger(name).fatal(message(message, params), exception);
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Map of Commons Logging <code>Log</code> instances, keyed by name.</p>
     */
    private Map loggers = new HashMap();


    /**
     * <p>Return a Commons Logging <code>Log</code> instance for the specified
     * logger name, creating a new one if necessary.</p>
     *
     * @param name Name of the requested logger
     */
    private synchronized Log logger(String name) {

        Log logger = (Log) loggers.get(name);
        if (logger == null) {
            logger = LogFactory.getLog(name);
            loggers.put(name, logger);
        }
        return logger;

    }


}
