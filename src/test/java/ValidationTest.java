import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseEmptyUserId(){
        User user = new User("John","",null);
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseTooLongUserId(){
        User user = new User("John","12345678901",null);
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseShortUserId(){
        User user = new User("John","1234",null);
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseInvalidCharPlaceUserId(){
        User user = new User("John","1234A5678",null);
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseInvalidRepeatedUserId(){
        User user = new User("John","123456789",null);
        String result = Validation.validateUser(user,existingIds);
        assertNotNull(result);
    }
    @Test
    public void TestcaseValidAllDigitsUserId(){
        User user = new User("John","158456678",null);
        String result = Validation.validateUser(user,existingIds);
        assertNull(result);
    }
    @Test
    public void TestcaseValidDigitsPlusCharUserId(){
        User user = new User("John","12345678A",null);
        String result = Validation.validateUser(user,existingIds);
        assertNull(result);
    }

    //------- User Name Validation Test Cases -------//


    @Test
    void Testcase1ValidUserName() {
        User user = new User("John Smith", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase2NullUserName() {
        User user = new User(null, "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase3EmptyUserName() {
        User user = new User("", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase4UserNameStartingWithSpace() {
        User user = new User(" John", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase5UserNameStartingWithMultipleSpaces() {
        User user = new User("    John", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase6UserNameWithLettersAndNumbers() {
        User user = new User("John123", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase7User7ameWithLettersAndSpecialChars() {
        User user = new User("John_Smith", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase8UserNameWithLettersNumbersAndSpecialChars() {
        User user = new User("John@132#Smith", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }


    @Test
    void Testcase9UserNameWithNumbersOnly() {
        User user = new User("123", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase10UserNameWithSpecialCharsOnly() {
        User user = new User("@#$", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase11UserNameWithNumbersAndSpecialChars() {
        User user = new User("1@2#3$", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        assertNotNull(result);
    }

    @Test
    void Testcase12UserNameWithMultipleSpacesInside() {
        User user = new User("John   Smith", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase13UserNameEndingWithMultipleSpaces() {
        User user = new User("John    ", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase14LowerCaseUserName() {
        User user = new User("johnsmith", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase15UpperCaseUserName() {
        User user = new User("JOHNSMITH", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

    @Test
    void Testcase16UpperAndLowerCaseUserName() {
        User user = new User("JoHnSmItH", "12345678A", null);
        String result = Validation.validateUserNameHelper(user,existingIds);
        Assertions.assertNull(result);
    }

}
