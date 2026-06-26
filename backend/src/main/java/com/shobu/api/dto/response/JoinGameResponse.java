package com.shobu.api.dto.response;

import com.shobu.domain.enums.Stone;

import java.util.UUID;

public record JoinGameResponse(String gameId, UUID playerId, GameState gameState, Stone playerColor) {
}
