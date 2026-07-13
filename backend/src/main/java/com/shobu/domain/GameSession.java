package com.shobu.domain;

import java.util.UUID;

public class GameSession {
    private String gameId;
    private  UUID whitePlayerId;
    private UUID blackPlayerId;
    private Game game;
    private GameSession rematchGame;

    public GameSession getRematchGame() {
        return rematchGame;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setRematchGame(GameSession rematchGame) {
        this.rematchGame = rematchGame;
    }

    public UUID getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(UUID whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
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