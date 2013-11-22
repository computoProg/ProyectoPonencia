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

package org.apache.shale.faces;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>Shale-specific VariableResolver for evaluating JavaServer Faces
 * value binding and method binding expressions.  The following special
 * variable names are recognized, and evaluated as indicated:</p>
 * <ul>
 * <li><strong>jndi</strong> - Returns the JNDI context at name
 *     <code>java:comp/env</code> (relative to the initial context
 *     supplied by the container.</li>
 * </ul>
 * <p>All other evaluations are delegated to the previous implementation
 * that was passed to our constructor.</p>
 *
 * <p>Since 1.0.1, if the optional <code>shale-tiger.jar</code> file
 * (containing the Shale Tiger Extensions) is available to this web
 * application, the extra variable resolver implementation found there
 * will be interposed between this instance and the previous
 * implementation instance passed to our constructor.</p>
 *
 * $Id: ShaleVariableResolver.java 464373 2006-10-16 04:21:54Z rahul $
 */
public class ShaleVariableResolver extends VariableResolver {


    // ------------------------------------------------------------- Constructor


    /**
     * <p>Construct a new {@link ShaleVariableResolver} instance.</p>
     *
     * @param original Original resolver to delegate to.
     */
    public ShaleVariableResolver(VariableResolver original) {

        // Determine if our optional resolver is present
        VariableResolver delegate = null;
        try {
            // Attempt to load the Shale Tiger Extensions class
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class clazz = cl.loadClass(TIGER_DELEGATE);
            // Instantiate a new instance, passing the original resolver
            Constructor constructor = clazz.getConstructor(VARIABLE_RESOLVER_ARGS);
            delegate =
              (VariableResolver) constructor.newInstance(new Object[] { original });
        } catch (ClassNotFoundException e) {
            delegate = null;
        } catch (RuntimeException e) {
            throw e;
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new FacesException(cause);
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }

        if (delegate != null) {
            // Interpose our delegate into the chain
            this.original = delegate;
        } else {
            // Use the provided instance directly
            this.original = original;
        }

    }


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The original <code>VariableResolver</code> passed to our constructor.</p>
     */
    private VariableResolver original = null;


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>Variable name to be resoved to our JNDI environment context.</p>
     */
    private static final String JNDI_VARIABLE_NAME = "jndi";


    /**
     * <p>Fully qualified class name of the variable resolver implementation
     * in the Shale Tiger Extensions library.</p>
     */
    private static final String TIGER_DELEGATE =
            "org.apache.shale.tiger.faces.VariableResolverImpl";


    /**
     * <p>Constructor signature for a VariableResolver that takes a
     * VariableResolver as a parameter.</p>
     */
    private static final Class VARIABLE_RESOLVER_ARGS[] =
    { VariableResolver.class };


    // ------------------------------------------------ VariableResolver Methods


    /**
     * <p>Resolve variable names known to this resolver; otherwise, delegate to
     * the original resolver passed to our constructor.</p>
     *
     * @param context FacesContext for the current request
     * @param name Variable name to be resolved
     *
     * @exception EvaluationException if the JNDI naming context
     *  throws a naming exception
     */
    public Object resolveVariable(FacesContext context, String name)
      throws EvaluationException {

        if (JNDI_VARIABLE_NAME.equals(name)) {
            try {
                InitialContext ic = new InitialContext();
                return (Context) ic.lookup("java:comp/env");
            } catch (NamingException e) {
                throw new EvaluationException(e);
            }
        } else {
            return original.resolveVariable(context, name);
        }

    }


}
