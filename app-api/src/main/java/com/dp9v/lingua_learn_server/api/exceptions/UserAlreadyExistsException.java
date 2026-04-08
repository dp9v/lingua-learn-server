package com.dp9v.lingua_learn_server.api.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with this email already exists");
    }
}
