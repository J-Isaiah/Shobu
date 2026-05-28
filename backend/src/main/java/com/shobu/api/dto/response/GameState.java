package com.shobu.api.dto.response;

import java.util.Map;
import java.util.UUID;

import com.shobu.domain.Board;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Stone;

public record GameState(UUID gameId, Stone sideToMove, Map<BoardId, Board> updatedGameBoards, Stone winner) {

}
