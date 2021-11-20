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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Base class for assigning generated artifacts to a project folder.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractTarget extends AbstractElement {

    @Nullable
    @XmlAttribute
    private String project;

    @Nullable
    @XmlAttribute
    private String folder;

    /**
     * Package visible default constructor for deserialization.
     */
    AbstractTarget() {
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
    public AbstractTarget(@Nullable final String project, @Nullable final String folder) {
        super();
        this.project = project;
        this.folder = folder;
    }

    /**
     * Returns the project.
     * 
     * @return Current project.
     */
    @Nullable
    public final String getProject() {
        return project;
    }

    /**
     * Sets the project.
     * 
     * @param project
     *            Project to set.
     */
    protected final void setProject(@Nullable final String project) {
        this.project = project;
    }

    /**
     * Returns the folder.
     * 
     * @return Current folder.
     */
    @Nullable
    public final String getFolder() {
        return folder;
    }

    /**
     * Sets the folder.
     * 
     * @param folder
     *            Folder to set.
     */
    protected final void setFolder(@Nullable final String folder) {
        this.folder = folder;
    }

}
