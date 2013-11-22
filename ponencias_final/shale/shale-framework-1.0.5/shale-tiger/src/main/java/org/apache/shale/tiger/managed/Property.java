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
 * <p>Field-level annotation indicating that the decorated field should be
 * initialized to a literal or value binding expression value specified by the
 * <code>value</code> attribute, when a managed instance of the containing
 * class is instantiated.  If desired, you may also specify the property name
 * that corresponds to the annotated field, rather than relying on the default
 * assumption that the field name is identical to the property name.</p>
 *
 * @since 1.0.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {

    /**
     * <p>The name of the JavaBeans property to which this annotation corresponds.
     * If not specified, the name of the field
     * is presumed to be the name of the property as well.</p>
     */
    String name() default "";


    /**
     * <p>The string representation of the literal value, or value
     * binding expression, used to intiialize this field.
     * Appropriate type conversion will be performed.</p>
     */
    String value();


}
