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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.utils4j.Utils4J;

/**
 * Configuration for a {@link ArtifactFactory}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "artifact-factory")
@XmlType(propOrder = { "factoryClassName", "artifact" })
public class ArtifactFactoryConfig extends AbstractElement {

    @XmlAttribute(name = "artifact")
    private String artifact;

    @XmlAttribute(name = "class")
    private String factoryClassName;

    @XmlAttribute(name = "incremental")
    private Boolean incremental;

    private transient SrcGen4JContext context;

    private transient ArtifactFactory<?> factory;

    /**
     * Default constructor.
     */
    public ArtifactFactoryConfig() {
        super();
    }

    /**
     * Constructor with all data.
     * 
     * @param artifact
     *            Artifact.
     * @param factoryClassName
     *            Full qualified factory class name.
     */
    public ArtifactFactoryConfig(final String artifact, final String factoryClassName) {
        super();
        this.artifact = artifact;
        this.factoryClassName = factoryClassName;
    }

    /**
     * Returns the name of the generated artifact.
     * 
     * @return Unique artifact name.
     */
    public final String getArtifact() {
        return artifact;
    }

    /**
     * Sets the name of the generated artifact.
     * 
     * @param artifact
     *            Unique artifact name.
     */
    public final void setArtifact(final String artifact) {
        this.artifact = artifact;
    }

    /**
     * Returns the name of the factory class.
     * 
     * @return Full qualified class name.
     */
    public final String getFactoryClassName() {
        return factoryClassName;
    }

    /**
     * Sets the name of the factory class.
     * 
     * @param factoryClassName
     *            Full qualified class name.
     */
    public final void setFactoryClassName(final String factoryClassName) {
        this.factoryClassName = factoryClassName;
    }

    /**
     * Returns the information if the factory should be called during an
     * incremental build.
     * 
     * @return If the factory executes on an incremental build TRUE, else FALSE.
     */
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
    public final void setIncremental(final Boolean incremental) {
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
     * 
     * @return Factory.
     */
    public final ArtifactFactory<?> getFactory() {
        if (factory == null) {
            if (factoryClassName == null) {
                return null;
            }
            final Object obj = Utils4J.createInstance(factoryClassName, context.getClassLoader());
            if (!ArtifactFactory.class.isAssignableFrom(obj.getClass())) {
                throw new IllegalStateException("Expected an object of type '"
                        + ArtifactFactory.class.getName() + "', but was: " + obj.getClass());
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
     * 
     * @return This instance.
     */
    public final ArtifactFactoryConfig init(final SrcGen4JContext context) {
        this.context = context;
        return this;
    }

}
