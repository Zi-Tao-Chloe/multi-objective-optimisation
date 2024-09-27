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
package org.moeaframework.problem.single;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.TestThresholds;
import org.moeaframework.core.Solution;
import org.moeaframework.mock.MockSolution;
import org.moeaframework.problem.ProblemTest;

public class ZakharovTest extends ProblemTest {
	
	@Test
	public void test() {		
		try (AbstractSingleObjectiveProblem problem = new Zakharov()) {
			for (Solution solution : problem.getReferenceSet()) {
				Assert.assertEquals(0.0, solution.getObjective(0), TestThresholds.HIGH_PRECISION);
				Assert.assertGreaterThan(MockSolution.of(solution).addNoise(0.1).evaluate(problem).getObjective(0), 0.0);
			}
			
			Assert.assertEquals(51050.0, MockSolution.of(problem).atLowerBounds().evaluate().getObjective(0), 0.0000001);
			Assert.assertEquals(51050.0, MockSolution.of(problem).atUpperBounds().evaluate().getObjective(0), 0.0000001);
		}
	}
	
	@Test
	public void testProblemProvider() {
		assertProblemDefined("Zakharov", 1);
	}

}
