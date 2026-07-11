package com.shobu.api.errors.apiExceptions;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super("Invalid Login");
    }
}
