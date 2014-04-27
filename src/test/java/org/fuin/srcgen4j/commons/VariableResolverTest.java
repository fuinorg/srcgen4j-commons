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
import static org.fest.assertions.MapAssert.entry;
import static org.fuin.srcgen4j.commons.VariableResolver.references;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test for {@link VariableResolver}.
 */
public class VariableResolverTest {

	// CHECKSTYLE:OFF

	@Test
	public void testNullConstruction() {

		// PREPARE & TEST
		final VariableResolver testee = new VariableResolver(null);

		// VERIFY
		assertThat(testee.getDepth()).isEmpty();
		assertThat(testee.getResolved()).isEmpty();
		assertThat(testee.getUnresolved()).isEmpty();

	}

	@Test
	public void testEmptyConstruction() {

		// PREPARE & TEST
		final VariableResolver testee = new VariableResolver(
				new ArrayList<Variable>());

		// VERIFY
		assertThat(testee.getDepth()).isEmpty();
		assertThat(testee.getResolved()).isEmpty();
		assertThat(testee.getUnresolved()).isEmpty();

	}

	@Test
	public void testOneLevel() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "1"));
		vars.add(new Variable("b", "2"));
		vars.add(new Variable("c", "3"));

		// TEST
		final VariableResolver testee = new VariableResolver(vars);

		// VERIFY
		assertThat(testee.getDepth()).includes(entry("a", 0), entry("b", 0),
				entry("c", 0));
		assertThat(testee.getResolved()).includes(entry("a", "1"),
				entry("b", "2"), entry("c", "3"));
		assertThat(testee.getUnresolved()).includes(entry("a", "1"),
				entry("b", "2"), entry("c", "3"));

	}

	@Test
	public void testTwoLevels() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "a${b}"));
		vars.add(new Variable("b", "2"));

		// TEST
		final VariableResolver testee = new VariableResolver(vars);

		// VERIFY
		assertThat(testee.getDepth()).includes(entry("a", 1), entry("b", 0));
		assertThat(testee.getResolved()).includes(entry("a", "a2"),
				entry("b", "2"));
		assertThat(testee.getUnresolved()).includes(entry("a", "a${b}"),
				entry("b", "2"));

	}

	@Test
	public void testThreeLevels() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "1${b}"));
		vars.add(new Variable("b", "2${c}"));
		vars.add(new Variable("c", "3"));

		// TEST
		final VariableResolver testee = new VariableResolver(vars);

		// VERIFY
		assertThat(testee.getDepth()).includes(entry("a", 2), entry("b", 1),
				entry("c", 0));
		assertThat(testee.getResolved()).includes(entry("a", "123"),
				entry("b", "23"), entry("c", "3"));
		assertThat(testee.getUnresolved()).includes(entry("a", "1${b}"),
				entry("b", "2${c}"), entry("c", "3"));

	}

	@Test
	public void testFourLevels() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "1${b}"));
		vars.add(new Variable("b", "2${c}"));
		vars.add(new Variable("c", "3${d}"));
		vars.add(new Variable("d", "4"));

		// TEST
		final VariableResolver testee = new VariableResolver(vars);

		// VERIFY
		assertThat(testee.getDepth()).includes(entry("a", 3), entry("b", 2),
				entry("c", 1), entry("d", 0));
		assertThat(testee.getResolved()).includes(entry("a", "1234"),
				entry("b", "234"), entry("c", "34"), entry("d", "4"));
		assertThat(testee.getUnresolved()).includes(entry("a", "1${b}"),
				entry("b", "2${c}"), entry("c", "3${d}"), entry("d", "4"));

	}

	@Test
	public void testCycleOneLevel() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "${b}"));
		vars.add(new Variable("b", "${a}"));

		// TEST
		try {
			new VariableResolver(vars);
		} catch (final IllegalStateException ex) {
			assertThat(ex.getMessage()).isEqualTo("Cycle: a > b > a");
		}

	}

	@Test
	public void testCycleTwoLevels() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "${b}"));
		vars.add(new Variable("b", "${c}"));
		vars.add(new Variable("c", "${a}"));

		// TEST
		try {
			new VariableResolver(vars);
		} catch (final IllegalStateException ex) {
			assertThat(ex.getMessage()).isEqualTo("Cycle: a > b > c > a");
		}

	}

	@Test
	public void testUnresolved() {

		// PREPARE
		final List<Variable> vars = new ArrayList<Variable>();
		vars.add(new Variable("a", "1${b}"));
		vars.add(new Variable("b", "${c}"));

		// TEST
		final VariableResolver testee = new VariableResolver(vars);

		// VERIFY
		assertThat(testee.getDepth()).includes(entry("a", 2), entry("b", 1));
		assertThat(testee.getResolved()).includes(entry("a", "1${c}"),
				entry("b", "${c}"));
		assertThat(testee.getUnresolved()).includes(entry("a", "1${b}"),
				entry("b", "${c}"));

	}

	@Test
	public void testReferences() {

		assertThat(references(null)).isEmpty();
		assertThat(references("")).isEmpty();
		assertThat(references("a")).isEmpty();
		assertThat(references("a b c")).isEmpty();
		assertThat(references(" a $b c ")).isEmpty();
		assertThat(references(" a ${b c ")).isEmpty(); // Non closing bracket
		assertThat(references("${a}")).contains("a");
		assertThat(references("a ${b} ${c}")).contains("b", "c");
		assertThat(references("${a}${b}")).contains("a", "b");

	}

	// CHECKSTYLE:ON

}
