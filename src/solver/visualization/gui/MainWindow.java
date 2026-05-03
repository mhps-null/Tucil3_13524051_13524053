package solver.visualization.gui;

import solver.core.*;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private BoardPanel boardPanel;

    public MainWindow() {
        setTitle("Ice Sliding Puzzle Solver");
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel();
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        ControlPanel controlPanel = new ControlPanel();

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Dummy data
        Board dummy = createDummyBoard();
        State s = new State(5, 1);

        boardPanel.setState(dummy, s);

        setVisible(true);
    }

    private Board createDummyBoard() {
        Board b = new Board(7, 7);

        String[] grid = {
                "XXXXXXX",
                "X0****X",
                "X**X**X",
                "X****OX",
                "X***1LX",
                "XZ**X*X",
                "XXXXXXX"
        };

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                b.grid[i][j] = Tile.fromChar(grid[i].charAt(j));
            }
        }

        return b;
    }
}