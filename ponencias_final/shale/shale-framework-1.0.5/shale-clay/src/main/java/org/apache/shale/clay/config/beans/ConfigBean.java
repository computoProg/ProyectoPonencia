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
 * $Id: ConfigBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.net.URL;

import javax.servlet.ServletContext;

/**
 * <p>This interfaces should be implemented by a object pool that
 *  is registered with the {@link ConfigBeanFactory}.
 * </p>
 */
public interface ConfigBean extends Comparable {

     /**
      * <p>Top-level interface that defines a single configuration
      * file entry.<p>
      */
     interface ConfigDefinition {
         /**
          * <p>Returns the URL of the config file.</p>
          *
          * @return config file
          */
         URL getConfigUrl();
         /**
          * <p>Returns the last modified date of the config file.</p>
          *
          * @return date last modified
          */
         long getLastModified();
         /**
          * <p>Sets the last modified date of the config file.</p>
          *
          * @param lastModified timestamp the file was last changed
          */
         void setLastModified(long lastModified);
     }

    /**
     * <p>Factory method that returns a {@link ComponentBean}
     * using an identifier.
     * </p>
     *
     * @param id jsfid of a config bean
     * @return config bean
     */
    ComponentBean getElement(String id);
    /**
     * <p>Returns <code>true</code> if the <code>jsfid</code> can be
     * used by the <code>getElement(jsfid)</code> to return a
     * {@link ComponentBean}.</p>
     *
     * @param id jsfid of a config bean
     * @return <code>true</code> if the config bean is handled here
     */
    boolean validMoniker(String id);
    /**
     * <p>A ordering weight used by the {@link ConfigBeanFactory}
     * for determining the ConfigBean that will return a {@link ComponentBean}
     * for a <code>jsfid</code>.
     * </p>
     *
     * @return ordinal value representing the handlers precedence
     */
    int getWeight();
    /**
     * <p>Initialization method passing the <code>ServletContext</code>.</p>
     *
     * @param context web container servlet context
     */
    void init(ServletContext context);
    /**
     * <p>This method is invoked with the application is unloaded.  The
     * {@link ConfigBeanFactory} will invoke this method on all
     * registered <code>ConfigBean</code>.  This sequence will be started
     * by the {@link org.apache.shale.clay.config.ClayConfigureListener}
     * </p>
     */
    void destroy();
    /**
     * <p>Returns an instance of the <code>ServletContext</code> set
     * by the <code>init(ServletContext)</code> method.
     *
     * @return web container servlet context
     */
    ServletContext getServletContext();
    /**
     * <p>Fixes up the meta inheritance of a {@link ComponentBean}.  It
     * assumes that <code>assignParent(ComponentBean</code> has already
     * been called</p>
     *
     * @param b config bean needing inheritance resolved
     */
    void realizingInheritance(ComponentBean b);
    /**
     * <p>Sets the isA parent's for a {@link ComponentBean}.  The next
     * step would be to call the <code>realizeInheritance(ComponentBean)</code>
     * method.
     * </p>
     *
     * @param b config bean needing heritage fixed-up
     */
    void assignParent(ComponentBean b);

    /**
     * <p>This method should be called from key points in the application to invoke
     * automatic reloading of the configuration files if they have been modified since
     * last reloaded.  If the parameter <code>forceReload</code> is <code>true</code>,
     * all files will be reloaded.  The return value is <code>true</code>, if the
     * files were reloaded.</p>
     *
     * @param forceReload <code>true</code> if all template and config files are reloaded
     * @return <code>true</code> if a modifed file was found
     */
    boolean refresh(boolean forceReload);

    /**
     * <p>Verifies there is not a duplicate component id within a naming container.
     * A root {@link ComponentBean} is passed as a single parameter.</p>
     *
     * @param b root config bean
     */
     void checkTree(ComponentBean b);


}
