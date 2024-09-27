import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.core.Problem;
import java.util.ArrayList;
import java.util.List;

public class CustomNSGAII extends NSGAII {
    private List<NondominatedPopulation> generations = new ArrayList<>();

    public CustomNSGAII(Problem problem) {
        super(problem);
    }

    @Override
    protected void initialize() {
        super.initialize();
        generations.add(new NondominatedPopulation(getResult()));
    }

    @Override
    public void iterate() {
        super.iterate();
        generations.add(new NondominatedPopulation(getResult()));
    }

    public NondominatedPopulation getFirstGeneration() {
        return generations.isEmpty() ? null : generations.get(0);
    }

    public NondominatedPopulation getLastGeneration() {
        return generations.isEmpty() ? null : generations.get(generations.size() - 1);
    }
}
