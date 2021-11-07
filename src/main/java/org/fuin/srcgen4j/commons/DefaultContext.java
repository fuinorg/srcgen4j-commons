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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.fuin.objects4j.common.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a context for the build process that just stores the markers and logs the add messages.
 */
public final class DefaultContext implements SrcGen4JContext, FileMarkerCapable {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultContext.class);

    @NotNull
    private final Map<File, Set<DefaultFileMarker>> markers;

    @NotNull
    private final ClassLoader classLoader;

    @NotNull
    private final List<File> jarFiles;

    @NotNull
    private final List<File> binDirs;

    /**
     * Default constructor.
     */
    public DefaultContext() {
        this(DefaultContext.class.getClassLoader());
    }

    /**
     * Constructor without class path information.
     * 
     * @param classLoader
     *            Class loader to use in this context.
     */
    public DefaultContext(@NotNull final ClassLoader classLoader) { // NOSONAR Ignore not initialized fields
        super();
        Contract.requireArgNotNull("classLoader", classLoader);
        this.markers = new HashMap<File, Set<DefaultFileMarker>>();
        this.classLoader = classLoader;
        this.jarFiles = Collections.unmodifiableList(new ArrayList<>());
        this.binDirs = Collections.unmodifiableList(new ArrayList<>());
    }

    /**
     * Constructor with class path information.
     * 
     * @param classLoader
     *            Class loader to use in this context.
     * @param cp
     *            Class path with JAR files and binary directories.
     */
    public DefaultContext(@NotNull final ClassLoader classLoader, @NotNull final List<File> cp) {
        super();
        Contract.requireArgNotNull("classLoader", classLoader);
        Contract.requireArgNotNull("cp", cp);

        markers = new HashMap<File, Set<DefaultFileMarker>>();
        this.classLoader = classLoader;
        final List<File> files = new ArrayList<>();
        final List<File> dirs = new ArrayList<>();
        for (final File file : cp) {
            if (file.isDirectory()) {
                dirs.add(file);
            } else {
                files.add(file);
            }
        }
        jarFiles = Collections.unmodifiableList(files);
        binDirs = Collections.unmodifiableList(dirs);
    }

    private DefaultFileMarker add(final File file, final DefaultFileMarker marker) {
        Set<DefaultFileMarker> set = markers.get(file);
        if (set == null) {
            set = new HashSet<>();
            markers.put(file, set);
        }
        set.add(marker);
        if (marker.getSeverity() == FileMarkerSeverity.ERROR) {
            LOG.error(marker.toString());
        }
        if (marker.getSeverity() == FileMarkerSeverity.WARNING) {
            LOG.warn(marker.toString());
        }
        if (marker.getSeverity() == FileMarkerSeverity.INFO) {
            LOG.info(marker.toString());
        }
        return marker;
    }

    @Override
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity, final String message) {
        return add(file, new DefaultFileMarker(severity, message));
    }

    @Override
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity, final String message, final int line) {
        return add(file, new DefaultFileMarker(severity, message, line));
    }

    @Override
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity, final String message, final int start,
            final int length) {
        return add(file, new DefaultFileMarker(severity, message, start, length));
    }

    @Override
    public final Iterator<? extends FileMarker> getMarkerIterator() {
        final Set<DefaultFileMarker> all = new HashSet<>();
        final Iterator<File> fileIt = markers.keySet().iterator();
        while (fileIt.hasNext()) {
            final Set<DefaultFileMarker> set = markers.get(fileIt.next());
            for (final DefaultFileMarker marker : set) {
                all.add(marker);
            }
        }
        return all.iterator();
    }

    @Override
    public final Iterator<? extends FileMarker> getMarkerIterator(final File file) {
        final Set<DefaultFileMarker> set = markers.get(file);
        if (set == null) {
            return null;
        }
        return set.iterator();
    }

    @Override
    public final void removeAllMarkers() {
        markers.clear();
    }

    @Override
    public final void removeAllMarkers(final File file) {
        markers.remove(file);
    }

    @Override
    public final void removeMarker(final File file, final FileMarker marker) {
        final Set<DefaultFileMarker> set = markers.get(file);
        if (set != null) {
            set.remove(marker);
        }
    }

    @Override
    public final ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public final List<File> getJarFiles() {
        return jarFiles;
    }

    @Override
    public final List<File> getBinDirs() {
        return binDirs;
    }

}
