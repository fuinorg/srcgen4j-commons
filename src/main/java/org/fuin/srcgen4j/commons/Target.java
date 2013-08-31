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
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Maps a single generated artifact based on a regular expression to a
 * project/folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target")
@XmlType(propOrder = { "pattern" })
public class Target extends AbstractTarget implements InitializableElement<Target, Artifact> {

    @XmlAttribute
    private String pattern;

    @XmlTransient
    private transient Artifact parent;

    @XmlTransient
    private transient Pattern regExpr;

    /**
     * Default constructor.
     */
    public Target() {
        super();
    }

    /**
     * Constructor with pattern, project and folder.
     * 
     * @param pattern
     *            Pattern to set.
     * @param project
     *            Project to set.
     * @param folder
     *            Folder to set.
     */
    public Target(final String pattern, final String project, final String folder) {
        super(project, folder);
        this.pattern = pattern;
    }

    /**
     * Returns the pattern.
     * 
     * @return Current pattern.
     */
    public final String getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern.
     * 
     * @param pattern
     *            Pattern to set.
     */
    public final void setPattern(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return Artifact.
     */
    public final Artifact getParent() {
        return parent;
    }

    /**
     * Sets the parent of the object.
     * 
     * @param parent
     *            Artifact.
     */
    public final void setParent(final Artifact parent) {
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
            return parent.getDefProject();
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
            return parent.getDefFolder();
        }
        return getFolder();
    }

    @Override
    public final Target init(final Artifact parent, final Map<String, String> vars) {
        this.parent = parent;
        pattern = replaceVars(pattern, vars);
        setProject(replaceVars(getProject(), vars));
        setFolder(replaceVars(getFolder(), vars));
        if (pattern == null) {
            regExpr = null;
        } else {
            regExpr = Pattern.compile(pattern);
        }
        return this;
    }

    /**
     * Returns if the pattern matches the given path.
     * 
     * @param targetPath
     *            Path to test.
     * 
     * @return If the target matches TRUE, else FALSE.
     */
    public final boolean matches(final String targetPath) {
        if (regExpr == null) {
            return true;
        }
        return regExpr.matcher(targetPath).find();
    }

}
