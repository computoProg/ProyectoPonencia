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
 * $Id: ClayAmalgam.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.el.ValueBinding;

import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.config.ClayConfigureListener;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.AttributeBean;
import org.apache.shale.clay.config.beans.ComponentBean;
import org.apache.shale.clay.config.beans.ConfigBean;
import org.apache.shale.clay.config.beans.ConfigBeanFactory;
import org.apache.shale.clay.config.beans.ElementBean;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.util.Messages;
import org.apache.shale.util.Tags;

/**
 * <p>
 * This class is a mix of runtime utilities for the
 * {@link org.apache.shale.clay.component.Clay} component. It is loaded as a
 * managed bean in application scope by the clay component's registration.
 * </p>
 */
public class ClayAmalgam {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", ClayConfigureListener.class
                    .getClassLoader());

    /**
     * <p>
     * Shale tag helper class that contains utility methods for setting
     * component binding and method properties.
     * </p>
     */
    private Tags tagUtils = new Tags();

    /**
     * <p>
     * Replaces tokens in the <code>document</code> with matching tokens in
     * the <code>context</code>.
     * </p>
     *
     * @param document
     *            containing tokens to replace
     * @param context
     *            tokens
     */
    protected void replace(StringBuffer document, Map context) {

        Iterator di = context.entrySet().iterator();
        while (di.hasNext()) {
            Map.Entry e = (Map.Entry) di.next();

            String name = (String) e.getKey();
            String value = (String) e.getValue();

            for (int i = 0; i <= (document.length() - name.length()); i++) {
                String token = document.substring(i, i + name.length());
                if (token.compareToIgnoreCase(name) == 0) {
                    document.delete(i, i + name.length());
                    document.insert(i, value);
                }

                token = null;
            }

            e = null;
            name = null;
            value = null;
        }
        di = null;

    }

    /**
     * <p>
     * Mapping used to encode special characters.
     * </p>
     */
    private static TreeMap encodeMap = null;
    static {
        encodeMap = new TreeMap();
        encodeMap.put("'", "&#39;");
        encodeMap.put("&", "&amp;");
        encodeMap.put("<", "&lt;");
        encodeMap.put(">", "&gt;");
        encodeMap.put("}", "&#125;");
        encodeMap.put("{", "&#123;");
    }

    /**
     * <p>
     * Encodes a string value using the <code>encodeMap</code>.
     * </p>
     *
     * @param value
     *            source string
     * @return target encode string
     */
    public String encode(String value) {
        StringBuffer buff = new StringBuffer(value);
        replace(buff, encodeMap);
        return buff.toString();
    }

    /**
     * <p>
     * Mapping used to decode special characters.
     * </p>
     */
    private static TreeMap decodeMap = null;
    static {
        decodeMap = new TreeMap();
        decodeMap.put("&quot;", "\"");
        decodeMap.put("&#39;", "'");
        decodeMap.put("&amp;", "&");
        decodeMap.put("&lt;", "<");
        decodeMap.put("&gt;", ">");
        decodeMap.put("&#125;", "}");
        decodeMap.put("&#123;", "{");
    }

    /**
     * <p>
     * Decodes a string value using the <code>decodeMap</code>.
     * </p>
     *
     * @param value
     *            source string
     * @return decoded value
     */
    public String decode(String value) {
        StringBuffer buff = new StringBuffer(value);
        replace(buff, decodeMap);
        return buff.toString();
    }

    /**
     * <p>
     * This is a method binding "validator" signature that can be bound to the
     * <code>shapeValidator</code> attribute of the
     * {@link org.apache.shale.clay.component.Clay} component. It expects that
     * the <code>value</code> attribute will contain an html string that
     * represents an HTML node. The value will be encode or decode depending on
     * the value of the <code>escapeXml</code> optional attribute. The default
     * is "false".
     * </p>
     *
     * @param context
     *            faces context
     * @param component
     *            clay
     * @param displayElementRoot
     *            config bean
     */
    public void clayOut(javax.faces.context.FacesContext context,
            javax.faces.component.UIComponent component,
            java.lang.Object displayElementRoot) {

        if (!(displayElementRoot instanceof ComponentBean)
                || !(component instanceof Clay)) {
            throw new RuntimeException(messages.getMessage("invalid.binding",
                    new Object[] { "clayOut" }));
        }

        ComponentBean text = (ComponentBean) displayElementRoot;
        Clay clay = (Clay) component;
        String value = (String) clay.getAttributes().get("value");
        value = tagUtils.evalString(value);
        if (value == null) {
            throw new IllegalArgumentException(messages.getMessage(
                    "missing.attribute", new Object[] { "value", "clayOut" }));
        }

        boolean escapeXml = false;
        String tmp = (String) clay.getAttributes().get("escapeXml");
        if (tmp != null) {
            escapeXml = tagUtils.evalBoolean(tmp).booleanValue();
        }

        if (!escapeXml) {
            value = decode(value);
        } else {
            value = encode(value);
        }

        text.setJsfid("outputText");
        text.setComponentType("javax.faces.HtmlOutputText");

        // add a value attribute
        AttributeBean attr = new AttributeBean();
        attr.setName("value");
        attr.setValue(value);
        text.addAttribute(attr);

        // add a escape attribute
        attr = new AttributeBean();
        attr.setName("escape");
        attr.setValue(Boolean.FALSE.toString());
        text.addAttribute(attr);

        // add a isTransient attribute
        attr = new AttributeBean();
        attr.setName("isTransient");
        attr.setValue(Boolean.TRUE.toString());
        text.addAttribute(attr);

    }

    /**
     * <p>
     * This is a method binding "validator" signature that can be bound to the
     * <code>shapeValidator</code> attribute of the
     * {@link org.apache.shale.clay.component.Clay} component. It expects that
     * the <code>url</code> attribute will contain the file to import relative
     * to the web context root. The content of the file will be encode or decode
     * depending on the value of the <code>escapeXml</code> optional
     * attribute. The default doesn't apply any encoding.
     * </p>
     *
     * @param context
     *            faces context
     * @param component
     *            clay
     * @param displayElementRoot
     *            config bean
     */
    public void clayImport(javax.faces.context.FacesContext context,
            javax.faces.component.UIComponent component,
            java.lang.Object displayElementRoot) {

        if (!(displayElementRoot instanceof ComponentBean)
                || !(component instanceof Clay)) {
            throw new RuntimeException(messages.getMessage("invalid.binding",
                    new Object[] { "clayImport" }));
        }

        ComponentBean text = (ComponentBean) displayElementRoot;
        Clay clay = (Clay) component;
        String url = (String) clay.getAttributes().get("url");
        if (url == null) {
            throw new IllegalArgumentException(messages.getMessage(
                    "missing.attribute", new Object[] { "url", "clayImport" }));
        }
        url = tagUtils.evalString(url);

        boolean escapeXml = true;
        String escAttribute = (String) clay.getAttributes().get("escapeXml");
        if (escAttribute != null) {
            escapeXml = tagUtils.evalBoolean(escAttribute).booleanValue();
        }

        StringBuffer value = new StringBuffer();
        StringBuffer buff = new StringBuffer(url);

        // look for a classpath prefix.
        int i = buff.indexOf(Globals.CLASSPATH_PREFIX);
        if (i > -1) {
            buff.delete(0, i + Globals.CLASSPATH_PREFIX.length());
        }

        InputStream in = null;

        try {
            // if classpath prefix found, use the classloader
            if (i > -1) {
                // load form the classpath
                ClassLoader classloader = Thread.currentThread()
                        .getContextClassLoader();
                if (classloader == null) {
                    classloader = this.getClass().getClassLoader();
                }

                in = classloader.getResourceAsStream(buff.toString());

            } else {
                // load from the context root
                in = context.getExternalContext().getResourceAsStream(
                        buff.toString());
            }

            if (in != null) {
                int c = 0;
                done: while (true) {
                    c = in.read();
                    if (c > -1) {
                        value.append((char) c);
                    } else {
                        break done;
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(messages.getMessage("invalid.attribute",
                    new Object[] { "url", "clayImport" }));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }

        }

        if (escAttribute != null) {
            if (!escapeXml) {
                replace(value, decodeMap);
            } else {
                replace(value, encodeMap);
            }
        }

        text.setJsfid("outputText");
        text.setComponentType("javax.faces.HtmlOutputText");

        // add a value attribute
        AttributeBean attr = new AttributeBean();
        attr.setName("value");
        attr.setValue(value.toString());
        text.addAttribute(attr);

        // add a escape attribute
        attr = new AttributeBean();
        attr.setName("escape");
        attr.setValue(Boolean.FALSE.toString());
        text.addAttribute(attr);

        // add a isTransient attribute
        attr = new AttributeBean();
        attr.setName("isTransient");
        attr.setValue(Boolean.TRUE.toString());
        text.addAttribute(attr);
    }

    /**
     * <p>
     * This is a method binding "validator" signature that can be bound to the
     * <code>shapeValidator</code> attribute of the
     * {@link org.apache.shale.clay.component.Clay} component. It expects four
     * attributes value, bodyJsfid, var and scope. The <code>value</code>
     * attribute is like a dataTable. It should be a value binding expression
     * that is a Map, List or Object[]. The <code>bodyJsfid</code> attribute
     * is root of the subtree that will be repeated in the for loop. The
     * <code>var</code> attribute is the tag used to cache a Map of the bound
     * objects. It will always be loaed in "session" scope. Limitation exists
     * because the "shapeValidator" event is only called when the component is
     * created. This means that you must take care in removing the
     * <code>var</code> object from session scope.
     * </p>
     *
     * @param context
     *            faces
     * @param component
     *            clay
     * @param displayElementRoot
     *            config bean
     */
    public void clayForEach(javax.faces.context.FacesContext context,
            javax.faces.component.UIComponent component,
            java.lang.Object displayElementRoot) {

        if (!(displayElementRoot instanceof ComponentBean)
                || !(component instanceof Clay)) {
            throw new RuntimeException(messages.getMessage("invalid.binding",
                    new Object[] { "clayForEach" }));
        }

        Clay clay = (Clay) component;
        String value = (String) clay.getAttributes().get("value");
        if (value == null) {
            throw new IllegalArgumentException(messages.getMessage(
                    "missing.attribute",
                    new Object[] { "value", "clayForEach" }));
        }

        String bodyJsfid = (String) clay.getAttributes().get("bodyJsfid");
        bodyJsfid = tagUtils.evalString(bodyJsfid);
        if (bodyJsfid == null) {
            throw new IllegalArgumentException(messages.getMessage(
                    "missing.attribute", new Object[] { "bodyJsfid",
                            "clayForEach" }));
        }

        String var = (String) clay.getAttributes().get("var");
        var = tagUtils.evalString(var);
        if (var == null) {
            throw new IllegalArgumentException(messages.getMessage(
                    "missing.attribute", new Object[] { "var", "clayForEach" }));
        }

        // lookup the ConfigBean that handles the bodyJsfid
        ConfigBean config = ConfigBeanFactory.findConfig(bodyJsfid);
        if (config == null) {
            throw new NullPointerException(messages
                    .getMessage("clay.config.notloaded"));
        }

        // make sure it's parsed and cached
        ComponentBean b = config.getElement(bodyJsfid);
        if (b == null) {
            throw new NullPointerException(messages.getMessage(
                    "clay.jsfid.notfound", new Object[] { bodyJsfid }));
        }

        ValueBinding vb = context.getApplication().createValueBinding(value);
        final Object valueList = vb.getValue(context);

        Map beans = new TreeMap();
        int i = 0;
        Iterator vi = null;
        if (valueList == null) {
            vi = Collections.EMPTY_LIST.iterator();
        } else if (valueList instanceof List) {
            vi = ((List) valueList).iterator();
        } else if (valueList instanceof Map) {
            vi = ((Map) valueList).entrySet().iterator();
        } else {
            Object[] anArray = new Object[0];
            if (anArray.getClass().isAssignableFrom(valueList.getClass())) {
                vi = new Iterator() {
                    private int index = 0;

                    private final Object[] list = (Object[]) valueList;

                    public boolean hasNext() {
                        return (index < list.length);
                    }

                    public Object next() {
                        return list[index++];
                    }

                    public void remove() {
                    };

                };
            } else {
                throw new IllegalArgumentException(messages.getMessage(
                        "invalid.collectiontype", new Object[] { value }));
            }
        }

        if (vi != null) {
            while (vi.hasNext()) {
                // create a key for the beans map
                StringBuffer id = new StringBuffer("bean" + ++i);
                // add the subscripted bean to the beans map with the generated
                // key
                beans.put(id.toString(), vi.next());

                // create a naming container to hold the row
                ElementBean namingContainer = new ElementBean();
                namingContainer.setRenderId(i);
                namingContainer.setJsfid("namingContainer");
                namingContainer.setComponentType("javax.faces.NamingContainer");

                // create a new nested bean
                ElementBean target = new ElementBean();
                target.setJsfid(bodyJsfid);
                target.setExtends(bodyJsfid);
                target.setRenderId(i);
                config.assignParent(target);
                config.realizingInheritance(target);

                // prepend the var to the generated key
                id.insert(0, var + ".");
                SymbolBean symbol = new SymbolBean();
                symbol.setName(Globals.MANAGED_BEAN_MNEMONIC);
                symbol.setValue(id.toString());
                target.addSymbol(symbol);

                namingContainer.addChild(target);
                ((ComponentBean) displayElementRoot).addChild(namingContainer);
            }

        }

        context.getExternalContext().getSessionMap().put(var, beans);

    }
}
