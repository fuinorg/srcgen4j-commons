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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

/**
 * Tests for {@link ArtifactFactoryConfig}.
 */
public class ArtifactFactoryConfigTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(ArtifactFactoryConfig.class);
        final ArtifactFactoryConfig testee = new ArtifactFactoryConfig("abc", "a.b.c.X");

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result).isEqualTo(
                XML + "<artifact-factory artifact=\"abc\" class=\"a.b.c.X\""
                        + " xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(ArtifactFactoryConfig.class);

        // TEST
        final ArtifactFactoryConfig testee = new JaxbHelper().create(
                "<artifact-factory artifact=\"abc\" class=\"a.b.c.X\""
                        + " xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>", jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getArtifact()).isEqualTo("abc");
        assertThat(testee.getFactoryClassName()).isEqualTo("a.b.c.X");

    }

    @Test
    public final void testGetFactory() {

        // PREPARE
        final ArtifactFactoryConfig testee = new ArtifactFactoryConfig("abc",
                "org.fuin.srcgen4j.commons.TestArtifactFactory");
        testee.init(new SrcGen4JContext() {
            @Override
            public ClassLoader getClassLoader() {
                return ArtifactFactoryConfigTest.class.getClassLoader();
            }

            @Override
            public List<File> getJarFiles() {
                return new ArrayList<File>();
            }

            @Override
            public List<File> getBinDirs() {
                return new ArrayList<File>();
            }
        }, new HashMap<String, String>());

        // TEST
        final ArtifactFactory<?> factory = testee.getFactory();

        // VERIFY
        assertThat(factory).isInstanceOf(TestArtifactFactory.class);

    }

    // CHECKSTYLE:ON

}
