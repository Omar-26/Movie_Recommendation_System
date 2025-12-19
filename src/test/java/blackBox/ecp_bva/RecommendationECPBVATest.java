package blackBox.ecp_bva;

import logic.Recommendation;
import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationECPBVATest {

    private List<Movie> movies;

    // ECP:
    @BeforeEach
    void setUp() {
        movies = List.of(
                new Movie("The Matrix", "TM123", new String[]{"Action", "Science Fiction"}),
                new Movie("John Wick", "JW456", new String[]{"Action"}),
                new Movie("Inception", "IN789", new String[]{"Science Fiction", "Thriller"}),
                new Movie("The Notebook", "TN321", new String[]{"Drama", "Romance"})
        );
    }

    @Test
    void TC_R1_valid_recommendationsExist() {
        Set<String> watchedMovies = Set.of("TM123");

        Set<String> actual = Recommendation.recommendMovies(watchedMovies, movies);

        Set<String> expected = Set.of("John Wick", "Inception");
        assertEquals(expected, actual);
    }

    @Test
    void TC_R2_invalid_watchedMoviesEmpty() {
        Set<String> watchedMovies = Set.of();

        Set<String> actual = Recommendation.recommendMovies(watchedMovies, movies);

        assertTrue(actual.isEmpty());
    }

    @Test
    void TC_R3_invalid_moviesEmpty() {
        Set<String> watchedMovies = Set.of("TM123");
        List<Movie> emptyMovies = List.of();

        Set<String> actual = Recommendation.recommendMovies(watchedMovies, emptyMovies);

        assertTrue(actual.isEmpty());
    }
}
