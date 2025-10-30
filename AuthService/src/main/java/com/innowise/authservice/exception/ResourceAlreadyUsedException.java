package com.innowise.authservice.exception;

import lombok.Getter;

import java.io.Serial;
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
    @Serial
    private final UUID errorId = UUID.randomUUID();

    public ResourceAlreadyUsedException(String message) {
        super(message);
    }

    public ResourceAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyUsedException(Throwable cause) {
        super(cause);
    }
}
