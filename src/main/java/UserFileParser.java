import model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFileParser {
    
    public List<User> readUsers(String filePath) throws Exception {
        List<User> users = new ArrayList<>();
        
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = br.readLine()) != null) {
            //skip blank lines
            if(line.trim().isEmpty()){
                continue;
            }
            
            String[] nameAndId = line.split(",");
            if (nameAndId.length != 2) {
                throw new Exception("ERROR: Wrong user line format: " + line);
            }
            
            String name = nameAndId[0].trim();
            String userId = nameAndId[1].trim();
            
            String favouriteMoviesLine = br.readLine();
            //TODO remove as it doesn't make sense to throw an error if user has no favourite movies
            if (favouriteMoviesLine == null) {
                throw new Exception("ERROR: Favourite Movies missing for movie: " + name);
            }
            
            String[] favouriteMovies = Arrays.stream(favouriteMoviesLine.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
            
            users.add(new User(name, userId, favouriteMovies));
        }
        
        br.close();
        return users;
    }
}