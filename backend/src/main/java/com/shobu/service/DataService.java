package com.shobu.service;

import com.shobu.api.dto.response.GetStatsResponse;
import com.shobu.data.entity.GameResult;
import com.shobu.data.repository.GameResultRepository;
import com.shobu.domain.enums.Stone;
import com.shobu.properties.PlayerProperties;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DataService {
    private final GameResultRepository gameResultRepository;
    private final PlayerProperties playerProperties;

    public DataService(GameResultRepository gameResultRepository, PlayerProperties playerProperties) {
        this.gameResultRepository = gameResultRepository;
        this.playerProperties = playerProperties;
    }

    public GetStatsResponse getStats() {

        long blackWins = gameResultRepository.countByWinner(Stone.BLACK);
        long whiteWins = gameResultRepository.countByWinner(Stone.WHITE);
        long juliaWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(playerProperties.juliaPlayerId)));
        long isaiahWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(playerProperties.isaiahPlayerId)));
        long totalGamesPlayed = gameResultRepository.count();


        return new GetStatsResponse(blackWins, whiteWins, juliaWins, isaiahWins,  totalGamesPlayed);
    }

    public void saveGameResult(GameResult result){
        gameResultRepository.save(result);
    }
}
