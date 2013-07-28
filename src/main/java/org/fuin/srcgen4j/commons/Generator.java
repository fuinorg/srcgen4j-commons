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

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a code generator.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generator")
@XmlType(propOrder = { "artifacts" })
public class Generator extends AbstractNamedTarget {

	@XmlElement(name = "artifact")
	private List<Artifact> artifacts;

	@XmlTransient
	private GeneratorConfig parent;
	
	/**
	 * Default constructor.
	 */
	public Generator() {
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
	public Generator(final String name, final String project,
			final String folder) {
		super(name, project, folder);
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
	 * @return GeneratorConfig.
	 */
	public final GeneratorConfig getParent() {
		return parent;
	}

	/**
	 * Sets the parent of the object.
	 * 
	 * @param parent GeneratorConfig.
	 */
	public final void setParent(final GeneratorConfig parent) {
		this.parent = parent;
	}
	
	/**
	 * Called when the object is deserialized with JAXB. 
	 * 
	 * @param unmarshaller Unmarshaller.
	 * @param parent Parent object.
	 */
	final void afterUnmarshal(final Unmarshaller unmarshaller, final Object parent) {
		this.parent = (GeneratorConfig) parent;
	}

	/**
	 * Replaces all variables in all configuration objects.
	 * 
	 * @param vars Variables to use.
	 */
	public final void replaceVariables(final Map<String, String> vars) {
		setName(replaceVars(getName(), vars));
		setProject(replaceVars(getProject(), vars));
		setFolder(replaceVars(getFolder(), vars));
		if (artifacts != null) {
			for (final Artifact artifact : artifacts) {
				artifact.replaceVariables(vars);
			}
		}
	}	
	
}
