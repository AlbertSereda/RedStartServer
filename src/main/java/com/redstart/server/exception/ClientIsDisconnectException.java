package com.redstart.server.exception;

public class ClientIsDisconnectException extends RuntimeException {
    public ClientIsDisconnectException(String message) {
        super(message);
    }
}
