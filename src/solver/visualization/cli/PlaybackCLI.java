package solver.visualization.cli;

import solver.core.*;
import java.util.List;
import java.util.Scanner;

public class PlaybackCLI {

    // ANSI escape codes
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String CYAN = "\033[36m";

    public static void play(Board board, List<State> history, Scanner sc) {
        if (history == null || history.isEmpty()) {
            System.out.println(RED + "Error: History kosong. Tidak ada yang bisa diputar." + RESET);
            return;
        }

        int idx = 0;
        int totalSteps = history.size();
        String statusMessage = "";

        while (true) {
            clearScreen();

            System.out.println(CYAN + BOLD + "=== SOLVER PLAYBACK ===" + RESET);
            System.out.printf("Step: %d / %d\n", idx + 1, totalSteps); // 1-based indexing
            printProgressBar(idx, totalSteps);
            System.out.println();

            RendererCLI.render(board, history.get(idx));

            if (!statusMessage.isEmpty()) {
                System.out.println("\n" + statusMessage);
                statusMessage = "";
            }

            System.out
                    .println("\n" + BOLD + "[n] next | [p] prev | [f] first | [l] last | [j] jump | [q] quit" + RESET);
            System.out.print("> ");

            String cmd = sc.next().trim().toLowerCase();

            switch (cmd) {
                case "n":
                    if (idx < totalSteps - 1) {
                        idx++;
                    } else {
                        statusMessage = RED + "[Warning] Anda sudah berada di step terakhir." + RESET;
                    }
                    break;

                case "p":
                    if (idx > 0) {
                        idx--;
                    } else {
                        statusMessage = RED + "[Warning] Anda sudah berada di step pertama." + RESET;
                    }
                    break;

                case "j":
                    System.out.print("Lompat ke step (1 - " + totalSteps + "): ");
                    if (sc.hasNextInt()) {
                        int target = sc.nextInt();
                        if (target >= 1 && target <= totalSteps) {
                            idx = target - 1; // 1-based to 0-based
                        } else {
                            statusMessage = RED + "[Warning] Step tidak valid! Masukkan angka antara 1 - " + totalSteps
                                    + "." + RESET;
                        }
                    } else {
                        statusMessage = RED + "[Warning] Input tidak valid! Harap masukkan angka." + RESET;
                        sc.next();
                    }
                    break;

                case "f":

                    if (idx != 0) {
                        idx = 0;
                    } else {
                        statusMessage = RED + "[Warning] Anda sudah berada di step pertama." + RESET;
                    }

                    break;

                case "l":

                    if (idx != totalSteps - 1) {
                        idx = totalSteps - 1;
                    } else {
                        statusMessage = RED + "[Warning] Anda sudah berada di step terakhir." + RESET;
                    }

                    break;

                case "q":
                    System.out.println(GREEN + "Keluar dari playback..." + RESET);
                    return;

                default:
                    statusMessage = RED + "[Warning] Perintah '" + cmd + "' tidak dikenali." + RESET;
                    break;
            }
        }
    }

    private static void clearScreen() {

        try {

            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {

                new ProcessBuilder(
                        "cmd",
                        "/c",
                        "cls").inheritIO().start().waitFor();

            } else {

                System.out.print("\033[H\033[2J");
                System.out.flush();
            }

        } catch (Exception e) {

            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static void printProgressBar(int currentIdx, int totalSteps) {
        int barLength = 30;
        int progress = (int) Math.round((double) (currentIdx + 1) / totalSteps * barLength);

        System.out.print("[");
        for (int i = 0; i < barLength; i++) {
            if (i < progress) {
                System.out.print("=");
            } else {
                System.out.print("-");
            }
        }
        System.out.println("]");
    }
}