import java.util.*;

/**
 * Depth-Limited Search implementation for solving Sudoku puzzles.
 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
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
    public boolean solve(SudokuGraph initialGraph, int maxDepth) {
        solutions.clear();
        boolean searchResult = dls(initialGraph.copyGrid(), maxDepth);
        System.out.println("Number of DLS solutions found: " + solutions.size());
        return !solutions.isEmpty();
    }
    
    /**
     * Recursive helper for DLS.
     */
    private boolean dls(int[][] currentGrid, int maxDepth) {
        
        // If we have traversed as far as we can, there is no solution
        if (maxDepth == 0) {
            return false;
        }
        
        // Create a new Graph based on the current state of the grid
        SudokuGraph sudokuGraph = new SudokuGraph(currentGrid);
        
        if (sudokuGraph.isPuzzleSolved()) {
            solutions.add(currentGrid);
            return true;
        }

        // Do a DLS
        for (int currRow = 0; currRow < currentGrid.length; currRow ++) {
            for (int currCol = 0; currCol < currentGrid[currRow].length; currCol ++) {
                // If the value in the current grid is a zero, that means we have to find a value to input
                if (currentGrid [currRow][currCol] == 0) {
                    // Create a list of all the values that we can put in the slot
                    List <Integer> values = sudokuGraph.validValueList(currRow, currCol);
                    // Put each possible value in the grid
                    for (int possibleValue : values) {
                        currentGrid [currRow][currCol] = possibleValue;
                        
                        // Run the DLS with the cell that we input, testing if it's a valid solution
                        if (dls(currentGrid, maxDepth - 1)) {
                            // If the DLS is successful, return true
                            return true;
                        }
                        // If the search was not successful, set the value ack to zero, and move on
                        currentGrid[currRow][currCol] = 0;
                    }
                    return false;
                }
            }
        }
        return false;
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
