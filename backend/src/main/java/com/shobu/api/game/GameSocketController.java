package com.shobu.api.game;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@MessageMapping("/game/{gameId}")
public class GameSocketController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameSocketController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameService = gameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public String test(String message) {
        System.out.println("Received websocket message: " + message);
        return "Server received: " + message;
    }

    @MessageMapping("/makeMove")
    public void makeMove(
            @DestinationVariable String gameId,
            MakeMoveRequest request
    ) {
        GameState gameState = gameService.makeMove(gameId, request);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, gameState);

    }
}