package com.shobu.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.shobu.api.dto.response.JoinGameResponse;
import com.shobu.api.errors.apiExceptions.GameFullException;
import com.shobu.domain.GameSession;
import com.shobu.domain.GenerateLegalMoves;
import com.shobu.api.errors.apiExceptions.GameNotFoundException;
import com.shobu.domain.enums.Stone;
import org.springframework.stereotype.Service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.domain.Game;

@Service
public class GameService {
    private final Map<UUID, GameSession> games = new HashMap<>();

    public StartGameResponse startGame() {
        UUID whitePlayerId = UUID.randomUUID();
        GameSession gameSession = new GameSession(whitePlayerId);
        UUID id = UUID.randomUUID();
        Game game = Game.start(Stone.WHITE);
        gameSession.setGame(game);
        GenerateLegalMoves generator = new GenerateLegalMoves(game);


        games.put(id, gameSession);
        return new StartGameResponse(whitePlayerId, id, game, generator.generateLegalMovesByBoardAndPosition(), Stone.WHITE);
    }

    public GameState makeMove(UUID gameId, MakeMoveRequest request) {
        GameSession gameSession = games.get(gameId);
        if (gameSession == null) {
            throw new GameNotFoundException(gameId);
        }

        Game game = gameSession.getGame();

        Game updatedGame = game.makeMove(request.move());
        GenerateLegalMoves generator = new GenerateLegalMoves(updatedGame);
        gameSession.setGame(updatedGame);

        // TODO: Return legal moves
        games.put(gameId, gameSession);

        return new GameState(gameId, updatedGame.getTurnPhase(), updatedGame.getBoards(), updatedGame.getWinner(), generator.generateLegalMovesByBoardAndPosition(), generator.getReturnedPassiveMove());

    }

    public GameState getGameState(UUID gameId) {
        GameSession gameSession = games.get(gameId);
        Game game = gameSession.getGame();
        GenerateLegalMoves generator = new GenerateLegalMoves(game);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return new GameState(gameId, game.getTurnPhase(), game.getBoards(), game.getWinner(), generator.generateLegalMovesByBoardAndPosition(), generator.getReturnedPassiveMove());
    }

    public JoinGameResponse joinGame(UUID gameId) {
        GameSession gameSession = games.get(gameId);
        Game game = gameSession.getGame();

        if (game == null) {
            throw new GameNotFoundException(gameId);
        }

        if (gameSession.getBlackPlayerId() != null) {
            throw new GameFullException(gameId);

        }
        gameSession.setBlackPlayerId(UUID.randomUUID());

        games.put(gameId, gameSession);

        return new JoinGameResponse(gameId, gameSession.getBlackPlayerId(), buildGameState(gameId, game), Stone.BLACK);
    }

    private GameState buildGameState(UUID gameId, Game game) {
        GenerateLegalMoves generator = new GenerateLegalMoves(game);


        return new GameState(gameId, game.getTurnPhase(), game.getBoards(), game.getWinner(), generator.generateLegalMovesByBoardAndPosition(), generator.getReturnedPassiveMove());

    }
}
