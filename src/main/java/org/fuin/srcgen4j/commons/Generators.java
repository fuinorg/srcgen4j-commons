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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;

/**
 * Represents a set of code generators.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generators")
@XmlType(propOrder = { "list" })
public class Generators extends AbstractTarget implements InitializableElement<Generators, SrcGen4JConfig> {

    @Nullable
    @Valid
    @XmlElement(name = "generator")
    private List<GeneratorConfig> list;

    @Nullable
    private transient SrcGen4JConfig parent;

    /**
     * Package visible default constructor for deserialization.
     */
    Generators() {
        super();
    }

    /**
     * Constructor with project and folder.
     * 
     * @param project
     *            Project to set.
     * @param folder
     *            Folder to set.
     */
    public Generators(@Nullable final String project, @Nullable final String folder) {
        super(project, folder);
    }

    /**
     * Returns the list of generators.
     * 
     * @return Generators.
     */
    @Nullable
    public final List<GeneratorConfig> getList() {
        return list;
    }

    /**
     * Sets the list of generators.
     * 
     * @param list
     *            Generators.
     */
    public final void setList(@Nullable final List<GeneratorConfig> list) {
        this.list = list;
    }

    /**
     * Adds a generator to the list. If the list does not exist it's created.
     * 
     * @param generator
     *            Generator to add.
     */
    public final void addGenerator(@NotNull final GeneratorConfig generator) {
        Contract.requireArgNotNull("generator", generator);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(generator);
    }

    /**
     * Returns the parent of the object.
     * 
     * @return GeneratorConfig.
     */
    @Nullable
    public final SrcGen4JConfig getParent() {
        return parent;
    }

    @Override
    public final Generators init(final SrcGen4JContext context, final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        setProject(replaceVars(getProject(), getVarMap()));
        setFolder(replaceVars(getFolder(), getVarMap()));
        if (list != null) {
            for (final GeneratorConfig generator : list) {
                generator.init(context, this, getVarMap());
            }
        }
        return this;
    }

    /**
     * Returns a generator by it's name.
     * 
     * @param generatorName
     *            Name to find.
     * 
     * @return The generator.
     * 
     * @throws GeneratorNotFoundException
     *             No generator with the given name was found.
     */
    @Nullable
    public final GeneratorConfig findByName(@NotEmpty final String generatorName) throws GeneratorNotFoundException {
        Contract.requireArgNotEmpty("generatorName", generatorName);
        final int idx = list.indexOf(new GeneratorConfig(generatorName, "dummy", "dummy"));
        if (idx < 0) {
            throw new GeneratorNotFoundException(generatorName);
        }
        return list.get(idx);
    }

    /**
     * Returns the appropriate folder for a given artifact.
     * 
     * @param generatorName
     *            Unique name of the generator to return a target folder for.
     * @param artifactName
     *            Unique name of the artifact to return a target folder for.
     * 
     * @return Target folder.
     */
    @Nullable
    public final Folder findTargetFolder(@NotEmpty final String generatorName, @NotEmpty final String artifactName) {
        Contract.requireArgNotEmpty("generatorName", generatorName);
        Contract.requireArgNotEmpty("artifactName", artifactName);
        if (parent == null) {
            throw new IllegalStateException("Parent for generators is not set");
        }
        try {
            return parent.findTargetFolder(generatorName, artifactName);
        } catch (final ProjectNameNotDefinedException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        } catch (final ArtifactNotFoundException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        } catch (final FolderNameNotDefinedException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        } catch (final GeneratorNotFoundException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        } catch (final ProjectNotFoundException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        } catch (final FolderNotFoundException ex) {
            throw new RuntimeException(
                    "Couldn't determine target folder for generator '" + generatorName + "' and artifact '" + artifactName + "'", ex);
        }
    }

}
