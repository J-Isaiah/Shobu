package com.shobu.api.dto.request;

import java.util.UUID;

public record RematchRequest(UUID internalPlayerId, String gameId) {
}
