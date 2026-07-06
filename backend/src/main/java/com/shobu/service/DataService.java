package com.shobu.service;

import com.shobu.api.dto.response.GetStatsResponse;
import com.shobu.data.repository.GameResultRepository;
import com.shobu.domain.enums.Stone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DataService {
    private final GameResultRepository gameResultRepository;

    @Value("${app.players.isaiah-id}")
    private UUID isaiahPlayerId;

    @Value("${app.players.julia-id}")
    private UUID juliaPlayerId;


    public DataService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    public GetStatsResponse getStats() {

        long blackWins = gameResultRepository.countByWinner(Stone.BLACK);
        long whiteWins = gameResultRepository.countByWinner(Stone.WHITE);
        long juliaWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(juliaPlayerId)));
        long isaiahWins = gameResultRepository.countByWinningPlayerId(UUID.fromString(String.valueOf(isaiahPlayerId)));
        long totalGamesPlayed = gameResultRepository.count();


        return new GetStatsResponse(blackWins, whiteWins, juliaWins, isaiahWins,  totalGamesPlayed);
    }
}
