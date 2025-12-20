package unit;

import logic.Recommendation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationTest {
    
    static List<Movie> movies = List.of();
    
    @BeforeAll
    public static void init() {
        Movie actionOne = new Movie("ActionOne", "AO123", new String[]{"Action"});
        Movie actionTwo = new Movie("ActionTwo", "AT321", new String[]{"Action"});
        Movie dramaOne = new Movie("DramaOne", "DO456", new String[]{"Drama"});
        Movie dramaTwo = new Movie("DramaTwo", "DT654", new String[]{"Drama"});
        Movie mixed = new Movie("Mixed", "M789", new String[]{"Action", "Drama"});
        
        movies = List.of(actionOne, actionTwo, dramaOne, dramaTwo, mixed);
    }
    
    @Test
    @DisplayName("Test recommendations when no movies have been watched")
    public void testNoWatchedMovies() {
        // Arrange
        Set<String> watched = Set.of();
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertTrue(recs.isEmpty(), "Expected no recommendations when nothing was watched");
    }
    
    @Test
    @DisplayName("Test recommendations based on a single genre watched")
    public void testRecommendBasedOnSingleGenre() {
        // Arrange
        Set<String> watched = Set.of("AO123");
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertEquals(Set.of("ActionTwo", "Mixed"), recs);
    }
    
    @Test
    @DisplayName("Test recommendations based on a multiple genre watched")
    public void testRecommendBasedOnMultipleGenre() {
        // Arrange
        Set<String> watched = Set.of("AO123", "DO456");
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertEquals(Set.of("ActionTwo", "DramaTwo", "Mixed"), recs);
    }
    
    
    @Test
    public void testExcludeAlreadyWatched() {
        // Arrange
        Set<String> watched = Set.of("AO123", "DO456");
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertTrue(Collections.disjoint(recs, watched)); // Ensure no overlap between recommendations and watched movies
    }
    
    @Test
    @DisplayName("Test recommendations when all movies have been watched")
    public void testAllMoviesWatched() {
        // Arrange
        Set<String> watched = Set.of("AO123", "AT321", "DO456", "DT654", "M789");
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertTrue(recs.isEmpty(), "Expected no recommendations when all movies have been watched");
    }
    
    @Test
    @DisplayName("Test recommendations when watched movies are not in the movies list")
    public void testWatchedMoviesNotinMoviesList() {
        // Arrange
        Set<String> watched = Set.of("TO345");
        
        // Act
        Set<String> recs = Recommendation.recommendMovies(watched, movies);
        
        // Assert
        assertNotNull(recs);
        assertTrue(recs.isEmpty(), "Expected no recommendations when watched movies are not in the movies list");
    }
}