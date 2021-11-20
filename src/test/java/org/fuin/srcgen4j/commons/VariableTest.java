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
import static org.fuin.utils4j.jaxb.JaxbUtils.XML_PREFIX;
import static org.fuin.utils4j.jaxb.JaxbUtils.marshal;
import static org.fuin.utils4j.jaxb.JaxbUtils.unmarshal;
import static org.fuin.srcgen4j.commons.TestUtils.NS_SG4JC;
import static org.fuin.srcgen4j.commons.TestUtils.createPojoValidator;

import java.util.HashMap;
import java.util.Map;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link Variable}.
 */
public class VariableTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Variable.class);
        final Validator pv = createPojoValidator();
        pv.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final Variable testee = new Variable("abc", "def");

        // TEST
        final String result = marshal(testee, Variable.class);

        // VERIFY
        XmlAssert.assertThat(result).and(XML_PREFIX + "<sg4jc:variable name=\"abc\" value=\"def\" " + "xmlns:sg4jc=\"" + NS_SG4JC + "\"/>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // TEST
        final Variable testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(Variable.class).build(),
                "<sg4jc:variable name=\"abc\" value=\"def\" xmlns:sg4jc=\"" + NS_SG4JC + "\"/>");

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("abc");
        assertThat(testee.getValue()).isEqualTo("def");

    }

    @Test
    public final void testHashCodeEquals() {
        EqualsVerifier.forClass(Variable.class).withIgnoredFields("value", "urlStr", "encoding").suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    public final void testInit() {

        // PREPARE
        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");

        final Variable testee = new Variable("x", "${a}");

        // TEST
        testee.init(vars);

        // VERIFY
        assertThat(testee.getValue()).isEqualTo("1");

    }

    // CHECKSTYLE:ON

}
