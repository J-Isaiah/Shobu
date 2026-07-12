package com.shobu.api.errors.apiExceptions;

public class PlayerNotInGameException extends RuntimeException {
    public PlayerNotInGameException(String gameId) {
        super("Player Not Found In Game ID:" + gameId);
    }
}
