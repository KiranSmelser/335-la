package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests for User class
 */
class UserTest {

    private User user;

    /**
     * Constructs a new User object before each test
     */
    @BeforeEach
    void setUp() {
        user = new User("testUser", "randomSalt", "hashedPassword123");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("testUser", user.getUsername());
        assertEquals("randomSalt", user.getSalt());
        assertEquals("hashedPassword123", user.getPasswordHash());
    }

    @Test
    void testNotEqualsWhenUsernameDiffers() {
        User user2 = new User("otherUser", "randomSalt", "hashedPassword123");
        assertNotEquals(user2.getUsername(), user.getUsername());
    }

    @Test
    void testSameSaltDifferentHash() {
        User user2 = new User("testUser", "randomSalt", "someOtherHash456");
        assertEquals(user.getSalt(), user2.getSalt());
        assertNotEquals(user.getPasswordHash(), user2.getPasswordHash());
    }
}