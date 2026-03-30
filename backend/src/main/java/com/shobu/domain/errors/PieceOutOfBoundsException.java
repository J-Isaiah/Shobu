package com.shobu.domain.errors;

public class PieceOutOfBoundsException extends RuntimeException {

    public PieceOutOfBoundsException(String message) {
        super(message);
    }

}
