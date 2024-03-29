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
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link GeneratorConfig}.
 */
public class GeneratorConfigTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(GeneratorConfig.class);
        final Validator validator = createPojoValidatorBuilder().build();
        validator.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(GeneratorConfig.class, Artifact.class);
        final GeneratorConfig testee = new GeneratorConfig("NAME", "CLASS", "PARSER", "PRJ", "FLD");
        testee.addArtifact(new Artifact("NAME", "PROJECT", "FOLDER"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        XmlAssert.assertThat(result)
                .and(XML + "<sg4jc:generator class=\"CLASS\" parser=\"PARSER\" name=\"NAME\" project=\"PRJ\" folder=\"FLD\""
                        + " xmlns:sg4jc=\"" + NS_SG4JC + "\">" + "<sg4jc:artifact name=\"NAME\" project=\"PROJECT\" folder=\"FOLDER\"/>"
                        + "</sg4jc:generator>")
                .areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(GeneratorConfig.class, Folder.class);

        // TEST
        final GeneratorConfig testee = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(),
                "<generator name=\"abc\" " + "project=\"def\" folder=\"ghi\" xmlns=\"" + NS_SG4JC + "\">"
                        + "<artifact name=\"NAME\" project=\"PROJECT\" folder=\"FOLDER\"/></generator>");

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("abc");
        assertThat(testee.getProject()).isEqualTo("def");
        assertThat(testee.getFolder()).isEqualTo("ghi");
        assertThat(testee.getArtifacts()).isNotNull();
        assertThat(testee.getArtifacts()).hasSize(1);
        assertThat(testee.getArtifacts().get(0).getName()).isEqualTo("NAME");
        assertThat(testee.getArtifacts().get(0).getProject()).isEqualTo("PROJECT");
        assertThat(testee.getArtifacts().get(0).getFolder()).isEqualTo("FOLDER");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Generators parent = new Generators();
        final GeneratorConfig testee = new GeneratorConfig("A${a}A", "CLASS", "PARSER", "${b}2B", "C3${c}");
        testee.addArtifact(new Artifact("A ${x}", "${y}B", "a${z}c"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "1");
        vars.put("b", "B");
        vars.put("c", "C");
        vars.put("x", "NAME");
        vars.put("y", "PRJ");
        vars.put("z", "b");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getName()).isEqualTo("A1A");
        assertThat(testee.getProject()).isEqualTo("B2B");
        assertThat(testee.getFolder()).isEqualTo("C3C");
        final Artifact artifact = testee.getArtifacts().get(0);
        assertThat(artifact.getName()).isEqualTo("A NAME");
        assertThat(artifact.getProject()).isEqualTo("PRJB");
        assertThat(artifact.getFolder()).isEqualTo("abc");

    }

    @Test
    public final void testGetDefProjectAndFolder() {

        // PREPARE
        final SrcGen4JConfig config = new SrcGen4JConfig();
        final Generators generators = new Generators();
        final GeneratorConfig testee = new GeneratorConfig("NAME1", "a.b.c.D", "PARSER1");

        config.setGenerators(generators);
        generators.addGenerator(testee);

        config.init(new DefaultContext(), new File("."));

        // TEST & VERIFY

        // No value set in hierarchy
        assertThat(testee.getDefProject()).isNull();
        assertThat(testee.getDefFolder()).isNull();

        // Generator level
        testee.setProject("C");
        testee.setFolder("D");
        assertThat(testee.getDefProject()).isEqualTo("C");
        assertThat(testee.getDefFolder()).isEqualTo("D");
        testee.setProject(null);
        testee.setFolder(null);

        // Generators level
        generators.setProject("E");
        generators.setFolder("F");
        assertThat(testee.getDefProject()).isEqualTo("E");
        assertThat(testee.getDefFolder()).isEqualTo("F");
        generators.setProject(null);
        generators.setFolder(null);

    }

    // CHECKSTYLE:ON

}
