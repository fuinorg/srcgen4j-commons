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

import static org.fuin.utils4j.Utils4J.replaceVars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.utils4j.Utils4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a code generator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generator")
@XmlType(propOrder = { "config", "artifacts", "parser", "className" })
public final class GeneratorConfig extends AbstractNamedTarget implements InitializableElement<GeneratorConfig, Generators> {

    private static final Logger LOG = LoggerFactory.getLogger(GeneratorConfig.class);

    @NotNull
    @XmlAttribute(name = "class")
    private String className;

    @NotNull
    @XmlAttribute(name = "parser")
    private String parser;

    @Nullable
    @Valid
    @XmlElement(name = "artifact")
    private List<Artifact> artifacts;

    @Nullable
    @Valid
    @XmlElement(name = "config")
    private Config<GeneratorConfig> config;

    @Nullable
    private transient SrcGen4JContext context;

    @Nullable
    private transient Generators parent;

    @Nullable
    private transient Generator<Object> generator;

    /**
     * Package visible default constructor for deserialization.
     */
    GeneratorConfig() { // NOSONAR Ignore not initialized fields
        super();
    }

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name to set.
     * @param className
     *            Full qualified name of the generator class to set.
     * @param parser
     *            Unique name of the parser that delivers the input for this generator to set.
     */
    public GeneratorConfig(@NotEmpty final String name, @NotEmpty final String className, @NotEmpty final String parser) {
        this(name, className, parser, null, null);
    }

    /**
     * Constructor with name, project and folder.
     * 
     * @param name
     *            Name to set.
     * @param className
     *            Full qualified name of the generator class to set.
     * @param parser
     *            Unique name of the parser that delivers the input for this generator to set.
     * @param project
     *            Project to set.
     * @param folder
     *            Folder to set.
     */
    public GeneratorConfig(@NotEmpty final String name, @NotEmpty final String className, @NotEmpty final String parser, // NOSONAR
            @Nullable final String project, @Nullable final String folder) {
        super(name, project, folder);
        Contract.requireArgNotNull("className", className);
        Contract.requireArgNotNull("parser", parser);
        this.className = className;
        this.parser = parser;
    }

    /**
     * Returns the name of the generator class.
     * 
     * @return Full qualified class name.
     */
    @NotEmpty
    public final String getClassName() {
        return className;
    }

    /**
     * Returns the name of the parser that delivers the input for this generator.
     * 
     * @return Unique parser name.
     */
    @NotEmpty
    public final String getParser() {
        return parser;
    }

    /**
     * Returns the list of artifacts.
     * 
     * @return Artifacts.
     */
    @Nullable
    public final List<Artifact> getArtifacts() {
        return artifacts;
    }

    /**
     * Sets the list of artifacts.
     * 
     * @param artifacts
     *            Artifacts.
     */
    public final void setArtifacts(@Nullable final List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * Adds a artifact to the list. If the list does not exist it's created.
     * 
     * @param artifact
     *            Artifact to add.
     */
    public final void addArtifact(@NotNull final Artifact artifact) {
        Contract.requireArgNotNull("artifact", artifact);
        if (artifacts == null) {
            artifacts = new ArrayList<Artifact>();
        }
        artifacts.add(artifact);
    }

    /**
     * Returns the generator specific configuration.
     * 
     * @return Configuration for the parser.
     */
    @Nullable
    public final Config<GeneratorConfig> getConfig() {
        return config;
    }

    /**
     * Sets the generator specific configuration.
     * 
     * @param config
     *            Configuration for the parser.
     */
    public final void setConfig(@Nullable final Config<GeneratorConfig> config) {
        this.config = config;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return Generators.
     */
    @Nullable
    public final Generators getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            Generators.
     */
    public final void setParent(@Nullable final Generators parent) {
        this.parent = parent;
    }

    /**
     * Returns the defined project from this object or any of it's parents.
     * 
     * @return Project.
     */
    @Nullable
    public final String getDefProject() {
        if (getProject() == null) {
            if (parent == null) {
                return null;
            }
            return parent.getProject();
        }
        return getProject();
    }

    /**
     * Returns the defined folder from this object or any of it's parents.
     * 
     * @return Folder.
     */
    @Nullable
    public final String getDefFolder() {
        if (getFolder() == null) {
            if (parent == null) {
                return null;
            }
            return parent.getFolder();
        }
        return getFolder();
    }

    @Override
    public final GeneratorConfig init(final SrcGen4JContext context, final Generators parent, final Map<String, String> vars) {
        this.context = context;
        this.parent = parent;
        inheritVariables(vars);
        setName(replaceVars(getName(), getVarMap()));
        setProject(replaceVars(getProject(), getVarMap()));
        setFolder(replaceVars(getFolder(), getVarMap()));
        if (artifacts != null) {
            for (final Artifact artifact : artifacts) {
                artifact.init(context, this, getVarMap());
            }
        }
        if (config != null) {
            config.init(context, this, getVarMap());
        }
        return this;
    }

    /**
     * Returns an existing generator instance or creates a new one if it's the first call to this method.
     * 
     * @return Generator of type {@link #className}.
     */
    @SuppressWarnings("unchecked")
    public final Generator<Object> getGenerator() {
        if (generator != null) {
            return generator;
        }
        LOG.info("Creating generator: " + className);
        if (className == null) {
            throw new IllegalStateException("Class name was not set: " + getName());
        }
        if (context == null) {
            throw new IllegalStateException("Context class loader was not set: " + getName() + " / " + className);
        }
        final Object obj = Utils4J.createInstance(className, context.getClassLoader());
        if (!(obj instanceof Generator<?>)) {
            throw new IllegalStateException("Expected class to be of type '" + Generator.class.getName() + "', but was: " + className);
        }
        generator = (Generator<Object>) obj;
        generator.initialize(this);
        return generator;
    }

    /**
     * Returns the appropriate folder for a given artifact.
     * 
     * @param artifactName
     *            Unique name of the artifact to return a target folder for.
     * 
     * @return Target folder.
     */
    public final Folder findTargetFolder(final String artifactName) {
        if (parent == null) {
            throw new IllegalStateException("Parent for generator config is not set: " + getName());
        }
        return parent.findTargetFolder(getName(), artifactName);
    }

    /**
     * Returns the context the configuration belongs to.
     * 
     * @return Current context.
     */
    public final SrcGen4JContext getContext() {
        return context;
    }

}
