package solver.visualization.gui;

import solver.core.*;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private Board board;
    private State currentState;

    public void setState(Board board, State state) {
        this.board = board;
        this.currentState = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board == null || currentState == null)
            return;

        int panelW = getWidth();
        int panelH = getHeight();

        int cellSize = Math.min(panelW / board.M, panelH / board.N);

        // center grid
        int offsetX = (panelW - (cellSize * board.M)) / 2;
        int offsetY = (panelH - (cellSize * board.N)) / 2;

        for (int i = 0; i < board.N; i++) {
            for (int j = 0; j < board.M; j++) {

                int x = offsetX + j * cellSize;
                int y = offsetY + i * cellSize;

                Tile t = board.grid[i][j];

                switch (t.type) {
                    case WALL:
                        g.setColor(new Color(40, 40, 40));
                        break;
                    case PATH:
                        g.setColor(Color.WHITE);
                        break;
                    case GOAL:
                        g.setColor(new Color(100, 200, 100));
                        break;
                    case LAVA:
                        g.setColor(new Color(220, 80, 80));
                        break;
                    case NUMBER:
                        g.setColor(new Color(255, 220, 120));
                        break;
                    default:
                        g.setColor(Color.LIGHT_GRAY);
                }

                g.fillRect(x, y, cellSize, cellSize);

                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);

                if (t.type == TileType.NUMBER) {
                    g.setColor(Color.BLACK);
                    g.drawString(
                            String.valueOf(t.value),
                            x + cellSize / 3,
                            y + cellSize / 2);
                }
            }
        }

        // actor (centered)
        g.setColor(new Color(50, 120, 255));
        g.fillOval(
                offsetX + currentState.y * cellSize + cellSize / 6,
                offsetY + currentState.x * cellSize + cellSize / 6,
                cellSize * 2 / 3,
                cellSize * 2 / 3);
    }
}