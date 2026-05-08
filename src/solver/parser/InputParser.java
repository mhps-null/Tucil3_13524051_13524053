package solver.parser;

import solver.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputParser {

    public static Board parse(String filepath) {
        try (Scanner sc = new Scanner(new File(filepath))) {

            // Read N M
            if (!sc.hasNextInt())
                throw new RuntimeException("Missing N");
            int N = sc.nextInt();

            if (!sc.hasNextInt())
                throw new RuntimeException("Missing M");
            int M = sc.nextInt();

            if (N <= 0 || M <= 0) {
                throw new RuntimeException("Invalid board size");
            }

            Board board = new Board(N, M);

            int startCount = 0;
            int goalCount = 0;

            // Read grid
            for (int i = 0; i < N; i++) {
                if (!sc.hasNext())
                    throw new RuntimeException("Missing grid row");

                String line = sc.next();
                if (line.length() != M) {
                    throw new RuntimeException("Invalid row length at row " + i);
                }

                for (int j = 0; j < M; j++) {
                    Tile tile = Tile.fromChar(line.charAt(j));
                    board.grid[i][j] = tile;

                    if (tile.type == TileType.START) {
                        board.startX = i;
                        board.startY = j;
                        startCount++;
                    }

                    if (tile.type == TileType.GOAL) {
                        board.goalX = i;
                        board.goalY = j;
                        goalCount++;
                    }

                    if (tile.type == TileType.NUMBER) {
                        if (board.targetCoords.containsKey(tile.value)) {
                            throw new RuntimeException("Duplicate number found: " + tile.value);
                        }

                        board.targetCoords.put(tile.value, new int[]{i, j});

                        board.maxNumber = Math.max(board.maxNumber, tile.value);
                    }
                }
            }

            if (board.maxNumber != -1) {
                if (board.targetCoords.size() != board.maxNumber + 1) {
                    throw new RuntimeException("The number sequence is incomplete! The maximum number is " + 
                        board.maxNumber + " but there (is/are) only " + board.targetCoords.size() + " number tile(s).");
                }
            }

            if (startCount != 1)
                throw new RuntimeException("Must have exactly 1 START");

            if (goalCount != 1)
                throw new RuntimeException("Must have exactly 1 GOAL");

            // Read cost
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (!sc.hasNextInt()) {
                        throw new RuntimeException("Invalid cost matrix");
                    }

                    int costValue = sc.nextInt();

                    if (costValue < 0) {
                        throw new RuntimeException("Invalid cost at (" + i + "," + j + "): cost can't be negative.");
                    }
                    
                    board.cost[i][j] = costValue;
                }
            }

            return board;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filepath);
        }
    }
}