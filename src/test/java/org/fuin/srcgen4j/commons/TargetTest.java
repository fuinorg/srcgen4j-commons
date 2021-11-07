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

import org.junit.Test;
import org.xmlunit.assertj3.XmlAssert;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * Tests for {@link Target}.
 */
public class TargetTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Target.class);

        final Validator validator = ValidatorBuilder.create().with(new NoPublicFieldsRule()).with(new NoFieldShadowingRule())
                .with(new DefaultValuesNullTester()).with(new SetterTester()).with(new GetterTester()).build();

        validator.validate(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Target.class, AbstractTarget.class);
        final Target testee = new Target(".*", "abc", "def");

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        XmlAssert.assertThat(result).and(XML + "<sg4jc:target pattern=\".*\" "
                + "project=\"abc\" folder=\"def\" xmlns:sg4jc=\"http://www.fuin.org/srcgen4j/commons\"/>").areIdentical();

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Target.class, AbstractTarget.class);

        // TEST
        final Target testee = new JaxbHelper().create(
                "<target pattern=\".*\" " + "project=\"abc\" folder=\"def\" xmlns=\"http://www.fuin.org/srcgen4j/commons\"/>", jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getPattern()).isEqualTo(".*");
        assertThat(testee.getProject()).isEqualTo("abc");
        assertThat(testee.getFolder()).isEqualTo("def");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Artifact parent = new Artifact();
        final Target testee = new Target("${a}name", "t${b}rj", "xy${c}");

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("a", "t");
        vars.put("b", "p");
        vars.put("c", "z");

        // TEST
        testee.init(new DefaultContext(), parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getPattern()).isEqualTo("tname");
        assertThat(testee.getProject()).isEqualTo("tprj");
        assertThat(testee.getFolder()).isEqualTo("xyz");

    }

    @Test
    public final void testMatches() {

        // PREPARE
        final Artifact parent = new Artifact();

        // TEST & VERIFY
        assertThat(new Target("MyName\\.java", "prj", "folder").init(new DefaultContext(), parent, null).matches("MyName.java")).isTrue();
        assertThat(new Target(".*MyName\\.java", "prj", "folder").init(new DefaultContext(), parent, null).matches("MyName.java")).isTrue();
        assertThat(new Target(".*MyName\\.java", "prj", "folder").init(new DefaultContext(), parent, null).matches("/MyName.java"))
                .isTrue();
        assertThat(new Target(".*a/b/c", "prj", "folder").init(new DefaultContext(), parent, null).matches("a/b/c/MyName.java")).isTrue();

        assertThat(new Target("MyName\\.java", "prj", "folder").init(new DefaultContext(), parent, null).matches("Another.java")).isFalse();

    }

    @Test
    public final void testGetDefProjectAndFolder() {

        // PREPARE
        final SrcGen4JConfig config = new SrcGen4JConfig();
        final Generators generators = new Generators();
        final GeneratorConfig generator = new GeneratorConfig("NAME1", "a.b.c.D", "PARSER1");
        final Artifact artifact = new Artifact("ARTIFACT");
        final Target testee = new Target();

        config.setGenerators(generators);
        generators.addGenerator(generator);
        generator.addArtifact(artifact);
        artifact.addTarget(testee);

        config.init(new DefaultContext(), new File("."));

        // TEST & VERIFY

        // No value set in hierarchy
        assertThat(testee.getDefProject()).isNull();
        assertThat(testee.getDefFolder()).isNull();

        // Artifact level
        artifact.setProject("A");
        artifact.setFolder("B");
        assertThat(testee.getDefProject()).isEqualTo("A");
        assertThat(testee.getDefFolder()).isEqualTo("B");
        artifact.setProject(null);
        artifact.setFolder(null);

        // Generator level
        generator.setProject("C");
        generator.setFolder("D");
        assertThat(testee.getDefProject()).isEqualTo("C");
        assertThat(testee.getDefFolder()).isEqualTo("D");
        generator.setProject(null);
        generator.setFolder(null);

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
