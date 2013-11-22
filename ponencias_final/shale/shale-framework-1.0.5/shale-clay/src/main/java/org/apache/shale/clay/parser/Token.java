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
 * $Id: Token.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.parser;

/**
 * <p>A <code>Token</code> identifies an offset range
 * within the document.  Nodes and Attributes implementing
 * this interface are identified by the {@link NodeTokenizer} and
 * {@link AttributeTokenizer}.
 * </p>
 */
public interface Token {

    /**
     * <p>Beginning offset of the token within the document.</p>
     *
     * @return beginning offset
     */
    int getBeginOffset();

    /**
     * <p>Ending offset of the token within the document.</p>
     *
     * @return ending offset
     */
    int getEndOffset();

    /**
     * <p>The complete identifier within the document defined
     * by the range between the <code>beginOffset</code> and
     * <code>endOffset</code>.
     * </p>
     *
     * @return text between offset ranges
     */
    String getRawText();

    /**
     * <p>Returns a reference to the complete document.</p>
     *
     * @return parsed document
     */
    StringBuffer getDocument();

    /**
     * <p>Returns the line number in the document that the node starts.</p>
     *
     * @return line number the token is find on
     */
    int getLineNumber();

    /**
     * <p>Returns the line begining offset in the document that the node starts.</p>
     *
     * @return line offset the token is found on
     */
    int getLineBeginOffset();

}
