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
		assertThat(result).isEqualTo(XML + "<artifact name=\"abc\" project=\"def\" folder=\"ghi\"><target pattern=\"PATTERN\" project=\"PROJECT\" folder=\"FOLDER\"/></artifact>");

	}

	@Test
	public final void testUnmarshal() throws Exception {

		// PREPARE
		final JAXBContext jaxbContext = JAXBContext.newInstance(Artifact.class, Folder.class);

		// TEST
		final Artifact testee = new JaxbHelper().create(
				"<artifact name=\"abc\" project=\"def\" folder=\"ghi\"><target pattern=\"PATTERN\" project=\"PROJECT\" folder=\"FOLDER\"/></artifact>",
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
	public final void testReplaceVariables() {
		
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
		
		// TEST
		testee.replaceVariables(vars);
		
		// VERIFY
		assertThat(testee.getName()).isEqualTo("A NAME");
		assertThat(testee.getProject()).isEqualTo("PRJB");
		assertThat(testee.getFolder()).isEqualTo("abc");
		final Target target = testee.getTargets().get(0);
		assertThat(target.getPattern()).isEqualTo("tname");
		assertThat(target.getProject()).isEqualTo("tprj");
		assertThat(target.getFolder()).isEqualTo("xyz");
		
	}
	
	// CHECKSTYLE:ON
	
}
