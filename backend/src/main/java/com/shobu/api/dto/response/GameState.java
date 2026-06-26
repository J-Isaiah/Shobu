package com.shobu.api.dto.response;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shobu.domain.Board;
import com.shobu.domain.Position;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.enums.TurnPhase;
import com.shobu.domain.moveData.LegalMove;
import com.shobu.domain.moveData.Move;

public record GameState(String gameId, TurnPhase turnPhase, Map<BoardId, Board> updatedGameBoards, Stone winner,
                        EnumMap<BoardId, Map<String, List<LegalMove>>> legalMovesForPlayer, Move pendingPassiveMove) {

}
