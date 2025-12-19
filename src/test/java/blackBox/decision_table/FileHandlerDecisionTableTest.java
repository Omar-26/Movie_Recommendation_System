package blackBox.decision_table;

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
 * Black Box Testing - Decision Table for FileHandler class
 * Tests all combinations of conditions using decision table technique
 */
public class FileHandlerDecisionTableTest {
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

    //------- Decision Table: writeFirstError() - Error Hierarchy -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 | Rule 7 | Rule 8 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | UserName Error (T/F)  |   T    |   T    |   T    |   T    |   F    |   F    |   F    |   F    |
     * | UserId Error (T/F)    |   T    |   T    |   F    |   F    |   T    |   T    |   F    |   F    |
     * | NoRec Error (T/F)     |   T    |   F    |   T    |   F    |   T    |   F    |   T    |   F    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Output                |   N    |   N    |   N    |   N    |   I    |   I    |   R    |   W    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Error Exists
     *   F = False/No Error
     *   N = Write UserName Error (highest priority)
     *   I = Write UserId Error (second priority)
     *   R = Write NoRecommendations Error (lowest priority)
     *   W = Write Nothing (no errors)
     *
     * Interpretation:
     *   Rule 1: All errors present → Write UserName error only
     *   Rule 2: UserName + UserId errors → Write UserName error only
     *   Rule 3: UserName + NoRec errors → Write UserName error only
     *   Rule 4: Only UserName error → Write UserName error
     *   Rule 5: UserId + NoRec errors → Write UserId error only
     *   Rule 6: Only UserId error → Write UserId error
     *   Rule 7: Only NoRec error → Write NoRec error
     *   Rule 8: No errors → Write nothing
     */

    // Rule 1: UserName Error=T, UserId Error=T, NoRec Error=T → N (UserName Error)
    @Test
    public void testWriteFirstError_DT_Rule1_AllErrors() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "BadID",
                "ERROR: Name", "ERROR: ID", "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // N = UserName Error
        assertFalse(lines.get(0).contains("ID"));
        assertFalse(lines.get(0).contains("NoRec"));
    }

    // Rule 2: UserName Error=T, UserId Error=T, NoRec Error=F → N (UserName Error)
    @Test
    public void testWriteFirstError_DT_Rule2_NameAndIdErrors() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "BadID",
                "ERROR: Name", "ERROR: ID", null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // N = UserName Error
    }

    // Rule 3: UserName Error=T, UserId Error=F, NoRec Error=T → N (UserName Error)
    @Test
    public void testWriteFirstError_DT_Rule3_NameAndNoRecErrors() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "GoodID",
                "ERROR: Name", null, "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // N = UserName Error
    }

    // Rule 4: UserName Error=T, UserId Error=F, NoRec Error=F → N (UserName Error)
    @Test
    public void testWriteFirstError_DT_Rule4_OnlyNameError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "BadName", "GoodID",
                "ERROR: Name", null, null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Name")); // N = UserName Error
    }

    // Rule 5: UserName Error=F, UserId Error=T, NoRec Error=T → I (UserId Error)
    @Test
    public void testWriteFirstError_DT_Rule5_IdAndNoRecErrors() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "BadID",
                null, "ERROR: ID", "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("ID")); // I = UserId Error
        assertFalse(lines.get(0).contains("NoRec"));
    }

    // Rule 6: UserName Error=F, UserId Error=T, NoRec Error=F → I (UserId Error)
    @Test
    public void testWriteFirstError_DT_Rule6_OnlyIdError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "BadID",
                null, "ERROR: ID", null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("ID")); // I = UserId Error
    }

    // Rule 7: UserName Error=F, UserId Error=F, NoRec Error=T → R (NoRec Error)
    @Test
    public void testWriteFirstError_DT_Rule7_OnlyNoRecError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "GoodID",
                null, null, "ERROR: NoRec");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("NoRec")); // R = NoRec Error
    }

    // Rule 8: UserName Error=F, UserId Error=F, NoRec Error=F → W (Write Nothing)
    @Test
    public void testWriteFirstError_DT_Rule8_NoErrors() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeFirstError(writer, "GoodName", "GoodID",
                null, null, null);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(0, lines.size()); // W = Write Nothing
    }

    //------- Decision Table: writeRecommendation() -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 |
     * +-----------------------+--------+--------+--------+
     * | Valid Name (T/F)      |   T    |   T    |   T    |
     * | Valid ID (T/F)        |   T    |   T    |   T    |
     * | Has Recs (T/F)        |   T    |   F    |   T    |
     * | Single Rec (T/F)      |   F    |   -    |   T    |
     * +-----------------------+--------+--------+--------+
     * | Output                |   M    |   E    |   S    |
     * +-----------------------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Valid/Has
     *   F = False/Invalid/Empty
     *   - = Don't Care
     *   M = Write Multiple Recommendations
     *   E = Write Empty Line
     *   S = Write Single Recommendation
     *
     * Interpretation:
     *   Rule 1: Valid name, valid ID, multiple recommendations → Write all
     *   Rule 2: Valid name, valid ID, empty recommendations → Write empty line
     *   Rule 3: Valid name, valid ID, single recommendation → Write single movie
     */

    // Rule 1: Valid Name=T, Valid ID=T, Has Recs=T, Single Rec=F → M (Multiple)
    @Test
    public void testWriteRecommendation_DT_Rule1_AllValid() throws IOException {
        Set<String> recs = new HashSet<>();
        recs.add("Movie1");
        recs.add("Movie2");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "John Doe", "123456789", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("John Doe,123456789", lines.get(0));
        assertTrue(lines.get(1).contains("Movie1")); // M = Multiple Recommendations
        assertTrue(lines.get(1).contains("Movie2"));
    }

    // Rule 2: Valid Name=T, Valid ID=T, Has Recs=F, Single Rec=- → E (Empty)
    @Test
    public void testWriteRecommendation_DT_Rule2_EmptyRecs() throws IOException {
        Set<String> recs = new HashSet<>();
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "John Doe", "123456789", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("John Doe,123456789", lines.get(0));
        assertEquals("", lines.get(1)); // E = Empty Line
    }

    // Rule 3: Valid Name=T, Valid ID=T, Has Recs=T, Single Rec=T → S (Single)
    @Test
    public void testWriteRecommendation_DT_Rule3_SingleRec() throws IOException {
        Set<String> recs = new HashSet<>();
        recs.add("SingleMovie");
        
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeRecommendation(writer, "Jane", "987654321", recs);
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(2, lines.size());
        assertEquals("Jane,987654321", lines.get(0));
        assertEquals("SingleMovie", lines.get(1)); // S = Single Recommendation
    }

    //------- Decision Table: writeError() with ANSI codes -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 |
     * +-----------------------+--------+--------+--------+--------+
     * | Has ANSI (T/F)        |   T    |   F    |   T    |   F    |
     * | Valid Name (T/F)      |   T    |   T    |   T    |   T    |
     * | Valid ID (T/F)        |   T    |   T    |   T    |   T    |
     * | Error Empty (T/F)     |   F    |   F    |   F    |   T    |
     * +-----------------------+--------+--------+--------+--------+
     * | Output                |   A    |   N    |   A    |   E    |
     * +-----------------------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Has/Valid
     *   F = False/No/Empty
     *   A = Write with ANSI Removed
     *   N = Write as-is (No ANSI)
     *   E = Write Empty Error
     *
     * Interpretation:
     *   Rule 1: Has ANSI, valid name/ID, error not empty → Remove ANSI and write
     *   Rule 2: No ANSI, valid name/ID, error not empty → Write as-is
     *   Rule 3: Has ANSI, special chars in name, valid ID → Remove ANSI and write
     *   Rule 4: No ANSI, valid name/ID, error empty → Write empty error
     */

    // Rule 1: Has ANSI=T, Valid Name=T, Valid ID=T, Error Empty=F → A (ANSI Removed)
    @Test
    public void testWriteError_DT_Rule1_WithANSI() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "John", "123", "\u001B[31mERROR\u001B[0m");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("John,123,ERROR", lines.get(0)); // A = ANSI Removed
    }

    // Rule 2: Has ANSI=F, Valid Name=T, Valid ID=T, Error Empty=F → N (No ANSI)
    @Test
    public void testWriteError_DT_Rule2_NoANSI() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "John", "123", "ERROR");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("John,123,ERROR", lines.get(0)); // N = No ANSI (as-is)
    }

    // Rule 3: Has ANSI=T, Valid Name=T (special chars), Valid ID=T, Error Empty=F → A
    @Test
    public void testWriteError_DT_Rule3_SpecialCharsInName() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "John,Smith", "123", "\u001B[31mERROR\u001B[0m");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("John,Smith"));
        assertTrue(lines.get(0).contains("ERROR")); // A = ANSI Removed
    }

    // Rule 4: Has ANSI=F, Valid Name=T, Valid ID=T, Error Empty=T → E (Empty Error)
    @Test
    public void testWriteError_DT_Rule4_EmptyError() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
            fileHandler.writeError(writer, "John", "123", "");
        }
        
        List<String> lines = Files.readAllLines(outputFilePath);
        assertEquals(1, lines.size());
        assertEquals("John,123,", lines.get(0)); // E = Empty Error
    }
}
