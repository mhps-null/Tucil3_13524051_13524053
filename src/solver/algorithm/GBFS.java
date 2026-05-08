package solver.algorithm;

import solver.core.*;
import solver.engine.*;
import solver.algorithm.heuristic.Heuristic;

import java.util.*;

public class GBFS implements SearchAlgorithm {

    private final Heuristic heuristic;

    public GBFS(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public SearchResult solve(Board board) {

        PriorityQueue<State> pq = new PriorityQueue<>(
                Comparator.comparingInt(s -> heuristic.estimate(s, board)));

        Set<VisitedKey> visited = new HashSet<>();
        List<State> history = new ArrayList<>();

        State start = new State(board.startX, board.startY);
        pq.add(start);

        int iterations = 0;

        while (!pq.isEmpty()) {
            State current = pq.poll();
            iterations++;

            VisitedKey key = new VisitedKey(current.x, current.y, current.nextNumber);

            if (visited.contains(key))
                continue;
            visited.add(key);

            history.add(new State(current));

            // GOAL
            if (current.x == board.goalX &&
                    current.y == board.goalY &&
                    current.nextNumber == board.maxNumber + 1) {

                return new SearchResult(
                        true,
                        current.gCost,
                        current.path,
                        iterations,
                        history);
            }

            // EXPAND
            for (Direction dir : Direction.values()) {
                Optional<State> next = Movement.slide(board, current, dir);
                
                if (next.isPresent()) {
                    State nextState = next.get();
                    nextState.parent = current;
                    nextState.lastDir = dir;
                    pq.add(nextState);
                } else {
                    State deadState = new State(current, dir);
                    history.add(deadState);
                }
            }
        }

        return new SearchResult(false, -1, "", iterations, history);
    }
}