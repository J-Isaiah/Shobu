package com.shobu.service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.api.dto.response.JoinGameResponse;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.api.errors.apiExceptions.GameFullException;
import com.shobu.api.errors.apiExceptions.GameNotFoundException;
import com.shobu.domain.Game;
import com.shobu.domain.GameSession;
import com.shobu.domain.GenerateLegalMoves;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.utils.GenerateShortCode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GameService {
    private final Map<String, GameSession> games = new HashMap<>();
    private final GenerateShortCode generateShortCode;

    public GameService(GenerateShortCode generateShortCode){
        this.generateShortCode=generateShortCode;
    }

    public StartGameResponse startGame() {
        UUID whitePlayerId = UUID.randomUUID();
        GameSession gameSession = new GameSession(whitePlayerId);
        String id = generateShortCode.generate();
        Game game = Game.start(Stone.WHITE);
        gameSession.setGame(game);
        GenerateLegalMoves generator = new GenerateLegalMoves(game);


        games.put(id, gameSession);
        System.out.println("Created game: [" + id + "]");
        return new StartGameResponse(whitePlayerId, id, game, generator.generateLegalMovesByBoardAndPosition(), Stone.WHITE);
    }

    public GameState makeMove(String gameId, MakeMoveRequest request) {
        debugLookup("makeMove", gameId);
        GameSession gameSession = games.get(gameId);
        if (gameSession == null) {
            throw new GameNotFoundException(gameId);
        }

        Game game = gameSession.getGame();
        Stone sideToMove = game.getSideToMove();

        UUID expectedPlayerId = sideToMove == Stone.WHITE ? gameSession.getWhitePlayerId() : gameSession.getBlackPlayerId();

        if (!expectedPlayerId.equals(request.userId())) {
            throw new InvalidMoveException("It is not your turn");
        }


        Game updatedGame = game.makeMove(request.move());
        GenerateLegalMoves generator = new GenerateLegalMoves(updatedGame);
        gameSession.setGame(updatedGame);

        // TODO: Return legal moves
        games.put(gameId, gameSession);

        return new GameState(gameId, updatedGame.getTurnPhase(), updatedGame.getBoards(), updatedGame.getWinner(), generator.generateLegalMovesByBoardAndPosition(), generator.getReturnedPassiveMove());

    }

    public GameState getGameState(String gameId) {
        debugLookup("getGameState", gameId);

        GameSession gameSession = games.get(gameId);

        if (gameSession == null) {
            throw new GameNotFoundException(gameId);
        }

        Game game = gameSession.getGame();

        if (game == null) {
            throw new GameNotFoundException(gameId);
        }

        return buildGameState(gameId, game);
    }

    public JoinGameResponse joinGame(String gameId) {
        debugLookup("joinGame", gameId);
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

    private GameState buildGameState(String gameId, Game game) {
        GenerateLegalMoves generator = new GenerateLegalMoves(game);


        return new GameState(gameId, game.getTurnPhase(), game.getBoards(), game.getWinner(), generator.generateLegalMovesByBoardAndPosition(), generator.getReturnedPassiveMove());

    }
    private void debugLookup(String method, String gameId) {
        System.out.println(method + " lookup: [" + gameId + "]");
        System.out.println("active games: " + games.keySet());
    }

}
