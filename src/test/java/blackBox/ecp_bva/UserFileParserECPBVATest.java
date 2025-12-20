package blackBox.ecp_bva;

import logic.UserFileParser;
import model.User;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class UserFileParserECPBVATest {

    // BVA is not applicable

    // ECP:
    @Test
    void UP_ECP1_valid_blankLinesSkipped(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("users.txt");

        Files.write(file, List.of(
                "John Smith, 12345678A",
                "TM123, IN456",
                "",
                "Mona, 87654321B",
                "TM123, IN456"
        ));

        UserFileParser parser = new UserFileParser();
        List<User> users = parser.readUsers(file.toString());

        assertEquals(2, users.size());

        assertEquals("John Smith", users.get(0).name());
        assertEquals("12345678A", users.get(0).id());
        assertEquals(Set.of("TM123", "IN456"), users.get(0).watchedMovies());

        assertEquals("Mona", users.get(1).name());
        assertEquals("87654321B", users.get(1).id());
        assertEquals(Set.of("TM123", "IN456"), users.get(1).watchedMovies());
    }

    @Test
    void UP_ECP2_invalid_missingCommaInUserLine(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("users.txt");

        Files.write(file, List.of(
                "John Smith 12345678A",
                "TM123, IN456"
        ));

        UserFileParser parser = new UserFileParser();

        try {
            parser.readUsers(file.toString());
            fail("Exception expected due to wrong user line format (missing comma).");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ERROR: Wrong user line format"));
        }
    }

    @Test
    void UP_ECP3_invalid_tooManyFields(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("users.txt");

        Files.write(file, List.of(
                "John, Smith, 12345678A",
                "TM123, IN456"
        ));

        UserFileParser parser = new UserFileParser();

        try {
            parser.readUsers(file.toString());
            fail("Exception expected due to wrong user line format (too many fields).");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ERROR: Wrong user line format"));
        }
    }

    @Test
    void UP_ECP4_invalid_missingWatchedMoviesLine(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("users.txt");

        Files.write(file, List.of(
                "John Smith, 12345678A"
        ));

        UserFileParser parser = new UserFileParser();

        try {
            parser.readUsers(file.toString());
            fail("NullPointerException expected because watchedMoviesLine is missing.");
        } catch (NullPointerException ex) {
            assertNotNull(ex);
        } catch (Exception ex) {
            fail("Expected NullPointerException, but got: " + ex.getClass().getSimpleName());
        }
    }

    @Test
    void UP_ECP5_valid_nameHasLeadingSpaces(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("users.txt");

        Files.write(file, List.of(
                "     John Smith , 12345678A",
                "TM123"
        ));

        UserFileParser parser = new UserFileParser();
        List<User> users = parser.readUsers(file.toString());

        assertEquals(1, users.size());
        assertEquals("     John Smith ", users.get(0).name());
        assertEquals("12345678A", users.get(0).id());
        assertEquals(Set.of("TM123"), users.get(0).watchedMovies());
    }
}
