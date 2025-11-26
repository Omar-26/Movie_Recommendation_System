import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }

        } catch (IOException e) {
            System.out.println("ERROR: Failed to read file: " + filePath);
        }

        return lines;
    }

    public void writeFile(String filePath, List<String> content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : content) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Failed to write to file: " + filePath);
        }
    }
}
