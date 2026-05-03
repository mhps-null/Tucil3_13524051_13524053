package solver.core;

public class State {
    public int x, y;
    public int nextNumber;
    public int gCost;
    public String path;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
        this.nextNumber = 0;
        this.gCost = 0;
        this.path = "";
    }

    public State(State other) {
        this.x = other.x;
        this.y = other.y;
        this.nextNumber = other.nextNumber;
        this.gCost = other.gCost;
        this.path = other.path;
    }
}