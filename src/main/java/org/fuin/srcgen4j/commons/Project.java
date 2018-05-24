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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverEmpty;
import org.fuin.objects4j.common.NotEmpty;
import org.fuin.objects4j.common.Nullable;
import org.fuin.xmlcfg4j.AbstractNamedElement;

/**
 * Represents a target project.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "project")
@XmlType(propOrder = { "folders", "maven", "path" })
public class Project extends AbstractNamedElement implements InitializableElement<Project, SrcGen4JConfig> {

    @NotEmpty
    @XmlAttribute
    private String path;

    @Nullable
    @XmlAttribute
    private Boolean maven;

    @Nullable
    @Valid
    @XmlElement(name = "folder")
    private List<Folder> folders;

    private transient SrcGen4JConfig parent;

    /**
     * Package visible default constructor for deserialization.
     */
    Project() {
        super();
    }

    /**
     * Constructor with name and path.
     * 
     * @param name
     *            Name to set.
     * @param path
     *            path to set.
     */
    public Project(@NotEmpty final String name, @NotEmpty final String path) {
        super(name);
        Contract.requireArgNotNull("path", path);
        this.path = path;
    }

    /**
     * Returns the path.
     * 
     * @return Current path.
     */
    @NeverEmpty
    public final String getPath() {
        return path;
    }

    /**
     * Returns the information if Maven default path should be used.
     * 
     * @return Maven default path setup (TRUE) or not (FALSE).
     */
    public final boolean isMaven() {
        if (maven == null) {
            return true;
        }
        return maven.booleanValue();
    }

    /**
     * Returns the information if Maven default path should be used.
     * 
     * @return Maven default path setup (TRUE / NULL) or not (FALSE).
     */
    @Nullable
    public final Boolean getMaven() {
        return maven;
    }

    /**
     * Returns the list of folders.
     * 
     * @return Folders or NULL.
     */
    @Nullable
    public final List<Folder> getFolders() {
        return folders;
    }

    /**
     * Sets the list of folders.
     * 
     * @param folders
     *            Folders or NULL.
     */
    public final void setFolders(@Nullable final List<Folder> folders) {
        this.folders = folders;
    }

    /**
     * Sets the information if Maven default path should be used.
     * 
     * @param maven
     *            Maven default path setup (TRUE) or not (NULL or FALSE).
     */
    public final void setMaven(@Nullable final Boolean maven) {
        this.maven = maven;
    }

    /**
     * Adds a folder to the list. If the list does not exist it's created.
     * 
     * @param folder
     *            Folder to add.
     */
    public final void addFolder(@NotNull final Folder folder) {
        Contract.requireArgNotNull("folder", folder);
        if (folders == null) {
            folders = new ArrayList<Folder>();
        }
        folders.add(folder);
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
    public final Project init(final SrcGen4JContext context, final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        setName(replaceVars(getName(), getVarMap()));
        path = replaceVars(path, getVarMap());
        if (folders != null) {
            for (final Folder folder : folders) {
                folder.setParent(this);
                folder.init(context, this, getVarMap());
            }
        }
        if (isMaven()) {
            addIfNotExists(new Folder(this, "mainJava", "src/main/java", false, false, null, false, null));
            addIfNotExists(new Folder(this, "mainRes", "src/main/resources", false, false, null, false, null));
            addIfNotExists(new Folder(this, "genMainJava", "src-gen/main/java", true, false, null, true, null));
            addIfNotExists(new Folder(this, "genMainRes", "src-gen/main/resources", true, false, null, true, null));
            addIfNotExists(new Folder(this, "testJava", "src/test/java", false, false, null, false, null));
            addIfNotExists(new Folder(this, "testRes", "src/test/resources", false, false, null, false, null));
            addIfNotExists(new Folder(this, "genTestJava", "src-gen/test/java", true, false, null, true, null));
            addIfNotExists(new Folder(this, "genTestRes", "src-gen/test/resources", true, false, null, true, null));
        }
        return this;
    }

    private void addIfNotExists(final Folder folder) {
        if (folders == null) {
            folders = new ArrayList<Folder>();
        }
        final int idx = folders.indexOf(folder);
        if (idx < 0) {
            folders.add(folder);
        }
    }

}
