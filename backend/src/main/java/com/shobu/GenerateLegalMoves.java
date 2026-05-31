package com.shobu;

import com.shobu.domain.Board;
import com.shobu.domain.Game;
import com.shobu.domain.Position;
import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.moveData.Move;
import com.shobu.domain.moveData.MoveInformation;

import java.util.*;

public class GenerateLegalMoves {
    private final Game game;
    private final Stone sideToMove;


    public GenerateLegalMoves(Game game) {
        this.game = game;
        this.sideToMove = game.getSideToMove();

    }



    public EnumMap<BoardId, Map<Position, List<MoveInformation>>> generateLegalPassiveMoves() {
        EnumMap<BoardId, Map<Position, List<MoveInformation>>> movesByBoard =
                new EnumMap<>(BoardId.class);
        for (BoardId boardId : BoardId.values()) {
            if (boardId.getBoardOwner() != sideToMove) {
                continue;
            }

            Map<Position, List<MoveInformation>> movesByPosition = new HashMap<>();
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {

                    Position start = new Position(row, col);

                    List<MoveInformation> allPositionMoves = new ArrayList<>();
                    for (int distance = 1; distance < 3; distance++) {
                        for (Direction direction : Direction.values()) {
                            Move move = new Move(boardId, start, distance, direction);

                            if (game.canMakeMove(move)) {
                                MoveInformation moveInformation = new MoveInformation(boardId, false, move);
                                allPositionMoves.add(moveInformation);


                            }

                        }
                    }
                    if (!allPositionMoves.isEmpty()) {
                        movesByPosition.put(start, allPositionMoves);
                    }
                    movesByPosition.put(start, allPositionMoves);
                }
            }

            movesByBoard.put(boardId, movesByPosition);

        }
        return movesByBoard;


    }
}
