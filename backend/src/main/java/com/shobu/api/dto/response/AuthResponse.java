package com.shobu.api.dto.response;

import java.util.UUID;

public record AuthResponse(UUID playerInternalId, String playerName) {
}
