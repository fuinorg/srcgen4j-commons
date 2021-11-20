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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A container for multiple variable definitions.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "variables")
public final class Variables {

    @NotEmpty
    @Valid
    @XmlElement(name = "variable")
    private List<Variable> list;

    /**
     * Package visible default constructor for deserialization.
     */
    Variables() {
        super();
    }

    /**
     * Constructor with array.
     * 
     * @param variables
     *            Array to set.
     */
    public Variables(@NotEmpty final Variable... variables) {
        super();
        Validate.notEmpty(variables);
        Validate.noNullElements(variables);
        this.list = Arrays.asList(variables);
    }

    /**
     * Constructor with collection.
     * 
     * @param variables
     *            Collection to set.
     */
    public Variables(@NotEmpty final Collection<Variable> variables) {
        super();
        Validate.notEmpty(variables);
        Validate.noNullElements(variables);
        this.list = new ArrayList<>(variables.size());
        this.list.addAll(variables);
    }

    /**
     * Replaces variables (if defined) in the value.
     * 
     * @param vars
     *            Variables to use.
     */
    public final void init(@Nullable final Map<String, String> vars) {
        for (Variable variable : list) {
            variable.init(vars);
        }
    }

    /**
     * Returns a list of variables.
     * 
     * @return Unmodifiable list.
     */
    @NotEmpty
    public final List<Variable> asList() {
        return Collections.unmodifiableList(list);
    }

    /**
     * Returns a map of key/values.
     * 
     * @return Unmodifiable map.
     */
    @NotEmpty
    public Map<String, String> asMap() {
        if (list == null) {
            return Collections.emptyMap();
        }
        final Map<String, String> map = new HashMap<>();
        for (final Variable var : list) {
            map.put(var.getName(), var.getValue());
        }
        return map;
    }

}
