import logic.MovieFileParser;
import model.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @TempDir
    Path tempDir;

    private MovieFileParser movieParser;

    @BeforeEach
    public void setUp() {
        movieParser = new MovieFileParser();
    }

    // ===================== VALID FILE TESTS =====================
    @Test
    @DisplayName("Test reading a valid file with a single movie")
    public void testReadMovies_ValidSingleMovie() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Drama, Thriller
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertEquals("The Dark Knight", movies.getFirst().title());
        assertEquals("TDK123", movies.getFirst().id());
        assertArrayEquals(new String[]{"Action", "Drama", "Thriller"}, movies.getFirst().genres());
    }

    @Test
    @DisplayName("Test reading a valid file with multiple movies")
    public void testReadMovies_ValidMultipleMovies() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Drama, Thriller
                        Inception, I456
                        Sci-Fi, Thriller
                        The Matrix, TM789
                        Action, Sci-Fi
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(3, movies.size());

        // Verify first movie
        assertEquals("The Dark Knight", movies.getFirst().title());
        assertEquals("TDK123", movies.get(0).id());
        assertArrayEquals(new String[]{"Action", "Drama", "Thriller"}, movies.get(0).genres());

        // Verify second movie
        assertEquals("Inception", movies.get(1).title());
        assertEquals("I456", movies.get(1).id());

        // Verify third movie
        assertEquals("The Matrix", movies.get(2).title());
        assertEquals("TM789", movies.get(2).id());
    }

    @Test
    @DisplayName("Test reading valid file with blank lines between movies")
    public void testReadMovies_ValidWithBlankLines() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        
                        The Dark Knight, TDK123
                        Action, Drama, Thriller
                        
                        
                        Inception, I456
                        Sci-Fi, Thriller
                        
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(2, movies.size());
        assertEquals("The Dark Knight", movies.get(0).title());
        assertEquals("Inception", movies.get(1).title());
    }

    @Test
    @DisplayName("Test reading valid file with extra whitespace")
    public void testReadMovies_ExtraWhitespace() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                          The Dark Knight  ,  TDK123 \s
                          Action  ,  Drama  ,  Thriller \s
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertEquals("The Dark Knight", movies.getFirst().title());
        assertEquals("TDK123", movies.getFirst().id());
        assertArrayEquals(new String[]{"Action", "Drama", "Thriller"}, movies.getFirst().genres());
    }

    @Test
    @DisplayName("Test reading movie with single genre")
    public void testReadMovies_SingleGenre() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertArrayEquals(new String[]{"Action"}, movies.getFirst().genres());
    }

    @Test
    @DisplayName("Test reading movie with many genres")
    public void testReadMovies_MultipleGenres() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Adventure, Crime, Drama, Thriller
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertArrayEquals(new String[]{"Action", "Adventure", "Crime", "Drama", "Thriller"},
                movies.getFirst().genres());
    }

    @Test
    @DisplayName("Test reading movie with single word title")
    public void testReadMovies_SingleWordTitle() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        Inception, I456
                        Sci-Fi, Thriller
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertEquals("Inception", movies.getFirst().title());
        assertEquals("I456", movies.getFirst().id());
    }

    // ===================== EMPTY FILE TESTS =====================
    @Test
    @DisplayName("Test reading completely empty file")
    public void testReadMovies_EmptyFile() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile, "");

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(0, movies.size());
        assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("Test reading file with only blank lines")
    public void testReadMovies_OnlyBlankLines() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile, "\n\n\n\n\n");

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(0, movies.size());
        assertTrue(movies.isEmpty());
    }

    @Test
    @DisplayName("Test reading file with only whitespace")
    public void testReadMovies_OnlyWhitespace() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile, "   \n  \n    \n");

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(0, movies.size());
        assertTrue(movies.isEmpty());
    }

    // ===================== MISSING LINES TESTS =====================
    @Test
    @DisplayName("Test missing genres line for single movie")
    public void testReadMovies_MissingGenresLine() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                "The Dark Knight, TDK123\n");

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Genres missing for movie"));
        assertTrue(exception.getMessage().contains("The Dark Knight"));
    }

    @Test
    @DisplayName("Test missing genres line for last movie in multiple movies")
    public void testReadMovies_LastMovieMissingGenres() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Drama
                        Inception, I456
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Genres missing for movie"));
        assertTrue(exception.getMessage().contains("Inception"));
    }

    // ===================== WRONG FORMAT TESTS =====================
    @Test
    @DisplayName("Test wrong format - missing comma separator")
    public void testReadMovies_WrongFormatMissingComma() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight TDK123
                        Action, Drama
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong movie line format"));
        assertTrue(exception.getMessage().contains("The Dark Knight TDK123"));
    }

    @Test
    @DisplayName("Test wrong format - too many comma-separated fields")
    public void testReadMovies_WrongFormatTooManyFields() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123, Extra Field
                        Action, Drama
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong movie line format"));
    }

    @Test
    @DisplayName("Test wrong format - only movie title without ID")
    public void testReadMovies_WrongFormatOnlyTitle() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight
                        Action, Drama
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong movie line format"));
    }

    @Test
    @DisplayName("Test wrong format - only comma without title and ID")
    public void testReadMovies_WrongFormatOnlyComma() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        ,
                        Action, Drama
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong movie line format"));
    }

    @Test
    @DisplayName("Test wrong format in second movie")
    public void testReadMovies_WrongFormatSecondMovie() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Drama
                        Inception I456
                        Sci-Fi
                        """);

        Exception exception = assertThrows(Exception.class, () -> {
            movieParser.readMovies(movieFile.toString());
        });

        assertTrue(exception.getMessage().contains("Wrong movie line format"));
        assertTrue(exception.getMessage().contains("Inception I456"));
    }

    // ===================== FILE NOT FOUND TEST =====================
    @Test
    @DisplayName("Test reading non-existent file")
    public void testReadMovies_FileNotFound() {
        assertThrows(Exception.class, () -> {
            movieParser.readMovies("nonexistent_file_path.txt");
        });
    }

    @Test
    @DisplayName("Test reading from null path")
    public void testReadMovies_NullPath() {
        assertThrows(Exception.class, () -> {
            movieParser.readMovies(null);
        });
    }

    // ===================== EDGE CASES TESTS =====================
    @Test
    @DisplayName("Test movie with very long title")
    public void testReadMovies_LongTitle() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        String longTitle = "The Quick Brown Fox Jumps Over The Lazy Dog Every Single Day";
        Files.writeString(movieFile,
                longTitle + ", TQBFJOTLDESD123\n" +
                        "Drama\n");

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertEquals(longTitle, movies.getFirst().title());
    }

    @Test
    @DisplayName("Test genres line is empty but present")
    public void testReadMovies_EmptyGenresLine() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(1, movies.size());
        assertEquals(1, movies.getFirst().genres().length);
        assertEquals("", movies.getFirst().genres()[0]);
    }

    @Test
    @DisplayName("Test mixed valid and handling multiple movies correctly")
    public void testReadMovies_ComplexValidFile() throws Exception {
        Path movieFile = tempDir.resolve("movies.txt");
        Files.writeString(movieFile,
                """
                        The Dark Knight, TDK123
                        Action, Drama, Thriller
                        
                        Inception, I456
                        Sci-Fi
                        The Matrix, TM789
                        Action, Sci-Fi, Thriller
                        
                        Pulp Fiction, PF012
                        Crime, Drama
                        """);

        List<Movie> movies = movieParser.readMovies(movieFile.toString());

        assertEquals(4, movies.size());
        assertEquals("The Dark Knight", movies.get(0).title());
        assertEquals("Inception", movies.get(1).title());
        assertEquals("The Matrix", movies.get(2).title());
        assertEquals("Pulp Fiction", movies.get(3).title());
    }
}