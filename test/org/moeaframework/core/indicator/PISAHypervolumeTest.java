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

import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;

public class PISAHypervolumeTest extends AbstractHypervolumeTest<PISAHypervolume> {

	@Override
	public PISAHypervolume createInstance(Problem problem, NondominatedPopulation referenceSet) {
		return new PISAHypervolume(problem, referenceSet);
	}
	
	@Override
	public PISAHypervolume createInstance(Problem problem, double[] minimum, double[] maximum) {
		return new PISAHypervolume(problem, minimum, maximum);
	}
	
	@Override
	public PISAHypervolume createInstance(Problem problem, NondominatedPopulation referenceSet, double[] referencePoint) {
		return new PISAHypervolume(problem, referenceSet, referencePoint);
	}

}
