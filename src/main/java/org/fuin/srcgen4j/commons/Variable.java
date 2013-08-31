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
 * Represents a variable definition (name and value).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "variable")
@XmlType(propOrder = { "xpath", "value", "name" })
public class Variable extends AbstractElement implements
        InitializableElement<Variable, SrcGen4JConfig> {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String value;

    @XmlAttribute
    private String xpath;

    private transient SrcGen4JConfig parent;

    /**
     * Default constructor.
     */
    public Variable() {
        super();
    }

    /**
     * Constructor with name and value.
     * 
     * @param name
     *            Name to set.
     * @param value
     *            Value to set.
     */
    public Variable(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor with name, value and xpath.
     * 
     * @param name
     *            Name to set.
     * @param value
     *            Value to set.
     * @param xpath
     *            Value to set.
     */
    public Variable(final String name, final String value, final String xpath) {
        super();
        this.name = name;
        this.value = value;
        this.xpath = xpath;
    }

    /**
     * Returns the name of the variable.
     * 
     * @return Current name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the variable.
     * 
     * @param name
     *            Name to set.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the value of the variable.
     * 
     * @return Current value.
     */
    public final String getValue() {
        return value;
    }

    /**
     * Sets the value of the variable.
     * 
     * @param value
     *            Value to set.
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    /**
     * Returns the xpath of the variable.
     * 
     * @return Current xpath.
     */
    public final String getXpath() {
        return xpath;
    }

    /**
     * Sets the xpath of the variable.
     * 
     * @param xpath
     *            Xpath to set.
     */
    public final void setXpath(final String xpath) {
        this.xpath = xpath;
    }

    /**
     * Returns the parent of the variable.
     * 
     * @return Generator configuration.
     */
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    /**
     * Sets the parent of the variable.
     * 
     * @param parent
     *            Generator configuration.
     */
    public final void setParent(final SrcGen4JConfig parent) {
        this.parent = parent;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Variable other = (Variable) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public final Variable init(final SrcGen4JConfig parent, final Map<String, String> vars) {
        setValue(replaceVars(getValue(), vars));
        return this;
    }

    // CHECKSTYLE:ON

}
