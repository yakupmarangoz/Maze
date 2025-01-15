import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Interface extends JFrame {
    int N = 10;
    char[][] graph = new char[N][N];
    JButton[][] tiles = new JButton[N][N];
    PathFinder.Cell start;
    PathFinder.Cell end;

    public void InitUI() {
        setTitle("Pathfinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(N, N));
        initializeGraph();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = new JButton(String.valueOf(graph[i][j]));
                tiles[i][j].setBackground(getTileColor(graph[i][j]));
                int x = i, y = j;
                tiles[i][j].addActionListener(e -> handleTileClick(x, y));
                gridPanel.add(tiles[i][j]);
            }
        }

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start Pathfinding");
        JButton generateButton = new JButton("Generate Random Grid");
        JButton resetButton = new JButton("Reset");

        startButton.addActionListener(e -> startPathfinding());
        generateButton.addActionListener(e -> regenerateGrid());
        resetButton.addActionListener(e -> resetGrid());

        controlPanel.add(startButton);
        controlPanel.add(generateButton);
        controlPanel.add(resetButton);

        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private void initializeGraph() {
        Random random = new Random();
        char[] tiles = {' ', 'W', 'M', 'F', 'I'}; 
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                graph[i][j] = tiles[random.nextInt(tiles.length)];
            }
        }
    }
    
    private void regenerateGrid() {
        initializeGraph();
        start = null;
        end = null;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j].setText(String.valueOf(graph[i][j]));
                tiles[i][j].setBackground(getTileColor(graph[i][j]));
            }
        }
    }
    private void resetGrid() {
        start = null;
        end = null;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j].setText(String.valueOf(graph[i][j]));
                tiles[i][j].setBackground(getTileColor(graph[i][j]));
            }
        }
    }
    private Color getTileColor(char tile) {
        switch (tile) {
            case 'W': return Color.BLACK;  
            case 'M': return Color.ORANGE; 
            case 'F': return Color.RED;    
            case 'I': return Color.CYAN;   
            default: return Color.WHITE;   
        }
    }

    private void handleTileClick(int x, int y) {
        PathFinder.Cell clickedCell = new PathFinder.Cell(x, y);
        if (start != null && start.equals(clickedCell)) {
            start = null;
            tiles[x][y].setBackground(getTileColor(graph[x][y]));
            tiles[x][y].setText(String.valueOf(graph[x][y]));
        } else if (end != null && end.equals(clickedCell)) {
            end = null;
            tiles[x][y].setBackground(getTileColor(graph[x][y]));
            tiles[x][y].setText(String.valueOf(graph[x][y]));
        } else if (start == null) {
            start = clickedCell;
            tiles[x][y].setBackground(Color.GREEN);
            tiles[x][y].setText("S");
        } else if (end == null) {
            end = clickedCell;
            tiles[x][y].setBackground(Color.BLUE);
            tiles[x][y].setText("E");
        } else {
            JOptionPane.showMessageDialog(this, "Start and End are already set. Click on either to deselect, or press 'Generate Random Grid' to reset.");
        }
    }
    

    private void startPathfinding() {
        if (start == null || end == null) {
            JOptionPane.showMessageDialog(this, "Please select start and end points!");
            return;
        }

        List<PathFinder.Cell> path = PathFinder.DijkstraPath(graph, end, start);
        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No path found!");
        } else {
            for (PathFinder.Cell cell : path) {
                if (!cell.equals(start) && !cell.equals(end)) { 
                    tiles[cell.x][cell.y].setBackground(Color.MAGENTA);
                }
            }
            JOptionPane.showMessageDialog(this, "Path found!");
            JOptionPane.showMessageDialog(this, "Cost is: " + PathFinder.TotalCost(path, graph));
        }
    }

    
}
