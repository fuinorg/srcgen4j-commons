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

/**
 * A folder with a given name was not found.
 */
public class FolderNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor with names.
     * 
     * @param generatorName
     *            Name of the generator with the requested artifact.
     * @param artifactName
     *            Name of the artifact with the request target.
     * @param targetPath
     *            Target path.
     * @param projectName
     *            Project name.
     * @param folderName
     *            Folder name.
     */
    public FolderNotFoundException(final String generatorName, final String artifactName,
            final String targetPath, final String projectName, final String folderName) {
        super("The folder name '" + folderName + "' in project '" + projectName
                + "' is not known - Selection was: " + generatorName + " / " + artifactName + " / "
                + targetPath);
    }

}
