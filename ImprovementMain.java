import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImprovementMain {
    public static void main (String[] args) {

    }

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
