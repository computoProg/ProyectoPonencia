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
 * $Id: ClayConfigParser.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config;

import java.io.IOException;
import java.net.URL;

import org.apache.shale.clay.config.beans.ConfigBean;
import org.xml.sax.SAXException;

/**
 * <p>This interfaces is used by a {@link ConfigBean} instance to load
 * a configuration file.  These files come in two flavors XML and HTML.
 * The {@link org.apache.shale.clay.config.beans.ComponentConfigBean}
 * handles materializing XML documents into a graph of
 * {@link org.apache.shale.clay.config.beans.ComponentBean} and the
 * {@link org.apache.shale.clay.config.beans.TemplateConfigBean}
 * loads an HTML template into {@link org.apache.shale.clay.config.beans.ComponentBean}
 * object representations.
 * </p>
 */
public interface ClayConfigParser {

    /**
     * <p>Sets an instance of the {@link ConfigBean} that pools a
     * collection of faces meta-component definitions.
     * </p>
     *
     * @param config bean pool
     */
    void setConfig(ConfigBean config);

    /**
     * <p>Returns an instance of the {@link ConfigBean} that pools a
     * collection of faces meta-component definitions.
     * </p>
     *
     * @return bean pool
     */
    ConfigBean getConfig();

    /**
     * <p>Loads the <code>url</code> identified by the <code>watchDogName</code>
     * into the {@link ConfigBean} object pool.</p>
     *
     * @param url file to load
     * @param watchDogName dependency watch group for the url file
     * @exception IOException file load
     * @exception SAXException xml parser
     */
    void loadConfigFile(URL url, String watchDogName)
           throws IOException, SAXException;
}
