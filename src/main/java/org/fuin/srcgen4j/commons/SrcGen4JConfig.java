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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.FileExists;
import org.fuin.objects4j.common.FileExistsValidator;
import org.fuin.objects4j.common.IsDirectory;
import org.fuin.objects4j.common.IsDirectoryValidator;
import org.fuin.utils4j.VariableResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration that maps generator output to projects.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "srcgen4j-config")
public class SrcGen4JConfig {

    private static final String ROOT_DIR_VAR = "rootDir";

    private static final Logger LOG = LoggerFactory.getLogger(SrcGen4JConfig.class);

    @Nullable
    @Valid
    @XmlElement(name = "variables")
    private Variables variables;

    @Nullable
    @Valid
    @XmlElementWrapper(name = "projects")
    @XmlElement(name = "project")
    private List<Project> projects;

    @Nullable
    @Valid
    @XmlElement(name = "parsers")
    private Parsers parsers;

    @Nullable
    @Valid
    @XmlElement(name = "generators")
    private Generators generators;

    @Nullable
    @XmlTransient
    private Map<String, String> varMap;

    @XmlTransient
    private boolean initialized = false;

    /**
     * Default constructor.
     */
    public SrcGen4JConfig() {
        super();
    }

    /**
     * Returns a list of variables.
     * 
     * @return Variables.
     */
    @Nullable
    public final Variables getVariables() {
        return variables;
    }

    /**
     * Returns a map of variables.
     * 
     * @return Map of variables.
     */
    @Nullable
    public final Map<String, String> getVarMap() {
        return varMap;
    }

    /**
     * Sets a list of variables.
     * 
     * @param variables
     *            Variables.
     */
    public final void setVariables(@Nullable final Variables variables) {
        this.variables = variables;
    }

    /**
     * Returns a list of projects.
     * 
     * @return Projects.
     */
    @Nullable
    public final List<Project> getProjects() {
        return projects;
    }

    /**
     * Sets a list of projects.
     * 
     * @param projects
     *            Projects.
     */
    public final void setProjects(@Nullable final List<Project> projects) {
        this.projects = projects;
    }

    /**
     * Returns the list of parsers.
     * 
     * @return Parsers.
     */
    @Nullable
    public final Parsers getParsers() {
        return parsers;
    }

    /**
     * Sets the list of parsers.
     * 
     * @param parsers
     *            Parsers.
     */
    public final void setParsers(@Nullable final Parsers parsers) {
        this.parsers = parsers;
    }

    /**
     * Adds a parser to the configuration. If the list of parsers does not exist it will be created.
     * 
     * @param parser
     *            Parser to add.
     */
    public final void addParser(@NotNull final ParserConfig parser) {
        Contract.requireArgNotNull("parser", parser);
        if (parsers == null) {
            parsers = new Parsers();
        }
        parsers.addParser(parser);
    }

    /**
     * Returns a set of generators.
     * 
     * @return Generators.
     */
    @Nullable
    public final Generators getGenerators() {
        return generators;
    }

    /**
     * Sets a the of generators.
     * 
     * @param generators
     *            Generators.
     */
    public final void setGenerators(@Nullable final Generators generators) {
        this.generators = generators;
    }

    /**
     * Returns the information if the object has been initialized.
     * 
     * @return If the method {@link #init(SrcGen4JContext, File)} was called TRUE, else FALSE.
     */
    public final boolean isInitialized() {
        return initialized;
    }

    private void initVarMap(final File rootDir) {
        varMap = new HashMap<String, String>();
        varMap.put(ROOT_DIR_VAR, rootDir.toString());
        if (variables != null) {
            final List<Variable> vars = variables.asList();
            for (final Variable var : vars) {
                if (var.getName().equals(ROOT_DIR_VAR)) {
                    LOG.warn("Replaced root directory '{}' with: '{}'", var.getValue(), rootDir);
                } else {
                    varMap.put(var.getName(), var.getValue());
                }
            }
            varMap = new VariableResolver(varMap).getResolved();
        }
    }

    /**
     * Initializes this object and it's childs.<br>
     * <br>
     * <b>CAUTION:</b> Elements contained in this configuration may be changed. If you serialize the object after calling this method the
     * new state will be saved.
     * 
     * @param context
     *            Current context.
     * @param rootDir
     *            Root directory that is available as variable 'rootDir'.
     * 
     * @return This instance.
     */
    public final SrcGen4JConfig init(@NotNull final SrcGen4JContext context, @NotNull @FileExists @IsDirectory final File rootDir) {

        Contract.requireArgNotNull("context", context);
        Contract.requireArgNotNull(ROOT_DIR_VAR, rootDir);
        FileExistsValidator.requireArgValid(ROOT_DIR_VAR, rootDir);
        IsDirectoryValidator.requireArgValid(ROOT_DIR_VAR, rootDir);

        initVarMap(rootDir);
        if (variables != null) {
            variables.init(varMap);
        }
        if (projects != null) {
            for (final Project project : projects) {
                project.init(context, this, varMap);
            }
        }
        if (generators != null) {
            generators.init(context, this, varMap);
        }
        if (parsers != null) {
            parsers.init(context, this, varMap);
        }
        initialized = true;
        return this;
    }

    /**
     * Returns a target directory for a given combination of generator name and artifact name.
     * 
     * @param generatorName
     *            Name of the generator.
     * @param artifactName
     *            Name of the artifact.
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
    public final Folder findTargetFolder(@NotNull final String generatorName, @NotNull final String artifactName)
            throws ProjectNameNotDefinedException, ArtifactNotFoundException, FolderNameNotDefinedException, GeneratorNotFoundException,
            ProjectNotFoundException, FolderNotFoundException {

        Contract.requireArgNotNull("generatorName", generatorName);
        Contract.requireArgNotNull("artifactName", artifactName);

        return findTargetFolder(generatorName, artifactName, null);

    }

    /**
     * Returns a target directory for a given combination of generator name, artifact name and target sub path.
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
    public final Folder findTargetFolder(@NotNull final String generatorName, @NotNull final String artifactName, final String targetPath)
            throws ProjectNameNotDefinedException, ArtifactNotFoundException, FolderNameNotDefinedException, GeneratorNotFoundException,
            ProjectNotFoundException, FolderNotFoundException {

        Contract.requireArgNotNull("generatorName", generatorName);
        Contract.requireArgNotNull("artifactName", artifactName);

        final GeneratorConfig generator = generators.findByName(generatorName);
        if (generator == null) {
            throw new GeneratorNotFoundException(generatorName);
        }

        int idx = generator.getArtifacts().indexOf(new Artifact(artifactName));
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
            throw new ProjectNameNotDefinedException(generatorName, artifactName, targetPattern);
        }
        if (folderName == null) {
            throw new FolderNameNotDefinedException(generatorName, artifactName, targetPattern);
        }

        idx = projects.indexOf(new Project(projectName, "dummy"));
        if (idx < 0) {
            throw new ProjectNotFoundException(generatorName, artifactName, targetPattern, projectName);
        }
        final Project project = projects.get(idx);

        idx = project.getFolders().indexOf(new Folder(folderName, "NotUsed"));
        if (idx < 0) {
            throw new FolderNotFoundException(generatorName, artifactName, targetPattern, projectName, folderName);
        }
        return project.getFolders().get(idx);

    }

    /**
     * Find all generators that are connected to a given parser.
     * 
     * @param parserName
     *            Name of the parser to return the generators for.
     * 
     * @return List of generators.
     */
    @NotNull
    public final List<GeneratorConfig> findGeneratorsForParser(@NotNull final String parserName) {
        Contract.requireArgNotNull("parserName", parserName);

        final List<GeneratorConfig> list = new ArrayList<>();
        final List<GeneratorConfig> gcList = generators.getList();
        for (final GeneratorConfig gc : gcList) {
            if (gc.getParser().equals(parserName)) {
                list.add(gc);
            }
        }
        return list;
    }

    /**
     * Creates a new configuration with a single project and a Maven directory structure.
     * 
     * @param context
     *            Current context.
     * @param projectName
     *            Name of the one and only project.
     * @param rootDir
     *            Root directory that is available as variable 'srcgen4jRootDir'.
     * 
     * @return New initialized configuration instance.
     */
    @NotNull
    public static SrcGen4JConfig createMavenStyleSingleProject(@NotNull final SrcGen4JContext context, @NotNull final String projectName,
            @NotNull @FileExists @IsDirectory final File rootDir) {

        Contract.requireArgNotNull("context", context);
        Contract.requireArgNotNull(ROOT_DIR_VAR, rootDir);
        FileExistsValidator.requireArgValid(ROOT_DIR_VAR, rootDir);
        IsDirectoryValidator.requireArgValid(ROOT_DIR_VAR, rootDir);
        Contract.requireArgNotNull("projectName", projectName);

        final SrcGen4JConfig config = new SrcGen4JConfig();
        final List<Project> projects = new ArrayList<>();
        final Project project = new Project(projectName, ".");
        project.setMaven(true);
        projects.add(project);
        config.setProjects(projects);
        config.init(context, rootDir);
        return config;
    }

}
