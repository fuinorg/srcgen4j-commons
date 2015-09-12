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
 * Tests for {@link ParserConfig}.
 */
public class ParserConfigTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(ParserConfig.class);
        final PojoValidator pv = createPojoValidator();
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(
                ParserConfig.class, TestInput.class);
        final ParserConfig testee = new ParserConfig("parser1",
                "a.b.c.TestParser");
        final TestInput testInput = new TestInput("a/b/c");
        final Config<ParserConfig> config = new Config<ParserConfig>(testInput);
        testee.setConfig(config);

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result)
                .isEqualTo(
                        XML
                                + "<parser class=\"a.b.c.TestParser\" name=\"parser1\" "
                                + "xmlns=\"http://www.fuin.org/srcgen4j/commons\" "
                                + "xmlns:ns2=\"http://www.fuin.org/srcgen4j/commons/test\">"
                                + "<config><ns2:input path=\"a/b/c\"/></config>"
                                + "</parser>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(
                ParserConfig.class, TestInput.class);

        // TEST
        final ParserConfig testee = new JaxbHelper()
                .create("<parser class=\"a.b.c.TestParser\" name=\"parser1\" "
                        + "xmlns:ns2=\"http://www.fuin.org/srcgen4j/commons/test\" "
                        + "xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                        + "<config><ns2:input path=\"a/b/c\"/></config>"
                        + "</parser>", jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("parser1");
        assertThat(testee.getClassName()).isEqualTo("a.b.c.TestParser");
        assertThat(testee.getConfig()).isNotNull();
        assertThat(testee.getConfig().getConfig())
                .isInstanceOf(TestInput.class);
        final TestInput testInput = (TestInput) testee.getConfig().getConfig();
        assertThat(testInput.getPath()).isEqualTo("a/b/c");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Parsers parent = new Parsers();
        final ParserConfig testee = new ParserConfig("parser${a}",
                "a.${x}.c.TestParser");
        final TestInput testInput = new TestInput("a/b/${y}");
        final Config<ParserConfig> config = new Config<ParserConfig>(testInput);
        testee.setConfig(config);

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");
        vars.put("x", "b");
        vars.put("y", "c");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getName()).isEqualTo("parser1");
        assertThat(testee.getClassName()).isEqualTo("a.b.c.TestParser");
        assertThat(testInput.getPath()).isEqualTo("a/b/c");

    }

    // CHECKSTYLE:ON

}
