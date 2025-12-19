

import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - ECP & BVA for Validation class
 * Tests equivalence class partitioning and boundary value analysis
 */
public class ValidationECPBVATest {
    private Set<String> existingIds;
    private Set<String> existingMovieIds;

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("123456789");
        existingMovieIds = new HashSet<>();
        existingMovieIds.add("SM112"); // For uniqueness testing
    }

    //------- Movie Title Validation - ECP & BVA -------//

    // ECP: Valid equivalence class - proper title format
    @Test
    public void testMovieTitle_ValidSingleWord() {
        Movie movie = new Movie("Inception", "I123", new String[]{});
        assertNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_ValidMultipleWords() {
        Movie movie = new Movie("The Dark Knight", "TDK123", new String[]{});
        assertNull(Validation.validateMovieTitle(movie));
    }

    // BVA: Boundary - single character title
    @Test
    public void testMovieTitle_BVA_SingleCharacterValid() {
        Movie movie = new Movie("A", "A123", new String[]{});
        assertNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_BVA_SingleCharacterInvalid() {
        Movie movie = new Movie("a", "a123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    // ECP: Invalid equivalence class - null/empty
    @Test
    public void testMovieTitle_ECP_Null() {
        Movie movie = new Movie(null, "123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_ECP_Empty() {
        Movie movie = new Movie("", "123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    // ECP: Invalid equivalence class - lowercase start
    @Test
    public void testMovieTitle_ECP_LowercaseStart() {
        Movie movie = new Movie("matrix", "M123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_ECP_MixedCaseInvalid() {
        Movie movie = new Movie("Harry potter", "HP123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    // ECP: Invalid equivalence class - numeric/special char start
    @Test
    public void testMovieTitle_ECP_NumericStart() {
        Movie movie = new Movie("1917", "123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_ECP_SpecialCharStart() {
        Movie movie = new Movie("$Money", "M123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    // BVA: Boundary - hyphenated words
    @Test
    public void testMovieTitle_BVA_HyphenatedValid() {
        Movie movie = new Movie("X-Men", "XM123", new String[]{});
        assertNull(Validation.validateMovieTitle(movie));
    }

    @Test
    public void testMovieTitle_BVA_HyphenatedInvalid() {
        Movie movie = new Movie("x-Men", "xM123", new String[]{});
        assertNotNull(Validation.validateMovieTitle(movie));
    }

    //------- Movie ID Validation - ECP & BVA -------//

    // ECP: Valid equivalence class - correct format
    @Test
    public void testMovieId_ECP_ValidFormat() {
        Movie movie = new Movie("Spider Man", "SM123", new String[]{});
        assertNull(Validation.validateMovieId(movie));
    }

    // ECP: Invalid equivalence class - wrong prefix
    @Test
    public void testMovieId_ECP_WrongPrefix() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    @Test
    public void testMovieId_ECP_LowercasePrefix() {
        Movie movie = new Movie("Spider Man", "sm123", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    // BVA: Boundary - suffix length
    @Test
    public void testMovieId_BVA_SuffixTooShort() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    @Test
    public void testMovieId_BVA_SuffixTooLong() {
        Movie movie = new Movie("Spider Man", "SM1234", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    @Test
    public void testMovieId_BVA_SuffixExactly3Digits() {
        Movie movie = new Movie("Spider Man", "SM999", new String[]{});
        assertNull(Validation.validateMovieId(movie));
    }

    // ECP: Invalid equivalence class - non-digit in suffix
    @Test
    public void testMovieId_ECP_NonDigitSuffix() {
        Movie movie = new Movie("Spider Man", "SM12A", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    // ECP: Invalid equivalence class - null/empty
    @Test
    public void testMovieId_ECP_Empty() {
        Movie movie = new Movie("Spider Man", "", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    // BVA: Boundary - incomplete prefix
    @Test
    public void testMovieId_BVA_IncompletePrefix() {
        Movie movie = new Movie("Spider Man", "S123", new String[]{});
        String error = Validation.validateMovieId(movie);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters"));
    }

    // ECP: Invalid equivalence class - non-unique digits (with Set parameter)
    @Test
    public void testMovieId_ECP_NonUniqueDigits() {
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});
        String error = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(error);
        assertTrue(error.contains("aren't unique"));
    }

    //------- User Name Validation - ECP & BVA -------//

    // ECP: Valid equivalence class - proper name format
    @Test
    public void testUserName_ECP_ValidName() {
        User user = new User("John Smith", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_ValidSingleName() {
        User user = new User("John", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    // BVA: Boundary - single character name
    @Test
    public void testUserName_BVA_SingleCharacter() {
        User user = new User("A", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Invalid equivalence class - null/empty
    @Test
    public void testUserName_ECP_Null() {
        User user = new User(null, "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_Empty() {
        User user = new User("", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Invalid equivalence class - leading space
    @Test
    public void testUserName_ECP_LeadingSpace() {
        User user = new User(" John", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_MultipleLeadingSpaces() {
        User user = new User("    John", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Invalid equivalence class - contains numbers
    @Test
    public void testUserName_ECP_WithNumbers() {
        User user = new User("John123", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Invalid equivalence class - contains special characters
    @Test
    public void testUserName_ECP_WithSpecialChars() {
        User user = new User("John_Smith", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_WithMultipleSpecialChars() {
        User user = new User("John@132#Smith", "12345678A", null);
        assertNotNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Valid equivalence class - multiple internal spaces allowed
    @Test
    public void testUserName_ECP_MultipleInternalSpaces() {
        User user = new User("John   Smith", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Valid equivalence class - trailing spaces allowed
    @Test
    public void testUserName_ECP_TrailingSpaces() {
        User user = new User("John    ", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    // ECP: Valid equivalence class - case variations
    @Test
    public void testUserName_ECP_Lowercase() {
        User user = new User("johnsmith", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_Uppercase() {
        User user = new User("JOHNSMITH", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    @Test
    public void testUserName_ECP_MixedCase() {
        User user = new User("JoHnSmItH", "12345678A", null);
        assertNull(Validation.validateUserName(user, existingIds));
    }

    //------- User ID Validation - ECP & BVA -------//

    // ECP: Valid equivalence class - 9 digits
    @Test
    public void testUserId_ECP_Valid9Digits() {
        User user = new User("John", "158456678", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    // ECP: Valid equivalence class - 8 digits + letter
    @Test
    public void testUserId_ECP_Valid8DigitsPlusLetter() {
        User user = new User("John", "12345678A", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void testUserId_ECP_Valid8DigitsPlusLowercaseLetter() {
        User user = new User("John", "12345678a", null);
        assertNull(Validation.validateUserId(user, existingIds));
    }

    // ECP: Invalid equivalence class - null/empty
    @Test
    public void testUserId_ECP_Null() {
        User user = new User("John", null, null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void testUserId_ECP_Empty() {
        User user = new User("John", "", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    // BVA: Boundary - length variations
    @Test
    public void testUserId_BVA_TooShort() {
        User user = new User("John", "1234", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void testUserId_BVA_TooLong() {
        User user = new User("John", "12345678901", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void testUserId_BVA_Exactly8Chars() {
        User user = new User("John", "12345678", null);
        assertNotNull(Validation.validateUserId(user, existingIds)); // Invalid: needs letter or 9 digits
    }

    @Test
    public void testUserId_BVA_Exactly10Chars() {
        User user = new User("John", "1234567890", null);
        assertNotNull(Validation.validateUserId(user, existingIds)); // Too long
    }

    // ECP: Invalid equivalence class - letter in wrong position
    @Test
    public void testUserId_ECP_LetterInMiddle() {
        User user = new User("John", "1234A5678", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    @Test
    public void testUserId_ECP_LetterAtStart() {
        User user = new User("John", "A12345678", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }

    // ECP: Invalid equivalence class - duplicate ID
    @Test
    public void testUserId_ECP_Duplicate() {
        User user = new User("John", "123456789", null);
        assertNotNull(Validation.validateUserId(user, existingIds)); // Already exists
    }

    // BVA: Boundary - multiple letters at end
    @Test
    public void testUserId_BVA_MultipleLettersAtEnd() {
        User user = new User("John", "1234567AB", null);
        assertNotNull(Validation.validateUserId(user, existingIds)); // Invalid format
    }

    // ECP: Invalid equivalence class - special characters
    @Test
    public void testUserId_ECP_WithSpecialChars() {
        User user = new User("John", "12345678@", null);
        assertNotNull(Validation.validateUserId(user, existingIds));
    }
}
