
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - ECP & BVA for UserFileParser class
 * Tests equivalence class partitioning and boundary value analysis
 */
public class UserFileParserECPBVATest {
    private UserFileParser parser;
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException {
        parser = new UserFileParser();
        testFilePath = Files.createTempFile("users", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    //------- readUsers() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal user format
    @Test
    public void testReadUsers_ECP_ValidFormat() throws Exception {
        String content = "John Doe,123456789\nM001,M002,M003\nJane Smith,987654321\nM004,M005";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
        assertEquals(3, users.get(0).watchedMovies().size());
        assertTrue(users.get(0).watchedMovies().contains("M001"));
    }

    // BVA: Boundary - single user
    @Test
    public void testReadUsers_BVA_SingleUser() throws Exception {
        String content = "Solo User,111111111\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals("Solo User", users.get(0).name());
    }

    // BVA: Boundary - empty file
    @Test
    public void testReadUsers_BVA_EmptyFile() throws Exception {
        Files.writeString(testFilePath, "");
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertTrue(users.isEmpty());
    }

    // ECP: Valid equivalence class - single watched movie
    @Test
    public void testReadUsers_ECP_SingleWatchedMovie() throws Exception {
        String content = "User,123456789\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).watchedMovies().size());
        assertTrue(users.get(0).watchedMovies().contains("M001"));
    }

    // ECP: Valid equivalence class - multiple watched movies
    @Test
    public void testReadUsers_ECP_MultipleWatchedMovies() throws Exception {
        String content = "User,123456789\nM001,M002,M003,M004,M005";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals(5, users.get(0).watchedMovies().size());
    }

    // ECP: Invalid equivalence class - wrong name/ID format
    @Test
    public void testReadUsers_ECP_WrongNameIdFormat() {
        String content = "InvalidFormat";
        
        assertThrows(Exception.class, () -> {
            Files.writeString(testFilePath, content);
            parser.readUsers(testFilePath.toString());
        });
    }

    // ECP: Invalid equivalence class - too many commas in user line
    @Test
    public void testReadUsers_ECP_TooManyCommas() {
        String content = "John,Doe,123456789\nM001";
        
        assertThrows(Exception.class, () -> {
            Files.writeString(testFilePath, content);
            parser.readUsers(testFilePath.toString());
        });
    }

    // BVA: Boundary - blank lines in file
    @Test
    public void testReadUsers_BVA_WithBlankLines() throws Exception {
        String content = "\n\nJohn,123456789\nM001\n\nJane,987654321\nM002\n\n";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(2, users.size());
    }

    // ECP: Valid equivalence class - whitespace in user ID
    @Test
    public void testReadUsers_ECP_WhitespaceInUserId() throws Exception {
        String content = "John,  123456789  \nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals("123456789", users.get(0).id()); // Trimmed
    }

    // ECP: Valid equivalence class - name with spaces preserved
    @Test
    public void testReadUsers_ECP_NameWithSpaces() throws Exception {
        String content = "John Smith,123456789\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals("John Smith", users.get(0).name());
    }

    // ECP: Valid equivalence class - name with leading spaces (preserved)
    @Test
    public void testReadUsers_ECP_NameWithLeadingSpaces() throws Exception {
        String content = " John,123456789\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        // Leading space preserved in name
        assertEquals(" John", users.get(0).name());
    }

    // BVA: Boundary - empty watched movies line
    @Test
    public void testReadUsers_BVA_EmptyWatchedMovies() throws Exception {
        String content = "User,123456789\n";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertTrue(users.get(0).watchedMovies().isEmpty());
    }

    // BVA: Boundary - watched movies with only commas
    @Test
    public void testReadUsers_BVA_OnlyCommasInWatchedMovies() throws Exception {
        String content = "User,123456789\n,,,";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        // Empty strings filtered out
        assertTrue(users.get(0).watchedMovies().isEmpty());
    }

    // ECP: Valid equivalence class - whitespace in watched movies
    @Test
    public void testReadUsers_ECP_WhitespaceInWatchedMovies() throws Exception {
        String content = "User,123456789\n  M001  ,  M002  ,  M003  ";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals(3, users.get(0).watchedMovies().size());
        assertTrue(users.get(0).watchedMovies().contains("M001"));
        assertTrue(users.get(0).watchedMovies().contains("M002"));
        assertTrue(users.get(0).watchedMovies().contains("M003"));
    }

    // BVA: Boundary - many users
    @Test
    public void testReadUsers_BVA_ManyUsers() throws Exception {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("User").append(i).append(",").append(String.format("%09d", i)).append("\n");
            content.append("M").append(String.format("%03d", i)).append("\n");
        }
        Files.writeString(testFilePath, content.toString());
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(50, users.size());
    }

    // ECP: Valid equivalence class - duplicate movie IDs in watched list
    @Test
    public void testReadUsers_ECP_DuplicateWatchedMovies() throws Exception {
        String content = "User,123456789\nM001,M002,M001,M003,M002";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        // Set should remove duplicates
        assertEquals(3, users.get(0).watchedMovies().size());
        assertTrue(users.get(0).watchedMovies().contains("M001"));
        assertTrue(users.get(0).watchedMovies().contains("M002"));
        assertTrue(users.get(0).watchedMovies().contains("M003"));
    }

    // ECP: Valid equivalence class - user ID with letter
    @Test
    public void testReadUsers_ECP_UserIdWithLetter() throws Exception {
        String content = "User,12345678A\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals("12345678A", users.get(0).id());
    }

    // BVA: Boundary - very long name
    @Test
    public void testReadUsers_BVA_VeryLongName() throws Exception {
        String longName = "A".repeat(100);
        String content = longName + ",123456789\nM001";
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals(longName, users.get(0).name());
    }

    // BVA: Boundary - many watched movies
    @Test
    public void testReadUsers_BVA_ManyWatchedMovies() throws Exception {
        StringBuilder watchedMovies = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            if (i > 0) watchedMovies.append(",");
            watchedMovies.append("M").append(String.format("%03d", i));
        }
        String content = "User,123456789\n" + watchedMovies;
        Files.writeString(testFilePath, content);
        
        List<User> users = parser.readUsers(testFilePath.toString());
        
        assertEquals(1, users.size());
        assertEquals(100, users.get(0).watchedMovies().size());
    }
}
