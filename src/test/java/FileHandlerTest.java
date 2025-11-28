import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    private FileHandler fileHandler;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileHandler = new FileHandler();
    }

    @AfterEach
    void tearDown() {
        fileHandler = null;
    }

    // ==================== readFile() Test Cases ====================

    @Test
    void testReadFile_ValidFile_ReturnsContent() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("test.txt");
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        Files.write(testFile, expectedLines);

        // Act
        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    void testReadFile_EmptyFile_ReturnsEmptyList() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("empty.txt");
        Files.createFile(testFile);

        // Act
        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Assert
        assertTrue(actualLines.isEmpty());
    }

    @Test
    void testReadFile_FileWithWhitespace_ReturnsTrimmedLines() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("whitespace.txt");
        List<String> inputLines = Arrays.asList("  Line 1  ", "\tLine 2\t", "   Line 3");
        Files.write(testFile, inputLines);

        // Act
        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Assert
        assertEquals(Arrays.asList("Line 1", "Line 2", "Line 3"), actualLines);
    }

    @Test
    void testReadFile_NonExistentFile_ReturnsEmptyList() {
        // Arrange
        String nonExistentPath = tempDir.resolve("nonexistent.txt").toString();

        // Act
        List<String> actualLines = fileHandler.readFile(nonExistentPath);

        // Assert
        assertTrue(actualLines.isEmpty());
    }

    @Test
    void testReadFile_FileWithBlankLines_IncludesBlankLines() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("blank_lines.txt");
        List<String> inputLines = Arrays.asList("Line 1", "", "Line 3", "  ", "Line 5");
        Files.write(testFile, inputLines);

        // Act
        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Assert
        assertEquals(Arrays.asList("Line 1", "", "Line 3", "", "Line 5"), actualLines);
    }

    @Test
    void testReadFile_LargeFile_ReadsAllLines() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("large.txt");
        List<String> expectedLines = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            expectedLines.add("Line " + i);
        }
        Files.write(testFile, expectedLines);

        // Act
        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Assert
        assertEquals(1000, actualLines.size());
        assertEquals(expectedLines, actualLines);
    }

    // ==================== writeFile() Test Cases (UPDATED) ====================

    @Test
    void testWriteFile_ValidContent_WritesSuccessfully() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("output.txt");
        String content = "Line 1" + System.lineSeparator() + "Line 2" + System.lineSeparator() + "Line 3";

        // Act
        FileHandler.writeFile(testFile, content);

        // Assert
        List<String> actualLines = Files.readAllLines(testFile);
        assertEquals(Arrays.asList("Line 1", "Line 2", "Line 3"), actualLines);
    }

    @Test
    void testWriteFile_EmptyString_WritesNewLine() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("empty_output.txt");
        String content = "";

        // Act
        FileHandler.writeFile(testFile, content);

        // Assert
        // The updated writeFile method adds a newLine() after writing content.
        // So an empty string input results in a file with a single newline.
        List<String> actualLines = Files.readAllLines(testFile);
        assertEquals(1, actualLines.size());
        assertEquals("", actualLines.get(0));
    }

    @Test
    void testWriteFile_OverwritesExistingFile() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("overwrite.txt");
        List<String> initialContent = Arrays.asList("Old Line 1", "Old Line 2");
        Files.write(testFile, initialContent);

        String newContent = "New Line 1" + System.lineSeparator() + "New Line 2";

        // Act
        FileHandler.writeFile(testFile, newContent);

        // Assert
        List<String> actualLines = Files.readAllLines(testFile);
        assertEquals(Arrays.asList("New Line 1", "New Line 2"), actualLines);
    }

    // ==================== writeRecommendation() Test Cases ====================

    @Test
    void testWriteRecommendation_ValidData_FormatsCorrectly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("recommendations.txt");
        Set<String> recommendations = new LinkedHashSet<>(Arrays.asList("Movie A", "Movie B", "Movie C"));

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "John Smith", "12345678A", recommendations);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("Movie A,Movie B,Movie C", lines.get(1));
    }

    @Test
    void testWriteRecommendation_SingleMovie_FormatsCorrectly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("single_recommendation.txt");
        Set<String> recommendations = new LinkedHashSet<>(Collections.singletonList("Movie A"));

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "Jane Doe", "987654321", recommendations);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertEquals("Jane Doe,987654321", lines.get(0));
        assertEquals("Movie A", lines.get(1));
    }

    @Test
    void testWriteRecommendation_EmptyRecommendations_WritesEmptyLine() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("empty_recommendations.txt");
        Set<String> recommendations = new LinkedHashSet<>();

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "Test User", "111111111", recommendations);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(2, lines.size());
        assertEquals("Test User,111111111", lines.get(0));
        assertEquals("", lines.get(1));
    }

    @Test
    void testWriteRecommendation_MultipleUsers_AllWrittenCorrectly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("multiple_users.txt");
        Set<String> recommendations1 = new LinkedHashSet<>(Arrays.asList("Movie A", "Movie B"));
        Set<String> recommendations2 = new LinkedHashSet<>(Arrays.asList("Movie C", "Movie D"));

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "User One", "11111111A", recommendations1);
            fileHandler.writeRecommendation(writer, "User Two", "22222222B", recommendations2);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(4, lines.size());
        assertEquals("User One,11111111A", lines.get(0));
        assertEquals("Movie A,Movie B", lines.get(1));
        assertEquals("User Two,22222222B", lines.get(2));
        assertEquals("Movie C,Movie D", lines.get(3));
    }

    @Test
    void testWriteRecommendation_UserNameWithSpaces_FormatsCorrectly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("user_with_spaces.txt");
        Set<String> recommendations = new LinkedHashSet<>(Arrays.asList("Movie A"));

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "John Michael Smith", "12345678A", recommendations);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals("John Michael Smith,12345678A", lines.get(0));
    }

    // ==================== writeError() Test Cases ====================

    @Test
    void testWriteError_SimpleError_FormatsCorrectly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("error.txt");
        String errorMessage = "Invalid user ID";

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "John Smith", "123", errorMessage);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,123,Invalid user ID", lines.get(0));
    }

    @Test
    void testWriteError_ErrorWithAnsiCodes_RemovesAnsiCodes() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("error_ansi.txt");
        String errorMessage = "\u001B[31mERROR: Invalid user ID\u001B[0m";

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "John Smith", "123", errorMessage);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,123,ERROR: Invalid user ID", lines.get(0));
        assertFalse(lines.get(0).contains("\u001B"));
    }

    @Test
    void testWriteError_MultipleAnsiCodes_RemovesAllCodes() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("error_multiple_ansi.txt");
        String errorMessage = "\u001B[31m\u001B[1mERROR:\u001B[0m \u001B[33mWarning message\u001B[0m";

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "Test User", "999", errorMessage);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals("Test User,999,ERROR: Warning message", lines.get(0));
    }

    @Test
    void testWriteError_NullErrorMessage_HandlesGracefully() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("error_null.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "John Smith", "123", null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals("John Smith,123,", lines.get(0));
    }

    @Test
    void testWriteError_InvalidUserData_WritesAsProvided() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("error_invalid_user.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "", "", "User name cannot be empty");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(",,User name cannot be empty", lines.get(0));
    }

    @Test
    void testWriteError_MultipleErrors_AllWrittenInCSVFormat() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("multiple_errors.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeError(writer, "User1", "123", "Error 1");
            fileHandler.writeError(writer, "User2", "456", "Error 2");
            fileHandler.writeError(writer, "User3", "789", "Error 3");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(3, lines.size());
        assertEquals("User1,123,Error 1", lines.get(0));
        assertEquals("User2,456,Error 2", lines.get(1));
        assertEquals("User3,789,Error 3", lines.get(2));
    }

    // ==================== writeFirstError() Test Cases ====================

    @Test
    void testWriteFirstError_UserNameErrorOnly_WritesUserNameError() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("first_error_username.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "123Invalid", "12345678A",
                    "User name must contain only letters and spaces", null, null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("123Invalid,12345678A,User name must contain only letters and spaces", lines.get(0));
    }

    @Test
    void testWriteFirstError_UserIdErrorOnly_WritesUserIdError() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("first_error_userid.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "123",
                    null, "User ID must be exactly 9 characters", null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,123,User ID must be exactly 9 characters", lines.get(0));
    }

    @Test
    void testWriteFirstError_NoRecommendationsErrorOnly_WritesNoRecommendationsError() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("first_error_no_recommendations.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "12345678A",
                    null, null, "No recommendations available");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,12345678A,No recommendations available", lines.get(0));
    }

    @Test
    void testWriteFirstError_UserNameAndUserIdErrors_WritesUserNameErrorOnly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("priority_username_userid.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "123Invalid", "123",
                    "User name must contain only letters and spaces",
                    "User ID must be exactly 9 characters",
                    null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("123Invalid,123,User name must contain only letters and spaces", lines.get(0));
    }

    @Test
    void testWriteFirstError_AllThreeErrors_WritesUserNameErrorOnly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("priority_all_errors.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "123Invalid", "123",
                    "User name must contain only letters and spaces",
                    "User ID must be exactly 9 characters",
                    "No recommendations available");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("123Invalid,123,User name must contain only letters and spaces", lines.get(0));
    }

    @Test
    void testWriteFirstError_UserIdAndNoRecommendationsErrors_WritesUserIdErrorOnly() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("priority_userid_norec.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "123",
                    null,
                    "User ID must be exactly 9 characters",
                    "No recommendations available");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,123,User ID must be exactly 9 characters", lines.get(0));
    }

    @Test
    void testWriteFirstError_NoErrors_WritesNothing() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("no_errors.txt");

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "12345678A",
                    null, null, null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertTrue(lines.isEmpty());
    }

    @Test
    void testWriteFirstError_ErrorPriorityHierarchy_VerifyOrder() throws IOException {
        // Test Priority: User Name Error (Highest) > User ID Error > No Recommendations Error (Lowest)
        Path testFile = tempDir.resolve("error_hierarchy.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            // Scenario 1: Only User Name Error
            fileHandler.writeFirstError(writer, "Invalid123", "12345678A",
                    "Invalid user name", null, null);

            // Scenario 2: Only User ID Error
            fileHandler.writeFirstError(writer, "Valid Name", "123",
                    null, "Invalid user ID", null);

            // Scenario 3: Only No Recommendations Error
            fileHandler.writeFirstError(writer, "Valid Name", "12345678A",
                    null, null, "No recommendations");

            // Scenario 4: User Name Error + User ID Error (should write User Name Error)
            fileHandler.writeFirstError(writer, "Invalid123", "123",
                    "Invalid user name", "Invalid user ID", null);

            // Scenario 5: User ID Error + No Recommendations (should write User ID Error)
            fileHandler.writeFirstError(writer, "Valid Name", "123",
                    null, "Invalid user ID", "No recommendations");
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(5, lines.size());
        assertEquals("Invalid123,12345678A,Invalid user name", lines.get(0));
        assertEquals("Valid Name,123,Invalid user ID", lines.get(1));
        assertEquals("Valid Name,12345678A,No recommendations", lines.get(2));
        assertEquals("Invalid123,123,Invalid user name", lines.get(3)); // User Name has priority
        assertEquals("Valid Name,123,Invalid user ID", lines.get(4));   // User ID has priority
    }

    @Test
    void testWriteFirstError_WithAnsiCodes_RemovesCodesFromFirstError() throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("first_error_ansi.txt");
        String userNameError = "\u001B[31mERROR: Invalid user name\u001B[0m";
        String userIdError = "\u001B[33mERROR: Invalid user ID\u001B[0m";

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "Invalid User", "123",
                    userNameError, userIdError, null);
        }

        // Assert
        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("Invalid User,123,ERROR: Invalid user name", lines.get(0));
        assertFalse(lines.get(0).contains("\u001B"));
    }

    // ==================== Integration Test Cases (UPDATED) ====================

    @Test
    void testReadAndWrite_Integration_WorksTogether() throws IOException {
        // Arrange
        Path inputFile = tempDir.resolve("input.txt");
        Path outputFile = tempDir.resolve("output.txt");
        List<String> originalContent = Arrays.asList("Line 1", "Line 2", "Line 3");
        Files.write(inputFile, originalContent);

        // Act
        List<String> readContent = fileHandler.readFile(inputFile.toString());
        
        // Convert the list to a single string to match the new writeFile signature
        String joinedContent = String.join(System.lineSeparator(), readContent);
        FileHandler.writeFile(outputFile, joinedContent);

        // Assert
        List<String> finalContent = Files.readAllLines(outputFile);
        assertEquals(originalContent, finalContent);
    }

    @Test
    void testCompleteRecommendationWorkflow_MixedOutput() throws IOException {
        // Arrange
        Path outputFile = tempDir.resolve("complete_recommendations.txt");
        Set<String> recommendations1 = new LinkedHashSet<>(Arrays.asList("Movie A", "Movie B"));
        Set<String> recommendations2 = new LinkedHashSet<>(Arrays.asList("Movie C"));

        // Act
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {
            // Successful recommendation
            fileHandler.writeRecommendation(writer, "John Smith", "12345678A", recommendations1);

            // Error case
            fileHandler.writeFirstError(writer, "Invalid User", "123",
                    null, "User ID must be exactly 9 characters", null);

            // Another successful recommendation
            fileHandler.writeRecommendation(writer, "Jane Doe", "987654321", recommendations2);
        }

        // Assert
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(5, lines.size());
        assertEquals("John Smith,12345678A", lines.get(0));
        assertEquals("Movie A,Movie B", lines.get(1));
        assertEquals("Invalid User,123,User ID must be exactly 9 characters", lines.get(2));
        assertEquals("Jane Doe,987654321", lines.get(3));
        assertEquals("Movie C", lines.get(4));
    }
}