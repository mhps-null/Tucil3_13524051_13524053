package solver.core;

public class Board {
    public int N, M;
    public Tile[][] grid;
    public int[][] cost;
    public int maxNumber = -1;

    public int startX = -1, startY = -1;
    public int goalX = -1, goalY = -1;


    public Board(int N, int M) {
        this.N = N;
        this.M = M;
        grid = new Tile[N][M];
        cost = new int[N][M];
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < M;
    }
}