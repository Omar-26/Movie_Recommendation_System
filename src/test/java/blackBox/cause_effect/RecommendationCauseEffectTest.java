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
    // Causes: C1=WatchedMovies is null/empty, C2=MovieList is null/empty, C3=No genre overlap
    // Effects: E1=Return empty set, E2=Return recommendations
    // Constraints: If C1 OR C2 then E1 (skip genre matching)

    // Cause C1 → Effect E1 (null watched movies causes empty result)
    @Test
    public void testRecommend_CE_NullWatchedCausesEmpty() {
        Set<String> result = Recommendation.recommendMovies(null, movieList);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C1 → Effect E1 (empty watched movies causes empty result)
    @Test
    public void testRecommend_CE_EmptyWatchedCausesEmpty() {
        Set<String> result = Recommendation.recommendMovies(new HashSet<>(), movieList);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C2 → Effect E1 (null movie list causes empty result)
    @Test
    public void testRecommend_CE_NullMoviesCausesEmpty() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, null);
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C2 → Effect E1 (empty movie list causes empty result)
    @Test
    public void testRecommend_CE_EmptyMoviesCausesEmpty() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, new ArrayList<>());
        assertTrue(result.isEmpty()); // Effect: Empty set
    }

    // Cause C3 → Effect E1 (no genre overlap causes empty result)
    @Test
    public void testRecommend_CE_NoGenreOverlapCausesEmpty() {
        watchedMovies.add("XXX999"); // Non-existent movie
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertTrue(result.isEmpty()); // Effect: Empty set (no genres extracted)
    }

    // No causes → Effect E2 (valid inputs with genre overlap cause recommendations)
    @Test
    public void testRecommend_CE_NoCausesCausesRecommendations() {
        watchedMovies.add("A001"); // Watched Action1
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.isEmpty()); // Effect: Recommendations
        assertTrue(result.contains("Action2")); // Same genre
    }

    // Constraint: C1 takes precedence (null/empty watched checked first)
    @Test
    public void testRecommend_CE_NullWatchedTakesPrecedence() {
        Set<String> result = Recommendation.recommendMovies(null, null);
        assertTrue(result.isEmpty()); // Both null, but watched checked first
    }

    //------- Cause-Effect: Genre Matching Logic -------//
    // Causes: C1=Watched movie has genres, C2=Unwatched movie has matching genre, C3=Movie already watched
    // Effects: E1=Add to recommendations, E2=Don't add to recommendations
    // Constraints: If C3 then E2 (watched movies excluded)

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

    // Cause NOT C2 → Effect E2 (no matching genre, not recommended)
    @Test
    public void testGenreMatch_CE_NoMatchingGenreNotRecommended() {
        watchedMovies.add("A001"); // Action genre
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.contains("Drama1")); // Effect: Not recommended (different genre)
    }

    // Constraint: Watched movies always excluded (C3 constraint)
    @Test
    public void testGenreMatch_CE_WatchedAlwaysExcluded() {
        watchedMovies.add("A001");
        watchedMovies.add("A002");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        assertFalse(result.contains("Action1")); // Excluded
        assertFalse(result.contains("Action2")); // Excluded
    }

    //------- Cause-Effect: Multiple Genres -------//
    // Causes: C1=Watched has genre A, C2=Watched has genre B, C3=Unwatched has genre A or B
    // Effects: E1=Recommend, E2=Don't recommend
    // Constraints: If C1 OR C2 then genres union is used

    // Cause C1 → genres include A
    @Test
    public void testMultiGenre_CE_WatchedGenreACausesMatch() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Action2", "A002", new String[]{"Action"}));
        
        watchedMovies.add("A001"); // Genre A
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        assertTrue(result.contains("Action2")); // Effect: Recommended (genre A match)
    }

    // Cause C1 AND C2 → genres include A and B
    @Test
    public void testMultiGenre_CE_MultipleWatchedCausesUnion() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Action2", "A002", new String[]{"Action"}));
        movies.add(new Movie("Drama2", "D002", new String[]{"Drama"}));
        
        watchedMovies.add("A001"); // Genre A (Action)
        watchedMovies.add("D001"); // Genre B (Drama)
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.contains("Action2")); // Genre A match
        assertTrue(result.contains("Drama2")); // Genre B match
    }

    // Cause C3 with genre A or B → Effect E1
    @Test
    public void testMultiGenre_CE_UnwatchedMatchesEitherGenre() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Mixed", "M001", new String[]{"Action", "Drama"}));
        
        watchedMovies.add("A001"); // Genre Action
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.contains("Mixed")); // Effect: Recommended (has Action)
    }

    //------- Cause-Effect: Empty Genres -------//
    // Causes: C1=Watched movie has no genres, C2=Unwatched movie has no genres
    // Effects: E1=No recommendations, E2=Not recommended
    // Constraints: If C1 then no genres extracted

    // Cause C1 → Effect E1 (watched with no genres causes no recommendations)
    @Test
    public void testEmptyGenres_CE_WatchedNoGenresCausesEmpty() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("NoGenre", "NG001", new String[]{}));
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        
        watchedMovies.add("NG001"); // No genres
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.isEmpty()); // Effect: Empty (no genres to match)
    }

    // Cause C2 → Effect E2 (unwatched with no genres not recommended)
    @Test
    public void testEmptyGenres_CE_UnwatchedNoGenresNotRecommended() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("NoGenre", "NG001", new String[]{}));
        
        watchedMovies.add("A001"); // Action genre
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertFalse(result.contains("NoGenre")); // Effect: Not recommended (no genres)
    }

    //------- Cause-Effect: Recommendation Independence -------//
    // Test that recommendations for different users are independent

    // Different watched movies cause different recommendations
    @Test
    public void testCE_DifferentWatchedCausesDifferentRecommendations() {
        Set<String> watched1 = new HashSet<>();
        watched1.add("A001"); // Action
        
        Set<String> watched2 = new HashSet<>();
        watched2.add("D001"); // Drama
        
        Set<String> result1 = Recommendation.recommendMovies(watched1, movieList);
        Set<String> result2 = Recommendation.recommendMovies(watched2, movieList);
        
        // Different recommendations
        assertTrue(result1.contains("Action2"));
        assertFalse(result1.contains("Drama1"));
        
        assertFalse(result2.contains("Action2"));
        // Drama1 is watched, so not in result2
    }

    // Same watched movies cause same recommendations
    @Test
    public void testCE_SameWatchedCausesSameRecommendations() {
        Set<String> watched1 = new HashSet<>();
        watched1.add("A001");
        
        Set<String> watched2 = new HashSet<>();
        watched2.add("A001");
        
        Set<String> result1 = Recommendation.recommendMovies(watched1, movieList);
        Set<String> result2 = Recommendation.recommendMovies(watched2, movieList);
        
        assertEquals(result1, result2); // Same recommendations
    }
}
