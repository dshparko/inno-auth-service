package com.innowise.authservice.secutiry;


/**
 * Interface defining password encoding and verification operations.
 *
 * @Author dshparko
 * @Date 04.10.2025 19:35
 * @Version 1.0
 */
public interface PasswordEncoder {

    /**
     * Generates a cryptographically secure random salt.
     *
     * @return base64-encoded salt string
     */
    String generateSalt();

    /**
     * Encodes the raw password using the provided salt.
     *
     * @param rawPassword the plain password
     * @param salt        the salt to use
     * @return base64-encoded hash
     */
    String encode(String rawPassword, String salt);

    /**
     * Verifies that the raw password matches the stored hash using the provided salt.
     *
     * @param rawPassword the plain password
     * @param salt        the salt used during encoding
     * @param storedHash  the previously stored hash
     * @return true if the password matches, false otherwise
     */
    boolean matches(String rawPassword, String salt, String storedHash);
}
