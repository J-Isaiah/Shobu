package com.shobu.domain;

import java.util.UUID;

public class GameSession {
    private final UUID whitePlayerId;
    private UUID blackPlayerId;
    private Game game;

    public UUID getWhitePlayerId() {
        return whitePlayerId;
    }

    public GameSession(UUID whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    public UUID getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(UUID blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}