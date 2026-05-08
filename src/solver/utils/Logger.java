package solver.utils;

import solver.core.Board;
import solver.core.State;
import solver.core.Tile;
import solver.core.TileType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Logger {
    
    public static void saveResult(String filepath, Board board, List<State> solutionPath, int cost, int iters, long time) {
        String outPath = filepath.replace(".txt", "_Result.txt");
        
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
                
                // Cetak Board ke File
                for (int r = 0; r < board.N; r++) {
                    for (int c = 0; c < board.M; c++) {
                        if (r == s.x && c == s.y) writer.print('Z');
                        else {
                            Tile t = board.grid[r][c];
                            // Sesuaikan dengan char asli map-mu
                            if (t.type == TileType.WALL) writer.print('X');
                            else if (t.type == TileType.LAVA) writer.print('L');
                            else if (t.type == TileType.GOAL) writer.print('O');
                            else if (t.type == TileType.NUMBER) writer.print(t.value);
                            else writer.print('*');
                        }
                    }
                    writer.println();
                }
                writer.println();
            }
        } catch (IOException e) {
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
}