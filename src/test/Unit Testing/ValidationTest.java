import model.User;
import model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValidationTest {
    private Set<String> existingIds;

    //------- User ID Validation Test Cases -------//

    @BeforeEach
    void setUp() {
        existingIds = new HashSet<>();
        existingIds.add("123456789");   // Example existing ID
    }
    @Test
    public void TestcaseNullUserId(){
        User user = new User("John",null,null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseEmptyUserId(){
        User user = new User("John","",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseTooLongUserId(){
        User user = new User("John","12345678901",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseShortUserId(){
        User user = new User("John","1234",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseInvalidCharPlaceUserId(){
        User user = new User("John","1234A5678",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseInvalidRepeatedUserId(){
        User user = new User("John","123456789",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseValidAllDigitsUserId(){
        User user = new User("John","158456678",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNull(result);
    }
    @Test
    public void TestcaseValidDigitsPlusCharUserId(){
        User user = new User("John","12345678A",null);
        String result = Validation.validateUserId(user,existingIds);
        assertNull(result);
    }

    //------- User Name Validation Test Cases -------//


    @Test
    void Testcase1ValidUserName() {
        User user = new User("John Smith", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase2NullUserName() {
        User user = new User(null, "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase3EmptyUserName() {
        User user = new User("", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase4UserNameStartingWithSpace() {
        User user = new User(" John", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase5UserNameStartingWithMultipleSpaces() {
        User user = new User("    John", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase6UserNameWithLettersAndNumbers() {
        User user = new User("John123", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase7User7ameWithLettersAndSpecialChars() {
        User user = new User("John_Smith", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase8UserNameWithLettersNumbersAndSpecialChars() {
        User user = new User("John@132#Smith", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }


    @Test
    void Testcase9UserNameWithNumbersOnly() {
        User user = new User("123", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase10UserNameWithSpecialCharsOnly() {
        User user = new User("@#$", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase11UserNameWithNumbersAndSpecialChars() {
        User user = new User("1@2#3$", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase12UserNameWithMultipleSpacesInside() {
        User user = new User("John   Smith", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase13UserNameEndingWithMultipleSpaces() {
        User user = new User("John    ", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase14LowerCaseUserName() {
        User user = new User("johnsmith", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase15UpperCaseUserName() {
        User user = new User("JOHNSMITH", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase16UpperAndLowerCaseUserName() {
        User user = new User("JoHnSmItH", "12345678A", null);
        String result = Validation.validateUserName(user,existingIds);
        Assertions.assertNull(result);
    }
    
    //------- Movie Title Validation Test Cases -------//
    
    @Test
    void testValidSingleWordTitle() {
        Movie movie = new Movie("Inception", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "Title starting with uppercase should be valid");
    }

    @Test
    void testValidMultiWordTitle() {
        Movie movie = new Movie("The Dark Knight", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "All words starting with uppercase should be valid");
    }

    @Test
    void testNullTitle() {
        Movie movie = new Movie(null, "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
    }

    @Test
    void testEmptyTitle() {
        Movie movie = new Movie("", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);  
        assertNotNull(result);
    }

    @Test
    void testLowercaseStart() {
        Movie movie = new Movie("matrix", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
    }

    @Test
    void testMixedCaseFailure() {
        Movie movie = new Movie("Harry potter", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
    }

    @Test
    void testNumericStart() {
        Movie movie = new Movie("1917", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
    }

    @Test
    void testSymbolStart() {
        Movie movie = new Movie("$money", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        assertNotNull(result);
    }
    
    @Test
    void testTrailingWhitespace() {
        Movie movie = new Movie("Inception ", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "Trailing whitespace should be ignored");
    }

    @Test
    void testMultipleInternalSpaces() {
        Movie movie = new Movie("The   Dark   Knight", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "Multiple spaces between words should be handled safely");
    }

    @Test
    void testHyphenatedWordValid() {
        Movie movie = new Movie("X-Men", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "Hyphenated words starting with uppercase should be valid");
    }

    @Test
    void testHyphenatedWordInvalid() {
        Movie movie = new Movie("x-Men", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        assertNotNull(result, "Hyphenated words starting with lowercase should fail");
    }

    @Test
    void testSeparatedHyphenFailure() {
        Movie movie = new Movie("Spider - Man", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        Assertions.assertNull(result, "A standalone hyphen is not an uppercase letter");
    }

    @Test
    void testApostropheValid() {
        // "Schindler's List". "Schindler's" starts with 'S'.
        // Result: Valid (null).
        Movie movie = new Movie("Schindler's List", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNull(result, "Words containing apostrophes are valid if they start with uppercase");
    }

    @Test
    void testApostropheStartFailure() {
        // "'Tis Pity". Starts with ' (Apostrophe/Single Quote).
        // Result: Invalid (Error string).
        Movie movie = new Movie("'Tis Pity", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNotNull(result, "Starting with an apostrophe should fail");
    }

    @Test
    void testParenthesisFailure() {
        // "(500) Days". Starts with '('.
        // Result: Invalid (Error string).
        Movie movie = new Movie("(500) Days Of Summer", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNotNull(result, "Starting with parenthesis should fail");
    }
    
    @Test
    void testSingleCharacterTitleValid() {
        // "A". Uppercase. Result: Valid (null).
        Movie movie = new Movie("A", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNull(result, "Single uppercase letter title is valid");
    }

    @Test
    void testSingleCharacterTitleInvalid() {
        // "a". Lowercase. Result: Invalid (Error string).
        Movie movie = new Movie("a", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNotNull(result, "Single lowercase letter title is invalid");
    }

    @Test
    void testUnicodeUppercase() {
        // "Ève". 'È' is considered Uppercase in Java.
        // Result: Valid (null).
        Movie movie = new Movie("Ève", "123", new String[]{"Action"});
        String result = Validation.validateMovieTitle(movie);
        
        Assertions.assertNull(result, "Accented uppercase characters should be valid");
    }

     // ------------ VALID MOVIE ID ------------

    @Test
    public void testValidMovieId() {
        Movie movie = new Movie("Spider Man", "SM123", new String[0]);

        String error = Validation.validateMovieId(movie);

        assertNull(error); // means valid
    }

    // ------------ WRONG PREFIX ------------
    @Test
    public void testWrongPrefix() {
        Movie m = new Movie("Spider Man", "SP123",new String[0]);

        String error = Validation.validateMovieId(m);
        assertNotNull(error);
        assertTrue(error.contains("ERROR: Movie Id letters SP123 are wrong"));
    }


    // ------------ LOWERCASE PREFIX ------------
    @Test
    public void testLowercasePrefix() {
        Movie m = new Movie("Spider Man", "sm123",new String[0]);

        String error = Validation.validateMovieId(m);

        assertTrue(error.contains("ERROR: Movie Id letters sm123 are wrong"));
    }

    // ------------ SUFFIX NOT 3 DIGITS ------------
    @Test
    public void testSuffixTooShort() {
        Movie m = new Movie("Spider Man", "SM12",new String[0]);

        String error = Validation.validateMovieId(m);

        assertTrue(error.contains("ERROR: Movie Id letters SM12 are wrong"));
    }

    @Test
    public void testSuffixTooLong() {
        Movie m = new Movie("Spider Man", "SM1234",new String[0]);

        String error = Validation.validateMovieId(m);

        assertTrue(error.contains("ERROR: Movie Id letters SM1234 are wrong"));

    }

    // ------------ NON-DIGIT IN SUFFIX ------------
    @Test
    public void testSuffixNonDigit() {
        Movie m = new Movie("Spider Man", "SM12A",new String[0]);

        String error = Validation.validateMovieId(m);

        assertTrue(error.contains("ERROR: Movie Id letters SM12A are wrong"));
    }

    // ------------ not unique ------------
    @Test
    void testNonUniqueDigits() {
        Movie m = new Movie("Spider Man", "SM112",new String[0]);
        String result = Validation.validateMovieId(m);

        assertNotNull(result);
        assertTrue(result.contains("ERROR: Movie Id numbers SM112 aren't unique"));
    }

    // --------------not complete prefix ---------------
    @Test
    void testNotCompletePrefix(){
        Movie m = new Movie("Spider Man", "S123",new String[0]);
        String result = Validation.validateMovieId(m);
        assertNotNull(result);
        assertTrue(result.contains("ERROR: Movie Id letters S123 are wrong"));
    }

    // --------------empty id -----------------
    @Test
    void testEmptyId(){
        Movie m = new Movie("Spider Man", "",new String[0]);
        String result = Validation.validateMovieId(m);
        assertNotNull(result);
        assertTrue(result.contains("ERROR: Movie Id letters  are wrong"));
    }
}
