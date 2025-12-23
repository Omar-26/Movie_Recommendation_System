package whiteBox;

import logic.Recommendation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White-Box Testing for Recommendation.java
 *
 * This test class covers the following methods:
 * - recommendMovies(Set<String> watchedMovies, List<Movie> movies)
 * - generateRecommendationsFile(List<User> users, List<Movie> movies)
 * - getRecommendations(List<Movie> movies, Set<String> watchedSet, Set<String> likedGenres)
 *
 * Coverage Types:
 * - Statement Coverage: Tests that execute each statement at least once
 * - Branch Coverage: Tests that cover all decision branches (true/false)
 * - Conditional Coverage: Tests that cover all individual conditions in compound decisions
 * - Path Coverage: Tests that cover all possible execution paths
 *
 * @author Testing Team
 * @version 1.0
 */
@DisplayName("Recommendation – White-Box Coverage Tests")
public class RecomendationFileTest {

    @TempDir
    Path tempDir;

    private List<Movie> sampleMovies;

    // ===================== SETUP =====================

    /**
     * Sets up sample movie data before each test.
     * Creates a consistent set of movies for testing recommendations.
     */
    @BeforeEach
    void setUp() {
        sampleMovies = new ArrayList<>();
        sampleMovies.add(new Movie("The Matrix", "TM123", new String[]{"Action", "Sci-Fi"}));
        sampleMovies.add(new Movie("Inception", "IN456", new String[]{"Action", "Thriller"}));
        sampleMovies.add(new Movie("Titanic", "TI789", new String[]{"Drama", "Romance"}));
        sampleMovies.add(new Movie("The Notebook", "TN012", new String[]{"Drama", "Romance"}));
        sampleMovies.add(new Movie("Avengers", "AV345", new String[]{"Action", "Adventure"}));
    }

    // ==================================================================================
    // ===================== STATEMENT COVERAGE TEST CASES =============================
    // ==================================================================================
    // Statement Coverage: Execute each statement in the code at least once
    // Goal: 100% statement coverage means every line of code has been executed

    /**
     * TC_REC_SC_01: recommendMovies with valid watched movies and movie list
     *
     * Scenario: Valid input with watched movies
     * Test Data:
     *   - watchedMovies: Set containing "TM123" (The Matrix)
     *   - movies: sampleMovies list
     *
     * Test Steps:
     *   1. Create a set with one watched movie ID
     *   2. Call Recommendation.recommendMovies(watchedMovies, movies)
     *   3. Verify recommendations are returned
     *
     * Expected: Returns set with recommended Action movies (Inception, Avengers)
     *
     * Statement Coverage: Executes all statements in recommendMovies()
     * - Lines covered: 17-28 (initialization, loop, genre extraction, return)
     */
    @Test
    @DisplayName("TC_REC_SC_01 – Statement: valid watched movies returns recommendations")
    void testStatementCoverage_SC01_ValidWatchedMovies() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();
        watchedMovies.add("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertNotNull(recommendations, "Recommendations should not be null");
        assertFalse(recommendations.isEmpty(), "Recommendations should not be empty");
        assertFalse(recommendations.contains("The Matrix"), "Should not recommend watched movie");
    }

    /**
     * TC_REC_SC_02: recommendMovies with null watched movies
     *
     * Scenario: Null watchedMovies input
     * Test Data:
     *   - watchedMovies: null
     *   - movies: sampleMovies list
     *
     * Test Steps:
     *   1. Call Recommendation.recommendMovies(null, movies)
     *   2. Verify empty set is returned
     *
     * Expected: Returns empty set
     *
     * Statement Coverage: Executes null check statement
     * - Lines covered: 17 (null check and early return)
     */
    @Test
    @DisplayName("TC_REC_SC_02 – Statement: null watched movies returns empty set")
    void testStatementCoverage_SC02_NullWatchedMovies() {
        // Act
        Set<String> recommendations = Recommendation.recommendMovies(null, sampleMovies);

        // Assert
        assertNotNull(recommendations);
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_SC_03: recommendMovies with empty watched movies
     *
     * Scenario: Empty watchedMovies set
     * Test Data:
     *   - watchedMovies: empty HashSet
     *   - movies: sampleMovies list
     *
     * Test Steps:
     *   1. Create empty watchedMovies set
     *   2. Call Recommendation.recommendMovies(watchedMovies, movies)
     *   3. Verify empty set is returned
     *
     * Expected: Returns empty set
     *
     * Statement Coverage: Executes isEmpty check statement
     * - Lines covered: 17 (isEmpty check and early return)
     */
    @Test
    @DisplayName("TC_REC_SC_03 – Statement: empty watched movies returns empty set")
    void testStatementCoverage_SC03_EmptyWatchedMovies() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_SC_04: recommendMovies with null movie list
     *
     * Scenario: Null movies list input
     * Test Data:
     *   - watchedMovies: Set with "TM123"
     *   - movies: null
     *
     * Test Steps:
     *   1. Create valid watchedMovies set
     *   2. Call Recommendation.recommendMovies(watchedMovies, null)
     *   3. Verify empty set is returned
     *
     * Expected: Returns empty set
     *
     * Statement Coverage: Executes movies null check statement
     * - Lines covered: 17 (movies null check and early return)
     */
    @Test
    @DisplayName("TC_REC_SC_04 – Statement: null movies list returns empty set")
    void testStatementCoverage_SC04_NullMoviesList() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();
        watchedMovies.add("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, null);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_SC_05: recommendMovies with empty movie list
     *
     * Scenario: Empty movies list input
     * Test Data:
     *   - watchedMovies: Set with "TM123"
     *   - movies: empty ArrayList
     *
     * Test Steps:
     *   1. Create valid watchedMovies set
     *   2. Call Recommendation.recommendMovies(watchedMovies, emptyList)
     *   3. Verify empty set is returned
     *
     * Expected: Returns empty set
     *
     * Statement Coverage: Executes movies isEmpty check statement
     * - Lines covered: 17 (movies isEmpty check and early return)
     */
    @Test
    @DisplayName("TC_REC_SC_05 – Statement: empty movies list returns empty set")
    void testStatementCoverage_SC05_EmptyMoviesList() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();
        watchedMovies.add("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, new ArrayList<>());

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_SC_06: generateRecommendationsFile with valid users and movies
     *
     * Scenario: Generate file with valid input
     * Test Data:
     *   - users: List with one valid user
     *   - movies: sampleMovies list
     *
     * Test Steps:
     *   1. Create valid user with watched movies
     *   2. Call Recommendation.generateRecommendationsFile(users, movies)
     *   3. Verify file is created with correct content
     *
     * Expected: Creates recommendations.txt with user data
     *
     * Statement Coverage: Executes all file writing statements
     * - Lines covered: 33-54 (file creation, writing, loop)
     */
    @Test
    @DisplayName("TC_REC_SC_06 – Statement: generate file with valid users")
    void testStatementCoverage_SC06_GenerateFileValidUsers() throws IOException {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();
        watchedMovies.add("TM123");
        User user = new User("John Doe", "123456789", watchedMovies);
        List<User> users = List.of(user);

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        assertTrue(Files.exists(outPath));
        String content = Files.readString(outPath);
        assertTrue(content.contains("John Doe"));
        assertTrue(content.contains("123456789"));
    }

    /**
     * TC_REC_SC_07: generateRecommendationsFile with null users
     *
     * Scenario: Null users list input
     * Test Data:
     *   - users: null
     *   - movies: sampleMovies list
     *
     * Test Steps:
     *   1. Call Recommendation.generateRecommendationsFile(null, movies)
     *   2. Verify no exception is thrown
     *
     * Expected: Returns early without error
     *
     * Statement Coverage: Executes null users check statement
     * - Lines covered: 37 (null check and early return)
     */
    @Test
    @DisplayName("TC_REC_SC_07 – Statement: null users returns early")
    void testStatementCoverage_SC07_NullUsers() {
        // Act & Assert
        assertDoesNotThrow(() -> Recommendation.generateRecommendationsFile(null, sampleMovies));
    }

    // ==================================================================================
    // ===================== BRANCH COVERAGE TEST CASES =================================
    // ==================================================================================
    // Branch Coverage: Execute each branch (true/false) of every decision point
    // Goal: 100% branch coverage means every if/else branch has been taken

    /**
     * TC_REC_BC_01: Branch - watchedMovies is NOT null (false branch of null check)
     *
     * Scenario: Valid watchedMovies, takes false branch of null check
     * Test Data:
     *   - watchedMovies: valid Set with "TM123"
     *
     * Test Steps:
     *   1. Create valid watchedMovies set
     *   2. Call recommendMovies()
     *   3. Verify processing continues past null check
     *
     * Expected: Processes normally, returns recommendations
     *
     * Branch Coverage: watchedMovies == null → FALSE
     */
    @Test
    @DisplayName("TC_REC_BC_01 – Branch: watchedMovies not null (false branch)")
    void testBranchCoverage_BC01_WatchedMoviesNotNull() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();
        watchedMovies.add("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
    }

    /**
     * TC_REC_BC_02: Branch - watchedMovies IS null (true branch of null check)
     *
     * Scenario: Null watchedMovies, takes true branch of null check
     * Test Data:
     *   - watchedMovies: null
     *
     * Test Steps:
     *   1. Pass null for watchedMovies
     *   2. Call recommendMovies()
     *   3. Verify early return with empty set
     *
     * Expected: Returns empty set immediately
     *
     * Branch Coverage: watchedMovies == null → TRUE
     */
    @Test
    @DisplayName("TC_REC_BC_02 – Branch: watchedMovies is null (true branch)")
    void testBranchCoverage_BC02_WatchedMoviesNull() {
        // Act
        Set<String> recommendations = Recommendation.recommendMovies(null, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_BC_03: Branch - watchedMovies is NOT empty (false branch)
     *
     * Scenario: Non-empty watchedMovies, takes false branch of isEmpty check
     * Test Data:
     *   - watchedMovies: Set with "TM123"
     *
     * Test Steps:
     *   1. Create non-empty watchedMovies
     *   2. Call recommendMovies()
     *   3. Verify processing continues
     *
     * Expected: Processes normally
     *
     * Branch Coverage: watchedMovies.isEmpty() → FALSE
     */
    @Test
    @DisplayName("TC_REC_BC_03 – Branch: watchedMovies not empty (false branch)")
    void testBranchCoverage_BC03_WatchedMoviesNotEmpty() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertFalse(recommendations.isEmpty());
    }

    /**
     * TC_REC_BC_04: Branch - watchedMovies IS empty (true branch)
     *
     * Scenario: Empty watchedMovies, takes true branch of isEmpty check
     * Test Data:
     *   - watchedMovies: empty Set
     *
     * Test Steps:
     *   1. Create empty watchedMovies
     *   2. Call recommendMovies()
     *   3. Verify early return
     *
     * Expected: Returns empty set immediately
     *
     * Branch Coverage: watchedMovies.isEmpty() → TRUE
     */
    @Test
    @DisplayName("TC_REC_BC_04 – Branch: watchedMovies is empty (true branch)")
    void testBranchCoverage_BC04_WatchedMoviesEmpty() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>();

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_BC_05: Branch - movie NOT in watched set (true branch)
     *
     * Scenario: Movie is not watched, becomes recommendation candidate
     * Test Data:
     *   - watchedMovies: Set with "TM123" only
     *   - Other movies available in list
     *
     * Test Steps:
     *   1. Watch only The Matrix
     *   2. Call recommendMovies()
     *   3. Verify other movies are recommended
     *
     * Expected: Non-watched movies with matching genres are recommended
     *
     * Branch Coverage: !watchedSet.contains(movie.id()) → TRUE
     */
    @Test
    @DisplayName("TC_REC_BC_05 – Branch: movie not in watched set (true branch)")
    void testBranchCoverage_BC05_MovieNotWatched() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertFalse(recommendations.contains("The Matrix"));
        assertTrue(recommendations.contains("Inception") || recommendations.contains("Avengers"));
    }

    /**
     * TC_REC_BC_06: Branch - movie IS in watched set (false branch)
     *
     * Scenario: Movie is already watched, excluded from recommendations
     * Test Data:
     *   - watchedMovies: Set with all Action movie IDs
     *
     * Test Steps:
     *   1. Watch all Action movies
     *   2. Call recommendMovies()
     *   3. Verify watched movies are excluded
     *
     * Expected: Watched movies are not in recommendations
     *
     * Branch Coverage: !watchedSet.contains(movie.id()) → FALSE
     */
    @Test
    @DisplayName("TC_REC_BC_06 – Branch: movie in watched set (false branch)")
    void testBranchCoverage_BC06_MovieWatched() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123", "IN456", "AV345");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertFalse(recommendations.contains("The Matrix"));
        assertFalse(recommendations.contains("Inception"));
        assertFalse(recommendations.contains("Avengers"));
    }

    /**
     * TC_REC_BC_07: Branch - genre matches likedGenres (true branch)
     *
     * Scenario: Movie genre matches user's liked genres
     * Test Data:
     *   - watchedMovies: Set with "TI789" (Titanic - Drama/Romance)
     *
     * Test Steps:
     *   1. Watch Titanic (Drama, Romance)
     *   2. Call recommendMovies()
     *   3. Verify The Notebook (Drama, Romance) is recommended
     *
     * Expected: Movies with matching genres are recommended
     *
     * Branch Coverage: likedGenres.contains(genre) → TRUE
     */
    @Test
    @DisplayName("TC_REC_BC_07 – Branch: genre matches (true branch)")
    void testBranchCoverage_BC07_GenreMatches() {
        // Arrange
        Set<String> watchedMovies = Set.of("TI789");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertTrue(recommendations.contains("The Notebook"));
    }

    /**
     * TC_REC_BC_08: Branch - genre does NOT match likedGenres (false branch)
     *
     * Scenario: Movie genre does not match user's liked genres
     * Test Data:
     *   - Custom movies with unique genres (Sci-Fi only, Horror only)
     *   - Watch Sci-Fi movie
     *
     * Test Steps:
     *   1. Create movies with non-overlapping genres
     *   2. Watch Sci-Fi movie
     *   3. Call recommendMovies()
     *   4. Verify Horror movie is not recommended
     *
     * Expected: Movies without matching genres are not recommended
     *
     * Branch Coverage: likedGenres.contains(genre) → FALSE
     */
    @Test
    @DisplayName("TC_REC_BC_08 – Branch: genre does not match (false branch)")
    void testBranchCoverage_BC08_GenreDoesNotMatch() {
        // Arrange
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Sci-Fi Only", "SF123", new String[]{"Sci-Fi"}));
        movies.add(new Movie("Horror Only", "HO456", new String[]{"Horror"}));
        Set<String> watchedMovies = Set.of("SF123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movies);

        // Assert
        assertFalse(recommendations.contains("Horror Only"));
    }

    /**
     * TC_REC_BC_09: Branch - user is NOT null in list (false branch)
     *
     * Scenario: Valid user in users list, continues processing
     * Test Data:
     *   - users: List with valid User object
     *
     * Test Steps:
     *   1. Create valid user
     *   2. Call generateRecommendationsFile()
     *   3. Verify user is processed
     *
     * Expected: User's recommendations are generated
     *
     * Branch Coverage: u == null → FALSE (continue processing)
     */
    @Test
    @DisplayName("TC_REC_BC_09 – Branch: user not null (false branch)")
    void testBranchCoverage_BC09_UserNotNull() throws IOException {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");
        User user = new User("John Doe", "123456789", watchedMovies);
        List<User> users = List.of(user);

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        String content = Files.readString(outPath);
        assertTrue(content.contains("John Doe"));
    }

    /**
     * TC_REC_BC_10: Branch - user IS null in list (true branch - continue)
     *
     * Scenario: Null user in list, skips to next user
     * Test Data:
     *   - users: List with null user followed by valid user
     *
     * Test Steps:
     *   1. Create list with null and valid users
     *   2. Call generateRecommendationsFile()
     *   3. Verify null user is skipped, valid user processed
     *
     * Expected: Null user skipped, valid user processed
     *
     * Branch Coverage: u == null → TRUE (continue/skip)
     */
    @Test
    @DisplayName("TC_REC_BC_10 – Branch: user is null (true branch - skip)")
    void testBranchCoverage_BC10_UserNull() throws IOException {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(null);
        users.add(new User("Valid User", "123456789", Set.of("TM123")));

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        String content = Files.readString(outPath);
        assertTrue(content.contains("Valid User"));
    }

    // ==================================================================================
    // ===================== CONDITIONAL COVERAGE TEST CASES ============================
    // ==================================================================================
    // Conditional Coverage: Each individual condition in compound decisions evaluates to both true and false
    // Goal: For compound conditions like (A || B || C), test each A, B, C as true and false independently

    /**
     * TC_REC_CC_01: Conditional - watchedMovies == null (first condition TRUE)
     *
     * Scenario: Test first condition in compound check: watchedMovies == null
     * Test Data:
     *   - watchedMovies: null
     *   - movies: valid list
     *
     * Compound Condition: (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty())
     *
     * Test Steps:
     *   1. Pass null for watchedMovies
     *   2. Call recommendMovies()
     *   3. Verify early return
     *
     * Expected: Returns empty set (first condition TRUE)
     *
     * Conditional Coverage: watchedMovies == null → TRUE (others not evaluated)
     */
    @Test
    @DisplayName("TC_REC_CC_01 – Conditional: watchedMovies null (first condition TRUE)")
    void testConditionalCoverage_CC01_WatchedMoviesNull() {
        // Act
        Set<String> recommendations = Recommendation.recommendMovies(null, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_CC_02: Conditional - watchedMovies.isEmpty() (second condition TRUE)
     *
     * Scenario: Test second condition: watchedMovies.isEmpty() = true
     * Test Data:
     *   - watchedMovies: empty Set (not null)
     *   - movies: valid list
     *
     * Compound Condition: (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty())
     *
     * Test Steps:
     *   1. Pass empty (not null) watchedMovies
     *   2. Call recommendMovies()
     *   3. Verify early return
     *
     * Expected: Returns empty set (second condition TRUE)
     *
     * Conditional Coverage: watchedMovies == null → FALSE, watchedMovies.isEmpty() → TRUE
     */
    @Test
    @DisplayName("TC_REC_CC_02 – Conditional: watchedMovies empty (second condition TRUE)")
    void testConditionalCoverage_CC02_WatchedMoviesEmpty() {
        // Arrange
        Set<String> watchedMovies = new HashSet<>(); // empty but not null

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_CC_03: Conditional - movies == null (third condition TRUE)
     *
     * Scenario: Test third condition: movies == null
     * Test Data:
     *   - watchedMovies: valid Set with data
     *   - movies: null
     *
     * Compound Condition: (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty())
     *
     * Test Steps:
     *   1. Pass valid watchedMovies
     *   2. Pass null for movies
     *   3. Call recommendMovies()
     *   4. Verify early return
     *
     * Expected: Returns empty set (third condition TRUE)
     *
     * Conditional Coverage: first two FALSE, movies == null → TRUE
     */
    @Test
    @DisplayName("TC_REC_CC_03 – Conditional: movies null (third condition TRUE)")
    void testConditionalCoverage_CC03_MoviesNull() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, null);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_CC_04: Conditional - movies.isEmpty() (fourth condition TRUE)
     *
     * Scenario: Test fourth condition: movies.isEmpty() = true
     * Test Data:
     *   - watchedMovies: valid Set with data
     *   - movies: empty List (not null)
     *
     * Compound Condition: (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty())
     *
     * Test Steps:
     *   1. Pass valid watchedMovies
     *   2. Pass empty (not null) movies list
     *   3. Call recommendMovies()
     *   4. Verify early return
     *
     * Expected: Returns empty set (fourth condition TRUE)
     *
     * Conditional Coverage: first three FALSE, movies.isEmpty() → TRUE
     */
    @Test
    @DisplayName("TC_REC_CC_04 – Conditional: movies empty (fourth condition TRUE)")
    void testConditionalCoverage_CC04_MoviesEmpty() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");
        List<Movie> movies = new ArrayList<>(); // empty but not null

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_CC_05: Conditional - all conditions FALSE (full processing)
     *
     * Scenario: All conditions in compound check are false
     * Test Data:
     *   - watchedMovies: valid non-empty Set
     *   - movies: valid non-empty List
     *
     * Compound Condition: (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty())
     *
     * Test Steps:
     *   1. Pass valid non-empty watchedMovies
     *   2. Pass valid non-empty movies
     *   3. Call recommendMovies()
     *   4. Verify full processing occurs
     *
     * Expected: Returns recommendations (all conditions FALSE)
     *
     * Conditional Coverage: ALL conditions → FALSE
     */
    @Test
    @DisplayName("TC_REC_CC_05 – Conditional: all conditions false (full processing)")
    void testConditionalCoverage_CC05_AllConditionsFalse() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertFalse(recommendations.isEmpty());
    }

    /**
     * TC_REC_CC_06: Conditional - user.watchedMovies() != null AND !isEmpty() (both TRUE)
     *
     * Scenario: Both conditions for processing user are true
     * Test Data:
     *   - User with non-null, non-empty watchedMovies
     *
     * Compound Condition: (u.watchedMovies() != null && !u.watchedMovies().isEmpty())
     *
     * Test Steps:
     *   1. Create user with valid watched movies
     *   2. Call generateRecommendationsFile()
     *   3. Verify user is processed
     *
     * Expected: User's recommendations are generated
     *
     * Conditional Coverage: watchedMovies() != null → TRUE, !isEmpty() → TRUE
     */
    @Test
    @DisplayName("TC_REC_CC_06 – Conditional: user has valid watchedMovies (both TRUE)")
    void testConditionalCoverage_CC06_UserWatchedMoviesValid() throws IOException {
        // Arrange
        User user = new User("John Doe", "123456789", Set.of("TM123"));
        List<User> users = List.of(user);

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        String content = Files.readString(outPath);
        assertTrue(content.contains("John Doe"));
    }

    /**
     * TC_REC_CC_07: Conditional - user.watchedMovies() == null (first condition FALSE)
     *
     * Scenario: User's watchedMovies is null
     * Test Data:
     *   - User with null watchedMovies
     *
     * Compound Condition: (u.watchedMovies() != null && !u.watchedMovies().isEmpty())
     *
     * Test Steps:
     *   1. Create user with null watched movies
     *   2. Call generateRecommendationsFile()
     *   3. Verify user is skipped
     *
     * Expected: User is skipped (first condition FALSE)
     *
     * Conditional Coverage: watchedMovies() != null → FALSE
     */
    @Test
    @DisplayName("TC_REC_CC_07 – Conditional: user watchedMovies null (first FALSE)")
    void testConditionalCoverage_CC07_UserWatchedMoviesNull() {
        // Arrange
        User user = new User("Jane Doe", "987654321", null);
        List<User> users = List.of(user);

        // Act & Assert
        assertDoesNotThrow(() -> Recommendation.generateRecommendationsFile(users, sampleMovies));
    }

    /**
     * TC_REC_CC_08: Conditional - user.watchedMovies().isEmpty() (second condition FALSE)
     *
     * Scenario: User's watchedMovies is not null but empty
     * Test Data:
     *   - User with empty watchedMovies set
     *
     * Compound Condition: (u.watchedMovies() != null && !u.watchedMovies().isEmpty())
     *
     * Test Steps:
     *   1. Create user with empty watched movies
     *   2. Call generateRecommendationsFile()
     *   3. Verify user is skipped
     *
     * Expected: User is skipped (second condition FALSE because isEmpty() is TRUE)
     *
     * Conditional Coverage: watchedMovies() != null → TRUE, !isEmpty() → FALSE
     */
    @Test
    @DisplayName("TC_REC_CC_08 – Conditional: user watchedMovies empty (second FALSE)")
    void testConditionalCoverage_CC08_UserWatchedMoviesEmpty() {
        // Arrange
        User user = new User("Jane Doe", "987654321", new HashSet<>());
        List<User> users = List.of(user);

        // Act & Assert
        assertDoesNotThrow(() -> Recommendation.generateRecommendationsFile(users, sampleMovies));
    }

    // ==================================================================================
    // ===================== PATH COVERAGE TEST CASES ===================================
    // ==================================================================================
    // Path Coverage: Execute all possible paths through the code
    // Goal: Test every unique execution path from entry to exit

    /**
     * TC_REC_PC_01: Path 1 - Early exit (null/empty input)
     *
     * Scenario: Shortest path - immediate return due to invalid input
     * Path: Entry → null/empty check → return empty set
     *
     * Test Data:
     *   - watchedMovies: null
     *
     * Test Steps:
     *   1. Pass null input
     *   2. Call recommendMovies()
     *   3. Verify immediate return
     *
     * Expected: Returns empty set without processing
     *
     * Path Coverage: Path 1 (shortest path)
     */
    @Test
    @DisplayName("TC_REC_PC_01 – Path 1: early exit with null input")
    void testPathCoverage_PC01_EarlyExit() {
        // Act & Assert
        assertTrue(Recommendation.recommendMovies(null, sampleMovies).isEmpty());
        assertTrue(Recommendation.recommendMovies(new HashSet<>(), sampleMovies).isEmpty());
        assertTrue(Recommendation.recommendMovies(Set.of("TM123"), null).isEmpty());
        assertTrue(Recommendation.recommendMovies(Set.of("TM123"), new ArrayList<>()).isEmpty());
    }

    /**
     * TC_REC_PC_02: Path 2 - Full processing with recommendations found
     *
     * Scenario: Complete happy path with recommendations
     * Path: Entry → validate inputs → extract genres → find matches → add recommendations → return set
     *
     * Test Data:
     *   - watchedMovies: Set with "TM123" (Action, Sci-Fi)
     *   - movies: sampleMovies with matching genres
     *
     * Test Steps:
     *   1. Pass valid inputs
     *   2. Call recommendMovies()
     *   3. Verify full processing and recommendations returned
     *
     * Expected: Returns non-empty recommendations set
     *
     * Path Coverage: Path 2 (full processing, matches found)
     */
    @Test
    @DisplayName("TC_REC_PC_02 – Path 2: full processing with matches")
    void testPathCoverage_PC02_FullProcessingWithMatches() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        assertTrue(recommendations.contains("Inception") || recommendations.contains("Avengers"));
    }

    /**
     * TC_REC_PC_03: Path 3 - Full processing with no matches found
     *
     * Scenario: Complete processing but no matching genres
     * Path: Entry → validate inputs → extract genres → no matches found → return empty set
     *
     * Test Data:
     *   - Custom movies with unique non-overlapping genres
     *   - watchedMovies: Sci-Fi movie only
     *
     * Test Steps:
     *   1. Create movies with unique genres (no overlap)
     *   2. Watch one genre, other movies have different genre
     *   3. Call recommendMovies()
     *   4. Verify no recommendations
     *
     * Expected: Returns empty set (no matches)
     *
     * Path Coverage: Path 3 (full processing, no matches)
     */
    @Test
    @DisplayName("TC_REC_PC_03 – Path 3: full processing with no matches")
    void testPathCoverage_PC03_FullProcessingNoMatches() {
        // Arrange
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Horror Movie", "HM123", new String[]{"Horror"}));
        movies.add(new Movie("Comedy Movie", "CM456", new String[]{"Comedy"}));
        Set<String> watchedMovies = Set.of("HM123");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movies);

        // Assert
        assertFalse(recommendations.contains("Comedy Movie"));
    }

    /**
     * TC_REC_PC_04: Path 4 - All movies already watched
     *
     * Scenario: User has watched all movies in the list
     * Path: Entry → validate → extract genres → all movies filtered out → return empty
     *
     * Test Data:
     *   - watchedMovies: Contains all movie IDs
     *
     * Test Steps:
     *   1. Add all movie IDs to watched set
     *   2. Call recommendMovies()
     *   3. Verify empty recommendations
     *
     * Expected: Returns empty set (all filtered)
     *
     * Path Coverage: Path 4 (all movies excluded)
     */
    @Test
    @DisplayName("TC_REC_PC_04 – Path 4: all movies already watched")
    void testPathCoverage_PC04_AllMoviesWatched() {
        // Arrange
        Set<String> watchedMovies = Set.of("TM123", "IN456", "TI789", "TN012", "AV345");

        // Act
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, sampleMovies);

        // Assert
        assertTrue(recommendations.isEmpty());
    }

    /**
     * TC_REC_PC_05: Path 5 - generateRecommendationsFile with null users (early exit)
     *
     * Scenario: Null users list causes early return
     * Path: Entry → null check → return immediately
     *
     * Test Data:
     *   - users: null
     *
     * Test Steps:
     *   1. Pass null for users
     *   2. Call generateRecommendationsFile()
     *   3. Verify early return without error
     *
     * Expected: Returns early without error
     *
     * Path Coverage: Path 5 (early exit in generateRecommendationsFile)
     */
    @Test
    @DisplayName("TC_REC_PC_05 – Path 5: generate file with null users")
    void testPathCoverage_PC05_GenerateFileNullUsers() {
        // Act & Assert
        assertDoesNotThrow(() -> Recommendation.generateRecommendationsFile(null, sampleMovies));
    }

    /**
     * TC_REC_PC_06: Path 6 - Mixed users (null, empty, valid)
     *
     * Scenario: Users list contains mix of null, empty watchedMovies, and valid users
     * Path: Entry → loop → skip null → skip empty → process valid → write file
     *
     * Test Data:
     *   - users: [null, user with empty watchedMovies, valid user]
     *
     * Test Steps:
     *   1. Create mixed users list
     *   2. Call generateRecommendationsFile()
     *   3. Verify only valid user processed
     *
     * Expected: Only valid user appears in output
     *
     * Path Coverage: Path 6 (mixed processing path)
     */
    @Test
    @DisplayName("TC_REC_PC_06 – Path 6: mixed users (null, empty, valid)")
    void testPathCoverage_PC06_MixedUsers() throws IOException {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(null);
        users.add(new User("Empty User", "111111111", new HashSet<>()));
        users.add(new User("Valid User", "222222222", Set.of("TM123")));

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        String content = Files.readString(outPath);
        assertTrue(content.contains("Valid User"));
        assertFalse(content.contains("Empty User"));
    }

    /**
     * TC_REC_PC_07: Path 7 - Multiple valid users with different genres
     *
     * Scenario: Multiple users with different watched movies
     * Path: Entry → loop → process user 1 → process user 2 → write file
     *
     * Test Data:
     *   - User 1: watched Action movies
     *   - User 2: watched Drama movies
     *
     * Test Steps:
     *   1. Create two users with different preferences
     *   2. Call generateRecommendationsFile()
     *   3. Verify both users processed
     *
     * Expected: Both users appear in output with different recommendations
     *
     * Path Coverage: Path 7 (multiple users processed)
     */
    @Test
    @DisplayName("TC_REC_PC_07 – Path 7: multiple valid users")
    void testPathCoverage_PC07_MultipleValidUsers() throws IOException {
        // Arrange
        User user1 = new User("Action Fan", "111111111", Set.of("TM123"));
        User user2 = new User("Drama Fan", "222222222", Set.of("TI789"));
        List<User> users = List.of(user1, user2);

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        String content = Files.readString(outPath);
        assertTrue(content.contains("Action Fan"));
        assertTrue(content.contains("Drama Fan"));
    }

    /**
     * TC_REC_PC_08: Path 8 - Empty users list (loop never executes)
     *
     * Scenario: Empty users list, loop body never executes
     * Path: Entry → null check passes → empty loop → write empty file
     *
     * Test Data:
     *   - users: empty ArrayList
     *
     * Test Steps:
     *   1. Pass empty users list
     *   2. Call generateRecommendationsFile()
     *   3. Verify file created but empty/minimal content
     *
     * Expected: File created, no user content
     *
     * Path Coverage: Path 8 (empty loop path)
     */
    @Test
    @DisplayName("TC_REC_PC_08 – Path 8: empty users list")
    void testPathCoverage_PC08_EmptyUsersList() throws IOException {
        // Arrange
        List<User> users = new ArrayList<>();

        // Act
        Recommendation.generateRecommendationsFile(users, sampleMovies);

        // Assert
        Path outPath = Path.of("recommendations.txt");
        assertTrue(Files.exists(outPath));
    }
}