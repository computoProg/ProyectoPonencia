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

package org.apache.shale.view.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.shale.view.ViewControllerMapper;

/**
 * <p>{@link DefaultViewControllerMapper} is a default implementation of {@link ViewControllerMapper}.  The following
 * algorithm is implemented:</p>
 * <ul>
 * <li>Strip any leading slash ("/") character.</li>
 * <li>Strip any traling extension (".xxx"), as long as it occurs
 *     after any remaining slash ("/") character.</li>
 * <li>Convert each instance of a slash ("/")
 *     character into a dollar sign ("$") character.</li>
 * <li>If the resulting name matches one of the reserved names recognized
 *     by the default <code>VariableResolver</code>, prefix it with an
 *     underscore character ("_"), to avoid problems loading managed beans.</li>
 * <li>If the resulting name starts with a digit character, prefix it with
 *     an underscore character("_"), to avoid problems evaluating value
 *     binding expressions using it (because this would be treated as a
 *     variable name starting with a digit, and that is not allowed by the
 *     syntax of expression evaluation).</li>
 * </ul>
 *
 * <p>Examples of correct managed bean names for typical JSF view identifiers,
 * when this mapper is used, would include:</p>
 * <ul>
 * <li>For a view identifier <code>/mainmenu.jsp</code>, your managed bean
 *     name <em>must</em> be <code>mainmenu</code> (leading slash and
 *     trailing extension were stripped).</li>
 * <li>For a view identifier <code>/customer/details.jsp</code>, your managed
 *     bean name <em>must</em> be <code>customer$details</code> (intermediate
 *     slash character also converted).</li>
 * <li>For a view identifier <code>/header.jsp</code>, your managed bean
 *     name <em>must</em> be <code>_header</code> ("header" is a magic JSF
 *     variable returning a Map of HTTP headers for the current request, so
 *     you cannot use this name for your own managed beans).</li>
 * </ul>
 *
 *
 * <p>Since the managed bean names also need to be valid variable names in
 * the expression language, this mapper implementation imposes certain
 * restrictions on the view identifiers. View identifiers must not contain
 * characters which have reserved meanings in the expression language, such
 * as '-' (minus) or '+' (plus). A best practice while using this mapper
 * is to ensure view identifiers use letters of the English alphabet
 * in upper or lower case, digits from 0 to 9, '$' (dollar signs) and '_'
 * (underscores) only.</p>
 *
 * $Id: DefaultViewControllerMapper.java 464373 2006-10-16 04:21:54Z rahul $
 */

public class DefaultViewControllerMapper implements ViewControllerMapper {


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Reserved variable names.</p>
     */
    private static Set reserved = new HashSet();

    static {
        reserved.add("applicationScope");
        reserved.add("cookie");
        reserved.add("facesContext");
        reserved.add("header");
        reserved.add("headerValues");
        reserved.add("initParam");
        reserved.add("param");
        reserved.add("paramValues");
        reserved.add("requestScope");
        reserved.add("sessionScope");
        reserved.add("view");
    }


    // ---------------------------------------------------------- Public Methods


    /** {@inheritDoc} */
    public String mapViewId(String viewId) {

        if (viewId == null) {
            return null;
        }
        if (viewId.startsWith("/")) {
            viewId = viewId.substring(1);
        }
        int slash = viewId.lastIndexOf("/");
        int period = viewId.lastIndexOf(".");
        if ((period >= 0) && (period > slash)) {
            viewId = viewId.substring(0, period);
        }
        viewId = viewId.replace('/', '$');
        if (reserved.contains(viewId)) {
            return "_" + viewId;
        } else if ((viewId.length() > 0) && Character.isDigit(viewId.charAt(0))) {
            return "_" + viewId;
        } else {
            return viewId;
        }

    }


}
