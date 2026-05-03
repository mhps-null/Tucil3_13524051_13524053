package solver.visualization.gui;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    public ControlPanel() {
        setLayout(new BorderLayout());

        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton loadBtn = new JButton("Load");
        JButton runBtn = new JButton("Run");

        JComboBox<String> algoBox = new JComboBox<>(new String[] {
                "UCS", "A*", "GBFS"
        });

        JButton prevBtn = new JButton("◀");
        JButton nextBtn = new JButton("▶");

        left.add(loadBtn);
        left.add(new JLabel("Algo:"));
        left.add(algoBox);
        left.add(runBtn);

        right.add(prevBtn);
        right.add(nextBtn);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }
}