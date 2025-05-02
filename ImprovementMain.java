/****************************
 * Program 3 - Sudoku Solving - Improvement
 * Program By: Michael Beehler, Brian Quintero
 * Date Last Edited: May 2nd, 2025
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

        // Solve using DLS
        System.out.println("\nSolving with DLS...");
        // Create a new graph that will hold the grid
        SudokuGraph fourByFourSudokuGraph = new SudokuGraph(fourbyfour);
        DLSSolver fourByFourdlsSolver = new DLSSolver();
        long dlsStartTime = System.nanoTime();
                    
        // Imporvement: change the depth limit to be the amount of cells
        int depthLimit = 256;
        boolean fourByFourdlsSolved = fourByFourdlsSolver.solve(fourByFourSudokuGraph, depthLimit); // Find up to 5 solutions
        long dlsEndTime = System.nanoTime();
                                    
        if (fourByFourdlsSolved) {
            System.out.println("DLS found " + fourByFourdlsSolver.getSolutions().size() + " solution(s) in " + 
                                + (dlsEndTime - dlsStartTime) + "ns");
            System.out.println("First solution:");
            printGrid(fourByFourdlsSolver.getSolutions().get(0));
        } else {
            System.out.println("DLS could not solve the puzzle with depth limit " + depthLimit);
        }

        // Solve the 16x16 Puzzle
        if (sixteenbysixteen != null) {
            for (int [] row : sixteenbysixteen) {
                for (int col : row) {
                    System.out.print((col < 10 ? " " : "") + col + " ");
                }
                System.out.println();
            }
        }
        

        // Solve using DLS
        System.out.println("\nSolving with DLS...");
        // Create a new graph that will hold the grid
        SudokuGraph dlSudokuGraph = new SudokuGraph(sixteenbysixteen);
        DLSSolver dlsSolver = new DLSSolver();
        dlsStartTime = System.nanoTime();
                    
        // Imporvement: change the depth limit to be the amount of cells
        depthLimit = 256;
        boolean dlsSolved = dlsSolver.solve(dlSudokuGraph, depthLimit); // Find up to 5 solutions
        dlsEndTime = System.nanoTime();
                                    
        if (dlsSolved) {
            System.out.println("DLS found " + dlsSolver.getSolutions().size() + " solution(s) in " + 
                                + (dlsEndTime - dlsStartTime) + "ns");
            System.out.println("First solution:");
            printGrid(dlsSolver.getSolutions().get(0));
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

                if (line.isEmpty()) {
                    continue;
                }

                // If
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
    
}
