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

import org.apache.commons.lang3.Validate;

/**
 * Base class with a name that is used as unique identifier. Equals and hash code are also based on the name and the type of the class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractNamedElement extends AbstractElement {

    @NotEmpty
    @XmlAttribute
    private String name;

    /**
     * Package visible default constructor for deserialization.
     */
    protected AbstractNamedElement() {
        super();
    }

    /**
     * Constructor with name, project and folder.
     * 
     * @param name
     *            Name to set.
     */
    public AbstractNamedElement(@NotEmpty final String name) {
        super();
        Validate.notEmpty(name);
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
        Validate.notEmpty(name);
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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractNamedElement other = (AbstractNamedElement) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
