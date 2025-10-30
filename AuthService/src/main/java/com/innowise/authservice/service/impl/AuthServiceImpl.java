package com.innowise.authservice.service.impl;

import com.innowise.authservice.exception.InvalidResourceException;
import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.model.RoleEnum;
import com.innowise.authservice.model.dto.AuthDto;
import com.innowise.authservice.model.dto.AuthenticationResponse;
import com.innowise.authservice.model.dto.LoginDto;
import com.innowise.authservice.model.dto.TokenInfo;
import com.innowise.authservice.model.dto.TokenPayload;
import com.innowise.authservice.model.entity.Credential;
import com.innowise.authservice.model.entity.Role;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.repository.CredentialRepository;
import com.innowise.authservice.repository.RoleRepository;
import com.innowise.authservice.secutiry.JwtService;
import com.innowise.authservice.secutiry.PasswordEncoder;
import com.innowise.authservice.secutiry.impl.UserPrincipal;
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
    private final PasswordEncoder encoder;


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
        String requestedRoleName = request.getCredentials().role().toUpperCase();

        Role role = roleRepository.findByName(requestedRoleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role '%s' was not found".formatted(requestedRoleName)));

        String creatorRoleName = resolveCreatorRole(token);

        validateRoleAssignment(requestedRoleName, creatorRoleName);

        userService.register(
                request,
                role,
                token
        );
    }

    public TokenInfo validate(TokenPayload token) {
        String rawToken = extractRawToken(token);
        validateExpiration(rawToken);

        String email = extractAndValidateEmail(rawToken);
        String role = extractAndValidateRole(rawToken);
        String type = extractAndValidateType(rawToken);

        validateClaims(rawToken, email, role);

        return new TokenInfo(email, role, type);
    }


    public AuthenticationResponse refresh(TokenPayload request) {
        String token = request.token();
        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        if (!jwtService.isTokenValid(token, email, role)) {
            throw new InvalidResourceException("Refresh token is invalid");
        }

        if (!jwtService.isRefreshToken(token)) {
            throw new InvalidResourceException("We can't refresh access token");
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

    private String extractRawToken(TokenPayload token) {
        String rawToken = token.token();
        if (rawToken == null || rawToken.isBlank()) {
            throw new InvalidResourceException("Token is missing or empty");
        }
        return rawToken;
    }


    private void validateExpiration(String token) {
        if (jwtService.isTokenExpired(token)) {
            throw new InvalidResourceException("Token has expired");
        }
    }


    private String extractAndValidateEmail(String token) {
        String email = jwtService.extractEmail(token);
        if (email == null || email.isBlank()) {
            throw new InvalidResourceException("Token does not contain a valid email");
        }
        return email;
    }

    private String extractAndValidateRole(String token) {
        String role = jwtService.extractRole(token);
        if (role == null || role.isBlank()) {
            throw new InvalidResourceException("Token does not contain a valid role");
        }
        return role;
    }

    private String extractAndValidateType(String token) {
        String type = jwtService.extractType(token);
        if (type == null || type.isBlank()) {
            throw new InvalidResourceException("Token does not contain a valid role");
        }
        return type;
    }

    private void validateClaims(String token, String expectedEmail, String expectedRole) {
        if (!jwtService.isTokenValid(token, expectedEmail, expectedRole)) {
            throw new InvalidResourceException("Token is invalid");
        }
    }

    private String resolveCreatorRole(String token) {
        if (token == null || token.isBlank()) {
            return RoleEnum.USER.name();
        }
        return jwtService.extractRole(token);
    }

    private void validateRoleAssignment(String requestedRole, String creatorRole) {
        if (RoleEnum.ADMIN.name().equals(requestedRole) && !RoleEnum.ADMIN.name().equals(creatorRole)) {
            throw new AccessDeniedException("Only ADMIN can register ADMIN users");
        }
    }


}
