package com.redstart.server.exception;

public class UnauthorizedUserProcessException extends RuntimeException {
    public UnauthorizedUserProcessException(String message) {
        super(message);
    }
}
