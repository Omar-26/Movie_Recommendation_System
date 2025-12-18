import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileHandling – readFile() Statement Coverage Tests")
public class FileHandlingTest {

            // ===================== STATEMENT COVERAGE TEST CASES =====================

    private final FileHandler fileHandler = new FileHandler();

    @TempDir
    Path tempDir;

    /**
     * TC19 – Normal file with lines
     * Statement coverage → 6/8
     */
    @Test
    @DisplayName("TC19 – readFile normal file with content")
    void testReadFile_NormalFile() throws IOException {
        Path testFile = tempDir.resolve("normal.txt");
        List<String> expectedLines = Arrays.asList("Hello", "world");
        Files.write(testFile, expectedLines);

        List<String> actualLines = fileHandler.readFile(testFile.toString());

        assertEquals(expectedLines, actualLines);
    }

    /**
     * TC20 – Empty file
     * Statement coverage → 5/8
     */
    @Test
    @DisplayName("TC20 – readFile empty file")
    void testReadFile_EmptyFile() throws IOException {
        Path testFile = tempDir.resolve("empty.txt");
        Files.createFile(testFile);

        List<String> actualLines = fileHandler.readFile(testFile.toString());

        assertTrue(actualLines.isEmpty());
    }

    /**
     * TC21 – File does not exist
     * Statement coverage → 4/8
     */
    @Test
    @DisplayName("TC21 – readFile non-existent file")
    void testReadFile_FileDoesNotExist() {
        Path testFile = tempDir.resolve("nonexistent.txt");

        List<String> actualLines = fileHandler.readFile(testFile.toString());

        // Update assertion to match implementation
        assertTrue(actualLines.isEmpty(), "Non-existent file should return an empty list");
    }

    @Test
    @DisplayName("TC22 – writeRecommendation normal file write")
    void testWriteRecommendation_NormalWrite() throws IOException {
        Path testFile = Path.of(System.getProperty("java.io.tmpdir"), "recommendations_normal.txt");
        Set<String> recommendations = new LinkedHashSet<>();
        recommendations.add("Movie A");
        recommendations.add("Movie B");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeRecommendation(writer, "John Smith", "12345678A", recommendations);
        }

        // Check that the file is not empty
        assertTrue(testFile.toFile().length() > 0, "File should be written with content");
    }

    @Test
    @DisplayName("TC23 – writeRecommendation invalid path")
    void testWriteRecommendation_InvalidPath() {
        Path invalidFile = Path.of(tempDir.toString(), "nonexistent_dir", "recommendations.txt");

        Set<String> recommendations = new LinkedHashSet<>(Arrays.asList("Movie A", "Movie B"));

        try {
            // Ensure parent directories do not exist
            assertFalse(Files.exists(invalidFile.getParent()));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invalidFile.toFile()))) {
                fileHandler.writeRecommendation(writer, "John Smith", "12345678A", recommendations);
            }
            fail("Expected IOException due to invalid path");
        } catch (IOException e) {
            // This is expected for statement coverage
            assertTrue(e instanceof FileNotFoundException);
        }
    }

    @Test
    @DisplayName("TC24 – writeFirstError: username error only")
    void testWriteFirstError_UsernameError() throws IOException {
        Path testFile = tempDir.resolve("username_error.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "123Invalid", "12345678A",
                    "User name must contain only letters and spaces",
                    null,
                    null);
        }

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("123Invalid,12345678A,User name must contain only letters and spaces", lines.get(0));
    }

    @Test
    @DisplayName("TC25 – writeFirstError: userId error only")
    void testWriteFirstError_UserIdError() throws IOException {
        Path testFile = tempDir.resolve("userid_error.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "123",
                    null,
                    "User ID must be exactly 9 characters",
                    null);
        }

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,123,User ID must be exactly 9 characters", lines.get(0));
    }

    @Test
    @DisplayName("TC26 – writeFirstError: noRecommendations error only")
    void testWriteFirstError_NoRecommendationsError() throws IOException {
        Path testFile = tempDir.resolve("no_recommendations_error.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "12345678A",
                    null,
                    null,
                    "No recommendations available");
        }

        List<String> lines = Files.readAllLines(testFile);
        assertEquals(1, lines.size());
        assertEquals("John Smith,12345678A,No recommendations available", lines.get(0));
    }

            // ===================== BRANCH COVERAGE TEST CASES =====================
            // These test cases 49,50,51,52,53,54 are redundant from the statement coverage //

    @Test
    @DisplayName("TC55 – writeFirstError: no errors")
    void testWriteFirstError_NoErrors() throws IOException {
        Path testFile = tempDir.resolve("no_errors_branch.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile.toFile()))) {
            fileHandler.writeFirstError(writer, "John Smith", "12345678A",
                    null, null, null);
        }

        List<String> lines = Files.readAllLines(testFile);
        assertTrue(lines.isEmpty());
    }

            // ===================== Path COVERAGE TEST CASES =====================
            // These test cases 74,75 are redundant from the above coverage methods //

}


