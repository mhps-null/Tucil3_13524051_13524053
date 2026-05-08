package solver.visualization.gui;

import solver.core.Board;
import solver.core.State;
import solver.core.Tile;
import solver.core.TileType;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private Board board;
    private State currentState;
    private State targetState; 

    private float renderRow = 0;
    private float renderCol = 0;
    private Timer animTimer;

    public BoardPanel() {
        setBackground(new Color(30, 30, 30));
    }

    public void setState(Board board, State state) {
        this.board = board;
        this.currentState = state;
        this.targetState = null;
        if (state != null) {
            this.renderRow = state.x;
            this.renderCol = state.y;
        }
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
        }
        repaint();
    }

    public void animateToState(State newState, int durationMs) {
        if (animTimer != null && animTimer.isRunning()) {
            animTimer.stop();
            if (targetState != null) {
                this.currentState = targetState;
                this.renderRow = targetState.x;
                this.renderCol = targetState.y;
            }
        } else {
            if (currentState != null) {
                this.renderRow = currentState.x;
                this.renderCol = currentState.y;
            }
        }

        this.targetState = newState;

        float startRow = this.renderRow;
        float startCol = this.renderCol;
        float endRow = newState.x;
        float endCol = newState.y;
        
        int delay = 15;
        int totalFrames = Math.max(1, durationMs / delay);
        final int[] frame = {0};

        animTimer = new Timer(delay, e -> {
            frame[0]++;
            float linearProgress = Math.min(1.0f, (float) frame[0] / totalFrames);
            
            float progress = (float) Math.sin(linearProgress * Math.PI / 2);
            
            if (linearProgress >= 1.0f) {
                renderRow = endRow;
                renderCol = endCol;
                animTimer.stop();
                currentState = newState;
                targetState = null; 
            } else {
                renderRow = startRow + (endRow - startRow) * progress;
                renderCol = startCol + (endCol - startCol) * progress;
            }
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelW = getWidth();
        int panelH = getHeight();

        if (board == null || (currentState == null && targetState == null)) {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 20));
            String msg = "(Belum ada papan yang diunggah)";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(msg, (panelW - fm.stringWidth(msg)) / 2, panelH / 2);
            return;
        }

        State logicState = (currentState != null) ? currentState : targetState;

        int cellSize = Math.min(panelW / board.M, panelH / board.N) - 2;
        int offsetX = (panelW - (cellSize * board.M)) / 2;
        int offsetY = (panelH - (cellSize * board.N)) / 2;

        g.setFont(new Font("Arial", Font.BOLD, cellSize / 2));

        for (int r = 0; r < board.N; r++) {
            for (int c = 0; c < board.M; c++) {
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;
                Tile t = board.grid[r][c];

                if (t.type == TileType.WALL) g.setColor(new Color(60, 60, 60));
                else if (t.type == TileType.LAVA) g.setColor(new Color(239, 68, 68));
                else if (t.type == TileType.GOAL) g.setColor(new Color(34, 197, 94));
                else g.setColor(Color.WHITE);
                
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);

                if (t.type == TileType.NUMBER && logicState != null) {
                    if (t.value >= logicState.nextNumber) {
                        g.setColor(Color.BLUE);
                        FontMetrics fm = g.getFontMetrics();
                        String val = String.valueOf(t.value);
                        g.drawString(val, x + (cellSize - fm.stringWidth(val)) / 2, y + (cellSize + fm.getAscent()) / 2 - 4);
                    }
                }
            }
        }

        int playerX = offsetX + (int)(renderCol * cellSize);
        int playerY = offsetY + (int)(renderRow * cellSize);
        
        g.setColor(new Color(255, 165, 0)); 
        g.fillOval(playerX + 5, playerY + 5, cellSize - 10, cellSize - 10);
    }
}