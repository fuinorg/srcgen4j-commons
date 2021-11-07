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

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.rule.impl.SetterMustExistRule;

/**
 * Tests for {@link JaxbHelper}.
 */
public class JaxbHelperTest extends AbstractTest {

    // CHECKSTYLE:OFF Tests

    private JaxbHelper testee;

    @Before
    public void setup() {
        testee = new JaxbHelper();
    }

    @After
    public void teardown() {
        testee = null;
    }

    @Test
    public void testContainsStartTag() {

        // PREPARE
        final File file = new File("./src/test/resources/containsStartTag.xml");

        // TEST & VERIFY
        assertThat(testee.containsStartTag(file, "test-tag")).isTrue();
        assertThat(testee.containsStartTag(file, "abc-xyz")).isFalse();

    }

    @Test
    public void testCreateReaderJAXBContext() throws Exception {

        // PREPARE
        final Reader reader = new StringReader("<dummy name=\"Joe\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>");
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);

        // TEST
        final Dummy dummy = testee.create(reader, jaxbContext);

        // VERIFY
        assertThat(dummy).isNotNull();
        assertThat(dummy.getName()).isEqualTo("Joe");

    }

    @Test
    public void testCreateFileJAXBContext() throws Exception {

        // PREPARE
        final File file = new File("./src/test/resources/dummy.xml");
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);

        // TEST
        final Dummy dummy = testee.create(file, jaxbContext);

        // VERIFY
        assertThat(dummy).isNotNull();
        assertThat(dummy.getName()).isEqualTo("Joe");

    }

    @Test
    public void testCreateStringJAXBContext() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);

        // TEST
        final Dummy dummy = testee.create("<dummy name=\"Joe\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>", jaxbContext);

        // VERIFY
        assertThat(dummy).isNotNull();
        assertThat(dummy.getName()).isEqualTo("Joe");

    }

    @Test
    public void testWriteTYPEFileJAXBContext() throws Exception {

        // PREPARE
        final File expected = new File("./src/test/resources/dummy.xml");
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);
        final File file = File.createTempFile("test", "xml");
        file.deleteOnExit();
        final Dummy dummy = new Dummy();
        dummy.setName("Joe");
        testee.setFormattedOutput(false);

        // TEST
        testee.write(dummy, file, jaxbContext);

        // VERIFY
        assertThat(file).exists();
        assertThat(file).hasSameTextualContentAs(expected);

    }

    @Test
    public void testWriteTYPEJAXBContext() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);
        final Dummy dummy = new Dummy();
        dummy.setName("Joe");
        testee.setFormattedOutput(false);

        // TEST
        final String result = testee.write(dummy, jaxbContext);

        // VERIFY
        XmlAssert.assertThat(result).and(XML + "<sg4jc:dummy name=\"Joe\" xmlns:sg4jc=\"http://www.fuin.org/srcgen4j/commons\"/>")
                .areIdentical();

    }

    @Test
    public void testWriteTYPEWriterJAXBContext() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);
        final Writer writer = new StringWriter();
        final Dummy dummy = new Dummy();
        dummy.setName("Joe");
        testee.setFormattedOutput(false);

        // TEST
        testee.write(dummy, writer, jaxbContext);

        // VERIFY
        XmlAssert.assertThat(writer.toString())
                .and(XML + "<sg4jc:dummy name=\"Joe\" xmlns:sg4jc=\"http://www.fuin.org/srcgen4j/commons\"/>").areIdentical();

    }

    @Test
    public void testStructureAndBehaviour() {

        final PojoClass pc = PojoClassFactory.getPojoClass(JaxbHelper.class);
        final Validator validator = createPojoValidatorBuilder().with(new SetterMustExistRule()).build();
        validator.validate(pc);

    }

    // CHECKSTYLE:ON

}
