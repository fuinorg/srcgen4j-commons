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

import java.io.File;
import java.util.Iterator;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Nullable;

/**
 * A context that supports marking files.
 */
public interface FileMarkerCapable {

    /**
     * Adds a general marker to a file.
     * 
     * @param file
     *            File to add the marker to.
     * @param severity
     *            Type of marker to add.
     * @param message
     *            Message to associate with the file.
     * 
     * @return New marker instance or NULL if the file was not found in the
     *         context.
     */
    @Nullable
    public FileMarker addMarker(@NotNull File file,
	    @NotNull FileMarkerSeverity severity, @NotNull String message);

    /**
     * Adds a line marker to a file.
     * 
     * @param file
     *            File to add the marker to.
     * @param severity
     *            Type of marker to add.
     * @param message
     *            Message to associate with a line number of the file.
     * @param line
     *            Line number the message is related to.
     * 
     * @return New marker instance or NULL if the file was not found in the
     *         context.
     */
    @Nullable
    public FileMarker addMarker(@NotNull File file,
	    @NotNull FileMarkerSeverity severity, @NotNull String message,
	    int line);

    /**
     * Adds a position marker to a file.
     * 
     * @param file
     *            File to add the marker to.
     * @param severity
     *            Type of marker to add.
     * @param message
     *            Message to associate with a part of the file.
     * @param start
     *            Absolute start position in the file.
     * @param length
     *            Length of the part.
     * 
     * @return New marker instance or NULL if the file was not found in the
     *         context.
     */
    @Nullable
    public FileMarker addMarker(@NotNull File file,
	    @NotNull FileMarkerSeverity severity, @NotNull String message,
	    int start, int length);

    /**
     * Removes the given marker from a file. Does nothing if the file was not
     * found in the context.
     * 
     * @param file
     *            File to remove the marker from.
     * @param marker
     *            Marker to remove.
     */
    public void removeMarker(@NotNull File file, @NotNull FileMarker marker);

    /**
     * Removes all markers associated with a file. Does nothing if the file was
     * not found in the context.
     * 
     * @param file
     *            File to remove all markers from.
     */
    public void removeAllMarkers(@NotNull File file);

    /**
     * Returns an iterator for all markers associated with a file.
     * 
     * @param file
     *            File to return a marker iterator for.
     * 
     * @return Iterator or NULL if the file was not found in the context or has
     *         no markers.
     */
    @Nullable
    public Iterator<? extends FileMarker> getMarkerIterator(@NotNull File file);

    /**
     * Removes all markers from all files.
     */
    public void removeAllMarkers();

    /**
     * Returns an iterator for all markers on all files.
     * 
     * @return Iterator or NULL if there are no files with markers.
     */
    @Nullable
    public Iterator<? extends FileMarker> getMarkerIterator();

}
