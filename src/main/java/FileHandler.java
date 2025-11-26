import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * Writes a successful recommendation to the output file.
     * Format:
     * Line 1: User Name,User ID
     * Line 2: Recommended Movie Titles (comma-separated)
     *
     * @param writer          the BufferedWriter to write to
     * @param userName        the user's name
     * @param userId          the user's ID
     * @param recommendations set of recommended movie titles
     * @throws IOException if writing fails
     */
    public void writeRecommendation(BufferedWriter writer, String userName, String userId, Set<String> recommendations)
            throws IOException {
        // First line: User Name,User ID
        writer.write(userName + "," + userId);
        writer.newLine();

        // Second line: Recommended Titles (comma-separated)
        String recommendedTitles = String.join(",", recommendations);
        writer.write(recommendedTitles);
        writer.newLine();
    }

    /**
     * Writes an error entry to the output file in CSV format.
     * Format: User Name,User ID,Error Message
     * Only the FIRST error encountered should be written (error hierarchy).
     *
     * @param writer       the BufferedWriter to write to
     * @param userName     the user's name (may be invalid)
     * @param userId       the user's ID (may be invalid)
     * @param errorMessage the error message to write
     * @throws IOException if writing fails
     */
    public void writeError(BufferedWriter writer, String userName, String userId, String errorMessage)
            throws IOException {
        // Format: User Name,User ID,Error Message
        // Remove ANSI color codes from error message for clean CSV output
        String cleanError = removeAnsiCodes(errorMessage);

        writer.write(userName + "," + userId + "," + cleanError);
        writer.newLine();
    }

    /**
     * Determines the first error based on error hierarchy and writes it to file.
     * Error Priority (highest to lowest):
     * 1. User Name errors
     * 2. User ID errors
     * 3. No favorite movies / Empty recommendations
     *
     * @param writer                 the BufferedWriter to write to
     * @param userName               the user's name
     * @param userId                 the user's ID
     * @param userNameError          error message for user name (null if valid)
     * @param userIdError            error message for user ID (null if valid)
     * @param noRecommendationsError error message for no recommendations (null if
     *                               valid)
     * @throws IOException if writing fails
     */
    public void writeFirstError(BufferedWriter writer, String userName, String userId,
            String userNameError, String userIdError, String noRecommendationsError) throws IOException {
        // Write only the first error according to hierarchy
        if (userNameError != null) {
            writeError(writer, userName, userId, userNameError);
        } else if (userIdError != null) {
            writeError(writer, userName, userId, userIdError);
        } else if (noRecommendationsError != null) {
            writeError(writer, userName, userId, noRecommendationsError);
        }
    }

    /**
     * Removes ANSI color codes from a string for clean file output.
     *
     * @param text the text containing ANSI codes
     * @return the text with ANSI codes removed
     */
    private String removeAnsiCodes(String text) {
        if (text == null) {
            return "";
        }
        // Remove ANSI escape sequences (e.g., \u001B[31m, \u001B[0m)
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
