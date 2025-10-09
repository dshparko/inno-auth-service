package com.innowise.authservice.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.UUID;

/**
 * @ClassName AuthServiceException
 * @Description Exception thrown when an authentication problems occur.
 * @Author dshparko
 * @Date 08.10.2025 15:10
 * @Version 1.0
 */
public class AuthServiceException extends RuntimeException {
    @Getter
    @Serial
    private final UUID errorId = UUID.randomUUID();

    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthServiceException(Throwable cause) {
        super(cause);
    }
}
