import model.Movie;

import java.io.*;
import java.util.*;

public class MovieFileParser {
    
    public List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();
        
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = br.readLine()) != null) {
            //skip blank lines
            if(line.trim().isEmpty()){
                continue;
            }
            
            String[] titleAndId = line.split(",");
            if (titleAndId.length != 2) {
                throw new Exception("ERROR: Wrong movie line format: " + line);
            }
            
            String title = titleAndId[0].trim();
            String movieId = titleAndId[1].trim();
            
//            validateMovieTitle(title);
//            validateMovieId(title, movieId);
            
            String genresLine = br.readLine();
            if (genresLine == null) {
                throw new Exception("ERROR: Genres missing for movie: " + title);
            }
            
            String[] genres = Arrays.stream(genresLine.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
            
            movies.add(new Movie(title, movieId, genres));
        }
        
        br.close();
        return movies;
    }
    
//    private void validateMovieTitle(String title) throws Exception {
//        for (String word : title.split(" ")) {
//            if (!Character.isUpperCase(word.charAt(0))) {
//                throw new Exception("ERROR: Movie Title " + title + " is wrong");
//            }
//        }
//    }
//    private void validateMovieId(String title, String id) throws Exception {
//        StringBuilder letters = new StringBuilder();
//        for (char c : title.toCharArray()) {
//            if (Character.isUpperCase(c)) letters.append(c);
//        }
//
//        String expectedLetters = letters.toString();
//        String actualLetters = id.replaceAll("[0-9]", "");
//
//        if (!actualLetters.equals(expectedLetters)) {
//            throw new Exception("ERROR: Movie Id letters " + id + " are wrong");
//        }
//
//        String digits = id.replaceAll("[A-Z]", "");
//
//        if (digits.length() != 3 || hasDuplicateDigits(digits)) {
//            throw new Exception("ERROR: Movie Id numbers " + id + " aren’t unique");
//        }
//    }
//
//    private boolean hasDuplicateDigits(String s) {
//        return s.charAt(0)==s.charAt(1) ||
//                s.charAt(0)==s.charAt(2) ||
//                s.charAt(1)==s.charAt(2);
//    }
}