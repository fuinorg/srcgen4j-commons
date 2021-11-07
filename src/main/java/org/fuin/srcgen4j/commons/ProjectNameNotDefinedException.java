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

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * For a given generator/artifact/target was no project found in a generator configuration.
 */
public class ProjectNameNotDefinedException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor with names.
     * 
     * @param generatorName
     *            Name of the generator with the requested artifact - Should not be <code>null</code>.
     * @param artifactName
     *            Name of the artifact with the request target - Should not be <code>null</code>.
     * @param targetPattern
     *            Target pattern - Should not be <code>null</code>.
     */
    public ProjectNameNotDefinedException(@Nullable final String generatorName, @Nullable final String artifactName,
            @Nullable final String targetPattern) {
        super("No project name is defined for: " + generatorName + " / " + artifactName + " / " + targetPattern);
    }

}
