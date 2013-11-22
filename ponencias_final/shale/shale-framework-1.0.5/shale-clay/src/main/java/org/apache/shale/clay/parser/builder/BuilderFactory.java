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
 * $Id: BuilderFactory.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser.builder;

import java.net.URL;

import javax.faces.context.FacesContext;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.parser.Node;
import org.apache.shale.clay.parser.builder.chain.BuilderRuleContext;

/**
 * <p>
 * This is an abstract factory that returns a {@link Builder} mapped to a
 * {@link Node}. The mapping is performed by executing <code>Chain</code>
 * <code>Globals.FIND_BUILDER_COMMAND_NAME</code>
 * in <code>Globals.BUILDER_CATALOG_NAME</code> defined in
 * <code>Globals.BUILDER_RESOURCE_NAME</code>.
 * </p>
 */
public final class BuilderFactory {

    /**
     * <p>
     * Common Logger utility.
     * </p>
     */
    private static Log log;
    static {
        log = LogFactory.getLog(BuilderFactory.class);
    }

    /**
     * <p>Hidden constructor for utility class.</p>
     */
    private BuilderFactory() {
       super();
    }

    /**
     * <p>
     * Returns catalog <code>Globals.BUILDER_CATALOG_NAME</code> from file
     * <code>Globals.BUILDER_RESOURCE_NAME</code>.
     * </p>
     *
     * @return commons chains catalog
     * @exception Exception finding catalog
     */
    protected static Catalog getCatalog() throws Exception {

        Catalog catalog = CatalogFactory.getInstance().getCatalog(Globals.BUILDER_CATALOG_NAME);
        if (catalog == null) {

            ConfigParser parser = new ConfigParser();
            URL url = parser.getClass().getClassLoader().getResource(Globals.BUILDER_RESOURCE_NAME);
            if (url == null) {
                throw new IllegalArgumentException(Globals.BUILDER_RESOURCE_NAME);
            }
            parser.parse(url);

            catalog = CatalogFactory.getInstance().getCatalog(Globals.BUILDER_CATALOG_NAME);

        }

        return catalog;

    }

    /**
     * <p>
     * Returns a {@link Builder} mapped to a {@link Node} by executing chain
     * command <code>Globals.FIND_BUILDER_COMMAND_NAME</code> The
     * {@link BuilderRuleContext} is passed to the chain.
     * </p>
     *
     * @param node the markup node
     * @return builder that handles the node
     */
    public static Builder getRenderer(Node node) {

        BuilderRuleContext context = new BuilderRuleContext();
        context.setNode(node);
        try {
            Catalog catalog = getCatalog();

            Command command = null;
            if (node.getQname() == null) {
                command = catalog.getCommand(Globals.FIND_DEFAULT_BUILDER_COMMAND_NAME);
            } else {
                String prefix = node.getQname();
                String uri = node.getNamespaceURI(prefix);
                if (uri != null) {
                    command = catalog.getCommand(uri);
                    if (command == null) {
                        FacesContext facesContext = FacesContext.getCurrentInstance();
                        if (facesContext != null) {
                            facesContext.getExternalContext().getRequestMap()
                            .put(Globals.CLAY_CUSTOM_BUILDER_XMLNS, uri);
                        }

                        command = catalog.getCommand(Globals.FIND_UNKNOWN_BUILDER_COMMAND_NAME);
                    }
                } else {
                    command = catalog.getCommand(Globals.FIND_UNKNOWN_BUILDER_COMMAND_NAME);
                }

            }

            command.execute(context);

        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

        return context.getBuilder();
    }
}
