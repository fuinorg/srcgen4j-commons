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

import org.fuin.objects4j.common.Contract;
import org.fuin.objects4j.common.NeverEmpty;
import org.fuin.objects4j.common.NeverNull;
import org.fuin.objects4j.common.NotEmpty;

/**
 * Generated artifact.
 */
public final class GeneratedArtifact {

    private final String name;

    private final String pathAndName;

    private final byte[] data;

    /**
     * Constructor with all data.
     * 
     * @param name
     *            Unique artifact name.
     * @param pathAndName
     *            Relative path and filename to write the source code to.
     * @param data
     *            Generated data.
     */
    public GeneratedArtifact(@NotEmpty final String name, @NotEmpty final String pathAndName,
            @NotNull final byte[] data) {
        super();
        Contract.requireArgNotEmpty("name", name);
        Contract.requireArgNotEmpty("pathAndName", pathAndName);
        Contract.requireArgNotNull("data", data);
        this.name = name;
        this.pathAndName = pathAndName;
        this.data = data;
    }

    /**
     * Returns the unique artifact name.
     * 
     * @return Name.
     */
    @NeverEmpty
    public final String getName() {
        return name;
    }

    /**
     * Returns the relative path and filename.
     * 
     * @return Path and filename to write the source code to.
     */
    @NeverEmpty
    public final String getPathAndName() {
        return pathAndName;
    }

    /**
     * Returns the generated data (source code).
     * 
     * @return Data.
     */
    @NeverNull
    public final byte[] getData() {
        return data;
    }

    @Override
    public final String toString() {
        return pathAndName + " [" + name + "]";
    }

}
