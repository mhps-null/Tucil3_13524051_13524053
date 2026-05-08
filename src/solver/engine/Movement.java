package solver.engine;

import solver.core.*;

import java.util.Optional;

public class Movement {

    public static Optional<State> slide(Board board, State current, Direction dir) {
        int x = current.x;
        int y = current.y;

        int nextNumber = current.nextNumber;
        int totalCost = 0;

        while (true) {
            int nx = x + dir.dx;
            int ny = y + dir.dy;

            if (!board.isInside(nx, ny)) {
                return Optional.empty();
            }

            Tile tile = board.grid[nx][ny];

            if (tile.type == TileType.WALL) {
                break;
            }

            if (tile.type == TileType.LAVA) {
                return Optional.empty();
            }

            totalCost += board.cost[nx][ny];

            if (tile.type == TileType.NUMBER) {
                if (tile.value == nextNumber) {
                    nextNumber++;
                } else if (tile.value > nextNumber) {
                    return Optional.empty();
                }
            }

            x = nx;
            y = ny;
        }

        if (x == current.x && y == current.y) {
            return Optional.empty();
        }

        State next = new State(current);
        next.x = x;
        next.y = y;
        next.nextNumber = nextNumber;
        next.gCost += totalCost;
        next.path += dir.symbol;
        next.lastDir = dir;

        return Optional.of(next);
    }
}