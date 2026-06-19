package com.shobu.api.dto.response;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shobu.domain.Game;
import com.shobu.domain.Position;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.moveData.LegalMove;

public record StartGameResponse(UUID playerId,UUID gameId, Game game, EnumMap<BoardId, Map<String, List<LegalMove>>> legalMovesForPlayer ) {

}
