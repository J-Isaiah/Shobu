package com.shobu.domain.moveData;

import com.shobu.domain.Game;
import com.shobu.domain.enums.BoardId;

public record PassiveMoveInformation(
        BoardId boardId,
        Move move,
        Game stateAfterPassive
) {
}
