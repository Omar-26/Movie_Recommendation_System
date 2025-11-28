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
        Path outPath = Path.of("recommendations.txt");
        String output = "";
        
        
        MovieFileParser movieParser = new MovieFileParser();
        UserFileParser userParser = new UserFileParser();
        List<Movie> movies = List.of();
        List<User> users = List.of();
        Set<String> existingUserIds = new java.util.HashSet<>(Set.of());
        
        //-------- Movies Processing --------//
        
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

                output = Validation.validateMovieTitle(m);
                if(output != null){
                    output = FileHandler.removeAnsiCodes(output);
                    FileHandler.writeFile(outPath, output);
                    return;
                }
                output = Validation.validateMovieId(m);
                if(output != null){
                    output = FileHandler.removeAnsiCodes(output);
                    FileHandler.writeFile(outPath, output);
                    return;
                }

                System.out.println("-----------------------------------");
            }

        //-------- Users Processing --------//
        
        // Users Parsing
        try {
            users = userParser.readUsers("src/main/resources/users.txt");
            System.out.println("\u001B[1;32mUsers parsed successfully\u001B[0m");
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // Users Validation
        for (User u : users) {
            System.out.println("User Name: " + u.name());
            System.out.println("User ID: " + u.id());
            
            output = Validation.validateUserName(u, existingUserIds);
                if(output != null){
                    output = FileHandler.removeAnsiCodes(output);
                    FileHandler.writeFile(outPath, output);
                    return;
                }
            output = Validation.validateUserId(u, existingUserIds);
                if(output != null){
                    output = FileHandler.removeAnsiCodes(output);
                    FileHandler.writeFile(outPath, output);
                    return;
                }
            
            existingUserIds.add(u.id());
            
            System.out.println("-----------------------------------");
        }
        
        // Generate Recommendations File
        Recommendation.generateRecommendationsFile(users, movies);
    }
}
