package solver.algorithm;

import solver.core.State;
import java.util.List;

public class SearchResult {
    public boolean found;
    public int totalCost;
    public String path;
    public int iterations;
    public List<State> history;

    public SearchResult(boolean found, int cost, String path, int iterations, List<State> history) {
        this.found = found;
        this.totalCost = cost;
        this.path = path;
        this.iterations = iterations;
        this.history = history;
    }
}