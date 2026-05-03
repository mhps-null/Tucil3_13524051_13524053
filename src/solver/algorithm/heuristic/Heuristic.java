package solver.algorithm.heuristic;

import solver.core.*;

public interface Heuristic {
    int estimate(State state, Board board);
}