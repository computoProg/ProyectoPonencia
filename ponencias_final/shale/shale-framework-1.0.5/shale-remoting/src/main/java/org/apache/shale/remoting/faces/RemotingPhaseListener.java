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

package org.apache.shale.remoting.faces;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Mapping;
import org.apache.shale.remoting.Mappings;
import org.apache.shale.remoting.Processor;

/**
 * <p>A JavaServer Faces <code>PhaseListener</code> that provides support for
 * intercepting some incoming requests, and processing them separately from the
 * standard Request Processing Lifecycle, without requiring explicit
 * configuration (for example, of a Servlet or a Filter).</p>
 */
public class RemotingPhaseListener implements PhaseListener {


    // ------------------------------------------------------------ Constructors


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 9188955342844983587L;


    /**
     * <p>Creates a new instance of RemotingPhaseListener.</p>
     */
    public RemotingPhaseListener() {
    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p><code>ResourceBundle</code> containing our localized messages.</p>
     */
    private ResourceBundle bundle =
            ResourceBundle.getBundle("org.apache.shale.remoting.Bundle");


    /**
     * <p>Helper object to acquire a reference to the {@link Mappings}
     * instance for this web application.</p>
     */
    private MappingsHelper helper = new MappingsHelper();


    /**
     * <p>Log instance for this class.</p>
     */
    private transient Log log = null;


    // --------------------------------------------------- PhaseListener Methods


    /**
     * <p>Perform interception processing activities <em>after</em> the
     * specified phase processing has been performed.</p>
     *
     * @param event <code>PhaseEvent</code> to be processed
     */
    public void afterPhase(PhaseEvent event) {

        // Acquire a reference to the FacesContext for this request
        FacesContext context = event.getFacesContext();
        if (log().isDebugEnabled()) {
            log().debug("Checking view identifier '" + context.getViewRoot().getViewId() + "'");
        }

        // Match this view identifier against our configured patterns
        Iterator mappings = helper.getMappings(context).getMappings().iterator();
        while (mappings.hasNext()) {
            Mapping mapping = (Mapping) mappings.next();
            String resourceId = mapping.mapViewId(context);
            if (resourceId != null) {
                if (log().isTraceEnabled()) {
                    log().trace("View identifier '" + context.getViewRoot().getViewId()
                                + "' matched pattern '" + mapping.getPattern()
                                + "' with resource id '" + resourceId + "'");
                }
                try {
                    Processor processor = mapping.getProcessor();
                    processor.process(context, resourceId);
                } catch (IOException e) {
                    throw new FacesException(e);
                }
                break;
            }
        }

    }


    /**
     * <p>Perform interception processing activities <em>before</em> the
     * specified phase processing has been performed.</p>
     *
     * @param event <code>PhaseEvent</code> to be processed
     */
    public void beforePhase(PhaseEvent event) {

        // No processing required

    }


    /**
     * <p>Return the identifier of the JavaServer Faces request processing
     * lifecycle phase(s) that we are interested in.</p>
     */
    public PhaseId getPhaseId() {

        return PhaseId.RESTORE_VIEW;

    }


    // --------------------------------------------------------- Support Methods


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(RemotingPhaseListener.class);
        }
        return log;

    }


}
