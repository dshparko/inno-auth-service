package com.innowise.authservice.service;

import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface defining user creation and lookup operations.
 * Used for secure registration and retrieval of user entities by email.
 *
 * @author dshparko
 * @version 1.0
 * @since 06.10.2025
 */
public interface UserService {

    /**
     * Registers a new user with the given email, password, and role.
     * <p>
     * Ensures email uniqueness and securely hashes the password with a generated salt.
     * </p>
     *
     * @param email       user's email
     * @param rawPassword raw password to be hashed
     * @param role        role to assign
     */
    void register(String email, String rawPassword, Role role) ;

    /**
     * Finds a user by their email address.
     * <p>
     * Looks up the credential and returns the associated user entity.
     * </p>
     *
     * @param email user's email
     * @return user entity
     * @throws UsernameNotFoundException if no credential is found
     */
    User findByEmail(String email) throws UsernameNotFoundException;
}

