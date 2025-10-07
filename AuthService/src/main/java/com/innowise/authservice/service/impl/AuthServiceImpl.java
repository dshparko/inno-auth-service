package com.innowise.authservice.service.impl;

import com.innowise.authservice.exception.InvalidResourceException;
import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.secutiry.impl.SaltedPasswordEncoder;
import com.innowise.authservice.model.dto.LoginDto;
import com.innowise.authservice.model.dto.AuthDto;
import com.innowise.authservice.model.entity.Credential;
import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.secutiry.JwtService;
import com.innowise.authservice.secutiry.impl.UserPrincipal;
import com.innowise.authservice.repository.CredentialRepository;
import com.innowise.authservice.repository.RoleRepository;
import com.innowise.authservice.model.dto.AuthenticationResponse;
import com.innowise.authservice.model.dto.TokenPayload;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.service.AuthService;
import com.innowise.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


/**
 * @ClassName AuthService
 * @Description Service responsible for authentication, registration, and token lifecycle operations.
 * @Author dshparko
 * @Date 04.10.2025 19:26
 * @Version 1.0
 */
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final SaltedPasswordEncoder encoder;


    public AuthenticationResponse login(LoginDto request) {
        Credential credential = credentialRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Credential with email " + request.email() + " wasn't found"));

        if (!encoder.matches(request.password(), credential.getSalt(), credential.getPasswordHash())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        User user = credential.getUser();
        UserPrincipal principal = UserPrincipal.of(user);

        return new AuthenticationResponse(
                jwtService.generateAccessToken(principal),
                jwtService.generateRefreshToken(principal)
        );
    }

    public void register(AuthDto request, String authHeader) {
        String token = extractToken(authHeader);
        String requestedRole = request.role().toUpperCase();

        Role role = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + requestedRole + " was not found"));

        String creatorRole = (token == null || token.isBlank())
                ? "USER"
                : jwtService.extractRole(token);

        if ("ADMIN".equals(requestedRole) && !"ADMIN".equals(creatorRole)) {
            throw new AccessDeniedException("Only ADMIN can register ADMIN users");
        }

        userService.register(request.email(), request.password(), role);
    }

    public String validate(TokenPayload token) {
        String email = jwtService.extractEmail(token.token());

        if (!jwtService.isTokenValid(token.token(), email)) {
            throw new InvalidResourceException("Invalid or expired token");
        }

        return email;
    }


    public AuthenticationResponse refresh(TokenPayload request) {
        String token = request.token();
        String email = jwtService.extractEmail(token);

        if (!jwtService.isTokenValid(token, email)) {
            throw new InvalidResourceException("Refresh token is invalid");
        }

        User user = userService.findByEmail(email);
        UserPrincipal principal = UserPrincipal.of(user);

        return new AuthenticationResponse(
                jwtService.generateAccessToken(principal),
                jwtService.generateRefreshToken(principal)
        );
    }

    private String extractToken(String authHeader) {
        return (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : null;
    }

}
