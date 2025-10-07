package com.innowise.authservice.secutiry;

import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

/**
 * Interface defining JWT token operations.
 *
 * @Author dshparko
 * @Date 04.10.2025 19:24
 * @Version 1.0
 */
public interface JwtService {

    /**
     * Generates a signed access token for the given user.
     *
     * @param user the authenticated user
     * @return JWT access token string
     */
    String generateAccessToken(UserDetails user);

    /**
     * Generates a signed refresh token for the given user.
     *
     * @param user the authenticated user
     * @return JWT refresh token string
     */
    String generateRefreshToken(UserDetails user);

    /**
     * Extracts the email (subject) from the given token.
     *
     * @param token the JWT token
     * @return email string
     */
    String extractEmail(String token);

    /**
     * Extracts the role claim from the given token.
     *
     * @param token the JWT token
     * @return role string (e.g., "USER", "ADMIN")
     */
    String extractRole(String token);

    /**
     * Validates the token against the expected email and expiration.
     *
     * @param token the JWT token
     * @param email the expected email
     * @return true if valid, false otherwise
     */
    boolean isTokenValid(String token, String email);

    /**
     * Extracts a specific claim from the token using a resolver function.
     *
     * @param token    the JWT token
     * @param resolver function to extract desired claim
     * @param <T>      type of the claim
     * @return extracted claim value
     */
    <T> T extractClaim(String token, Function<Claims, T> resolver);
}

