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
package org.moeaframework.core.indicator;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.TestThresholds;
import org.moeaframework.core.Indicator;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.mock.MockRealProblem;
import org.moeaframework.mock.MockSolution;

public class SpacingTest extends AbstractIndicatorTest<Spacing> {
	
	@Override
	public Spacing createInstance(Problem problem, NondominatedPopulation referenceSet) {
		return new Spacing(problem);
	}
	
	@Override
	public double getWorstValue() {
		return 0.0;
	}
	
	@Override
	public void testEmptyReferenceSet() {
		throw new IllegalArgumentException("bypass this test, spacing does not require a reference set");
	}
	
	@Test
	public void test() {
		Problem problem = new MockRealProblem(2);
		NondominatedPopulation referenceSet = getDefaultReferenceSet();
		Indicator indicator = createInstance(problem, referenceSet);
		
		NondominatedPopulation approximationSet = new NondominatedPopulation();

		approximationSet.add(MockSolution.of(problem).withObjectives(0.5, 0.5));
		Assert.assertEquals(0.0, indicator.evaluate(approximationSet), TestThresholds.HIGH_PRECISION);
		
		approximationSet.clear();
		approximationSet.add(MockSolution.of(problem).withObjectives(0.0, 1.0));
		approximationSet.add(MockSolution.of(problem).withObjectives(1.0, 0.0));
		Assert.assertEquals(0.0, indicator.evaluate(approximationSet), TestThresholds.HIGH_PRECISION);

		approximationSet.clear();
		approximationSet.add(MockSolution.of(problem).withObjectives(0.0, 1.0));
		approximationSet.add(MockSolution.of(problem).withObjectives(0.5, 0.5));
		approximationSet.add(MockSolution.of(problem).withObjectives(1.0, 0.0));
		Assert.assertEquals(0.0, indicator.evaluate(approximationSet), TestThresholds.HIGH_PRECISION);
		
		approximationSet.clear();
		approximationSet.add(MockSolution.of(problem).withObjectives(0.0, 1.0));
		approximationSet.add(MockSolution.of(problem).withObjectives(0.25, 0.75));
		approximationSet.add(MockSolution.of(problem).withObjectives(1.0, 0.0));
		Assert.assertGreaterThan(indicator.evaluate(approximationSet), 0.0);
	}

}
