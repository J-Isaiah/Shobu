package com.shobu.domain.moveData;

import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.Position;

public record Move(
        BoardId boardId,
        Position start,
        int distance,
        Direction direction) {

    public Move {
        if (boardId == null || start == null || direction == null) {
            throw new IllegalArgumentException("Move args cannot be null");
        }

        if (distance < 1 || distance > 2) {
            throw new IllegalArgumentException("Distance must be 1 or 2");
        }
    }

    public Position end() {
        return new Position(
                start.getRow() + direction.dx * distance,
                start.getCol() + direction.dy * distance);
    }

}