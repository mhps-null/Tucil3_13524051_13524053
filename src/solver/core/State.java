package solver.core;

import solver.engine.Direction;

public class State {
    public int x, y;
    public int nextNumber;
    public int gCost;
    public String path;
    public Direction lastDir;
    public boolean isGameOver;
    public State parent;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
        this.nextNumber = 0;
        this.gCost = 0;
        this.path = "";
        this.lastDir = null; 
        this.isGameOver = false;
        this.parent = null;
    }

    public State(State other) {
        this.x = other.x;
        this.y = other.y;
        this.nextNumber = other.nextNumber;
        this.gCost = other.gCost;
        this.path = other.path;
        this.lastDir = other.lastDir;
        this.isGameOver = other.isGameOver;
        this.parent = other.parent;
    }

    public State(State previousState, Direction failedDir) {
        this.x = previousState.x;
        this.y = previousState.y;
        this.nextNumber = previousState.nextNumber;
        this.gCost = previousState.gCost;
        this.path = previousState.path;
        this.lastDir = failedDir;
        this.isGameOver = true;
        this.parent = previousState;
    }
}