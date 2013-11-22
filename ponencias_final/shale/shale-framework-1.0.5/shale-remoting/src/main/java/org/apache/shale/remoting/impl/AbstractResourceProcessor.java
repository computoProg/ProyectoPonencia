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
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shale.remoting.Processor;
import org.apache.shale.remoting.faces.ResponseFactory;

/**
 * <p>Convenience abstract base class for {@link Processor} implementations
 * that serve up static resources.</p>
 */
public abstract class AbstractResourceProcessor extends FilteringProcessor {


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The <code>Log</code> instance for this class.</p>
     */
    private transient Log log = null;


    // ------------------------------------------------------- Processor Methods


    /**
     * <p>Check if the specified resource actually exists.  If it does not,
     * return an HTTP 404 status (servlet) or throw an IllegalArgumentException
     * (portlet).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource to be served
     *
     * @exception IllegalArgumentException if the specified resource does
     *  not exist in a portlet environment (because we cannot return an
     *  HTTP status 404)
     * @exception IOException if an input/output error occurs
     */
    public void process(FacesContext context, String resourceId) throws IOException {

        // Validate our input parameters
        if (resourceId == null) {
            throw new NullPointerException();
        }
        if (!resourceId.startsWith("/")) {
            throw new IllegalArgumentException(resourceId);
        }

        // If someone else has completed the response, we do not have
        // anything to do
        if (context.getResponseComplete()) {
            return;
        }

        // Filter based on our includes and excludes patterns
        if (!accept(resourceId)) {
            if (log().isTraceEnabled()) {
                log().trace("Resource id '" + resourceId
                            + "' rejected by include/exclude rules");
            }
            // Send an HTTP "not found" response to avoid giving the client
            // any information about a resource that exists and was refused,
            // versus a resource that does not exist
            sendNotFound(context, resourceId);
            context.responseComplete();
            return;
        }

        // Acquire a URL to the specified resource, if it exists
        // If not, send an HTTP "not found" response
        URL url = getResourceURL(context, resourceId);
        if (log().isDebugEnabled()) {
            log().debug("Translated resource id '" + resourceId + "' to URL '"
                        + url + "'");
        }
        if (url == null) {
            if (log().isTraceEnabled()) {
                log().trace("Resource '" + resourceId + "' not found, returning 404");
            }
            sendNotFound(context, resourceId);
            context.responseComplete();
            return;
        }

        // If this request includes "If-Modified-Since" header, return
        // an HTTP "not modified" response if the specified timestamp is
        // equal to or later than our application resource timestamp
        long ifModifiedSince = ifModifiedSince(context);
        if ((ifModifiedSince >= 0)
            && ((ifModifiedSince + 1000L) >= getLastModified())) {
            if (log().isTraceEnabled()) {
                log().trace("Resource '" + resourceId + "' not modified, returning 304");
            }
            sendNotModified(context, resourceId);
            context.responseComplete();
            return;
        }

        // Set up the response headers
        sendLastModified(context, getLastModifiedString());

        // Copy the resource contents to the response output stream
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = inputStream(context, url);
            String contentType = mimeType(context, resourceId);
            outputStream = outputStream(context, contentType);
            copyStream(context, inputStream, outputStream);
        } finally {
            if (outputStream != null) {
                try { outputStream.close(); } catch (Exception e) { ; }
            }
            if (inputStream != null) {
                try { inputStream.close(); } catch (Exception e) { ; }
            }
        }

        // Finish up by indicating that this response is already complete
        context.responseComplete();

    }



    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The buffer size when copying the input stream to the output stream.</p>
     */
    private int bufferSize = 1024;


    /**
     * <p>The date/time (in milliseconds since the epoch) value to generate on the
     * <code>Last-Modified</code> header included with each served resource.</p>
     */
    private long lastModified = 0;


    /**
     * <p>The string version of the <code>lastModified</code> value.</p>
     */
    private String lastModifiedString = null;


    /**
     * <p><code>Map</code> of MIME types, keyed by file extension.  This is
     * used as a fallback if the <code>ServletContext</code> or
     * <code>PortletContext</code> call to <code>mimeType()</code> does not
     * return any result.</p>
     */
    protected Map mimeTypes = new HashMap();
    {
        mimeTypes.put(".css", "text/css");
        mimeTypes.put(".gif", "image/gif");
        mimeTypes.put(".ico", "image/vnd.microsoft.icon");
        mimeTypes.put(".jpeg", "image/jpeg");
        mimeTypes.put(".jpg", "image/jpeg");
        mimeTypes.put(".js", "text/javascript");
        mimeTypes.put(".png", "image/png");
    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>The date formatting helper we will use in <code>httpTimestamp()</code>.
     * Note that usage of this helper must be synchronized.</p>
     */
    private static SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",
                                 Locale.US);
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the buffer size when copying.</p>
     */
    public int getBufferSize() {
        return this.bufferSize;
    }


    /**
     * <p>Set the buffer size when copying.</p>
     *
     * @param bufferSize The new buffer size
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    /**
     * <p>Return the date/time (expressed as the number of milliseconds since
     * the epoch) that will be generated on the <code>Last-Modified</code>
     * header of all resources served by this processor.  If this value has
     * not been set upon first call to this method, it will be set to the
     * current date and time.</p>
     */
    public long getLastModified() {
       if (lastModified == 0) {
            setLastModified((new Date()).getTime());
        }
        return lastModified;
    }


    /**
     * <p>Set the date/time (expressed as the number of milliseconds since
     * the epoch) that wll be generated on the <code>Last-Modified</code>
     * header of all resources served by this processor.</p>
     *
     * @param lastModified The new last modified value
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
        this.lastModifiedString = httpTimestamp(lastModified);
    }


    /**
     * <p>Return a String version of the last modified date/time, formatted
     * as required by Section 3.3.1 of the HTTP/1.1 Specification.  If the
     * <code>lastModified</code> property has not been set upon first call to
     * this method, it will be set to the current date and time.</p>
     */
    public String getLastModifiedString() {
        if (lastModified == 0) {
            setLastModified((new Date()).getTime());
        }
        return lastModifiedString;
    }


    // -------------------------------------------------------- Abstract Methods


    /**
     * <p>Convert the specified resource identifier into a URL, if the resource
     * actually exists.  Otherwise, return <code>null</code>.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier to translate
     */
    protected abstract URL getResourceURL(FacesContext context, String resourceId);


    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Copy the contents of the specified input stream to the specified
     * output stream.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param inputStream <code>InputStream</code> to be copied from
     * @param outputStream <code>OutputStream</code> to be copied to
     *
     * @exception IOException if an input/output error occurs
     */
    protected void copyStream(FacesContext context, InputStream inputStream,
                              OutputStream outputStream) throws IOException {

        byte[] buffer = new byte[getBufferSize()];
        while (true) {
            int len = inputStream.read(buffer);
            if (len <= 0) {
                break;
            }
            outputStream.write(buffer, 0, len);
        }

    }


    /**
     * <p>Return a textual representation of the specified date/time stamp
     * (expressed as a <code>java.util.Date</code> object)
     * in the format required by the HTTP/1.1 Specification (RFC 2616),
     * Section 3.3.1.  An example of this format is:
     * <blockquote>
     *   Sun, 06 Nov 1994 08:49:37 GMT
     * </blockquote></p>
     *
     * @param timestamp The date/time to be formatted, expressed as
     *  a <code>java.util.Date</code>
     */
    protected String httpTimestamp(Date timestamp) {

        synchronized (format) {
            return format.format(timestamp);
        }

    }


    /**
     * <p>Return a textual representation of the specified date/time stamp
     * (expressed in milliseconds since the epoch, and assumed to be GMT)
     * in the format required by the HTTP/1.1 Specification (RFC 2616),
     * Section 3.3.1.  An example of this format is:
     * <blockquote>
     *   Sun, 06 Nov 1994 08:49:37 GMT
     * </blockquote></p>
     *
     * @param timestamp The date/time to be formatted, expressed as the number
     *  of milliseconds since the epoch
     */
    protected String httpTimestamp(long timestamp) {

        return httpTimestamp(new Date(timestamp));

    }


    /**
     * <p>Return the value of the <code>If-Modified-Since</code> header
     * included on this request, as a number of milliseconds since the
     * epoch.  If this header was not included (or we cannot tell if it
     * was included), return -1 instead.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    protected long ifModifiedSince(FacesContext context) {

        Object request = context.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest) request).getDateHeader("If-Modified-Since");
        }
        return -1;

    }


    /**
     * <p>Return an <code>InputStream</code> derived from the specified URL,
     * which will point to the static resource to be served.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param url <code>URL</code> from which to derive an input stream
     *
     * @exception IOException if an input/output error occurs
     */
    protected InputStream inputStream(FacesContext context, URL url) throws IOException {

        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        return conn.getInputStream();

    }


    /**
     * <p>Return the appropriate MIME type (if known) for the specified resource
     * path.  This method is portable across servlet and portlet environments.
     * If no MIME type is known, fall back to a configured list, based on the
     * extension of the requested resource.  If no result can be found in the
     * fallback list, return <code>null</code>.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource to categorize
     */
    protected String mimeType(FacesContext context, String resourceId) {

        Object ctxt = context.getExternalContext().getContext();
        Class clazz = ctxt.getClass();
        Method method = null;
        try {
            method = clazz.getMethod("getMimeType", new Class[] { String.class });
            // Return the container calculated type, if any
            String result = (String) method.invoke(ctxt,
                                                   new Object[] { resourceId });
            if (result != null) {
                return result;
            }
            // Check our fallback list
            Iterator entries = mimeTypes.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (resourceId.endsWith((String) entry.getKey())) {
                    return (String) entry.getValue();
                }
            }
            // We have no clue what MIME type should be used for this resource
            return null;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("mimeType.exception", e);
            }
            return null;
        }

    }


    /**
     * <p>Return an <code>OutputStream</code> to which our static
     * resource is to be served.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType Content type for this response
     *
     * @exception IOException if an input/output error occurs
     */
    protected OutputStream outputStream(FacesContext context, String contentType)
      throws IOException {

        return (new ResponseFactory()).getResponseStream(context, contentType);

    }


    /**
     * <p>Return <code>true</code> if we are processing a servlet request (as
     * opposed to a portlet request).</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    protected boolean servletRequest(FacesContext context) {

        return context.getExternalContext().getContext() instanceof ServletContext;

    }


    /**
     * <p>Set the content type on the servlet or portlet response object.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param contentType The content type to be set
     */
    protected void sendContentType(FacesContext context, String contentType) {

        Object response = context.getExternalContext().getResponse();
        try {
            Method method =
              response.getClass().getMethod("setResponseType",
                                             new Class[] { String.class });
            method.invoke(response, new Object[] { contentType });
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("contentType.exception", e);
            }
        }

    }


    /**
     * <p>Set the <code>Last-Modified</code> header to the specified timestamp.</p>
     *
     * @param context <code>FacesContext</code> for this request
     * @param timestamp String version of the last modified timestamp
     */
    protected void sendLastModified(FacesContext context, String timestamp) {

        Object response = context.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).setHeader("Last-Modified", timestamp);
        /* else it is a portlet response with mechanism to support this
        } else {
            ;
        */
        }

    }


    /**
     * <p>Send a "not found" HTTP response, if possible.  Otherwise, throw an
     * <code>IllegalArgumentException</code> that will ripple out.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource that was not found
     *
     * @exception IllegalArgumentException if we cannot send an HTTP response
     * @exception IOException if an input/output error occurs
     */
    protected void sendNotFound(FacesContext context, String resourceId) throws IOException {

        if (servletRequest(context)) {
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_NOT_FOUND, resourceId);
        } else {
            throw new IllegalArgumentException(resourceId);
        }

    }


    /**
     * <p>Send a "not modified" HTTP response, if possible.  Otherwise, throw an
     * <code>IllegalArgumentException</code> that will ripple out.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param resourceId Resource identifier of the resource that was not modified
     *
     * @exception IllegalArgumentException if we cannot send an HTTP response
     * @exception IOException if an input/output error occurs
     */
    protected void sendNotModified(FacesContext context, String resourceId) throws IOException {

        if (servletRequest(context)) {
            HttpServletResponse response = (HttpServletResponse)
              context.getExternalContext().getResponse();
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED, resourceId);
        } else {
            throw new IllegalArgumentException(resourceId);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return the <code>Log</code> instance to use, creating one if needed.</p>
     */
    private Log log() {

        if (this.log == null) {
            log = LogFactory.getLog(AbstractResourceProcessor.class);
        }
        return log;

    }


}
