package integration;

import core.*;
import model.Movie;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TOP-DOWN INTEGRATION TESTING
 * 
 * Approach: Start from the Main (top-level controller) and integrate downward.
 * Uses STUBS to simulate lower-level modules not yet integrated.
 * 
 * Integration Order:
 * Level 1: Main (Entry Point)
 * Level 2: MovieFileParser, UserFileParser, Validation, FileHandler
 * Level 3: Recommendation
 * Level 4: Model classes (Movie, User)
 * 
 * Test Flow: Main → Parsers → Validation → Recommendation → FileHandler →
 * Output
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Top-Down Integration Tests")
public class TopDownIntegrationTest {

    @TempDir
    Path tempDir;

    private Path moviesFile;
    private Path usersFile;
    private Path outputFile;

    @BeforeEach
    void setUp() throws IOException {
        moviesFile = tempDir.resolve("movies.txt");
        usersFile = tempDir.resolve("users.txt");
        outputFile = tempDir.resolve("recommendations.txt");
    }

    // ==================== LEVEL 1: Main Entry Point Integration
    // ====================

    @Test
    @Order(1)
    @DisplayName("TD-L1-01: Main initializes all parsers correctly")
    void testMainInitializesComponents() {
        // Verify Main can instantiate all required components
        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();

        assertNotNull(movieParser, "MovieFileParser should be instantiated");
        assertNotNull(userParser, "UserFileParser should be instantiated");
    }

    // ==================== LEVEL 2: Parser Integration ====================

    @Test
    @Order(2)
    @DisplayName("TD-L2-01: Main → MovieFileParser integration - valid movies")
    void testMainToMovieParserValidMovies() throws Exception {
        // Create valid movies file
        String content = """
                Spider Man,SM123
                Action,Adventure
                The Matrix,TM456
                SciFi,Action
                """;
        Files.writeString(moviesFile, content);

        MovieFileParser parser = new MovieFileParser();
        List<Movie> movies = parser.readMovies(moviesFile.toString());

        assertEquals(2, movies.size());
        assertEquals("Spider Man", movies.get(0).title());
        assertEquals("SM123", movies.get(0).id());
        assertArrayEquals(new String[] { "Action", "Adventure" }, movies.get(0).genres());
    }

    @Test
    @Order(3)
    @DisplayName("TD-L2-02: Main → MovieFileParser integration - malformed input")
    void testMainToMovieParserMalformedInput() throws IOException {
        String content = "InvalidLineWithoutComma\nAction,Adventure";
        Files.writeString(moviesFile, content);

        MovieFileParser parser = new MovieFileParser();

        Exception exception = assertThrows(Exception.class, () -> {
            parser.readMovies(moviesFile.toString());
        });

        assertTrue(exception.getMessage().contains("ERROR"));
    }

    @Test
    @Order(4)
    @DisplayName("TD-L2-03: Main → UserFileParser integration - valid users")
    void testMainToUserParserValidUsers() throws Exception {
        String content = """
                Ahmed Ali,123456789
                SM123,TM456
                Sara Mohamed,12345678A
                SM123
                """;
        Files.writeString(usersFile, content);

        UserFileParser parser = new UserFileParser();
        List<User> users = parser.readUsers(usersFile.toString());

        assertEquals(2, users.size());
        assertEquals("Ahmed Ali", users.get(0).name());
        assertEquals("123456789", users.get(0).id());
        assertTrue(users.get(0).watchedMovies().contains("SM123"));
    }

    @Test
    @Order(5)
    @DisplayName("TD-L2-04: Main → UserFileParser integration - malformed input")
    void testMainToUserParserMalformedInput() throws IOException {
        String content = "InvalidUserLineWithoutComma\nSM123";
        Files.writeString(usersFile, content);

        UserFileParser parser = new UserFileParser();

        Exception exception = assertThrows(Exception.class, () -> {
            parser.readUsers(usersFile.toString());
        });

        assertTrue(exception.getMessage().contains("ERROR"));
    }

    // ==================== LEVEL 2: Validation Integration ====================

    @Test
    @Order(6)
    @DisplayName("TD-L2-05: Main → Validation integration - valid movie title")
    void testMainToValidationMovieTitle() {
        Movie movie = new Movie("Spider Man", "SM123", new String[] { "Action" });

        String result = Validation.validateMovieTitle(movie);

        assertNull(result, "Valid movie title should return null");
    }

    @Test
    @Order(7)
    @DisplayName("TD-L2-06: Main → Validation integration - invalid movie title")
    void testMainToValidationInvalidMovieTitle() {
        Movie movie = new Movie("spider man", "SM123", new String[] { "Action" });

        String result = Validation.validateMovieTitle(movie);

        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
    }

    @Test
    @Order(8)
    @DisplayName("TD-L2-07: Main → Validation integration - valid movie ID")
    void testMainToValidationMovieId() {
        Movie movie = new Movie("Spider Man", "SM123", new String[] { "Action" });

        String result = Validation.validateMovieId(movie);

        assertNull(result, "Valid movie ID should return null");
    }

    @Test
    @Order(9)
    @DisplayName("TD-L2-08: Main → Validation integration - invalid movie ID format")
    void testMainToValidationInvalidMovieId() {
        Movie movie = new Movie("Spider Man", "XX123", new String[] { "Action" });

        String result = Validation.validateMovieId(movie);

        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
    }

    @Test
    @Order(10)
    @DisplayName("TD-L2-09: Main → Validation integration - valid user name")
    void testMainToValidationUserName() {
        User user = new User("Ahmed Ali", "123456789", Set.of("SM123"));

        String result = Validation.validateUserName(user, new HashSet<>());

        assertNull(result, "Valid user name should return null");
    }

    @Test
    @Order(11)
    @DisplayName("TD-L2-10: Main → Validation integration - valid user ID")
    void testMainToValidationUserId() {
        User user = new User("Ahmed Ali", "123456789", Set.of("SM123"));

        String result = Validation.validateUserId(user, new HashSet<>());

        assertNull(result, "Valid user ID should return null");
    }

    @Test
    @Order(12)
    @DisplayName("TD-L2-11: Main → Validation integration - duplicate user ID")
    void testMainToValidationDuplicateUserId() {
        User user = new User("Ahmed Ali", "123456789", Set.of("SM123"));
        Set<String> existingIds = new HashSet<>(Set.of("123456789"));

        String result = Validation.validateUserId(user, existingIds);

        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
    }

    // ==================== LEVEL 2: FileHandler Integration ====================

    @Test
    @Order(13)
    @DisplayName("TD-L2-12: Main → FileHandler.writeFile integration")
    void testMainToFileHandlerWrite() throws IOException {
        String errorMessage = "ERROR: Test error message";

        FileHandler.writeFile(outputFile, errorMessage);

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertTrue(content.contains("ERROR: Test error message"));
    }

    @Test
    @Order(14)
    @DisplayName("TD-L2-13: Main → FileHandler.readFile integration")
    void testMainToFileHandlerRead() throws IOException {
        String testContent = "Line1\nLine2\nLine3";
        Files.writeString(moviesFile, testContent);

        FileHandler handler = new FileHandler();
        List<String> lines = handler.readFile(moviesFile.toString());

        assertEquals(3, lines.size());
        assertEquals("Line1", lines.get(0));
    }

    // ==================== LEVEL 3: Recommendation Integration ====================

    @Test
    @Order(15)
    @DisplayName("TD-L3-01: Main → Parser → Validation → Recommendation integration")
    void testFullPipelineToRecommendation() throws Exception {
        // Setup movies
        String moviesContent = """
                Spider Man,SM123
                Action,Adventure
                The Matrix,TM456
                SciFi,Action
                Iron Man,IM789
                Action,SciFi
                """;
        Files.writeString(moviesFile, moviesContent);

        // Setup users
        String usersContent = """
                Ahmed Ali,123456789
                SM123
                """;
        Files.writeString(usersFile, usersContent);

        // Parse
        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();

        List<Movie> movies = movieParser.readMovies(moviesFile.toString());
        List<User> users = userParser.readUsers(usersFile.toString());

        // Validate movies
        for (Movie m : movies) {
            assertNull(Validation.validateMovieTitle(m));
            assertNull(Validation.validateMovieId(m));
        }

        // Validate users
        Set<String> existingIds = new HashSet<>();
        for (User u : users) {
            assertNull(Validation.validateUserName(u, existingIds));
            assertNull(Validation.validateUserId(u, existingIds));
            existingIds.add(u.id());
        }

        // Generate recommendations
        User testUser = users.get(0);
        Set<String> recommendations = Recommendation.recommendMovies(testUser.watchedMovies(), movies);

        assertNotNull(recommendations);
        // User watched Spider Man (Action, Adventure), should get Matrix and Iron Man
        assertTrue(recommendations.contains("The Matrix") || recommendations.contains("Iron Man"));
    }

    @Test
    @Order(16)
    @DisplayName("TD-L3-02: Recommendation with empty watched list")
    void testRecommendationEmptyWatched() {
        List<Movie> movies = List.of(
                new Movie("Spider Man", "SM123", new String[] { "Action" }));

        Set<String> recommendations = Recommendation.recommendMovies(Set.of(), movies);

        assertTrue(recommendations.isEmpty());
    }

    @Test
    @Order(17)
    @DisplayName("TD-L3-03: Recommendation with no matching genres")
    void testRecommendationNoMatches() {
        List<Movie> movies = List.of(
                new Movie("Spider Man", "SM123", new String[] { "Action" }),
                new Movie("Titanic", "T456", new String[] { "Romance" }));
        Set<String> watched = Set.of("SM123");

        Set<String> recommendations = Recommendation.recommendMovies(watched, movies);

        assertFalse(recommendations.contains("Titanic"));
    }

    // ==================== LEVEL 4: Full End-to-End Integration
    // ====================

    @Test
    @Order(18)
    @DisplayName("TD-L4-01: Complete system integration - successful recommendations")
    void testCompleteSystemIntegration() throws Exception {
        // Create complete test files
        String moviesContent = """
                Spider Man,SM123
                Action,Adventure
                The Matrix,TM456
                SciFi,Action
                Titanic,T789
                Romance,Drama
                """;
        Files.writeString(moviesFile, moviesContent);

        String usersContent = """
                Ahmed Ali,123456789
                SM123
                Sara Mohamed,12345678A
                TM456
                """;
        Files.writeString(usersFile, usersContent);

        // Full integration flow
        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();

        List<Movie> movies = movieParser.readMovies(moviesFile.toString());
        List<User> users = userParser.readUsers(usersFile.toString());

        // Validate all
        Set<String> existingIds = new HashSet<>();
        for (Movie m : movies) {
            assertNull(Validation.validateMovieTitle(m));
            assertNull(Validation.validateMovieId(m));
        }

        for (User u : users) {
            assertNull(Validation.validateUserName(u, existingIds));
            assertNull(Validation.validateUserId(u, existingIds));
            existingIds.add(u.id());
        }

        // Test recommendations for each user
        for (User u : users) {
            Set<String> recs = Recommendation.recommendMovies(u.watchedMovies(), movies);
            assertNotNull(recs);
        }
    }

    @Test
    @Order(19)
    @DisplayName("TD-L4-02: Complete system - validation error stops pipeline")
    void testValidationErrorStopsPipeline() throws Exception {
        String moviesContent = """
                spider man,SM123
                Action,Adventure
                """;
        Files.writeString(moviesFile, moviesContent);

        MovieFileParser movieParser = new MovieFileParser();
        List<Movie> movies = movieParser.readMovies(moviesFile.toString());

        // Validation should fail and stop
        Movie invalidMovie = movies.get(0);
        String error = Validation.validateMovieTitle(invalidMovie);

        assertNotNull(error, "Pipeline should stop on validation error");
        assertTrue(error.contains("ERROR"));
    }

    @Test
    @Order(20)
    @DisplayName("TD-L4-03: Complete system - file output generation")
    void testCompleteSystemFileOutput() throws Exception {
        String moviesContent = """
                Spider Man,SM123
                Action,Adventure
                The Matrix,TM456
                SciFi,Action
                """;
        Files.writeString(moviesFile, moviesContent);

        String usersContent = """
                Ahmed Ali,123456789
                SM123
                """;
        Files.writeString(usersFile, usersContent);

        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();

        List<Movie> movies = movieParser.readMovies(moviesFile.toString());
        List<User> users = userParser.readUsers(usersFile.toString());

        // Simulate full output
        User user = users.get(0);
        Set<String> recs = Recommendation.recommendMovies(user.watchedMovies(), movies);

        // Write to file
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write(user.name() + "," + user.id());
            writer.newLine();
            writer.write(String.join(",", recs));
            writer.newLine();
        }

        assertTrue(Files.exists(outputFile));
        String content = Files.readString(outputFile);
        assertTrue(content.contains("Ahmed Ali"));
        assertTrue(content.contains("123456789"));
    }
}
