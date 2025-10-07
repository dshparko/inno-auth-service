package com.innowise.authservice.service;

import com.innowise.authservice.model.dto.AuthDto;
import com.innowise.authservice.model.dto.AuthenticationResponse;
import com.innowise.authservice.model.dto.LoginDto;
import com.innowise.authservice.model.dto.TokenPayload;

/**
 * Interface defining authentication and token lifecycle operations.
 * Used for login, registration, token validation, and refresh logic.
 *
 * @author dshparko
 * @version 1.0
 * @since 04.10.2025
 */
public interface AuthService {

    /**
     * Authenticates user and returns access/refresh tokens.
     *
     * @param request login credentials
     * @return JWT token pair
     */
    AuthenticationResponse login(LoginDto request);

    /**
     * Registers a new user with role-based access control.
     *
     * @param request    user creation payload
     * @param authHeader optional Authorization header
     */
    void register(AuthDto request, String authHeader);

    /**
     * Validates access or refresh token.
     *
     * @param token payload containing JWT
     * @return email extracted from token
     */
    String validate(TokenPayload token);

    /**
     * Refreshes access and refresh tokens using a valid refresh token.
     *
     * @param request payload containing refresh token
     * @return new JWT token pair
     */
    AuthenticationResponse refresh(TokenPayload request);
}
