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
package org.moeaframework.examples.gp.ant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.util.io.Resources;
import org.moeaframework.util.io.Resources.ResourceOption;

/**
 * Example running the ant trail problem with the Santa Fe trail.  NSGA-II
 * isn't really designed for single-objective functions, but this serves as a
 * demonstration of genetic programming.
 */
public class SantaFeExample {
	
	/**
	 * Starts the example running the ant trail problem.
	 * 
	 * @param args the command line arguments
	 * @throws FileNotFoundException if the ant trail file could not be found
	 * @throws IOException if an I/O error occurred
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		int maxMoves = 500;
		
		InputStream input = Resources.asStream(SantaFeExample.class, "santafe.trail",
				ResourceOption.REQUIRED);
		
		try (AntProblem problem = new AntProblem(input, maxMoves)) {
			// solve the ant trail instance
			NondominatedPopulation results = new Executor()
					.withProblem(problem)
					.withAlgorithm("GA")
					.withProperty("populationSize", 500)
					.withMaxEvaluations(250000)
					.run();
			
			// display the result
			problem.evaluate(results.get(0));
			problem.displayLastEvaluation();
		}
	}

}
