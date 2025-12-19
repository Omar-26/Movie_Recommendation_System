package whiteBox;

import core.*;
import model.User;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User File Parser – Statement Coverage Tests")
public class UserFileTest {

    private static Path tempDir;
    private UserFileParser userParser;

    // ===================== SETUP & CLEANUP =====================

    @BeforeAll
    static void setupTempDir() throws IOException {
        tempDir = Files.createTempDirectory("userParserTest");
    }

    @BeforeEach
    void setup() {
        userParser = new UserFileParser();
    }

    @AfterAll
    static void cleanup() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    // ===================== STATEMENT COVERAGE TEST CASES =====================

    /**
     * TC1: Normal valid input
     * User_name,user_id
     * Movie1_id,movie2_id
     *
     * Statement Coverage: 13/15
     */
    @Test
    @DisplayName("TC1 – Statement coverage: normal valid input")
    public void testStatementCoverage_TC1_NormalValidInput() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe,111111111
                        TC379,I123
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("111111111", users.get(0).id());
        assertEquals(2, users.get(0).watchedMovies().size());
    }

    /**
     * TC2: File with a blank line
     * // Blank line //
     * User_name,user_id
     * Movie1_id,movie2_id
     *
     * Statement Coverage: 14/15
     */
    @Test
    @DisplayName("TC2 – Statement coverage: file with blank line")
    public void testStatementCoverage_TC2_BlankLine() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """

                        John Doe,111111111
                        TC379,I123
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals(2, users.get(0).watchedMovies().size());
    }

    /**
     * TC3: Invalid user line
     * User_name/user_id
     * Movie1_id,movie2_id
     *
     * Statement Coverage: 14/15
     */
    @Test
    @DisplayName("TC3 – Statement coverage: invalid user line")
    public void testStatementCoverage_TC3_InvalidUserLine() {
        Path userFile = tempDir.resolve("users.txt");

        Exception exception = assertThrows(Exception.class, () -> {
            Files.writeString(userFile,
                    """
                            JohnDoe/111111111
                            TC379,I123
                            """);
            userParser.readUsers(userFile.toString());
        });

        assertTrue(
                exception.getMessage().toLowerCase().contains("format") ||
                        exception.getMessage().toLowerCase().contains("wrong"));
    }

    // ===================== BRANCH COVERAGE TEST CASES ===================== //

    // Test cases 27,28,29 are redundant from the statement coverage //

    /**
     * TC30 – Branch coverage: empty file
     * while = false → loop never executes
     */
    @Test
    @DisplayName("TC30 – Branch coverage: empty file")
    public void testBranchCoverage_TC30_EmptyFile() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile, "");

        List<User> users = userParser.readUsers(userFile.toString());

        assertTrue(users.isEmpty());
    }
    // ===================== PATH COVERAGE TEST CASES ===================== //
    // The path coverage test cases 56,57,58 are also redundant //

}
