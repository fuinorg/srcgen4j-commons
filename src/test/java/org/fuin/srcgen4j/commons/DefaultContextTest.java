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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link DefaultContext}.
 */
public class DefaultContextTest {

    // CHECKSTYLE:OFF

    private DefaultContext testee;

    @Before
    public void setup() {
        testee = new DefaultContext();
    }

    @After
    public void teardown() {
        testee = null;
    }

    @Test
    public void testAdd() {

        // PREPARE
        final File file = new File("./bla.txt");
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";

        // TEST
        final FileMarker marker = testee.addMarker(file, severity, message);

        // VERIFY
        assertThat(marker).isNotNull();
        assertThat(marker).isInstanceOf(DefaultFileMarker.class);
        final Iterator<? extends FileMarker> markerIterator = testee
                .getMarkerIterator();
        assertThat(markerIterator.next()).isSameAs(marker);
        assertThat(markerIterator.hasNext()).isFalse();

        final DefaultFileMarker dm = (DefaultFileMarker) marker;
        assertThat(dm.getSeverity()).isEqualTo(severity);
        assertThat(dm.getMessage()).isEqualTo(message);
        assertThat(dm.getLine()).isEqualTo(-1);
        assertThat(dm.getStart()).isEqualTo(-1);
        assertThat(dm.getLength()).isEqualTo(-1);

    }

    @Test
    public void testAddLine() {

        // PREPARE
        final File file = new File("./bla.txt");
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";
        final int line = 123;

        // TEST
        final FileMarker marker = testee.addMarker(file, severity, message,
                line);

        // VERIFY
        assertThat(marker).isNotNull();
        assertThat(marker).isInstanceOf(DefaultFileMarker.class);
        final Iterator<? extends FileMarker> markerIterator = testee
                .getMarkerIterator();
        assertThat(markerIterator.next()).isSameAs(marker);
        assertThat(markerIterator.hasNext()).isFalse();

        final DefaultFileMarker dm = (DefaultFileMarker) marker;
        assertThat(dm.getSeverity()).isEqualTo(severity);
        assertThat(dm.getMessage()).isEqualTo(message);
        assertThat(dm.getLine()).isEqualTo(line);
        assertThat(dm.getStart()).isEqualTo(-1);
        assertThat(dm.getLength()).isEqualTo(-1);

    }

    @Test
    public void testAddPos() {

        // PREPARE
        final File file = new File("./bla.txt");
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";
        final int start = 123;
        final int length = 45;

        // TEST
        final FileMarker marker = testee.addMarker(file, severity, message,
                start, length);

        // VERIFY
        assertThat(marker).isNotNull();
        assertThat(marker).isInstanceOf(DefaultFileMarker.class);
        final Iterator<? extends FileMarker> markerIterator = testee
                .getMarkerIterator();
        assertThat(markerIterator.next()).isSameAs(marker);
        assertThat(markerIterator.hasNext()).isFalse();

        final DefaultFileMarker dm = (DefaultFileMarker) marker;
        assertThat(dm.getSeverity()).isEqualTo(severity);
        assertThat(dm.getMessage()).isEqualTo(message);
        assertThat(dm.getLine()).isEqualTo(-1);
        assertThat(dm.getStart()).isEqualTo(start);
        assertThat(dm.getLength()).isEqualTo(length);

    }

    @Test
    public void testRemoveSingleMarkerFromFile() {

        // PREPARE

        // First file
        final File file1 = new File("./bla.txt");

        final FileMarkerSeverity severity1a = FileMarkerSeverity.INFO;
        final String message1a = "Abc123";
        final FileMarker marker1a = testee.addMarker(file1, severity1a,
                message1a);

        final FileMarkerSeverity severity1b = FileMarkerSeverity.ERROR;
        final String message1b = "Xyz456";
        final FileMarker marker1b = testee.addMarker(file1, severity1b,
                message1b);

        // Second file
        final File file2 = new File("./blub.txt");

        final FileMarkerSeverity severity2 = FileMarkerSeverity.INFO;
        final String message2 = "Abc123Def";
        final FileMarker marker2 = testee.addMarker(file2, severity2, message2);

        // TEST
        testee.removeMarker(file1, marker1a);

        // VERIFY
        Iterator<? extends FileMarker> markerIterator;

        markerIterator = testee.getMarkerIterator(file1);
        assertThat(markerIterator.next()).isSameAs(marker1b);
        assertThat(markerIterator.hasNext()).isFalse();

        markerIterator = testee.getMarkerIterator(file2);
        assertThat(markerIterator.next()).isSameAs(marker2);
        assertThat(markerIterator.hasNext()).isFalse();

    }

    @Test
    public void testRemoveAllMarkersFromFile() {

        // PREPARE

        // First file
        final File file1 = new File("./bla.txt");

        final FileMarkerSeverity severity1a = FileMarkerSeverity.INFO;
        final String message1a = "Abc123";
        testee.addMarker(file1, severity1a, message1a);

        final FileMarkerSeverity severity1b = FileMarkerSeverity.ERROR;
        final String message1b = "Xyz456";
        testee.addMarker(file1, severity1b, message1b);

        // Second file
        final File file2 = new File("./blub.txt");

        final FileMarkerSeverity severity2 = FileMarkerSeverity.INFO;
        final String message2 = "Abc123Def";
        final FileMarker marker2 = testee.addMarker(file2, severity2, message2);

        // TEST
        testee.removeAllMarkers(file1);

        // VERIFY
        Iterator<? extends FileMarker> markerIterator;

        markerIterator = testee.getMarkerIterator(file1);
        assertThat(markerIterator).isNull();

        markerIterator = testee.getMarkerIterator(file2);
        assertThat(markerIterator.next()).isSameAs(marker2);
        assertThat(markerIterator.hasNext()).isFalse();

    }

    @Test
    public void testRemoveAllMarkersFromAllFiles() {

        // PREPARE

        // First file
        final File file1 = new File("./bla.txt");

        final FileMarkerSeverity severity1a = FileMarkerSeverity.INFO;
        final String message1a = "Abc123";
        testee.addMarker(file1, severity1a, message1a);

        final FileMarkerSeverity severity1b = FileMarkerSeverity.ERROR;
        final String message1b = "Xyz456";
        testee.addMarker(file1, severity1b, message1b);

        // Second file
        final File file2 = new File("./blub.txt");

        final FileMarkerSeverity severity2 = FileMarkerSeverity.INFO;
        final String message2 = "Abc123Def";
        testee.addMarker(file2, severity2, message2);

        // TEST
        testee.removeAllMarkers();

        // VERIFY
        Iterator<? extends FileMarker> markerIterator;

        markerIterator = testee.getMarkerIterator(file1);
        assertThat(markerIterator).isNull();

        markerIterator = testee.getMarkerIterator(file2);
        assertThat(markerIterator).isNull();

    }

    // CHECKSTYLE:ON

}
