/****************************
 * Program 3 - Sudoku Solving - Improvement Main
 * Program By: Michael Beehler, Brian Quintero
 * Date Last Edited: May 2nd,2025
 * Description: Solve Sudoku games of varying dimensions and difficulty by using BFS and DFS searches

 * CITATION:
 * Based on the paper: "Comparison Analysis of Breadth First Search and Depth Limited Search Algorithms in Sudoku Game"
 * by Lina, Tirsa & Rumetna, Matheus. (2021).
 * https://www.researchgate.net/publication/358642884_Comparison_Analysis_of_Breadth_First_Search_and_Depth_Limited_Search_Algorithms_in_Sudoku_Game
****************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImprovementMain {
    public static void main (String[] args) {
        int [][] fourbyfour = readImprovedSudokuFiles("4x4.txt", 4);
        int [][] ninebynine = readImprovedSudokuFiles("9x9.txt", 9);
        int [][] sixteenbysixteen = readImprovedSudokuFiles("16x16.txt", 16);
        int[][] ninebyninemult = readImprovedSudokuFiles("9x9mult.txt", 9);

        // Run the searches on these grids
        runSearches(fourbyfour);
        System.out.println();
        System.out.println();
        runSearches(ninebynine);
        System.out.println();
        System.out.println();
        runSearches (ninebyninemult);
        System.out.println();
        System.out.println();
        runSearches(sixteenbysixteen);
    }


    // Given an int[][], run the three searches that we are interested in
    private static void runSearches (int[][] grid) {
        ImprovementBFS bfsSolver = new ImprovementBFS();
        ImprovementDFS improvedDLSSolver = new ImprovementDFS();
        DLSSolver regDlsSolver = new DLSSolver();

        int depthLimit = 256;

        // Print the Grid
        System.out.println("Initital Grid:");
        printGrid(grid);


        // Run the Improved BFS Search
        System.out.println("\n Solving with Improved BFS: ");

        SudokuGraph bfSudokuGraph = new SudokuGraph(grid);
        long bfsStartTime = System.nanoTime();
                    
        boolean bfsSolved = bfsSolver.solve(bfSudokuGraph, 5); // Find up to 5 solutions
        long bfsEndTime = System.nanoTime();
                                    
        if (bfsSolved) {
            System.out.println("Improved BFS found " + bfsSolver.getSolutions().size() + 
                              " solution(s) in " + (bfsEndTime - bfsStartTime) + "ns");
            System.out.println("States explored by BFS: " + bfsSolver.getExploredStates());
            System.out.println("Final empty cells: " + countEmptyCells(bfsSolver.getSolutions().get(0)));
            System.out.println("First solution:");
            printGrid(bfsSolver.getSolutions().get(0));
        } else {
            System.out.println("Improved BFS could not solve the puzzle");
        }

        // Run the Improved DLS Search
        System.out.println("\nSolving with Improved DLS (This will take some time)...");
        SudokuGraph dlSudokuGraph = new SudokuGraph(grid);

        long impdlsStartTime = System.nanoTime();
        boolean dlsSolved = improvedDLSSolver.solve(dlSudokuGraph, depthLimit);
        long impdlsEndTime = System.nanoTime();
                                    
        if (dlsSolved) {
            System.out.println("Improved DLS found " + improvedDLSSolver.getSolutions().size() + 
                              " solution(s) in " + (impdlsEndTime - impdlsStartTime) + "ns");
            System.out.println("First solution:");
            printGrid(improvedDLSSolver.getSolutions().get(0));
        } else {
            System.out.println("Improved DLS could not solve the puzzle with depth limit " + depthLimit);
        }

        System.out.println("\nSolving with Regular DLS...");
        SudokuGraph regDlsGraph = new SudokuGraph(grid);

        long regdlsStartTime = System.nanoTime();
        boolean regFourdlsSolved = regDlsSolver.solve(regDlsGraph, depthLimit);
        long regdlsEndTime = System.nanoTime();
                                    
        if (regFourdlsSolved) {
            System.out.println("DLS found " + regDlsSolver.getSolutions().size() + 
                              " solution(s) in " + (regdlsEndTime - regdlsStartTime) + "ns");
            System.out.println("First solution:");
            printGrid(regDlsSolver.getSolutions().get(0));
        } else {
            System.out.println("DLS could not solve the puzzle with depth limit " + depthLimit);
        }
    }

    // This code improves upon the original by supporting non-standard 9x9 grids. 
    // It works for numbers that are perfect squares (4x4, 9x9, 16x16, etc...)
    private static int[][] readImprovedSudokuFiles(String fileName, int dimension) {
        // Initialize the board as a 2D array
        List <int[]> boardList = new ArrayList<>();

        // Reference: https://www.geeksforgeeks.org/java-io-bufferedreader-class-java/
        try (BufferedReader sudokBufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            
            // Traverse the file
            while ((line = sudokBufferedReader.readLine()) != null) {
                // Remove whitespace
                line = line.trim();

                // Go to the next line if the current is empty
                if (line.isEmpty()) {
                    continue;
                }

                // If the line matches the expected dimensions, 
                if (line.length() == dimension) {
                    int [] row = new int[dimension];

                    for (int i = 0; i < dimension; i ++) {
                        char c = Character.toUpperCase(line.charAt(i));
                        row[i] = charToInt(c);
                    }
                    boardList.add(row);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return null;
        }

        if (boardList.size() != dimension) {
            System.out.println("Invalid Board");
            return null;
        }
        int [][] board = new int [dimension][dimension];
        for (int i = 0; i < dimension; i ++) {
            board[i] = boardList.get(i);
        }
        return board;
    }

    // Given a character as input, return it's decimal equivalent (A  = 10, for example).
    // This is very useful when doing grids that require alphabets in them, like the 16x16
    private static int charToInt (char c) {
        if (c >= 'A' && c <= 'F') {
            return 10 + (c - 'A');
        }
        if (c == 'G') {
            return 16;
        }
        if (c >- '1' && c <= '9') {
            return c - '0';
        }
        return 0;
    }

    //Print a great in neat columns
    private static void printGrid(int[][] grid) {
        int size = grid.length;
        
        for (int row = 0; row < size; row++) {        
            for (int col = 0; col < size; col++) {
                System.out.printf("%4d", grid[row][col]);
            }
            System.out.println();
        }
    }
    
    private static int countEmptyCells(int[][] grid) {
        int count = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) count++;
            }
        }
        return count;
    }
}
