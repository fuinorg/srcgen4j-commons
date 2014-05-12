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

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverEmpty;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.NotEmpty;
import org.fuin.objects4j.common.Nullable;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a model parser.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parser")
@XmlType(propOrder = { "config", "className" })
public class ParserConfig extends AbstractNamedElement implements
	InitializableElement<ParserConfig, Parsers> {

    private static final Logger LOG = LoggerFactory
	    .getLogger(ParserConfig.class);

    @NotEmpty
    @XmlAttribute(name = "class")
    private String className;

    @Nullable
    @Valid
    @XmlElement(name = "config")
    private Config<ParserConfig> config;

    @Nullable
    private transient SrcGen4JContext context;

    @Nullable
    private transient Parsers parent;

    @Nullable
    private transient Parser<Object> parser;

    /**
     * Package visible default constructor for deserialization.
     */
    ParserConfig() {
	super();
    }

    /**
     * Constructor with name and class.
     * 
     * @param name
     *            Name to set.
     * @param className
     *            Full qualified name of the class to set.
     */
    public ParserConfig(@NotEmpty final String name,
	    @NotEmpty final String className) {
	super(name);
	Contract.requireArgNotEmpty("className", className);
	this.className = className;
    }

    /**
     * Returns the name of the parser class.
     * 
     * @return Full qualified name.
     */
    @NeverEmpty
    public final String getClassName() {
	return className;
    }

    /**
     * Returns the parser specific configuration.
     * 
     * @return Configuration for the parser.
     */
    @Nullable
    public final Config<ParserConfig> getConfig() {
	return config;
    }

    /**
     * Sets the parser specific configuration.
     * 
     * @param config
     *            Configuration for the parser.
     */
    public final void setConfig(@Nullable final Config<ParserConfig> config) {
	this.config = config;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return GeneratorConfig.
     */
    @Nullable
    public final Parsers getParent() {
	return parent;
    }

    @Override
    public final ParserConfig init(final SrcGen4JContext context,
	    final Parsers parent, final Map<String, String> vars) {
	this.context = context;
	this.parent = parent;
	inheritVariables(vars);
	setName(replaceVars(getName(), getVarMap()));
	this.className = replaceVars(className, getVarMap());
	if (config != null) {
	    config.init(context, this, getVarMap());
	}
	return this;
    }

    /**
     * Returns an existing parser instance or creates a new one if it's the
     * first call to this method.
     * 
     * @return Parser of type {@link #className}.
     */
    @SuppressWarnings("unchecked")
    @NeverNull
    public final Parser<Object> getParser() {
	if (parser != null) {
	    return parser;
	}
	LOG.info("Creating parser: " + className);
	if (className == null) {
	    throw new IllegalStateException("Class name was not set: "
		    + getName());
	}
	if (context == null) {
	    throw new IllegalStateException(
		    "Context class loader was not set: " + getName() + " / "
			    + className);
	}
	final Object obj = Utils4J.createInstance(className,
		context.getClassLoader());
	if (!(obj instanceof Parser<?>)) {
	    throw new IllegalStateException("Expected class to be of type '"
		    + Parser.class.getName() + "', but was: " + className);
	}
	parser = (Parser<Object>) obj;
	parser.initialize(context, this);
	return parser;
    }

    /**
     * Returns the context the configuration belongs to.
     * 
     * @return Current context.
     */
    @Nullable
    public final SrcGen4JContext getContext() {
	return context;
    }

}
