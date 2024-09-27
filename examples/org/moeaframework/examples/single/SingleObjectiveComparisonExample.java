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
package org.moeaframework.examples.single;

import java.io.IOException;

import org.moeaframework.algorithm.single.DifferentialEvolution;
import org.moeaframework.algorithm.single.EvolutionStrategy;
import org.moeaframework.algorithm.single.GeneticAlgorithm;
import org.moeaframework.algorithm.single.SimulatedAnnealing;
import org.moeaframework.core.Problem;

/**
 * Compares the final solutions found using the different single-objective optimizers.
 */
public class SingleObjectiveComparisonExample {
	
	public static void main(String[] args) throws IOException {
		Problem problem = new Rosenbrock();
		
		System.out.println("Genetic Algorithm (GA):");
		GeneticAlgorithm ga = new GeneticAlgorithm(problem);
		ga.run(100000);
		ga.getResult().display();
		
		System.out.println();
		System.out.println("Differential Evolution (DE):");
		DifferentialEvolution de = new DifferentialEvolution(problem);
		de.run(100000);
		de.getResult().display();
		
		System.out.println();
		System.out.println("Evolution Strategy (ES):");
		EvolutionStrategy es = new EvolutionStrategy(problem);
		es.run(100000);
		es.getResult().display();
		
		System.out.println();
		System.out.println("Simulated Annealing (SA):");
		SimulatedAnnealing sa = new SimulatedAnnealing(problem);
		sa.run(100000);
		sa.getResult().display();
	}

}
