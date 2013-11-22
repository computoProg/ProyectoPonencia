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

package org.apache.shale.dialog.basic.model;

/**
 * <p>{@link EndState} is a spacialized {@link ViewState} that also marks
 * this as the final {@link State} to be executed in the owning {@link Dialog}.
 * When this {@link State} is entered, context information for the owning
 * {@link Dialog} is removed.  Then, if a view identifier is specified,
 * the corresponding JavaServer Faces <code>view</code> will be rendered,
 * and the logical outcome returned by the subsequently invoked action method
 * will be returned to the parent {@link Dialog} (if any) as the logical
 * outcome of this dialog's execution.  If no view identifier is specified,
 * it is assumed that some other mechanism will be used to return output to
 * the client.</p>
 *
 * @since 1.0.4
 */

public interface EndState extends ViewState {

}
