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
package org.moeaframework;

import org.moeaframework.core.Settings;

/**
 * Compares floating-point numbers allowing a small relative error when determining equality.  Using a relative error
 * allows better scaling across floating-point values of varying magnitudes.
 */
public class RelativeError implements FloatingPointError {

	/**
	 * The maximum relative error permitted when considering if two floating-point numbers are equal.
	 */
	private final double relativeError;

	/**
	 * Creates a comparator of floating-point values allowing a small relative error when determining equality.
	 * 
	 * @param epsilon the maximum relative error permitted when considering if two floating-point numbers are equal
	 */
	public RelativeError(double relativeError) {
		super();
		this.relativeError = relativeError;
	}

	@Override
	public void assertEquals(final double expected, final double actual) {
		if (Math.abs(expected - actual) > Settings.EPS) {
			double error = 0.0;

			if (Math.abs(expected) > Math.abs(actual)) {
				error = Math.abs((expected - actual) / expected);
			} else {
				error = Math.abs((expected - actual) / actual);
			}

			if (error > relativeError) {
				//relative error breaks down when the values approach 0; this is an attempt to correct using
				//absolute difference
				if (((expected != 0.0) && (actual != 0.0)) || (Math.abs(expected - actual) > relativeError)) {
					Assert.fail("values " + expected + " and " + actual + " differ by more than " +
							(relativeError*100.0) + "% error");
				}
			}
		}
	}

}
