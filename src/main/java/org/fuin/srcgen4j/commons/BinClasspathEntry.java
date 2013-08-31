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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a folder in a target folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bin")
@XmlType(propOrder = "path")
public class BinClasspathEntry extends AbstractElement implements
        InitializableElement<BinClasspathEntry, Classpath> {

    @XmlAttribute
    private String path;

    private transient Classpath parent;

    /**
     * Default constructor.
     */
    public BinClasspathEntry() {
        super();
    }

    /**
     * Constructor with path.
     * 
     * @param path
     *            Path to set.
     */
    public BinClasspathEntry(final String path) {
        super();
        this.path = path;
    }

    /**
     * Returns the path.
     * 
     * @return Current path.
     */
    public final String getPath() {
        return path;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            Path to set.
     */
    public final void setPath(final String path) {
        this.path = path;
    }

    /**
     * Returns the parent for the folder.
     * 
     * @return Parent or <code>null</code>.
     */
    public final Classpath getParent() {
        return parent;
    }

    /**
     * Sets the parent for the folder.
     * 
     * @param parent
     *            Parent or <code>null</code>.
     */
    public final void setParent(final Classpath parent) {
        this.parent = parent;
    }

    @Override
    public final BinClasspathEntry init(final Classpath parent, final Map<String, String> vars) {
        this.parent = parent;
        path = replaceVars(path, vars);
        return this;
    }

}
