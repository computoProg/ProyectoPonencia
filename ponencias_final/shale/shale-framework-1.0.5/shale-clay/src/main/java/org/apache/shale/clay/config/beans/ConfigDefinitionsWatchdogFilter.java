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
 * $Id: ConfigDefinitionsWatchdogFilter.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.config.beans;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <p>This is the timing mechanism for looking for modified
 * Clay templates and configuration files.  This is a preprocess
 * filter chains command that should only be used in the
 * development environment.</p>
 */
public class ConfigDefinitionsWatchdogFilter implements Command {

    /**
     * <p>Comma-delimited regular expression patterns to include remote host
     * names that match.</p>
     */
    private String includes = null;

    /**
     * <p>Array of regular expression patterns for the includes list.</p>
     */
    private Pattern[] includesPatterns = new Pattern[0];

    /**
     * @return an array of regular expression patterns for the includes list.
     */
    private Pattern[] getIncludesPatterns() { return includesPatterns; }

    /**
     * @return Return the comma-delimited regular expresson patterns to include
     * remote host names that match, if any; otherwise, return
     * <code>null</code>.
     */
    public String getIncludes() { return this.includes; }


    /**
     * <p>Set the comma-delimited regular expression patterns to include
     * remote host names that match, if any; or <code>null</code> for no
     * restrictions.</p>
     *
     * @param includes New include pattern(s)
     */
    public void setIncludes(String includes) {
        this.includes = includes;
        this.includesPatterns = precompile(includes);
    }

    /**
     * <p>Log instance for this class.</p>
     */
    private static Log log = LogFactory.getLog(ConfigDefinitionsWatchdogFilter.class);


    /**
     * <p>Return the servlet path (if any) concatenated with the path info
     * (if any) for this request.</p>
     *
     * @param context <code>Context</code> for the current request
     * @return servletPath and pathInfo
     */
    protected String value(Context context) {

        HttpServletRequest request = (HttpServletRequest) context.get("request");
        String servletPath = request.getServletPath();
        if (servletPath == null) {
            servletPath = "";
        }
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }
        return servletPath + pathInfo;

    }


    /**
     * <p>Perform the matching algorithm against the value
     * returned by the <code>value()</code> method.  If the
     * value <code>matches()</code> the <code>includes</code>
     * pattern list, the {@link org.apache.shale.clay.component.Clay}
     * configuration files are checked for changes and reloaded.</p>
     *
     * @param context <code>ShaleWebContext</code> for this request
     * @return <code>true</code> if the chain is done
     * @exception Exception thrown back to the calling command
     */
    public boolean execute(Context context) throws Exception {

        // Acquire the value to be tested
        String value = value(context);
        if (log.isDebugEnabled()) {
            log.debug("execute(" + value + ")");
        }

        // Check for a match on the included list
        if (value != null && matches(value, getIncludesPatterns(), true)) {
            if (log.isTraceEnabled()) {
                log.trace("  accept(include)");
            }
            accept(context);
        }

        return false;

    }


    /**
     * <p>Trigger reloading of the {@link org.apache.shale.clay.component.Clay}'s
     * xml configuration files if the
     * <code>org.apache.shale.clay.AUTO_RELOAD_CONFIG_FILES</code> init
     * parameter is set to <code>true</code> in the web.xml.  The HTML templates
     * are re-cashed on-demand due to their atomicity.  The XML configuration
     * files are shared by all {@link org.apache.shale.clay.component.Clay}
     * view composition mechanisms so all files must be reloaded if a change
     * is made.</p>
     *
     * @param context <code>Context</code> for the current request
     * @exception Exception thrown back to the caller
     */
    protected void accept(Context context) throws Exception {
        ConfigBeanFactory.refresh();
    }

    /**
     * <p>Parse the specified string of comma-delimited (and optionally quoted,
     * if an embedded comma is required) regular expressions into an array
     * of precompiled <code>Pattern</code> instances that represent these
     * expressons.</p>
     *
     * @param expr Comma-delimited regular expressions
     * @return Recognized regular expressions
     */
     private Pattern[] precompile(String expr) {

        if (expr == null) {
            return new Pattern[0];
        }

        // Set up to parse the specified expression
        StreamTokenizer st =
          new StreamTokenizer(new StringReader(expr));
        st.eolIsSignificant(false);
        st.lowerCaseMode(false);
        st.slashSlashComments(false);
        st.slashStarComments(false);
        st.wordChars(0x00, 0xff);
        st.quoteChar('\'');
        st.quoteChar('"');
        st.whitespaceChars(0, ' ');
        st.whitespaceChars(',', ',');
        List list = new ArrayList();
        int type = 0;

        // Parse each included expression
        while (true) {
            try {
                type = st.nextToken();
            } catch (IOException e) {
                ; // Can not happen
            }
            if (type == StreamTokenizer.TT_EOF) {
                break;
            } else if (type == StreamTokenizer.TT_NUMBER) {
                list.add(Pattern.compile("" + st.nval));
            } else if (type == StreamTokenizer.TT_WORD) {
                list.add(Pattern.compile(st.sval.trim()));
            } else {
                throw new IllegalArgumentException(expr);
            }
        }

        // Return the precompiled patterns as an array
        return (Pattern[]) list.toArray(new Pattern[list.size()]);

    }

     /**
      * <p>Match the specified expression against the specified precompiled
      * patterns.  If there are no patterns, return the specified unrestricted
      * return value; otherwise, return <code>true</code> if the expression
      * matches one of the patterns, or <code>false</code> otherwise.</p>
      *
      * @param expr Expression to be tested
      * @param patterns Array of <code>Pattern</code> to be tested against
      * @param unrestricted Result to be returned if there are no matches
      * @return <code>true</code> if a match is found
      */
     protected boolean matches(String expr, Pattern[] patterns,
                             boolean unrestricted) {

         // Check for the unrestricted case
         if ((patterns == null) || (patterns.length == 0)) {
             return unrestricted;
         }

         // Compare each pattern in turn for a match
         for (int i = 0; i < patterns.length; i++) {
             if (patterns[i].matcher(expr).matches()) {
                 return true;
             }
         }

         // No match found, so return false
         return false;

     }

}
