package com.shobu.api.errors.apiExceptions;

public class SignUpException extends RuntimeException {
    public SignUpException(String message) {
        super(message);
    }
}
