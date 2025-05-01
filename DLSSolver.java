import java.util.*;

/**
 * Depth-Limited Search implementation for solving Sudoku puzzles.
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Ade Chandra Nugraha, Asep Id Hadiana, and Deden Witarsyah (2022)
 */
public class DLSSolver {
    private final List<int[][]> solutions;
    private int steps;
    
    public DLSSolver() {
        this.solutions = new ArrayList<>();
        this.steps = 0;
    }
    
    /**
     * Solves the Sudoku puzzle using Depth-Limited Search.
     * @param initialGrid The initial Sudoku grid
     * @param maxDepth Maximum depth to search
     * @param maxSolutions Maximum number of solutions to find (0 for all)
     * @return true if at least one solution was found
     */
    public boolean solve(int[][] initialGrid, int maxDepth, int maxSolutions) {
        solutions.clear();
        steps = 0;
        
        dls(initialGrid, 0, maxDepth, maxSolutions);
        
        return !solutions.isEmpty();
    }
    
    /**
     * Recursive helper for DLS.
     */
    private boolean dls(int[][] currentGrid, int depth, int maxDepth, int maxSolutions) {
        steps++;
        
        if (depth > maxDepth) {
            return false;
        }
        
        SudokuGraph tempGraph = new SudokuGraph(currentGrid);
        int[] emptyCell = tempGraph.findEmptyCell();
        
        if (emptyCell == null) {
            // No empty cells, we found a solution
            solutions.add(copyGrid(currentGrid, tempGraph.getSize()));
            return maxSolutions == 0 || solutions.size() < maxSolutions;
        }
        
        int row = emptyCell[0];
        int col = emptyCell[1];
        int size = tempGraph.getSize();
        
        // Try each possible value for the empty cell
        for (int value = 1; value <= size; value++) {
            if (tempGraph.isValid(row, col, value)) {
                int[][] newGrid = copyGrid(currentGrid, size);
                newGrid[row][col] = value;
                
                if (!dls(newGrid, depth + 1, maxDepth, maxSolutions)) {
                    return false;
                }
                
                if (maxSolutions > 0 && solutions.size() >= maxSolutions) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Creates a deep copy of a grid.
     */
    private int[][] copyGrid(int[][] grid, int size) {
        int[][] copy = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, size);
        }
        return copy;
    }
    
    /**
     * Gets the solutions found by DLS.
     */
    public List<int[][]> getSolutions() {
        return solutions;
    }
    
    /**
     * Gets the number of steps taken by DLS.
     */
    public int getSteps() {
        return steps;
    }
}
