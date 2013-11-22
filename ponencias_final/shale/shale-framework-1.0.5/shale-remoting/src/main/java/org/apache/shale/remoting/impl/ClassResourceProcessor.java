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

package org.apache.shale.remoting.impl;

import java.net.URL;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Constants;
import org.apache.shale.remoting.Processor;

/**
 * <p>Implementation of {@link Processor} which serves resources from the
 * class path of the web application.  View identifiers shoud be a fully
 * qualified path, beginning with a slash ("/") character (for example,
 * <code>/org/apache/shale/remoting/MyResource.css</code>).</p>
 */
public class ClassResourceProcessor extends AbstractResourceProcessor {


    // ------------------------------------------------------------ Constructors



    // ------------------------------------------------------ Instance Variables


    /**
     * <p><code>ResourceBundle</code> containing our localized messages.</p>
     */
    private ResourceBundle bundle = ResourceBundle.getBundle("org.apache.shale.remoting.Bundle");


    /**
     * <p>Log instance for this class.</p>
     */
    private transient Log log = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Force our default excludes list to be included.</p>
     *
     * @param excludes Application specified excludes list
     */
    public void setExcludes(String excludes) {

        if ((excludes != null) && (excludes.length() > 0)) {
            super.setExcludes(Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT
                              + "," + excludes);
        } else {
            super.setExcludes(Constants.CLASS_RESOURCES_EXCLUDES_DEFAULT);
        }

    }


    // -------------------------------------------------------- Abstract Methods


    /** {@inheritDoc} */
    protected URL getResourceURL(FacesContext context, String resourceId) {

        // Disallow access to Java classes
        String resourceIdLower = resourceId.toLowerCase();
        if (resourceIdLower.endsWith(".class")) {
            if (log().isWarnEnabled()) {
                log().warn(bundle.getString("resource.refuse"));
                log().warn(resourceId);
            }
            return null;
        }

        // Acquire a reference to the ClassLoader for this web application
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        // Return a URL to the class loader resource (if it exists)
        try {
            URL url = cl.getResource(resourceId.substring(1));
            if (log().isDebugEnabled()) {
                log().debug("getResource(" + resourceId + ") --> " + url);
            }
            return url;
        } catch (Exception e) {
            if (log().isErrorEnabled()) {
                log().error(bundle.getString("resource.exception"), e);
                log().error(resourceId);
            }
            return null;
        }

    }



    // --------------------------------------------------------- Private Methods



    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(ClassResourceProcessor.class);
        }
        return log;

    }


}
