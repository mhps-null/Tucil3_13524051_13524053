package solver;

import solver.algorithm.*;
import solver.algorithm.heuristic.*;
import solver.core.Board;
import solver.core.State;
import solver.parser.InputParser;
import solver.utils.Logger;
import solver.utils.Timer;
import solver.visualization.cli.PlaybackCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==========ICE SLIDING PUZZLE SOLVER==========");

        // Input File
        System.out.print("Masukkan nama file input (contoh: map1.txt): ");
        String filename = sc.nextLine();

        String filepath = "test/" + filename; 

        Board board;
        try {
            board = InputParser.parse(filepath);
        } catch (Exception e) {
            System.out.println("[ERROR] Gagal membaca file: " + e.getMessage());
            return;
        }

        // Pilih Algoritma
        int algoChoice = 0;
        while (true) {
            System.out.println("\nPilih Algoritma Pencarian:");
            System.out.println("1. Uniform Cost Search (UCS)");
            System.out.println("2. Greedy Best-First Search (GBFS)");
            System.out.println("3. A* Search (AStar)");
            System.out.println("4. Iterative Deepening A* (IDA*)");
            System.out.print("Pilihan (1-4): ");
            
            try {
                algoChoice = Integer.parseInt(sc.nextLine());
                if (algoChoice >= 1 && algoChoice <= 4) {
                    break;
                } else {
                    System.out.println("Pilihan tidak valid! Masukkan angka 1 hingga 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid! Masukkan angka");
            }
        }

        Heuristic heuristic = null;
        int heurChoice = 0;

        // Pilih Heuristik (Jika algoritma membutuhkan heuristik)
        if (algoChoice == 2 || algoChoice == 3 || algoChoice == 4) {
            while (true) {
                System.out.println("\nPilih Heuristik:");
                System.out.println("1. Manhattan to Goal (H1)");
                System.out.println("2. Manhattan to Next Target (H2)");
                System.out.println("3. Manhattan Total to Goal (H3)");
                System.out.print("Pilihan (1-3): ");
                
                try {
                    heurChoice = Integer.parseInt(sc.nextLine());
                    if (heurChoice >= 1 && heurChoice <= 3) {
                        switch (heurChoice) {
                            case 1:
                                heuristic = new ManhattanGoal();
                                break;
                            case 2:
                                heuristic = new ManhattanNextTarget();
                                break;
                            case 3:
                                heuristic = new ManhattanTotal();
                                break;
                        }
                        break;
                    } else {
                        System.out.println("Pilihan tidak valid! Masukkan angka 1 hingga 3.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Pilihan tidak valid! Masukkan angka.");
                }
            }
        }

        SearchAlgorithm solver = null;
        switch (algoChoice) {
            case 1:
                solver = new UCS();
                break;
            case 2:
                solver = new GBFS(heuristic);
                break;
            case 3:
                solver = new AStar(heuristic);
                break;
            case 4:
                solver = new IDAStar(heuristic);
                break;
        }

        System.out.println("\n[INFO] Memulai pencarian...\n");

        Timer timer = new Timer();
        timer.start();
        SearchResult result = solver.solve(board);
        timer.stop();

        // Keluaran (Output)
        if (result != null && result.found) {
            Logger.printSearchTrace(result, board, timer.getElapsedTime());

            List<State> solutionPath = new ArrayList<>();
            State temp = result.history.get(result.history.size() - 1);
            while (temp != null) {
                solutionPath.add(0, temp);
                temp = temp.parent;
            }
            
            // Playback
            String playbackChoice = "";
            while (true) {
                System.out.print("\nApakah Anda ingin melakukan playback? (Y/N) : ");
                playbackChoice = sc.nextLine().trim();
                
                if (playbackChoice.equalsIgnoreCase("Y") || playbackChoice.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("Input tidak valid! Masukkan Y atau N.");
                }
            }

            if (playbackChoice.equalsIgnoreCase("Y")) {
                System.out.println("\nMemulai fitur Playback...");
                PlaybackCLI.play(board, result.history, sc);
            }

            String algoLabel = "";
            switch (algoChoice) {
                case 1: algoLabel = "UCS"; break;
                case 2: algoLabel = "GBFS"; break;
                case 3: algoLabel = "AStar"; break;
                case 4: algoLabel = "IDAStar"; break;
            }

            String heurLabel = "";
            if (algoChoice > 1) {
                switch (heurChoice) {
                    case 1: heurLabel = "MG"; break;
                    case 2: heurLabel = "MNT"; break;
                    case 3: heurLabel = "MT"; break;
                }
            }

            // Simpan File
            String saveChoice = "";
            while (true) {
                System.out.print("\nApakah Anda ingin menyimpan solusi? (Y/N) : ");
                saveChoice = sc.nextLine().trim();
                
                if (saveChoice.equalsIgnoreCase("Y") || saveChoice.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("Input tidak valid! Masukkan Y atau N.");
                }
            }

            if (saveChoice.equalsIgnoreCase("Y")) {
                Logger.saveResult(filepath, board, solutionPath, result.totalCost, result.iterations, timer.getElapsedTime(), algoLabel, heurLabel);
                
                String suffix = algoLabel.equals("UCS") ? algoLabel : algoLabel + "_" + heurLabel;
                System.out.println("[INFO] Solusi berhasil disimpan di test/" + filename.replace(".txt", "") + "_Result_" + suffix + ".txt");
            }

        } else {
            int iters = (result != null) ? result.iterations : 0;
            Logger.saveFailedResult(filepath, iters, timer.getElapsedTime());
            System.out.println("\n[INFO] Log kegagalan berhasil disimpan.");
        }
        
        sc.close();
    }
}

// package solver;

// import solver.visualization.gui.MainWindow;
// import javax.swing.SwingUtilities;

// public class Main {
//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             new MainWindow();
//         });
//     }
// }