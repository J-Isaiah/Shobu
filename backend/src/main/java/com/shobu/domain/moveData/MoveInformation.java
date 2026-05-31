package com.shobu.domain.moveData;

import com.shobu.domain.enums.BoardId;

public record MoveInformation(
        BoardId boardId,
        Boolean pushes,
        Move move
) {
}
