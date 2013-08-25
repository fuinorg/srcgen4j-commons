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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a code generator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generator")
@XmlType(propOrder = { "artifacts", "parser", "className" })
public class Generator extends AbstractNamedTarget {

    @XmlAttribute(name = "class")
    private String className;

    @XmlAttribute(name = "parser")
    private String parser;

    @XmlElement(name = "artifact")
    private List<Artifact> artifacts;

    @XmlTransient
    private Generators parent;

    /**
     * Default constructor.
     */
    public Generator() {
        super();
    }

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name to set.
     */
    public Generator(final String name) {
        super(name, null, null);
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
    public Generator(final String name, final String project, final String folder) {
        super(name, project, folder);
    }

    /**
     * Returns the name of the generator class.
     * 
     * @return Full qualified class name.
     */
    public final String getClassName() {
        return className;
    }

    /**
     * Sets the name of the generator class.
     * 
     * @param className
     *            Full qualified class name.
     */
    public final void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Returns the name of the parser that delivers the input for this
     * generator.
     * 
     * @return Unique parser name.
     */
    public final String getParser() {
        return parser;
    }

    /**
     * Sets the name of the parser that delivers the input for this generator.
     * 
     * @param parser
     *            Unique parser name.
     */
    public final void setParser(final String parser) {
        this.parser = parser;
    }

    /**
     * Returns the list of artifacts.
     * 
     * @return Artifacts or NULL.
     */
    public final List<Artifact> getArtifacts() {
        return artifacts;
    }

    /**
     * Sets the list of artifacts.
     * 
     * @param artifacts
     *            Artifacts or NULL.
     */
    public final void setArtifacts(final List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * Adds a artifact to the list. If the list does not exist it's created.
     * 
     * @param artifact
     *            Artifact to add - Cannot be NULL.
     */
    public final void addArtifact(final Artifact artifact) {
        if (artifacts == null) {
            artifacts = new ArrayList<Artifact>();
        }
        artifacts.add(artifact);
    }

    /**
     * Returns the parent of the object.
     * 
     * @return Generators.
     */
    public final Generators getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            Generators.
     */
    public final void setParent(final Generators parent) {
        this.parent = parent;
    }

    /**
     * Returns the defined project from this object or any of it's parents.
     * 
     * @return Project or <code>null</code>.
     */
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
     * @return Folder or <code>null</code>.
     */
    public final String getDefFolder() {
        if (getFolder() == null) {
            if (parent == null) {
                return null;
            }
            return parent.getFolder();
        }
        return getFolder();
    }

    /**
     * Initializes this object and it's childs.
     * 
     * @param parent
     *            Parent.
     * @param vars
     *            Variables to use.
     * 
     * @return This instance.
     */
    public final Generator init(final Generators parent, final Map<String, String> vars) {
        this.parent = parent;
        setName(replaceVars(getName(), vars));
        setProject(replaceVars(getProject(), vars));
        setFolder(replaceVars(getFolder(), vars));
        if (artifacts != null) {
            for (final Artifact artifact : artifacts) {
                artifact.init(this, vars);
            }
        }
        return this;
    }

}
