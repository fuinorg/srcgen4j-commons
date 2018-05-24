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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;

/**
 * Test for {@link DefaultFileMarker}.
 */
public class DefaultFileMarkerTest {

    // CHECKSTYLE:OFF

    @Test
    public void testPojo() {
        final PojoValidator pv = new PojoValidator();
        pv.addRule(new NoPublicFieldsRule());
        pv.addRule(new NoFieldShadowingRule());
        pv.addRule(new GetterMustExistRule());
    }

    @Test
    public void testConstruction() {

        // PREPARE
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";

        // TEST
        final DefaultFileMarker testee = new DefaultFileMarker(severity, message);

        // VERIFY
        assertThat(testee.getSeverity()).isEqualTo(severity);
        assertThat(testee.getMessage()).isEqualTo(message);
        assertThat(testee.getLine()).isEqualTo(-1);
        assertThat(testee.getStart()).isEqualTo(-1);
        assertThat(testee.getLength()).isEqualTo(-1);

    }

    @Test
    public void testConstructionLine() {

        // PREPARE
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";
        final int line = 123;

        // TEST
        final DefaultFileMarker testee = new DefaultFileMarker(severity, message, line);

        // VERIFY
        assertThat(testee.getSeverity()).isEqualTo(severity);
        assertThat(testee.getMessage()).isEqualTo(message);
        assertThat(testee.getLine()).isEqualTo(line);
        assertThat(testee.getStart()).isEqualTo(-1);
        assertThat(testee.getLength()).isEqualTo(-1);

    }

    @Test
    public void testConstructionPos() {

        // PREPARE
        final FileMarkerSeverity severity = FileMarkerSeverity.INFO;
        final String message = "Abc123Def";
        final int start = 123;
        final int length = 45;

        // TEST
        final DefaultFileMarker testee = new DefaultFileMarker(severity, message, start, length);

        // VERIFY
        assertThat(testee.getSeverity()).isEqualTo(severity);
        assertThat(testee.getMessage()).isEqualTo(message);
        assertThat(testee.getLine()).isEqualTo(-1);
        assertThat(testee.getStart()).isEqualTo(start);
        assertThat(testee.getLength()).isEqualTo(length);

    }

    // CHECKSTYLE:ON

}
