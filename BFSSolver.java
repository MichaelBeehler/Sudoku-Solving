import java.util.*;

/**
 * Breadth-First Search implementation for solving Sudoku puzzles.
 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
 */
public class BFSSolver {
    private final List<int[][]> solutions;
    private int steps;
    
    public BFSSolver() {
        this.solutions = new ArrayList<>();
        this.steps = 0;
    }
    
    /**
     * Solves the Sudoku puzzle using BFS.
     * @param initialGrid The initial Sudoku grid
     * @param maxSolutions Maximum number of solutions to find (0 for all)
     * @return true if at least one solution was found
     */
    public boolean solve(int[][] initialGrid, int maxSolutions) {
        solutions.clear();
        steps = 0;
        
        int size = initialGrid.length;
        Queue<int[][]> queue = new LinkedList<>();
        queue.add(copyGrid(initialGrid, size));
        
        while (!queue.isEmpty() && (maxSolutions == 0 || solutions.size() < maxSolutions)) {
            int[][] currentGrid = queue.poll();
            steps++;
            
            // Create a temporary graph with the current grid
            SudokuGraph tempGraph = new SudokuGraph(currentGrid);
            int[] emptyCell = tempGraph.findEmptyCell();
            
            if (emptyCell == null) {
                // No empty cells, we found a solution
                solutions.add(currentGrid);
                continue;
            }
            
            int row = emptyCell[0];
            int col = emptyCell[1];
            
            // Try each possible value for the empty cell
            for (int value = 1; value <= size; value++) {
                if (tempGraph.isValid(row, col, value)) {
                    int[][] newGrid = copyGrid(currentGrid, size);
                    newGrid[row][col] = value;
                    queue.add(newGrid);
                }
            }
        }
        
        return !solutions.isEmpty();
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
     * Gets the solutions found by BFS.
     */
    public List<int[][]> getSolutions() {
        return solutions;
    }
    
    /**
     * Gets the number of steps taken by BFS.
     */
    public int getSteps() {
        return steps;
    }
}
