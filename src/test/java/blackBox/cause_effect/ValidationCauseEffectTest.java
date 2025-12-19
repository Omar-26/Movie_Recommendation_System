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
    // Causes: C1=Title is null/empty, C2=First char not uppercase, C3=Word starts lowercase
    // Effects: E1=Return error, E2=Return null (valid)
    // Constraints: If C1 then E1 (skip other checks)

    // Cause C1 → Effect E1 (null title causes error)
    @Test
    public void testMovieTitle_CE_NullCausesError() {
        Movie movie = new Movie(null, "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // Effect: Error
    }

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

    // Constraint: C1 takes precedence (null/empty checked first)
    @Test
    public void testMovieTitle_CE_NullTakesPrecedence() {
        Movie movie = new Movie(null, "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
        assertTrue(result.contains("null")); // Null error, not format error
    }

    //------- Cause-Effect: Movie ID Validation -------//
    // Causes: C1=Wrong prefix, C2=Wrong digit format, C3=Not unique
    // Effects: E1=Prefix error, E2=Format error, E3=Uniqueness error, E4=Valid
    // Constraints: If C1 or C2 then skip C3 check

    // Cause C1 → Effect E1 (wrong prefix causes prefix error)
    @Test
    public void testMovieId_CE_WrongPrefixCausesError() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Effect: Prefix error
    }

    // Cause C2 → Effect E2 (wrong format causes format error)
    @Test
    public void testMovieId_CE_WrongFormatCausesError() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Only 2 digits
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Effect: Format error
    }

    // Cause C3 → Effect E3 (not unique causes uniqueness error)
    @Test
    public void testMovieId_CE_NotUniqueCausesError() {
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("unique")); // Effect: Uniqueness error
    }

    // No causes → Effect E4 (all valid)
    @Test
    public void testMovieId_CE_NoCausesCausesValid() {
        Movie movie = new Movie("Spider Man", "SM999", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNull(result); // Effect: Valid
    }

    // Constraint: Format error takes precedence over uniqueness
    @Test
    public void testMovieId_CE_FormatTakesPrecedenceOverUniqueness() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Wrong format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Format error, not uniqueness
    }

    // Constraint: Prefix error takes precedence
    @Test
    public void testMovieId_CE_PrefixTakesPrecedence() {
        Movie movie = new Movie("Spider Man", "SP12", new String[]{}); // Wrong prefix and format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("letters")); // Prefix/format error
    }

    //------- Cause-Effect: User Name Validation -------//
    // Causes: C1=Null/empty, C2=Leading space, C3=Invalid chars
    // Effects: E1=Error, E2=Valid
    // Constraints: If C1 then E1 (skip other checks)

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

    // Constraint: Null/empty checked first
    @Test
    public void testUserName_CE_NullTakesPrecedence() {
        User user = new User(null, "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result);
        assertTrue(result.contains("null")); // Null error
    }

    // Combined causes: C2 AND C3 → Effect E1
    @Test
    public void testUserName_CE_MultipleCausesCauseError() {
        User user = new User(" John123", "123456789", null); // Leading space AND invalid chars
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // Effect: Error (either cause triggers error)
    }

    //------- Cause-Effect: User ID Validation -------//
    // Causes: C1=Null/empty, C2=Wrong length, C3=Wrong format, C4=Duplicate
    // Effects: E1=Error, E2=Valid
    // Constraints: If C1 then E1, If C2 or C3 then E1 (skip C4)

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
    public void testUserId_CE_WrongFormatCausesError() {
        User user = new User("John", "12345678@", null); // Invalid char
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    // Cause C4 → Effect E1 (duplicate causes error)
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

    // Constraint: Format error takes precedence over duplicate check
    @Test
    public void testUserId_CE_FormatTakesPrecedenceOverDuplicate() {
        User user = new User("John", "1234", null); // Wrong length (format error)
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Format error, not duplicate check
    }

    // Combined causes: C2 AND C3 → Effect E1
    @Test
    public void testUserId_CE_MultipleCausesCauseError() {
        User user = new User("John", "123@", null); // Wrong length AND wrong format
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Effect: Error
    }

    //------- Cause-Effect: Validation Interactions -------//
    // Test that validation methods are independent

    // Movie title error does NOT affect movie ID validation
    @Test
    public void testCE_MovieTitleErrorIndependent() {
        Movie movie = new Movie("invalid", "I123", new String[]{}); // Invalid title
        String titleResult = Validation.validateMovieTitle(movie);
        String idResult = Validation.validateMovieId(movie);
        
        assertNotNull(titleResult); // Title error
        assertNull(idResult); // ID still valid independently
    }

    // User name error does NOT affect user ID validation
    @Test
    public void testCE_UserNameErrorIndependent() {
        User user = new User(" Invalid", "987654321", null); // Invalid name
        String nameResult = Validation.validateUserName(user, existingIds);
        String idResult = Validation.validateUserId(user, existingIds);
        
        assertNotNull(nameResult); // Name error
        assertNull(idResult); // ID still valid independently
    }
}
