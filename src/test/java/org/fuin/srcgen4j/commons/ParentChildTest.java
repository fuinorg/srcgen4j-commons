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
import static org.assertj.core.api.Assertions.entry;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.Test;

/**
 * Tests parent/child variable inheritance.
 */
public class ParentChildTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testVariableInheritance() throws Exception {

        // TEST
        final String resVal = "/**\n * Test /var/tmp.\n */";
        final String escapesVal = "\r\n\t";
        String rootVal = "/var/tmp";
        String pathVal = "${root}/example";

        final String xml = read("parent-child.xml");
        final ParentElement parent = unmarshal(
                new UnmarshallerBuilder().addClassesToBeBound(ParentElement.class, ChildElement.class).build(), xml);
        parent.init();

        // VERIFY

        assertThat(parent.getVariables()).containsOnly(new Variable("root", rootVal), new Variable("path", pathVal),
                new Variable("res", resVal), new Variable("escapes", escapesVal));
        assertThat(parent.getVarMap()).containsOnly(entry("root", rootVal), entry("path", "/var/tmp/example"), entry("res", resVal),
                entry("escapes", escapesVal));

        assertThat(parent.getChilds()).hasSize(1);

        final ChildElement child = parent.getChilds().get(0);

        assertThat(child.getVariables()).containsOnly(new Variable("path", pathVal));
        assertThat(child.getVarMap()).containsOnly(entry("root", rootVal), entry("path", "/var/tmp/example/child"), entry("res", resVal),
                entry("escapes", escapesVal));

    }

    private static String read(final String resource) {
        try {
            final InputStream in = ParentChildTest.class.getClassLoader().getResourceAsStream(resource);
            try {
                return IOUtils.toString(in, "utf-8");
            } finally {
                in.close();
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
