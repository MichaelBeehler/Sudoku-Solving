/****************************
 * Program 3 - Sudoku Solving
 * Program By: Michael Beehler, Brian Quintero
 * Date Last Edited: May 1st, 2025
 * Description: Solve Sudoku games of varying difficulty using BFS and DLS

 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
****************************/
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SudokuMain {
    
    public static void main(String[] args) {
        
        // Try to read puzzles from puzzles.txt
        try {
            File puzzlesFile = new File("puzzles.txt");
            Scanner puzzleScanner = new Scanner(puzzlesFile);

        }
        catch (Exception e) {
            System.out.println(e);

        }
        try {
            // Read puzzles from file
            List<int[][]> puzzles = SudokuFileReader.readPuzzles(puzzleFile);
            
            if (puzzles.isEmpty()) {
                System.out.println("No puzzles found in the file.");
                return;
            }
            
            System.out.println("Found " + puzzles.size() + " puzzles.");
            
            // Process each puzzle
            for (int i = 0; i < puzzles.size(); i++) {
                int[][] puzzle = puzzles.get(i);
                System.out.println("\nProcessing Puzzle " + (i + 1) + " (Size: " + puzzle.length + "x" + puzzle.length + ")");
                
                // Print the original puzzle
                System.out.println("Original Puzzle:");
                printGrid(puzzle);
                
                // Solve using BFS
                System.out.println("\nSolving with BFS...");
                BFSSolver bfsSolver = new BFSSolver();
                long bfsStartTime = System.currentTimeMillis();
                boolean bfsSolved = bfsSolver.solve(puzzle, 5); // Find up to 5 solutions
                long bfsEndTime = System.currentTimeMillis();
                
                if (bfsSolved) {
                    System.out.println("BFS found " + bfsSolver.getSolutions().size() + " solution(s) in " + 
                                      bfsSolver.getSteps() + " steps and " + (bfsEndTime - bfsStartTime) + "ms");
                    System.out.println("First solution:");
                    printGrid(bfsSolver.getSolutions().get(0));
                } else {
                    System.out.println("BFS could not solve the puzzle.");
                }
                
                // Solve using DLS
                System.out.println("\nSolving with DLS...");
                DLSSolver dlsSolver = new DLSSolver();
                long dlsStartTime = System.currentTimeMillis();
                // For DLS, we set a depth limit based on the number of empty cells
                int emptyCount = countEmptyCells(puzzle);
                int depthLimit = emptyCount * 2; // A heuristic for depth limit
                boolean dlsSolved = dlsSolver.solve(puzzle, depthLimit, 5); // Find up to 5 solutions
                long dlsEndTime = System.currentTimeMillis();
                
                if (dlsSolved) {
                    System.out.println("DLS found " + dlsSolver.getSolutions().size() + " solution(s) in " + 
                                      dlsSolver.getSteps() + " steps and " + (dlsEndTime - dlsStartTime) + "ms");
                    System.out.println("First solution:");
                    printGrid(dlsSolver.getSolutions().get(0));
                } else {
                    System.out.println("DLS could not solve the puzzle with depth limit " + depthLimit);
                }
                
                // Compare the algorithms
                System.out.println("\nComparison:");
                System.out.println("BFS Steps: " + bfsSolver.getSteps() + ", Time: " + (bfsEndTime - bfsStartTime) + "ms");
                System.out.println("DLS Steps: " + dlsSolver.getSteps() + ", Time: " + (dlsEndTime - dlsStartTime) + "ms");
                
                if (bfsSolved && dlsSolved) {
                    if (bfsSolver.getSteps() < dlsSolver.getSteps()) {
                        System.out.println("BFS was more efficient in terms of steps.");
                    } else if (dlsSolver.getSteps() < bfsSolver.getSteps()) {
                        System.out.println("DLS was more efficient in terms of steps.");
                    } else {
                        System.out.println("Both algorithms took the same number of steps.");
                    }
                    
                    if ((bfsEndTime - bfsStartTime) < (dlsEndTime - dlsStartTime)) {
                        System.out.println("BFS was faster in terms of time.");
                    } else if ((dlsEndTime - dlsStartTime) < (bfsEndTime - bfsStartTime)) {
                        System.out.println("DLS was faster in terms of time.");
                    } else {
                        System.out.println("Both algorithms took the same amount of time.");
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading puzzle file: " + e.getMessage());
        }
    }

    // Given in
    private static void generateGrid () {

    }
    
    /**
     * Prints a Sudoku grid.
     */
    private static void printGrid(int[][] grid) {
        int size = grid.length;
        int boxSize = (int) Math.sqrt(size);
        
        for (int row = 0; row < size; row++) {
            if (row > 0 && row % boxSize == 0) {
                for (int i = 0; i < size + boxSize - 1; i++) {
                    System.out.print("-");
                }
                System.out.println();
            }
            
            for (int col = 0; col < size; col++) {
                if (col > 0 && col % boxSize == 0) {
                    System.out.print("|");
                }
                
                if (grid[row][col] == 0) {
                    System.out.print(".");
                } else {
                    System.out.print(grid[row][col]);
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Counts the number of empty cells in a grid.
     */
    private static int countEmptyCells(int[][] grid) {
        int count = 0;
        int size = grid.length;
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == 0) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
