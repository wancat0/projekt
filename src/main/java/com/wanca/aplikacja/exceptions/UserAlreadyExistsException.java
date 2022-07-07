package com.wanca.aplikacja.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User " + email + "already exists");
    }
}
