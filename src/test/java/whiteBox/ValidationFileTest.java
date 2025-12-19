import logic.Validation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validation – Movie Title Statement Coverage Tests")
public class ValidationFileTest {
    private Set<String> existingIds;

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("123456789");   // Example existing ID
    }
            // ===================== STATEMENT COVERAGE TEST CASES =====================

    /**
     * TC4: Title = "Good Movie"
     * Statement coverage → 5/7
     */
    @Test
    @DisplayName("TC4 – Statement coverage: valid movie title")
    void testStatementCoverage_TC4_ValidTitle() {
        Movie movie = new Movie("Good Movie", "123", new String[]{"Drama"});

        String result = Validation.validateMovieTitle(movie);

        assertNull(result);
    }

    /**
     * TC5: Title = null
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC5 – Statement coverage: null movie title")
    void testStatementCoverage_TC5_NullTitle() {
        Movie movie = new Movie(null, "123", new String[]{"Drama"});

        String result = Validation.validateMovieTitle(movie);

        assertNotNull(result);
    }

    /**
     * TC5: Title = empty string
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC5 – Statement coverage: empty movie title")
    void testStatementCoverage_TC5_EmptyTitle() {
        Movie movie = new Movie("", "123", new String[]{"Drama"});

        String result = Validation.validateMovieTitle(movie);

        assertNotNull(result);
    }

    /**
     * TC6: Title = "goodMovie"
     * Statement coverage → 5/7
     */
    @Test
    @DisplayName("TC6 – Statement coverage: lowercase starting title")
    void testStatementCoverage_TC6_LowercaseStart() {
        Movie movie = new Movie("goodMovie", "123", new String[]{"Drama"});

        String result = Validation.validateMovieTitle(movie);

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC7 – Statement coverage: valid movie ID")
    void testStatementCoverage_TC7_ValidId() {
        Movie movie = new Movie("Spider Man", "SM123", new String[]{});

        String result = Validation.validateMovieId(movie);

        assertNull(result); // Valid ID
    }

    @Test
    @DisplayName("TC8 – Statement coverage: non-unique digits in movie ID")
    void testStatementCoverage_TC8_NonUniqueDigits() {
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});

        String result = Validation.validateMovieId(movie);

        assertNotNull(result); // Digits not unique
    }

    @Test
    @DisplayName("TC9 – Statement coverage: wrong prefix letters")
    void testStatementCoverage_TC9_WrongPrefix() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{});

        String result = Validation.validateMovieId(movie);

        assertNotNull(result); // Wrong prefix
    }

    @Test
    @DisplayName("TC10 – Statement coverage: suffix not 3 digits")
    void testStatementCoverage_TC10_SuffixNotThreeDigits() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{});

        String result = Validation.validateMovieId(movie);

        assertNotNull(result); // Suffix length invalid
    }

    /**
     * TC11: username = null
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC11 – Statement coverage: null username")
    void testStatementCoverage_TC11_NullUsername() {
        User user = new User(null, "12345678A", null);

        String result = Validation.validateUserName(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC11: username = empty
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC11 – Statement coverage: empty username")
    void testStatementCoverage_TC11_EmptyUsername() {
        User user = new User("", "12345678A", null);

        String result = Validation.validateUserName(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC12: username started with space
     * Statement coverage → 3/7
     */
    @Test
    @DisplayName("TC12 – Statement coverage: username starts with space")
    void testStatementCoverage_TC12_UsernameStartsWithSpace() {
        User user = new User(" John", "12345678A", null);

        String result = Validation.validateUserName(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC13: username contains non-alphabetic characters
     * Statement coverage → 4/7
     */
    @Test
    @DisplayName("TC13 – Statement coverage: username with non-alphabetic chars")
    void testStatementCoverage_TC13_UsernameWithNonAlphabeticChars() {
        User user = new User("John123", "12345678A", null);

        String result = Validation.validateUserName(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC14: Valid username
     * Statement coverage → 4/7
     */
    @Test
    @DisplayName("TC14 – Statement coverage: valid username")
    void testStatementCoverage_TC14_ValidUsername() {
        User user = new User("John Smith", "12345678A", null);

        String result = Validation.validateUserName(user, existingIds);

        assertNull(result);
    }


    /**
     * TC15: userId = null
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC15 – Statement coverage: null user ID")
    void testStatementCoverage_TC15_NullUserId() {
        User user = new User("John", null, null);

        String result = Validation.validateUserId(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC15: userId = empty
     * Statement coverage → 2/7
     */
    @Test
    @DisplayName("TC15 – Statement coverage: empty user ID")
    void testStatementCoverage_TC15_EmptyUserId() {
        User user = new User("John", "", null);

        String result = Validation.validateUserId(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC16: userId is not of 9 characters (length < 9 or length > 9)
     * Statement coverage → 3/7
     */
    @Test
    @DisplayName("TC16 – Statement coverage: invalid length user IDs")
    void testStatementCoverage_TC16_InvalidLengthUserId() {
        User userShort = new User("Jane", "1234567", null); // too short
        User userLong = new User("Bob", "1234567890", null); // too long

        String resultShort = Validation.validateUserId(userShort, existingIds);
        String resultLong = Validation.validateUserId(userLong, existingIds);

        assertNotNull(resultShort);
        assertNotNull(resultLong);
    }

    /**
     * TC17: userId is not unique
     * Statement coverage → 4/7
     */
    @Test
    @DisplayName("TC17 – Statement coverage: non-unique user ID")
    void testStatementCoverage_TC17_NonUniqueUserId() {
        User user = new User("John", "123456789", null); // already exists in existingIds

        String result = Validation.validateUserId(user, existingIds);

        assertNotNull(result);
    }

    /**
     * TC18: Valid userId
     * Statement coverage → 4/7
     */
    @Test
    @DisplayName("TC18 – Statement coverage: valid user ID")
    void testStatementCoverage_TC18_ValidUserId() {
        User user = new User("John", "158456678", null); // unique and valid format

        String result = Validation.validateUserId(user, existingIds);

        assertNull(result);
    }

            // ===================== BRANCH COVERAGE TEST CASES =====================
            // the test cases 31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48
            // are redundant from the statement coverage //


            // ===================== PATH COVERAGE TEST CASES =====================
            // the test cases 59,60,61,62,63,64,65,66,67,68,69,70,71,72,73
            // are redundant from the above coverage methods //
}
