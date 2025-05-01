

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SudokuFileReader {
    
    public static List<int[][]> readPuzzles(String filePath) throws IOException {
        List<int[][]> puzzles = new ArrayList<>();
        
        // Debug information
        File file = new File(filePath);
        System.out.println("Reading file: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        
        if (!file.exists()) {
            System.out.println("File does not exist!");
            
            // Try looking in the current directory
            File currentDirFile = new File("puzzles.txt");
            System.out.println("Trying current directory: " + currentDirFile.getAbsolutePath());
            System.out.println("File exists in current directory: " + currentDirFile.exists());
            
            if (currentDirFile.exists()) {
                filePath = "puzzles.txt";
                file = currentDirFile;
            } else {
                return puzzles;
            }
        }
        
        System.out.println("File size: " + file.length() + " bytes");
        
        // Read all lines from the file
        List<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                System.out.println("Read line: [" + line + "]");
            }
        }
        
        System.out.println("Read " + allLines.size() + " lines from file");
        
        // If file is empty or couldn't be read, use a hardcoded puzzle for testing
        if (allLines.isEmpty()) {
            System.out.println("File is empty or couldn't be read. Using test puzzle.");
            int[][] testPuzzle = {
                {5,3,0,0,7,0,0,0,0},
                {6,0,0,1,9,5,0,0,0},
                {0,9,8,0,0,0,0,6,0},
                {8,0,0,0,6,0,0,0,3},
                {4,0,0,8,0,3,0,0,1},
                {7,0,0,0,2,0,0,0,6},
                {0,6,0,0,0,0,2,8,0},
                {0,0,0,4,1,9,0,0,5},
                {0,0,0,0,8,0,0,7,9}
            };
            puzzles.add(testPuzzle);
            return puzzles;
        }
        
        // Process the lines to find puzzles
        boolean inPuzzle = false;
        List<String> currentPuzzleLines = new ArrayList<>();
        
        for (int i = 0; i < allLines.size(); i++) {
            String line = allLines.get(i).trim();
            
            if (line.startsWith("Puzzle")) {
                System.out.println("Found puzzle header at line " + (i+1) + ": " + line);
                inPuzzle = true;
                currentPuzzleLines.clear();
                continue;
            }
            
            if (inPuzzle && !line.isEmpty()) {
                currentPuzzleLines.add(line);
                System.out.println("Added puzzle line: " + line);
                
                // Check if we have a complete puzzle (9 lines for a 9x9 puzzle)
                if (currentPuzzleLines.size() == 9) {
                    System.out.println("Collected 9 lines for a puzzle");
                    int[][] puzzle = new int[9][9];
                    
                    for (int row = 0; row < 9; row++) {
                        String puzzleLine = currentPuzzleLines.get(row);
                        System.out.println("Processing row " + row + ": " + puzzleLine);
                        
                        for (int col = 0; col < Math.min(9, puzzleLine.length()); col++) {
                            char c = puzzleLine.charAt(col);
                            puzzle[row][col] = (c == '.' || c == '0') ? 0 : Character.getNumericValue(c);
                        }
                    }
                    
                    puzzles.add(puzzle);
                    System.out.println("Added puzzle to list. Total puzzles: " + puzzles.size());
                    inPuzzle = false;
                }
            }
        }
        
        return puzzles;
    }
}
