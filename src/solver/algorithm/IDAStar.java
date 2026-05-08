package solver.algorithm;

import solver.algorithm.heuristic.Heuristic;
import solver.core.Board;
import solver.core.State;
import solver.engine.Direction;
import solver.engine.Movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IDAStar implements SearchAlgorithm {
    private final Heuristic heuristic;
    private int iterations;

    public IDAStar(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public SearchResult solve(Board board) {
        this.iterations = 0;
        State start = new State(board.startX, board.startY);
        int threshold = heuristic.estimate(start, board);
        
        List<State> history = new ArrayList<>();

        while (true) {
            history.clear();
            Object result = search(start, threshold, board, history);
            
            if (result instanceof SearchResult) {
                return (SearchResult) result;
            }
            
            int nextThreshold = (Integer) result;
            if (nextThreshold == Integer.MAX_VALUE) {
                return new SearchResult(false, 0, "", iterations, new ArrayList<>());
            }
            threshold = nextThreshold;
        }
    }

    private Object search(State current, int threshold, Board board, List<State> history) {
        iterations++;
        
        int f = current.gCost + heuristic.estimate(current, board);
        
        if (f > threshold) {
            return f;
        }
        
        history.add(current);

        if (current.x == board.goalX && current.y == board.goalY && current.nextNumber == board.maxNumber + 1) {
            return new SearchResult(
                        true,
                        current.gCost,
                        current.path,
                        iterations,
                        new ArrayList<>(history));
        }

        int min = Integer.MAX_VALUE;
        
        for (Direction dir : Direction.values()) {
            Optional<State> nextOpt = Movement.slide(board, current, dir);
            
            if (nextOpt.isPresent()) {
                State next = nextOpt.get();
                
                if (isStateInHistory(next, history)) continue;

                Object res = search(next, threshold, board, history);
                
                if (res instanceof SearchResult) {
                    return res;
                }
                
                int t = (Integer) res;
                if (t < min) min = t;
            }
        }
        
        history.remove(history.size() - 1);
        return min;
    }

    private boolean isStateInHistory(State next, List<State> history) {
        for (State s : history) {
            if (s.x == next.x && s.y == next.y && s.nextNumber == next.nextNumber) {
                return true;
            }
        }
        return false;
    }
}