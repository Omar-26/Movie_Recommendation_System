// ===================== Abdulrahman Ahmed Saeed =====================
// =====================        2100811          =====================

import logic.UserFileParser;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api. Assertions.*;
import model.User;

@DisplayName("User File Parser Tests")
public class UserFileParserTest {

    private static Path tempDir;
    private UserFileParser userParser;

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
                            e. printStackTrace();
                        }
                    });
        }
    }

    // ===================== VALID FORMAT TESTS =====================

    @Test
    @DisplayName("Test reading valid user with single name")
    public void testReadUsers_ValidSingleName() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John,111111111
                TC379
                """);

        List<User> users = userParser. readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John", users.get(0). name());
        assertEquals("111111111", users.get(0). id());
        assertNotNull(users.get(0). watchedMovies());
        assertTrue(users.get(0).watchedMovies().contains("TC379"));
    }

    @Test
    @DisplayName("Test reading valid user with two-part name")
    public void testReadUsers_ValidTwoPartName() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John Doe,111111111
                TC379
                """);

        List<User> users = userParser. readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("111111111", users.get(0).id());
        assertTrue(users.get(0).watchedMovies().contains("TC379"));
    }

    @Test
    @DisplayName("Test reading multiple valid users")
    public void testReadUsers_MultipleValidUsers() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John Doe,111111111
                TC379
                
                Jane Smith,222222222
                I123
                
                Valid User,333333333
                SP123
                """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(3, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("Jane Smith", users.get(1).name());
        assertEquals("Valid User", users. get(2).name());
        assertTrue(users.get(0).watchedMovies().contains("TC379"));
        assertTrue(users.get(1).watchedMovies(). contains("I123"));
        assertTrue(users.get(2).watchedMovies().contains("SP123"));
    }

    @Test
    @DisplayName("Test reading user with 9-digit userId")
    public void testReadUsers_NineDigitUserId() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                Valid User,333333333
                SP123
                """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("333333333", users.get(0).id());
    }

    @Test
    @DisplayName("Test reading user with 10th position letter")
    public void testReadUsers_UserIdWithTenthPositionLetter() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                Valid User,123456789A
                SP123
                """);

        List<User> users = userParser.readUsers(userFile. toString());

        assertEquals(1, users.size());
        assertEquals("123456789A", users.get(0).id());
    }

    @Test
    @DisplayName("Test reading user with multiple watched movies")
    public void testReadUsers_MultipleWatchedMovies() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files. writeString(userFile,
                """
                John Doe,111111111
                TC379, I123, SP123
                """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals(3, users.get(0). watchedMovies().size());
        assertTrue(users.get(0).watchedMovies().contains("TC379"));
        assertTrue(users.get(0).watchedMovies().contains("I123"));
        assertTrue(users.get(0).watchedMovies().contains("SP123"));
    }

    @Test
    @DisplayName("Test reading user name that starts with space")
    public void testReadUsers_NameStartsWithSpace() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                 John Doe,111111111
                TC379
                """);

        List<User> users = userParser. readUsers(userFile.toString());

        assertEquals(1, users. size());
        assertEquals(" John Doe", users.get(0).name());
        assertTrue(users.get(0).name().startsWith(" "),
                "Name should start with a space to allow validation");
        assertEquals("111111111", users. get(0).id());
        assertTrue(users.get(0).watchedMovies().contains("TC379"));
    }

    // ===================== EMPTY FILE TESTS =====================

    @Test
    @DisplayName("Test reading completely empty file")
    public void testReadUsers_EmptyFile() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile, "");

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(0, users.size());
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Test reading file with only blank lines")
    public void testReadUsers_OnlyBlankLines() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile, "\n\n\n\n\n");

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(0, users.size());
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Test reading file with only whitespace")
    public void testReadUsers_OnlyWhitespace() throws Exception {
        Path userFile = tempDir.resolve("users. txt");
        Files.writeString(userFile, "   \n  \n    \n");

        List<User> users = userParser. readUsers(userFile.toString());

        assertEquals(0, users.size());
        assertTrue(users.isEmpty());
    }

    // ===================== MISSING LINES TESTS =====================

    @Test
    @DisplayName("Test missing movie ID line for single user")
    public void testReadUsers_MissingMovieIdLine() throws Exception {
        Path userFile = tempDir. resolve("users.txt");
        Files.writeString(userFile,
                "John Doe,111111111\n");

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Movie") ||
                exception.getMessage().contains("missing") ||
                exception.getMessage().contains("John Doe"));
    }

    @Test
    @DisplayName("Test missing movie ID line for last user in multiple users")
    public void testReadUsers_LastUserMissingMovieId() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John Doe,111111111
                TC379
                
                Jane Smith,222222222
                """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser. readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Movie") ||
                exception. getMessage().contains("missing") ||
                exception.getMessage().contains("Jane Smith"));
    }

    // ===================== WRONG FORMAT TESTS =====================

    @Test
    @DisplayName("Test wrong format - missing comma separator")
    public void testReadUsers_WrongFormatMissingComma() throws Exception {
        Path userFile = tempDir.resolve("users. txt");
        Files.writeString(userFile,
                """
                John Doe 111111111
                TC379
                """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong") ||
                exception.getMessage().contains("format") ||
                exception. getMessage().contains("John Doe 111111111"));
    }

    @Test
    @DisplayName("Test wrong format - too many comma separators in user line")
    public void testReadUsers_WrongFormatTooManyCommas() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John,Doe,111111111
                TC379
                """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception. getMessage().contains("Wrong") ||
                exception.getMessage().contains("format"));
    }

    @Test
    @DisplayName("Test user with empty watched movies list")
    public void testReadUsers_EmptyWatchedMovies() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                John Doe,111111111
                
                """);

        List<User> users = userParser.readUsers(userFile.toString());
        assertEquals(1, users.size());
        assertTrue(users.get(0).watchedMovies().isEmpty() ||
                users.get(0).watchedMovies() == null);
    }
}