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
package org.moeaframework.algorithm.single;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.TestThresholds;
import org.moeaframework.core.Solution;
import org.moeaframework.mock.MockSolution;

public class LinearObjectiveComparatorTest {
	
	private double calculate(Solution solution, double[] weights) {
		return LinearObjectiveComparator.calculateFitness(solution, weights);
	}
	
	@Test
	public void testCalculateFitness() {
		double[] weights = new double[] { 1.0, 1.0 };
		
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(0.0, 1.0), weights), 1.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(1.0, 0.0), weights), 1.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(-1.0, 0.0), weights), -1.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(0.0, -1.0), weights), -1.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(1.0, 1.0), weights), 2.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(1.0, -1.0), weights), 0.0, TestThresholds.LOW_PRECISION);
		Assert.assertEquals(calculate(MockSolution.of().withObjectives(0.0, 0.0), weights), 0.0, TestThresholds.LOW_PRECISION);
	}
	
	@Test
	public void testNoWeights() {
		LinearObjectiveComparator comparator = new LinearObjectiveComparator();

		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(1.0, 0.0)), 0);
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(0.5, 0.5)), 0);
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(0.5, 0.0)), 1);
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(0.5, 1.0)), -1);
	}
	
	@Test
	public void testGivenWeights() {
		LinearObjectiveComparator comparator = new LinearObjectiveComparator(new double[] { 0.5, 0.25 });
	
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(1.0, 0.0)), -1);
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(0.5, 0.0)), 0);
		Assert.assertEquals(comparator.compare(MockSolution.of().withObjectives(0.0, 1.0),
				MockSolution.of().withObjectives(0.25, 0.0)), 1);
	}

}
