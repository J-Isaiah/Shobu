package com.shobu.api.dto.request;

import com.shobu.domain.moveData.Move;

import java.util.UUID;

public record MakeMoveRequest(UUID userId, Move move) {

}
