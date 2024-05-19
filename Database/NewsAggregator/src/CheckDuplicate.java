import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class CheckDuplicate {
    public static void main(String[] args) {
        String fileName = "Blockworks.txt";
        HashSet<String> uniqueLines = new HashSet<>();
        int duplicateLineCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!uniqueLines.add(line)) {
                    duplicateLineCount++;
                    System.out.println(line);
                }
            }
            System.out.println("Total number of duplicate lines: " + duplicateLineCount);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}