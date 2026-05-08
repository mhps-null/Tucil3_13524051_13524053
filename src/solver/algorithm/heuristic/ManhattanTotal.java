package solver.algorithm.heuristic;

import solver.core.Board;
import solver.core.State;

public class ManhattanTotal implements Heuristic {
    @Override
    public int estimate(State state, Board board) {
        int totalHeuristic = 0;
        
        if (state.nextNumber <= board.maxNumber) {
            int[] firstTarget = board.targetCoords.get(state.nextNumber);
            totalHeuristic += Math.abs(state.x - firstTarget[0]) + Math.abs(state.y - firstTarget[1]);

            for (int i = state.nextNumber; i < board.maxNumber; i++) {
                int[] currentTarget = board.targetCoords.get(i);
                int[] nextTarget = board.targetCoords.get(i + 1);
                totalHeuristic += Math.abs(currentTarget[0] - nextTarget[0]) + Math.abs(currentTarget[1] - nextTarget[1]);
            }

            int[] lastTarget = board.targetCoords.get(board.maxNumber);
            totalHeuristic += Math.abs(lastTarget[0] - board.goalX) + Math.abs(lastTarget[1] - board.goalY);
            
        } else {
            totalHeuristic += Math.abs(state.x - board.goalX) + Math.abs(state.y - board.goalY);
        }

        return totalHeuristic;
    }
}
