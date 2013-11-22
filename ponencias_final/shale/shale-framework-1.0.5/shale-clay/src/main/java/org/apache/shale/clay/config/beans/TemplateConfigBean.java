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
 * $Id: TemplateConfigBean.java 516836 2007-03-11 01:36:16Z gvanmatre $
 */
package org.apache.shale.clay.config.beans;

import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.shale.clay.config.ClayTemplateParser;
import org.apache.shale.clay.config.Globals;

/**
 * <p>The second type of top-level object pool.  This implementation
 * is designed to provide Tapestry like template composition.  The
 * top-level {@link ComponentBean} is materialized from a HTML fragment
 * where HTML elements are bound to meta components defined in the
 * XML configuration files and cached by an instance of {@link ComponentConfigBean}
 * </p>
 */
public class TemplateConfigBean extends ComponentConfigBean {

    /**
     * <p>Returns a {@link ComponentBean} that is materialized
     * using a HTML template fragment.  The <code>templateName</code>
     * is the name of the file relative to the context root of the
     * web application</p>
     * @param templateName name of the markup template
     * @return root component bean for the <code>templateName</code>
     */
    public ComponentBean getElement(String templateName) {

        StringBuffer jsfid = new StringBuffer(templateName);
        if (jsfid.length() > 0 &&  jsfid.charAt(0) != '/') {
          jsfid.insert(0, '/');
        }

        // look for a watcher identified by the template name
        WatchDog watchDog = (WatchDog) watchDogs.get(jsfid.toString());

        //if a watcher doesn't exist, check for a common
        if (watchDog == null) {
            watchDog = (WatchDog) watchDogs.get(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG);
        }

        if (watchDog == null || super.getElement(jsfid.toString()) == null) {
            //The first time the page is created, create a watcher

            watchDog = new WatchDog(getConfigDefinitions(jsfid.toString()), jsfid.toString());

            // register by name
            watchDogs.put(watchDog.getName(), watchDog);

            //loads the HTML template the first time and when file
            //has been modified
            watchDog.refresh(false);
        }

        // returns the cached element
        return super.getElement(jsfid.toString());
    }


    /**
     * <p>Returns an integer value use to order the registered {@link ConfigBean} instances
     * with the {@link ConfigBeanFactory}.
     * </p>
     *
     * @return weight value of <code>1</code>
     */
    public int getWeight() {
        return 1;
    }

    /**
     * <p>Overrides the super call to change the condition of the filter.  This
     * {@link ConfigBean} can create components where the id end in the suffix
     * defined in the web deployment descriptor as a initialization parameter with
     * the name defined by <code>Globals.CLAY_HTML_TEMPLATE_SUFFIX</code>  Or, using
     * the default defined by <code>Globals.CLAY_DEFAULT_HTML_TEMPLATE_SUFFIX</code>
     *
     * @param id jsfid
     * @return <code>true</code> if the <code>jsfid</code> can be handled here
     */
    public boolean validMoniker(String id) {
        return id.endsWith(suffixes[0]);
    }

    /**
     * <p>This is an overridden method called from the init method.
     * It loads an instance of the {@link ClayTemplateParser} and
     * establishes a Map collection to hold the resource
     * {@link org.apache.shale.clay.config.beans.ComponentConfigBean$WatchDog}'s.</p>
     */
    protected void loadConfigFiles() {
        parser = new ClayTemplateParser();
        parser.setConfig(this);

        watchDogs = Collections.synchronizedMap(new TreeMap());
    }


    /**
     * <p>The HTML templates are loaded on-demand.  Override this method forcing the auto
     * load option on.  The XML configuration files are only effected by the
     * <code>auto-reload-clay-files</code> initialization parameter.</p>
     *
     * @param context web container servlet context
     */
    public void init(ServletContext context) {
        super.init(context);

        isWatchDogOn = true;
    }


    /**
     * <p>If the <code>watchDogName</code> equals
     * the {@link ComponentBean} that defines the selected
     * template, remove it.</p>
     *
     * @param watchDogName grouping of template files
     */
    protected void clear(String watchDogName) {
        if (!watchDogName.equals(Globals.DEFAULT_COMPONENT_CONFIG_WATCHDOG)) {
            //unassign a single template component
            ComponentBean b = (ComponentBean) displayElements.get(watchDogName);
            displayElements.remove(watchDogName);
            if (b != null) {
                try {
                    unassignParent(b);
                } catch (RuntimeException e1) {
                    // log.error(e1);
                }
            }
            b = null;
        } else {
           super.clear(watchDogName);
        }
    }


    /**
     * <p>If the <code>forceReload</code> is <code>true</code>,
     * the <code>displayElements</code> cache is invalidated.
     * A <code>true</code> value is returned if cache has
     * been cleared.</p>
     *
     * @param forceReload invalidate the cache flag
     * @return <code>true</code> if the templates were reloaded
     */
    public boolean refresh(boolean forceReload) {
        if (forceReload) {
            //remove all old templates
            Iterator wi = watchDogs.entrySet().iterator();
            while (wi.hasNext()) {
                Map.Entry e = (Map.Entry) wi.next();
                WatchDog watchDog = (WatchDog) e.getValue();
                clear(watchDog.getName());
                if (watchDog != null) {
                    watchDog.destroy();
                }

            }
            watchDogs.clear();
        }

        return forceReload;
    }


    /**
     * <p>
     * Returns the root metadata component that is used to add to the component
     * tree. It locates the {@link ComponentBean} using the <code>jsfid</code>
     * attribute as the key. A call to the {@link ConfigBeanFactory} locates the
     * correct {@link ConfigBean} used to find the {@link ComponentBean}. </p>
     *
     * @param jsfid parent id of a config bean
     * @return parent config bean
     */
    protected ComponentBean getTopLevelElement(String jsfid) {

        if (validMoniker(jsfid)) {
            return getElement(jsfid);
        }

        //broaden the search to the other ConfigBean's
        ConfigBean config = ConfigBeanFactory.findConfig(jsfid);

        if (config == null) {
            throw new NullPointerException(messages
                    .getMessage("config.notloaded", new Object[] { jsfid }));
        }

        if (config == this) {
            throw new NullPointerException(messages.getMessage(
                    "jsfid.notfound", new Object[] { jsfid }));
        }

        // find the top-level display element associated with the subtree
        ComponentBean b = config.getElement(jsfid);
        if (b == null) {
            throw new NullPointerException(messages.getMessage(
                    "jsfid.notfound", new Object[] { jsfid }));
        }

        return b;
    }


    /**
     * <p>Determines if the <code>node</code> is a transient
     * <code>outputText</code> (<strong>verbatim</strong>) component.</p>
     *
     * @param node a config bean that represents a template token
     * @return <code>true</code> if the node is a verbatim node
     */
    private boolean isVerbatim(ComponentBean node) {

        AttributeBean attr = null;
        if ((node.getJsfid().equals("verbatim") || node.getJsfid().equals("f:verbatim"))
             && node.getComponentType().equals("javax.faces.HtmlOutputText")) {
            attr = node.getAttribute("isTransient");
            if (attr != null) {
                if (attr.getValue() != null && attr.getValue().length() > 0) {
                    return (Character.toLowerCase(attr.getValue().charAt(0)) == 't');
                }
            }
        }

        return false;
    }


    /**
     * <p>Recursively walks down the graph of meta-data {@link ComponentBean}'s
     * looking at the children of the <code>root</code>.  Adjacent
     * children that are both <code>verbatim</code> component
     * definitions are merged.  If there is only one child and
     * the child and root nodes are both <code>verbatim</code>
     * definitions, the child is merged up to the root.</p>
     *
     * @param root top config bean that represents a markup template
     */
    public void optimizeTree(ComponentBean root) {

        // children is a TreeSet that is returned as a Collection.
        int size = root.getChildren().size();
        ComponentBean[] children = new ComponentBean[size];
        BitSet verbatimSet = new BitSet(size);
        verbatimSet.clear(0, size);

        StringBuffer buff = new StringBuffer();

        int i = 0;
        Iterator ci = root.getChildrenIterator();
        while (ci.hasNext()) {
            children[i] = (ComponentBean) ci.next();
            if (isVerbatim(children[i])) {
                verbatimSet.set(i);
            }

            if (children[i].getChildren().size() > 0) {
                optimizeTree(children[i]);    // merge children for the top down
                // starting a the botton of the tree.
            }

            i++;
        }

        int s = -1;
        while ((s = verbatimSet.nextSetBit(++s)) > -1) {

            merge: for (int j = s + 1; j < children.length; j++) {
                if (verbatimSet.get(j)) {
                    buff.setLength(0);

                    // grap the value attribute of the first one in the stack
                    // and concat to a buffer
                    AttributeBean attrTop = children[s].getAttribute("value");
                    if (attrTop != null) {
                        if (attrTop.getValue() != null) {
                            buff.append(attrTop.getValue());
                        }
                    } else {
                        break merge;   // a verbatim without a value should never happen
                    }

                    AttributeBean attrNext = children[j].getAttribute("value");  // the next in sequence to be merged
                    if (attrNext != null) {
                        if (attrNext.getValue() != null) {
                            buff.append(attrNext.getValue());
                        }
                    } else {
                        continue merge;   // a verbatim without a value should never happen
                    }
                    // merge node values
                    attrTop.setValue(buff.toString());
                    root.getChildren().remove(children[j]); // delete the node after merge from the parent

                } else {
                    // the verbatims are not in sequence (true, false, true)
                    s = j;
                    break merge;
                }
            }


        }

        // if the root is a verbatim and the only child is a verbatim
        // merge up to the root
        if (isVerbatim(root) && root.getChildren().size() == 1
                && isVerbatim(children[0])) {

            buff.setLength(0);

            // grap the value attribute of the first one in the stack
            // and concat to a buffer
            AttributeBean attrTop = root.getAttribute("value");
            if (attrTop != null) {
                if (attrTop.getValue() != null) {
                    buff.append(attrTop.getValue());
                }

                AttributeBean attrNext = children[0].getAttribute("value");  // the next in sequence to be merged
                if (attrNext != null) {
                    if (attrNext.getValue() != null) {
                        buff.append(attrNext.getValue());
                    }
                }
                // merge node values
                attrTop.setValue(buff.toString());
                root.getChildren().clear(); // delete the node after merge from the parent
            }
        }


    }

}
