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
package org.moeaframework.analysis;

import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.Epsilons;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.spi.ProblemFactory;
import org.moeaframework.mock.MockRealProblem;
import org.moeaframework.problem.DTLZ.DTLZ2;

public class EpsilonHelperTest {
	
	@Test
	public void test() {
		Assert.assertGreaterThan(EpsilonHelper.getEpsilons(new DTLZ2(3)).get(0), EpsilonHelper.DEFAULT);
	}
	
	@Test
	public void testUndefinedProblem() {
		Assert.assertEquals(EpsilonHelper.getEpsilons(new MockRealProblem()).get(0), EpsilonHelper.DEFAULT);
	}
	
	@Test
	public void testConvertNoEpsilons() {
		NondominatedPopulation referenceSet = ProblemFactory.getInstance().getReferenceSet("DTLZ2_2");
		EpsilonBoxDominanceArchive expected = new EpsilonBoxDominanceArchive(0.1, referenceSet);
		EpsilonBoxDominanceArchive actual = EpsilonHelper.convert(referenceSet, Epsilons.of(0.1));
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(0.1, actual.getComparator().getEpsilons().get(0));
	}
	
	@Test
	public void testConvertDifferentEpsilons() {
		NondominatedPopulation referenceSet = ProblemFactory.getInstance().getReferenceSet("DTLZ2_2");
		NondominatedPopulation population = new EpsilonBoxDominanceArchive(0.1, referenceSet);
		EpsilonBoxDominanceArchive actual = EpsilonHelper.convert(population, Epsilons.of(0.25));
		
		Assert.assertNotSame(actual, population);
		Assert.assertEquals(0.25, actual.getComparator().getEpsilons().get(0));
	}
	
	@Test
	public void testConvertSameEpsilons() {
		NondominatedPopulation referenceSet = ProblemFactory.getInstance().getReferenceSet("DTLZ2_2");
		NondominatedPopulation population = new EpsilonBoxDominanceArchive(0.1, referenceSet);
		EpsilonBoxDominanceArchive actual = EpsilonHelper.convert(population, Epsilons.of(0.1));
		
		Assert.assertSame(actual, population);
		Assert.assertEquals(0.1, actual.getComparator().getEpsilons().get(0));
	}
	
}
