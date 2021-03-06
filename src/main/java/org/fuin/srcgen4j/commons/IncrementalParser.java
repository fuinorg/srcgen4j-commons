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
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Parses some kind of model in a full or an incremental mode.
 * 
 * @param <MODEL>
 *            Type of the model.
 */
public interface IncrementalParser<MODEL> extends Parser<MODEL> {

    /**
     * Returns a file filter to use for selecting the appropriate files.
     * 
     * @return File filter.
     */
    @NotNull
    public IOFileFilter getFileFilter();

    /**
     * Parses a model using the initially given configuration and a file list.
     * 
     * @param files
     *            Set of files to parse.
     * 
     * @return Model.
     * 
     * @throws ParseException
     *             Error during parse process.
     */
    @NotNull
    public MODEL parse(@NotNull Set<File> files) throws ParseException;

}
