package blackBox.ecp_bva;

import logic.MovieFileParser;
import model.Movie;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MovieFileParserECPBVATest {

    // BVA is not applicable

    // ECP:
    @Test
    void MP_ECP1_valid_blankLinesSkipped(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("movies.txt");

        Files.write(file, List.of(
                "The Matrix, TM123",
                "Action, Science Fiction",
                "",
                "Inception, IN456",
                "Science Fiction, Thriller"
        ));

        MovieFileParser parser = new MovieFileParser();
        List<Movie> movies = parser.readMovies(file.toString());

        assertEquals(2, movies.size());
        assertEquals("The Matrix", movies.get(0).title());
        assertEquals("TM123", movies.get(0).id());
        assertArrayEquals(
                new String[]{"Action", "Science Fiction"},
                movies.get(0).genres()
        );
        assertEquals("Inception", movies.get(1).title());
        assertEquals("IN456", movies.get(1).id());
        assertArrayEquals(
                new String[]{"Science Fiction", "Thriller"},
                movies.get(1).genres()
        );
    }

    @Test
    void MP_ECP2_invalid_missingCommaInMovieLine(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("movies.txt");

        Files.write(file, List.of(
                "The Matrix TM123",
                "Action, Science Fiction"
        ));

        MovieFileParser parser = new MovieFileParser();

        try {
            parser.readMovies(file.toString());
            fail("Exception expected due to wrong movie line format");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ERROR: Wrong movie line format"));
        }
    }

    @Test
    void MP_ECP3_invalid_tooManyFields(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("movies.txt");

        Files.write(file, List.of(
                "The Matrix, TM123, EXTRA",
                "Action, Science Fiction"
        ));

        MovieFileParser parser = new MovieFileParser();

        try {
            parser.readMovies(file.toString());
            fail("Exception expected due to wrong movie line format");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ERROR: Wrong movie line format"));
        }
    }

    @Test
    void MP_ECP4_invalid_missingGenresLine(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("movies.txt");

        Files.write(file, List.of(
                "The Matrix, TM123"
        ));

        MovieFileParser parser = new MovieFileParser();

        try {
            parser.readMovies(file.toString());
            fail("Exception expected due to missing genres line");
        } catch (Exception ex) {
            assertTrue(ex.getMessage().contains("ERROR: Genres missing for movie"));
        }
    }
}
