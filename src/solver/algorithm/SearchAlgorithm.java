package solver.algorithm;

import solver.core.Board;

public interface SearchAlgorithm {
    SearchResult solve(Board board);
}