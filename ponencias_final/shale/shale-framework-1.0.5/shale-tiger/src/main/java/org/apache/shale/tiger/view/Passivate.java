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

package org.apache.shale.tiger.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Method-level annotation indicating that the decorated method should
 * have the semantics of
 * <code>org.apache.shale.view.AbstractSessionBean.passivate()</code>,
 * even if it is named differently.</p>
 *
 * <p><strong>ASSERTION</strong> - the annotated method is public, has
 * a return type of <code>void</code>, and takes no parameters.</p>
 *
 * <p><strong>ASSERTION</strong> - the annotated method is part of a class
 * that is annotated with the {@link Session} annotation.</p>
 *
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Passivate {
}
