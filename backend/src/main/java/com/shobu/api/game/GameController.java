package com.shobu.api.game;

import java.util.UUID;

import com.shobu.api.dto.request.JoinGameRequest;
import com.shobu.api.dto.request.RematchRequest;
import com.shobu.api.dto.request.StartGameRequest;
import com.shobu.api.dto.response.JoinGameResponse;
import com.shobu.api.dto.response.RematchResponse;
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
    public StartGameResponse startGame(@RequestBody StartGameRequest request) {
        return gameService.startGame(request);

    }

    @PostMapping("/{gameId}/makeMove")
    public GameState makeMove(@PathVariable String gameId, @RequestBody MakeMoveRequest request) {
        return gameService.makeMove(gameId, request);
    }
    @PostMapping("{gameId}/joinGame")
    public JoinGameResponse joinGame(@PathVariable String gameId, @RequestBody JoinGameRequest request){
        return gameService.joinGame(gameId, request.playerInternalId());

    }
    @PostMapping("{gameId}/rematch")
    public RematchResponse rematch(@PathVariable String gameId, @RequestBody RematchRequest request){
        return gameService.rematch(gameId, request);
    }

    @GetMapping("/{gameId}/getGameState")
    public GameState getGameState(@PathVariable String gameId) {
        return gameService.getGameState(gameId);
    }


}
