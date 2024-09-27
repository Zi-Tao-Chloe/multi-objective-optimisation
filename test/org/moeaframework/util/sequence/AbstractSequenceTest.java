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
package org.moeaframework.util.sequence;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.moeaframework.Assert;
import org.moeaframework.CIRunner;
import org.moeaframework.Retryable;
import org.moeaframework.TestThresholds;

/**
 * Abstract class for testing implementation of {@link Sequence}.
 */
@RunWith(CIRunner.class)
@Retryable
public abstract class AbstractSequenceTest<T extends Sequence> {
	
	/**
	 * Returns a new instance of the sequence being tested.
	 * 
	 * @return the sequence to test
	 */
	public abstract T createInstance();
	
	@Test
	public void test1D() {
		test(createInstance(), 1);
	}
	
	@Test
	public void test2D() {
		test(createInstance(), 2);
	}
	
	@Test
	public void test10D() {
		test(createInstance(), 10);
	}

	/**
	 * Generates samples using the specified sequence and checks if the samples are uniformly distributed in the range
	 * {@code [0, 1]}.
	 * 
	 * @param sequence the sequence to test
	 * @param D the dimension of the samples
	 */
	protected void test(Sequence sequence, int D) {
		double[][] points = sequence.generate(TestThresholds.SAMPLES, D);

		Assert.assertEquals(TestThresholds.SAMPLES, points.length);

		checkRange(points, D);
		checkStatistics(points, D);
	}

	/**
	 * Asserts the samples are in the range {@code [0, 1]}.
	 * 
	 * @param points the samples
	 * @param D the dimension of the samples
	 */
	protected void checkRange(double[][] points, int D) {
		for (int i = 0; i < points.length; i++) {
			Assert.assertEquals(D, points[i].length);

			for (int j = 0; j < D; j++) {
				Assert.assertBetween(0.0, 1.0, points[i][j]);
			}
		}
	}

	/**
	 * Asserts the samples are uniformly distributed.
	 * 
	 * @param points the samples
	 * @param D the dimension of the samples
	 */
	protected void checkStatistics(double[][] points, int D) {
		for (int i = 0; i < D; i++) {
			DescriptiveStatistics statistics = new DescriptiveStatistics();

			for (int j = 0; j < points.length; j++) {
				statistics.addValue(points[j][i]);
			}

			testUniformDistribution(0.0, 1.0, statistics);
		}
	}

	/**
	 * Asserts that the collected statistics appear to be from a uniform distribution.
	 * 
	 * @param min the minimum value
	 * @param max the maximum value
	 * @param statistics the collected statistics
	 */
	public void testUniformDistribution(double min, double max, DescriptiveStatistics statistics) {
		Assert.assertEquals((min + max) / 2.0, statistics.getMean(), TestThresholds.LOW_PRECISION);
		Assert.assertEquals(Math.pow(max - min, 2.0) / 12.0, statistics.getVariance(), TestThresholds.LOW_PRECISION);
		Assert.assertEquals(0.0, statistics.getSkewness(), TestThresholds.LOW_PRECISION);
		Assert.assertEquals(-6.0 / 5.0, statistics.getKurtosis(), TestThresholds.LOW_PRECISION);
		Assert.assertEquals(min, statistics.getMin(), TestThresholds.LOW_PRECISION);
		Assert.assertEquals(max, statistics.getMax(), TestThresholds.LOW_PRECISION);
	}

}
