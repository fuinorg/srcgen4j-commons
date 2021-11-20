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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.fuin.utils4j.VariableResolver;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link AbstractElement}.
 */
public class AbstractElementTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(MyElement.class);
        final Validator pv = createPojoValidator();
        pv.validate(pc);

    }

    @Test
    public final void testInheritVariables() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement();
        testee.addVariable(new Variable("a", "A"));
        testee.addVariable(new Variable("b", "B"));
        testee.addVariable(new Variable("c", "C"));

        final Map<String, String> parentVars = new HashMap<>();
        parentVars.put("b", "1");
        parentVars.put("d", "D");

        // TEST
        testee.inheritVariables(parentVars);

        // VERIFY
        assertThat(testee.getVariables()).containsOnly(new Variable("a", "A"), new Variable("b", "B"), new Variable("c", "C"));
        assertThat(testee.getVarMap()).containsOnly(entry("a", "A"), entry("b", "B"), entry("c", "C"), entry("d", "D"));

    }

    @Test
    public final void testResolvingVariables() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement();
        testee.addVariable(new Variable("a", "${root}/A"));
        testee.addVariable(new Variable("b", "${a}/B"));
        testee.addVariable(new Variable("c", "${b}/C"));

        final Map<String, String> unresolvedParentVars = new HashMap<>();
        unresolvedParentVars.put("root", "ROOT");
        unresolvedParentVars.put("c", "C");
        unresolvedParentVars.put("d", "${b}/D");
        final Map<String, String> parentVars = new VariableResolver(unresolvedParentVars).getResolved();

        // TEST
        testee.inheritVariables(parentVars);

        // VERIFY
        assertThat(testee.getVariables()).containsOnly(new Variable("a", "${root}/a"), new Variable("b", "${a}/b"),
                new Variable("c", "${b}/c"));
        assertThat(testee.getVarMap()).containsOnly(entry("root", "ROOT"), entry("a", "ROOT/A"), entry("b", "ROOT/A/B"),
                entry("c", "ROOT/A/B/C"), entry("d", "ROOT/A/B/D"));

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement();

        // TEST
        final String result = marshal(testee, MyElement.class);

        // VERIFY
        XmlAssert.assertThat(result)
                .and(XML_PREFIX + "<ns2:my-element xmlns:sg4jc=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\"/>").areIdentical();

    }

    @Test
    public final void testMarshalVariables() throws Exception {

        // PREPARE
        final MyElement testee = new MyElement();
        testee.addVariable(new Variable("a", "1"));

        // TEST
        final String result = marshal(testee, MyElement.class);

        // VERIFY
        XmlAssert.assertThat(result).and(XML_PREFIX + "<ns2:my-element xmlns:sg4jc=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\">"
                + "<sg4jc:variable name=\"a\" value=\"1\"/>" + "</ns2:my-element>").areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // TEST
        final MyElement testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(MyElement.class).build(),
                "<ns2:my-element xmlns=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\"/>");

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVariables()).isNull();
        assertThat(testee.getVarMap()).hasSize(0);

    }

    @Test
    public final void testUnmarshalVariables() throws Exception {

        // TEST
        final MyElement testee = unmarshal(new UnmarshallerBuilder().addClassesToBeBound(MyElement.class).build(),
                "<ns2:my-element xmlns=\"" + NS_SG4JC + "\"" + " xmlns:ns2=\"" + NS_TEST + "\">" + "<variable value=\"1\" name=\"a\"/>"
                        + "</ns2:my-element>");
        testee.inheritVariables(null);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getVariables()).containsExactly(new Variable("a", "1"));
        assertThat(testee.getVarMap()).containsOnly(entry("a", "1"));

    }

    /**
     * Test class.
     */
    @XmlRootElement(name = "my-element", namespace = NS_TEST)
    private static class MyElement extends AbstractElement {

    }

}
