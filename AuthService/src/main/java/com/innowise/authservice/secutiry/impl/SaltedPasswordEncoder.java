package com.innowise.authservice.secutiry.impl;

import com.innowise.authservice.exception.AuthServiceException;
import com.innowise.authservice.secutiry.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static com.innowise.authservice.secutiry.AuthConstant.DEFAULT_ALGORITHM;

/**
 * @ClassName SaltedPasswordEncoder
 * @Description Utility component for encoding and verifying passwords using SHA-256 with salt.
 * @Author dshparko
 * @Date 07.10.2025 15:13
 * @Version 1.0
 */
@Component
public class SaltedPasswordEncoder implements PasswordEncoder {

    private final MessageDigest digest;

    public SaltedPasswordEncoder() {
        try {
            this.digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthServiceException("Unable to initialize password encoder: " + DEFAULT_ALGORITHM +
                    " algorithm is not available", e);
        }
    }


    public String generateSalt() {
        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    public String encode(String rawPassword, String salt) {
        String salted = salt + rawPassword;
        byte[] hash = digest.digest(salted.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public boolean matches(String rawPassword, String salt, String storedHash) {
        String encoded = encode(rawPassword, salt);
        return encoded.equals(storedHash);
    }
}
