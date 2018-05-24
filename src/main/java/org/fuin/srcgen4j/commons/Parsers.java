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
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.xmlcfg4j.AbstractElement;

/**
 * Represents a set of code parsers.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parsers")
@XmlType(propOrder = { "list" })
public class Parsers extends AbstractElement implements InitializableElement<Parsers, SrcGen4JConfig> {

    @Nullable
    @Valid
    @XmlElement(name = "parser")
    private List<ParserConfig> list;

    @Nullable
    private transient SrcGen4JConfig parent;

    /**
     * Returns the list of parsers.
     * 
     * @return Parsers.
     */
    @Nullable
    public final List<ParserConfig> getList() {
        return list;
    }

    /**
     * Sets the list of parsers.
     * 
     * @param list
     *            Parsers.
     */
    public final void setList(@Nullable final List<ParserConfig> list) {
        this.list = list;
    }

    /**
     * Adds a parser to the list. If the list does not exist it's created.
     * 
     * @param parser
     *            Parser to add.
     */
    public final void addParser(@NotNull final ParserConfig parser) {
        Contract.requireArgNotNull("parser", parser);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(parser);
    }

    /**
     * Returns the parent of the object.
     * 
     * @return ParserConfig.
     */
    @Nullable
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    @Override
    public final Parsers init(final SrcGen4JContext context, final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        if (list != null) {
            for (final ParserConfig parser : list) {
                parser.init(context, this, getVarMap());
            }
        }
        return this;
    }

}
