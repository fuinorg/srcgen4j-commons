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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a context for the build process that just stores the markers and
 * logs the add messages.
 */
public final class DefaultContext implements SrcGen4JContext, FileMarkerCapable {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultContext.class);

    private final Map<File, Set<DefaultFileMarker>> markers;

    /**
     * Default constructor.
     */
    public DefaultContext() {
        super();
        markers = new HashMap<File, Set<DefaultFileMarker>>();
    }

    private DefaultFileMarker add(final File file, final DefaultFileMarker marker) {
        Set<DefaultFileMarker> set = markers.get(file);
        if (set == null) {
            set = new HashSet<DefaultFileMarker>();
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
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity,
            final String message) {
        return add(file, new DefaultFileMarker(severity, message));
    }

    @Override
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity,
            final String message, final int line) {
        return add(file, new DefaultFileMarker(severity, message, line));
    }

    @Override
    public final FileMarker addMarker(final File file, final FileMarkerSeverity severity,
            final String message, final int start, final int length) {
        return add(file, new DefaultFileMarker(severity, message, start, length));
    }

    @Override
    public final Iterator<? extends FileMarker> getMarkerIterator() {
        final Set<DefaultFileMarker> all = new HashSet<DefaultFileMarker>();
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
        return DefaultContext.class.getClassLoader();
    }

}
