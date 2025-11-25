import model.Movie;
import model.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        
        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();
        List<Movie> movies = List.of();
        List<User> users = List.of();
        Set<String> existingUserIds = new java.util.HashSet<>(Set.of());
        
        // Movies Parsing
        try {
            movies = movieParser.readMovies("src/main/resources/movies.txt");
            System.out.println("-----------------------------------");
            System.out.println("\u001B[1;32mMovies parsed successfully\u001B[0m");
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // Movies Validation
        for (Movie m : movies) {
            System.out.println("Movie Title: " + m.title());
            System.out.println("Movie ID: " + m.id());
            
            Validation.validateMovie(m);
            
            System.out.println("-----------------------------------");
        }
        
        // Users Parsing
        try {
            users = userParser.readUsers("src/main/resources/users.txt");
            System.out.println("\u001B[1;32mUsers parsed successfully\u001B[0m");
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        Path outPath = Path.of("recommendations.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(outPath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            
            // Users Validation
            for (User u : users) {
                System.out.println("User Name: " + u.name());
                System.out.println("User ID: " + u.id());
                
                Validation.validateUser(u, existingUserIds);
                
                existingUserIds.add(u.id());
                
                System.out.println("-----------------------------------");
                
                Set<String> recommendedMovies = Recommendation.recommendMovies(u.favoriteMovies(), movies);
                
                // Write user line then recommendations line
                writer.write(u.name() + "," + u.id());
                writer.newLine();
                
                String joined = String.join(",", recommendedMovies);
                writer.write(joined);
                writer.newLine();
                
                // Recommended Movies
//            //TODO shouldn't get the movies the user has already watched
//            Set<String> recommendedMovies = Recommendation.recommendMovies(u.favoriteMovies(), movies);
//            for (String title : recommendedMovies) {
//                System.out.println("\u001B[1;34mRecommended Movie: " + title + "\u001B[0m");
//            }
                
                //TODO if the user is invalid we shouldn't recommend movies
                //TODO if the user doesn't have favorite movies we shouldn't add the user to recommendations file
                //TODO if the user already watched all movies we shouldn't add the user to recommendations file
            }
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}