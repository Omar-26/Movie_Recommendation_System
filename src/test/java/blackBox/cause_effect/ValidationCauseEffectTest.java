package blackBox.cause_effect;

import logic.Validation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - Cause-Effect Graph for Validation class
 * Tests cause-effect relationships and constraints
 */
public class ValidationCauseEffectTest {
    private Set<String> existingIds;
    private Set<String> existingMovieIds;

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("123456789");
        existingMovieIds = new HashSet<>();
        existingMovieIds.add("SM112");
    }

    //------- Cause-Effect: Movie Title Validation -------//
    // Cause C1 → Effect E1 (empty title causes error)
    @Test
    public void testMovieTitle_CE_EmptyCausesError() {
        Movie movie = new Movie("", "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // Effect: Error
    }

    // Cause C2 → Effect E1 (first char not uppercase causes error)
    @Test
    public void testMovieTitle_CE_FirstCharNotUpperCausesError() {
        Movie movie = new Movie("matrix", "M123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // Effect: Error
    }

    // Cause C3 → Effect E1 (word starts lowercase causes error)
    @Test
    public void testMovieTitle_CE_WordStartsLowerCausesError() {
        Movie movie = new Movie("The matrix", "TM123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // Effect: Error
    }

    // No causes → Effect E2 (all valid causes valid result)
    @Test
    public void testMovieTitle_CE_NoCausesCausesValid() {
        Movie movie = new Movie("The Matrix", "TM123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNull(result); // Effect: Valid
    }

    //------- Cause-Effect: Movie ID Validation -------//
    // Cause C1 → Effect E1 (wrong prefix causes prefix error)
    @Test
    public void testMovieId_CE_WrongPrefixCausesError() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Effect: Prefix error
    }

    // Cause C2 → Effect E1 (wrong format causes format error)
    @Test
    public void testMovieId_CE_WrongFormatCausesError() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Only 2 digits
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Effect: Format error
    }

    // Cause C3 → Effect E1 (not unique causes uniqueness error)
    @Test
    public void testMovieId_CE_NotUniqueCausesError() {
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("unique")); // Effect: Uniqueness error
    }

    // No causes → Effect E2 (all valid)
    @Test
    public void testMovieId_CE_NoCausesCausesValid() {
        Movie movie = new Movie("Spider Man", "SM999", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNull(result); // Effect: Valid
    }

    //------- Cause-Effect: User Name Validation -------//
    // Cause C1 → Effect E1 (null causes error)
    @Test
    public void testUserName_CE_NullCausesError() {
        User user = new User(null, "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // Cause C2 → Effect E1 (leading space causes error)
    @Test
    public void testUserName_CE_LeadingSpaceCausesError() {
        User user = new User(" John", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // Cause C3 → Effect E1 (invalid chars cause error)
    @Test
    public void testUserName_CE_InvalidCharsCauseError() {
        User user = new User("John123", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // No causes → Effect E2 (valid)
    @Test
    public void testUserName_CE_NoCausesCausesValid() {
        User user = new User("John Smith", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNull(result); // Effect: Valid
    }

    @Test
    public void testUserName_CE_CombinedErrors() {
        User user = new User(" John123", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // Effect: Error
    }
    
    //------- Cause-Effect: User ID Validation -------//
    // Cause C1 → Effect E1 (null causes error)
    @Test
    public void testUserId_CE_NullCausesError() {
        User user = new User("John", null, null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // Cause C2 → Effect E1 (wrong length causes error)
    @Test
    public void testUserId_CE_WrongLengthCausesError() {
        User user = new User("John", "1234", null); // Too short
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // Cause C3 → Effect E1 (wrong format causes error)
    @Test
    public void testUserId_CE_NoStartWithNumber() {
        User user = new User("John", "A12345678", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }
    
    // Cause C4 → Effect E1 (wrong format causes error)
    @Test
    public void testUserId_CE_EndWithOneLetter() {
        User user = new User("John", "12345678A", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result); // Effect: Valid
    }

    // Cause C5 → Effect E1 (duplicate causes error)
    @Test
    public void testUserId_CE_DuplicateCausesError() {
        User user = new User("John", "123456789", null); // Exists
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // No causes → Effect E2 (valid)
    @Test
    public void testUserId_CE_NoCausesCausesValid() {
        User user = new User("John", "987654321", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result); // Effect: Valid
    }
}
