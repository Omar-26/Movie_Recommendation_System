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
}
