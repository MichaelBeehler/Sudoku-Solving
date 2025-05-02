import java.util.*;

/**
 * Graph representation of a Sudoku puzzle.
 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
 */
public class SudokuGraph {
    private final int size; // Size of the grid (standard is 9)
    private final int boxSize; // Size of each box (standard is 3)
    private final int[][] grid;
    private final List<List<Integer>> adjacencyList;
    private final int totalCells;

    public SudokuGraph(int[][] initialGrid) {
        this.grid = initialGrid;
        this.size = initialGrid.length;
        this.boxSize = (int) Math.sqrt(size);
        this.totalCells = size * size;
        
        // Initialize adjacency list for the graph
        adjacencyList = new ArrayList<>(totalCells);
        for (int i = 0; i < totalCells; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        
        buildGraph();
    }
    
    /**
     * Builds the graph representation of the Sudoku puzzle.
     * Each cell is connected to cells in the same row, column, and box.
     */
    private void buildGraph() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int cellId = row * size + col;
                
                // Connect to cells in the same row
                for (int c = 0; c < size; c++) {
                    if (c != col) {
                        adjacencyList.get(cellId).add(row * size + c);
                    }
                }
                
                // Connect to cells in the same column
                for (int r = 0; r < size; r++) {
                    if (r != row) {
                        adjacencyList.get(cellId).add(r * size + col);
                    }
                }
                
                // Connect to cells in the same box
                int boxRow = row / boxSize;
                int boxCol = col / boxSize;
                for (int r = boxRow * boxSize; r < (boxRow + 1) * boxSize; r++) {
                    for (int c = boxCol * boxSize; c < (boxCol + 1) * boxSize; c++) {
                        if (r != row && c != col) {
                            adjacencyList.get(cellId).add(r * size + c);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Gets a copy of the current grid.
     */
    public int[][] copyGrid() {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, size);
        }
        return copy;
    }
    
    /**
     * Gets the adjacency list for a specific cell.
     */
    public List<Integer> getAdjacent(int cellId) {
        return adjacencyList.get(cellId);
    }
    
    /**
     * Gets the size of the grid.
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Gets the box size.
     */
    public int getBoxSize() {
        return boxSize;
    }
    
    /**
     * Gets the value at a specific cell.
     */
    public int getValue(int row, int col) {
        return grid[row][col];
    }
    
    /**
     * Sets the value at a specific cell.
     */
    public void setValue(int row, int col, int value) {
        grid[row][col] = value;
    }
    
    /**
     * Checks if a value is valid at a specific position.
     */
    public boolean isValid(int row, int col, int value) {
        // Check row
        for (int c = 0; c < size; c++) {
            if (grid[row][c] == value) {
                return false;
            }
        }
        
        // Check column
        for (int r = 0; r < size; r++) {
            if (grid[r][col] == value) {
                return false;
            }
        }
        
        // Check box
        int boxRow = row - row % boxSize;
        int boxCol = col - col % boxSize;
        for (int r = boxRow; r < boxRow + boxSize; r++) {
            for (int c = boxCol; c < boxCol + boxSize; c++) {
                if (grid[r][c] == value) {
                    return false;
                }
            }
        }
        
        return true;
    }

    // Given a row and column, return the values that can be placed in that location
    public List<Integer> validValueList (int row, int col) {
        List<Integer> validValues = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            if (isValid(row, col, i)) {
                validValues.add(i);
            }
        }

        return validValues;
    }

    public boolean isPuzzleSolved () {
        for (int row[] : grid) {
            for (int col : row) {
                if (col == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Finds the next empty cell.
     * @return int[] with row and column, or null if no empty cell exists
     */
    public int[] findEmptyCell() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == 0) {
                    return new int[]{row, col};
                }
            }
        }
        return null; // No empty cell found
    }
}
