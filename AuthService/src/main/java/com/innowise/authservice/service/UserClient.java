package com.innowise.authservice.service;

import com.innowise.authservice.model.dto.UserDto;

/**
 * @interface UserClient
 * @description Defines a contract for communication with the User Service.
 * Provides operations for creating and managing user accounts in a remote service.
 */
public interface UserClient {

    /**
     * Creates a new user in the User Service.
     * This method sends a request to the User Service with the provided user details.
     * The request is authenticated using the supplied JWT token.
     *
     * @param userDto the user data transfer object containing registration details
     * @param token   the JWT token used for authorization when calling the User Service
     */
    String createUser(UserDto userDto, String token);

}
