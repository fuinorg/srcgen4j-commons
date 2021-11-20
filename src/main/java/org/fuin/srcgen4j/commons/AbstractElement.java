/**
 * Copyright (C) 2015 Michael Schnell. All rights reserved. 
 * http://www.fuin.org/
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
 * along with this library. If not, see http://www.gnu.org/licenses/.
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
import javax.xml.bind.annotation.XmlTransient;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.utils4j.Utils4J;
import org.fuin.utils4j.VariableResolver;

/**
 * Basic functionality used by all configuration sub classes.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractElement {

    @Nullable
    @Valid
    @XmlElement(name = "variable")
    private List<Variable> variables;

    @XmlTransient
    private Map<String, String> varMap;

    /**
     * Derives all variables from a parent map plus the ones defined in the object itself.
     * 
     * @param parentVars
     *            Variables defined by the parent.
     */
    protected final void inheritVariables(@Nullable final Map<String, String> parentVars) {
        if (varMap == null) {
            varMap = new HashMap<>();
        }
        if (parentVars != null) {
            varMap.putAll(parentVars);
        }
        if (variables != null) {
            for (Variable variable : variables) {
                final Map<String, String> vars = varMap;
                varMap.put(variable.getName(), Utils4J.replaceVars(variable.getValue(), vars));
            }
            varMap = new VariableResolver(varMap).getResolved();
        }
    }

    /**
     * Returns the map of variables defined by this object and it's parents.
     * 
     * @return Unmodifiable map of variables.
     */
    @NotNull
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
            variables = new ArrayList<>();
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
