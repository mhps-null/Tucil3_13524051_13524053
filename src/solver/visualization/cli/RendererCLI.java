package solver.visualization.cli;

import solver.core.*;

public class RendererCLI {

    public static void render(Board board, State state) {
        for (int i = 0; i < board.N; i++) {
            for (int j = 0; j < board.M; j++) {

                if (i == state.x && j == state.y) {
                    System.out.print('Z');
                    continue;
                }

                Tile t = board.grid[i][j];

                switch (t.type) {
                    case WALL:
                        System.out.print('X');
                        break;
                    case PATH:
                        System.out.print('*');
                        break;
                    case START:
                        System.out.print('*');
                        break;
                    case GOAL:
                        System.out.print('O');
                        break;
                    case LAVA:
                        System.out.print('L');
                        break;
                    case NUMBER:
                        System.out.print(t.value);
                        break;
                }
            }
            System.out.println();
        }
    }
}