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
package org.moeaframework.core.comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.core.Solution;
import org.moeaframework.mock.MockSolution;

public class ParetoDominanceComparatorTest {

	private ParetoDominanceComparator comparator;

	@Before
	public void setUp() {
		comparator = new ParetoDominanceComparator();
	}

	@After
	public void tearDown() {
		comparator = null;
	}

	@Test
	public void testDominance() {
		Solution solution1 = MockSolution.of().withObjectives(0.0, 0.0).withConstraints(1.0);
		Solution solution2 = MockSolution.of().withObjectives(1.0, 1.0).withConstraints(0.0);
		Solution solution3 = MockSolution.of().withObjectives(1.0, 0.0).withConstraints(0.0);

		Assert.assertTrue(comparator.compare(solution1, solution2) > 0);
		Assert.assertTrue(comparator.compare(solution2, solution1) < 0);
		Assert.assertTrue(comparator.compare(solution1, solution3) > 0);
		Assert.assertTrue(comparator.compare(solution3, solution1) < 0);
		Assert.assertTrue(comparator.compare(solution2, solution3) > 0);
		Assert.assertTrue(comparator.compare(solution3, solution2) < 0);
	}

	@Test
	public void testNondominance() {
		Solution solution1 = MockSolution.of().withObjectives(0.75, 0.25).withConstraints(1.0);
		Solution solution2 = MockSolution.of().withObjectives(0.25, 0.75).withConstraints(1.0);

		Assert.assertTrue(comparator.compare(solution1, solution2) == 0);
		Assert.assertTrue(comparator.compare(solution2, solution1) == 0);
	}

}
