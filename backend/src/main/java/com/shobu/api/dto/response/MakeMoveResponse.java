package com.shobu.api.dto.response;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import com.shobu.domain.Board;
import com.shobu.domain.BoardId;
import com.shobu.domain.Game;
import com.shobu.domain.Stone;

public record MakeMoveResponse(UUID gameId, Stone sideToMove, Map<BoardId, Board> updatedGameBoards, Stone winner) {

}
