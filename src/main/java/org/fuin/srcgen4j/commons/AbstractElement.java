/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.srcgen4j.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.Nullable;

/**
 * Basic functionality used by all configuration sub classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractElement {

    @Nullable
    @Valid
    @XmlElement(name = "variable")
    private List<Variable> variables;

    private transient Map<String, String> varMap;

    /**
     * Replaces all variables inside a string with values from a map. A
     * <code>null</code> input string value will return <code>null</code>.
     * 
     * @param str
     *            Text with variables (Format: ${key} ).
     * @param vars
     *            Map with key/values (both of type <code>String</code>.
     * 
     * @return String with replaced variables. Unknown variables will remain
     *         unchanged.
     */
    @Nullable
    public final String replaceVars(@Nullable final String str,
            @Nullable final Map<String, String> vars) {
        return VariableResolver.replaceVars(str, vars);
    }

    /**
     * Derives all variables from a parent map plus the ones defined in the
     * object itself.
     * 
     * @param parentVars
     *            Variables defined by the parent.
     */
    @NeverNull
    protected final void inheritVariables(
            @Nullable final Map<String, String> parentVars) {
        if (varMap == null) {
            varMap = new HashMap<String, String>();
        }
        if (parentVars != null) {
            varMap.putAll(parentVars);
        }
        if (variables != null) {
            for (Variable variable : variables) {
                varMap.put(variable.getName(),
                        replaceVars(variable.getValue(), varMap));
            }
        }
    }

    /**
     * Returns the map of variables defined by this object and it's parents.
     * 
     * @return Unmodifiable map of variables.
     */
    @NeverNull
    public final Map<String, String> getVarMap() {
        if (varMap == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(varMap);
    }

    /**
     * Adds a variable to the element.
     * 
     * @param variable
     *            Variable to add.
     */
    public final void addVariable(@NotNull final Variable variable) {
        if (variables == null) {
            variables = new ArrayList<Variable>();
        }
        variables.add(variable);
    }

    /**
     * Returns a list of variables.
     * 
     * @return Variables.
     */
    @Nullable
    public final List<Variable> getVariables() {
        return variables;
    }

}
