import logic.FileHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - Cause-Effect Graph for FileHandler class
 * Tests cause-effect relationships and constraints
 */
public class FileHandlerCauseEffectTest {
    private FileHandler fileHandler;
    private Path outputFilePath;

    @BeforeEach
    void setUp() throws IOException {
        fileHandler = new FileHandler();
        outputFilePath = Files.createTempFile("output", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(outputFilePath);
    }

    //------- Cause-Effect: writeFirstError() - Error Hierarchy -------//
    // Causes: C1=UserName error exists, C2=UserId error exists, C3=NoRecommendations error exists
    // Effects: E1=Write userName error, E2=Write userId error, E3=Write noRec error, E4=Write nothing
    // Constraints: If C1 then E1 (skip C2, C3), If C2 then E2 (skip C3)

    // Cause C1 → Effect E1 (userName error causes userName error written)
    @Test
    public void testWriteFirstError_CE_UserNameErrorCausesUserNameWritten() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "123",
                "ERROR: Name", null, null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // Effect: UserName error written
    }

    // Cause C2 (without C1) → Effect E2 (userId error causes userId error written)
    @Test
    public void testWriteFirstError_CE_UserIdErrorCausesUserIdWritten() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "BadID",
                null, "ERROR: ID", null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("ID")); // Effect: UserId error written
    }

    // Cause C3 (without C1, C2) → Effect E3 (noRec error causes noRec error written)
    @Test
    public void testWriteFirstError_CE_NoRecErrorCausesNoRecWritten() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "GoodID",
                null, null, "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("NoRec")); // Effect: NoRec error written
    }

    // No causes → Effect E4 (no errors cause nothing written)
    @Test
    public void testWriteFirstError_CE_NoCausesCausesNothingWritten() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "GoodID",
                null, null, null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(0, lines.size()); // Effect: Nothing written
    }

    // Constraint: C1 takes precedence over C2 and C3
    @Test
    public void testWriteFirstError_CE_UserNameTakesPrecedence() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "BadID",
                "ERROR: Name", "ERROR: ID", "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // Only userName error written
        assertFalse(lines.get(0).contains("ID"));
        assertFalse(lines.get(0).contains("NoRec"));
    }

    // Constraint: C2 takes precedence over C3
    @Test
    public void testWriteFirstError_CE_UserIdTakesPrecedenceOverNoRec() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "BadID",
                null, "ERROR: ID", "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("ID")); // Only userId error written
        assertFalse(lines.get(0).contains("NoRec"));
    }

    //------- Cause-Effect: writeError() with ANSI Codes -------//
    // Causes: C1=Error message has ANSI codes
    // Effects: E1=ANSI codes removed, E2=Error written as-is
    // Constraints: If C1 then E1

    // Cause C1 → Effect E1 (ANSI codes cause removal)
    @Test
    public void testWriteError_CE_AnsiCodesCausesRemoval() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "User", "123", "\u001B[31mERROR\u001B[0m");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("User,123,ERROR", lines.get(0)); // Effect: ANSI removed
        assertFalse(lines.get(0).contains("\u001B")); // No ANSI codes
    }

    // No cause C1 → Effect E2 (no ANSI codes, written as-is)
    @Test
    public void testWriteError_CE_NoAnsiCausesAsIs() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "User", "123", "ERROR");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("User,123,ERROR", lines.get(0)); // Effect: Written as-is
    }

    // Multiple ANSI codes cause all removed
    @Test
    public void testWriteError_CE_MultipleAnsiCausesAllRemoved() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "User", "123", 
                "\u001B[31mRed\u001B[0m and \u001B[32mGreen\u001B[0m");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("User,123,Red and Green", lines.get(0)); // All ANSI removed
    }

    //------- Cause-Effect: writeRecommendation() -------//
    // Causes: C1=Recommendations set is empty, C2=Recommendations set has data
    // Effects: E1=Write empty recommendations line, E2=Write recommendations
    // Constraints: Always write user info line first

    // Cause C1 → Effect E1 (empty recommendations cause empty line)
    @Test
    public void testWriteRecommendation_CE_EmptyRecsCausesEmptyLine() throws IOException {
        Set<String> recs = new HashSet<>();
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "User", "123", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("User,123", lines.get(0)); // User info always written
        assertEquals("", lines.get(1)); // Effect: Empty recommendations line
    }

    // Cause C2 → Effect E2 (recommendations cause recommendations written)
    @Test
    public void testWriteRecommendation_CE_HasRecsCausesRecsWritten() throws IOException {
        Set<String> recs = new HashSet<>();
        recs.add("Movie1");
        recs.add("Movie2");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "User", "123", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("User,123", lines.get(0)); // User info always written
        assertTrue(lines.get(1).contains("Movie1")); // Effect: Recommendations written
        assertTrue(lines.get(1).contains("Movie2"));
    }

    // Constraint: User info always written first
    @Test
    public void testWriteRecommendation_CE_UserInfoAlwaysFirst() throws IOException {
        Set<String> recs = new HashSet<>();
        recs.add("Movie");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "TestUser", "999", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("TestUser,999", lines.get(0)); // Always first
        assertEquals("Movie", lines.get(1)); // Recommendations second
    }

    //------- Cause-Effect: removeAnsiCodes() -------//
    // Causes: C1=Text is null, C2=Text has ANSI codes, C3=Text is empty
    // Effects: E1=Return empty string, E2=Return text with ANSI removed, E3=Return text as-is
    // Constraints: If C1 then E1 (skip other processing)

    // Cause C1 → Effect E1 (null causes empty string)
    @Test
    public void testRemoveAnsi_CE_NullCausesEmpty() {
        String result = FileHandler.removeAnsiCodes(null);
        assertEquals("", result); // Effect: Empty string
    }

    // Cause C3 → Effect E3 (empty causes empty)
    @Test
    public void testRemoveAnsi_CE_EmptyCausesEmpty() {
        String result = FileHandler.removeAnsiCodes("");
        assertEquals("", result); // Effect: Empty string
    }

    // Cause C2 → Effect E2 (ANSI codes cause removal)
    @Test
    public void testRemoveAnsi_CE_AnsiCausesRemoval() {
        String result = FileHandler.removeAnsiCodes("\u001B[31mText\u001B[0m");
        assertEquals("Text", result); // Effect: ANSI removed
    }

    // No cause C2 → Effect E3 (no ANSI, return as-is)
    @Test
    public void testRemoveAnsi_CE_NoAnsiCausesAsIs() {
        String result = FileHandler.removeAnsiCodes("Plain Text");
        assertEquals("Plain Text", result); // Effect: As-is
    }

    // Constraint: Null checked first
    @Test
    public void testRemoveAnsi_CE_NullTakesPrecedence() {
        String result = FileHandler.removeAnsiCodes(null);
        assertEquals("", result); // Null handling takes precedence
    }

    //------- Cause-Effect: Error Writing Independence -------//
    // Test that different error types are independent

    // Writing one error doesn't affect ability to write another
    @Test
    public void testCE_ErrorWritingIndependent() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "User1", "123", "Error1");
            fileHandler.writeError(writer, "User2", "456", "Error2");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("Error1"));
        assertTrue(lines.get(1).contains("Error2")); // Both written independently
    }

    // writeFirstError and writeError are independent
    @Test
    public void testCE_FirstErrorAndErrorIndependent() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "User1", "123", "Error1", null, null);
            fileHandler.writeError(writer, "User2", "456", "Error2");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("Error1"));
        assertTrue(lines.get(1).contains("Error2")); // Independent operations
    }
}
