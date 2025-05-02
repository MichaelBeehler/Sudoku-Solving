import java.util.*;

/**
 * Improved BFS implementation for solving Sudoku puzzles.
 * This implementation uses a most-constrained-cell heuristic to improve efficiency.
 * 
 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
 */
public class ImprovementBFS {
    private List<int[][]> solutions;
    private final int MAX_SOLUTIONS = 5; // Limit number of solutions to find
    private int exploredStates; // Add this field to track states explored

    public ImprovementBFS() {
        solutions = new ArrayList<>();
        exploredStates = 0;
    }

    public boolean solve(SudokuGraph graph, int maxSolutions) {
        solutions.clear();
        exploredStates = 0; // Reset counter
        
        // Use the copyGrid method from SudokuGraph to get a copy of the initial grid
        int[][] initialGrid = graph.copyGrid();
        
        Queue<int[][]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.add(initialGrid);
        visited.add(gridToString(initialGrid));
        
        while (!queue.isEmpty() && solutions.size() < maxSolutions) {
            int[][] currentGrid = queue.poll();
            exploredStates++; // Increment counter for each state explored
            
            if (isComplete(currentGrid)) {
                solutions.add(deepCopy(currentGrid));
                continue;
            }
            
            // Improvement: Find the most constrained cell (cell with fewest valid options)
            int[] bestCell = findMostConstrainedCell(currentGrid);
            if (bestCell == null) continue;
            
            int row = bestCell[0];
            int col = bestCell[1];
            int size = currentGrid.length;
            
            // Try each possible value
            for (int num = 1; num <= size; num++) {
                if (isValid(currentGrid, row, col, num)) {
                    int[][] newGrid = deepCopy(currentGrid);
                    newGrid[row][col] = num;
                    
                    String gridStr = gridToString(newGrid);
                    if (!visited.contains(gridStr)) {
                        visited.add(gridStr);
                        queue.add(newGrid);
                    }
                }
            }
        }
        
        return !solutions.isEmpty();
    }
    
    // Improvement: Find the cell with fewest valid options to reduce branching factor
    private int[] findMostConstrainedCell(int[][] grid) {
        int minOptions = Integer.MAX_VALUE;
        int[] bestCell = null;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0) {
                    int options = countValidOptions(grid, i, j);
                    if (options < minOptions) {
                        minOptions = options;
                        bestCell = new int[]{i, j};
                        
                        // Optimization: If we find a cell with only one option, return immediately
                        if (minOptions == 1) return bestCell;
                    }
                }
            }
        }
        return bestCell;
    }
    
    private int countValidOptions(int[][] grid, int row, int col) {
        int count = 0;
        int size = grid.length;
        
        for (int num = 1; num <= size; num++) {
            if (isValid(grid, row, col, num)) {
                count++;
            }
        }
        return count;
    }
    
    private boolean isComplete(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }
    
    private boolean isValid(int[][] grid, int row, int col, int num) {
        int size = grid.length;
        int boxSize = (int) Math.sqrt(size);
        
        // Check row
        for (int j = 0; j < size; j++) {
            if (grid[row][j] == num) return false;
        }
        
        // Check column
        for (int i = 0; i < size; i++) {
            if (grid[i][col] == num) return false;
        }
        
        // Check box
        int boxRowStart = row - row % boxSize;
        int boxColStart = col - col % boxSize;
        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                if (grid[boxRowStart + i][boxColStart + j] == num) return false;
            }
        }
        
        return true;
    }
    
    private String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            for (int cell : row) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }
    
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
    
    public List<int[][]> getSolutions() {
        return solutions;
    }
    
    // Add this method to get the number of states explored
    public int getExploredStates() {
        return exploredStates;
    }
}
