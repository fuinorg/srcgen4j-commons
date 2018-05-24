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

import java.io.File;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Provides a context for the build process.
 */
public interface SrcGen4JContext {

    /**
     * Returns the class loader to use for loading parsers, generators and other stuff.
     * 
     * @return Class loader.
     */
    @NotNull
    public ClassLoader getClassLoader();

    /**
     * Returns a list of JAR files to add to the class path used during parse/generate process.
     * 
     * @return Unmodifiable list of JAR files.
     */
    @Nullable
    public List<File> getJarFiles();

    /**
     * Returns a list of binary directories to add to the class path used during parse/generate process.
     * 
     * @return Unmodifiable list of "bin"/"classes" directories.
     */
    @Nullable
    public List<File> getBinDirs();

}
