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

import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;

/**
 * Tests for {@link GeneratorConfig}.
 */
public class GeneratorConfigTest extends AbstractTest {

	// CHECKSTYLE:OFF

	/**
	 * Basic POJO test.
	 */
	@Test
	public final void testPojoStructureAndBehavior() {

		final PojoClass pc = PojoClassFactory
				.getPojoClass(GeneratorConfig.class);
		final PojoValidator pv = createPojoValidator();
		pv.runValidation(pc);

	}

	@Test
	public final void testUnmarshal() throws Exception {

		// PREPARE
		final JAXBContext jaxbContext = JAXBContext
				.newInstance(GeneratorConfig.class);
		final Reader reader = new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream("test-config.xml"));
		try {

			// TEST
			final GeneratorConfig testee = new JaxbHelper().create(reader,
					jaxbContext);

			// VERIFY
			assertThat(testee).isNotNull();

			assertThat(testee.getVariables()).isNotNull();
			assertThat(testee.getVariables()).hasSize(1);
			final Variable var = testee.getVariables().get(0);
			assertThat(var.getName()).isEqualTo("root");
			assertThat(var.getValue()).isEqualTo("/var/tmp");

			assertThat(testee.getProjects()).isNotNull();
			assertThat(testee.getProjects()).hasSize(1);
			final Project prj = testee.getProjects().get(0);
			assertThat(prj.getName()).isEqualTo("example");
			assertThat(prj.getPath()).isEqualTo("${root}/example");
			assertThat(prj.getFolders()).hasSize(8);
			assertThat(prj.getFolders()).contains(
					new Folder("mainJava"),
					new Folder("mainRes"),
					new Folder("genMainJava"),
					new Folder("genMainRes"),
					new Folder("testJava"),
					new Folder("testRes"),
					new Folder("genTestJava"),
					new Folder("genTestRes"));

			assertThat(testee.getGenerators()).isNotNull();
			assertThat(testee.getGenerators()).hasSize(1);
			final Generator gen = testee.getGenerators().get(0);
			assertThat(gen.getName()).isEqualTo("gen1");
			assertThat(gen.getProject()).isEqualTo("example");
			assertThat(gen.getFolder()).isEqualTo("genMainJava");
			assertThat(gen.getArtifacts()).hasSize(3);
			assertThat(gen.getArtifacts()).contains(
					new Artifact("one"),
					new Artifact("abstract"),
					new Artifact("manual"));
			int idx = gen.getArtifacts().indexOf(new Artifact("one"));
			assertThat(idx).isGreaterThan(-1);
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

	// CHECKSTYLE:ON

}
