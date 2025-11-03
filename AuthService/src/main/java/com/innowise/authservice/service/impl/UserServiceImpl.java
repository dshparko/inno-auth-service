package com.innowise.authservice.service.impl;

import com.innowise.authservice.exception.ResourceAlreadyUsedException;
import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.model.dto.AuthDto;
import com.innowise.authservice.model.entity.Credential;
import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.repository.CredentialRepository;
import com.innowise.authservice.repository.UserRepository;
import com.innowise.authservice.secutiry.PasswordEncoder;
import com.innowise.authservice.service.UserClient;
import com.innowise.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserClient userClient;

    @Transactional
    public void register(AuthDto request, Role role, String token) {
        if (credentialRepository.existsByEmail(request.getCredentials().email())) {
            throw new ResourceAlreadyUsedException("Email " + request.getCredentials().email() + " is already in use");
        }

        User user = new User();
        user.setRole(role);
        userRepository.save(user);

        String salt = encoder.generateSalt();
        String hash = encoder.encode(request.getCredentials().password(), salt);

        Credential credential = new Credential();
        credential.setEmail(request.getCredentials().email());
        credential.setSalt(salt);
        credential.setPasswordHash(hash);
        credential.setUser(user);
        credentialRepository.save(credential);

        userClient.createUser(request.getUserData(), token);
    }

    public User findById(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"))
                .getUser();
    }
}