package com.shobu.api.dto.response;

import com.shobu.domain.enums.Stone;

import java.util.UUID;

public record RematchResponse(String newGameId, Stone playerColor, UUID playerId) {
}
