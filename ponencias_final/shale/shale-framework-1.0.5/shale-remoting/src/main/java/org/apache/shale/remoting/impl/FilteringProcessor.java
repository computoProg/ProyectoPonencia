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

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.shale.remoting.Processor;

/**
 * <p>Abstract base class for {@link Processor} implementations that filter
 * requests based on matching the resource identifier against a set of
 * <code>includes</code> and <code>excludes</code> regular expressions.</p>
 *
 * @since 1.0.4
 */
public abstract class FilteringProcessor implements Processor {
    

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>Comma-delimited regular expression patterns to exclude remote host
     * names that match.</p>
     */
    private String excludes = null;


    /**
     * <p>Array of regular expression patterns for the excludes list.</p>
     */
    private String excludesPatterns[] = new String[0];


    /**
     * <p>Comma-delimited regular expression patterns to include remote host
     * names that match.</p>
     */
    private String includes = null;


    /**
     * <p>Array of regular expression patterns for the includes list.</p>
     */
    private String includesPatterns[] = new String[0];


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the comma-delimited regular expresson patterns to exclude
     * remote host names that match, if any; otherwise, return
     * <code>null</code>.</p>
     */
    public String getExcludes() { return this.excludes; }


    /**
     * <p>Set the comma-delimited regular expression patterns to exclude
     * remote host names that match, if any; or <code>null</code> for no
     * restrictions.</p>
     *
     * @param excludes New exclude pattern(s)
     */
    public void setExcludes(String excludes) {
        this.excludes = excludes;
        this.excludesPatterns = precompile(excludes);
    }


    /**
     * <p>Return the comma-delimited regular expresson patterns to include
     * remote host names that match, if any; otherwise, return
     * <code>null</code>.</p>
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


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Return <code>true</code> if we should accept a request for the
     * specified resource identifier, based upon our configured includes
     * and excludes patterns (if any).</p>
     *
     * @param resourceId Resource identifier to validate
     */
    protected boolean accept(String resourceId) {

        // Check for a match on the excluded list
        if (matches(resourceId, excludesPatterns, false)) {
            return false;
        }

        // Check for a match on the included list
        if (matches(resourceId, includesPatterns, true)) {
            return true;
        }

        // If there was at least one include pattern,
        // unconditionally reject this request
        if ((includesPatterns != null) && (includesPatterns.length > 0)) {
            return false;
        }

        // Unconditionally accept this request
        return true;

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Match the specified expression against the specified precompiled
     * patterns.  If there are no patterns, return the specified unrestricted
     * return value; otherwise, return <code>true</code> if the expression
     * matches one of the patterns, or <code>false</code> otherwise.</p>
     *
     * @param expr Expression to be tested
     * @param patterns Array of <code>Pattern</code> to be tested against
     * @param unrestricted Result to be returned if there are no matches
     */
    private boolean matches(String expr, String patterns[],
                            boolean unrestricted) {

        // Check for the unrestricted case
        if ((patterns == null) || (patterns.length == 0)) {
            return unrestricted;
        }

        // Compare each pattern in turn for a match
        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i].startsWith("*")) {
                if (expr.endsWith(patterns[i].substring(1))) {
                    return true;
                }
            } else if (patterns[i].endsWith("*")) {
                if (expr.startsWith(patterns[i].substring(0, patterns[i].length() - 1))) {
                    return true;
                }
            } else {
                if (patterns[i].equals(expr)) {
                    return true;
                }
            }
        }

        // No match found, so return false
        return false;

    }


    /**
     * <p>Parse the specified string of comma-delimited URL pattern
     * matching expressions into an array of patterns that can be processed
     * at runtime more quickly.  Valid patterns are the same as those
     * supported for matching a request URI to a Processor instance:</p>
     * <ul>
     * <li>Must not be null or zero-length string</li>
     * <li>EITHER must start with "/" and end with "/*"</li>
     * <li>OR must start with "*." and not have any other period</li>
     * </ul>
     *
     * @param expr Comma-delimited URL pattern matching expressions
     *
     * @exception IllegalArgumentException if an invalid pattern is encountered
     */
     private String[] precompile(String expr) {

        if (expr == null) {
            return new String[0];
        }

        // Set up to parse the specified expression
        String pattern = null;
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

        // Parse and validate each included pattern
        while (true) {

            // Parse the next pattern
            try {
                type = st.nextToken();
            } catch (IOException e) {
                ; // Can not happen
            }
            if (type == StreamTokenizer.TT_EOF) {
                break;
            } else if (type == StreamTokenizer.TT_NUMBER) {
                pattern = "" + st.nval;
            } else if (type == StreamTokenizer.TT_WORD) {
                pattern = st.sval.trim();
            } else {
                throw new IllegalArgumentException(expr);
            }

            // Validate this pattern
            if (pattern.length() < 1) {
                throw new IllegalArgumentException(pattern);
            }
            if (pattern.startsWith("/")) {
                if (!pattern.endsWith("/*")) {
                    throw new IllegalArgumentException(pattern);
                }
            } else if (pattern.startsWith("*.")) {
                if (pattern.substring(2).indexOf('.') > 0) {
                    throw new IllegalArgumentException(pattern);
                }
            } else {
                throw new IllegalArgumentException(pattern);
            }

            // Add this pattern to our list
            list.add(pattern);

        }

        // Return the precompiled patterns as an array
        return (String[]) list.toArray(new String[list.size()]);

    }


}
