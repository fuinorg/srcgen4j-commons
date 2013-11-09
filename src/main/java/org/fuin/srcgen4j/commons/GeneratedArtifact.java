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

import javax.validation.constraints.NotNull;

/**
 * Generated artifact.
 */
public final class GeneratedArtifact {

    private final String name;

    private final String pathAndName;

    private final String source;

    /**
     * Constructor with all data.
     * 
     * @param name
     *            Unique artifact name.
     * @param pathAndName
     *            Relative path and filename to write the source code to.
     * @param source
     *            Generated source code.
     */
    public GeneratedArtifact(@NotNull final String name, @NotNull final String pathAndName,
            @NotNull final String source) {
        super();
        this.name = name;
        this.pathAndName = pathAndName;
        this.source = source;
    }

    /**
     * Returns the unique artifact name.
     * 
     * @return Name.
     */
    @NotNull
    public final String getName() {
        return name;
    }

    /**
     * Returns the relative path and filename.
     * 
     * @return Path and filename to write the source code to.
     */
    @NotNull
    public final String getPathAndName() {
        return pathAndName;
    }

    /**
     * Returns the generated source code.
     * 
     * @return Source code.
     */
    @NotNull
    public final String getSource() {
        return source;
    }

    @Override
    public final String toString() {
        return pathAndName + " [" + name + "]";
    }

}
