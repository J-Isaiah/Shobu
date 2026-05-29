package com.shobu.api.dto.response;

import java.util.Map;
import java.util.UUID;

import com.shobu.domain.Board;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.enums.TurnPhase;

public record GameState(UUID gameId, TurnPhase turnPhase, Map<BoardId, Board> updatedGameBoards, Stone winner) {

}
