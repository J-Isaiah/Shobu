package com.shobu.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.shobu.api.errors.apiExceptions.GameNotFoundException;
import com.shobu.domain.enums.Stone;
import org.springframework.stereotype.Service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.request.StartGameRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.domain.Game;

@Service
public class GameService {
    private final Map<UUID, Game> games = new HashMap<>();

    public StartGameResponse startGame(StartGameRequest request) {
        UUID id = UUID.randomUUID();
        Game game = Game.start(Stone.WHITE);

        // TODO: Return legal moves

        games.put(id, game);
        return new StartGameResponse(id, game);
    }

    public GameState makeMove(UUID gameId, MakeMoveRequest request) {
        Game game = games.get(gameId);
        if (game==null){
            throw new GameNotFoundException(gameId);
        }

        Game updatedGame = game.makeMove(request.move());

        // TODO: Return legal moves
        games.put(gameId, updatedGame);

        return new GameState(gameId, updatedGame.getTurnPhase(), updatedGame.getBoards(), updatedGame.getWinner());

    }
    public GameState getGameState(UUID gameId) {
        Game game = games.get(gameId);
        if (game==null){
            throw new GameNotFoundException(gameId);
        }

        // TODO: Return legal moves
        return new GameState(gameId, game.getTurnPhase(), game.getBoards(), game.getWinner());

    }
}
