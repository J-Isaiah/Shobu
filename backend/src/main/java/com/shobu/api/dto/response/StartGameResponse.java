package com.shobu.api.dto.response;

import java.util.UUID;

import com.shobu.domain.Game;

public record StartGameResponse(UUID gameId, Game game) {

}
