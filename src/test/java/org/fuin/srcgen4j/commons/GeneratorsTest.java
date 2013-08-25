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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;

/**
 * Tests for {@link Generators}.
 */
public class GeneratorsTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Generators.class);
        final PojoValidator pv = createPojoValidator();
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Generators.class);
        final Generators testee = new Generators("abc", "def");
        testee.addGenerator(new Generator("NAME", "PROJECT", "FOLDER"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result)
                .isEqualTo(
                        XML
                                + "<generators project=\"abc\" "
                                + "folder=\"def\" xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                                + "<generator name=\"NAME\" project=\"PROJECT\" folder=\"FOLDER\"/></generators>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Generators.class);

        // TEST
        final Generators testee = new JaxbHelper()
                .create("<generators project=\"abc\" folder=\"def\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"><generator name=\"NAME\" project=\"PROJECT\" folder=\"FOLDER\"/></generators>",
                        jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getProject()).isEqualTo("abc");
        assertThat(testee.getFolder()).isEqualTo("def");
        assertThat(testee.getList()).isNotNull();
        assertThat(testee.getList()).hasSize(1);
        assertThat(testee.getList().get(0).getName()).isEqualTo("NAME");
        assertThat(testee.getList().get(0).getProject()).isEqualTo("PROJECT");
        assertThat(testee.getList().get(0).getFolder()).isEqualTo("FOLDER");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final SrcGen4JConfig parent = new SrcGen4JConfig();
        final Generators testee = new Generators("A${a}A", "${b}2B");
        testee.addGenerator(new Generator("A ${x}", "${y}B", "a${z}c"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");
        vars.put("b", "B");
        vars.put("x", "NAME");
        vars.put("y", "PRJ");
        vars.put("z", "b");

        // TEST
        testee.init(parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getProject()).isEqualTo("A1A");
        assertThat(testee.getFolder()).isEqualTo("B2B");
        final Generator generator = testee.getList().get(0);
        assertThat(generator.getName()).isEqualTo("A NAME");
        assertThat(generator.getProject()).isEqualTo("PRJB");
        assertThat(generator.getFolder()).isEqualTo("abc");

    }

    // CHECKSTYLE:ON

}
