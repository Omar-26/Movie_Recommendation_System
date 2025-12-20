package blackBox.cause_effect;

import logic.Recommendation;
import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - Cause-Effect Graph for Recommendation class
 * Tests cause-effect relationships and constraints
 */
public class RecommendationCauseEffectTest {
    private List<Movie> movieList;
    private Set<String> watchedMovies;

    @BeforeEach
    void setUp() {
        movieList = new ArrayList<>();
        movieList.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movieList.add(new Movie("Action2", "A002", new String[]{"Action"}));
        movieList.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        
        watchedMovies = new HashSet<>();
    }

    //------- Cause-Effect: recommendMovies() -------//
    // Cause C1 → Effect E1 (null watched movies causes empty result)
    @Test
    public void testRecommend_CE_NullWatchedCausesEmpty() {
        Set<String> result = Recommendation.recommendMovies(null, movieList);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C2 → Effect E1 (empty watched movies causes empty result)
    @Test
    public void testRecommend_CE_EmptyWatchedCausesEmpty() {
        Set<String> result = Recommendation.recommendMovies(new HashSet<>(), movieList);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C3 → Effect E1 (null movie list causes empty result)
    @Test
    public void testRecommend_CE_NullMoviesCausesEmpty() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, null);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C4 → Effect E1 (empty movie list causes empty result)
    @Test
    public void testRecommend_CE_EmptyMoviesCausesEmpty() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, new ArrayList<>());
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // No causes → Effect E2 (valid inputs with genre overlap cause recommendations)
    @Test
    public void testRecommend_CE_NoCausesCausesRecommendations() {
        watchedMovies.add("A001"); // Watched Action1
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.isEmpty()); // Effect: Recommendations
        assertTrue(result.contains("Action2")); // Same genre
    }

    //------- Cause-Effect: Genre Matching Logic -------//
    // Cause C1 AND C2 AND NOT C3 → Effect E1 (add to recommendations)
    @Test
    public void testGenreMatch_CE_MatchingGenreCausesRecommendation() {
        watchedMovies.add("A001"); // Has Action genre
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertTrue(result.contains("Action2")); // Effect: Recommended (matching genre, not watched)
    }

    // Cause C3 → Effect E2 (watched movie not recommended)
    @Test
    public void testGenreMatch_CE_WatchedMovieNotRecommended() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.contains("Action1")); // Effect: Not recommended (already watched)
    }

    @Test
    public void testGenreMatch_CE_NoMatchingGenreNotRecommended() {
        watchedMovies.add("A001"); // Action genre
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.contains("Drama1")); // Effect: Not recommended (different genre)
    }

    @Test
    public void testEmptyGenres_CE_WatchedNoGenresCausesEmpty() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("NoGenre", "NG001", new String[]{}));
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        
        watchedMovies.add("NG001"); // No genres
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.isEmpty()); // Effect: Empty (no genres to match)
    }
}
