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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.fuin.objects4j.common.Contract;

/**
 * Single generated artifact.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "artifact")
@XmlType(propOrder = { "targets" })
public final class Artifact extends AbstractNamedTarget implements InitializableElement<Artifact, GeneratorConfig> {

    @Nullable
    @Valid
    @XmlElement(name = "target")
    private List<Target> targets;

    @Nullable
    private transient GeneratorConfig parent;

    /**
     * Package visible default constructor for deserialization.
     */
    Artifact() {
        super();
    }

    /**
     * Constructor with name.
     * 
     * @param name
     *            Name to set.
     */
    public Artifact(@NotNull final String name) {
        super(name, null, null);
    }

    /**
     * Constructor with name, project and folder.
     * 
     * @param name
     *            Name to set.
     * @param project
     *            Project to set.
     * @param folder
     *            Folder to set.
     */
    public Artifact(@NotNull final String name, @Nullable final String project, @Nullable final String folder) {
        super(name, project, folder);
    }

    /**
     * Returns the list of targets.
     * 
     * @return Targets.
     */
    @Nullable
    public final List<Target> getTargets() {
        return targets;
    }

    /**
     * Sets the list of targets.
     * 
     * @param targets
     *            Targets.
     */
    public final void setTargets(@Nullable final List<Target> targets) {
        this.targets = targets;
    }

    /**
     * Adds a target to the list. If the list does not exist it's created.
     * 
     * @param target
     *            Target to add.
     */
    public final void addTarget(@NotNull final Target target) {
        Contract.requireArgNotNull("target", target);
        if (targets == null) {
            targets = new ArrayList<>();
        }
        targets.add(target);
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

    /**
     * Returns the parent of the object.
     * 
     * @return Generator.
     */
    @Nullable
    public final GeneratorConfig getParent() {
        return parent;
    }

    /**
     * Returns the first target that matched the given path based on the defined patterns. Returns null if the argument is
     * <code>null</code>.
     * 
     * @param targetPath
     *            Path to find.
     * 
     * @return Target.
     */
    @Nullable
    public final Target findTargetFor(@Nullable final String targetPath) {
        if (targets == null) {
            return null;
        }
        if (targetPath == null) {
            return null;
        }
        for (final Target target : targets) {
            if (target.matches(targetPath)) {
                return target;
            }
        }
        return null;
    }

    @Override
    public final Artifact init(final SrcGen4JContext context, final GeneratorConfig parent, final Map<String, String> vars) {
        this.parent = parent;
        inheritVariables(vars);
        setName(replaceVars(getName(), getVarMap()));
        setProject(replaceVars(getProject(), getVarMap()));
        setFolder(replaceVars(getFolder(), getVarMap()));
        if (targets != null) {
            for (final Target target : targets) {
                target.init(context, this, getVarMap());
            }
        }
        return this;
    }

}
