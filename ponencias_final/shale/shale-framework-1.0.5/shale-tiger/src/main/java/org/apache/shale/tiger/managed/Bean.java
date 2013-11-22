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

package org.apache.shale.tiger.managed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Class-level annotation indicating that the decorated class should be
 * treated as a JavaServer Faces "managed bean" definition for the bean name
 * specified by the "name" attribute.  Managed beans defined through annotations
 * are orthogonal to those defined via the standard configuration files (although
 * this might change in the future to support an overrides mechanism).</p>
 *
 * <p><strong>ASSERTION</strong> - The annotated class has a public, zero-args
 * constructor for dynamic instantiation.</p>
 *
 * <p><strong>ASSERTION</strong> - The value specified for the <code>name</code>
 * attribute is unique across all classes visible (at runtime) in the same
 * application unit.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {

    /**
     * <p>The managed bean name used to cause managed creation of instances
     * of this class.</p>
     */
    String name();


    /**
     * <p>The scope (if any) into which newly created beans should be stored.</p>
     */
    Scope scope() default Scope.NONE;

}
