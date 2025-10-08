package com.innowise.authservice.secutiry;

/**
 * @ClassName AuthConstant
 * @Description Represents constant values used across authentication and security components.
 * @Author dshparko
 * @Date 08.10.2025 15:25
 * @Version 1.0
 */
public final class AuthConstant {
    private AuthConstant() {
    }

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String DEFAULT_ALGORITHM = "SHA-256";
}
