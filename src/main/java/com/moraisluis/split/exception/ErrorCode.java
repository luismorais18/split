package com.moraisluis.split.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not found"),
    NOT_A_MEMBER(HttpStatus.NOT_FOUND, "User is not a member of this group"),
    ALREADY_A_MEMBER(HttpStatus.CONFLICT, "User is already a member"),
    EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT, "Email already registered");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() { return status; }
    public String message() { return message; }
}
