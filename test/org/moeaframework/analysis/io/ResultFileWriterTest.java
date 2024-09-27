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
package org.moeaframework.analysis.io;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.moeaframework.Assert;
import org.moeaframework.TempFiles;
import org.moeaframework.TestThresholds;
import org.moeaframework.analysis.io.ResultFileWriter.ResultFileWriterSettings;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.core.variable.Grammar;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.mock.MockSolution;
import org.moeaframework.mock.MockUnsupportedVariable;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.util.TypedProperties;

public class ResultFileWriterTest {
	
	private Problem problem;
	private Solution solution1; // feasible
	private Solution solution2; // feasible
	private Solution solution3; // violates constraints

	@Before
	public void setUp() {
		problem = new AbstractProblem(3, 2, 1) {
			
			@Override
			public void evaluate(Solution solution) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Solution newSolution() {
				Solution solution = new Solution(3, 2, 1);
				solution.setVariable(0, new RealVariable(0.0, 1.0));
				solution.setVariable(1, new BinaryVariable(5));
				solution.setVariable(2, new Permutation(3));
				return solution;
			}
			
		};
		
		solution1 = problem.newSolution();
		((RealVariable)solution1.getVariable(0)).setValue(0.0);
		((BinaryVariable)solution1.getVariable(1)).set(2, true);
		((Permutation)solution1.getVariable(2)).swap(0, 2);
		solution1.setObjectives(new double[] { 0.0, 1.0 });
		
		solution2 = problem.newSolution();
		((RealVariable)solution1.getVariable(0)).setValue(1.0);
		((BinaryVariable)solution1.getVariable(1)).set(1, true);
		((Permutation)solution1.getVariable(2)).swap(0, 1);
		solution2.setObjectives(new double[] { 1.0, 0.0 });
		
		solution3 = problem.newSolution();
		((RealVariable)solution1.getVariable(0)).setValue(0.5);
		((BinaryVariable)solution1.getVariable(1)).set(1, true);
		((Permutation)solution1.getVariable(2)).swap(1, 2);
		solution3.setObjectives(new double[] { 0.5, 0.5 });
		solution3.setConstraints(new double[] { -1.0 });
	}

	@After
	public void tearDown() {
		problem = null;
		solution1 = null;
		solution2 = null;
		solution3 = null;
	}
	
	@Test
	public void testSpecialCharactersInProperties() throws IOException {
		File file = TempFiles.createFile();
		
		NondominatedPopulation population = new NondominatedPopulation();

		TypedProperties properties = new TypedProperties();
		properties.setString("\"'!@#$=:%^&*()\\\r\n//\t ", "\"'!@#$=:%^&*()\\\r\n//\t ");
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, properties));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			Assert.assertEquals(properties, reader.next().getProperties());
		}
	}

	@Test
	public void testNullProperties() throws IOException {
		File file = TempFiles.createFile();
		
		NondominatedPopulation population = new NondominatedPopulation();
		TypedProperties properties = new TypedProperties();
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, (TypedProperties)null));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			Assert.assertEquals(properties, reader.next().getProperties());
		}
	}

	@Test
	public void testEmptyProperties() throws IOException {
		File file = TempFiles.createFile();
		
		NondominatedPopulation population = new NondominatedPopulation();
		TypedProperties properties = new TypedProperties();
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, properties));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			Assert.assertEquals(properties, reader.next().getProperties());
		}
	}
	
	@Test
	public void testNormal() throws IOException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution1);
		population.add(solution2);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, properties));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			ResultEntry entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
		}
	}
	
	@Test
	public void testNoVariables() throws IOException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution1);
		population.add(solution2);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");
		
		try (ResultFileWriter writer = new ResultFileWriter(problem, file,
				new ResultFileWriterSettings(Optional.empty(), Optional.of(false)))) {
			writer.append(new ResultEntry(population, properties));
		}
		
		population.clear();
		population.add(MockSolution.of().withObjectives(solution1.getObjectives()));
		population.add(MockSolution.of().withObjectives(solution2.getObjectives()));
		
		try (ResultFileReader reader = new ResultFileReader(problem, file)) {
			ResultEntry entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
		}
	}
	
	@Test
	public void testConstrainedSolution() throws IOException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution3);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, properties));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			ResultEntry entry = reader.next();
			Assert.assertEquals(0, entry.getPopulation().size());
			Assert.assertEquals(properties, entry.getProperties());
		}
	}

	@Test
	public void testAppend() throws IOException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution1);
		population.add(solution2);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");

		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			Assert.assertEquals(0, writer.getNumberOfEntries());
			writer.append(new ResultEntry(population, properties));
			writer.append(new ResultEntry(population, properties));
			Assert.assertEquals(2, writer.getNumberOfEntries());
		}

		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			Assert.assertEquals(2, writer.getNumberOfEntries());
			writer.append(new ResultEntry(population, properties));
			Assert.assertEquals(3, writer.getNumberOfEntries());
		}

		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			ResultEntry entry = null;

			Assert.assertTrue(reader.hasNext());
			entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
			
			Assert.assertTrue(reader.hasNext());
			entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
			
			Assert.assertTrue(reader.hasNext());
			entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
			
			Assert.assertFalse(reader.hasNext());
		}
	}
	
	@Test
	public void testOverwrite() throws IOException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution1);
		population.add(solution2);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");

		try (ResultFileWriter writer = ResultFileWriter.overwrite(problem, file)) {
			Assert.assertEquals(0, writer.getNumberOfEntries());
			writer.append(new ResultEntry(population, properties));
			writer.append(new ResultEntry(population, properties));
			Assert.assertEquals(2, writer.getNumberOfEntries());
		}

		try (ResultFileWriter writer = ResultFileWriter.overwrite(problem, file)) {
			Assert.assertEquals(0, writer.getNumberOfEntries());
			writer.append(new ResultEntry(population, properties));
			Assert.assertEquals(1, writer.getNumberOfEntries());
		}

		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			Assert.assertTrue(reader.hasNext());
			ResultEntry entry = reader.next();
			Assert.assertEquals(population, entry.getPopulation());
			Assert.assertEquals(properties, entry.getProperties());
			
			Assert.assertFalse(reader.hasNext());
		}
	}
	
	@Test
	public void testUnsupportedDecisionVariable() throws IOException {
		File file = TempFiles.createFile();
		
		problem = new AbstractProblem(2, 2, 1) {

			@Override
			public void evaluate(Solution solution) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Solution newSolution() {
				Solution solution = new Solution(2, 2, 1);
				solution.setVariable(0, new RealVariable(0.0, 1.0));
				solution.setVariable(1, new MockUnsupportedVariable());
				return solution;
			}
			
		};

		NondominatedPopulation population = new NondominatedPopulation();
		
		Solution solution = problem.newSolution();
		((RealVariable)solution.getVariable(0)).setValue(0.5);
		solution.setObjectives(new double[] { 0.0, 1.0 });
		population.add(solution);
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, null));
		}
		
		try (ResultFileReader reader = ResultFileReader.open(problem, file)) {
			ResultEntry entry = reader.next();
			Assert.assertEquals(1, entry.getPopulation().size());
			Assert.assertArrayEquals(solution.getObjectives(), entry.getPopulation().get(0).getObjectives(),
					TestThresholds.HIGH_PRECISION);
			Assert.assertEquals(solution.getVariable(0), entry.getPopulation().get(0).getVariable(0));
		}
	}
	
	@Test
	public void testEncode() throws IOException {
		File file = TempFiles.createFile();
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			RealVariable rv = new RealVariable(0.5, 0.0, 1.0);
			Assert.assertEquals("0.5", writer.encode(rv));
			Assert.assertFalse(writer.encode(rv).matches(".*\\s.*"));
			
			BinaryVariable bv = new BinaryVariable(5);
			bv.set(2, true);
			Assert.assertEquals("00100", writer.encode(bv));
			Assert.assertFalse(writer.encode(bv).matches(".*\\s.*"));
			
			Permutation p = new Permutation(5);
			Assert.assertEquals("0,1,2,3,4", writer.encode(p));
			Assert.assertFalse(writer.encode(p).matches(".*\\s.*"));
			
			Grammar g = new Grammar(5);
			Assert.assertEquals("Grammar(0,0,0,0,0)", writer.encode(g));
			Assert.assertFalse(writer.encode(g).matches(".*\\s.*"));
		}
	}
	
	@Test
	public void testSettings() throws ParseException {
		ResultFileWriterSettings settings = ResultFileWriterSettings.getDefault();
		Assert.assertTrue(settings.isAppend());
		Assert.assertTrue(settings.isIncludeVariables());
		
		settings = ResultFileWriterSettings.overwrite();
		Assert.assertFalse(settings.isAppend());
		Assert.assertTrue(settings.isIncludeVariables());
		
		Options options = new Options();
		options.addOption(Option.builder().longOpt("novariables").build());
		
		CommandLine commandLine = DefaultParser.builder().build().parse(options, new String[] { "--novariables" });
		settings = ResultFileWriterSettings.from(commandLine);
		Assert.assertTrue(settings.isAppend());
		Assert.assertFalse(settings.isIncludeVariables());
	}
	
	@Test
	public void testFileTimestamp() throws IOException, InterruptedException {
		File file = TempFiles.createFile();

		NondominatedPopulation population = new NondominatedPopulation();
		population.add(solution1);
		population.add(solution2);
		
		TypedProperties properties = new TypedProperties();
		properties.setString("foo", "bar");

		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			writer.append(new ResultEntry(population, properties));
			writer.append(new ResultEntry(population, properties));
		}
		
		long originalTimestamp = file.lastModified();
		
		Thread.sleep(100);
		
		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			Assert.assertEquals(2, writer.getNumberOfEntries());
		}
		
		Assert.assertEquals(originalTimestamp, file.lastModified());
		
		Thread.sleep(100);

		try (ResultFileWriter writer = ResultFileWriter.append(problem, file)) {
			Assert.assertEquals(2, writer.getNumberOfEntries());
			writer.append(new ResultEntry(population, properties));
		}

		Assert.assertNotEquals(originalTimestamp, file.lastModified());
	}
	
}
