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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Configuration that maps generator output to projects.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generator-config")
@XmlType(propOrder = { "generators", "projects", "variables" })
public class GeneratorConfig {

	@XmlElementWrapper(name = "variables")
	@XmlElement(name = "variable")
	private List<Variable> variables;

	@XmlElementWrapper(name = "projects")
	@XmlElement(name = "project")
	private List<Project> projects;

	@XmlElementWrapper(name = "generators")
	@XmlElement(name = "generator")
	private List<Generator> generators;

	/**
	 * Returns a list of variables.
	 * 
	 * @return Variables or NULL.
	 */
	public final List<Variable> getVariables() {
		return variables;
	}

	/**
	 * Returns a map of variables.
	 * 
	 * @return Map of variables or NULL.
	 */
	public final Map<String, String> getVarMap() {
		if (variables == null) {
			return null;
		}
		final Map<String, String> map = new HashMap<String, String>();
		for (final Variable var : variables) {
			map.put(var.getName(), var.getValue());
		}
		return map;
	}

	/**
	 * Sets a list of variables.
	 * 
	 * @param variables
	 *            Variables or NULL.
	 */
	public final void setVariables(final List<Variable> variables) {
		this.variables = variables;
	}

	/**
	 * Returns a list of projects.
	 * 
	 * @return Projects or NULL.
	 */
	public final List<Project> getProjects() {
		return projects;
	}

	/**
	 * Sets a list of projects.
	 * 
	 * @param projects
	 *            Projects or NULL.
	 */
	public final void setProjects(final List<Project> projects) {
		this.projects = projects;
	}

	/**
	 * Returns a list of generators.
	 * 
	 * @return Generators or NULL.
	 */
	public final List<Generator> getGenerators() {
		return generators;
	}

	/**
	 * Sets a list of generators.
	 * 
	 * @param generators
	 *            Generators or NULL.
	 */
	public final void setGenerators(final List<Generator> generators) {
		this.generators = generators;
	}

	/**
	 * Replaces all variables in all configuration objects.<br>
	 * <br>
	 * <b>CAUTION:</b> Elements contained in this configuration will be changed.
	 * If you serialize the object after calling this method the current state
	 * <b>without</b> previously defined variables will be saved.
	 */
	public final void init() {
		if (variables != null) {
			if (projects != null) {
				for (final Project project : projects) {
					project.init(getVarMap());
				}
			}
			if (generators != null) {
				for (final Generator generator : generators) {
					generator.init(getVarMap());
				}
			}
		}
	}

	/**
	 * Returns a target directory for a given combination of generator name,
	 * artifact name and target sub path.
	 * 
	 * @param generatorName
	 *            Name of the generator.
	 * @param artifactName
	 *            Name of the artifact.
	 * @param targetPath
	 *            Current path and file.
	 * 
	 * @return Target file based on the configuration.
	 * 
	 * @throws GeneratorNotFoundException
	 *             The given generator name was not found in the configuration.
	 * @throws ArtifactNotFoundException
	 *             The given artifact was not found in the generator.
	 * @throws ProjectNameNotDefinedException
	 *             No project name is bound to the given combination.
	 * @throws FolderNameNotDefinedException
	 *             No folder name is bound to the given combination.
	 * @throws ProjectNotFoundException
	 *             The project name based on the selection is unknown.
	 * @throws FolderNotFoundException
	 *             The folder in the project based on the selection is unknown.
	 */
	public final Folder findTargetFolder(final String generatorName,
			final String artifactName, final String targetPath)
			throws ProjectNameNotDefinedException, ArtifactNotFoundException,
			FolderNameNotDefinedException, GeneratorNotFoundException,
			ProjectNotFoundException, FolderNotFoundException {

		int idx = generators.indexOf(new Generator(generatorName));
		if (idx < 0) {
			throw new GeneratorNotFoundException(generatorName);
		}
		final Generator generator = generators.get(idx);

		idx = generator.getArtifacts().indexOf(new Artifact(artifactName));
		if (idx < 0) {
			throw new ArtifactNotFoundException(generatorName, artifactName);
		}
		final Artifact artifact = generator.getArtifacts().get(idx);

		final String projectName;
		final String folderName;
		final String targetPattern;
		final Target target = artifact.findTargetFor(targetPath);
		if (target == null) {
			projectName = artifact.getDefProject();
			folderName = artifact.getDefFolder();
			targetPattern = null;
		} else {
			projectName = target.getDefProject();
			folderName = target.getDefFolder();
			targetPattern = target.getPattern();
		}

		if (projectName == null) {
			throw new ProjectNameNotDefinedException(generatorName,
					artifactName, targetPattern);
		}
		if (folderName == null) {
			throw new FolderNameNotDefinedException(generatorName,
					artifactName, targetPattern);
		}

		idx = projects.indexOf(new Project(projectName));
		if (idx < 0) {
			throw new ProjectNotFoundException(generatorName, artifactName,
					targetPattern, projectName);
		}
		final Project project = projects.get(idx);

		idx = project.getFolders().indexOf(new Folder(folderName));
		if (idx < 0) {
			throw new FolderNotFoundException(generatorName, artifactName,
					targetPattern, projectName, folderName);
		}
		return project.getFolders().get(idx);

	}

}
