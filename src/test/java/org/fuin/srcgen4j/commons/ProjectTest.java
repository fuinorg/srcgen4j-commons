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

import javax.xml.bind.JAXBContext;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;

/**
 * Tests for {@link Project}.
 */
public class ProjectTest extends AbstractTest {

	// CHECKSTYLE:OFF
	
	@Test
	public final void testPojoStructureAndBehavior() {
		
		final PojoClass pc = PojoClassFactory.getPojoClass(Project.class);
		final PojoValidator pv = createPojoValidator();
		pv.runValidation(pc);
		
	}

	@Test
	public final void testMarshal() throws Exception {

		// PREPARE
		final JAXBContext jaxbContext = JAXBContext.newInstance(Project.class, Folder.class);
		final Project testee = new Project("abc", "def");
		testee.setMaven(true);
		testee.addFolder(new Folder("NAME", "PATH"));

		// TEST
		final String result = new JaxbHelper(false).write(testee, jaxbContext);

		// VERIFY
		assertThat(result).isEqualTo(XML + "<project name=\"abc\" path=\"def\" maven=\"true\"><folder name=\"NAME\" path=\"PATH\"/></project>");

	}

	@Test
	public final void testUnmarshal() throws Exception {

		// PREPARE
		final JAXBContext jaxbContext = JAXBContext.newInstance(Project.class, Folder.class);

		// TEST
		final Project testee = new JaxbHelper().create(
				"<project name=\"abc\" path=\"def\" maven=\"true\"><folder name=\"NAME\" path=\"PATH\"/></project>",
				jaxbContext);

		// VERIFY
		assertThat(testee).isNotNull();
		assertThat(testee.getName()).isEqualTo("abc");
		assertThat(testee.getPath()).isEqualTo("def");
		assertThat(testee.isMaven()).isTrue();
		assertThat(testee.getFolders()).isNotNull();
		assertThat(testee.getFolders()).hasSize(1);
		assertThat(testee.getFolders().get(0).getName()).isEqualTo("NAME");
		assertThat(testee.getFolders().get(0).getPath()).isEqualTo("PATH");

	}

	// CHECKSTYLE:ON
	
}
