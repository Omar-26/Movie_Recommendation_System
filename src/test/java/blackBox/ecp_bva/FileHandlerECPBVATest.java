import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - ECP & BVA for FileHandler class
 * Tests equivalence class partitioning and boundary value analysis
 */
public class FileHandlerECPBVATest {
    private FileHandler fileHandler;
    private Path testFilePath;
    private Path outputFilePath;

    @BeforeEach
    void setUp() throws IOException {
        fileHandler = new FileHandler();
        testFilePath = Files.createTempFile("test", ".txt");
        outputFilePath = Files.createTempFile("output", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
        Files.deleteIfExists(outputFilePath);
    }

    //------- readFile() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal file with content
    @Test
    public void testReadFile_ECP_ValidFileWithContent() throws IOException {
        Files.writeString(testFilePath, "Line 1\nLine 2\nLine 3");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertEquals(3, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    // BVA: Boundary - empty file
    @Test
    public void testReadFile_BVA_EmptyFile() throws IOException {
        Files.writeString(testFilePath, "");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertTrue(lines.isEmpty());
    }

    // BVA: Boundary - single line file
    @Test
    public void testReadFile_BVA_SingleLine() throws IOException {
        Files.writeString(testFilePath, "Single Line");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertEquals(1, lines.size());
        assertEquals("Single Line", lines.get(0));
    }

    // ECP: Valid equivalence class - file with whitespace
    @Test
    public void testReadFile_ECP_WithWhitespace() throws IOException {
        Files.writeString(testFilePath, "  Line with spaces  \n\tLine with tab\t");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertEquals(2, lines.size());
        assertEquals("Line with spaces", lines.get(0)); // Trimmed
        assertEquals("Line with tab", lines.get(1)); // Trimmed
    }

    // ECP: Valid equivalence class - file with blank lines
    @Test
    public void testReadFile_ECP_WithBlankLines() throws IOException {
        Files.writeString(testFilePath, "Line 1\n\nLine 3\n\n\nLine 6");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        // Blank lines are read as empty strings after trim
        assertEquals(6, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    // ECP: Invalid equivalence class - non-existent file
    @Test
    public void testReadFile_ECP_NonExistentFile() {
        List<String> lines = fileHandler.readFile("non_existent_file.txt");
        
        // Should return empty list on error
        assertTrue(lines.isEmpty());
    }

    // BVA: Boundary - large file
    @Test
    public void testReadFile_BVA_LargeFile() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            content.append("Line ").append(i).append("\n");
        }
        Files.writeString(testFilePath, content.toString());
        
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertEquals(1000, lines.size());
        assertEquals("Line 0", lines.get(0));
        assertEquals("Line 999", lines.get(999));
    }

    // ECP: Valid equivalence class - file with special characters
    @Test
    public void testReadFile_ECP_SpecialCharacters() throws IOException {
        Files.writeString(testFilePath, "Line with @#$%\nLine with émojis 😀");
        List<String> lines = fileHandler.readFile(testFilePath.toString());
        
        assertEquals(2, lines.size());
        assertEquals("Line with @#$%", lines.get(0));
        assertTrue(lines.get(1).contains("émojis"));
    }

    //------- removeAnsiCodes() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - text with ANSI codes
    @Test
    public void testRemoveAnsiCodes_ECP_WithAnsiCodes() {
        String text = "\u001B[31mRed Text\u001B[0m";
        String result = FileHandler.removeAnsiCodes(text);
        
        assertEquals("Red Text", result);
    }

    // ECP: Valid equivalence class - text without ANSI codes
    @Test
    public void testRemoveAnsiCodes_ECP_WithoutAnsiCodes() {
        String text = "Plain Text";
        String result = FileHandler.removeAnsiCodes(text);
        
        assertEquals("Plain Text", result);
    }

    // BVA: Boundary - null input
    @Test
    public void testRemoveAnsiCodes_BVA_Null() {
        String result = FileHandler.removeAnsiCodes(null);
        assertEquals("", result);
    }

    // BVA: Boundary - empty string
    @Test
    public void testRemoveAnsiCodes_BVA_Empty() {
        String result = FileHandler.removeAnsiCodes("");
        assertEquals("", result);
    }

    // ECP: Valid equivalence class - multiple ANSI codes
    @Test
    public void testRemoveAnsiCodes_ECP_MultipleAnsiCodes() {
        String text = "\u001B[31mRed\u001B[0m and \u001B[32mGreen\u001B[0m";
        String result = FileHandler.removeAnsiCodes(text);
        
        assertEquals("Red and Green", result);
    }

    // BVA: Boundary - only ANSI codes
    @Test
    public void testRemoveAnsiCodes_BVA_OnlyAnsiCodes() {
        String text = "\u001B[31m\u001B[0m";
        String result = FileHandler.removeAnsiCodes(text);
        
        assertEquals("", result);
    }

    //------- writeRecommendation() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal recommendation
    @Test
    public void testWriteRecommendation_ECP_Valid() throws IOException {
        Set<String> recommendations = new HashSet<>();
        recommendations.add("Movie1");
        recommendations.add("Movie2");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "John Doe", "123456789", recommendations);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("John Doe,123456789", lines.get(0));
        assertTrue(lines.get(1).contains("Movie1"));
        assertTrue(lines.get(1).contains("Movie2"));
    }

    // BVA: Boundary - single recommendation
    @Test
    public void testWriteRecommendation_BVA_SingleRecommendation() throws IOException {
        Set<String> recommendations = new HashSet<>();
        recommendations.add("SingleMovie");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "Jane Smith", "987654321", recommendations);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("Jane Smith,987654321", lines.get(0));
        assertEquals("SingleMovie", lines.get(1));
    }

    // BVA: Boundary - empty recommendations
    @Test
    public void testWriteRecommendation_BVA_EmptyRecommendations() throws IOException {
        Set<String> recommendations = new HashSet<>();
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "Empty User", "111111111", recommendations);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("Empty User,111111111", lines.get(0));
        assertEquals("", lines.get(1)); // Empty line for recommendations
    }

    // ECP: Valid equivalence class - many recommendations
    @Test
    public void testWriteRecommendation_ECP_ManyRecommendations() throws IOException {
        Set<String> recommendations = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            recommendations.add("Movie" + i);
        }
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "User", "123456789", recommendations);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        // Second line should contain all movie titles
        String[] movies = lines.get(1).split(",");
        assertEquals(10, movies.length);
    }

    //------- writeError() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal error
    @Test
    public void testWriteError_ECP_Valid() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "John", "123", "ERROR: Test error");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("John,123,ERROR: Test error", lines.get(0));
    }

    // ECP: Valid equivalence class - error with ANSI codes
    @Test
    public void testWriteError_ECP_WithAnsiCodes() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "Jane", "456", "\u001B[31mERROR: Red error\u001B[0m");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("Jane,456,ERROR: Red error", lines.get(0)); // ANSI codes removed
    }

    // BVA: Boundary - empty error message
    @Test
    public void testWriteError_BVA_EmptyErrorMessage() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "User", "789", "");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("User,789,", lines.get(0));
    }

    //------- writeFirstError() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - userName error (highest priority)
    @Test
    public void testWriteFirstError_ECP_UserNameError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "Bad Name", "123", 
                "ERROR: User Name error", "ERROR: User ID error", "ERROR: No recommendations");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("User Name error"));
        assertFalse(lines.get(0).contains("User ID error"));
    }

    // ECP: Valid equivalence class - userId error (second priority)
    @Test
    public void testWriteFirstError_ECP_UserIdError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "Good Name", "BadID", 
                null, "ERROR: User ID error", "ERROR: No recommendations");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("User ID error"));
        assertFalse(lines.get(0).contains("No recommendations"));
    }

    // ECP: Valid equivalence class - no recommendations error (lowest priority)
    @Test
    public void testWriteFirstError_ECP_NoRecommendationsError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "Good Name", "123456789", 
                null, null, "ERROR: No recommendations");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("No recommendations"));
    }

    // BVA: Boundary - all errors null
    @Test
    public void testWriteFirstError_BVA_AllErrorsNull() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "User", "123", null, null, null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        // No error written
        assertEquals(0, lines.size());
    }

    // ECP: Valid equivalence class - multiple errors (priority test)
    @Test
    public void testWriteFirstError_ECP_AllErrorsPresent() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, " BadName", "BadID", 
                "ERROR: Name", "ERROR: ID", "ERROR: Recommendations");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        // Should only write userName error (highest priority)
        assertTrue(lines.get(0).contains("Name"));
        assertFalse(lines.get(0).contains("ID"));
        assertFalse(lines.get(0).contains("Recommendations"));
    }
}
