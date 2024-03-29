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

import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.objects4j.common.Contract;

/**
 * Base class for assigning generated artifacts to a project folder and a name as unique identifier.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractNamedTarget extends AbstractTarget {

    @NotEmpty
    @XmlAttribute
    private String name;

    /**
     * Package visible default constructor for deserialization.
     */
    AbstractNamedTarget() {
        super();
    }

    /**
     * Constructor with name, project and folder.
     * 
     * @param name
     *            Name to set.
     * @param project
     *            Project to set.
     * @param folder
     *            Folder to set.
     */
    public AbstractNamedTarget(@NotEmpty final String name, @Nullable final String project, @Nullable final String folder) {
        super(project, folder);
        Contract.requireArgNotEmpty("name", name);
        this.name = name;
    }

    /**
     * Returns the name.
     * 
     * @return Current name.
     */
    @NotEmpty
    public final String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            Name to set.
     */
    protected final void setName(@NotEmpty final String name) {
        Contract.requireArgNotEmpty("name", name);
        this.name = name;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractNamedTarget other = (AbstractNamedTarget) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
