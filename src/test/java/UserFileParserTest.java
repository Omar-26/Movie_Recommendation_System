import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.nio.file.*;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class UserFileParserTest {

    @TempDir
    Path tempDir;

    private UserFileParser userParser;

    @BeforeEach
    public void setUp() {
        userParser = new UserFileParser();
    }

    // ===================== VALID FILE TESTS =====================
    @Test
    @DisplayName("Test reading a valid file with a single user")
    public void testReadUsers_ValidSingleUser() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002, M003
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
        assertEquals(Set.of("M001", "M002", "M003"), users.get(0).watchedMovies());
    }

    @Test
    @DisplayName("Test reading a valid file with multiple users")
    public void testReadUsers_ValidMultipleUsers() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002
                        Jane Smith, 987654321
                        M003, M004, M005
                        Bob Wilson, 111222333
                        M006
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(3, users.size());

        // Verify first user
        assertEquals("John Doe", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
        assertEquals(Set.of("M001", "M002"), users.get(0).watchedMovies());

        // Verify second user
        assertEquals("Jane Smith", users.get(1).name());
        assertEquals("987654321", users.get(1).id());
        assertEquals(Set.of("M003", "M004", "M005"), users.get(1).watchedMovies());

        // Verify third user
        assertEquals("Bob Wilson", users.get(2).name());
        assertEquals("111222333", users.get(2).id());
        assertEquals(Set.of("M006"), users.get(2).watchedMovies());
    }

    @Test
    @DisplayName("Test reading valid file with blank lines between users")
    public void testReadUsers_ValidWithBlankLines() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        
                        John Doe, 123456789
                        M001, M002
                        
                        
                        Jane Smith, 987654321
                        M003
                        
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("Jane Smith", users.get(1).name());
    }

    @Test
    @DisplayName("Test reading valid file with extra whitespace")
    public void testReadUsers_ExtraWhitespace() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                          John Doe  ,  123456789 \s
                          M001  ,  M002  ,  M003 \s
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
        assertEquals(Set.of("M001", "M002", "M003"), users.get(0).watchedMovies());
    }

    @Test
    @DisplayName("Test reading user with single watched movie")
    public void testReadUsers_SingleWatchedMovie() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals(Set.of("M001"), users.get(0).watchedMovies());
    }

    @Test
    @DisplayName("Test reading user with many watched movies")
    public void testReadUsers_ManyWatchedMovies() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002, M003, M004, M005, M006, M007
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals(Set.of("M001", "M002", "M003", "M004", "M005", "M006", "M007"), 
                users.get(0).watchedMovies());
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
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile, "   \n  \n    \n");

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(0, users.size());
        assertTrue(users.isEmpty());
    }

    // ===================== WRONG FORMAT TESTS =====================
    @Test
    @DisplayName("Test wrong format - missing comma separator")
    public void testReadUsers_WrongFormatMissingComma() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe 123456789
                        M001, M002
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong user line format"));
        assertTrue(exception.getMessage().contains("John Doe 123456789"));
    }

    @Test
    @DisplayName("Test wrong format - too many comma-separated fields")
    public void testReadUsers_WrongFormatTooManyFields() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789, Extra Field
                        M001, M002
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong user line format"));
    }

    @Test
    @DisplayName("Test wrong format - only name without ID")
    public void testReadUsers_WrongFormatOnlyName() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe
                        M001, M002
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong user line format"));
    }

    @Test
    @DisplayName("Test wrong format - only comma without name and ID")
    public void testReadUsers_WrongFormatOnlyComma() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        ,
                        M001, M002
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong user line format"));
    }

    @Test
    @DisplayName("Test wrong format in second user")
    public void testReadUsers_WrongFormatSecondUser() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002
                        Jane Smith 987654321
                        M003
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            userParser.readUsers(userFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong user line format"));
        assertTrue(exception.getMessage().contains("Jane Smith 987654321"));
    }

    // ===================== FILE NOT FOUND TEST =====================
    @Test
    @DisplayName("Test reading non-existent file")
    public void testReadUsers_FileNotFound() {
        assertThrows(Exception.class, () -> {
            userParser.readUsers("nonexistent_file_path.txt");
        });
    }

    @Test
    @DisplayName("Test reading from null path")
    public void testReadUsers_NullPath() {
        assertThrows(Exception.class, () -> {
            userParser.readUsers(null);
        });
    }

    // ===================== EDGE CASES TESTS =====================
    @Test
    @DisplayName("Test user with very long name")
    public void testReadUsers_LongName() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        String longName = "John Michael David William Robert Doe Smith";
        Files.writeString(userFile,
                longName + ", 123456789\n" +
                        "M001\n");

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals(longName, users.get(0).name());
    }

    @Test
    @DisplayName("Test watched movies are stored as set (no duplicates)")
    public void testReadUsers_WatchedMoviesAsSet() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002, M001, M003, M002
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals(3, users.get(0).watchedMovies().size());
        assertEquals(Set.of("M001", "M002", "M003"), users.get(0).watchedMovies());
    }

    @Test
    @DisplayName("Test mixed valid users are parsed correctly")
    public void testReadUsers_ComplexValidFile() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        John Doe, 123456789
                        M001, M002, M003
                        
                        Jane Smith, 987654321
                        M004
                        Bob Wilson, 111222333
                        M005, M006, M007, M008
                        
                        Alice Brown, 444555666
                        M009, M010
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(4, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("Jane Smith", users.get(1).name());
        assertEquals("Bob Wilson", users.get(2).name());
        assertEquals("Alice Brown", users.get(3).name());
    }

    @Test
    @DisplayName("Test user with single word name")
    public void testReadUsers_SingleWordName() throws Exception {
        Path userFile = tempDir.resolve("users.txt");
        Files.writeString(userFile,
                """
                        Madonna, 123456789
                        M001, M002
                        """);

        List<User> users = userParser.readUsers(userFile.toString());

        assertEquals(1, users.size());
        assertEquals("Madonna", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
    }
}
