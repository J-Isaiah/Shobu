package com.shobu.api.dto.response;

public record GetStatsResponse(long blackWins, long whiteWins, long juliaWins, long isaiahWins, long totalGamesPlayed) {
}
