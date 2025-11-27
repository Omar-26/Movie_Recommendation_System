import model.Movie;

import java.io.*;
import java.util.*;


public class MovieFileParser {

    public List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()){
                    continue;
                }

                String[] titleAndId = line.split(",");
                if (titleAndId.length != 2) {
                    throw new Exception("ERROR: Wrong movie line format: " + line);
                }

                String title = titleAndId[0].trim();
                String movieId = titleAndId[1].trim();

                String genresLine = br.readLine();
                if (genresLine == null) {
                    throw new Exception("ERROR: Genres missing for movie: " + title);
                }

                String[] genres = Arrays.stream(genresLine.split(","))
                        .map(String::trim)
                        .toArray(String[]::new);

                movies.add(new Movie(title, movieId, genres));
            }
        }

        return movies;
    }
}
