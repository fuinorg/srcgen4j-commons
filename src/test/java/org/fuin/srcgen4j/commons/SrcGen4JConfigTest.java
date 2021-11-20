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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.fuin.utils4j.jaxb.JaxbUtils;
import org.fuin.utils4j.jaxb.UnmarshallerBuilder;
import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;

/**
 * Tests for {@link SrcGen4JConfig}.
 */
public class SrcGen4JConfigTest extends AbstractTest {

    // CHECKSTYLE:OFF

    @Test
    public final void testPojoStructureAndBehavior() {

        final PojoClass pc = PojoClassFactory.getPojoClass(SrcGen4JConfig.class);
        final Validator validator = createPojoValidatorBuilder().build();
        validator.validate(pc);

    }

    @Test
    public final void testGetVarMap() {

        // PREPARE
        final Variables vars = new Variables(new Variable("a", "1"), new Variable("B", "b"), new Variable("x", "2"));
        final SrcGen4JConfig testee = new SrcGen4JConfig();
        testee.setVariables(vars);
        testee.init(new DefaultContext(), new File("."));

        // TEST
        final Map<String, String> varMap = testee.getVarMap();

        // VERIFY
        assertThat(varMap).isNotNull().containsOnly(entry("rootDir", "."), entry("a", "1"), entry("B", "b"), entry("x", "2"));

    }

    @Test
    public final void testUnmarshal() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(SrcGen4JConfig.class);
        final Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("test-config.xml"));
        try {

            // TEST
            final SrcGen4JConfig testee = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext)
                    .addClasspathSchemas("/srcgen4j-commons-0_4_3.xsd", "/test-input.xsd").build(), reader);

            // VERIFY
            assertThat(testee).isNotNull();

            final Variables variables = testee.getVariables();
            assertThat(variables).isNotNull();
            final List<Variable> varList = variables.asList();
            assertThat(varList).hasSize(2);
            final Variable var0 = varList.get(0);
            assertThat(var0.getName()).isEqualTo("root");
            assertThat(var0.getValue()).isEqualTo("/var/tmp");
            final Variable var1 = varList.get(1);
            assertThat(var1.getName()).isEqualTo("project_example_path");
            assertThat(var1.getValue()).isEqualTo("${root}/example");

            assertThat(testee.getProjects()).isNotNull();
            assertThat(testee.getProjects()).hasSize(1);
            final Project prj = testee.getProjects().get(0);
            assertThat(prj.getName()).isEqualTo("example");
            assertThat(prj.getPath()).isEqualTo("${root}/example");
            assertThat(prj.getFolders()).hasSize(8);
            assertThat(prj.getFolders()).contains(new Folder("mainJava", ""), new Folder("mainRes", ""), new Folder("genMainJava", ""),
                    new Folder("genMainRes", ""), new Folder("testJava", ""), new Folder("testRes", ""), new Folder("genTestJava", ""),
                    new Folder("genTestRes", ""));
            final int idxGenMainJava = prj.getFolders().indexOf(new Folder("genMainJava", ""));
            assertThat(idxGenMainJava).isNotNegative();
            assertThat(prj.getFolders().get(idxGenMainJava).getCleanExclude()).isEqualTo("\\..*");

            assertThat(testee.getGenerators()).isNotNull();
            assertThat(testee.getGenerators().getList()).hasSize(1);
            final GeneratorConfig gen = testee.getGenerators().getList().get(0);
            assertThat(gen.getName()).isEqualTo("gen1");
            assertThat(gen.getProject()).isEqualTo("example");
            assertThat(gen.getFolder()).isEqualTo("genMainJava");
            assertThat(gen.getArtifacts()).hasSize(3);
            assertThat(gen.getArtifacts()).contains(new Artifact("one"), new Artifact("abstract"), new Artifact("manual"));
            int idx = gen.getArtifacts().indexOf(new Artifact("one"));
            assertThat(idx).isNotNegative();
            final Artifact one = gen.getArtifacts().get(idx);
            assertThat(one.getTargets()).isNotNull();
            assertThat(one.getTargets()).hasSize(1);
            final Target target = one.getTargets().get(0);
            assertThat(target.getPattern()).isEqualTo(".*//abc//def//ghi//.*//.java");
            assertThat(target.getProject()).isEqualTo("example");
            assertThat(target.getFolder()).isEqualTo("genMainJava");

        } finally {
            reader.close();
        }
    }

    @Test
    public final void testInit() {

        // PREPARE
        final SrcGen4JConfig testee = new SrcGen4JConfig();

        final Variables vars = new Variables(new Variable("project.name", "1"), new Variable("project.path", "2"),
                new Variable("generator.name", "3"), new Variable("generator.project", "4"), new Variable("generator.folder", "5"),
                new Variable("folder.name", "6"), new Variable("folder.path", "7"), new Variable("artifact.name", "8"),
                new Variable("artifact.project", "9"), new Variable("artifact.folder", "10"), new Variable("target.pattern", "11"),
                new Variable("target.project", "12"), new Variable("target.folder", "13"), new Variable("generators.project", "14"),
                new Variable("generators.folder", "15"));

        final List<Project> projects = new ArrayList<Project>();
        final Project project = new Project("${project.name}", "${project.path}");
        projects.add(project);
        project.addFolder(new Folder("${folder.name}", "${folder.path}"));

        final List<GeneratorConfig> genList = new ArrayList<GeneratorConfig>();
        final GeneratorConfig generator = new GeneratorConfig("${generator.name}", "CLASS", "PARSER", "${generator.project}",
                "${generator.folder}");
        genList.add(generator);
        final Artifact artifact = new Artifact("${artifact.name}", "${artifact.project}", "${artifact.folder}");
        generator.addArtifact(artifact);
        artifact.addTarget(new Target("${target.pattern}", "${target.project}", "${target.folder}"));

        final Generators generators = new Generators("${generators.project}", "${generators.folder}");
        generators.addVariable(new Variable("a", "1"));
        generators.setList(genList);

        testee.setVariables(vars);
        testee.setProjects(projects);
        testee.setGenerators(generators);

        // TEST
        testee.init(new DefaultContext(), new File("."));

        // VERIFY

        final Project resultProject = testee.getProjects().get(0);
        assertThat(resultProject.getName()).isEqualTo("1");
        assertThat(resultProject.getPath()).isEqualTo("2");
        final Folder resultFolder = resultProject.getFolders().get(0);
        assertThat(resultFolder.getName()).isEqualTo("6");
        assertThat(resultFolder.getPath()).isEqualTo("7");

        assertThat(testee.getGenerators()).isNotNull();
        assertThat(testee.getGenerators().getVarMap()).contains(entry("a", "1"));
        assertThat(testee.getGenerators().getProject()).isEqualTo("14");
        assertThat(testee.getGenerators().getFolder()).isEqualTo("15");
        assertThat(testee.getGenerators().getList()).isNotNull();
        assertThat(testee.getGenerators().getList()).hasSize(1);

        final GeneratorConfig resultGenerator = testee.getGenerators().getList().get(0);
        assertThat(resultGenerator.getName()).isEqualTo("3");
        assertThat(resultGenerator.getProject()).isEqualTo("4");
        assertThat(resultGenerator.getFolder()).isEqualTo("5");
        final Artifact resultArtifact = resultGenerator.getArtifacts().get(0);
        assertThat(resultArtifact.getName()).isEqualTo("8");
        assertThat(resultArtifact.getProject()).isEqualTo("9");
        assertThat(resultArtifact.getFolder()).isEqualTo("10");
        final Target resultTarget = resultArtifact.getTargets().get(0);
        assertThat(resultTarget.getPattern()).isEqualTo("11");
        assertThat(resultTarget.getProject()).isEqualTo("12");
        assertThat(resultTarget.getFolder()).isEqualTo("13");

    }

    @Test
    public final void testDerivedVariables() throws Exception {

        // PREPARE
        final JAXBContext jaxbContext = JAXBContext.newInstance(SrcGen4JConfig.class);
        final Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("test-variables.xml"));
        try {
            // TEST
            final SrcGen4JConfig testee = JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(), reader);
            testee.init(new DefaultContext(), new File("."));

            // VERIFY
            assertThat(testee.getVarMap()).containsOnly(entry("rootDir", "."), entry("root", "/var/tmp"), entry("a", "base"),
                    entry("res", "/**\n * Test base.\n */"), entry("escapes", "\r\n\t"));

            final Parsers parsers = testee.getParsers();
            assertThat(parsers.getVarMap()).contains(entry("a", "base/parsers1"), entry("b", "base/parsers1/parsers2"));
            final ParserConfig parserConfig = parsers.getList().get(0);
            // @formatter:off
            assertThat(parserConfig.getVarMap()).contains(
                    entry("a", "base/parsers1/parser1"),
                    entry("b", "base/parsers1/parsers2"),
                    entry("c", "base/parsers1/parser1/parser3"),
                    entry("x", "/**\n * Test base/parsers1/parser1.\n */"),
                    entry("root", "/var/tmp"), entry("rootDir", "."));
            // @formatter:off

            final Generators generators = testee.getGenerators();
            assertThat(generators.getVarMap()).contains(
                    entry("a", "base/generators1"),
                    entry("b", "base/generators1/generators2"));
            final GeneratorConfig generatorConfig = generators.getList().get(0);
            // @formatter:off
            assertThat(generatorConfig.getVarMap()).contains(
                    entry("a", "base/generators1/generator1"),
                    entry("b", "base/generators1/generators2"),
                    entry("c", "base/generators1/generator1/generator3"),
                    entry("root", "/var/tmp"), entry("rootDir", "."));
            // @formatter:off

        } finally {
            reader.close();
        }

    }

    @Test
    public final void testInitNullContent() {

        // PREPARE
        final SrcGen4JConfig testee = new SrcGen4JConfig();

        // TEST
        testee.init(new DefaultContext(), new File("."));

        // VERIFY
        // Test makes sure the "init(File)" does not throw NullPointerException
        // if nothing is set

    }

    @Test
    public void testFindTargetPositive() throws Exception {

        // PREPARE
        final SrcGen4JConfig testee = load("findTarget.xml");
        testee.init(new DefaultContext(), new File("."));

        // TEST
        final Folder folder = testee.findTargetFolder("gen1", "arti1",
                "a/b/c/MyClass.java");

        // VERIFY
        assertThat(folder.getPath()).isEqualTo("src/main/java");
        assertThat(folder.getParent().getPath())
                .isEqualTo("/var/tmp/myproject");

    }

    @Test
    public void testFindTargetFolderNotFoundException() throws Exception {

        // PREPARE
        final SrcGen4JConfig testee = load("findTarget.xml");
        testee.init(new DefaultContext(), new File("."));

        // TEST
        try {
            testee.findTargetFolder("gen1", "arti1", "Unknown.java");
            fail("The file should lead to arti1's folder 'folder3' that is not defined");
        } catch (final FolderNotFoundException ex) {
            // VERIFIED
        }

    }

    @Test
    public void testCreateMavenStyleSingleProject() {

        // PREPARE
        final String projectName = "NAME";

        // TEST
        SrcGen4JConfig config = SrcGen4JConfig.createMavenStyleSingleProject(
                new DefaultContext(), projectName, new File("."));

        // VERIFY
        assertThat(config.getProjects()).hasSize(1);
        final Project project = config.getProjects().get(0);
        assertThat(project.getName()).isEqualTo(projectName);
        assertThat(project.getPath()).isEqualTo(".");
        assertThat(project.isMaven()).isTrue();
        assertThat(project.getFolders()).hasSize(8);
        assertThat(project.getFolders()).contains(new Folder("mainJava", ""),
                new Folder("mainRes", ""), new Folder("genMainJava", ""),
                new Folder("genMainRes", ""), new Folder("testJava", ""),
                new Folder("testRes", ""), new Folder("genTestJava", ""),
                new Folder("genTestRes", ""));

    }

    private SrcGen4JConfig load(final String resourceName) throws Exception {
        final JAXBContext jaxbContext = JAXBContext
                .newInstance(SrcGen4JConfig.class);
        final Reader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(resourceName));
        try {
            return JaxbUtils.unmarshal(new UnmarshallerBuilder().withContext(jaxbContext).build(), reader);
        } finally {
            reader.close();
        }
    }

    // CHECKSTYLE:ON

}
