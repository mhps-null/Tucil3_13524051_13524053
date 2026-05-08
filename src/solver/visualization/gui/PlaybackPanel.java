package solver.visualization.gui;

import solver.core.State;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlaybackPanel extends JPanel {
    private List<State> solution;
    private int currentIndex = 0;
    private Timer autoTimer;
    private MainWindow main;
    
    private JSpinner stepSpinner;
    private JSpinner speedSpinner;
    private JButton playPauseBtn;

    public PlaybackPanel(MainWindow main) {
        this.main = main;
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setOpaque(false);

        JButton backBtn = new JButton("🔙 Kembali");
        backBtn.setBackground(new Color(100, 100, 100));
        backBtn.setForeground(Color.WHITE);
        
        JButton prevBtn = new JButton("◀ Prev");
        JButton nextBtn = new JButton("Next ▶");
        playPauseBtn = new JButton("Play");
        
        stepSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
        speedSpinner = new JSpinner(new SpinnerNumberModel(500, 50, 3000, 50)); 

        autoTimer = new Timer(500, e -> {
            if (currentIndex < solution.size() - 1) {
                currentIndex++;
                updateBoard(true); 
            } else {
                autoTimer.stop();
                playPauseBtn.setText("Play");
            }
        });

        speedSpinner.addChangeListener(e -> {
            int speed = (Integer) speedSpinner.getValue();
            autoTimer.setDelay(speed);
        });

        backBtn.addActionListener(e -> {
            stopPlayback();
            main.goBackToConfig();
        });

        prevBtn.addActionListener(e -> { 
            if(currentIndex > 0) { currentIndex--; updateBoard(true); } 
        });
        
        nextBtn.addActionListener(e -> { 
            if(currentIndex < solution.size()-1) { currentIndex++; updateBoard(true); } 
        });
        
        stepSpinner.addChangeListener(e -> {
            int target = (Integer) stepSpinner.getValue();
            if (target != currentIndex && target >= 0 && target < solution.size()) {
                currentIndex = target;
                updateBoard(false); 
            }
        });

        playPauseBtn.addActionListener(e -> {
            if (autoTimer.isRunning()) {
                stopPlayback();
            } else {
                if (currentIndex >= solution.size() - 1) {
                    currentIndex = 0;
                    updateBoard(false); 
                }
                int speed = (Integer) speedSpinner.getValue();
                autoTimer.setDelay(speed);
                autoTimer.start();
                playPauseBtn.setText("Pause");
            }
        });

        add(backBtn);
        add(new JLabel(" | "));
        add(prevBtn);
        add(playPauseBtn);
        add(nextBtn);
        add(createWhiteLabel(" Step:"));
        add(stepSpinner);
        add(createWhiteLabel(" Delay(ms):"));
        add(speedSpinner);
    }
    
    private JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    public void setSolution(List<State> solution) {
        this.solution = solution;
        this.currentIndex = 0;
        
        int maxIndex = Math.max(0, solution.size() - 1);
        ((SpinnerNumberModel) stepSpinner.getModel()).setMaximum(maxIndex);
        stepSpinner.setValue(0);
        
        stopPlayback();
        updateBoard(false);
    }

    public void stopPlayback() {
        if (autoTimer != null && autoTimer.isRunning()) {
            autoTimer.stop();
            playPauseBtn.setText("Play");
        }
    }

    private void updateBoard(boolean animate) {
        if (solution == null || solution.isEmpty()) return;
        
        stepSpinner.setValue(currentIndex);
        
        if (animate) {
            int currentSpeed = (Integer) speedSpinner.getValue();
            int animDuration = (int)(currentSpeed * 0.85); 
            main.animateBoard(solution.get(currentIndex), animDuration);
        } else {
            main.setBoardStateInstant(solution.get(currentIndex));
        }
    }
}