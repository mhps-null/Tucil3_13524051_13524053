package solver;

import solver.algorithm.*;
import solver.algorithm.heuristic.*;
import solver.core.Board;
import solver.parser.InputParser;
import solver.visualization.cli.PlaybackCLI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            // INPUT FILE
            Board board = InputParser.parse("test/input.txt");

            // ALGORITHM
            System.out.print("Algoritma (UCS/GBFS/A*): ");
            String algoChoice = sc.nextLine();

            SearchAlgorithm algorithm;

            switch (algoChoice.toUpperCase()) {

                case "UCS":
                    algorithm = new UCS();
                    break;

                case "GBFS":
                    algorithm = new GBFS(new ManhattanGoal());
                    break;

                case "A*":
                    algorithm = new AStar(new ManhattanGoal());
                    break;

                default:
                    System.out.println("Algoritma tidak valid.");
                    return;
            }

            // SOLVE
            long start = System.currentTimeMillis();

            SearchResult result = algorithm.solve(board);

            long end = System.currentTimeMillis();

            // OUTPUT
            if (result.found) {

                System.out.println("\n=== SOLUSI DITEMUKAN ===");
                System.out.println("Path       : " + result.path);
                System.out.println("Cost       : " + result.totalCost);
                System.out.println("Iterasi    : " + result.iterations);
                System.out.println("Waktu      : " + (end - start) + " ms");

                // PLAYBACK
                System.out.print("\nPlayback? (y/n): ");
                String playback = sc.nextLine();

                if (playback.equalsIgnoreCase("y")) {

                    PlaybackCLI.play(
                            board,
                            result.history,
                            sc
                    );
                }

            } else {

                System.out.println("\nTidak ada solusi.");
                System.out.println("Iterasi : " + result.iterations);
                System.out.println("Waktu   : " + (end - start) + " ms");
            }

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}