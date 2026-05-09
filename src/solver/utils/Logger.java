package solver.utils;

import solver.core.Board;
import solver.core.State;
import solver.core.Tile;
import solver.core.TileType;
import solver.algorithm.SearchResult;
import solver.visualization.cli.RendererCLI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Logger {
    
    public static void saveResult(String filepath, Board board, List<State> solutionPath, int cost, int iters, long time, String algo, String heur) {
        String baseName = filepath;
        if (filepath.endsWith(".txt")) {
            baseName = filepath.substring(0, filepath.length() - 4);
        }

        String outPath;
        if (algo.equals("UCS")) {
            outPath = baseName + "_Result_" + algo + ".txt";
        } else {
            outPath = baseName + "_Result_" + algo + "_" + heur + ".txt";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(outPath))) {
            writer.println("=== LAPORAN SOLUSI ===");
            writer.println("Status  : Solusi Ditemukan");
            writer.println("Path    : " + solutionPath.get(solutionPath.size()-1).path);
            writer.println("Cost    : " + cost);
            writer.println("Iterasi : " + iters);
            writer.println("Waktu   : " + time + " ms\n");

            for (int i = 0; i < solutionPath.size(); i++) {
                State s = solutionPath.get(i);
                writer.println(i == 0 ? "Initial" : "Step " + i + " : " + s.lastDir);
                
                for (int r = 0; r < board.N; r++) {
                    for (int c = 0; c < board.M; c++) {
                        if (r == s.x && c == s.y) writer.print('Z');
                        else {
                            Tile t = board.grid[r][c];
                            if (t.type == TileType.WALL) writer.print('X');
                            else if (t.type == TileType.LAVA) writer.print('L');
                            else if (t.type == TileType.GOAL) writer.print('O');
                            else if (t.type == TileType.NUMBER) {
                                if (t.value >= s.nextNumber) writer.print(t.value);
                                else writer.print('*');
                            }
                            else writer.print('*');
                        }
                    }
                    writer.println();
                }
                writer.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFailedResult(String originalFilepath, int iterations, long timeMs) {
        String outputFilepath = originalFilepath.replace(".txt", "_Result.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(outputFilepath)))) {
            writer.println("=== HASIL PENCARIAN ===");
            writer.println("Status   : GAGAL (Tidak ada solusi)");
            writer.println("Iterasi  : " + iterations);
            writer.println("Waktu    : " + timeMs + " ms");
        } catch (IOException e) {
            System.err.println("[ERROR] Gagal menyimpan file: " + e.getMessage());
        }
    }

    public static void printSearchTrace(SearchResult result, Board board, long timeMs) {
        System.out.println("==========SOLUSI DITEMUKAN!==========");
        System.out.println("Path        : " + result.path);
        System.out.println("Total Cost  : " + result.totalCost);
        System.out.println("Iterasi     : " + result.iterations);
        System.out.println("Waktu       : " + timeMs + " ms");
        System.out.println("=====================================\n");

        System.out.println("--- CETAK LANGKAH PER LANGKAH ---");
        for (int i = 0; i < result.history.size(); i++) {
            State current = result.history.get(i);
            
            if (i == 0) {
                System.out.println("Step 0 : Initial");
            } else {
                String moveInfo = (current.lastDir != null) ? current.lastDir.toString() : "?";
                if (current.isGameOver) {
                    int parentIdx = 0;
                    for (int j = i - 1; j >= 0; j--) {
                        if (!result.history.get(j).isGameOver && result.history.get(j).path.equals(current.path)) {
                            parentIdx = j;
                            break;
                        }
                    }
                    System.out.println("Step " + i + " : " + moveInfo + " (Game Over, kembali ke Step " + parentIdx + ")");
                    continue;
                } else {
                    System.out.println("Step " + i + " : " + moveInfo);
                }
            }
            RendererCLI.render(board, current);
            System.out.println();
        }
    }

    public static String getSearchTraceString(SearchResult result, long timeMs) {
        StringBuilder sb = new StringBuilder();
        sb.append("==========SOLUSI DITEMUKAN!==========\n");
        sb.append(String.format("%-12s: %s\n", "Path", result.path));
        sb.append(String.format("%-12s: %d\n", "Total Cost", result.totalCost));
        sb.append(String.format("%-12s: %d\n", "Iterasi", result.iterations));
        sb.append(String.format("%-12s: %d ms\n", "Waktu", timeMs));
        sb.append("=====================================\n\n");

        sb.append("--- CETAK LANGKAH PER LANGKAH ---\n");
        for (int i = 0; i < result.history.size(); i++) {
            State current = result.history.get(i);
            
            if (i == 0) {
                sb.append("Step 0 : Initial\n");
            } else {
                String moveInfo = (current.lastDir != null) ? current.lastDir.toString() : "?";
                if (current.isGameOver) {
                    int parentIdx = 0;
                    for (int j = i - 1; j >= 0; j--) {
                        if (!result.history.get(j).isGameOver && result.history.get(j).path.equals(current.path)) {
                            parentIdx = j;
                            break;
                        }
                    }
                    sb.append("Step ").append(i).append(" : ").append(moveInfo)
                      .append(" (Game Over, kembali ke Step ").append(parentIdx).append(")\n");
                } else {
                    sb.append("Step ").append(i).append(" : ").append(moveInfo).append("\n");
                }
            }
        }
        return sb.toString();
    }
}