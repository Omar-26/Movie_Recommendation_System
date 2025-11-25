import model.Movie;
import model.User;

import java.util.Objects;
import java.util.Set;

public class Validation {
    
    //------- Movie Validation Methods -------//
    
    private static String validateMovieTitle(String Title) {
        if (Title == null || Title.isEmpty()) {
            return "\u001B[31mERROR: Movie Title {" + Title + "} is wrong\u001B[0m";
        }
        
        String[] words = Title.split("\\s+");
        for (String word : words) {
            if (!Character.isUpperCase(word.charAt(0))) {
                return "\u001B[31mERROR: Movie Title {" + Title + "} is wrong\u001B[0m";
            }
        }
        return null;
    }
    
    private static String validateMovieId(String title, String movieId) {
        
        String expectedPrefix = extractCapitalLetters(title);
        
        // Rule 1: Prefix must match
        if (!movieId.startsWith(expectedPrefix)) {
            return "\u001B[31mERROR: Movie Id letters " + movieId + " are wrong\u001B[0m";
        }
        
        // Remaining part must be exactly 3 digits
        String suffix = movieId.substring(expectedPrefix.length());
        if (suffix.length() != 3 || !suffix.matches("\\d{3}")) {
            return "\u001B[31mERROR: Movie Id letters " + movieId + " are wrong\u001B[0m";
        }
        
        // Digits must be unique
        if (suffix.charAt(0) == suffix.charAt(1) ||
                suffix.charAt(0) == suffix.charAt(2) ||
                suffix.charAt(1) == suffix.charAt(2)) {
            return "\u001B[31mERROR: Movie Id numbers " + movieId + " aren't unique\u001B[0m";
        }
        
        // Valid
        return null;
    }
    private static String extractCapitalLetters(String title) {
        StringBuilder sb = new StringBuilder();
        
        for (char ch : title.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
    //------- User Validation Methods -------//
    
    private static String validateUserName(String userName) {
        // Must not be empty
        if (userName == null || userName.isEmpty())
            return "\u001B[31mERROR: User Name {" + userName + "} is wrong\u001B[0m";
        
        // Must not start with a space
        if (userName.startsWith(" "))
            return "\u001B[31mERROR: User Name {" + userName + "} is wrong\u001B[0m";
        
        // Alphabetic characters + spaces only
        if (!userName.matches("[A-Za-z ]+"))
            return "\u001B[31mERROR: User Name {" + userName + "} is wrong\u001B[0m";
        
        // If all validations pass --> return null (no error)
        return null;
    }
    
    private static String validateUserId(String userId, Set<String> existingIds) {
        if (userId == null || userId.isEmpty())
            return "\u001B[31mERROR: User ID {" + userId + "} is wrong\u001B[0m";
        // user id ends by a digit or a number (whether it is 9 or not is enforced in this condition)
        if (!userId.matches("^(\\d{9}|\\d{8}[A-Za-z])$"))
            return "\u001B[31mERROR: User ID {" + userId + "} is wrong\u001B[0m";
        // check uniqueness
        if (existingIds.contains(userId))
            return "\u001B[31mERROR: User ID {" + userId + "} is wrong\u001B[0m";
        // If all validations pass --> return null (no error)
        return null;
    }
    
    //------- Helper Methods -------//
    
    public static void validateMovie(Movie movie) {
        // Movie Title Validation
        String error = validateMovieTitle(movie.title());
        
        //TODO if condition with return
        System.out.println(Objects.requireNonNullElse(error, "\u001B[1;32mValid Title ✔\u001B[0m"));
        
        // Movie ID Validation
        error = Validation.validateMovieId(movie.title(), movie.id());
        System.out.println(Objects.requireNonNullElse(error, "\u001B[1;32mValid ID ✔\u001B[0m"));
    }
    
    public static String validateUser(User user, Set<String> existingIds) {
        // User Name Validation
        String error = Validation.validateUserName(user.name());
        System.out.println(Objects.requireNonNullElse(error, "\u001B[1;32mValid Name ✔\u001B[0m"));
        
        // User ID Validation
        error = Validation.validateUserId(user.id(), existingIds);
        System.out.println(Objects.requireNonNullElse(error, "\u001B[1;32mValid ID ✔\u001B[0m"));
        return error;
    }
}