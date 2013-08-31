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

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.SetterMustExistRule;

/**
 * Tests for {@link Artifact}.
 */
public class ArtifactTest extends AbstractTest {

    // CHECKSTYLE:OFF

    /**
     * Basic POJO test.
     */
    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(Artifact.class);
        final PojoValidator pv = createPojoValidator();
        pv.addRule(new SetterMustExistRule());
        pv.runValidation(pc);

    }

    @Test
    public final void testMarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Artifact.class, Target.class);
        final Artifact testee = new Artifact("abc", "def", "ghi");
        testee.addTarget(new Target("PATTERN", "PROJECT", "FOLDER"));

        // TEST
        final String result = new JaxbHelper(false).write(testee, jaxbContext);

        // VERIFY
        assertThat(result)
                .isEqualTo(
                        XML
                                + "<artifact name=\"abc\" "
                                + "project=\"def\" folder=\"ghi\" xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                                + "<target pattern=\"PATTERN\" project=\"PROJECT\" folder=\"FOLDER\"/></artifact>");

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(Artifact.class, Folder.class);

        // TEST
        final Artifact testee = new JaxbHelper().create("<artifact name=\"abc\" "
                + "project=\"def\" folder=\"ghi\" xmlns=\"http://www.fuin.org/srcgen4j/commons\">"
                + "<target pattern=\"PATTERN\" project=\"PROJECT\" folder=\"FOLDER\"/></artifact>",
                jaxbContext);

        // VERIFY
        assertThat(testee).isNotNull();
        assertThat(testee.getName()).isEqualTo("abc");
        assertThat(testee.getProject()).isEqualTo("def");
        assertThat(testee.getFolder()).isEqualTo("ghi");
        assertThat(testee.getTargets()).isNotNull();
        assertThat(testee.getTargets()).hasSize(1);
        assertThat(testee.getTargets().get(0).getPattern()).isEqualTo("PATTERN");
        assertThat(testee.getTargets().get(0).getProject()).isEqualTo("PROJECT");
        assertThat(testee.getTargets().get(0).getFolder()).isEqualTo("FOLDER");

    }

    @Test
    public final void testInit() {

        // PREPARE
        final Artifact testee = new Artifact("A ${x}", "${y}B", "a${z}c");
        testee.addTarget(new Target("${a}name", "t${b}rj", "xy${c}"));

        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("x", "NAME");
        vars.put("y", "PRJ");
        vars.put("z", "b");
        vars.put("a", "t");
        vars.put("b", "p");
        vars.put("c", "z");

        final GeneratorConfig parent = new GeneratorConfig();

        // TEST
        testee.init(parent, vars);

        // VERIFY
        assertThat(testee.getParent()).isSameAs(parent);
        assertThat(testee.getName()).isEqualTo("A NAME");
        assertThat(testee.getProject()).isEqualTo("PRJB");
        assertThat(testee.getFolder()).isEqualTo("abc");
        final Target target = testee.getTargets().get(0);
        assertThat(target.getPattern()).isEqualTo("tname");
        assertThat(target.getProject()).isEqualTo("tprj");
        assertThat(target.getFolder()).isEqualTo("xyz");

    }

    @Test
    public void testFindTargetForNoTargetsDefined() {

        // PREPARE
        final Artifact testee = new Artifact("A", "B", "C");

        // TEST & VERIFY
        assertThat(testee.findTargetFor("whatever")).isNull();

    }

    @Test
    public void testFindTargetForArgumentNull() {

        // PREPARE
        final Artifact testee = new Artifact("A", "B", "C");

        // TEST & VERIFY
        assertThat(testee.findTargetFor(null)).isNull();

    }

    @Test
    public void testFindTarget() {

        // PREPARE
        final GeneratorConfig parent = new GeneratorConfig();
        final Artifact testee = new Artifact("A", "B", "C");
        final Target targetA = new Target("a", "b", "c");
        final Target targetB = new Target("d", "e", "f");
        final Target targetC = new Target("g", "h", "i");
        testee.addTarget(targetA);
        testee.addTarget(targetB);
        testee.addTarget(targetC);
        testee.init(parent, new HashMap<String, String>());

        // TEST & VERIFY
        assertThat(testee.findTargetFor("a")).isSameAs(targetA);
        assertThat(testee.findTargetFor("d")).isSameAs(targetB);
        assertThat(testee.findTargetFor("g")).isSameAs(targetC);

    }

    @Test
    public final void testGetDefProjectAndFolder() {

        // PREPARE
        final SrcGen4JConfig config = new SrcGen4JConfig();
        final Generators generators = new Generators();
        final GeneratorConfig generator = new GeneratorConfig();
        final Artifact testee = new Artifact();

        config.setGenerators(generators);
        generators.addGenerator(generator);
        generator.addArtifact(testee);

        config.init();

        // TEST & VERIFY

        // No value set in hierarchy
        assertThat(testee.getDefProject()).isNull();
        assertThat(testee.getDefFolder()).isNull();

        // Artifact level
        testee.setProject("A");
        testee.setFolder("B");
        assertThat(testee.getDefProject()).isEqualTo("A");
        assertThat(testee.getDefFolder()).isEqualTo("B");
        testee.setProject(null);
        testee.setFolder(null);

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
