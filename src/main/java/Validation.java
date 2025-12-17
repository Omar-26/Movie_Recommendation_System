import model.Movie;
import model.User;

import java.util.Set;

public class Validation {

    //------- Movie Validation Methods -------//

    private static String validateMovieTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "ERROR: Movie Title {" + title + "} is wrong";
        }

        String[] words = title.trim().split("[\\s-]+");
        for (String word : words) {
            if (word.isEmpty() || !Character.isUpperCase(word.charAt(0))) {
                return "ERROR: Movie Title {" + title + "} is wrong";
            }
        }
        return null;
    }

    // -------- FORMAT VALIDATION ONLY --------
    private static String validateMovieId(String title, String movieId) {

        if (movieId == null || movieId.isEmpty()) {
            return "ERROR: Movie Id letters " + movieId + " are wrong";
        }

        String expectedPrefix = extractCapitalLetters(title);

        // prefix (letters) validation
        if (!movieId.startsWith(expectedPrefix)) {
            return "ERROR: Movie Id letters " + movieId + " are wrong";
        }

        String suffix = movieId.substring(expectedPrefix.length());

        // must be exactly 3 digits
        if (!suffix.matches("\\d{3}")) {
            return "ERROR: Movie Id letters " + movieId + " are wrong";
        }

        return null; // format valid
    }

    // -------- FORMAT + UNIQUENESS --------
    public static String validateMovieId(Movie movie, Set<String> existingMovieIds) {

        // 1️⃣ check format first
        String error = validateMovieId(movie.title(), movie.id());
        if (error != null) {
            return error;
        }

        // 2️⃣ check uniqueness of digits across movies
        String prefix = extractCapitalLetters(movie.title());
        String currentDigits = movie.id().substring(prefix.length());

        for (String existingId : existingMovieIds) {
            if (existingId.length() >= 3 &&
                    existingId.substring(existingId.length() - 3).equals(currentDigits)) {

                return "ERROR: Movie Id numbers " + movie.id() + " aren’t unique";
            }
        }

        return null; // fully valid
    }

    private static String extractCapitalLetters(String title) {
        StringBuilder sb = new StringBuilder();
        if (title == null) return "";

        for (char ch : title.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    //------- User Validation Methods -------//

    private static String validateUserName(String userName) {
        if (userName == null || userName.isEmpty())
            return "ERROR: User Name {" + userName + "} is wrong";

        if (userName.startsWith(" "))
            return "ERROR: User Name {" + userName + "} is wrong";

        if (!userName.matches("[A-Za-z ]+"))
            return "ERROR: User Name {" + userName + "} is wrong";

        return null;
    }

    private static String validateUserId(String userId, Set<String> existingIds) {
        if (userId == null || userId.isEmpty())
            return "ERROR: User ID {" + userId + "} is wrong";

        if (!userId.matches("^(\\d{9}|\\d{8}[A-Za-z])$"))
            return "ERROR: User ID {" + userId + "} is wrong";

        if (existingIds.contains(userId))
            return "ERROR: User ID {" + userId + "} is wrong";

        return null;
    }

    //------- Public APIs used by Tests -------//

    public static String validateMovieTitle(Movie movie) {
        return validateMovieTitle(movie.title());
    }

    public static String validateMovieId(Movie movie) {
        return validateMovieId(movie.title(), movie.id());
    }

    public static String validateUserId(User user, Set<String> existingIds) {
        return validateUserId(user.id(), existingIds);
    }

    public static String validateUserName(User user, Set<String> existingIds) {
        return validateUserName(user.name());
    }
}
