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

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.fuin.xmlcfg4j.AbstractNamedElement;

/**
 * Represents a folder in a target folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "folder")
public final class Folder extends AbstractNamedElement implements InitializableElement<Folder, Project> {

    @NotNull
    @XmlAttribute
    private String path;

    @Nullable
    @XmlAttribute
    private Boolean create;

    @Nullable
    @XmlAttribute
    private Boolean override;

    @Nullable
    @XmlAttribute
    private String overrideExclude;

    @Nullable
    @XmlAttribute
    private String overrideInclude;

    @Nullable
    @XmlAttribute
    private Boolean clean;

    @Nullable
    @XmlAttribute
    private String cleanExclude;

    @Nullable
    private transient RegexFileFilter overrideExcludeFilter;

    @Nullable
    private transient RegexFileFilter overrideIncludeFilter;

    @Nullable
    private transient RegexFileFilter cleanExcludeFilter;

    @Nullable
    private transient Project parent;

    /**
     * Package visible default constructor for deserialization.
     */
    Folder() { // NOSONAR Ignore not initialized fields
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
    public Folder(@NotNull final String name, @NotNull final String path) {
        super(name);
        this.path = path;
    }

    /**
     * Constructor with all data.
     * 
     * @param parent
     *            Parent folder.
     * @param name
     *            Name to set.
     * @param path
     *            path to set.
     * @param create
     *            Create directories (TRUE) or not (NULL or FALSE).
     * @param override
     *            Override files (TRUE) or not (NULL or FALSE).
     * @param overrideExclude
     *            Regular expression of files to exclude from override.
     * @param clean
     *            Clean directory (TRUE) or not (NULL or FALSE).
     * @param cleanExclude
     *            Regular expression of files to exclude from cleaning.
     */
    Folder(@NotNull final Project parent, @NotNull final String name, @NotNull final String path, final boolean create,
            final boolean override, final String overrideExclude, final boolean clean, final String cleanExclude) {
        super(name);
        this.parent = parent;
        this.path = path;
        this.create = create;
        this.override = override;
        this.overrideExclude = overrideExclude;
        this.clean = clean;
        this.cleanExclude = cleanExclude;
    }

    /**
     * Returns the path.
     * 
     * @return Current path.
     */
    @NotNull
    public final String getPath() {
        return path;
    }

    /**
     * Returns the information if the directories should be created if they don't exist.
     * 
     * @return Create directories (TRUE) or not (FALSE).
     */
    public final boolean isCreate() {
        if (create == null) {
            return false;
        }
        return create.booleanValue();
    }

    /**
     * Returns the information if the directories should be created if they don't exist.
     * 
     * @return Create directories (TRUE) or not (NULL or FALSE).
     */
    @Nullable
    public final Boolean getCreate() {
        return create;
    }

    /**
     * Sets the information if the directories should be created if they don't exist.
     * 
     * @param create
     *            Create directories (TRUE) or not (NULL or FALSE).
     */
    public final void setCreate(@Nullable final Boolean create) {
        this.create = create;
    }

    /**
     * Returns the information if the directory should be cleaned (delete all files in it).
     * 
     * @return Clean directory (TRUE) or not (FALSE).
     */
    public final boolean isClean() {
        if (clean == null) {
            return false;
        }
        return clean.booleanValue();
    }

    /**
     * Returns the information if the directory should be cleaned (delete all files in it).
     * 
     * @return Clean directory (TRUE) or not (NULL or FALSE).
     */
    @Nullable
    public final Boolean getClean() {
        return clean;
    }

    /**
     * Sets the information if the directory should be cleaned (delete all files in it).
     * 
     * @param clean
     *            Clean directory (TRUE) or not (NULL or FALSE).
     */
    public final void setClean(@Nullable final Boolean clean) {
        this.clean = clean;
    }

    /**
     * Returns the information if existing files in the directory should be overridden.
     * 
     * @return Override files (TRUE) or not (FALSE).
     */
    public final boolean isOverride() {
        if (override == null) {
            return false;
        }
        return override.booleanValue();
    }

    /**
     * Returns the information if existing files in the directory should be overridden.
     * 
     * @return Override files (TRUE) or not (NULL or FALSE).
     */
    @Nullable
    public final Boolean getOverride() {
        return override;
    }

    /**
     * Sets the information if existing files in the directory should be overridden.
     * 
     * @param override
     *            Override files (TRUE) or not (NULL or FALSE).
     */
    public final void setOverride(@Nullable final Boolean override) {
        this.override = override;
    }

    /**
     * Returns a regular expression of the files to exclude.
     * 
     * @return Regular expression used to exclude files.
     */
    @Nullable
    public final String getOverrideExclude() {
        return overrideExclude;
    }

    /**
     * Sets a regular expression of the files not to override.
     * 
     * @param overrideExclude
     *            Regular expression used to exclude files.
     */
    public final void setOverrideExclude(final String overrideExclude) {
        this.overrideExclude = overrideExclude;
    }

    /**
     * Returns a regular expression of the files to include if the directory itself is set to override=false.
     * 
     * @return Regular expression used to include files.
     */
    @Nullable
    public final String getOverrideInclude() {
        return overrideInclude;
    }

    /**
     * Sets a regular expression of the files to override if the directory itself is set to override=false.
     * 
     * @param overrideInclude
     *            Regular expression used to include files.
     */
    public final void setOverrideInclude(final String overrideInclude) {
        this.overrideInclude = overrideInclude;
    }

    /**
     * Checks if a file can be overridden.
     * 
     * @param file
     *            File to check against the exclude condition.
     * 
     * @return TRUE if the file can be overridden.
     */
    public final boolean overrideAllowed(final File file) {
        if (isOverride()) {
            // In general it's allowed to recreate existing files
            if (overrideExclude == null) {
                return true;
            } else {
                return !getOverrideExcludeFilter().accept(file);
            }
        } else {
            // It's NOT allowed to recreate existing files
            if (overrideInclude == null) {
                return false;
            } else {
                return getOverrideIncludeFilter().accept(file);
            }
        }
    }

    private RegexFileFilter getOverrideIncludeFilter() {
        if (overrideIncludeFilter == null) {
            overrideIncludeFilter = new RegexFileFilter(overrideInclude);
        }
        return overrideIncludeFilter;
    }

    private RegexFileFilter getOverrideExcludeFilter() {
        if (overrideExcludeFilter == null) {
            overrideExcludeFilter = new RegexFileFilter(overrideExclude);
        }
        return overrideExcludeFilter;
    }

    /**
     * Returns a regular expression of the files to exclude.
     * 
     * @return Regular expression used to exclude files.
     */
    @Nullable
    public final String getCleanExclude() {
        return cleanExclude;
    }

    /**
     * Sets a regular expression of the files to exclude from cleaning.
     * 
     * @param cleanExclude
     *            Regular expression used to exclude files.
     */
    public final void setCleanExclude(final String cleanExclude) {
        this.cleanExclude = cleanExclude;
    }

    /**
     * Checks if a file should be excluded from cleaning.
     * 
     * @param file
     *            File to check against the exlcude condition.
     * 
     * @return TRUE if the file can be cleaned.
     */
    public final boolean cleanAllowed(final File file) {
        if (cleanExclude == null) {
            return true;
        }
        if (cleanExcludeFilter == null) {
            cleanExcludeFilter = new RegexFileFilter(cleanExclude);
        }
        return !cleanExcludeFilter.accept(file);
    }

    /**
     * Returns the parent for the folder.
     * 
     * @return Parent.
     */
    @Nullable
    public final Project getParent() {
        return parent;
    }

    /**
     * Sets the parent for the folder.
     * 
     * @param parent
     *            Parent or <code>null</code>.
     */
    final void setParent(final Project parent) {
        this.parent = parent;
    }

    @Override
    public final Folder init(final SrcGen4JContext context, final Project parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        setName(replaceVars(getName(), getVarMap()));
        path = replaceVars(path, getVarMap());
        return this;
    }

    /**
     * Returns the full path from project and folder.
     * 
     * @return Path.
     */
    @Nullable
    public final String getDirectory() {
        if (parent == null) {
            return null;
        }
        return parent.getPath() + "/" + getPath();
    }

    /**
     * Returns the full path from project and folder.
     * 
     * @return Directory.
     */
    @Nullable
    public final File getCanonicalDir() {
        final String dir = getDirectory();
        if (dir == null) {
            return null;
        }
        try {
            return new File(dir).getCanonicalFile();
        } catch (final IOException ex) {
            throw new RuntimeException("Couldn't determine canonical file: " + dir, ex);
        }
    }

}
