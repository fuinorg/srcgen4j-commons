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

import static org.fuin.units4j.AssertCoverage.assertEveryClassHasATest;

import java.io.File;

import org.fuin.units4j.AssertCoverage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * General tests for the project.
 */
public class GeneralTest {

    /**
     * Verifies the test coverage of the project.
     */
    @Disabled("Need to move units4j dependency from junit4 to junit5")
    @Test
    public final void testEveryClassHasATest() {
        assertEveryClassHasATest(new File("src/main/java"), new AssertCoverage.ClassFilter() {
            @Override
            public boolean isIncludeClass(final Class<?> clasz) {
                return !clasz.getName().endsWith("Exception") && !clasz.equals(SrcGen4JCommonsNamespace.class);
            }
        });
    }

}
