import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - ECP & BVA for Recommendation class
 * Tests equivalence class partitioning and boundary value analysis
 */
public class RecommendationECPBVATest {
    private List<Movie> movieList;
    private Set<String> watchedMovies;

    @BeforeEach
    void setUp() {
        // Setup movie list with various genres
        movieList = new ArrayList<>();
        movieList.add(new Movie("Inception", "I001", new String[]{"Action", "Sci-Fi"}));
        movieList.add(new Movie("The Matrix", "TM002", new String[]{"Action", "Sci-Fi"}));
        movieList.add(new Movie("Titanic", "T003", new String[]{"Romance", "Drama"}));
        movieList.add(new Movie("The Notebook", "TN004", new String[]{"Romance", "Drama"}));
        movieList.add(new Movie("Interstellar", "I005", new String[]{"Sci-Fi", "Drama"}));
        movieList.add(new Movie("The Godfather", "TG006", new String[]{"Crime", "Drama"}));

        watchedMovies = new HashSet<>();
    }

    //------- recommendMovies() - ECP & BVA Tests -------//

    // ECP: Valid equivalence class - normal case with matches
    @Test
    public void testRecommendMovies_ECP_ValidWithMatches() {
        watchedMovies.add("I001"); // Watched Inception (Action, Sci-Fi)
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Should recommend movies with Action or Sci-Fi genres, excluding watched
        assertTrue(recommendations.contains("The Matrix"));
        assertTrue(recommendations.contains("Interstellar"));
        assertFalse(recommendations.contains("Inception")); // Already watched
        assertFalse(recommendations.contains("Titanic")); // No matching genres
    }

    @Test
    public void testRecommendMovies_ECP_ValidMultipleWatched() {
        watchedMovies.add("I001"); // Action, Sci-Fi
        watchedMovies.add("T003"); // Romance, Drama
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Should recommend based on combined genres
        assertTrue(recommendations.contains("The Matrix")); // Action, Sci-Fi
        assertTrue(recommendations.contains("The Notebook")); // Romance, Drama
        assertTrue(recommendations.contains("Interstellar")); // Sci-Fi, Drama
        assertTrue(recommendations.contains("The Godfather")); // Drama
    }

    // BVA: Boundary - single watched movie
    @Test
    public void testRecommendMovies_BVA_SingleWatchedMovie() {
        watchedMovies.add("T003"); // Titanic (Romance, Drama)
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertTrue(recommendations.contains("The Notebook")); // Romance, Drama
        assertTrue(recommendations.contains("Interstellar")); // Drama
        assertTrue(recommendations.contains("The Godfather")); // Drama
        assertFalse(recommendations.contains("Titanic")); // Already watched
    }

    // BVA: Boundary - all movies watched
    @Test
    public void testRecommendMovies_BVA_AllMoviesWatched() {
        watchedMovies.add("I001");
        watchedMovies.add("TM002");
        watchedMovies.add("T003");
        watchedMovies.add("TN004");
        watchedMovies.add("I005");
        watchedMovies.add("TG006");
        
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // No recommendations since all movies are watched
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Invalid equivalence class - null watched movies
    @Test
    public void testRecommendMovies_ECP_NullWatchedMovies() {
        Set<String> recommendations = Recommendation.recommendMovies(null, movieList);
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Invalid equivalence class - empty watched movies
    @Test
    public void testRecommendMovies_ECP_EmptyWatchedMovies() {
        Set<String> recommendations = Recommendation.recommendMovies(new HashSet<>(), movieList);
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Invalid equivalence class - null movie list
    @Test
    public void testRecommendMovies_ECP_NullMovieList() {
        watchedMovies.add("I001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, null);
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Invalid equivalence class - empty movie list
    @Test
    public void testRecommendMovies_ECP_EmptyMovieList() {
        watchedMovies.add("I001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, new ArrayList<>());
        assertTrue(recommendations.isEmpty());
    }

    // BVA: Boundary - watched movie not in list
    @Test
    public void testRecommendMovies_BVA_WatchedMovieNotInList() {
        watchedMovies.add("XXX999"); // Non-existent movie
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // No genres extracted, so no recommendations
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Valid equivalence class - no genre overlap
    @Test
    public void testRecommendMovies_ECP_NoGenreOverlap() {
        // Create a movie list with no overlapping genres
        List<Movie> uniqueGenreMovies = new ArrayList<>();
        uniqueGenreMovies.add(new Movie("Action Movie", "AM001", new String[]{"Action"}));
        uniqueGenreMovies.add(new Movie("Horror Movie", "HM002", new String[]{"Horror"}));
        uniqueGenreMovies.add(new Movie("Comedy Movie", "CM003", new String[]{"Comedy"}));
        
        watchedMovies.add("AM001"); // Watched Action
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, uniqueGenreMovies);
        
        // No recommendations since no other movies have Action genre
        assertTrue(recommendations.isEmpty());
    }

    // BVA: Boundary - single movie in list
    @Test
    public void testRecommendMovies_BVA_SingleMovieInList() {
        List<Movie> singleMovie = new ArrayList<>();
        singleMovie.add(new Movie("Solo Movie", "SM001", new String[]{"Drama"}));
        
        watchedMovies.add("SM001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, singleMovie);
        
        // No recommendations since only movie is watched
        assertTrue(recommendations.isEmpty());
    }

    // ECP: Valid equivalence class - multiple genres per movie
    @Test
    public void testRecommendMovies_ECP_MultipleGenresPerMovie() {
        watchedMovies.add("I005"); // Interstellar (Sci-Fi, Drama)
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Should match movies with either Sci-Fi or Drama
        assertTrue(recommendations.contains("Inception")); // Sci-Fi
        assertTrue(recommendations.contains("The Matrix")); // Sci-Fi
        assertTrue(recommendations.contains("Titanic")); // Drama
        assertTrue(recommendations.contains("The Notebook")); // Drama
        assertTrue(recommendations.contains("The Godfather")); // Drama
    }

    // BVA: Boundary - movie with empty genres array
    @Test
    public void testRecommendMovies_BVA_EmptyGenresArray() {
        List<Movie> moviesWithEmptyGenres = new ArrayList<>();
        moviesWithEmptyGenres.add(new Movie("Movie1", "M001", new String[]{"Action"}));
        moviesWithEmptyGenres.add(new Movie("Movie2", "M002", new String[]{})); // Empty genres
        
        watchedMovies.add("M001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, moviesWithEmptyGenres);
        
        // Movie2 has no genres, so won't be recommended
        assertFalse(recommendations.contains("Movie2"));
    }

    // ECP: Valid equivalence class - case sensitivity check
    @Test
    public void testRecommendMovies_ECP_CaseSensitiveIds() {
        watchedMovies.add("i001"); // lowercase - should not match "I001"
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // No genres extracted since ID doesn't match (case-sensitive)
        assertTrue(recommendations.isEmpty());
    }

    // BVA: Boundary - large number of movies
    @Test
    public void testRecommendMovies_BVA_LargeMovieList() {
        List<Movie> largeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeList.add(new Movie("Movie" + i, "M" + String.format("%03d", i), new String[]{"Action"}));
        }
        
        watchedMovies.add("M000");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, largeList);
        
        // Should recommend all other 99 movies with Action genre
        assertEquals(99, recommendations.size());
    }

    // BVA: Boundary - large number of watched movies
    @Test
    public void testRecommendMovies_BVA_ManyWatchedMovies() {
        // Watch 5 out of 6 movies
        watchedMovies.add("I001");
        watchedMovies.add("TM002");
        watchedMovies.add("T003");
        watchedMovies.add("TN004");
        watchedMovies.add("I005");
        
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Should only recommend The Godfather if it matches genres
        assertTrue(recommendations.size() <= 1);
    }

    // ECP: Valid equivalence class - duplicate genres in watched movies
    @Test
    public void testRecommendMovies_ECP_DuplicateGenresInWatched() {
        watchedMovies.add("I001"); // Action, Sci-Fi
        watchedMovies.add("TM002"); // Action, Sci-Fi (duplicate genres)
        
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Should still work correctly with duplicate genres
        assertTrue(recommendations.contains("Interstellar")); // Sci-Fi
        assertFalse(recommendations.contains("Inception")); // Watched
        assertFalse(recommendations.contains("The Matrix")); // Watched
    }

    // BVA: Boundary - movie with single genre
    @Test
    public void testRecommendMovies_BVA_SingleGenreMovie() {
        List<Movie> singleGenreMovies = new ArrayList<>();
        singleGenreMovies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        singleGenreMovies.add(new Movie("Action2", "A002", new String[]{"Action"}));
        singleGenreMovies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        
        watchedMovies.add("A001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, singleGenreMovies);
        
        // Should only recommend Action2
        assertTrue(recommendations.contains("Action2"));
        assertFalse(recommendations.contains("Drama1"));
        assertEquals(1, recommendations.size());
    }

    // ECP: Valid equivalence class - recommendations return titles not IDs
    @Test
    public void testRecommendMovies_ECP_ReturnsTitlesNotIds() {
        watchedMovies.add("I001");
        Set<String> recommendations = Recommendation.recommendMovies(watchedMovies, movieList);
        
        // Verify returns contain titles
        assertTrue(recommendations.contains("The Matrix"));
        assertFalse(recommendations.contains("TM002")); // Should not contain IDs
    }
}
