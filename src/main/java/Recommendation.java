// Movie Recommendation System/src/main/java/Recommendation.java
import model.Movie;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Recommendation {
    public static Set<String> recommendMovies(String[] watchedMovies, List<Movie> movies) {
        Set<String> watchedSet = Set.of(watchedMovies);
        Set<String> likedGenres = new HashSet<>();
        
        // Collect genres from watched movies
        for (Movie movie : movies) {
            if (watchedSet.contains(movie.id())) {
                likedGenres.addAll(List.of(movie.genres()));
            }
        }
        
        Set<String> recommendations = new HashSet<>();
        // Recommend movies from liked genres, excluding already watched
        for (Movie movie : movies) {
            if (!watchedSet.contains(movie.title())) {
                for (String genre : movie.genres()) {
                    if (likedGenres.contains(genre) && !watchedSet.contains(movie.id())) {
                        recommendations.add(movie.title());
                        break;
                    }
                }
            }
        }
        return recommendations;
    }
}