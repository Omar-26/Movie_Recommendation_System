import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ValidationTest {
    private Set<String> existingIds;

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
}
