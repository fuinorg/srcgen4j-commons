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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link BinClasspathEntry}.
 */
public class BinClasspathEntryTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(BinClasspathEntry.class);
        final Validator validator = createPojoValidatorBuilder().build();
        validator.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(BinClasspathEntry.class);
        final BinClasspathEntry testee = new BinClasspathEntry("a/b/c");

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        XmlAssert.assertThat(result).and(XML
                + "<sg4jc:bin path=\"a/b/c\" xmlns=\"http://www.fuin.org/xmlcfg4j\" xmlns:sg4jc=\"http://www.fuin.org/srcgen4j/commons\"/>")
                .areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(BinClasspathEntry.class);

        // TEST
        final BinClasspathEntry testee = new JaxbHelper().create("<bin path=\"a/b/c\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>",
                jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getPath()).isEqualTo("a/b/c");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Classpath parent = new Classpath();
        final BinClasspathEntry testee = new BinClasspathEntry("${x}/y/z");

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("x", "PATH");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getPath()).isEqualTo("PATH/y/z");

    }

    // CHECKSTYLE:ON

}
