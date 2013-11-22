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

package org.apache.shale.view.faces;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.view.AbstractApplicationBean;
import org.apache.shale.view.AbstractRequestBean;
import org.apache.shale.view.AbstractSessionBean;
import org.apache.shale.view.Constants;
import org.apache.shale.view.ExceptionHandler;
import org.apache.shale.view.ViewController;


/**
 * <p><strong>LifecycleListener</strong> implements the lifecycle startup
 * and shutdown calls (<code>init()</code> and <code>destroy()</code>) for
 * subclasses of {@link AbstractApplicationBean}, {@link AbstractRequestBean},
 * and {@link AbstractSessionBean}.</p>
 *
 * <p>It must be registered with the servlet container as a listener,
 * through an entry in either the <code>/WEB-INF/web.xml</code> resource
 * or a tag library descriptor included in the web application.</p>
 *
 * $Id: LifecycleListener.java 489966 2006-12-24 01:43:42Z craigmcc $
 *
 * @since 1.0.3
 */
public class LifecycleListener
    implements ServletContextAttributeListener,
               ServletContextListener,
               HttpSessionActivationListener,
               HttpSessionAttributeListener,
               HttpSessionListener,
               ServletRequestAttributeListener,
               ServletRequestListener {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Create a new lifecycle listener.</p>
     */
    public LifecycleListener() {
        if (log.isInfoEnabled()) {
            log.info("Initializing org.apache.shale.view.faces.LifecycleListener");
        }
    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private static final Log log = LogFactory.getLog(LifecycleListener.class);


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The fully qualified class name of the <em>Tiger Extensions</em>
     * verison of this listener class.</p>
     */
    private static final String TIGER_LISTENER =
            "org.apache.shale.tiger.view.faces.LifecycleListener2";


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <em>Tiger Extensions</em> implementation of this listener, if
     * such a class exists in the application classpath.  This value is lazily
     * instantiated.</p>
     */
    private LifecycleListener tiger = null;


    /**
     * <p>Flag indicating whether the <code>tiger</code> value has been calculated
     * already.</p>
     */
    private boolean tigerInitialized = false;


    // ------------------------------------------ ServletContextListener Methods


    /**
     * <p>Respond to a context created event.  No special processing
     * is required.</p>
     *
     * @param event Event to be processed
     */
    public void contextInitialized(ServletContextEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.contextInitialized(event);
        }

    }


    /**
     * <p>Respond to a context destroyed event.  Causes any application
     * scope attribute that implements {@link AbstractApplicationBean}
     * to be removed, triggering an <code>attributeRemoved()</code> event.</p>
     *
     * @param event Event to be processed
     */
    public void contextDestroyed(ServletContextEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.contextDestroyed(event);
        }

        // Remove any AbstractApplicationBean attributes, which will
        // trigger an attributeRemoved event
        List list = new ArrayList();
        Enumeration names = event.getServletContext().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            list.add(name);
        }
        Iterator keys = list.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            event.getServletContext().removeAttribute(key);
        }

    }


    // --------------------------------- ServletContextAttributeListener Methods


    /**
     * <p>Respond to an application scope attribute being added.  If the
     * value is an {@link AbstractApplicationBean}, call its
     * <code>init()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeAdded(ServletContextAttributeEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeAdded(event);
            return; // Tiger logic replaces our own
        }

        // If the new value is an AbstractApplicationBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireApplicationInit(value);
        }

    }


    /**
     * <p>Respond to an application scope attribute being replaced.
     * If the old value was an {@link AbstractApplicationBean}, call
     * its <code>destroy()</code> method.  If the new value is an
     * {@link AbstractApplicationBean}, call its <code>init()</code>
     * method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeReplaced(ServletContextAttributeEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeReplaced(event);
            return; // Tiger logic replaces our own
        }

        // If the old value is an AbstractApplicationBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireApplicationDestroy(value);
        }

        // If the new value is an AbstractApplicationBean, notify it
        value = event.getServletContext().getAttribute(event.getName());
        if (value != null) {
            fireApplicationInit(value);
        }

    }


    /**
     * <p>Respond to an application scope attribute being removed.
     * If the old value was an {@link AbstractApplicationBean}, call
     * its <code>destroy()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeRemoved(ServletContextAttributeEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeRemoved(event);
            return; // Tiger logic replaces our own
        }

        // If the old value is an AbstractApplicationBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireApplicationDestroy(value);
        }

    }


    // --------------------------------------------- HttpSessionListener Methods


    /**
     * <p>Respond to a session created event.  No special processing
     * is required.</p>
     *
     * @param event Event to be processed
     */
    public void sessionCreated(HttpSessionEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.sessionCreated(event);
        }

    }


    /**
     * <p>Respond to a session destroyed event.  Causes any session
     * scope attribute that implements {@link AbstractSessionBean}
     * to be removed, triggering an <code>attributeRemoved()</code> event.</p>
     *
     * @param event Event to be processed
     */
    public void sessionDestroyed(HttpSessionEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.sessionDestroyed(event);
        }

        // Remove any AbstractSessionBean attributes, which will
        // trigger an attributeRemoved event
        List list = new ArrayList();
        try {
            Enumeration names = event.getSession().getAttributeNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                list.add(name);
            }
            Iterator keys = list.iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                event.getSession().removeAttribute(key);
            }
        } catch (IllegalStateException e) {
            // If the session has already been invalidated, there is nothing
            // we can do.  In a Servlet 2.4 or later container, this should not
            // happen, because the event handler is supposed to be called before
            // invalidation occurs, rather than after.
            ;
        }

    }


    // ----------------------------------- HttpSessionActivationListener Methods


    /**
     * <p>Respond to a "session will passivate" event.  Notify all session
     * scope attributes that are {@link AbstractSessionBean}s.</p>
     *
     * @param event Event to be processed
     */
    public void sessionWillPassivate(HttpSessionEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.sessionWillPassivate(event);
            return; // Tiger logic replaces our own
        }

        // Notify any AbstractSessionBean attributes
        Enumeration names = event.getSession().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = event.getSession().getAttribute(name);
            if (value != null) {
                fireSessionPassivate(value);
            }
        }

    }


    /**
     * <p>Respond to a "session did activate" event.  Notify all session
     * scope attributes that are {@link AbstractSessionBean}s.</p>
     *
     * @param event Event to be processed
     */
    public void sessionDidActivate(HttpSessionEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.sessionDidActivate(event);
            return; // Tiger logic replaces our own
        }

        // Notify any AbstractSessionBean attributes
        Enumeration names = event.getSession().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = event.getSession().getAttribute(name);
            if (value != null) {
                fireSessionActivate(value);
            }
        }

    }


    // ------------------------------------ HttpSessionAttributeListener Methods


    /**
     * <p>Respond to a session scope attribute being added.  If the
     * value is an {@link AbstractSessionBean}, call its
     * <code>init()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeAdded(HttpSessionBindingEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeAdded(event);
            return; // Tiger logic replaces our own
        }

        // If the new value is an AbstractSessionBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireSessionInit(value);
        }

    }


    /**
     * <p>Respond to a session scope attribute being replaced.
     * If the old value was an {@link AbstractSessionBean}, call
     * its <code>destroy()</code> method.  If the new value is an
     * {@link AbstractSessionBean}, call its <code>init()</code>
     * method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeReplaced(event);
            return; // Tiger logic replaces our own
        }

        // If the old value is an AbstractSessionBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireSessionDestroy(value);
        }

        // If the new value is an AbstractSessionBean, notify it
        value = event.getSession().getAttribute(event.getName());
        if (value != null) {
            fireSessionInit(value);
        }

    }


    /**
     * <p>Respond to a session scope attribute being removed.
     * If the old value was an {@link AbstractSessionBean}, call
     * its <code>destroy()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeRemoved(event);
            return; // Tiger logic replaces our own
        }

        // If the old value is an AbstractSessionBean, notify it
        Object value = event.getValue();
        if (value != null) {
            fireSessionDestroy(value);
        }

    }


    // ------------------------------------------ ServletRequestListener Methods


    /**
     * <p>Respond to a request created event.  No special processing
     * is required.</p>
     *
     * @param event Event to be processed
     */
    public void requestInitialized(ServletRequestEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.requestInitialized(event);
        }

    }


    /**
     * <p>Respond to a request destroyed event.  Causes any request
     * scope attribute that implements {@link AbstractRequestBean}
     * or {@link AbstractFragmentBean} to be removed, triggering an
     * <code>attributeRemoved()</code> event.</p>
     *
     * @param event Event to be processed
     */
    public void requestDestroyed(ServletRequestEvent event) {

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.requestDestroyed(event);
            return;
        }

        // Remove any AbstractRequestBean or ViewController attributes,
        // which will trigger an attributeRemoved event
        List list = new ArrayList();
        ServletRequest request = event.getServletRequest();
        Enumeration names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = request.getAttribute(name);
            if ((value instanceof AbstractRequestBean) || (value instanceof ViewController)) {
                list.add(name);
            }
        }
        Iterator keys = list.iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            event.getServletRequest().removeAttribute(key);
        }

    }


    // --------------------------------- ServletRequestAttributeListener Methods


    /**
     * <p>Respond to a request scope attribute being added.  If the
     * value is an {@link AbstractRequestBean}, call its <code>init()</code> method.
     * </p>
     *
     * @param event Event to be processed
     */
    public void attributeAdded(ServletRequestAttributeEvent event) {

        if (log.isDebugEnabled()) {
            log.debug("ServletRequestAttributeAdded(" + event.getName()
              + "," + event.getValue() + ")");
        }

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeAdded(event);
            return; // Tiger logic replaces our own
        }

        Object value = event.getValue();
        if (value != null) {
            fireRequestInit(value);
        }

    }


    /**
     * <p>Respond to a request scope attribute being replaced.
     * If the old value was an {@link AbstractRequestBean},
     * call its <code>destroy()</code> method.  If the new value is an
     * {@link AbstractRequestBean}, call its <code>init()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeReplaced(ServletRequestAttributeEvent event) {

        if (log.isDebugEnabled()) {
            log.debug("ServletRequestAttributeReplaced(" + event.getName()
              + "," + event.getValue()
              + "," + event.getServletRequest().getAttribute(event.getName())
              + ")");
        }

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeReplaced(event);
            return; // Tiger logic replaces our own
        }

        Object value = event.getValue();
        if (value != null) {
            fireRequestDestroy(value);
        }

        value = event.getServletRequest().getAttribute(event.getName());
        if (value != null) {
            fireRequestInit(value);
        }

    }


    /**
     * <p>Respond to a request scope attribute being removed.
     * If the old value was an {@link AbstractRequestBean},
     * call its <code>destroy()</code> method.</p>
     *
     * @param event Event to be processed
     */
    public void attributeRemoved(ServletRequestAttributeEvent event) {

        if (log.isDebugEnabled()) {
            log.debug("ServletRequestAttributeRemoved(" + event.getName()
              + "," + event.getValue() + ")");
        }

        // Delegate to the Tiger Extensions instance if it exists
        LifecycleListener tiger = tiger();
        if (tiger != null) {
            tiger.attributeRemoved(event);
            return; // Tiger logic replaces our own
        }

        Object value = event.getValue();
        if (value != null) {
            fireRequestDestroy(value);
        }

    }


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Fire a destroy event on an @{link AbstractApplicationBean}.</p>
     *
     * @param bean {@link AbstractApplicationBean} to fire event on
     */
    protected void fireApplicationDestroy(Object bean) {

        try {
            if (bean instanceof AbstractApplicationBean) {
                ((AbstractApplicationBean) bean).destroy();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire an init event on an {@link AbstractApplicationBean}.</p>
     *
     * @param bean {@link AbstractApplicationBean} to fire event on
     */
    protected void fireApplicationInit(Object bean) {

        try {
            if (bean instanceof AbstractApplicationBean) {
                ((AbstractApplicationBean) bean).init();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire a destroy event on an @{link AbstractRequestBean}.</p>
     *
     * @param bean {@link AbstractRequestBean} to fire event on
     */
    protected void fireRequestDestroy(Object bean) {

        try {
            if (bean instanceof AbstractRequestBean) {
                ((AbstractRequestBean) bean).destroy();
            } else if (bean instanceof ViewController) {
                ((ViewController) bean).destroy();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire an init event on an {@link AbstractRequestBean}.</p>
     *
     * @param bean {@link AbstractRequestBean} to fire event on
     */
    protected void fireRequestInit(Object bean) {

        try {
            if (bean instanceof AbstractRequestBean) {
                ((AbstractRequestBean) bean).init();
            } else if (bean instanceof ViewController) {
                ((ViewController) bean).init();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire an activate event on an @{link AbstractSessionBean}.</p>
     *
     * @param bean {@link AbstractSessionBean} to fire event on
     */
    protected void fireSessionActivate(Object bean) {

        try {
            if (bean instanceof AbstractSessionBean) {
                ((AbstractSessionBean) bean).activate();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire a destroy event on an @{link AbstractSessionBean}.</p>
     *
     * @param bean {@link AbstractSessionBean} to fire event on
     */
    protected void fireSessionDestroy(Object bean) {

        try {
            if (bean instanceof AbstractSessionBean) {
                ((AbstractSessionBean) bean).destroy();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire an init event on an {@link AbstractSessionBean}.</p>
     *
     * @param bean {@link AbstractSessionBean} to fire event on
     */
    protected void fireSessionInit(Object bean) {

        try {
            if (bean instanceof AbstractSessionBean) {
                ((AbstractSessionBean) bean).init();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Fire an passivate event on an @{link AbstractSessionBean}.</p>
     *
     * @param bean {@link AbstractSessionBean} to fire event on
     */
    protected void fireSessionPassivate(Object bean) {

        try {
            if (bean instanceof AbstractSessionBean) {
                ((AbstractSessionBean) bean).passivate();
            }
        } catch (Exception e) {
            handleException(FacesContext.getCurrentInstance(), e);
        }

    }


    /**
     * <p>Handle the specified exception according to the strategy
     * defined by our current {@link ExceptionHandler}.</p>
     *
     * @param context FacesContext for the current request
     * @param exception Exception to be handled
     */
    protected void handleException(FacesContext context, Exception exception) {

        if (context == null) {
            exception.printStackTrace(System.out);
            return;
        }
        ExceptionHandler handler = (ExceptionHandler)
          context.getApplication().getVariableResolver().resolveVariable
                (context, Constants.EXCEPTION_HANDLER);
        handler.handleException(exception);

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <em>Tiger Extensions</em> implementation of this listener
     * if one exists; otherwise, return <code>null</code>.</p>
     */
    private LifecycleListener tiger() {

        // If we have already attempted to load the Tiger Extensions version
        // of this class, return the calculated result
        if (tigerInitialized) {
            return tiger;
        }

        // Attempt to load the Tiger Extensions version of this class, and
        // instantiate an appropriate instance
        try {
            Class clazz = this.getClass().getClassLoader().loadClass(TIGER_LISTENER);
            tiger = (LifecycleListener) clazz.newInstance();
        } catch (Exception e) {
            ; // Swallow any class not found or instantiation exception
        }

        // Return the calculated result
        tigerInitialized = true;
        return tiger;

    }


}
