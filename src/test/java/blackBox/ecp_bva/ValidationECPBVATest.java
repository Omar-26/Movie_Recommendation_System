package blackBox.ecp_bva;

import logic.Validation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationECPBVATest {
    private Set<String> existingIds;
    private Set<String> existingMovieIds;

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("87654321B");
        existingMovieIds = new HashSet<>();
        existingMovieIds.add("A125");
    }

    //------- Movie Title Validation - ECP & BVA -------//

    // BVA is not applicable for movie title

    // ECP:
    @Test
    public void movieTitle_ECP1_valid_allWordsStartWithCapital() {
        Movie movie = new Movie("The Matrix", "TM123", new String[]{});
        assertNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void movieTitle_ECP2_invalid_empty() {
        Movie movie = new Movie("", "TM123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void ovieTitle_ECP3_invalid_firstLetterNotCapital() {
        Movie movie = new Movie("the Matrix", "TM123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void movieTitle_ECP4_invalid_startsWithDigit() {
        Movie movie = new Movie("1Avengers", "A123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void movieTitle_ECP5_invalid_startsWithSpecialChar() {
        Movie movie = new Movie("$Avengers", "A123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }


    //------- Movie ID Validation - ECP & BVA -------//

    // BVA (partial on digits length):
    @Test
    public void movieId_BVA1_nominal_3Digits() {
        Movie movie = new Movie("The Matrix", "TM123", new String[]{});
        assertNull(Validation.validateMovieId(movie, existingMovieIds));
    }
    @Test
    public void movieId_BVA2_belowMin_2Digits() {
        Movie movie = new Movie("The Matrix", "TM12", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_BVA3_AboveMin_4Digits() {
        Movie movie = new Movie("The Matrix", "TM1234", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    // ECP:
    @Test
    public void movieId_ECP1_valid() {
        Movie movie = new Movie("The Dark", "TD123", new String[]{});
        assertNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP2_invalid_empty() {
        Movie movie = new Movie("The Dark Night", "", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP3_invalid_lowercase() {
        Movie movie = new Movie("The Dark Night", "Tdk123", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP4_invalid_2Digits() {
        Movie movie = new Movie("The Dark Night", "TDK12", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP5_invalid_4Digits() {
        Movie movie = new Movie("The Dark Night", "TDK1234", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP6_invalid_existsBefore() {
        Movie movie = new Movie("Avengers", "A125", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }

    @Test
    public void movieId_ECP7_invalid_hasSpecialChar() {
        Movie movie = new Movie("Avengers", "A@123", new String[]{});
        assertNotNull(Validation.validateMovieId(movie, existingMovieIds));
    }


    //------- User Name Validation - ECP & BVA -------//

    // BVA is not applicable for user name

    // ECP:
    @Test
    public void userName_ECP1_valid_lettersAndSpaces() {
        User user = new User("John Smith", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void userName_ECP2_invalid_empty() {
        User user = new User("", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void userName_ECP3_invalid_containsDigit() {
        User user = new User("John 1 Smith", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void userName_ECP4_invalid_containsSpecialChar() {
        User user = new User("John @ Smith", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void userName_ECP5_invalid_startsWithSpace() {
        User user = new User("", " John Smith", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }


    //------- User ID Validation - ECP & BVA -------//

    // BVA:
    @Test
    public void userId_BVA1_nominal_9Chars() {
        User user = new User("John", "12345678A", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_BVA2_belowMin_8Chars() {
        User user = new User("John", "1234567A", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_BVA3_aboveMax_10Chars() {
        User user = new User("John", "123456789A", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_BVA4_minCase_9Digits_endsWith0Letters() {
        User user = new User("John", "123456789", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_BVA5_nominal_9Digits_endsWith1Letter() {
        User user = new User("John", "12345678S", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_BVA6_nominal_9Digits_endsWith2Letters() {
        User user = new User("John", "1234567AB", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    // ECP:
    @Test
    public void userId_ECP1_valid_9Digits() {
        User user = new User("John", "987654321", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_ECP2_valid_8DigitsPlus1Letter() {
        User user = new User("John", "87654321A", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_ECP3_invalid_notUnique() {
        User user = new User("John", "87654321B", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_ECP4_invalid_tooShort() {
        User user = new User("John", "12345678", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_ECP5_invalid_tooLong() {
        User user = new User("John", "1234567890", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void userId_ECP6_invalid_endsWith2Letters() {
        User user = new User("John", "1234567AB", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

}
