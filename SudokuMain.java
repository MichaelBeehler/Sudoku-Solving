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
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuMain {
    
    public static void main(String[] args) {
        
        // Create an array of sudoku boards of easy, medium, and hard difficulty
        List <int[][]> sudokuBoards = readSudokuFile("puzzles.txt");
        for (int i = 0; i < sudokuBoards.size(); i ++) {
            System.out.println("Board #" + (i + 1));

            for (int[] row : sudokuBoards.get(i)) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();

            // Solve using DLS
            System.out.println("\nSolving with DLS...");
            SudokuGraph dlSudokuGraph = new SudokuGraph(sudokuBoards.get(i));
            DLSSolver dlsSolver = new DLSSolver();
            long dlsStartTime = System.nanoTime();
            // For the DLS, Set the depth limit at 81, meaning it can't traverse further
            int depthLimit = 81;
            boolean dlsSolved = dlsSolver.solve(dlSudokuGraph, depthLimit); // Find up to 5 solutions
            long dlsEndTime = System.nanoTime();
                            
            if (dlsSolved) {
                System.out.println("DLS found " + dlsSolver.getSolutions().size() + " solution(s) in " + 
                                    + (dlsEndTime - dlsStartTime) + "ns");
                System.out.println("First solution:");
                printGrid(dlsSolver.getSolutions().get(0));
            } else {
                System.out.println("DLS could not solve the puzzle with depth limit " + depthLimit);
            }
            
            // Solve using BFS
            System.out.println("\nSolving with BFS...");
            SudokuGraph bfsSudokuGraph = new SudokuGraph(sudokuBoards.get(i));
            BFSSolver bfsSolver = new BFSSolver();
            long bfsStartTime = System.nanoTime();
            boolean bfsSolved = bfsSolver.solve(bfsSudokuGraph);
            long bfsEndTime = System.nanoTime();
                            
            if (bfsSolved) {
                System.out.println("BFS found " + bfsSolver.getSolutions().size() + " solution(s) in " + (bfsEndTime - bfsStartTime) + "ns");
                System.out.println("First solution:");
                //String firstSolution = bfsSolver.getSolutions().get(0);
                printGrid(bfsSolver.getSolutions().get(0));
            } else {
                System.out.println("BFS could not solve the puzzle.");
            }

            // Compare the algorithms
            System.out.println("\nComparison:");
            System.out.println("BFS Time: " + (bfsEndTime - bfsStartTime) + "ns");
            System.out.println("DLS Time: " + (dlsEndTime - dlsStartTime) + "ns");

            if (bfsSolved && dlsSolved) {

                if ((bfsEndTime - bfsStartTime) < (dlsEndTime - dlsStartTime)) {
                    System.out.println("BFS was faster in terms of time.");
                } else if ((dlsEndTime - dlsStartTime) < (bfsEndTime - bfsStartTime)) {
                    System.out.println("DLS was faster in terms of time.");
                } else {
                    System.out.println("Both algorithms took the same amount of time.");
                }
            }
        }
    }

    // Given input, read a file and return a list of sudoku boards
    private static List<int[][]> readSudokuFile(String fileName) {
        List <int[][]> sudokuBoards = new ArrayList<>();

        // Reference: https://www.geeksforgeeks.org/java-io-bufferedreader-class-java/
        try (BufferedReader sudokBufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            // Traverse the file
            while ((line = sudokBufferedReader.readLine()) != null) {
                // Remove whitespace
                line = line.trim();
                int lineLength = line.length();
                // If the line length is not equal to 9 (the size of a standard board, don't do anything)
                if (lineLength != 9) {
                    continue;
                }
                // Initialize a 2D array that will be the board, with proper dimensions
                int [][] sudokuBoard = new int [lineLength][lineLength];

                int currRow = 0;

                do {
                    // Read through each character, get it's value, and add it to the board
                    for (int currCol = 0; currCol < lineLength; currCol ++) {
                        sudokuBoard[currRow][currCol] = Character.getNumericValue(line.charAt(currCol));
                    }
                    currRow ++;
                    
                    // Read the next line
                    if (currRow < 9) {
                        line = sudokBufferedReader.readLine();
                        if (line == null || line.length() != 9) {
                            break;
                        }
                    }
                } while (currRow < 9);
                
                // The board has been completed, add it to the list
                if (currRow == 9) {
                    sudokuBoards.add(sudokuBoard);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        // Return the list of boards
        return sudokuBoards;

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
