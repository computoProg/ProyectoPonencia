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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.ViewHandler;
import org.apache.shale.remoting.Mapping;
import org.apache.shale.remoting.Mappings;

/**
 * <p>Default implementation of {@link Mappings}.</p>
 */
public class MappingsImpl implements Mappings, Serializable {


    // ------------------------------------------------------ Instance Variables


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 7761184295213057256L;


    /**
     * <p>The extension that will replace the <code>FacesServlet</code>
     * extension, if the servlet is extension mapped.</p>
     */
    private String extension = ViewHandler.DEFAULT_SUFFIX;


    /**
     * <p>A list of {@link Mapping} instances we understand.</p>
     */
    private List mappings = new ArrayList();


    /**
     * <p>The zero relative index of the URL pattern, in the <code>patterns</code>
     * array, to use for generating resource URLs.</p>
     */
    private int patternIndex = 0;


    /**
     * <p>The list of URL patterns for <code>FacesServlet</code>.</p>
     */
    private String[] patterns = new String[0];


    // -------------------------------------------------------- Mappings Methods


    /** {@inheritDoc} */
    public void addMapping(Mapping mapping) {
        if (mapping == null) {
            throw new NullPointerException();
        }
        synchronized (mappings) {
            if (!mappings.contains(mapping)) {
                mappings.add(mapping);
            }
        }
    }


    /** {@inheritDoc} */
    public String getExtension() {
        return this.extension;
    }


    /** {@inheritDoc} */
    public Mapping getMapping(String pattern) {
        if (pattern == null) {
            throw new NullPointerException();
        }
        synchronized (mappings) {
            Iterator items = mappings.iterator();
            while (items.hasNext()) {
                Mapping item = (Mapping) items.next();
                if (pattern.equals(item.getPattern())) {
                    return item;
                }
            }
            return null;
        }
    }


    /** {@inheritDoc} */
    public List getMappings() {
        return this.mappings;
    }


    /** {@inheritDoc} */
    public int getPatternIndex() {
        return this.patternIndex;
    }


    /** {@inheritDoc} */
    public void setPatternIndex(int patternIndex) {
        this.patternIndex = patternIndex;
    }


    /** {@inheritDoc} */
    public String[] getPatterns() {
        return this.patterns;
    }


    /** {@inheritDoc} */
    public void removeMapping(Mapping mapping) {
        if (mapping == null) {
            throw new NullPointerException();
        }
        synchronized (mappings) {
            mappings.remove(mapping);
        }
    }


    /** {@inheritDoc} */
    public void setExtension(String extension) {
        this.extension = extension;
    }


    /** {@inheritDoc} */
    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }


}
