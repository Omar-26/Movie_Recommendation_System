import model.Movie;
import model. User;

import java.io. BufferedReader;
import java. io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserFileParser {

    public List<User> readUsers(String filePath) throws Exception {
        List<User> users = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                //skip blank lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] nameAndId = line.split(",", 2); // Limit split to 2 parts
                if (nameAndId.length != 2) {
                    throw new Exception("ERROR: Wrong user line format: " + line);
                }

                // Only trim the userId, preserve spaces in name
                String name = nameAndId[0]; // DO NOT trim - preserve leading/trailing spaces
                String userId = nameAndId[1]. trim();

                String watchedMoviesLine = br.readLine();

                Set<String> watchedMovies = Arrays.stream(watchedMoviesLine.split(","))
                        .map(String:: trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors. toSet());

                users.add(new User(name, userId, watchedMovies));
            }
        } //br.close();
        //System.out.println(users);
        return users;
    }
}


