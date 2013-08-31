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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a model parser.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parser")
@XmlType(propOrder = { "config", "className" })
public class ParserConfig extends AbstractNamedElement implements
        InitializableElement<ParserConfig, SrcGen4JConfig> {

    @XmlAttribute(name = "class")
    private String className;

    @XmlAnyElement(lax = true)
    private Object config;

    private transient SrcGen4JConfig parent;

    /**
     * Default constructor.
     */
    public ParserConfig() {
        super();
    }

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name to set.
     */
    public ParserConfig(final String name) {
        super(name);
    }

    /**
     * Constructor with name and class.
     * 
     * @param name
     *            Name to set.
     * @param className
     *            Full qualified name of the class to set.
     */
    public ParserConfig(final String name, final String className) {
        super(name);
        this.className = className;
    }

    /**
     * Returns the name of the parser class.
     * 
     * @return Full qualified name.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Sets the name of the parser class.
     * 
     * @param className
     *            Full qualified name.
     */
    public final void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Returns the parser specific configuration.
     * 
     * @return Configuration for the parser.
     */
    public final Object getConfig() {
        return config;
    }

    /**
     * Sets the parser specific configuration.
     * 
     * @param config
     *            Configuration for the parser.
     */
    public final void setConfig(final Object config) {
        this.config = config;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return GeneratorConfig.
     */
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            GeneratorConfig.
     */
    public final void setParent(final SrcGen4JConfig parent) {
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ParserConfig init(final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        setName(replaceVars(getName(), vars));
        setClassName(replaceVars(getClassName(), vars));
        if (config instanceof InitializableElement) {
            final InitializableElement<?, ParserConfig> ie = (InitializableElement<?, ParserConfig>) config;
            ie.init(this, vars);
        }
        return this;
    }

}