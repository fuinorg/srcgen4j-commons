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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class with a name that is used as unique identifier. Equals and hash
 * code are also based on the name.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name" })
public abstract class AbstractNamedElement extends AbstractElement {

    @XmlAttribute
    private String name;

    /**
     * Default constructor.
     */
    public AbstractNamedElement() {
        super();
    }

    /**
     * Constructor with name, project and folder.
     * 
     * @param name
     *            Name to set.
     */
    public AbstractNamedElement(final String name) {
        super();
        this.name = name;
    }

    /**
     * Returns the name.
     * 
     * @return Current name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            Name to set.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractNamedElement other = (AbstractNamedElement) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    // CHECKSTYLE:ON

}
