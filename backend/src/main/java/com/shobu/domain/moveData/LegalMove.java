package com.shobu.domain.moveData;

import com.shobu.domain.Position;
import com.shobu.domain.enums.BoardId;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public record LegalMove(
        Move passiveMove,
        List<Move> aggressiveMoves

) {
}
