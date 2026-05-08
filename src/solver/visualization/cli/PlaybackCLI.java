package solver.visualization.cli;

import solver.core.*;
import java.util.List;
import java.util.Scanner;

public class PlaybackCLI {

    public static void play(Board board, List<State> history, Scanner sc) {
        int idx = 0;
        boolean needsRender = true;

        while (true) {
            if (needsRender) {
                System.out.println("\nStep " + idx);
                RendererCLI.render(board, history.get(idx));
                needsRender = false;
            }

            System.out.println("\n[n] next | [p] prev | [j] jump | [q] quit");
            System.out.print("Masukkan perintah: ");
            String cmd = sc.next();

            if (cmd.equals("n")) {
                if (idx < history.size() - 1) {
                    idx++;
                    needsRender = true;
                } else {
                    System.out.println("[PERINGATAN] Sudah berada di step paling akhir! Tidak bisa next.");
                }
            } else if (cmd.equals("p")) {
                if (idx > 0) {
                    idx--;
                    needsRender = true;
                } else {
                    System.out.println("[PERINGATAN] Sudah berada di step paling awal! Tidak bisa prev.");
                }
            } else if (cmd.equals("j")) {
                System.out.print("Masukkan step: ");
                int target = sc.nextInt();
                if (target >= 0 && target < history.size()) {
                    idx = target;
                    needsRender = true;
                } else {
                    System.out.println("[PERINGATAN] Step tujuan di luar batas!");
                }
            } else if (cmd.equals("q")) {
                System.out.println("Keluar dari mode Playback...");
                break;
            } else {
                System.out.println("[PERINGATAN] Perintah tidak dikenali.");
            }
        }
    }
}