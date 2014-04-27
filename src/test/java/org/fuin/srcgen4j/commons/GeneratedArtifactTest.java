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

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.SetterMustExistRule;

/**
 * Tests for {@link GeneratedArtifact}.
 */
public class GeneratedArtifactTest extends AbstractTest {

	// CHECKSTYLE:OFF

	@Test
	public final void testPojoStructureAndBehavior() {

		final PojoClass pc = PojoClassFactory
				.getPojoClass(GeneratedArtifact.class);
		final PojoValidator pv = createPojoValidator();
		pv.addRule(new SetterMustExistRule());
		pv.runValidation(pc);

	}

	@Test
	public final void testConstructor() {

		// PREPARE
		final String name = "abc";
		final String pathAndName = "def";
		final byte[] source = "ghi".getBytes();

		// TEST
		final GeneratedArtifact testee = new GeneratedArtifact(name,
				pathAndName, source);

		// VERIFY
		assertThat(testee.getName()).isEqualTo(name);
		assertThat(testee.getPathAndName()).isEqualTo(pathAndName);
		assertThat(testee.getData()).isEqualTo(source);

	}

	// CHECKSTYLE:ON

}
