package com.redstart.server.core.socket.message.responsedata;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorResponse {
    //Login error
    INCORRECT_LOGIN_OR_PASSWORD("Incorrect login or password"),
    //Registration error
    LOGIN_OR_EMAIL_ALREADY_EXISTS("User with this login or email already exists"),
    UNKNOWN_ERROR("Unknown error"),
    INCORRECT_EMAIL("Incorrect email"),
    PASSWORD_LESS_CHARACTER("The password must not be less than 6 characters"),
    LOGIN_LESS_CHARACTER("The login must not be less than 4 characters"),
    MAX_COUNT_LEVEL_COMPLETED("You have completed the level the maximum number of times"),
    INACCESSIBLE_LEVEL("Inaccessible level"),
    INACCESSIBLE_ISLAND("Inaccessible island"),
    ISLAND_NOT_FOUND("Island not found"),
    NOT_ENOUGH_MONEY("Not enough money"),
    SPELL_FOR_USER_NOT_FOUND("Spell for user not found"),
    SPELL_MAXIMUM_LEVEL("The spell has a maximum level"),
    NO_ERROR("");

    private final String errorMessage;

    ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonValue
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
