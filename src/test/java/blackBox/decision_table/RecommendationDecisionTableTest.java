import model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - Decision Table for Recommendation class
 * Tests all combinations of conditions using decision table technique
 */
public class RecommendationDecisionTableTest {
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

    //------- Decision Table: recommendMovies() - Input Validation -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 | Rule 7 | Rule 8 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Watched Valid (T/F)   |   F    |   F    |   T    |   T    |   T    |   T    |   T    |   T    |
     * | Movies Valid (T/F)    |   -    |   -    |   F    |   F    |   T    |   T    |   T    |   T    |
     * | Genre Match (T/F)     |   -    |   -    |   -    |   -    |   T    |   F    |   T    |   F    |
     * | All Watched (T/F)     |   -    |   -    |   -    |   -    |   F    |   -    |   T    |   -    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Output                |   E    |   E    |   E    |   E    |   R    |   E    |   E    |   E    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Valid
     *   F = False/Invalid
     *   - = Don't Care (not checked if previous condition fails)
     *   E = Empty Set (no recommendations)
     *   R = Recommendations Returned
     *
     * Interpretation:
     *   Rule 1: Watched is null → Empty set
     *   Rule 2: Watched is empty → Empty set
     *   Rule 3: Watched valid, movies null → Empty set
     *   Rule 4: Watched valid, movies empty → Empty set
     *   Rule 5: Watched valid, movies valid, genre match, not all watched → Recommendations
     *   Rule 6: Watched valid, movies valid, no genre match → Empty set
     *   Rule 7: Watched valid, movies valid, genre match, all watched → Empty set
     *   Rule 8: Watched valid, movies valid, no genre overlap → Empty set
     */

    // Rule 1: Watched Valid=F (null), Movies Valid=-, Genre Match=- → E (Empty)
    @Test
    public void testRecommend_DT_Rule1_WatchedNull() {
        Set<String> result = Recommendation.recommendMovies(null, movieList);
        assertTrue(result.isEmpty()); // E = Empty
    }

    // Rule 2: Watched Valid=F (empty), Movies Valid=-, Genre Match=- → E (Empty)
    @Test
    public void testRecommend_DT_Rule2_WatchedEmpty() {
        Set<String> result = Recommendation.recommendMovies(new HashSet<>(), movieList);
        assertTrue(result.isEmpty()); // E = Empty
    }

    // Rule 3: Watched Valid=T, Movies Valid=F (null), Genre Match=- → E (Empty)
    @Test
    public void testRecommend_DT_Rule3_MoviesNull() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, null);
        assertTrue(result.isEmpty()); // E = Empty
    }

    // Rule 4: Watched Valid=T, Movies Valid=F (empty), Genre Match=- → E (Empty)
    @Test
    public void testRecommend_DT_Rule4_MoviesEmpty() {
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, new ArrayList<>());
        assertTrue(result.isEmpty()); // E = Empty
    }

    // Rule 5: Watched Valid=T, Movies Valid=T, Genre Match=T, All Watched=F → R (Recommendations)
    @Test
    public void testRecommend_DT_Rule5_AllValidWithMatch() {
        watchedMovies.add("A001"); // Watched Action1
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertFalse(result.isEmpty()); // R = Recommendations
        assertTrue(result.contains("Action2")); // Same genre
        assertFalse(result.contains("Action1")); // Already watched
    }

    // Rule 6: Watched Valid=T, Movies Valid=T, Genre Match=F → E (Empty)
    @Test
    public void testRecommend_DT_Rule6_AllValidNoMatch() {
        watchedMovies.add("XXX999"); // Non-existent movie
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertTrue(result.isEmpty()); // E = Empty (no genres extracted, no match)
    }

    // Rule 7: Watched Valid=T, Movies Valid=T, Genre Match=T, All Watched=T → E (Empty)
    @Test
    public void testRecommend_DT_Rule7_AllMoviesWatched() {
        watchedMovies.add("A001");
        watchedMovies.add("A002");
        watchedMovies.add("D001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertTrue(result.isEmpty()); // E = Empty (all watched, nothing to recommend)
    }

    // Rule 8: Watched Valid=T, Movies Valid=T, Genre Match=F (no overlap) → E (Empty)
    @Test
    public void testRecommend_DT_Rule8_NoGenreOverlap() {
        List<Movie> differentGenres = new ArrayList<>();
        differentGenres.add(new Movie("Action1", "A001", new String[]{"Action"}));
        differentGenres.add(new Movie("Horror1", "H001", new String[]{"Horror"}));
        
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, differentGenres);
        
        assertTrue(result.isEmpty()); // E = Empty (no Horror movies watched, no overlap)
    }

    //------- Decision Table: Genre Matching Logic -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 |
     * +-----------------------+--------+--------+--------+--------+
     * | Watched Has Genre(T/F)|   T    |   T    |   T    |   F    |
     * | Unwatched Has Genre(T/F)|  T    |   T    |   F    |   T    |
     * | Same Genre (T/F)      |   T    |   F    |   -    |   -    |
     * +-----------------------+--------+--------+--------+--------+
     * | Output                |   R    |   N    |   N    |   N    |
     * +-----------------------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Has
     *   F = False/Empty
     *   - = Don't Care
     *   R = Recommend
     *   N = Don't Recommend
     *
     * Interpretation:
     *   Rule 1: Watched has genre, unwatched has genre, same genre → Recommend
     *   Rule 2: Watched has genre, unwatched has genre, different genre → Don't recommend
     *   Rule 3: Watched has genre, unwatched has no genres → Don't recommend
     *   Rule 4: Watched has no genres, unwatched has genre → Don't recommend
     */

    // Rule 1: Watched Has Genre=T, Unwatched Has Genre=T, Same Genre=T → R (Recommend)
    @Test
    public void testGenreMatch_DT_Rule1_Match() {
        watchedMovies.add("A001"); // Action
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertTrue(result.contains("Action2")); // R = Recommend (same genre)
    }

    // Rule 2: Watched Has Genre=T, Unwatched Has Genre=T, Same Genre=F → N (Don't Recommend)
    @Test
    public void testGenreMatch_DT_Rule2_NoMatch() {
        watchedMovies.add("A001"); // Action
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movieList);
        
        assertFalse(result.contains("Drama1")); // N = Don't Recommend (different genre)
    }

    // Rule 3: Watched Has Genre=T, Unwatched Has Genre=F, Same Genre=- → N (Don't Recommend)
    @Test
    public void testGenreMatch_DT_Rule3_UnwatchedNoGenres() {
        List<Movie> moviesWithEmpty = new ArrayList<>();
        moviesWithEmpty.add(new Movie("Action1", "A001", new String[]{"Action"}));
        moviesWithEmpty.add(new Movie("NoGenre", "NG001", new String[]{}));
        
        watchedMovies.add("A001");
        Set<String> result = Recommendation.recommendMovies(watchedMovies, moviesWithEmpty);
        
        assertFalse(result.contains("NoGenre")); // N = Don't Recommend (no genres)
    }

    // Rule 4: Watched Has Genre=F, Unwatched Has Genre=T, Same Genre=- → N (Don't Recommend)
    @Test
    public void testGenreMatch_DT_Rule4_WatchedNoGenres() {
        List<Movie> moviesWithEmpty = new ArrayList<>();
        moviesWithEmpty.add(new Movie("NoGenre", "NG001", new String[]{}));
        moviesWithEmpty.add(new Movie("Action1", "A001", new String[]{"Action"}));
        
        watchedMovies.add("NG001"); // Watched movie with no genres
        Set<String> result = Recommendation.recommendMovies(watchedMovies, moviesWithEmpty);
        
        assertTrue(result.isEmpty()); // N = Don't Recommend (no genres to match)
    }

    //------- Decision Table: Multiple Watched Movies -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 |
     * +-----------------------+--------+--------+--------+--------+
     * | Has Genre A (T/F)     |   T    |   T    |   T    |   T    |
     * | Has Genre B (T/F)     |   T    |   T    |   T    |   T    |
     * | Unwatched=A (T/F)     |   T    |   F    |   F    |   T    |
     * | Unwatched=B (T/F)     |   F    |   T    |   F    |   T    |
     * | Unwatched=C (T/F)     |   F    |   F    |   T    |   F    |
     * +-----------------------+--------+--------+--------+--------+
     * | Output                |   R    |   R    |   N    |   R    |
     * +-----------------------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Matches
     *   F = False/No Match
     *   R = Recommend
     *   N = Don't Recommend
     *
     * Interpretation:
     *   Rule 1: Watched genres={A,B}, unwatched genre=A → Recommend
     *   Rule 2: Watched genres={A,B}, unwatched genre=B → Recommend
     *   Rule 3: Watched genres={A,B}, unwatched genre=C → Don't recommend
     *   Rule 4: Watched genres={A,B}, unwatched genre={A,B} → Recommend
     */

    // Rule 1: Has Genre A=T, Has Genre B=T, Unwatched=A(T), Unwatched=B(F), Unwatched=C(F) → R
    @Test
    public void testMultipleWatched_DT_Rule1_MatchFirst() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Action2", "A002", new String[]{"Action"}));
        
        watchedMovies.add("A001"); // Action
        watchedMovies.add("D001"); // Drama
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.contains("Action2")); // R = Recommend (matches Action)
    }

    // Rule 2: Has Genre A=T, Has Genre B=T, Unwatched=A(F), Unwatched=B(T), Unwatched=C(F) → R
    @Test
    public void testMultipleWatched_DT_Rule2_MatchSecond() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Drama2", "D002", new String[]{"Drama"}));
        
        watchedMovies.add("A001"); // Action
        watchedMovies.add("D001"); // Drama
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.contains("Drama2")); // R = Recommend (matches Drama)
    }

    // Rule 3: Has Genre A=T, Has Genre B=T, Unwatched=A(F), Unwatched=B(F), Unwatched=C(T) → N
    @Test
    public void testMultipleWatched_DT_Rule3_NoMatch() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Horror1", "H001", new String[]{"Horror"}));
        
        watchedMovies.add("A001"); // Action
        watchedMovies.add("D001"); // Drama
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertFalse(result.contains("Horror1")); // N = Don't Recommend (no match)
    }

    // Rule 4: Has Genre A=T, Has Genre B=T, Unwatched=A(T), Unwatched=B(T), Unwatched=C(F) → R
    @Test
    public void testMultipleWatched_DT_Rule4_MatchBoth() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Action1", "A001", new String[]{"Action"}));
        movies.add(new Movie("Drama1", "D001", new String[]{"Drama"}));
        movies.add(new Movie("Mixed", "M001", new String[]{"Action", "Drama"}));
        
        watchedMovies.add("A001"); // Action
        watchedMovies.add("D001"); // Drama
        Set<String> result = Recommendation.recommendMovies(watchedMovies, movies);
        
        assertTrue(result.contains("Mixed")); // R = Recommend (matches both)
    }
}
