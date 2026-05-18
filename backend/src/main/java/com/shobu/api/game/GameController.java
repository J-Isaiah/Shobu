package com.shobu.api.game;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.request.StartGameRequest;
import com.shobu.api.dto.response.MakeMoveResponse;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.application.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public StartGameResponse startGame(@RequestBody StartGameRequest request) {
        return gameService.startGame(request);

    }

    @PostMapping("/{gameId}/makeMove")
    public MakeMoveResponse makeMove(@PathVariable UUID gameId, @RequestBody MakeMoveRequest request) {
        return gameService.makeMove(gameId, request);

    }

}
