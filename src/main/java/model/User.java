package model;

/**
 * Represents a User using their username, salt, and password
 */
public class User {
    private final String username;
    private final String salt;
    private final String passwordHash;

    /**
     * @pre username != null && salt != null && passwordHash != null
     */
    public User(String username, String salt, String passwordHash) {
        this.username = username;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}