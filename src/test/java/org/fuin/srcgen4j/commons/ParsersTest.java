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
import static org.fest.assertions.MapAssert.entry;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;

/**
 * Tests for {@link Parsers}.
 */
public class ParsersTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Parsers.class);
        final PojoValidator pv = createPojoValidator();
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Parsers.class);
        final Parsers testee = new Parsers();
        testee.addVariable(new Variable("a", "1"));
        testee.addParser(new ParserConfig("NAME", "a.b.c.D"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result).isEqualTo(
                XML + "<parsers xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                        + "<variable value=\"1\" name=\"a\"/>"
                        + "<parser class=\"a.b.c.D\" name=\"NAME\"/>" + "</parsers>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Parsers.class);

        // TEST
        final Parsers testee = new JaxbHelper().create(
                "<parsers xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                        + "<parser name=\"NAME\" class=\"a.b.c.D\"/>"
                        + "<variable name=\"a\" value=\"1\"/>" + "</parsers>", jaxbContext);
        testee.inheritVariables(new HashMap<String, String>());

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVarMap()).includes(entry("a", "1"));
        assertThat(testee.getList()).isNotNull();
        assertThat(testee.getList()).hasSize(1);
        assertThat(testee.getList().get(0).getName()).isEqualTo("NAME");
        assertThat(testee.getList().get(0).getClassName()).isEqualTo("a.b.c.D");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final SrcGen4JConfig parent = new SrcGen4JConfig();
        final Parsers testee = new Parsers();
        testee.addParser(new ParserConfig("A ${x}", "CLASS"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("x", "NAME");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        final ParserConfig parser = testee.getList().get(0);
        assertThat(parser.getName()).isEqualTo("A NAME");

    }

    // CHECKSTYLE:ON

}
