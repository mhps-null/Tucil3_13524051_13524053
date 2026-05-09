package solver.visualization.gui;

import solver.core.Board;
import solver.core.State;
import solver.algorithm.*;
import solver.algorithm.heuristic.*;
import solver.parser.InputParser;
import solver.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private BoardPanel boardPanel;
    private JPanel rightPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private Board currentBoard;
    private SearchResult latestResult;
    private List<State> winningPath;
    private String currentFilepath;
    private String usedAlgorithm = "";
    private String usedHeuristic = "";
    
    private JTextArea logArea;
    private PlaybackPanel playbackPanel;

    public MainWindow() {
        setTitle("Ice Sliding Puzzle Solver");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boardPanel = new BoardPanel();
        
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(43, 43, 43)); 
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        JButton uploadBtn = new JButton("Unggah File Peta Baru");
        uploadBtn.addActionListener(e -> handleUpload());
        headerPanel.add(uploadBtn);
        rightPanel.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        setupPhases();
        rightPanel.add(cardPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, rightPanel);
        splitPane.setDividerLocation(700); 
        add(splitPane);

        setVisible(true);
    }

    private void setupPhases() {
        JPanel phase1 = new JPanel(new GridBagLayout());
        phase1.setOpaque(false);
        JLabel emptyLabel = createWhiteLabel("Silakan unggah file dari menu di atas.");
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        phase1.add(emptyLabel);
        cardPanel.add(phase1, "PHASE_1");

        JPanel phase2 = new JPanel(new BorderLayout(10, 10));
        phase2.setOpaque(false);
        phase2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerConfig = new JPanel();
        centerConfig.setLayout(new BoxLayout(centerConfig, BoxLayout.Y_AXIS));
        centerConfig.setOpaque(false);

        centerConfig.add(createWhiteLabel("[Z] Player  : Lingkaran Oranye"));
        centerConfig.add(Box.createVerticalStrut(5));
        centerConfig.add(createWhiteLabel("[X] Wall    : Petak Abu-abu"));
        centerConfig.add(Box.createVerticalStrut(5));
        centerConfig.add(createWhiteLabel("[L] Lava    : Petak Merah"));
        centerConfig.add(Box.createVerticalStrut(5));
        centerConfig.add(createWhiteLabel("[O] Goal    : Petak Hijau"));
        centerConfig.add(Box.createVerticalStrut(5));
        centerConfig.add(createWhiteLabel("[#] Number  : Teks Biru"));
        centerConfig.add(Box.createVerticalStrut(40));

        JComboBox<String> algoCb = new JComboBox<>(new String[]{"UCS", "GBFS", "AStar", "IDAStar"});
        JComboBox<String> heurCb = new JComboBox<>(new String[]{"Manhattan To Goal", "Manhattan To Next Target", "Manhattan Total To Goal"});
        JLabel heurLabel = createWhiteLabel("Pilih Heuristik:");

        heurLabel.setVisible(false);
        heurCb.setVisible(false);
        algoCb.addActionListener(e -> {
            boolean isHeuristicNeeded = !algoCb.getSelectedItem().equals("UCS");
            heurLabel.setVisible(isHeuristicNeeded);
            heurCb.setVisible(isHeuristicNeeded);
        });

        algoCb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        heurCb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        centerConfig.add(createWhiteLabel("Pilih Algoritma Pencarian:"));
        centerConfig.add(algoCb);
        centerConfig.add(Box.createVerticalStrut(20));
        centerConfig.add(heurLabel);
        centerConfig.add(heurCb);

        JPanel runPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        runPanel.setOpaque(false);
        JButton runBtn = new JButton("CARI SOLUSI");
        runBtn.setPreferredSize(new Dimension(200, 40));
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.addActionListener(e -> handleRun((String)algoCb.getSelectedItem(), heurCb.getSelectedIndex()));
        runPanel.add(runBtn);

        phase2.add(centerConfig, BorderLayout.CENTER);
        phase2.add(runPanel, BorderLayout.SOUTH);
        cardPanel.add(phase2, "PHASE_2");

        // --- PHASE 3 (HASIL) ---
        JPanel phase3 = new JPanel(new BorderLayout());
        phase3.setOpaque(false);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(100, 200)); 
        phase3.add(scrollPane, BorderLayout.NORTH);

        JPanel playbackContainer = new JPanel(new BorderLayout());
        playbackContainer.setOpaque(false);
        
        JButton saveBtn = new JButton("Simpan Jalur Solusi ke File");
        saveBtn.addActionListener(e -> handleSave());
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.setOpaque(false);
        savePanel.add(saveBtn);
        
        playbackPanel = new PlaybackPanel(this);
        
        playbackContainer.add(savePanel, BorderLayout.NORTH);
        playbackContainer.add(playbackPanel, BorderLayout.CENTER);
        
        phase3.add(playbackContainer, BorderLayout.CENTER);
        cardPanel.add(phase3, "PHASE_3");
    }

    private JLabel createWhiteLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, 14)); // Pakai Monospaced agar rata
        return label;
    }

    private void handleUpload() {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                currentFilepath = fc.getSelectedFile().getAbsolutePath();
                currentBoard = InputParser.parse(currentFilepath);
                setBoardStateInstant(new State(currentBoard.startX, currentBoard.startY));
                
                cardLayout.show(cardPanel, "PHASE_2");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal memuat file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRun(String algo, int heurIdx) {
        usedAlgorithm = algo;
        if (heurIdx == 0) usedHeuristic = "MG";
        else if (heurIdx == 1) usedHeuristic = "MNT";
        else if (heurIdx == 2) usedHeuristic = "MT";

        Heuristic h = null;
        if (heurIdx == 0) h = new ManhattanGoal();
        else if (heurIdx == 1) h = new ManhattanNextTarget();
        else if (heurIdx == 2) h = new ManhattanTotal();

        SearchAlgorithm solver;
        if(algo.equals("UCS")) solver = new UCS();
        else if(algo.equals("GBFS")) solver = new GBFS(h);
        else if(algo.equals("AStar")) solver = new AStar(h);
        else solver = new IDAStar(h);

        long start = System.currentTimeMillis();
        latestResult = solver.solve(currentBoard);
        long timeMs = System.currentTimeMillis() - start;

        if (latestResult.found) {
            winningPath = new ArrayList<>();
            State temp = latestResult.history.get(latestResult.history.size()-1);
            while(temp != null) {
                winningPath.add(0, temp);
                temp = temp.parent;
            }

            String traceLog = Logger.getSearchTraceString(latestResult, timeMs);
            
            logArea.setText(traceLog);
            logArea.setCaretPosition(0); 

            playbackPanel.setSolution(winningPath);
        } else {
            logArea.setText("=== GAGAL ===\nTidak ada solusi ditemukan.");
            playbackPanel.setSolution(new ArrayList<>());
        }
        cardLayout.show(cardPanel, "PHASE_3");
    }
    
    public void goBackToConfig() {
        if (currentBoard != null) {
            setBoardStateInstant(new State(currentBoard.startX, currentBoard.startY));
        }
        cardLayout.show(cardPanel, "PHASE_2");
    }

    public void setBoardStateInstant(State s) {
        boardPanel.setState(currentBoard, s);
    }

    public void animateBoard(State s, int durationMs) {
        boardPanel.animateToState(s, durationMs);
    }

    private void handleSave() {
        if (latestResult != null && latestResult.found) {
            Logger.saveResult(currentFilepath, currentBoard, winningPath, latestResult.totalCost, latestResult.iterations, 0, usedAlgorithm, usedHeuristic);
            
            String suffix = usedAlgorithm.equals("UCS") ? usedAlgorithm : usedAlgorithm + "_" + usedHeuristic;
            JOptionPane.showMessageDialog(this, "Berhasil disimpan sebagai file _Result_" + suffix + ".txt!");
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada solusi untuk disimpan.");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}