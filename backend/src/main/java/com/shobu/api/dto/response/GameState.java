package com.shobu.api.dto.response;

import com.shobu.domain.Board;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.enums.TurnPhase;
import com.shobu.domain.moveData.LegalMove;
import com.shobu.domain.moveData.Move;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record GameState(String gameId, TurnPhase turnPhase, Map<BoardId, Board> updatedGameBoards, Stone winner,
                        EnumMap<BoardId, Map<String, List<LegalMove>>> legalMovesForPlayer, Move pendingPassiveMove, Move lastAggressiveMove) {

}
