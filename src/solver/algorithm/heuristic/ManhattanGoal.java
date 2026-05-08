package solver.algorithm.heuristic;

import solver.core.*;

public class ManhattanGoal implements Heuristic {

    @Override
    public int estimate(State state, Board board) {
        return Math.abs(state.x - board.goalX) +
                Math.abs(state.y - board.goalY);
    }
}