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
package org.moeaframework.mock;

import java.util.concurrent.atomic.AtomicInteger;

import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;

public abstract class MockProblem extends AbstractProblem {
	
	private final AtomicInteger count;
	
	public MockProblem(int numberOfVariables, int numberOfObjectives) {
		this(numberOfVariables, numberOfObjectives, 0);
	}

	public MockProblem(int numberOfVariables, int numberOfObjectives, int numberOfConstraints) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints);
		
		count = new AtomicInteger();
	}
	
	@Override
	public void evaluate(Solution solution) {
		// Simple way to make the objective values variable but deterministic
		int value = solution.getVariable(0).hashCode();
				
		for (int i = 0; i < getNumberOfObjectives(); i++) {
			solution.setObjective(i, i % 2 == 0 ? value : Integer.MAX_VALUE - value);
		}
		
		count.incrementAndGet();
	}

	@Override
	public Solution newSolution() {
		return new Solution(getNumberOfVariables(), getNumberOfObjectives(), getNumberOfConstraints());
	}
	
	public int getCallCount() {
		return count.get();
	}

}
