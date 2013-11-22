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
 * $Id: AbstractCommand.java 473459 2006-11-10 20:30:12Z gvanmatre $
 */
package org.apache.shale.clay.component.chain;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.shale.clay.config.Globals;
import org.apache.shale.clay.config.beans.SymbolBean;
import org.apache.shale.util.Messages;
import org.apache.shale.util.Tags;

/**
 * <p>
 * The base class for the commands that create the component tree.
 * </p>
 */
public abstract class AbstractCommand implements Command {

    /**
     * <p>
     * Message resources for this class.
     * </p>
     */
    private static Messages messages = new Messages(
            "org.apache.shale.clay.Bundle", AbstractCommand.class
            .getClassLoader());

    /**
     * @return message resources
     */
    protected static Messages getMessages() {
       return messages;
    }

    /**
     * <p>Shale tag helper class that contains utility methods for setting
     * component binding and method properties.</p>
     */
    private Tags tagUtils = new Tags();

    /**
     * @return Shale tag helper class that contains utility methods for setting
     * component binding and method properties.
     */
    protected Tags getTagUtils() {
        return tagUtils;
    }

    /**
     * <p>
     * Returns the {@link Catalog} with a name of
     * <code>Globals.CLAY_CATALOG_NAME</code> in the
     * <code>Globals.CLAY_RESOURCE_NAME</code> configuration file.
     * </p>
     *
     * @return commons chains catalog
     * @exception Exception finding catalog
     */
    protected Catalog getCatalog() throws Exception {

        Catalog catalog = CatalogFactory.getInstance().getCatalog(
                Globals.CLAY_CATALOG_NAME);
        if (catalog == null) {

            ConfigParser parser = new ConfigParser();
            URL url = this.getClass().getClassLoader().getResource(
                    Globals.CLAY_RESOURCE_NAME);
            if (url == null) {
                throw new IllegalArgumentException(Globals.CLAY_RESOURCE_NAME);
            }
            parser.parse(url);

            catalog = CatalogFactory.getInstance().getCatalog(
                    Globals.CLAY_CATALOG_NAME);

        }

        return catalog;

    }

    /**
     * <p>
     * Returns the {@link Catalog} with a name identified by the
     * constant <code>Globals.CLAY_CUSTOMIZATION_CATALOG_NAME</code>.
     * </p>
     *
     * @return commons chains customizations catalog
     * @exception Exception finding customizations catalog
     */
    protected Catalog getCustomizationCatalog() throws Exception {

        Catalog catalog = CatalogFactory.getInstance().getCatalog(
                Globals.CLAY_CUSTOMIZATION_CATALOG_NAME);

        return catalog;
    }

    /**
     * <p>
     * This call is used to substitue an attribute binding expression containing
     * the <code>symbols</code> with the target property value in the {@link ClayContext}.
     * The current attribute within the context is assumed.
     * </p>
     *
     * @param context holding the symbols and the target attribute
     * @return string with the symbols replaces
     */
    public static String replaceMnemonic(ClayContext context) {
       return replaceMnemonic(context, context.getAttribute().getValue());
    }

    /**
     * <p>Evaluates nested symbols.  These are symbols that have references to other
     * symbols as their values.  The nested symbols evaluation is sensitive to dependencies.
     * The current scoped symbol table is found in the {@link ClayContext}.</p>
     *
     * @param context holding the symbols
     */
    public static void realizeSymbols(ClayContext context) {
        Map symbols = context.getSymbols();
        if (symbols == null || symbols.isEmpty()) {
            return;
        }

        Iterator si = symbols.entrySet().iterator();

        Map dependenciesMap = new TreeMap();
        TreeSet allNestedSymbols = new TreeSet();

        while (si.hasNext()) {
            Map.Entry e = (Map.Entry) si.next();
            SymbolBean symbol = (SymbolBean) e.getValue();
            if (symbol.getValue() != null) {
                List symbolDependencies = findSymbols(context, symbol.getValue());
                if (!symbolDependencies.isEmpty()) {
                    dependenciesMap.put(symbol.getName(), symbolDependencies);
                    allNestedSymbols.addAll(symbolDependencies);
                    allNestedSymbols.add(symbol.getName());
                }
            }
        }

        List allNestedSymbolsOrdered = getOrderedByDependencies(allNestedSymbols, dependenciesMap);
        for (int i = 0; i < allNestedSymbolsOrdered.size(); i++) {
            String symbolName = (String) allNestedSymbolsOrdered.get(i);
            SymbolBean sourceSymbol = (SymbolBean) symbols.get(symbolName);
            if (sourceSymbol != null && sourceSymbol.getValue() != null) {
                String value =  replaceMnemonic(context, sourceSymbol.getValue());
                SymbolBean targetSymbol = new SymbolBean();
                targetSymbol.setDescription(sourceSymbol.getDescription());
                targetSymbol.setName(sourceSymbol.getName());
                targetSymbol.setValue(value);

                symbols.put(targetSymbol.getName(), targetSymbol);
            }
        }

    }

    /**
     * <p>Orders the symbols by their dependencies.  Each symbol
     * can have multiple dependencies.</p>
     *
     * @param allNestedSymbols Set of symbol names
     * @param dependenciesMap List of dependencies for each symbol
     * @return ordered list of symbol names ordered by dependencies
     */
    private static List getOrderedByDependencies(Set allNestedSymbols, Map dependenciesMap) {

        List tmpList = new ArrayList(allNestedSymbols);

        ordered: for (int i = 0; i < tmpList.size(); i++) {
            boolean swap = false;
            for (int j = 0; j < tmpList.size(); j++) {
                String symbolName = (String) tmpList.get(j);
                List symbolDependencies  = (List) dependenciesMap.get(symbolName);
                if (symbolDependencies != null && symbolDependencies.size() > 0) {
                    int max = -1;
                    for (int n = 0; n < symbolDependencies.size(); n++) {
                        max = Math.max(max, tmpList.indexOf(symbolDependencies.get(n)));
                    }
                    if (max > j) {
                        String tmp = (String) tmpList.get(j);
                        tmpList.remove(j);
                        tmpList.add(max, tmp);
                        swap = true;
                        j = max;
                    }
                }

            }
            if (!swap) {
                break ordered;
            }
        }

        return tmpList;

    }

    /**
     * <p>Returns a List of symbols found within the <code>symbolToken</code>.
     * The comprehensive symbol table is found in the {@link ClayContext}.</p>
     *
     * @param context commons chains
     * @param symbolToken target token having nested symbols
     * @return list of nested symbols withing the target symbolToken
     */
    private static List findSymbols(ClayContext context, String symbolToken) {

        List targetList = new ArrayList();

        StringBuffer buff = new StringBuffer(symbolToken);
        Map symbols = context.getSymbols();
        Iterator si = symbols.entrySet().iterator();
        boolean wasSymbolFound = false;
        int i = buff.indexOf("@");
        replace: while (i > -1 && si.hasNext()) {
            Map.Entry e = (Map.Entry) si.next();
            SymbolBean symbol = (SymbolBean) e.getValue();
            String key = symbol.getName();
            i = (wasSymbolFound ? buff.indexOf("@") : i);
            if (i == -1) {
                break replace;
            }

            next: while (i > -1 && i <= (buff.length() - key.length())) {

                int n = -1;
                indexOf: for (int s = i; s <= (buff.length() - key.length()); s++) {
                    for (int c = 0; c < key.length(); c++) {
                        char skey = Character.toLowerCase(key.charAt(c));
                        char tkey = Character.toLowerCase(buff.charAt(s + c));
                        if (skey != tkey) {
                            continue indexOf;
                        }
                    }
                    // match found
                    n = s;
                    break indexOf;
                }

                if (n > -1) {
                    if (!targetList.contains(key)) {
                        targetList.add(key);
                    }
                    i = n + key.length();
                    wasSymbolFound = true;
                } else {
                    break next;
                }
            }

            e = null;
            key = null;
        }
        symbols = null;
        si = null;

        return targetList;
    }

    /**
     * <p>
     * This call is used to substitue an attribute binding expression containing
     * the <code>symbols</code> within the <code>sybmolToken</code>.
     * </p>
     *
     * @param context commons chains holding the substitution symbols
     * @param symbolToken target token having nested symbols
     * @return value with the symbols replaced
     */
    public static String replaceMnemonic(ClayContext context, String symbolToken) {

        StringBuffer buff = new StringBuffer(symbolToken);
        Map symbols = context.getSymbols();
        Iterator si = symbols.entrySet().iterator();
        boolean wasReplacementMade = false;
        int i = buff.indexOf("@");
        replace: while (i > -1 && si.hasNext()) {
            Map.Entry e = (Map.Entry) si.next();
            SymbolBean symbol = (SymbolBean) e.getValue();
            String key = symbol.getName();
            String value = (symbol.getValue() == null ? "" : symbol.getValue());
            i = (wasReplacementMade ? buff.indexOf("@") : i);
            if (i == -1) {
              break replace;
            }

            next: while (i > -1 && i <= (buff.length() - key.length())) {

                int n = -1;
                indexOf: for (int s = i; s <= (buff.length() - key.length()); s++) {
                    for (int c = 0; c < key.length(); c++) {
                        char skey = Character.toLowerCase(key.charAt(c));
                        char tkey = Character.toLowerCase(buff.charAt(s + c));
                        if (skey != tkey) {
                            continue indexOf;
                        }
                    }
                    // match found
                    n = s;
                    break indexOf;
                }

                if (n > -1) {
                    buff.delete(n, n + key.length());
                    buff.insert(n, value);
                    i = n + value.length();
                    wasReplacementMade = true;
                } else {
                    break next;
                }
            }

            e = null;
            key = null;
            value = null;
        }
        symbols = null;
        si = null;

        //if a replacement was made and the target length is zero,
        //return a null value; otherwise, an empty string.  Components
        //like the "selectItem" need to be able to set a empty string
        //value for the "itemLabel" or "itemValue" attributes.
        if (buff.length() == 0 && wasReplacementMade) {
           return null;
        } else {
           return buff.toString();
        }

    }

    /**
     * <p>
     * This method comes from the <code>Command</code> interfaces. This method is
     * invoked while executing the <code>Chain</code>.
     * </p>
     *
     * @param context commons chains
     * @return <code>true</code> if the chain is done
     * @exception Exception checked
     */
    public abstract boolean execute(Context context) throws Exception;

    /**
     * <p>Return true if the specified string contains an EL expression.</p>
     *
     * <p>This is taken almost verbatim from {@link javax.faces.webapp.UIComponentTag}
     * in order to remove JSP dependencies from the renderers.</p>
     *
     * @param value String to be checked for being an expression
     * @return <code>true</code> if the value is a binding expression
     */
    protected boolean isValueReference(String value) {

        if (value == null) {
            return false;
        }

        int start = value.indexOf("#{");
        if (start < 0) {
            return false;
        }

        int end = value.lastIndexOf('}');
        return (end >= 0) && (start < end);
    }

}

