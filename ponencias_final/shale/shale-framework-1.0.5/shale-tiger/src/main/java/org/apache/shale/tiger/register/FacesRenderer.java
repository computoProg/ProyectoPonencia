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

package org.apache.shale.tiger.register;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.faces.render.RenderKitFactory;

/**
 * <p>Class-level annotation indicating that this class should be registered
 * as a JavaServer Faces <code>Renderer</code> under the parameters
 * specified by our attributes.</p>
 *
 * <p><strong>ASSERTION</strong> - The annotated class implements
 * <code>Renderer</code>.</p>
 *
 * @since 1.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FacesRenderer {


    /**
     * <p>The <code>RenderKit</code> identifier of the <code>RenderKit</code>
     * with which we should register.  If not specified, we will register
     * in the default HTML Basic <code>RenderKit</code>.</p>
     */
    String renderKitId() default RenderKitFactory.HTML_BASIC_RENDER_KIT;


    /**
     * <p>The renderer type under which we should register.</p>
     */
    String rendererType();


    /**
     * <p>The component family for which we should register.</p>
     */
    String componentFamily();


}
