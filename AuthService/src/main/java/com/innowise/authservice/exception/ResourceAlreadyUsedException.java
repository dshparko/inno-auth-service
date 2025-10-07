package com.innowise.authservice.exception;

import lombok.Getter;

import java.util.UUID;

/**
 * @ClassName ResourceAlreadyUsedException
 * @Description Exception thrown when a requested resource is already used.
 * @Author dshparko
 * @Date 06.10.2025 21:44
 * @Version 1.0
 */
public class ResourceAlreadyUsedException extends RuntimeException {
    @Getter
    private final UUID errorId = UUID.randomUUID();

    public ResourceAlreadyUsedException(String emailAlreadyInUse) {
        super("Email " + emailAlreadyInUse + " is already in use");
    }

    public ResourceAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyUsedException(Throwable cause) {
        super(cause);
    }
}
