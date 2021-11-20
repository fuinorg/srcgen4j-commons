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

import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link Variables}.
 */
public class VariablesTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Variables.class);
        final Validator pv = createPojoValidator();
        pv.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final Variables testee = new Variables(new Variable("abc", "def"));

        // TEST
        final String result = marshal(testee, Variables.class);

        // VERIFY
        XmlAssert.assertThat(result).and(XML_PREFIX + "<sg4jc:variables xmlns:sg4jc=\"" + NS_SG4JC + "\">"
                + "<sg4jc:variable name=\"abc\" value=\"def\"/>" + "</sg4jc:variables>").areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // TEST
        final Variables testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(Variables.class).build(),
                "<sg4jc:variables xmlns:sg4jc=\"" + NS_SG4JC + "\">" + "<sg4jc:variable name=\"abc\" value=\"def\"/>"
                        + "</sg4jc:variables>");

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.asList()).containsOnly(new Variable("abc", "def"));

    }

    // CHECKSTYLE:ON

}
