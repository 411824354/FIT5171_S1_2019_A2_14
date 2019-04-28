package rockets.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class UserUnitTest {
    private User target;

    @BeforeEach
    public void setUp() {
        target = new User();
    }


    @DisplayName("should throw exception when pass a empty email address to setEmail function")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    public void shouldThrowExceptionWhenSetEmailToEmpty(String email) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exception when pass null to setEmail function")
    @Test
    public void shouldThrowExceptionWhenSetEmailToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
    }

    @DisplayName("should throw exceptions when pass a null password to setPassword function")
    @Test
    public void shouldThrowExceptionWhenSetPasswordToNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> target.setPassword(null));
        assertEquals("password cannot be null or empty", exception.getMessage());
    }

    //test jenkins change
    @DisplayName("should return true when two users have the same email")
    @Test
    public void shouldReturnTrueWhenUsersHaveSameEmail() {
        String email = "abc@example.com";
        target.setEmail(email);
        User anotherUser = new User();
        anotherUser.setEmail(email);
        assertTrue(target.equals(anotherUser));
    }


    @DisplayName("should return false when two users have different emails")
    @Test
    public void shouldReturnFalseWhenUsersHaveDifferentEmails() {
        target.setEmail("abc@example.com");
        User anotherUser = new User();
        anotherUser.setEmail("def@example.com");
        assertFalse(target.equals(anotherUser));
    }

    @DisplayName("a user need to have a non-empty last name")
    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    public void shouldNotEmptyLastName(String lastName){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setLastName(lastName));
        assertEquals("the last name cannot be null or empty",exception.getMessage());
    }

    @DisplayName("email should in correct format")
    @ParameterizedTest
    @ValueSource(strings = {"sdfsf","abc@123","aabbcc@gmialcome","piaogu1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111angxue@gmail.com"})
    public void emailShouldInCorrectFormat(String email){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setEmail(email));
        assertEquals("incorrect email format e.g:asdf@gmail.com",exception.getMessage());
    }

    @DisplayName("the password should involve letter and number and length in 6-12 and not involve blank")
    @ParameterizedTest
    @ValueSource(strings = {"sdfsf", "123 123","12123","12ee","11","aa","123  abcd"})
    public void shouldInPasswordFormat(String lastName){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> target.setPassword(lastName));
        assertEquals("incorrect password format",exception.getMessage());
    }

    @DisplayName("correct password format test")
    @Test
    public void shouldAssignSuccessPassword(){
        target.setPassword("123abcd");
        assertEquals("123abcd",target.getPassword());
    }

    @DisplayName("correct email format test")
    @Test
    public void shouldAssignSuccessEmail(){
        target.setEmail("tom123@gmail.com");
        assertEquals("tom123@gmail.com",target.getEmail());
    }

}