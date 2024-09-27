import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;

public class ExecutorProblem {
    public static void executeProblem(Problem problem, List<String> componentName){
        NondominatedPopulation result = new org.moeaframework.Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(10000)
                .withProperty("populationSize", 20)
                .run();

        int variableWidth = 5;
        int objectiveWidth = 15;

        for (int i = 1; i <= result.get(0).getNumberOfVariables(); i++) {
            System.out.printf("%-" + variableWidth + "s", "V" + i);
        }
        for (int i = 1; i <= result.get(0).getNumberOfObjectives(); i++) {
            System.out.printf("%-" + objectiveWidth + "s", "Objective" + i);
        }
        System.out.println();

        int solutionIndex = 0;
        for (Solution solution : result) {
            String fileName = "output_" + (solutionIndex++) + ".csv";
            try (FileWriter writer = new FileWriter(fileName)) {
                int labelIndex = 0;
                for (int i = 0; i < solution.getNumberOfVariables(); i++) {
                    int varValue = (int) Math.round(((RealVariable) solution.getVariable(i)).getValue());
                    if (i % 3 == 0) {
                        if (i > 0) {
                            writer.append('\n'); // Separate each variable with a comma
                        }
                        writer.append(componentName.get(labelIndex++));
                    }
                    // Append the variable value to the line
                    writer.append(',');
                    writer.append(String.valueOf(varValue));
                    System.out.printf("%-" + variableWidth + "d", varValue);
                }
                writer.append('\n');
            } catch (IOException e) {
                System.out.println("Error writing to file: " + fileName);
                e.printStackTrace();
            }
            for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
                System.out.printf("%-" + objectiveWidth + ".8f", solution.getObjective(j));
            }
            System.out.println();
        }
    }
}


