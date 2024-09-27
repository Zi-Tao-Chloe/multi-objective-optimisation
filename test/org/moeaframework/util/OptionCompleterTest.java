/* Copyright 2009-2024 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.moeaframework.Assert;

public class OptionCompleterTest {

	@Test
	public void testNormalUsage() {
		OptionCompleter completer = new OptionCompleter("subset", "superset");

		Assert.assertEquals("subset", completer.lookup("subset"));
		Assert.assertEquals("superset", completer.lookup("superset"));
		Assert.assertEquals("subset", completer.lookup("SUB"));
		Assert.assertEquals("superset", completer.lookup("SUP"));
		Assert.assertNull(completer.lookup("su"));
		Assert.assertNull(completer.lookup("k"));
		Assert.assertNull(completer.lookup(""));
	}

	@Test
	public void testDuplicateOption() {
		OptionCompleter completer = new OptionCompleter("subset", "subset");
		Assert.assertEquals("subset", completer.lookup("sub"));
	}

	@Test
	public void testEmptyStringWithSingleOption() {
		OptionCompleter completer = new OptionCompleter("subset");
		Assert.assertEquals("subset", completer.lookup(""));
	}
	
	@Test
	public void testEmptyStringWithMultipleOptions() {
		OptionCompleter completer = new OptionCompleter("subset", "superset");
		Assert.assertEquals(null, completer.lookup(""));
	}

	@Test
	public void testAdd() {
		OptionCompleter completer = new OptionCompleter();

		Assert.assertEquals(null, completer.lookup("sub"));
		completer.add("subset");
		Assert.assertEquals("subset", completer.lookup("sub"));
	}

	@Test
	public void testConstructors() {
		OptionCompleter completer1 = new OptionCompleter("subset");
		Assert.assertEquals("subset", completer1.lookup("sub"));

		List<String> options = new ArrayList<String>();
		options.add("subset");

		OptionCompleter completer2 = new OptionCompleter(options);
		Assert.assertEquals("subset", completer2.lookup("sub"));
	}

}
