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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a set of code generators.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "generators")
@XmlType(propOrder = { "list" })
public class Generators extends AbstractTarget implements
        InitializableElement<Generators, SrcGen4JConfig> {

    @XmlElement(name = "generator")
    private List<GeneratorConfig> list;

    private transient SrcGen4JConfig parent;

    /**
     * Default constructor.
     */
    public Generators() {
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
    public Generators(final String project, final String folder) {
        super(project, folder);
    }

    /**
     * Returns the list of generators.
     * 
     * @return Generators or NULL.
     */
    public final List<GeneratorConfig> getList() {
        return list;
    }

    /**
     * Sets the list of generators.
     * 
     * @param list
     *            Generators or NULL.
     */
    public final void setList(final List<GeneratorConfig> list) {
        this.list = list;
    }

    /**
     * Adds a generator to the list. If the list does not exist it's created.
     * 
     * @param generator
     *            Generator to add - Cannot be NULL.
     */
    public final void addGenerator(final GeneratorConfig generator) {
        if (list == null) {
            list = new ArrayList<GeneratorConfig>();
        }
        list.add(generator);
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
    public final Generators init(final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        setProject(replaceVars(getProject(), vars));
        setFolder(replaceVars(getFolder(), vars));
        if (list != null) {
            for (final GeneratorConfig generator : list) {
                generator.init(this, vars);
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
    public final GeneratorConfig findByName(final String generatorName)
            throws GeneratorNotFoundException {
        final int idx = list.indexOf(new GeneratorConfig(generatorName));
        if (idx < 0) {
            throw new GeneratorNotFoundException(generatorName);
        }
        return list.get(idx);
    }

}
