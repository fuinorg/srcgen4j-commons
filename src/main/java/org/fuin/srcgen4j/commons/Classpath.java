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
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.Nullable;

/**
 * Represents a class path.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "classpath")
@XmlType(propOrder = { "binList" })
public final class Classpath extends AbstractElement implements
        InitializableElement<Classpath, SrcGen4JConfig> {

    @Nullable
    @XmlAttribute(name = "context")
    private Boolean addContextCP;

    @Nullable
    @Valid
    @XmlElement(name = "bin")
    private List<BinClasspathEntry> binList;

    @Nullable
    private transient SrcGen4JConfig parent;

    /**
     * Package visible default constructor for deserialization.
     */
    Classpath() {
        super();
    }

    /**
     * Returns the information if the class path from the context should be
     * added.
     * 
     * @return If the context's class path should be added TRUE, else FALSE.
     */
    @Nullable
    public final Boolean getAddContextCP() {
        return addContextCP;
    }

    /**
     * Sets the information if the class path from the context should be added.
     * 
     * @param addContextCP
     *            If the context's class path should be added TRUE, else FALSE.
     */
    public final void setAddContextCP(@Nullable final Boolean addContextCP) {
        this.addContextCP = addContextCP;
    }

    /**
     * Returns the information the information if the class path from the
     * context should be added. If the information is not set (NULL) the value
     * defaults to TRUE.
     * 
     * @return If the context's class path should be added TRUE, else FALSE.
     */
    public final boolean isAddContextCP() {
        if (addContextCP == null) {
            return true;
        }
        return addContextCP;
    }

    /**
     * Returns the list of binary directories.
     * 
     * @return List.
     */
    @Nullable
    public final List<BinClasspathEntry> getBinList() {
        return binList;
    }

    /**
     * Sets the list of binary directories.
     * 
     * @param list
     *            List.
     */
    public final void setBinList(@Nullable final List<BinClasspathEntry> list) {
        this.binList = list;
    }

    /**
     * Adds a classes directory to the list. If the list does not exist it's
     * created.
     * 
     * @param entry
     *            Binaries directory to add.
     */
    public final void addBin(@NotNull final BinClasspathEntry entry) {
        Contract.requireArgNotNull("entry", entry);
        if (binList == null) {
            binList = new ArrayList<BinClasspathEntry>();
        }
        binList.add(entry);
    }

    /**
     * Returns the parent of the object.
     * 
     * @return BinClasspathEntry.
     */
    @Nullable
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    @Override
    public final Classpath init(final SrcGen4JContext context, final SrcGen4JConfig parent,
            final Map<String, String> vars) {
        this.parent = parent;
        if (binList != null) {
            for (final BinClasspathEntry bin : binList) {
                bin.init(context, this, vars);
            }
        }
        return this;
    }

}
