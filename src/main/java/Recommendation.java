import model.Movie;
import model.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Recommendation {
    
    // --- Recommendation method ---//
    public static Set<String> recommendMovies(Set<String> watchedMovies, List<Movie> movies) {
        if (watchedMovies == null || watchedMovies.isEmpty() || movies == null || movies.isEmpty()) {
            return new HashSet<>();
        }
        
        Set<String> likedGenres = new HashSet<>();
        for (Movie movie : movies) {
            if (watchedMovies.contains(movie.id())) {
                likedGenres.addAll(List.of(movie.genres()));
            }
        }
        
        return getRecommendations(movies, watchedMovies, likedGenres);
    }
    
    // --- Output generation method ---//
    public static void generateRecommendationsFile(List<User> users, List<Movie> movies) {
        Path outPath = Path.of("recommendations.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(outPath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            if (users == null) return;
            
            for (User u : users) {
                if (u == null) continue;
                
                if (u.watchedMovies() != null && !u.watchedMovies().isEmpty()) {
                    Set<String> recommendedMovies = recommendMovies(u.watchedMovies(), movies);
                    
                    writer.write(u.name() + "," + u.id());
                    writer.newLine();
                    
                    String joined = String.join(",", recommendedMovies);
                    writer.write(joined);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //--- Helper method to get recommendations ---//
    private static Set<String> getRecommendations(List<Movie> movies, Set<String> watchedSet, Set<String> likedGenres) {
        Set<String> recommendations = new HashSet<>();
        
        for (Movie movie : movies) {
            if (!watchedSet.contains(movie.id())) {
                for (String genre : movie.genres()) {
                    if (likedGenres.contains(genre)) {
                        recommendations.add(movie.title());
                        break;
                    }
                }
            }
        }
        return recommendations;
    }
}