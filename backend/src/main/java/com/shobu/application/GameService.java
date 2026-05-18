package com.shobu.application;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.request.StartGameRequest;
import com.shobu.api.dto.response.MakeMoveResponse;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.domain.Game;

@Service
public class GameService {
    private final Map<UUID, Game> games = new HashMap<>();

    public StartGameResponse startGame(StartGameRequest request) {
        UUID id = UUID.randomUUID();
        Game game = Game.start(request.startSide());

        games.put(id, game);
        return new StartGameResponse(id, game);
    }

    public MakeMoveResponse makeMove(UUID gameId, MakeMoveRequest request) {
        Game game = games.get(gameId);

        Game updatedGame = game.makeMove(request.turn());
        games.put(gameId, updatedGame);

        return new MakeMoveResponse(gameId, updatedGame.getSideToMove(), updatedGame, updatedGame.getWinner());

    }

    public Game getGame(UUID gameId) {
        return games.get(gameId);
    }
}
