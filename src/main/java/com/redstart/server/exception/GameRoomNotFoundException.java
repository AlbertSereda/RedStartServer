package com.redstart.server.exception;

public class GameRoomNotFoundException extends RuntimeException {
    public GameRoomNotFoundException(String message) {
        super(message);
    }
}
