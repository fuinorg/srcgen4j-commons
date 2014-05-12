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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.Nullable;

/**
 * Represents a folder in a target folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bin")
@XmlType(propOrder = "path")
public final class BinClasspathEntry extends AbstractElement implements
        InitializableElement<BinClasspathEntry, Classpath> {

    @NotNull
    @XmlAttribute
    private String path;

    @Nullable
    private transient Classpath parent;

    /**
     * Package visible default constructor for deserialization.
     */
    BinClasspathEntry() {
        super();
    }

    /**
     * Constructor with path.
     * 
     * @param path
     *            Path to set.
     */
    public BinClasspathEntry(@NotNull final String path) {
        super();
        this.path = path;
    }

    /**
     * Returns the path.
     * 
     * @return Current path.
     */
    @NeverNull
    public final String getPath() {
        return path;
    }

    /**
     * Returns the parent for the folder.
     * 
     * @return Parent.
     */
    @Nullable
    public final Classpath getParent() {
        return parent;
    }

    /**
     * Sets the parent for the folder.
     * 
     * @param parent
     *            Parent.
     */
    public final void setParent(@Nullable final Classpath parent) {
        this.parent = parent;
    }

    @Override
    public final BinClasspathEntry init(final SrcGen4JContext context,
            final Classpath parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        path = replaceVars(path, getVarMap());
        return this;
    }

}
