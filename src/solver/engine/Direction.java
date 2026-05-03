package solver.engine;

public enum Direction {
    UP(-1, 0, 'U'),
    DOWN(1, 0, 'D'),
    LEFT(0, -1, 'L'),
    RIGHT(0, 1, 'R');

    public final int dx;
    public final int dy;
    public final char symbol;

    Direction(int dx, int dy, char symbol) {
        this.dx = dx;
        this.dy = dy;
        this.symbol = symbol;
    }
}