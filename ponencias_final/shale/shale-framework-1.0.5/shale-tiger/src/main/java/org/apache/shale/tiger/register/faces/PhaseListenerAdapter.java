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
 *
 * $Id: FacesComponent.java 372303 2006-01-25 20:09:24Z craigmcc $
 */

package org.apache.shale.tiger.register.faces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.FacesException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.apache.shale.tiger.register.AfterPhase;
import org.apache.shale.tiger.register.BeforePhase;
import org.apache.shale.tiger.register.FacesPhaseListener;

/**
 * <p>Adapter class for a phase listener that specifies the
 * {@link FacesPhaseListener} annotation, but does not actually
 * implement the <code>PhaseListener</code> interface.</p>
 */
public final class PhaseListenerAdapter implements PhaseListener {


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -8684913975588239869L;


    /**
     * <p>Construct a new adapter instance around the specified object
     * instance, whose defining class MUST specify the
     * {@link FacesPhaseListener} annotation, and whose implementation
     * MUST NOT be an implementation of <code>javax.faces.event.PhaseListener</code>.</p>
     *
     * @param instance The object instance for which we should
     *  construct a call-through adapter
     *
     * @exception IllegalArgumentException if the class for the specified
     *  instance does not have a @FacesPhaseListener annotation
     * @exception IllegalArgumentException if the method specified by the
     *  @BeforePhase or @AfterPhase annotaion does not take exactly one
     *  parameter of type <code>javax.faces.event.PhaseEvent</code>
     */
    public PhaseListenerAdapter(Object instance) {

        // Save the instance that we are adapting for
        this.instance = instance;

        // Look up the @FacesFacesListener annotation for this class
        FacesPhaseListener fpl = instance.getClass().getAnnotation(FacesPhaseListener.class);
        if (fpl == null) {
            throw new IllegalArgumentException("Implementing class "
              + instance.getClass().getName()
              + " does not have the @FacesPhaseListener annotation");
        }

        // Extract the relevant before and after event methods from the
        // underlying class
        Method[] methods = instance.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Class[] signature = method.getParameterTypes();
            if (method.getAnnotation(BeforePhase.class) != null) {
                if (signature.length != 1) {
                    throw new IllegalArgumentException("Method " + method.getName()
                            + " of class " + instance.getClass().getName()
                            + " does not take a exactly one parameter");
                } else if (!(signature[0] == PhaseEvent.class)) {
                    throw new IllegalArgumentException("Method " + method.getName()
                            + " of class " + instance.getClass().getName()
                            + " does not take a javax.faces.event.PhaaseEvent parameter");
                } else {
                    this.beforePhase = method;
                }
            } else if (method.getAnnotation(AfterPhase.class) != null) {
                if (signature.length != 1) {
                    throw new IllegalArgumentException("Method " + method.getName()
                            + " of class " + instance.getClass().getName()
                            + " does not take a exactly one parameter");
                } else if (!(signature[0] == PhaseEvent.class)) {
                    throw new IllegalArgumentException("Method " + method.getName()
                            + " of class " + instance.getClass().getName()
                            + " does not take a javax.faces.event.PhaaseEvent parameter");
                } else {
                    this.afterPhase = method;
                }
            }
        }

        // Extract the relevant phase identifer from the underlying class
        FacesPhaseListener.PhaseId pi = fpl.phaseId();
        if (pi == FacesPhaseListener.PhaseId.ANY_PHASE) {
            this.phaseId = PhaseId.ANY_PHASE;
        } else if (pi == FacesPhaseListener.PhaseId.RESTORE_VIEW) {
            this.phaseId = PhaseId.RESTORE_VIEW;
        } else if (pi == FacesPhaseListener.PhaseId.APPLY_REQUEST_VALUES) {
            this.phaseId = PhaseId.APPLY_REQUEST_VALUES;
        } else if (pi == FacesPhaseListener.PhaseId.PROCESS_VALIDATIONS) {
            this.phaseId = PhaseId.PROCESS_VALIDATIONS;
        } else if (pi == FacesPhaseListener.PhaseId.UPDATE_MODEL_VALUES) {
            this.phaseId = PhaseId.UPDATE_MODEL_VALUES;
        } else if (pi == FacesPhaseListener.PhaseId.INVOKE_APPLICATION) {
            this.phaseId = PhaseId.INVOKE_APPLICATION;
        } else if (pi == FacesPhaseListener.PhaseId.RENDER_RESPONSE) {
            this.phaseId = PhaseId.RENDER_RESPONSE;
        }

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Method definition for the {@link AfterPhase} method for this
     * phase listener class.</p>
     */
    private Method afterPhase;


    /**
     * <p>Method definition for the {@link BeforePhase} method for this
     * phase listener class.</p>
     */
    private Method beforePhase;


    /**
     * <p>The listener instance for which we are an adapter.</p>
     */
    private Object instance;


    /**
     * <p>The <code>PhaseId</code> to be returned by our
     * <code>getPhaseId()</code> method.</p>
     */
    private PhaseId phaseId;


    // --------------------------------------------------- PhaseListener Methods


    /**
     * <p>Process an "after phase" event.</p>
     *
     * @param event The <code>PhaseEvent</code> to be processed
     *
     * @exception FacesException if an exception occurs invoking the
     *  appropriate event handling method
     */
    public void afterPhase(PhaseEvent event) {

        if (afterPhase == null) {
            return;
        }
        try {
            afterPhase.invoke(instance, event);
        } catch (InvocationTargetException e) {
            throw new FacesException(e.getTargetException());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /**
     * <p>Process a "before phase" event.</p>
     *
     * @param event The <code>PhaseEvent</code> to be processed
     *
     * @exception FacesException if an exception occurs invoking the
     *  appropriate event handling method
     */
    public void beforePhase(PhaseEvent event) {

        if (beforePhase == null) {
            return;
        }
        try {
            beforePhase.invoke(instance, event);
        } catch (InvocationTargetException e) {
            throw new FacesException(e.getTargetException());
        } catch (Exception e) {
            throw new FacesException(e);
        }

    }


    /**
     * <p>Return the identifier of the phase(s) we are interested in.</p>
     */
    public PhaseId getPhaseId() {

        return this.phaseId;

    }


}
