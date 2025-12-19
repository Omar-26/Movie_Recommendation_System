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
        existingMovieIds = new HashSet<>();
    }

    // Rule 1: Prefix=T, Numbers=T, Unique=T → V (Valid)
    @Test
    public void testMovieId_DT_Rule1_AllValid() {
        Movie movie = new Movie("Spider Man", "SM123", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNull(result);
    }

    // Rule 2: Prefix=T, Numbers=T, Unique=F → E (Error)
    @Test
    public void testMovieId_DT_Rule2_NotUnique() {
        existingMovieIds.add("SM112");
        Movie movie = new Movie("Spider Man", "SM112", new String[]{});
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 3: Prefix=T, Numbers=F, Unique=T → E (Error)
    @Test
    public void testMovieId_DT_Rule3_WrongFormat() {
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Only 2 digits
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 4: Prefix=T, Numbers=F, Unique=F → E (Error)
    @Test
    public void testMovieId_DT_Rule4_WrongFormatAndNotUnique() {
        existingMovieIds.add("SM12");
        Movie movie = new Movie("Spider Man", "SM12", new String[]{}); // Wrong format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 5: Prefix=F, Numbers=T, Unique=T → E (Error)
    @Test
    public void testMovieId_DT_Rule5_WrongPrefix() {
        Movie movie = new Movie("Spider Man", "SP123", new String[]{}); // Wrong prefix
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 6: Prefix=F, Numbers=T, Unique=F → E (Error)
    @Test
    public void testMovieId_DT_Rule6_WrongPrefixNotUnique() {
        existingMovieIds.add("SP112");
        Movie movie = new Movie("Spider Man", "SP112", new String[]{}); // Wrong prefix
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 7: Prefix=F, Format=F, Unique=T → E (Error)
    @Test
    public void testMovieId_DT_Rule7_WrongPrefixAndFormat() {
        Movie movie = new Movie("Spider Man", "SP12", new String[]{}); // Wrong prefix and format
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    // Rule 8: Prefix=F, Format=F, Unique=F → E (Error)
    @Test
    public void testMovieId_DT_Rule8_AllInvalid() {
        existingMovieIds.add("SP12");
        Movie movie = new Movie("Spider Man", "SP12", new String[]{}); // All wrong
        String result = Validation.validateMovieId(movie, existingMovieIds);
        assertNotNull(result);
    }

    //------- Decision Table: User ID Validation -------//
    // 16 Rules based on: Length=9, Start with Number, End with One Letter, Is Unique

    // Rule 1: Length=9(T), StartNum=T, EndOneLetter=T, Unique=T → V (Valid)
    @Test
    public void testUserId_DT_Rule1() {
        User user = new User("John", "12345678A", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result); // Valid
    }

    // Rule 2: Length=9(T), StartNum=T, EndOneLetter=T, Unique=F → E (Error - not unique)
    @Test
    public void testUserId_DT_Rule2() {
        existingIds.add("987654321A");
        User user = new User("John", "987654321A", null); // Already exists
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 3: Length=9(T), StartNum=T, EndOneLetter=F, Unique=T → V (Valid)
    @Test
    public void testUserId_DT_Rule3() {
        User user = new User("John", "987654321", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNull(result); // Valid
    }

    // Rule 4: Length=9(T), StartNum=T, EndOneLetter=F, Unique=F → E (Error - not unique)
    @Test
    public void testUserId_DT_Rule4() {
        existingIds.add("123456789");
        User user = new User("John", "123456789", null); // Already exists
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 5: Length=9(T), StartNum=F, EndOneLetter=T, Unique=T → E (Error - doesn't start with number)
    @Test
    public void testUserId_DT_Rule5() {
        User user = new User("John", "A12345678", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 6: Length=9(T), StartNum=F, EndOneLetter=T, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule6() {
        existingIds.add("B12345678");
        User user = new User("John", "B12345678", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 7: Length=9(T), StartNum=F, EndOneLetter=F, Unique=T → E (Error - doesn't start with number)
    @Test
    public void testUserId_DT_Rule7() {
        User user = new User("John", "A23456789", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 8: Length=9(T), StartNum=F, EndOneLetter=F, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule8() {
        existingIds.add("C23456789");
        User user = new User("John", "C23456789", null);
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 9: Length=9(F), StartNum=T, EndOneLetter=T, Unique=T → E (Error - wrong length)
    @Test
    public void testUserId_DT_Rule9() {
        User user = new User("John", "1234567A", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 10: Length=9(F), StartNum=T, EndOneLetter=T, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule10() {
        existingIds.add("2345678B");
        User user = new User("John", "2345678B", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 11: Length=9(F), StartNum=T, EndOneLetter=F, Unique=T → E (Error - wrong length)
    @Test
    public void testUserId_DT_Rule11() {
        User user = new User("John", "12345678", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 12: Length=9(F), StartNum=T, EndOneLetter=F, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule12() {
        existingIds.add("23456789");
        User user = new User("John", "23456789", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 13: Length=9(F), StartNum=F, EndOneLetter=T, Unique=T → E (Error)
    @Test
    public void testUserId_DT_Rule13() {
        User user = new User("John", "A123456B", null); // Only 8 chars, doesn't start with number
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 14: Length=9(F), StartNum=F, EndOneLetter=T, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule14() {
        existingIds.add("B234567C");
        User user = new User("John", "B234567C", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 15: Length=9(F), StartNum=F, EndOneLetter=F, Unique=T → E (Error)
    @Test
    public void testUserId_DT_Rule15() {
        User user = new User("John", "A1234567", null); // Only 8 chars, doesn't start with number
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }

    // Rule 16: Length=9(F), StartNum=F, EndOneLetter=F, Unique=F → E (Error)
    @Test
    public void testUserId_DT_Rule16() {
        existingIds.add("B2345678");
        User user = new User("John", "B2345678", null); // Only 8 chars
        String result = Validation.validateUserId(user, existingIds);
        assertNotNull(result); // Error
    }


    //------- Decision Table: User Name Validation -------//
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


    //------- Decision Table: Movie Title Validation -------//

    // Rule 1: OneWord=T, AllWordsStartUpper=T → V (Valid)
    @Test
    public void testMovieTitle_DT_Rule1_OneWordCapitalized() {
        Movie movie = new Movie("Inception", "I123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNull(result); // V = Valid
    }

    // Rule 2: OneWord=T, AllWordsStartUpper=F → E (Error - not capitalized)
    @Test
    public void testMovieTitle_DT_Rule2_OneWordNotCapitalized() {
        Movie movie = new Movie("inception", "I123", new String[]{}); // lowercase
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Error
    }

    // Rule 3: OneWord=F, AllWordsStartUpper=T → V (Valid - multiple words, all capitalized)
    @Test
    public void testMovieTitle_DT_Rule3_MultipleWordsCapitalized() {
        Movie movie = new Movie("Spider Man", "SM123", new String[]{});
        String result = Validation.validateMovieTitle(movie);
        assertNull(result); // V = Valid
    }

    // Rule 4: OneWord=F, AllWordsStartUpper=F → E (Error - multiple words, not all capitalized)
    @Test
    public void testMovieTitle_DT_Rule4_MultipleWordsNotAllCapitalized() {
        Movie movie = new Movie("Spider man", "SM123", new String[]{}); // 'man' not capitalized
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result); // E = Error
    }
}
