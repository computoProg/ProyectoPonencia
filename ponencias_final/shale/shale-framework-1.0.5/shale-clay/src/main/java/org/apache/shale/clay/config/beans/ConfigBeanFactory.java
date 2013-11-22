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
 * $Id: ConfigBeanFactory.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * <p>This is an abstract factory that contains a ordered collection of
 * object pools {@link ConfigBean}.  The configuration beans are registered on
 * application startup by the {@link ClayConfigListener}.
 * </p>
 */
public final class ConfigBeanFactory {

    /**
     * <p>Hide constructor on a factory class.</p>
     */
    private ConfigBeanFactory() {
        super();
    }

    /**
     * <p>A static ordered set of {@link ConfigBean} instances.</p>
     */
    private static Collection configBeans = new TreeSet();

    /**
     *  <p>This method is invoked to register a class instance implementing
     *  {@link ConfigBean} interface.
     *  </p>
     *
     *  @param config {@link ConfigBean} collection of faces component metadata
     */
    public static void register(ConfigBean config) {
        configBeans.add(config);
    }

    /**
     * <p>This method will return a {@link ConfigBean} that can be used
     * to return component metadata. The suffix of the parameter is used to
     * find a <code>ConfigBean</code> that has the metadata.
     * </p>
     *
     * @param id jsfid
     * @return config bean
     */
    public static ConfigBean findConfig(String id) {
        Iterator ci = configBeans.iterator();
        ConfigBean config = null;
        while (ci.hasNext()) {
            config = (ConfigBean) ci.next();
            if (config.validMoniker(id)) {
                break;
            }
        }

        return config;
    }

    /**
     * <p>Invoked by the context listener {@link org.apache.shale.clay.config.ClayConfigureListener} on
     * application shutdown to clean up cached resources.
     * </p>
     */
    public static void destroy() {
        Iterator ci = configBeans.iterator();
        ConfigBean config = null;
        while (ci.hasNext()) {
            config = (ConfigBean) ci.next();
            config.destroy();

        }
        configBeans.clear();
    }


    /**
     * <p>This method should be called from key points in the application to invoke
     * automatic reloading of the configuration files if they have been modified since
     * last reloaded.  If the XML files have changed, all files have to be reloaded.
     * This includes HTML template files.
     * </p>
     */
    public static void refresh() {
        Iterator ci = configBeans.iterator();
        //the logic assumes the ComponentConfigBean will be first
        //in the ordered list.  If the XML files are dirty, the
        //template cache must be reestablished.
        boolean wasDirty = false;
        while (ci.hasNext()) {
            ConfigBean config = (ConfigBean) ci.next();
            wasDirty = config.refresh(wasDirty);
        }
    }

}
