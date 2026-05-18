package com.shobu.api.dto.response;

import java.util.UUID;

import com.shobu.domain.Game;
import com.shobu.domain.Stone;

public record MakeMoveResponse(UUID gameId, Stone sideToMove, Game updatedGame, Stone winner) {

}
