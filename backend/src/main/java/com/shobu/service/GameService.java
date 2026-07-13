package com.shobu.service;

import com.shobu.api.dto.request.MakeMoveRequest;
import com.shobu.api.dto.request.RematchRequest;
import com.shobu.api.dto.request.StartGameRequest;
import com.shobu.api.dto.response.GameState;
import com.shobu.api.dto.response.JoinGameResponse;
import com.shobu.api.dto.response.RematchResponse;
import com.shobu.api.dto.response.StartGameResponse;
import com.shobu.api.errors.apiExceptions.GameFullException;
import com.shobu.api.errors.apiExceptions.GameNotFoundException;
import com.shobu.api.errors.apiExceptions.PlayerNotInGameException;
import com.shobu.data.entity.GameResult;
import com.shobu.domain.CreatedGame;
import com.shobu.domain.Game;
import com.shobu.domain.GameSession;
import com.shobu.domain.GenerateLegalMoves;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.properties.PlayerProperties;
import com.shobu.utils.GenerateShortCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, GameSession> games = new ConcurrentHashMap<>();

    private final GenerateShortCode generateShortCode;
    private final DataService dataService;
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    public GameService(GenerateShortCode generateShortCode, DataService dataService, PlayerProperties playerProperties) {
        this.generateShortCode = generateShortCode;
        this.dataService = dataService;
    }

    public StartGameResponse startGame(StartGameRequest request) {
        CreatedGame created = createEmptyGame();

        UUID whitePlayerId = request.startPlayer() != null && request.startPlayer().userId() != null ? request.startPlayer().userId() : UUID.randomUUID();

        created.session().setWhitePlayerId(whitePlayerId);

        Game game = created.session().getGame();
        GenerateLegalMoves generator = new GenerateLegalMoves(game);

        return new StartGameResponse(whitePlayerId, created.session().getGameId(), game, generator.generateLegalMovesByBoardAndPosition(), Stone.WHITE);
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

        Stone winner = updatedGame.getWinner();

        if (winner != null) {
            System.out.println("Winner Spotted");
            System.out.println(gameSession.getBlackPlayerId());
            System.out.println(gameSession.getWhitePlayerId());

            UUID whitePlayerId = knownPlayerOrNull(gameSession.getWhitePlayerId());
            UUID blackPlayerId = knownPlayerOrNull(gameSession.getBlackPlayerId());

            UUID winningPlayerId = winner == Stone.WHITE ? whitePlayerId : blackPlayerId;
            System.out.println(winningPlayerId);

            GameResult gameResult = new GameResult(whitePlayerId, blackPlayerId, winningPlayerId, winner);

            dataService.saveGameResult(gameResult);
            System.out.println("Winner posted to db");
        } else {
            games.put(gameId, gameSession);
        }


        return new GameState(gameId, updatedGame.getTurnPhase(), updatedGame.getBoards(), winner, generator.generateLegalMovesByBoardAndPosition(), updatedGame.getPendingPassiveMove(), updatedGame.getLastAggressiveMove());

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

    public JoinGameResponse joinGame(String gameId, UUID playerInternalId) {
        debugLookup("joinGame", gameId);
        GameSession gameSession = games.get(gameId);

        if (gameSession == null) {
            throw new GameNotFoundException(gameId);
        }
        Game game = gameSession.getGame();

        if (game == null) {
            throw new GameNotFoundException(gameId);
        }

        if (gameSession.getBlackPlayerId() != null) {
            throw new GameFullException(gameId);

        }
        UUID joiningPlayerId = dataService.playerExists(playerInternalId) ? playerInternalId : UUID.randomUUID();
        gameSession.setBlackPlayerId(joiningPlayerId);
        games.put(gameId, gameSession);

        return new JoinGameResponse(gameId, gameSession.getBlackPlayerId(), buildGameState(gameId, game), Stone.BLACK);
    }

    public RematchResponse rematch(String gameId, RematchRequest request) {

        GameSession oldSession = games.get(gameId);
        if (!request.playerId().equals(oldSession.getWhitePlayerId()) && !request.playerId().equals(oldSession.getBlackPlayerId())) {
            throw new PlayerNotInGameException(gameId);
        }

        Stone curStoneColor = oldSession.getBlackPlayerId().equals(request.playerId()) ? Stone.BLACK : Stone.WHITE;


        GameSession newSession = oldSession.getRematchGame();

        if (newSession == null) {
            newSession = createEmptyGame().session();
            oldSession.setRematchGame(newSession);
        }

        if (curStoneColor == Stone.WHITE) {
            newSession.setBlackPlayerId(request.playerId());
        }

        if (curStoneColor == Stone.BLACK) {
            newSession.setWhitePlayerId(request.playerId());
        }

        return new RematchResponse(newSession.getGameId(), curStoneColor == Stone.WHITE ? Stone.BLACK : Stone.WHITE, request.playerId());
    }


    private CreatedGame createEmptyGame() {
        String gameId = generateShortCode.generate();
        Game game = Game.start(Stone.WHITE);

        GameSession session = new GameSession(null);
        session.setGame(game);
        session.setGameId(gameId);
        games.put(gameId, session);

        return new CreatedGame(session);
    }


    private GameState buildGameState(String gameId, Game game) {
        GenerateLegalMoves generator = new GenerateLegalMoves(game);


        return new GameState(gameId, game.getTurnPhase(), game.getBoards(), game.getWinner(), generator.generateLegalMovesByBoardAndPosition(), game.getPendingPassiveMove(), game.getLastAggressiveMove());

    }

    private void debugLookup(String method, String gameId) {
        System.out.println(method + " lookup: [" + gameId + "]");
        System.out.println("active games: " + games.keySet());
    }

    private UUID knownPlayerOrNull(UUID playerId) {
        return dataService.playerExists(playerId) ? playerId : null;
    }

}
