package com.shobu.service;

import com.shobu.api.dto.response.GetStatsResponse;
import com.shobu.data.entity.GameResult;
import com.shobu.data.entity.PlayerProfile;
import com.shobu.data.repository.GameResultRepository;
import com.shobu.data.repository.PlayerRepository;
import com.shobu.domain.enums.Stone;
import com.shobu.properties.PlayerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DataService {
    private final GameResultRepository gameResultRepository;
    private final PlayerRepository playerRepository;
    private final PlayerProperties playerProperties;

    private static final Logger log = LoggerFactory.getLogger(GameService.class);


    public DataService(GameResultRepository gameResultRepository, PlayerRepository playerRepository, PlayerProperties playerProperties) {
        this.gameResultRepository = gameResultRepository;
        this.playerRepository = playerRepository;
        this.playerProperties = playerProperties;
    }

    public boolean playerExists(UUID playerId) {
        return playerId != null && playerRepository.existsById(playerId);
    }

    public GetStatsResponse getStats() {
        log.debug("fetching game statistics");

        long blackWins = gameResultRepository.countByWinner(Stone.BLACK);
        long whiteWins = gameResultRepository.countByWinner(Stone.WHITE);
        long juliaWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(playerProperties.juliaPlayerId)));
        long isaiahWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(playerProperties.isaiahPlayerId)));
        long totalGamesPlayed = gameResultRepository.count();

        log.debug("Stats: totalGames={}, blackWins={}, whiteWins={}",
                totalGamesPlayed, blackWins, whiteWins);
        return new GetStatsResponse(blackWins, whiteWins, juliaWins, isaiahWins,  totalGamesPlayed);
    }

    public void saveGameResult(GameResult result){
        log.info(
                "Saved game result: winner={}, winnerPlayerId={}",
                result.getWinner(),
                result.getWinningPlayerId()
        );
        gameResultRepository.save(result);
    }
}
