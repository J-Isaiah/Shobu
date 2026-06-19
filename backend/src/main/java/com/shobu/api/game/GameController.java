package com.shobu.api.game;

import java.util.UUID;

import com.shobu.api.dto.response.JoinGameResponse;
import org.springframework.web.bind.annotation.*;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/startGame")
    public StartGameResponse startGame() {
        return gameService.startGame();

    }

    @PostMapping("/{gameId}/makeMove")
    public GameState makeMove(@PathVariable UUID gameId, @RequestBody MakeMoveRequest request) {
        return gameService.makeMove(gameId, request);
    }
    @PostMapping("{gameId}/joinGame")
    public JoinGameResponse joinGame(@PathVariable UUID gameId){
        return gameService.joinGame(gameId);

    }

    @GetMapping("/{gameId}/getGameState")
    public GameState getGameState(@PathVariable UUID gameId) {
        return gameService.getGameState(gameId);
    }

}
