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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a class path.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "classpath")
@XmlType(propOrder = { "binList" })
public class Classpath extends AbstractElement implements
        InitializableElement<Classpath, SrcGen4JConfig> {

    @XmlElement(name = "bin")
    private List<BinClasspathEntry> binList;

    private transient SrcGen4JConfig parent;

    /**
     * Default constructor.
     */
    public Classpath() {
        super();
    }

    /**
     * Returns the list of binary directories.
     * 
     * @return List or NULL.
     */
    public final List<BinClasspathEntry> getBinList() {
        return binList;
    }

    /**
     * Sets the list of binary directories.
     * 
     * @param list
     *            List or NULL.
     */
    public final void setBinList(final List<BinClasspathEntry> list) {
        this.binList = list;
    }

    /**
     * Adds a classes directory to the list. If the list does not exist it's
     * created.
     * 
     * @param entry
     *            Binaries directory to add - Cannot be NULL.
     */
    public final void addBin(final BinClasspathEntry entry) {
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
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            BinClasspathEntry.
     */
    public final void setParent(final SrcGen4JConfig parent) {
        this.parent = parent;
    }

    @Override
    public final Classpath init(final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        if (binList != null) {
            for (final BinClasspathEntry bin : binList) {
                bin.init(this, vars);
            }
        }
        return this;
    }

}
