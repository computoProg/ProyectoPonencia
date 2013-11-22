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
 * $Id: ClayViewHandlerCommand.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.faces;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.shale.clay.config.Globals;

/**
 * <p>This is a Shale "preprocess" command that should be registered
 * with the shale chains catalog.  It is only need for implementing
 * full {@link org.apache.shale.clay.component.Clay} html or XML views with myfaces.
 * The myfaces implementation pays strict attention to the javax.faces.DEFAULT_SUFFIX setting,
 * only allowing viewId's with this suffix.  This command will place an indicator in request scope
 * before the request is processed by the faces servlet.  The {@link ClayViewHandler}
 * will use the indicator to determine that the view should be handled by the
 * {@link ClayViewHandler}.
 * </p>
 */
public class ClayViewHandlerCommand implements Command {

    /**
     * <p>Holds the suffixes used to identify a Clay full HTML or XML view template.</p>
     */
    private String[] suffixes = null;

    /**
     * <p>Looks at the request uri to determine if the target page
     * is a clay template.  If the request's pathInfo matches the
     * clay template suffixes, a flag is added to the request attributes.
     * This is need for the MyFaces JSF implementation.</p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chains is final
     * @exception Exception throws back up the calling stack
     */
    public boolean execute(Context context) throws Exception {

        ServletRequest request = (ServletRequest) context.get("request");
        int index = indexOfClayTemplateSuffix(context);
        if (index != -1) {
           request.setAttribute(Globals.CLAY_FULL_VIEW_SUFFIX, suffixes[index]);
        }

        return false;
    }


    /**
     * <p>Checks the <code>request.uri</code> to determine if it's suffix matches one of
     * the <code>suffixes</code>.  If a match is found, the index into the <code>suffixes</code>
     * array is returned.  A value of -1 is returned if a match is not found.</p>
     *
     * @param context commons chains
     * @return index into <code>suffixes</code>
     */
    protected int indexOfClayTemplateSuffix(Context context) {
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        ServletContext servletContext = (ServletContext) context.get("context");
        if (suffixes == null) {
            suffixes = new String[2];

            suffixes[0] = servletContext.getInitParameter(
                    Globals.CLAY_HTML_TEMPLATE_SUFFIX);
            if (suffixes[0] == null) {
                suffixes[0] = Globals.CLAY_DEFAULT_HTML_TEMPLATE_SUFFIX;
            }

            suffixes[1] = servletContext.getInitParameter(
                    Globals.CLAY_XML_TEMPLATE_SUFFIX);
            if (suffixes[1] == null) {
                suffixes[1] = Globals.CLAY_DEFAULT_XML_TEMPLATE_SUFFIX;
            }

        }
        String uri = request.getRequestURI();

        if (uri != null) {
            //look at the path
            for (int i = 0; i < suffixes.length; i++) {
                if (uri.endsWith(suffixes[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

}
