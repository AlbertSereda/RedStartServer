package com.redstart.server.exception;

public class IncorrectLoginOrPasswordException extends RuntimeException {
    public IncorrectLoginOrPasswordException(String message) {
        super(message);
    }
}
