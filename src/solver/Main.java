// package solver;

// import solver.algorithm.*;
// import solver.algorithm.heuristic.*;
// import solver.core.Board;
// import solver.core.State;
// import solver.parser.InputParser;
// import solver.utils.Logger;
// import solver.utils.Timer;
// import solver.visualization.cli.PlaybackCLI;
// import solver.visualization.cli.RendererCLI;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Scanner;
// import java.util.stream.Collectors;

// public class Main {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);

//         System.out.println("==========ICE SLIDING PUZZLE SOLVER==========");

//         // Input File
//         System.out.print("Masukkan nama file input (contoh: map1.txt): ");
//         String filename = sc.nextLine();

//         String filepath = "test/" + filename; 

//         Board board;
//         try {
//             board = InputParser.parse(filepath);
//         } catch (Exception e) {
//             System.out.println("[ERROR] Gagal membaca file: " + e.getMessage());
//             return;
//         }

//         // Pilih Algoritma
//         System.out.println("\nPilih Algoritma Pencarian:");
//         System.out.println("1. Uniform Cost Search (UCS)");
//         System.out.println("2. Greedy Best-First Search (GBFS)");
//         System.out.println("3. A* Search (AStar)");
//         System.out.println("4. Iterative Deepening A* (IDA*)");
//         System.out.print("Pilihan (1-4): ");
//         int algoChoice = sc.nextInt();

//         Heuristic heuristic = null;

//         // Pilih Heuristik (Jika algoritma membutuhkan heuristik)
//         if (algoChoice == 2 || algoChoice == 3 || algoChoice == 4) {
//             System.out.println("\nPilih Heuristik:");
//             System.out.println("1. Manhattan to Goal Asli (H1)");
//             System.out.println("2. Manhattan to Next Target (H2)");
//             System.out.println("3. Sequential Manhattan / Total Traversal (H3)");
//             System.out.print("Pilihan (1-3): ");
//             int heurChoice = sc.nextInt();

//             switch (heurChoice) {
//                 case 1:
//                     heuristic = new ManhattanGoal();
//                     break;
//                 case 2:
//                     heuristic = new ManhattanNextTarget();
//                     break;
//                 case 3:
//                     heuristic = new ManhattanTotal();
//                     break;
//                 default:
//                     System.out.println("Pilihan heuristik tidak valid. Menggunakan H1 default.");
//                     heuristic = new ManhattanGoal();
//             }
//         }

//         SearchAlgorithm solver = null;
//         switch (algoChoice) {
//             case 1:
//                 solver = new UCS();
//                 break;
//             case 2:
//                 solver = new GBFS(heuristic);
//                 break;
//             case 3:
//                 solver = new AStar(heuristic);
//                 break;
//             case 4:
//                 solver = new IDAStar(heuristic);
//                 break;
//             default:
//                 System.out.println("Pilihan algoritma tidak valid.");
//                 return;
//         }

//         System.out.println("\n[INFO] Memulai pencarian...\n");

//         Timer timer = new Timer();
//         timer.start();
//         SearchResult result = solver.solve(board);
//         timer.stop();

//         // Keluaran (Output)
//         if (result != null && result.found) {
//             System.out.println("==========SOLUSI DITEMUKAN!==========");
//             System.out.println("Path        : " + result.path);
//             System.out.println("Total Cost  : " + result.totalCost);
//             System.out.println("Iterasi     : " + result.iterations);
//             System.out.println("Waktu       : " + timer.getElapsedTime() + " ms");
//             System.out.println("=====================================\n");

//             System.out.println("--- CETAK LANGKAH PER LANGKAH ---");
//             for (int i = 0; i < result.history.size(); i++) {
//                 State current = result.history.get(i);
//                 if (i == 0) {
//                     System.out.println("Step 0 : Initial");
//                 } else {
//                     String moveInfo = (current.lastDir != null) ? current.lastDir.toString() : "?";
//                     if (current.isGameOver) {
//                         int parentIdx = 0;
//                         for (int j = i - 1; j >= 0; j--) {
//                             if (!result.history.get(j).isGameOver && result.history.get(j).path.equals(current.path)) {
//                                 parentIdx = j;
//                                 break;
//                             }
//                         }
//                         System.out.println("Step " + i + " : " + moveInfo + " (Game Over, kembali ke Step " + parentIdx + ")");
//                         continue;
//                     } else {
//                         System.out.println("Step " + i + " : " + moveInfo);
//                     }
//                 }
//                 RendererCLI.render(board, current);
//                 System.out.println();
//             }

//             List<State> solutionPath = new ArrayList<>();
//             State temp = result.history.get(result.history.size() - 1);

//             while (temp != null) {
//                 solutionPath.add(0, temp);
//                 temp = temp.parent;
//             }

//             System.out.print("\nApakah Anda ingin menyimpan solusi? (Ya/Tidak) : ");
//             if (sc.next().equalsIgnoreCase("Ya")) {
//                 Logger.saveResult(filepath, board, solutionPath, result.totalCost, result.iterations, timer.getElapsedTime());
//             }

//             System.out.print("Apakah Anda ingin melakukan playback? (Ya/Tidak) : ");
//             String playbackChoice = sc.next();
//             if (playbackChoice.equalsIgnoreCase("Ya")) {
//                 System.out.println("\nMemulai fitur Playback...");
//                 PlaybackCLI.play(board, solutionPath, sc);
//             }

//         } else {
//             int iters = (result != null) ? result.iterations : 0;
//             Logger.saveFailedResult(filepath, iters, timer.getElapsedTime());
//         }
        
//         sc.close();
//     }
// }


package solver;

import solver.visualization.gui.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}