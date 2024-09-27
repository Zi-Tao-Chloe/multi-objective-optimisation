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
package org.moeaframework.problem.ZDT;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.core.Problem;
import org.moeaframework.mock.MockSolution;
import org.moeaframework.problem.ProblemTest;

public class ZDT5Test extends ProblemTest {
	
	@Test
	public void test() {
		Problem problem = new ZDT5();
		
		Assert.assertArrayEquals(new double[] { 1.0, 20.0 },
				MockSolution.of(problem).atLowerBounds().evaluate().getObjectives(),
				0.000001);
		
		Assert.assertArrayEquals(new double[] { 31.0, 10.0/31.0 },
				MockSolution.of(problem).atUpperBounds().evaluate().getObjectives(),
				0.000001);
	}

	@Test
	public void testJMetal() {
		testAgainstJMetal("ZDT5");
	}
	
	@Test
	public void testProvider() {
		assertProblemDefined("ZDT5", 2);
	}
}
