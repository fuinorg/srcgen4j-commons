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

import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.objects4j.common.Contract;

/**
 * Maps a single generated artifact based on a regular expression to a project/folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target")
public class Target extends AbstractTarget implements InitializableElement<Target, Artifact> {

    @NotEmpty
    @XmlAttribute
    private String pattern;

    @Nullable
    private transient Artifact parent;

    @Nullable
    private transient Pattern regExpr;

    /**
     * Package visible default constructor for deserialization.
     */
    Target() {
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
    public Target(@NotEmpty final String pattern, @Nullable final String project, @Nullable final String folder) {
        super(project, folder);
        Contract.requireArgNotEmpty("pattern", pattern);
        this.pattern = pattern;
    }

    /**
     * Returns the pattern.
     * 
     * @return Current pattern.
     */
    @NotEmpty
    public final String getPattern() {
        return pattern;
    }

    /**
     * Returns the parent of the object.
     * 
     * @return Artifact.
     */
    @Nullable
    public final Artifact getParent() {
        return parent;
    }

    /**
     * Returns the defined project from this object or any of it's parents.
     * 
     * @return Project.
     */
    @Nullable
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
     * @return Folder.
     */
    @Nullable
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
    public final Target init(final SrcGen4JContext context, final Artifact parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        pattern = replaceVars(pattern, getVarMap());
        setProject(replaceVars(getProject(), getVarMap()));
        setFolder(replaceVars(getFolder(), getVarMap()));
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
    public final boolean matches(@NotNull final String targetPath) {
        Contract.requireArgNotNull("targetPath", targetPath);
        if (regExpr == null) {
            return true;
        }
        return regExpr.matcher(targetPath).find();
    }

}
