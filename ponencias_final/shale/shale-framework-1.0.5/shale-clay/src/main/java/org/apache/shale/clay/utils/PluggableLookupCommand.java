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
 * $Id: PluggableLookupCommand.java 473876 2006-11-12 04:50:13Z gvanmatre $
 */
package org.apache.shale.clay.utils;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.CatalogFactory;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.LookupCommand;

/**
 * <p>This chains class performs a check to verify the catalog exists.
 * If the catalog doesn't exist, it returns <code>true</code> if the
 * <code>optional</code> property in the super class is <code>true</code>.
 * If the catalog is not found and the command is not optional, it returns a
 * <code>false</code> value.  Otherwise, if the catalog exists, the super
 * implementation is invoked.  The name of the command can be a value
 * binding expression.  The value is evaluated if it contains an expression
 * and the resolved command is invoked.</p>
 *
 */
public class PluggableLookupCommand extends LookupCommand {


    /**
     * <p>Checks to see if the catalog exists.</p>
     *
     * @return <code>true</code> if the catalog exists
     */
    private boolean catalogExists() {
        String catalogName = getCatalogName();
        if (catalogName == null) {
            return false;
        }
        CatalogFactory catalogFactory = CatalogFactory.getInstance();

        return (catalogFactory.getCatalog(catalogName) != null);
    }

    /**
     * @return <code>Catalog</code> for the <code>catalogName</code>
     */
    private Catalog getCatalog() {
        CatalogFactory catalogFactory = CatalogFactory.getInstance();
        return catalogFactory.getCatalog(getCatalogName());
    }

    /**
     * <p>Adds an additional check to determine if the catalog name is loaded.
     * If loaded, the super implementation is invoked.  Otherwise, the chain
     * continues if the command is optional.</p>
     *
     * @param context chains context
     * @return <code>true</code> if the chain is done
     * @exception Exception up the calling chain
     */
    public boolean execute(Context context) throws Exception {

        if (!catalogExists()) {
           return !isOptional();
        }

        if (isValueReference(getName())) {
            // command is a binding expression; evaluate and the find the target command
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ValueBinding vb = facesContext.getApplication().createValueBinding(getName());
            String targetCommand = (String) vb.getValue(facesContext);

            if (targetCommand != null) {
               Command command = getCatalog().getCommand(targetCommand);
               if (command != null) {
                   return command.execute(context);
               }
            }
        }
        return super.execute(context);
    }

    /**
     * @param value the command name to test for a binding expression
     * @return <code>true</code> if the <code>value</code> is a binding expression
     */
    private boolean isValueReference(String value) {
        if (value == null) {
            return false;
        }

        if ((value.indexOf("#{") > -1)
                && (value.indexOf("}") > -1)) {
            return true;
        }

        return false;
    }

}
