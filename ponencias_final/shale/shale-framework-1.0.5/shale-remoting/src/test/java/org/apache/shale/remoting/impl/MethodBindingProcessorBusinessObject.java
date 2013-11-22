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
import java.io.PrintWriter;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.shale.remoting.faces.ResponseFactory;

/**
 * <p>Business object that includes methods to produce output, when
 * invoked indirectly via <code>MethodBindingProcessor</code>.</p>
 */
public class MethodBindingProcessorBusinessObject {
    

    /**
     * <p>Create binary output directy to the servlet response.</p>
     */
    public void directStream() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.setContentType("application/x-binary");
            ServletOutputStream stream = response.getOutputStream();
            for (int i = 0; i < 10; i++) {
                stream.write(i);
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }

    }


    /**
     * <p>Create character output directly to the servlet response.</p>
     */
    public void directWriter() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.setContentType("text/x-plain");
            PrintWriter writer = response.getWriter();
            for (int i = 0; i < 10; i++) {
                writer.write('a' + i);
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }

    }


    /**
     * <p>Create binary output indirecty via a factory.</p>
     */
    public void indirectStream() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ResponseStream stream =
              (new ResponseFactory()).getResponseStream(context, "application/x-binary");
            for (int i = 0; i < 10; i++) {
                stream.write(i);
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }

    }


    /**
     * <p>Create character output indirectly a factory.</p>
     */
    public void indirectWriter() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ResponseWriter writer =
              (new ResponseFactory()).getResponseWriter(context, "text/x-plain");
            for (int i = 0; i < 10; i++) {
                writer.write('a' + i);
            }
        } catch (IOException e) {
            throw new FacesException(e);
        }

    }


}
