package com.shobu.domain.errors;

public class CannotPushOwnPieceException extends RuntimeException {
    public CannotPushOwnPieceException(String message){
        super(message)
    }

}
