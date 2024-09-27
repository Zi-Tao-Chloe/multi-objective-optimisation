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
package org.moeaframework.problem.ZCAT;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.core.Problem;
import org.moeaframework.problem.ProblemTest;
import org.moeaframework.util.Vector;

public class ZCAT15Test extends ProblemTest {

	@Test
	public void test() {
		Problem problem = new ZCAT15(3);
		
		Assert.assertArrayEquals(new double[] { 1.739504, 6.196056, 14.395043 }, 
				evaluateAt(problem, Vector.of(problem.getNumberOfVariables(), 0.0)).getObjectives(),
				0.000001);
		
		Assert.assertArrayEquals(new double[] { 5.074577, 20.000000, 53.254233 }, 
				evaluateAtLowerBounds(problem).getObjectives(),
				0.000001);
		
		Assert.assertArrayEquals(new double[] { 3.056309, 12.945101, 22.789547 }, 
				evaluateAtUpperBounds(problem).getObjectives(),
				0.000001);
		
		Assert.assertArrayEquals(new double[] { 3.567551, 9.228666, 23.461998 }, 
				evaluateAt(problem, 0.198936, -0.367582, -1.375415, -1.591970, 2.193607, 2.508740, 2.884077, 2.957715, -1.764280, -4.313750, 4.988815, -1.817740, 3.228524, -1.291136, -4.234178, -4.083253, -0.283208, 5.227770, -3.743611, 7.512911, 0.905729, 9.565398, 8.514093, -10.834280, -8.729754, -7.267325, -0.201983, 7.760443, -11.686637, 12.481976).getObjectives(),
				0.0001);
	}
	
	@Test
	public void testGenerate() {
		assertGeneratedSolutionsAreNondominated(new ZCAT15(3), 1000);
	}
	
	@Test
	public void testProvider() {
		assertProblemDefined("ZCAT15_2", 2, true);
		assertProblemDefined("ZCAT15_3", 3, false);
	}
	
}
