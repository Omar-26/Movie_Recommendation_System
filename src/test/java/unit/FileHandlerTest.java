import logic.FileHandler;
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

}