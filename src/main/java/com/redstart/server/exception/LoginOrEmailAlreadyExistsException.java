package com.redstart.server.exception;

public class LoginOrEmailAlreadyExistsException extends RuntimeException {
    public LoginOrEmailAlreadyExistsException(String message) {
        super(message);
    }
}
