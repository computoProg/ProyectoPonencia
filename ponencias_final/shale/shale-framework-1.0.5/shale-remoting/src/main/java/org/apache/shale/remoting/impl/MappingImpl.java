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

import javax.faces.context.FacesContext;
import org.apache.shale.remoting.Mapping;
import org.apache.shale.remoting.Mappings;
import org.apache.shale.remoting.Mechanism;
import org.apache.shale.remoting.Processor;

/**
 * <p>Default implementation of {@link Mapping}.  This implementation recognizes
 * patterns similar to URL mappings in the Servlet Specification:</p>
 * <ul>
 * <li><em>/foo/*</em> - prefix matching</li>
 * <li><em>*.foo</em> - extension matching</li>
 * </ul>
 * <p>If a view identifier matches, the corresponding resource identifier is
 * calculated by stripping the non-wildcard part of the view identifier
 * (<code>/foo</code> or <code>.foo</code> for the examples above) and
 * returning the remainder.</p>
 */
public class MappingImpl implements Mapping {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an unconfigured instance.</p>
     */
    public MappingImpl() {
        this(null, null, null);
    }


    /**
     * <p>Construct a fully configured instance.</p>
     *
     * @param mechanism {@link Mechanism} used to produce response for this mapping
     * @param pattern URL matching pattern for this mapping
     * @param processor Processor instance for this mapping
     */
    public MappingImpl(Mechanism mechanism, String pattern, Processor processor) {
        setMechanism(mechanism);
        setPattern(pattern);
        setProcessor(processor);
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The {@link Mappings} instance that owns this mapping.</p>
     */
    private Mappings mappings = null;


    /**
     * <p>The non-wildcard part of the pattern for this mapping.</p>
     */
    private String match = null;


    /**
     * <p>The {@link Mechanism} used to produce response for this mapping.</p>
     */
    private Mechanism mechanism = null;


    /**
     * <p>The URL matching pattern for this mapping.</p>
     */
    private String pattern = null;


    /**
     * <p>Flag indicating we are doing prefix matching, versus extension
     * mapping.</p>
     */
    private boolean prefix = true;


    /**
     * <p>The <code>Processor</code> instance for this mapping.</p>
     */
    private Processor processor = null;


    // --------------------------------------------------------- Mapping Methods


    /** {@inheritDoc} */
    public Mappings getMappings() {
        return this.mappings;
    }


    /** {@inheritDoc} */
    public void setMappings(Mappings mappings) {
        this.mappings = mappings;
    }


    /** {@inheritDoc} */
    public Mechanism getMechanism() {
        return this.mechanism;
    }


    /** {@inheritDoc} */
    public void setMechanism(Mechanism mechanism) {
        this.mechanism = mechanism;
    }


    /** {@inheritDoc} */
    public String getPattern() {
        return this.pattern;
    }


    /** {@inheritDoc} */
    public void setPattern(String pattern) {

        if (pattern == null) {
            this.match = null;
            this.pattern = null;
            return;
        } else if (pattern.endsWith("/*")) {
            if (!pattern.startsWith("/")) {
                throw new IllegalArgumentException(pattern);
            }
            this.match = pattern.substring(0, pattern.length() - 1);
            this.pattern = pattern;
            this.prefix = true;
        } else if (pattern.startsWith("*.")) {
            int period = pattern.lastIndexOf('.');
            if (period != 1) {
                throw new IllegalArgumentException(pattern);
            }
            this.match = pattern.substring(1);
            this.pattern = pattern;
            this.prefix = false;
        } else {
            throw new IllegalArgumentException(pattern);
        }

    }


    /** {@inheritDoc} */
    public Processor getProcessor() {
        return this.processor;
    }


    /** {@inheritDoc} */
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }


    /** {@inheritDoc} */
    public String mapResourceId(FacesContext context, String resourceId) {

        // Acquire the servlet mapping to be used for FacesServlet (if any --
        // there will not be such a mapping in a portlet environment)
        String[] patterns = getMappings().getPatterns();
        String pattern = null;
        if ((patterns != null) && (patterns.length > 0)) {
            pattern = patterns[getMappings().getPatternIndex()];
        }

        // Configure the entire URL we will return
        StringBuffer sb = new StringBuffer();
        if (pattern != null) {
            sb.append(context.getExternalContext().getRequestContextPath());
        }
        if ((pattern != null) && (pattern.endsWith("/*"))) { // FacesServlet is prefix mapped
            sb.append(pattern.substring(0, pattern.length() - 2));
        }
        if (getPattern().endsWith("*")) { // Processor is prefix mapped
            sb.append(getPattern().substring(0, getPattern().length() - 2));
        }
        sb.append(resourceId);
        if (getPattern().startsWith("*.")) { // Processor is extension mapped
            sb.append(getPattern().substring(1));
        }
        if ((pattern != null) && (pattern.startsWith("*."))) { // FacesServlet is extension mapped
            sb.append(pattern.substring(1));
        }

        // Return the completed URL
        if (pattern == null) {
            // In a portlet environment, let the server map our "view identifier"
            // to something that will be processed through the JSF lifecycle
            return context.getApplication().getViewHandler().getActionURL(context, sb.toString());
        } else {
            // In a web application, our "view identifier" has already been
            // mapped exactly the way we need it
            return sb.toString();
        }

    }


    /** {@inheritDoc} */
    public String mapViewId(FacesContext context) {

        // Extract the view identifier we will be using to match against
        String viewId = viewId(context);
        if (viewId == null) {
            return null;
        }

        // Perform prefix or extension matching as requested
        if (prefix) {
            if (viewId.startsWith(match) && !viewId.equals(match)) {
                return viewId.substring(match.length() - 1);
            } else {
                return null;
            }
        } else {
            if (viewId.endsWith(match) && !viewId.equals(match)) {
                return viewId.substring(0, viewId.length() - match.length());
            } else {
                return null;
            }
        }

    }


    // ---------------------------------------------------------- Object Methods


    /**
     * <p>Return the hash code for this object.</p>
     */
    public int hashCode() {
        if (this.pattern == null) {
            return 0;
        } else {
            return this.pattern.hashCode();
        }
    }


    /**
     * <p>Two {@link Mapping}s are equal if they have the same pattern.</p>
     *
     * @param object Object to which we are tested for equality
     */
    public boolean equals(Object object) {
        if ((object == null) || !(object instanceof Mapping)) {
            return false;
        }
        if (this.pattern == null) {
            return ((Mapping) object).getPattern() == null;
        } else {
            return this.pattern.equals(((Mapping) object).getPattern());
        }
    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Extract and return the view identifier for this request, after
     * stripping any replacement suffix if <code>FacesServlet</code> is
     * being extension mapped.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    private String viewId(FacesContext context) {

        // Get the raw view identifier
        String viewId = context.getViewRoot().getViewId();

        // If the view identifier ends with the configured (or default)
        // replacement suffix, just strip it and return
        String extension = mappings.getExtension();
        if ((extension != null) && (viewId.endsWith(extension))) {
            return viewId.substring(0, viewId.length() - extension.length());
        }

        // The JSF RI (version 1.1) has a bug where it does *not* replace
        // the incoming extension during Restore View phase, as is required
        // by Section 2.2.1 of the JSF Specification.  As a result, the view
        // identifier immediately after Restore View completes will be something
        // like "/index.faces" instead of "/index.jsp".  To work around this
        // bug, walk through the URL patterns to which FacesServlet is mapped.
        // If we detect an extension matching pattern that is found on our
        // current view identifier, strip that and return as well.
        String[] patterns = mappings.getPatterns();
        if ((patterns == null) || (patterns.length < 1)) {
            return viewId;
        }
        for (int i = 0; i < patterns.length; i++) {
            if (!patterns[i].startsWith("*.")) {
                continue;
            }
            String match = patterns[i].substring(1);
            if (viewId.endsWith(match)) {
                return viewId.substring(0, viewId.length() - match.length());
            }
        }

        // No matches, so just return what we have
        return viewId;

    }

}
