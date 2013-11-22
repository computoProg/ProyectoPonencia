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
 * $Id: TemplateComponentConfigBean.java 511459 2007-02-25 06:38:52Z gvanmatre $
 */
package org.apache.shale.clay.config.beans;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.shale.clay.config.ClayXmlParser;
import org.apache.shale.clay.config.Globals;

/**
 * <p>This ConfigBean is responsible for handling full XML views.  For full XML views,
 * the viewId will correspond to an XML file that defines meta-component declarations
 * specific for the page.  It's assumed that here will be a top-level component with a
 * jsfid matching the viewId.</p>
 */
public class TemplateComponentConfigBean extends TemplateConfigBean {

    /**
     * <p>Returns an integer value use to order the registered {@link ConfigBean} instances
     * with the {@link ConfigBeanFactory}.
     * </p>
     *
     * @return a weight of 2
     */
    public int getWeight() {
        return 2;
    }


    /**
     * <p>This is an overridden method called from the init method.
     * It loads an instance of the {@link ClayXmlParser} and
     * establishes a Map collection to hold the resource
     * {@link org.apache.shale.clay.config.beans.ComponentConfigBean$WatchDog}'s.</p>
     */
    protected void loadConfigFiles() {

        parser = new ClayXmlParser();
        parser.setConfig(this);

        // a comma delimited value list of config files
        String param = context.getInitParameter(Globals.CLAY_FULLXML_CONFIG_FILES);

        // pass the config bean to the parser
        parser.setConfig(this);

        // map holding the resource watchers
        watchDogs = Collections.synchronizedMap(new TreeMap());


        if (param != null && param.length() > 0) {
            // create the watch dog with a list of config files to look for changes
            WatchDog watchDog = new WatchDog(getConfigDefinitions(param),
                    Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);

            // adds the watcher to a map identified by name
            watchDogs.put(watchDog.getName(), watchDog);

            // loads the config files
            watchDog.refresh(true);

        }


        param = null;
    }


    /**
     * <p>If the <code>forceReload</code> is <code>true</code>,
     * the <code>displayElements</code> cache is invalidated.
     * A <code>true</code> value is returned if cache has
     * been cleared.
     * <br/><br/>
     * All files loaded on-demand are purged.  Full view definitions
     * loaded using the <code>Globals.CLAY_FULLXML_CONFIG_FILES</code>
     * initialization parameter are reloaded if modified or if a
     * <code>forceReload</code> is specified.</p>
     *
     * @param forceReload <code>true</code> if the group of associated resources should be reloaded
     * @return <code>true</code> if one of the resources changed
     */
    public boolean refresh(boolean forceReload) {
        boolean wasDirty = forceReload;

        //look for a default watch of full view config files
        WatchDog defaultWatchDog = (WatchDog) watchDogs.get(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);

        //clear out all the on-demand beans
        if (forceReload) {

            //remove from the list without the clean-up
            if (defaultWatchDog != null) {
               watchDogs.remove(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);
            }

            // remove all old templates
            Iterator wi = watchDogs.entrySet().iterator();
            while (wi.hasNext()) {
                Map.Entry e = (Map.Entry) wi.next();
                WatchDog watchDog = (WatchDog) e.getValue();
                clear(watchDog.getName());
                if (watchDog != null) {
                    watchDog.destroy();
                }
            }
            watchDogs.clear();

            //push back into the list
            if (defaultWatchDog != null) {
               watchDogs.put(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG, defaultWatchDog);
            }

        }

        //check for dirty cache
        if (defaultWatchDog != null) {
            wasDirty = defaultWatchDog.refresh(forceReload);
        }

        return wasDirty;
    }


    /**
     * <p>Overrides the super call to change the condition of the filter.  This
     * {@link ConfigBean} can create components where the id end in the suffix
     * defined in the web deployment descriptor as a initialization parameter with
     * the name defined by <code>Globals.CLAY_XML_TEMPLATE_SUFFIX</code>  Or, using
     * the default defined by <code>Globals.CLAY_DEFAULT_XML_TEMPLATE_SUFFIX</code>
     *
     * @param id jsfid
     * @return <code>true</code> if the jsfid can be handle
     */
    public boolean validMoniker(String id) {
        return id.endsWith(suffixes[1]);
    }


}
