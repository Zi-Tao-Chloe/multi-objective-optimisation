import org.moeaframework.core.Algorithm;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

public class StepAlgorithm {
    public static void stepAlgorithm(Problem problem){
        Algorithm algorithm = new CustomNSGAII(problem);

        algorithm.step();
        while (!algorithm.isTerminated() && algorithm.getNumberOfEvaluations() < 10001) {
            algorithm.step();
        }

        NondominatedPopulation firstGen = ((CustomNSGAII) algorithm).getFirstGeneration();
        NondominatedPopulation lastGen = ((CustomNSGAII) algorithm).getLastGeneration();
        if (firstGen != null) {
            printGeneration(firstGen, "First Generation:");
        }

        if (lastGen != null) {
            printGeneration(lastGen, "Last Generation:");
        }
    }
    public static void printGeneration(NondominatedPopulation generation, String title) {
        System.out.println(title);
        int variableWidth = 5;
        for (Solution solution : generation) {
            for (int i = 0; i < solution.getNumberOfVariables(); i++) {
                int varValue = (int) Math.round(((RealVariable) solution.getVariable(i)).getValue());
                System.out.printf("%-" + variableWidth + "d", varValue);
            }
            System.out.println();
        }
    }
}
