package blackBox.decision_table;

import logic.Validation;
import model.Movie;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black Box Testing - Decision Table for Validation class
 * Tests all combinations of conditions using decision table technique
 */
public class ValidationDecisionTableTest {
    private Set<String> existingIds;
    private Set<String> existingMovieIds;

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("123456789");
        existingMovieIds = new HashSet<>();
        existingMovieIds.add("SM112");
    }

    //------- Decision Table: Movie ID Validation -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 | Rule 7 | Rule 8 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Prefix Match (T/F)    |   T    |   T    |   T    |   T    |   F    |   F    |   F    |   F    |
     * | Digit Format (T/F)    |   T    |   T    |   F    |   F    |   T    |   T    |   F    |   F    |
     * | Is Unique (T/F)       |   T    |   F    |   T    |   F    |   T    |   F    |   T    |   F    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Output                |   V    |   U    |   E    |   E    |   E    |   E    |   E    |   E    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Correct
     *   F = False/Incorrect
     *   V = Valid (null returned)
     *   U = Uniqueness Error
     *   E = Format/Prefix Error
     *
     * Interpretation:
     *   Rule 1: Prefix matches, format correct, ID unique → Valid
     *   Rule 2: Prefix matches, format correct, ID not unique → Uniqueness error
     *   Rule 3: Prefix matches, format wrong, ID unique → Format error
     *   Rule 4: Prefix matches, format wrong, ID not unique → Format error (precedence)
     *   Rule 5: Prefix wrong, format correct, ID unique → Prefix error
     *   Rule 6: Prefix wrong, format correct, ID not unique → Prefix error (precedence)
     *   Rule 7: Prefix wrong, format wrong, ID unique → Format error
     *   Rule 8: Prefix wrong, format wrong, ID not unique → Format error
     */

    // Rule 1: Prefix=T, Format=T, Unique=T → V (Valid)
    @Test
    public void testMovieId_DT_Rule1_AllValid() {
        Movie movie = new Movie("Spider Man", "SM123", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNull(result); // V = Valid
    }

    // Rule 2: Prefix=T, Format=T, Unique=F → U (Uniqueness Error)
    @Test
    public void testMovieId_DT_Rule2_NotUnique() {
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("aren't unique")); // U = Uniqueness Error
    }

    // Rule 3: Prefix=T, Format=F, Unique=T → E (Format Error)
    @Test
    public void testMovieId_DT_Rule3_WrongFormat() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Only 2 digits
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Format Error
    }

    // Rule 4: Prefix=T, Format=F, Unique=F → E (Format Error - precedence)
    @Test
    public void testMovieId_DT_Rule4_WrongFormatAndNotUnique() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Wrong format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Format Error
    }

    // Rule 5: Prefix=F, Format=T, Unique=T → E (Prefix Error)
    @Test
    public void testMovieId_DT_Rule5_WrongPrefix() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{}); // Wrong prefix
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Prefix Error
    }

    // Rule 6: Prefix=F, Format=T, Unique=F → E (Prefix Error - precedence)
    @Test
    public void testMovieId_DT_Rule6_WrongPrefixNotUnique() {
        Movie movie = new Movie("Spider Man", "SP112", new String[]{}); // Wrong prefix
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Prefix Error
    }

    // Rule 7: Prefix=F, Format=F, Unique=T → E (Format Error)
    @Test
    public void testMovieId_DT_Rule7_WrongPrefixAndFormat() {
        Movie movie = new Movie("Spider Man", "SP12", new String[]{}); // Wrong prefix and format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Format Error
    }

    // Rule 8: Prefix=F, Format=F, Unique=F → E (Format Error)
    @Test
    public void testMovieId_DT_Rule8_AllInvalid() {
        Movie movie = new Movie("Spider Man", "SP12", new String[]{}); // All wrong
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
        assertTrue(result.contains("are wrong")); // E = Format Error
    }

    //------- Decision Table: User ID Validation -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 | Rule 7 | Rule 8 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Length=9 (T/F)        |   T    |   T    |   F    |   F    |   F    |   F    |   T    |   F    |
     * | Format Valid (T/F)    |   T    |   T    |   T    |   T    |   F    |   F    |   F    |   F    |
     * | Is Duplicate (T/F)    |   F    |   T    |   F    |   T    |   F    |   F    |   F    |   F    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Output                |   V    |   E    |   V    |   E    |   E    |   E    |   E    |   E    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Correct
     *   F = False/Incorrect
     *   V = Valid (null returned)
     *   E = Error (validation failed)
     *
     * Interpretation:
     *   Rule 1: Length=9, all digits, not duplicate → Valid
     *   Rule 2: Length=9, all digits, is duplicate → Duplicate error
     *   Rule 3: Length=8, digit+letter, not duplicate → Valid
     *   Rule 4: Length=8, digit+letter, is duplicate → Duplicate error
     *   Rule 5: Length=8, all digits (invalid format), not duplicate → Format error
     *   Rule 6: Length=other (invalid), any format, not duplicate → Length error
     *   Rule 7: Length=9, invalid format (special char), not duplicate → Format error
     *   Rule 8: Length=8, invalid format (2 letters), not duplicate → Format error
     */

    // Rule 1: Length=9(T), Format Valid=T, Duplicate=F → V (Valid)
    @Test
    public void testUserId_DT_Rule1_Valid9Digits() {
        User user = new User("John", "987654321", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result);
    }

    // Rule 2: Length=9(T), Format Valid=T, Duplicate=T → E (Duplicate Error)
    @Test
    public void testUserId_DT_Rule2_Duplicate9Digits() {
        User user = new User("John", "123456789", null); // Exists
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result);
    }

    // Rule 3: Length=9(F), Format Valid=T, Duplicate=F → V (Valid - 8 digits+letter)
    @Test
    public void testUserId_DT_Rule3_Valid8DigitsLetter() {
        User user = new User("John", "12345678A", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result); // V = Valid
    }

    // Rule 4: Length=9(F), Format Valid=T, Duplicate=T → E (Duplicate Error)
    @Test
    public void testUserId_DT_Rule4_Duplicate8DigitsLetter() {
        existingIds.add("12345678A");
        User user = new User("John", "12345678A", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // E = Duplicate Error
    }

    // Rule 5: Length=9(F), Format Valid=F, Duplicate=F → E (Format Error - needs letter)
    @Test
    public void testUserId_DT_Rule5_Invalid8Digits() {
        User user = new User("John", "12345678", null); // Needs letter
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // E = Format Error
    }

    // Rule 6: Length=9(F), Format Valid=F, Duplicate=F → E (Length Error)
    @Test
    public void testUserId_DT_Rule6_InvalidLength() {
        User user = new User("John", "1234", null); // Too short
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // E = Length Error
    }

    // Rule 7: Length=9(T), Format Valid=F, Duplicate=F → E (Format Error - special char)
    @Test
    public void testUserId_DT_Rule7_Invalid9DigitsFormat() {
        User user = new User("John", "12345678@", null); // Special char
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // E = Format Error
    }

    // Rule 8: Length=9(F), Format Valid=F, Duplicate=F → E (Format Error - 2 letters)
    @Test
    public void testUserId_DT_Rule8_Invalid8DigitsFormat() {
        User user = new User("John", "1234567AB", null); // Two letters
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // E = Format Error
    }

    //------- Decision Table: User Name Validation -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+
     * | Not Empty/Null (T/F)  |   T    |   T    |   T    |   T    |   F    |   F    |
     * | No Leading Space(T/F) |   T    |   T    |   F    |   F    |   -    |   -    |
     * | Valid Chars (T/F)     |   T    |   F    |   T    |   F    |   -    |   -    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+
     * | Output                |   V    |   E    |   E    |   E    |   E    |   E    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Correct
     *   F = False/Incorrect
     *   - = Don't Care (not checked if empty/null)
     *   V = Valid (null returned)
     *   E = Error (validation failed)
     *
     * Interpretation:
     *   Rule 1: Not empty, no leading space, valid chars → Valid
     *   Rule 2: Not empty, no leading space, invalid chars → Invalid chars error
     *   Rule 3: Not empty, has leading space, valid chars → Leading space error
     *   Rule 4: Not empty, has leading space, invalid chars → Leading space error
     *   Rule 5: Empty → Empty error
     *   Rule 6: Null → Null error
     */

    // Rule 1: Not Empty=T, No Leading Space=T, Valid Chars=T → V (Valid)
    @Test
    public void testUserName_DT_Rule1_AllValid() {
        User user = new User("John Smith", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNull(result); // V = Valid
    }

    // Rule 2: Not Empty=T, No Leading Space=T, Valid Chars=F → E (Invalid Chars)
    @Test
    public void testUserName_DT_Rule2_InvalidChars() {
        User user = new User("John123", "123456789", null); // Has numbers
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // E = Invalid Chars Error
    }

    // Rule 3: Not Empty=T, No Leading Space=F, Valid Chars=T → E (Leading Space)
    @Test
    public void testUserName_DT_Rule3_LeadingSpace() {
        User user = new User(" John", "123456789", null); // Leading space
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // E = Leading Space Error
    }

    // Rule 4: Not Empty=T, No Leading Space=F, Valid Chars=F → E (Leading Space)
    @Test
    public void testUserName_DT_Rule4_LeadingSpaceAndInvalidChars() {
        User user = new User(" John123", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // E = Leading Space Error
    }

    // Rule 5: Not Empty=F (Empty), Leading Space=-, Valid Chars=- → E (Empty)
    @Test
    public void testUserName_DT_Rule5_Empty() {
        User user = new User("", "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // E = Empty Error
    }

    // Rule 6: Not Empty=F (Null), Leading Space=-, Valid Chars=- → E (Null)
    @Test
    public void testUserName_DT_Rule6_Null() {
        User user = new User(null, "123456789", null);
        String result = Validation.validateUserName(user, existingIds);
        assertNotNull(result); // E = Null Error
    }

    //------- Decision Table: Movie Title Validation -------//
    /*
     * Decision Table:
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Conditions            | Rule 1 | Rule 2 | Rule 3 | Rule 4 | Rule 5 | Rule 6 | Rule 7 | Rule 8 |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Not Null/Empty (T/F)  |   T    |   T    |   T    |   F    |   F    |   T    |   T    |   T    |
     * | First Char Upper(T/F) |   T    |   T    |   F    |   -    |   -    |   T    |   F    |   F    |
     * | All Words Upper (T/F) |   T    |   F    |   -    |   -    |   -    |   T    |   -    |   -    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     * | Output                |   V    |   E    |   E    |   E    |   E    |   V    |   E    |   E    |
     * +-----------------------+--------+--------+--------+--------+--------+--------+--------+--------+
     *
     * Legend:
     *   T = True/Correct
     *   F = False/Incorrect
     *   - = Don't Care (not checked if previous condition fails)
     *   V = Valid (null returned)
     *   E = Error (validation failed)
     *
     * Interpretation:
     *   Rule 1: Not null/empty, first char uppercase, all words uppercase → Valid
     *   Rule 2: Not null/empty, first char uppercase, some word lowercase → Error
     *   Rule 3: Not null/empty, first char lowercase → Error
     *   Rule 4: Null → Null error
     *   Rule 5: Empty → Empty error
     *   Rule 6: Not null/empty, single word uppercase → Valid
     *   Rule 7: Not null/empty, single word lowercase → Error
     *   Rule 8: Not null/empty, starts with number → Error
     */

    // Rule 1: Not Null/Empty=T, First Char Upper=T, All Words Upper=T → V (Valid)
    @Test
    public void testMovieTitle_DT_Rule1_AllValid() {
        Movie movie = new Movie("The Dark Knight", "TDK123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNull(result); // V = Valid
    }

    // Rule 2: Not Null/Empty=T, First Char Upper=T, All Words Upper=F → E (Word Error)
    @Test
    public void testMovieTitle_DT_Rule2_NotAllWordsUpper() {
        Movie movie = new Movie("The dark Knight", "TDK123", new String[]{}); // 'dark' lowercase
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Word Not Uppercase Error
    }

    // Rule 3: Not Null/Empty=T, First Char Upper=F, All Words Upper=- → E (First Char)
    @Test
    public void testMovieTitle_DT_Rule3_FirstNotUpper() {
        Movie movie = new Movie("the Dark Knight", "TDK123", new String[]{}); // First lowercase
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = First Char Not Uppercase Error
    }

    // Rule 4: Not Null/Empty=F (Null), First Char Upper=-, All Words Upper=- → E (Null)
    @Test
    public void testMovieTitle_DT_Rule4_Null() {
        Movie movie = new Movie(null, "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Null Error
    }

    // Rule 5: Not Null/Empty=F (Empty), First Char Upper=-, All Words Upper=- → E (Empty)
    @Test
    public void testMovieTitle_DT_Rule5_Empty() {
        Movie movie = new Movie("", "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Empty Error
    }

    // Rule 6: Not Null/Empty=T, First Char Upper=T, All Words Upper=T → V (Single Word)
    @Test
    public void testMovieTitle_DT_Rule6_SingleWordValid() {
        Movie movie = new Movie("Inception", "I123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNull(result); // V = Valid
    }

    // Rule 7: Not Null/Empty=T, First Char Upper=F, All Words Upper=- → E (Single Word Lower)
    @Test
    public void testMovieTitle_DT_Rule7_SingleWordInvalid() {
        Movie movie = new Movie("inception", "I123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = First Char Not Uppercase Error
    }

    // Rule 8: Not Null/Empty=T, First Char Upper=F (Number), All Words Upper=- → E (Number)
    @Test
    public void testMovieTitle_DT_Rule8_StartsWithNumber() {
        Movie movie = new Movie("1917", "123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Starts With Number Error
    }
}
