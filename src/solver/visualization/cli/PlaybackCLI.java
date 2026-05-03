package solver.visualization.cli;

import solver.core.*;
import java.util.List;
import java.util.Scanner;

public class PlaybackCLI {

    public static void play(Board board, List<State> history, Scanner sc) {
        int idx = 0;

        while (true) {
            System.out.println("\nStep " + idx);
            RendererCLI.render(board, history.get(idx));

            System.out.println("\n[n] next | [p] prev | [j] jump | [q] quit");
            String cmd = sc.next();

            if (cmd.equals("n")) {
                if (idx < history.size() - 1)
                    idx++;
            } else if (cmd.equals("p")) {
                if (idx > 0)
                    idx--;
            } else if (cmd.equals("j")) {
                System.out.print("Go to step: ");
                int target = sc.nextInt();
                if (target >= 0 && target < history.size()) {
                    idx = target;
                }
            } else if (cmd.equals("q")) {
                break;
            }
        }
    }
}