package com.shobu.api.dto.response;

import java.util.UUID;

public record JoinGameResponse(UUID gameId, UUID playerId, GameState gameState) {
}
