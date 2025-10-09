package com.innowise.authservice.service.impl;

import com.innowise.authservice.exception.ResourceAlreadyUsedException;
import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.secutiry.PasswordEncoder;
import com.innowise.authservice.model.entity.Credential;
import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.repository.CredentialRepository;
import com.innowise.authservice.repository.UserRepository;
import com.innowise.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description Service responsible for user creation and lookup operations.
 * @Author dshparko
 * @Date 06.10.2025 21:43
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void register(String email, String rawPassword, Role role) {
        if (credentialRepository.existsByEmail(email)) {
            throw new ResourceAlreadyUsedException("Email" + email + "already in use");
        }

        User user = new User();
        user.setRole(role);
        userRepository.save(user);

        String salt = encoder.generateSalt();
        String hash = encoder.encode(rawPassword, salt);

        Credential credential = new Credential();
        credential.setEmail(email);
        credential.setSalt(salt);
        credential.setPasswordHash(hash);
        credential.setUser(user);
        credentialRepository.save(credential);
    }

    public User findByEmail(String email) {
        return credentialRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + "not found"))
                .getUser();
    }
}