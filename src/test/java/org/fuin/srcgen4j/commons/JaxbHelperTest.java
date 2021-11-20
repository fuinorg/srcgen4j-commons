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

import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    public void setup() {
        testee = new JaxbHelper();
    }

    @AfterEach
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
        final Reader reader = new StringReader("<dummy name=\"Joe\" xmlns=\""+ NS_SG4JC +"\"/>");
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);

        // TEST
        final Dummy dummy = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(), reader);

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
        final Dummy dummy = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(), file);

        // VERIFY
        assertThat(dummy).isNotNull();
        assertThat(dummy.getName()).isEqualTo("Joe");

    }

    @Test
    public void testCreateStringJAXBContext() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Dummy.class);

        // TEST
        final Dummy dummy = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(),
                "<dummy name=\"Joe\" xmlns=\""+ NS_SG4JC + "\"/>");

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
        XmlAssert.assertThat(result).and(XML + "<sg4jc:dummy name=\"Joe\" xmlns:sg4jc=\""+ NS_SG4JC + "\"/>")
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
                .and(XML + "<sg4jc:dummy name=\"Joe\" xmlns:sg4jc=\""+ NS_SG4JC + "\"/>").areIdentical();

    }

    @Test
    public void testStructureAndBehaviour() {

        final PojoClass pc = PojoClassFactory.getPojoClass(JaxbHelper.class);
        final Validator validator = createPojoValidatorBuilder().with(new SetterMustExistRule()).build();
        validator.validate(pc);

    }

    // CHECKSTYLE:ON

}
