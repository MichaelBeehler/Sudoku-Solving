import java.util.*;

/**
 * Breadth-First Search implementation for solving Sudoku puzzles.
 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
 */
public class BFSSolver {
    // We will store solutions in a Hash Set.
    // This is very useful in order to ensure that we are not inserting any duplicate solutions
    private Set<String> previousSolutions = new HashSet<>();
    
    
    /**
     * Solves the Sudoku puzzle using BFS.
     * @param initialGrid The initial Sudoku grid
     * @param maxSolutions Maximum number of solutions to find (0 for all)
     * @return true if at least one solution was found
     */
    public boolean solve(SudokuGraph initialGraph) {
        previousSolutions.clear();
        
        Queue<SudokuGraph> queue = new LinkedList<>();
        queue.add(initialGraph);


        // Loop while there are graphs left in the queue
        while (!queue.isEmpty()) {
            SudokuGraph currGraph = queue.poll();
            
            // If the puzzle has been solved
            if (currGraph.isPuzzleSolved()) {
                // Convert it to a string to check if it has already been found
                String solvedAsStr = convertToString(currGraph);
                // If this is a new solution, add it to the Set
                if (!previousSolutions.contains(solvedAsStr)) {
                    previousSolutions.add(solvedAsStr);
                }
            
            }

            boolean visited = false; 
            // Loop trough the graph
            for (int currRow = 0; currRow < currGraph.getSize(); currRow ++) {
                for (int currCol = 0; currCol < currGraph.getSize(); currCol ++) {
                    // If we have found an empty slot, it needs to be filled
                    if (currGraph.getValue(currRow, currCol) == 0) {
                        // Find the possible valid values that can be put in the slot
                        List <Integer> validValuesList = currGraph.validValueList(currRow, currCol);
                        
                        // For every value in the list of possible values
                        for (int validValue : validValuesList) {
                            // Create a new grid and graph that will store the new data
                            int [][] newGrid = currGraph.copyGrid();
                            newGrid[currRow][currCol] = validValue;
                            SudokuGraph newGraph = new SudokuGraph(newGrid);
                            // Add this new graph to the queue, so that it can be checked for valid solutions
                            queue.add(newGraph);

                        }
                        visited = true;
                        break;
                    } 
                }
                if (visited) {
                    break;
                }

            }
        }
        return !previousSolutions.isEmpty();
    }
    
    /**
     * Gets the solutions found by BFS.
     */
    public Set<String> getSolutions() {
        return previousSolutions;
    }
    
    private String convertToString (SudokuGraph graph) {
        StringBuilder s = new StringBuilder();

        int [][] grid = graph.copyGrid();

        for (int[] row : grid) {
            for (int col : row) {
                s.append(col);
            }
        }
        return s.toString();
    }
}
