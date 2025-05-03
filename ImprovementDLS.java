/****************************
 * Program 3 - Sudoku Solving - ImprovementDFS
 * Program By: Michael Beehler, Brian Quintero
 * Date Last Edited: May 2nd, 2025
 * Description: Use threading in order to improve search speed for different sized sudoku games

 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
****************************/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ImprovementDLS {
    private final List<int[][]> solutions;
    
    public ImprovementDLS() {
        this.solutions = Collections.synchronizedList(new ArrayList<>());
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

        int [][] initialGrid = initialGraph.copyGrid();

        // Start the threading process, using one thread per available processor.
        // We will use threading in order to speed up search time, allowing bigger grids to run on our machines
        ExecutorService sudokuExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        
        // This list stores the results of future computations
        // This is important to have, as we are doing threading
        // If, after running the search, we can retrieve a value from this ArrayList, that means we've found a solution
        List<Future<Boolean>> futures = new ArrayList<>();

        // We are using a label so that we can jump out of all of these nested loops
        // We want to do this, as we need to exit from here as soon as we find an empty cell and start running our threads
        outerLoop:
        for (int currRow = 0; currRow < initialGrid.length; currRow ++) {
            for (int currCol = 0; currCol < initialGrid[currRow].length; currCol ++) {
                // If we have found an empty cell, we can start running the multiple threads
                if (initialGrid[currRow][currCol] == 0) {
                    // Get a list of the possible values we can place in the empty cell
                    List<Integer> validValues = initialGraph.validValueList (currRow, currCol);
                    // For these valid values, create a new branch that will run threads that test that specific "path"
                    for (int validVal : validValues) {
                        int[][] branch = initialGraph.copyGrid();
                        branch [currRow][currCol] = validVal;

                        // CHATGPT helped with the specific threading syntax here
                        // We are running the dls seach for the branches we created above on different threads, with the
                        // goal of finding solutions quickly
                        Callable<Boolean> task = () -> dls(branch, maxDepth - 1);
                        futures.add(sudokuExecutor.submit(task));
                    }
                    break outerLoop;
                }
            }
        }

        // Finish executing the, then stop running the thread
        sudokuExecutor.shutdown();

        // hasBeenFound will represent if we have found a solution
        boolean hasBeenFound = false;

        // See if any solution has been found
        for (Future<Boolean> future : futures) {
            try {
                if (future.get()) {
                    hasBeenFound = true;
                }
            }
            catch (InterruptedException | ExecutionException e) {
                System.out.println(e);
            }
        }

        // Stop the running of all threads and tasks, showing that we're finished
        sudokuExecutor.shutdownNow();
        System.out.println("Number of DLS solutions Found: " + solutions.size());
        // Return true if a solution has been found
        return hasBeenFound;
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
}
