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
import static org.fuin.utils4j.jaxb.JaxbUtils.XML_PREFIX;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;
import static org.fuin.srcgen4j.commons.TestUtils.NS_SG4JC;
import static org.fuin.srcgen4j.commons.TestUtils.createPojoValidator;
import static org.fuin.srcgen4j.commons.TestUtils.NS_TEST;

import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link AbstractNamedElement}.
 */
public class AbstractNamedElementTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(MyElement.class);
        final Validator pv = createPojoValidator();
        pv.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement("NAME");

        // TEST
        final String result = marshal(testee, MyElement.class);

        // VERIFY
        XmlAssert.assertThat(result).and(
                XML_PREFIX + "<ns2:my-named-element name=\"NAME\" xmlns:sg4jc=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\"/>")
                .areIdentical();

    }

    @Test
    public final void testMarshalVariables() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement("NAME");
        testee.addVariable(new Variable("a", "1"));

        // TEST
        final String result = marshal(testee, MyElement.class);

        // VERIFY
        XmlAssert
                .assertThat(result).and(XML_PREFIX + "<ns2:my-named-element name=\"NAME\" xmlns:sg4jc=\"" + NS_SG4JC + "\""
                        + " xmlns:ns2=\"" + NS_TEST + "\">" + "<sg4jc:variable name=\"a\" value=\"1\"/>" + "</ns2:my-named-element>")
                .areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // TEST
        final MyElement testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(MyElement.class).build(),
                "<ns2:my-named-element name=\"NAME\" xmlns=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\"/>");

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVariables()).isNull();
        assertThat(testee.getVarMap()).hasSize(0);

    }

    @Test
    public final void testUnmarshalVariables() throws Exception {

        // TEST
        final MyElement testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(MyElement.class).build(),
                "<ns2:my-named-element name=\"NAME\" xmlns=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\">"
                        + "<variable value=\"1\" name=\"a\"/>" + "</ns2:my-named-element>");
        testee.inheritVariables(null);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVariables()).containsExactly(new Variable("a", "1"));
        assertThat(testee.getVarMap()).containsOnly(entry("a", "1"));

    }

    /**
     * Test class.
     */
    @XmlRootElement(name = "my-named-element", namespace = NS_TEST)
    private static final class MyElement extends AbstractNamedElement {

        @SuppressWarnings("unused")
        public MyElement() {
            super();
        }

        public MyElement(@NotEmpty String name) {
            super(name);
        }

    }

}
