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
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a target project.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "project")
@XmlType(propOrder = { "folders", "maven", "path", "name" })
public class Project extends AbstractElement implements
        InitializableElement<Project, SrcGen4JConfig> {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String path;

    @XmlAttribute
    private Boolean maven;

    @XmlElement(name = "folder")
    private List<Folder> folders;

    private transient SrcGen4JConfig parent;

    /**
     * Default constructor.
     */
    public Project() {
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
    public Project(final String name, final String path) {
        super();
        this.name = name;
        this.path = path;
    }

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name to set.
     */
    public Project(final String name) {
        super();
        this.name = name;
    }

    /**
     * Returns the name.
     * 
     * @return Current name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            Name to set.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the path.
     * 
     * @return Current path.
     */
    public final String getPath() {
        return path;
    }

    /**
     * Sets the path.
     * 
     * @param path
     *            Path to set.
     */
    public final void setPath(final String path) {
        this.path = path;
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
    public final Boolean getMaven() {
        return maven;
    }

    /**
     * Returns the list of folders.
     * 
     * @return Folders or NULL.
     */
    public final List<Folder> getFolders() {
        return folders;
    }

    /**
     * Sets the list of folders.
     * 
     * @param folders
     *            Folders or NULL.
     */
    public final void setFolders(final List<Folder> folders) {
        this.folders = folders;
    }

    /**
     * Sets the information if Maven default path should be used.
     * 
     * @param maven
     *            Maven default path setup (TRUE) or not (NULL or FALSE).
     */
    public final void setMaven(final Boolean maven) {
        this.maven = maven;
    }

    /**
     * Adds a folder to the list. If the list does not exist it's created.
     * 
     * @param folder
     *            Folder to add - Cannot be NULL.
     */
    public final void addFolder(final Folder folder) {
        if (folders == null) {
            folders = new ArrayList<Folder>();
        }
        folders.add(folder);
    }

    // CHECKSTYLE:OFF Generated code
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Project other = (Project) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    // CHECKSTYLE:ON

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
    public final Project init(final SrcGen4JConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        name = replaceVars(name, vars);
        path = replaceVars(path, vars);
        if (folders != null) {
            for (final Folder folder : folders) {
                folder.setParent(this);
                folder.init(this, vars);
            }
        }
        if (isMaven()) {
            addIfNotExists(new Folder(this, "mainJava", "src/main/java", false, false, false));
            addIfNotExists(new Folder(this, "mainRes", "src/main/resources", false, false, false));
            addIfNotExists(new Folder(this, "genMainJava", "src-gen/main/java", true, true, true));
            addIfNotExists(new Folder(this, "genMainRes", "src-gen/main/resources", true, true,
                    true));
            addIfNotExists(new Folder(this, "testJava", "src/test/java", false, false, false));
            addIfNotExists(new Folder(this, "testRes", "src/test/resources", false, false, false));
            addIfNotExists(new Folder(this, "genTestJava", "src-gen/test/java", true, true, true));
            addIfNotExists(new Folder(this, "genTestRes", "src-gen/test/resources", true, true,
                    true));
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
