package com.innowise.authservice.exception;

import lombok.Getter;

import java.util.UUID;

/**
 * @ClassName ResourceNotFoundException
 * @Description Exception thrown when a requested resource is invalid, missing, or does not meet expected constraints.
 * @Author dshparko
 * @Date 07.10.2025 20:20
 * @Version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    @Getter
    private final UUID errorId = UUID.randomUUID();

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
