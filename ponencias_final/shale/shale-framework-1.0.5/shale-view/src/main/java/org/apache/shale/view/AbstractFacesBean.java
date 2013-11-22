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

package org.apache.shale.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

/**
 * <p>Convenient abstract base class for application beans that wish to
 * interact with JavaServer Faces request processing facilities.
 * <strong>WARNING</strong> - These methods are only effective during
 * the lifecycle of a JavaServer Faces request.</p>
 *
 * $Id: AbstractFacesBean.java 464373 2006-10-16 04:21:54Z rahul $
 */
public abstract class AbstractFacesBean {


    // -------------------------------- JavaServer Faces Object Accessor Methods


    /**
     * <p>Retiurn the <code>Application</code> instance for the current
     * web application.</p>
     */
    protected Application getApplication() {

        return FacesContext.getCurrentInstance().getApplication();

    }


    /**
     * <p>Return a <code>Map</code> of the application scope attributes
     * for this web application.</p>
     */
    protected Map getApplicationMap() {

        return getExternalContext().getApplicationMap();

    }


    /**
     * <p>Return the <code>ExternalContext</code> instance for the
     * current request.</p>
     */
    protected ExternalContext getExternalContext() {

        return FacesContext.getCurrentInstance().getExternalContext();

    }


    /**
     * <p>Return the <code>FacesContext</code> instance for the
     * current request.</p>
     */
    protected FacesContext getFacesContext() {

        return FacesContext.getCurrentInstance();

    }


    /**
     * <p>Return the configured <code>Lifecycle</code> instance for
     * the current application.</p>
     */
    protected Lifecycle getLifecycle() {

        String lifecycleId =
          getExternalContext().getInitParameter("javax.faces.LIFECYCLE_ID");
        if (lifecycleId == null || lifecycleId.length() == 0) {
            lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
        }
        LifecycleFactory lifecycleFactory = (LifecycleFactory)
          FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        return lifecycleFactory.getLifecycle(lifecycleId);

    }


    /**
     * <p>Return a <code>Map</code> of the request headers included in this
     * request.  If there is more than one header for a particular header
     * name, only the first value is included in this map.</p>
     */
    protected Map getRequestHeaderMap() {

        return getExternalContext().getRequestHeaderMap();

    }


    /**
     * <p>Return a <code>Map</code> of the request scope attributes
     * for this request.</p>
     */
    protected Map getRequestMap() {

        return getExternalContext().getRequestMap();

    }


    /**
     * <p>Return a <code>Map</code> of the request parameters included in this
     * request.  If there is more than one value for a particular parameter
     * name, only the first value is included in this map.</p>
     */
    protected Map getRequestParameterMap() {

        return getExternalContext().getRequestParameterMap();

    }


    /**
     * <p>Return a <code>Map</code> of the session scope attributes
     * for the current user.</p>
     */
    protected Map getSessionMap() {

        return getExternalContext().getSessionMap();

    }


    // --------------------------------------------------- Bean Accessor Methods


    /**
     * <p>Return the named bean from request, session, or application scope.
     * If this is a managed bean, it might also get created as a side effect.
     * Return <code>null</code> if no such bean can be found or created.</p>
     *
     * @param name Name of the desired bean
     */
    protected Object getBean(String name) {

        FacesContext context = getFacesContext();
        return context.getApplication().getVariableResolver().
          resolveVariable(context, name);

    }


    /**
     * <p>Replace the value of any attribute stored in request scope,
     * session scope, or application scope, under the specified name.
     * If there is no such value, store this value as a new request
     * scope attribute under the specified name.</p>
     *
     * @param name Name of the attribute to replace or create
     * @param value Value to be stored
     */
    protected void setBean(String name, Object value) {

        setValue("#{" + name + "}", value);

    }


    // -------------------------------------------- Expression Evauation Methods


    /**
     * <p>Evaluate the specified value binding expression and return
     * the value it points at.</p>
     *
     * @param expr Value binding expression to be evaluated
     */
    protected Object getValue(String expr) {

        ValueBinding vb = getApplication().createValueBinding(expr);
        return vb.getValue(getFacesContext());

    }


    /**
     * <p>Evaluate the specified value binding expression, and replace
     * the value it points at.</p>
     *
     * @param expr Value binding expression pointing at a writeable property
     * @param value New value to store there
     */
    protected void setValue(String expr, Object value) {

        ValueBinding vb = getApplication().createValueBinding(expr);
        vb.setValue(getFacesContext(), value);

    }


    // ----------------------------------------------- Save/Restore Data Methods


    /**
     * <p>The attribute name under which saved data will be stored on the
     * view root component.</p>
     */
    private static final String DATA_KEY = "org.apache.shale.DATA";



    /**
     * <p>Return the data object stored (typically when the component tree
     * was previously rendered) under the specified key, if any; otherwise,
     * return <code>null</code>.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE:</strong> Data objects will become
     * available only after the <em>Restore View</em> phase of the request
     * processing lifecycle has been completed.  A common place to reinitialize
     * state information, then, would be in the <code>preprocess()</code>
     * event handler of a {@link ViewController} backing bean.</p>
     *
     * @param key Key under which to retrieve the requested data
     */
    public Object retrieveData(String key) {

        FacesContext context = getFacesContext();
        if (context == null) {
            return null;
        }
        UIViewRoot view = context.getViewRoot();
        if (view == null) {
            return null;
        }
        Map map = (Map) view.getAttributes().get(DATA_KEY);
        if (map != null) {
            return map.get(key);
        } else {
            return null;
        }

    }


    /**
     * <p>Save the specified data object (which <strong>MUST</strong> be
     * <code>Serializable</code>) under the specified key, such that it can
     * be retrieved (via <code>getData()</code>) on a s subsequent request
     * immediately after the component tree has been restored.</p>
     *
     * <p><strong>IMPLEMENTATION NOTE:</strong> In order to successfully save
     * data objects, this method must be called before the <em>Render Response</em>
     * phase of the request processing lifecycle is executed.  A common scenario
     * is to save state information in the <code>prerender()</code> event handler
     * of a {@link ViewController} backing bean.</p>
     *
     * @param key Key under which to store the requested data
     * @param data Data object to be stored
     */
    public void saveData(String key, Object data) {

        Map map = (Map)
           getFacesContext().getViewRoot().getAttributes().get(DATA_KEY);
        if (map == null) {
            map = new HashMap();
            getFacesContext().getViewRoot().getAttributes().put(DATA_KEY, map);
        }
        map.put(key, data);

    }


    // -------------------------------------------------- Erase Submitted Values


    /**
     * <p>Erase submitted values on all <code>EditableValueHolder</code>
     * components in the current view.  This method should be called if
     * you have input components bound to data values, submit the form,
     * and then arbitrarily change the data that the binding points at
     * without going through the <em>Update Model Values</em> phase of
     * the request processing lifecycle.</p>
     */
    protected void erase() {

        UIComponent view = getFacesContext().getViewRoot();
        if (view != null) {
            erase(view);
        }

    }


    /**
     * <p>Private helper method for <code>erase()</code> that recursively
     * descends the component tree and performs the required processing.</p>
     *
     * @param component The component to be erased
     */
    private void erase(UIComponent component) {

        // Erase the component itself (if needed)
        if (component instanceof EditableValueHolder) {
            ((EditableValueHolder) component).setSubmittedValue(null);
        }
        // Process the facets and children of this component
        Iterator kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            erase((UIComponent) kids.next());
        }

    }


    // ----------------------------------------------- Request Parameter Methods


    /**
     * <p>Return the first (or only) value for the specified request parameter.
     * If no such request parameter exists for the current requset, return
     * <code>null</code> instead.</p>
     *
     * @param name Name of the request parameter to look for
     */
    public String getRequestParameter(String name) {

        return (String) getExternalContext().getRequestParameterMap().get(name);

    }


    /**
     * <p>Return an array of all the values for the specified request parameter,
     * if there are any.  If no such request parameter exists for the current
     * request, return <code>null</code> instead.</p>
     *
     * @param name Name of the request parameter to look for
     */
    public String[] getRequestParameterValues(String name) {

        return (String[])
          getExternalContext().getRequestParameterValuesMap().get(name);

    }


    // ------------------------------------------------------------- Log Methods


    /**
     * <p>Log the specified message to the server's log file.</p>
     *
     * @param message Message to be logged
     */
    protected void log(String message) {

        FacesContext context = getFacesContext();
        ExternalContext econtext = null;
        if (context != null) {
            econtext = context.getExternalContext();
        }
        if (econtext != null) {
            econtext.log(message);
        } else {
            System.out.println(message);
        }

    }


    /**
     * <p>Log the specified message and exception to the server's log file.</p>
     *
     * @param message Message to be logged
     * @param throwable Exception to be logged
     */
    protected void log(String message, Throwable throwable) {

        FacesContext context = getFacesContext();
        ExternalContext econtext = null;
        if (context != null) {
            econtext = context.getExternalContext();
        }
        if (econtext != null) {
            econtext.log(message, throwable);
        } else {
            System.out.println(message);
            throwable.printStackTrace(System.out);
        }

    }


    // --------------------------------------------------------- Message Methods


    /**
     * <p>Enqueue a global <code>FacesMessage</code> (not associated with any
     * particular component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_INFO</code>.</p>
     *
     * @param summary Summary text for this message
     */
    protected void info(String summary) {

        getFacesContext().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));

    }


    /**
     * <p>Enqueue a <code>FacesMessage</code> (associated with the
     * specified component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_INFO</code>.</p>
     *
     * @param component Component with which this message is associated
     * @param summary Summary text for this message
     */
    protected void info(UIComponent component, String summary) {

        FacesContext context = getFacesContext();
        context.addMessage(component.getClientId(context),
           new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null));

    }


    /**
     * <p>Enqueue a global <code>FacesMessage</code> (not associated with any
     * particular component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_WARN</code>.</p>
     *
     * @param summary Summary text for this message
     */
    protected void warn(String summary) {

        getFacesContext().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));

    }


    /**
     * <p>Enqueue a <code>FacesMessage</code> (associated with the
     * specified component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_WARN</code>.</p>
     *
     * @param component Component with which this message is associated
     * @param summary Summary text for this message
     */
    protected void warn(UIComponent component, String summary) {

        FacesContext context = getFacesContext();
        context.addMessage(component.getClientId(context),
           new FacesMessage(FacesMessage.SEVERITY_WARN, summary, null));

    }


    /**
     * <p>Enqueue a global <code>FacesMessage</code> (not associated with any
     * particular component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_ERROR</code>.</p>
     *
     * @param summary Summary text for this message
     */
    protected void error(String summary) {

        getFacesContext().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));

    }


    /**
     * <p>Enqueue a <code>FacesMessage</code> (associated with the
     * specified component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_ERROR</code>.</p>
     *
     * @param component Component with which this message is associated
     * @param summary Summary text for this message
     */
    protected void error(UIComponent component, String summary) {

        FacesContext context = getFacesContext();
        context.addMessage(component.getClientId(context),
           new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null));

    }


    /**
     * <p>Enqueue a global <code>FacesMessage</code> (not associated with any
     * particular component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_FATAL</code>.</p>
     *
     * @param summary Summary text for this message
     */
    protected void fatal(String summary) {

        getFacesContext().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null));

    }


    /**
     * <p>Enqueue a <code>FacesMessage</code> (associated with the
     * specified component) containing the specified summary text and a
     * message severity level of <code>FacesMessage.SEVERITY_FATAL</code>.</p>
     *
     * @param component Component with which this message is associated
     * @param summary Summary text for this message
     */
    protected void fatal(UIComponent component, String summary) {

        FacesContext context = getFacesContext();
        context.addMessage(component.getClientId(context),
           new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, null));

    }


}
