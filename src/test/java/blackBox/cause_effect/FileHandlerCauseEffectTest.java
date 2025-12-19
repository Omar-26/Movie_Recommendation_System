package blackBox.cause_effect;

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

    //------- Cause-Effect: writeFirstError() -------//
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

    // Cause C2 → Effect E2 (userId error causes userId error written)
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

    // Cause C3 → Effect E3 (noRec error causes noRec error written)
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
}