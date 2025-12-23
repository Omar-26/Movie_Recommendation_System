package whiteBox;

import logic.MovieFileParser;
import model.Movie;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * White-Box Testing for MovieFileParser.java
 *
 * This test class covers the following method:
 * - readMovies(String filePath)
 *
 * Coverage Types:
 * - Statement Coverage: Tests that execute each statement at least once
 * - Branch Coverage: Tests that cover all decision branches (true/false)
 * - Path Coverage: Tests that cover all possible execution paths
 *
 * Method Under Test: readMovies(String filePath)
 * -----------------------------------------------
 * This method reads a file containing movie data in the format:
 *   Line 1: MovieTitle,MovieId
 *   Line 2: Genre1,Genre2,Genre3,...
 *
 * Key Decision Points:
 * 1. line.trim().isEmpty() → skip blank lines
 * 2. titleAndId.length != 2 → throw Exception for wrong format
 * 3. genresLine == null → throw Exception for missing genres
 *
 * @author Testing Team
 * @version 1.0
 */
@DisplayName("MovieFileParser – White-Box Coverage Tests")
public class MovieFileTest {

    @TempDir
    Path tempDir;

    private MovieFileParser movieParser;

    // ===================== SETUP =====================

    /**
     * Initializes a fresh MovieFileParser instance before each test.
     * Ensures test isolation and clean state.
     */
    @BeforeEach
    void setUp() {
        movieParser = new MovieFileParser();
    }

    // ===================== STATEMENT COVERAGE TEST CASES =====================
    // These tests ensure each line of code is executed at least once
    // Target: Execute all 15 statements in readMovies() method

    /**
     * TC_MOV_01: Normal valid movie file with single movie
     *
     * Scenario: Parse a properly formatted movie file with one movie entry
     * Test Data:
     *   - File content: "The Matrix,TM123\nAction,Sci-Fi"
     *
     * Test Steps:
     *   1. Create temp file with valid movie data
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify returned list contains correct movie
     *
     * Expected: Returns list with 1 Movie object containing correct data
     *
     * Statement Coverage: 13/15 statements executed
     * - Covers: file reading, line parsing, splitting, array processing, object creation
     */
    @Test
    @DisplayName("TC_MOV_01 – Statement: valid single movie file")
    void testStatementCoverage_TC01_ValidSingleMovie() throws Exception {
        // Arrange: Create file with valid movie data
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                The Matrix,TM123
                Action,Sci-Fi
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Verify correct parsing
        assertEquals(1, movies.size(), "Should parse exactly 1 movie");
        assertEquals("The Matrix", movies.get(0).title(), "Movie title should match");
        assertEquals("TM123", movies.get(0).id(), "Movie ID should match");
        assertArrayEquals(new String[]{"Action", "Sci-Fi"}, movies.get(0).genres(),
                "Genres should match");
    }

    /**
     * TC_MOV_02: Movie file with multiple movies
     *
     * Scenario: Parse a file containing multiple movie entries
     * Test Data:
     *   - File content: Two complete movie entries
     *
     * Test Steps:
     *   1. Create temp file with multiple movies
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify all movies are parsed correctly
     *
     * Expected: Returns list with all Movie objects
     *
     * Statement Coverage: 13/15 statements (while loop iterations)
     */
    @Test
    @DisplayName("TC_MOV_02 – Statement: valid multiple movies file")
    void testStatementCoverage_TC02_ValidMultipleMovies() throws Exception {
        // Arrange: Create file with multiple movies
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                The Matrix,TM123
                Action,Sci-Fi
                Inception,IN456
                Action,Thriller
                Titanic,TI789
                Drama,Romance
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Verify all movies parsed
        assertEquals(3, movies.size(), "Should parse exactly 3 movies");
        assertEquals("The Matrix", movies.get(0).title());
        assertEquals("Inception", movies.get(1).title());
        assertEquals("Titanic", movies.get(2).title());
    }

    /**
     * TC_MOV_03: Movie file with blank lines (should be skipped)
     *
     * Scenario: Parse a file containing blank lines between entries
     * Test Data:
     *   - File content: Movie entries with blank lines
     *
     * Test Steps:
     *   1. Create temp file with blank lines
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify blank lines are skipped
     *
     * Expected: Returns correct movies, blank lines ignored
     *
     * Statement Coverage: 14/15 statements (includes isEmpty check → true branch)
     */
    @Test
    @DisplayName("TC_MOV_03 – Statement: file with blank lines skipped")
    void testStatementCoverage_TC03_FileWithBlankLines() throws Exception {
        // Arrange: Create file with blank lines
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                
                The Matrix,TM123
                Action,Sci-Fi
                
                Inception,IN456
                Action,Thriller
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Blank lines should be skipped
        assertEquals(2, movies.size(), "Should parse 2 movies, ignoring blank lines");
        assertEquals("The Matrix", movies.get(0).title());
        assertEquals("Inception", movies.get(1).title());
    }

    /**
     * TC_MOV_04: Wrong movie line format (missing comma)
     *
     * Scenario: Parse a file with invalid movie line format
     * Test Data:
     *   - File content: "TheMatrix-TM123\nAction,Sci-Fi" (dash instead of comma)
     *
     * Test Steps:
     *   1. Create temp file with invalid format
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify exception is thrown
     *
     * Expected: Throws Exception with "Wrong movie line format" message
     *
     * Statement Coverage: 15/15 statements (includes exception throw)
     */
    @Test
    @DisplayName("TC_MOV_04 – Statement: wrong line format throws exception")
    void testStatementCoverage_TC04_WrongLineFormat() {
        // Arrange: Create file with invalid format
        Path movieFile = tempDir.resolve("movies.txt");

        Exception exception = assertThrows(Exception.class, () -> {
            Files.writeString(movieFile,
                    """
                    TheMatrix-TM123
                    Action,Sci-Fi
                    """);
            movieParser.readMovies(movieFile.toString());
        });

        // Assert: Correct exception message
        assertTrue(exception.getMessage().toLowerCase().contains("wrong") ||
                        exception.getMessage().toLowerCase().contains("format"),
                "Should indicate wrong format in exception message");
    }

    /**
     * TC_MOV_05: Missing genres line for a movie
     *
     * Scenario: Parse a file where genres line is missing
     * Test Data:
     *   - File content: Only title line, no genres line
     *
     * Test Steps:
     *   1. Create temp file with missing genres
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify exception is thrown
     *
     * Expected: Throws Exception with "Genres missing" message
     *
     * Statement Coverage: 15/15 statements (includes genres null check → throw)
     */
    @Test
    @DisplayName("TC_MOV_05 – Statement: missing genres throws exception")
    void testStatementCoverage_TC05_MissingGenres() {
        // Arrange: Create file with missing genres
        Path movieFile = tempDir.resolve("movies.txt");

        Exception exception = assertThrows(Exception.class, () -> {
            Files.writeString(movieFile,
                    """
                    The Matrix,TM123
                    """);
            movieParser.readMovies(movieFile.toString());
        });

        // Assert: Correct exception message
        assertTrue(exception.getMessage().toLowerCase().contains("genres") ||
                        exception.getMessage().toLowerCase().contains("missing"),
                "Should indicate missing genres in exception message");
    }

    /**
     * TC_MOV_06: Empty file
     *
     * Scenario: Parse an empty movie file
     * Test Data:
     *   - File content: Empty string
     *
     * Test Steps:
     *   1. Create empty temp file
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify empty list returned
     *
     * Expected: Returns empty list, no exception
     *
     * Statement Coverage: 5/15 statements (while loop never enters)
     */
    @Test
    @DisplayName("TC_MOV_06 – Statement: empty file returns empty list")
    void testStatementCoverage_TC06_EmptyFile() throws Exception {
        // Arrange: Create empty file
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile, "");

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Should return empty list
        assertTrue(movies.isEmpty(), "Empty file should return empty list");
    }

    /**
     * TC_MOV_07: Movie with single genre
     *
     * Scenario: Parse a movie with only one genre
     * Test Data:
     *   - File content: "Action Movie,AM123\nAction"
     *
     * Test Steps:
     *   1. Create temp file with single genre movie
     *   2. Call movieParser.readMovies(filePath)
     *   3. Verify genres array has one element
     *
     * Expected: Returns movie with single genre in array
     *
     * Statement Coverage: Covers genres splitting with single element
     */
    @Test
    @DisplayName("TC_MOV_07 – Statement: single genre movie")
    void testStatementCoverage_TC07_SingleGenre() throws Exception {
        // Arrange: Create file with single genre
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Action Movie,AM123
                Action
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Single genre should be parsed
        assertEquals(1, movies.size());
        assertEquals(1, movies.get(0).genres().length, "Should have exactly 1 genre");
        assertEquals("Action", movies.get(0).genres()[0]);
    }

    // ===================== BRANCH COVERAGE TEST CASES =====================
    // These tests ensure all decision branches (true/false) are covered
    // Key branches: isEmpty check, titleAndId.length check, genresLine null check

    /**
     * TC_MOV_08: Branch - Line is not blank (isEmpty → false)
     *
     * Purpose: Cover the branch where line.trim().isEmpty() is false
     * Input: File with non-blank lines only
     * Expected: All lines processed normally
     *
     * Branch Coverage: line.trim().isEmpty() → false branch
     */
    @Test
    @DisplayName("TC_MOV_08 – Branch: non-blank lines processed")
    void testBranchCoverage_TC08_NonBlankLines() throws Exception {
        // Arrange: File with no blank lines
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Movie One,MO123
                Action,Drama
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Line should be processed (not skipped)
        assertEquals(1, movies.size(), "Non-blank line should be processed");
    }

    /**
     * TC_MOV_09: Branch - Line is blank (isEmpty → true, continue)
     *
     * Purpose: Cover the branch where line.trim().isEmpty() is true
     * Input: File with blank lines
     * Expected: Blank lines skipped via continue
     *
     * Branch Coverage: line.trim().isEmpty() → true branch (continue)
     */
    @Test
    @DisplayName("TC_MOV_09 – Branch: blank lines trigger continue")
    void testBranchCoverage_TC09_BlankLinesContinue() throws Exception {
        // Arrange: File with blank lines
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                
                
                Movie One,MO123
                Action
                
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Blank lines skipped
        assertEquals(1, movies.size(), "Blank lines should be skipped");
    }

    /**
     * TC_MOV_10: Branch - Title line has exactly 2 parts (length == 2 → valid)
     *
     * Purpose: Cover the branch where split produces 2 elements
     * Input: Properly formatted title line "Title,ID"
     * Expected: Processing continues normally
     *
     * Branch Coverage: titleAndId.length != 2 → false branch (no exception)
     */
    @Test
    @DisplayName("TC_MOV_10 – Branch: valid title line format passes")
    void testBranchCoverage_TC10_ValidTitleFormat() throws Exception {
        // Arrange: Valid format
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Valid Title,VT123
                Genre1,Genre2
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: No exception, movie parsed
        assertEquals(1, movies.size());
        assertEquals("Valid Title", movies.get(0).title());
    }

    /**
     * TC_MOV_11: Branch - Title line has wrong number of parts (length != 2 → throw)
     *
     * Purpose: Cover the branch where split produces != 2 elements
     * Input: Line with too many commas "Title,ID,Extra"
     * Expected: Exception thrown
     *
     * Branch Coverage: titleAndId.length != 2 → true branch (throw)
     */
    @Test
    @DisplayName("TC_MOV_11 – Branch: invalid title line format throws")
    void testBranchCoverage_TC11_InvalidTitleFormat() {
        // Arrange: Too many commas in title line
        Path movieFile = tempDir.resolve("movies.txt");

        Exception exception = assertThrows(Exception.class, () -> {
            Files.writeString(movieFile,
                    """
                    Title,ID,Extra
                    Genre
                    """);
            movieParser.readMovies(movieFile.toString());
        });

        // Assert: Exception thrown for wrong format
        assertNotNull(exception);
    }

    /**
     * TC_MOV_12: Branch - Genres line exists (genresLine != null → valid)
     *
     * Purpose: Cover the branch where genres line is present
     * Input: Complete movie entry with genres
     * Expected: Processing continues normally
     *
     * Branch Coverage: genresLine == null → false branch (no exception)
     */
    @Test
    @DisplayName("TC_MOV_12 – Branch: genres line present passes")
    void testBranchCoverage_TC12_GenresLinePresent() throws Exception {
        // Arrange: Complete entry
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Complete Movie,CM123
                Action,Comedy,Drama
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: No exception, genres parsed
        assertEquals(3, movies.get(0).genres().length);
    }

    /**
     * TC_MOV_13: Branch - Genres line missing (genresLine == null → throw)
     *
     * Purpose: Cover the branch where genres line is null (EOF)
     * Input: Title line only, no genres line
     * Expected: Exception thrown
     *
     * Branch Coverage: genresLine == null → true branch (throw)
     */
    @Test
    @DisplayName("TC_MOV_13 – Branch: missing genres line throws")
    void testBranchCoverage_TC13_GenresLineMissing() {
        // Arrange: No genres line
        Path movieFile = tempDir.resolve("movies.txt");

        Exception exception = assertThrows(Exception.class, () -> {
            Files.writeString(movieFile, "Title Only,TO123");
            movieParser.readMovies(movieFile.toString());
        });

        // Assert: Exception for missing genres
        assertTrue(exception.getMessage().contains("Genres") ||
                exception.getMessage().contains("missing"));
    }

    // ===================== PATH COVERAGE TEST CASES =====================
    // These tests cover different execution paths through the code

    /**
     * TC_MOV_14: Path - Happy path (all valid, multiple iterations)
     *
     * Purpose: Cover complete successful execution with loop iterations
     * Path: Open file → while(line != null) → !isEmpty → valid format →
     *       valid genres → add movie → repeat → return list
     *
     * Path Coverage: Full success path with multiple movies
     */
    @Test
    @DisplayName("TC_MOV_14 – Path: complete happy path multiple movies")
    void testPathCoverage_TC14_HappyPathMultiple() throws Exception {
        // Arrange: Multiple valid movies
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Movie A,MA123
                Action
                Movie B,MB456
                Drama,Romance
                Movie C,MC789
                Comedy,Family,Animation
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: All movies parsed correctly
        assertEquals(3, movies.size());
        assertEquals("Movie A", movies.get(0).title());
        assertEquals("Movie B", movies.get(1).title());
        assertEquals("Movie C", movies.get(2).title());
        assertEquals(3, movies.get(2).genres().length);
    }

    /**
     * TC_MOV_15: Path - Empty file path (while loop never executes)
     *
     * Purpose: Cover path where while loop condition is immediately false
     * Path: Open file → while(line != null) FALSE → return empty list
     *
     * Path Coverage: No iteration path
     */
    @Test
    @DisplayName("TC_MOV_15 – Path: empty file no iterations")
    void testPathCoverage_TC15_EmptyFileNoIterations() throws Exception {
        // Arrange: Empty file
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile, "");

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Empty list, no iterations
        assertTrue(movies.isEmpty());
    }

    /**
     * TC_MOV_16: Path - Blank line skip path
     *
     * Purpose: Cover path through blank line continue
     * Path: while → !isEmpty FALSE → continue → next iteration
     *
     * Path Coverage: Continue path within loop
     */
    @Test
    @DisplayName("TC_MOV_16 – Path: blank line continue path")
    void testPathCoverage_TC16_BlankLineContinuePath() throws Exception {
        // Arrange: Blank lines interspersed
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                
                Movie One,MO123
                Action
                
                
                Movie Two,MT456
                Drama
                
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Both movies parsed, blanks skipped
        assertEquals(2, movies.size());
    }

    /**
     * TC_MOV_17: Path - Exception path for invalid format
     *
     * Purpose: Cover path that leads to format exception
     * Path: while → !isEmpty TRUE → split → length != 2 TRUE → throw
     *
     * Path Coverage: Exception throw path
     */
    @Test
    @DisplayName("TC_MOV_17 – Path: exception path for format error")
    void testPathCoverage_TC17_ExceptionPathFormat() {
        // Arrange: Invalid format
        Path movieFile = tempDir.resolve("movies.txt");

        // Act & Assert: Exception thrown
        assertThrows(Exception.class, () -> {
            Files.writeString(movieFile, "NoCommaHere");
            movieParser.readMovies(movieFile.toString());
        });
    }

    /**
     * TC_MOV_18: Path - Exception path for missing genres
     *
     * Purpose: Cover path that leads to genres exception
     * Path: while → valid title → readLine returns null → throw
     *
     * Path Coverage: Genres null exception path
     */
    @Test
    @DisplayName("TC_MOV_18 – Path: exception path for missing genres")
    void testPathCoverage_TC18_ExceptionPathGenres() {
        // Arrange: Title without genres
        Path movieFile = tempDir.resolve("movies.txt");

        // Act & Assert: Exception thrown
        assertThrows(Exception.class, () -> {
            Files.writeString(movieFile, "Movie Title,MT123");
            movieParser.readMovies(movieFile.toString());
        });
    }

    /**
     * TC_MOV_19: Path - File not found exception
     *
     * Purpose: Cover path where file does not exist
     * Path: Open file throws FileNotFoundException
     *
     * Path Coverage: I/O exception path
     */
    @Test
    @DisplayName("TC_MOV_19 – Path: file not found exception")
    void testPathCoverage_TC19_FileNotFound() {
        // Arrange: Non-existent file path
        String nonExistentPath = tempDir.resolve("nonexistent.txt").toString();

        // Act & Assert: Exception thrown
        assertThrows(Exception.class, () -> {
            movieParser.readMovies(nonExistentPath);
        });
    }

    /**
     * TC_MOV_20: Path - Whitespace trimming in genres
     *
     * Purpose: Cover genres trimming functionality
     * Path: Split genres → map(String::trim) → toArray
     *
     * Path Coverage: Genres processing with whitespace
     */
    @Test
    @DisplayName("TC_MOV_20 – Path: genres whitespace trimmed")
    void testPathCoverage_TC20_GenresWhitespaceTrimmed() throws Exception {
        // Arrange: Genres with extra whitespace
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                Movie With Spaces,MW123
                  Action  ,  Drama  ,  Comedy
                """);

        // Act: Parse the movie file
        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        // Assert: Genres should be trimmed
        assertEquals(3, movies.get(0).genres().length);
        assertEquals("Action", movies.get(0).genres()[0]);
        assertEquals("Drama", movies.get(0).genres()[1]);
        assertEquals("Comedy", movies.get(0).genres()[2]);
    }
}