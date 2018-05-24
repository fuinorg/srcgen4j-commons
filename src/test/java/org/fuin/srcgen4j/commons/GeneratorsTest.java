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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.custommonkey.xmlunit.XMLAssert;
import org.fuin.xmlcfg4j.Variable;
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
        testee.addVariable(new Variable("a", "1"));
        testee.addGenerator(new GeneratorConfig("NAME", "a.b.c.D", "PARSER", "PROJECT", "FOLDER"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        XMLAssert.assertXMLEqual(XML
                + "<ns2:generators project=\"abc\" folder=\"def\" xmlns=\"http://www.fuin.org/xmlcfg4j\" xmlns:ns2=\"http://www.fuin.org/srcgen4j/commons\">"
                + "<variable value=\"1\" name=\"a\"/>" + "<ns2:generator class=\"a.b.c.D\" parser=\"PARSER\" name=\"NAME\""
                + " project=\"PROJECT\" folder=\"FOLDER\"/>" + "</ns2:generators>", result);

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Generators.class);

        // TEST
        final Generators testee = new JaxbHelper().create(
                "<ns2:generators project=\"abc\" folder=\"def\" xmlns=\"http://www.fuin.org/xmlcfg4j\" xmlns:ns2=\"http://www.fuin.org/srcgen4j/commons\">"
                        + "<ns2:generator name=\"NAME\" project=\"PROJECT\" folder=\"FOLDER\"/>" + "<variable name=\"a\" value=\"1\"/>"
                        + "</ns2:generators>",
                jaxbContext);
        testee.init(new DefaultContext(), null, new HashMap<>());

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVarMap()).containsOnly(entry("a", "1"));
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
        testee.addGenerator(new GeneratorConfig("A ${x}", "CLASS", "PARSER", "${y}B", "a${z}c"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");
        vars.put("b", "B");
        vars.put("x", "NAME");
        vars.put("y", "PRJ");
        vars.put("z", "b");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getProject()).isEqualTo("A1A");
        assertThat(testee.getFolder()).isEqualTo("B2B");
        final GeneratorConfig generator = testee.getList().get(0);
        assertThat(generator.getName()).isEqualTo("A NAME");
        assertThat(generator.getProject()).isEqualTo("PRJB");
        assertThat(generator.getFolder()).isEqualTo("abc");

    }

    // CHECKSTYLE:ON

}
