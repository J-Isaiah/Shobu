package com.shobu.api.errors.apiExceptions;

import java.util.UUID;

public class GameFullException extends RuntimeException {
    public GameFullException(String gameId) {
        super("gameid you have attempted to join is full: " +  gameId);
    }
}
