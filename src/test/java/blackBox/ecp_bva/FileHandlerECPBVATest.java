package blackBox.ecp_bva;

import logic.FileHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerECPBVATest {

    /*-----------------------------------------------------------
     * 1) readFile
     *----------------------------------------------------------*/

    @Test
    void TC_FH1_readFile_valid_fileExists(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("input.txt");
        Files.write(file, List.of("Line 1", "Line 2", "Line 3"));

        FileHandler fh = new FileHandler();
        List<String> lines = fh.readFile(file.toString());

        assertEquals(3, lines.size());
        assertEquals("Line 1", lines.get(0));
        assertEquals("Line 2", lines.get(1));
        assertEquals("Line 3", lines.get(2));
    }

    @Test
    void TC_FH2_readFile_invalid_fileNotExist() {
        FileHandler fh = new FileHandler();
        List<String> lines = fh.readFile("this_file_does_not_exist_12345.txt");

        // FileHandler catches IOException and returns empty list
        assertTrue(lines.isEmpty());
    }

    /*-----------------------------------------------------------
     * 2) writeFile
     *----------------------------------------------------------*/

    @Test
    void TC_FH3_writeFile_valid_pathValid(@TempDir Path tempDir) throws IOException {
        Path out = tempDir.resolve("out.txt");

        // writeFile writes ONE String + then writer.newLine()
        FileHandler.writeFile(out, "Line 1" + System.lineSeparator() + "Line 2");

        List<String> written = Files.readAllLines(out);

        // because writeFile adds an extra newline at the end,
        // the file may include a trailing empty line; only check first two.
        assertTrue(written.size() >= 2);
        assertEquals("Line 1", written.get(0));
        assertEquals("Line 2", written.get(1));
    }

    @Test
    void TC_FH4_writeFile_invalid_directoryNotExist(@TempDir Path tempDir) {
        Path missingDir = tempDir.resolve("missing_dir"); // not created
        Path out = missingDir.resolve("out.txt");

        FileHandler.writeFile(out, "Line 1");

        // writeFile catches exception, so file should not exist
        assertFalse(Files.exists(out));
    }

    /*-----------------------------------------------------------
     * 3) writeRecommendation
     *----------------------------------------------------------*/

    @Test
    void TC_FH5_writeRecommendation_multipleTitles() throws IOException {
        FileHandler fh = new FileHandler();

        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);

        // deterministic order
        Set<String> recs = new LinkedHashSet<>();
        recs.add("Movie A");
        recs.add("Movie B");

        fh.writeRecommendation(writer, "UserName", "12345678A", recs);
        writer.flush();

        String nl = System.lineSeparator();
        String expected =
                "UserName,12345678A" + nl +
                        "Movie A,Movie B" + nl;

        assertEquals(expected, sw.toString());
    }

    @Test
    void TC_FH6_writeRecommendation_singleTitle() throws IOException {
        FileHandler fh = new FileHandler();

        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);

        Set<String> recs = new LinkedHashSet<>();
        recs.add("Movie A");

        fh.writeRecommendation(writer, "UserName", "12345678A", recs);
        writer.flush();

        String nl = System.lineSeparator();
        String expected =
                "UserName,12345678A" + nl +
                        "Movie A" + nl;

        assertEquals(expected, sw.toString());
    }

    /*-----------------------------------------------------------
     * 4) writeFirstError
     *----------------------------------------------------------*/

    @Test
    void TC_FH7_writeFirstError_nameErrorPriority() throws IOException {
        FileHandler fh = new FileHandler();

        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);

        fh.writeFirstError(writer, "UserName", "12345678A",
                "Error 1",   // userNameError (highest priority)
                "Error 2",
                null
        );
        writer.flush();

        String expected = "UserName,12345678A,Error 1" + System.lineSeparator();
        assertEquals(expected, sw.toString());
    }

    @Test
    void TC_FH8_writeFirstError_idErrorWhenNameNull() throws IOException {
        FileHandler fh = new FileHandler();

        StringWriter sw = new StringWriter();
        BufferedWriter writer = new BufferedWriter(sw);

        fh.writeFirstError(writer, "UserName", "12345678A",
                null,
                "Error 2",
                null
        );
        writer.flush();

        String expected = "UserName,12345678A,Error 2" + System.lineSeparator();
        assertEquals(expected, sw.toString());
    }
}
