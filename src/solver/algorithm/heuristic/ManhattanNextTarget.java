package solver.algorithm.heuristic;

import solver.core.Board;
import solver.core.State;

public class ManhattanNextTarget implements Heuristic {
    @Override
    public int estimate(State state, Board board) {
        int targetX;
        int targetY;

        if (state.nextNumber <= board.maxNumber) {
            int[] coords = board.targetCoords.get(state.nextNumber);
            targetX = coords[0];
            targetY = coords[1];
        } 
        else {
            targetX = board.goalX;
            targetY = board.goalY;
        }

        return Math.abs(state.x - targetX) + Math.abs(state.y - targetY);
    }
}
