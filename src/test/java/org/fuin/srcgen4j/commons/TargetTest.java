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
 * Tests for {@link Target}.
 */
public class TargetTest extends AbstractTest {

	// CHECKSTYLE:OFF
	
	@Test
	public final void testPojoStructureAndBehavior() {
		
		final PojoClass pc = PojoClassFactory.getPojoClass(Target.class);
		final PojoValidator pv = createPojoValidator();
		pv.runValidation(pc);
		
	}
	
	@Test
	public final void testMarshal() throws Exception {
		
		// PREPARE
		final JAXBContext jaxbContext = JAXBContext.newInstance(Target.class, AbstractTarget.class);
		final Target testee = new Target(".*", "abc", "def");

		// TEST
		final String result = new JaxbHelper(false).write(testee, jaxbContext);
		
		// VERIFY
		assertThat(result).isEqualTo(XML + "<target pattern=\".*\" project=\"abc\" folder=\"def\"/>");
		
	}

	@Test
	public final void testUnmarshal() throws Exception {
		
		// PREPARE
		final JAXBContext jaxbContext = JAXBContext.newInstance(Target.class, AbstractTarget.class);
		
		// TEST
		final Target testee = new JaxbHelper().create("<target pattern=\".*\" project=\"abc\" folder=\"def\"/>", jaxbContext);
		
		// VERIFY
		assertThat(testee).isNotNull();
		assertThat(testee.getPattern()).isEqualTo(".*");
		assertThat(testee.getProject()).isEqualTo("abc");
		assertThat(testee.getFolder()).isEqualTo("def");
		
	}

	@Test
	public final void testReplaceVariables() {
		
		// PREPARE
		final Target testee = new Target("${a}name", "t${b}rj", "xy${c}");
		
		final Map<String, String> vars = new HashMap<String, String>();
		vars.put("a", "t");
		vars.put("b", "p");
		vars.put("c", "z");
		
		// TEST
		testee.replaceVariables(vars);
		
		// VERIFY
		assertThat(testee.getPattern()).isEqualTo("tname");
		assertThat(testee.getProject()).isEqualTo("tprj");
		assertThat(testee.getFolder()).isEqualTo("xyz");
		
	}
	
	// CHECKSTYLE:ON

}
