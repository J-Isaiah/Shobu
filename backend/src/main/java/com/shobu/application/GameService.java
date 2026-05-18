package com.shobu.application;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.response.MakeMoveResponse;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.domain.Game;
import com.shobu.domain.Stone;

@Service
public class GameService {
    private final Map<UUID, Game> games = new HashMap();

    public StartGameResponse startGame() {
        UUID id = UUID.randomUUID();
        Game game = Game.start(Stone.WHITE);

        games.put(id, game);
        return new StartGameResponse(id, game);
    }

    public MakeMoveResponse makeMove(MakeMoveRequest request) {
        Game game = games.get(request.gameId());

        Game updatedGame = game.makeMove(request.turn());
        games.put(request.gameId(), updatedGame);

        return new MakeMoveResponse(request.gameId(), updatedGame.getSideToMove(), updatedGame);

    }

    public Game getGame(UUID gameId) {
        return games.get(gameId);
    }
}
