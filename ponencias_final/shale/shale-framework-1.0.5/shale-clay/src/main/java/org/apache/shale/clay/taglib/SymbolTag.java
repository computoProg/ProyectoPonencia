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
 * $Id: SymbolTag.java 464373 2006-10-16 04:21:54Z rahul $
 */
package org.apache.shale.clay.taglib;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.shale.clay.component.Clay;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.util.Messages;

/**
 * <p>This Tag is used to add replacement symbols to the {@link Clay} component.
 * Replacement symbols are substituted within the meta-data used to build the subtree
 * under the Clay component.  This tag is similar to the standard JSF attribute tag.
 * The <code>name</code> attribute will be prepended with an '@' character.
 * The "at" character is the symbol identifier.</p>
 */
public class SymbolTag extends TagSupport {

    /**
     * <p>Unique id used by the <code>Serializable</code> interface.</p>
     */
    private static final long serialVersionUID = 3977021747121698357L;

    /**
     * <p>Message resources for this class.</p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", SymbolTag.class.getClassLoader());

    /**
     * <p>The <code>name</code> of the symbol.</p>
     */
    private String name = null;

    /**
     * <p>The <code>value</code> of the symbol.</p>
     */
    private String value = null;

    /**
     * <p>Returns the name of the symbol.</p>
     *
     * @return name of the symbol
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Sets the <code>name</code> of the symbol.</p>
     *
     * @param name of the symbol
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Returns the value for the symbol.</p>
     *
     * @return value of the symbol
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Sets the <code>value</code> for the symbol.</p>
     *
     * @param value of the symbol
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * <p>Finds the parent component and adds the symbol to the
     * Clay component's symbol table.  The parent has to be a {@link Clay} component.
     * </p>
     *
     * @return next transition
     * @exception JspException general JSP tag
     */
    public int doStartTag() throws JspException {
          UIComponentTag parentTag = UIComponentTag.getParentUIComponentTag(pageContext);
          if (parentTag == null) {
             throw new JspException(messages.getMessage("clayparent.notfound"));
          }

          UIComponent parentComponent = parentTag.getComponentInstance();
          if (parentComponent == null) {
              throw new JspException(messages.getMessage("clayparent.notfound"));
          }

          if (!(parentComponent instanceof Clay)) {
              throw new JspException(messages.getMessage("clayparent.notfound"));
          }

          Clay clayParent = (Clay) parentComponent;

          StringBuffer tmp = new StringBuffer(name);
          if (tmp.charAt(0) != '@') {
            tmp.insert(0, '@');
          }
          SymbolBean symbol = new SymbolBean();
          symbol.setName(tmp.toString());
          symbol.setValue(value);
          clayParent.getSymbols().put(symbol.getName(), symbol);

          return super.doStartTag();
    }

}
