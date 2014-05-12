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

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.Nullable;
import org.fuin.utils4j.Utils4J;

/**
 * Configuration for a {@link ArtifactFactory}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "artifact-factory")
@XmlType(propOrder = { "factoryClassName", "artifact" })
public final class ArtifactFactoryConfig extends AbstractElement {

    @NotNull
    @XmlAttribute(name = "artifact")
    private String artifact;

    @NotNull
    @XmlAttribute(name = "class")
    private String factoryClassName;

    @Nullable
    @XmlAttribute(name = "incremental")
    private Boolean incremental;

    @Nullable
    private transient SrcGen4JContext context;

    @Nullable
    private transient ArtifactFactory<?> factory;

    /**
     * Package visible default constructor for deserialization.
     */
    ArtifactFactoryConfig() {
        super();
    }

    /**
     * Constructor without incremental value.
     * 
     * @param artifact
     *            Artifact.
     * @param factoryClassName
     *            Full qualified factory class name.
     */
    public ArtifactFactoryConfig(@NotNull final String artifact,
            @NotNull final String factoryClassName) {
        this(artifact, factoryClassName, null);
    }

    /**
     * Constructor with all data.
     * 
     * @param artifact
     *            Artifact.
     * @param factoryClassName
     *            Full qualified factory class name.
     * @param incremental
     *            If the factory executes on an incremental build TRUE
     *            (default), else FALSE.
     */
    public ArtifactFactoryConfig(@NotNull final String artifact,
            @NotNull final String factoryClassName,
            @Nullable final Boolean incremental) {
        super();
        this.artifact = artifact;
        this.factoryClassName = factoryClassName;
    }

    /**
     * Returns the name of the generated artifact.
     * 
     * @return Unique artifact name.
     */
    @NeverNull
    public final String getArtifact() {
        return artifact;
    }

    /**
     * Returns the name of the factory class.
     * 
     * @return Full qualified class name.
     */
    @NeverNull
    public final String getFactoryClassName() {
        return factoryClassName;
    }

    /**
     * Returns the information if the factory should be called during an
     * incremental build.
     * 
     * @return If the factory executes on an incremental build TRUE, else FALSE.
     */
    @Nullable
    public final Boolean getIncremental() {
        return incremental;
    }

    /**
     * Sets the information if the factory should be called during an
     * incremental build.
     * 
     * @param incremental
     *            If the factory executes on an incremental build TRUE, else
     *            FALSE.
     */
    public final void setIncremental(@Nullable final Boolean incremental) {
        this.incremental = incremental;
    }

    /**
     * Returns the information if the factory should be called during an
     * incremental build. If the information is not set (NULL) the value
     * defaults to TRUE.
     * 
     * @return If the factory executes on an incremental build TRUE (default),
     *         else FALSE.
     */
    public final boolean isIncremental() {
        if (incremental == null) {
            return true;
        }
        return incremental;
    }

    /**
     * Returns the factory instance. If it does not exist, it will be created.
     * Requires that {@link #init(SrcGen4JContext)} was called once before.
     * 
     * @return Factory.
     */
    @NeverNull
    public final ArtifactFactory<?> getFactory() {
        if (factory == null) {
            if (factoryClassName == null) {
                throw new IllegalStateException(
                        "Factory class name was not set: " + artifact);
            }
            if (context == null) {
                throw new IllegalStateException(
                        "Context class loader was not set: " + artifact);
            }
            final Object obj = Utils4J.createInstance(factoryClassName,
                    context.getClassLoader());
            if (!ArtifactFactory.class.isAssignableFrom(obj.getClass())) {
                throw new IllegalStateException("Expected an object of type '"
                        + ArtifactFactory.class.getName() + "', but was: "
                        + obj.getClass());
            }
            factory = (ArtifactFactory<?>) obj;
            factory.init(this);
        }
        return factory;
    }

    /**
     * Initializes the object.
     * 
     * @param context
     *            Current context.
     * @param vars
     *            Variables from all parents.
     * 
     * @return This instance.
     */
    @NeverNull
    public final ArtifactFactoryConfig init(
            @NotNull final SrcGen4JContext context,
            final Map<String, String> vars) {
        Contract.requireArgNotNull("context", context);
        this.context = context;
        inheritVariables(vars);
        return this;
    }

}
