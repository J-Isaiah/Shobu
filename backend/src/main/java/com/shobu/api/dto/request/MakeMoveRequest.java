package com.shobu.api.dto.request;

import java.util.UUID;

import com.shobu.domain.moveData.Turn;

public record MakeMoveRequest(UUID userId, Turn turn) {

}
