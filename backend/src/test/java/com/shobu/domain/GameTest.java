package com.shobu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.shobu.domain.moveData.Move;
import com.shobu.domain.moveData.Turn;

class GameTest {

    @Test
    void start_createsGameWithWhiteToMove() {
        Game game = Game.start(Stone.WHITE);

        assertEquals(Stone.WHITE, game.getSideToMove());
    }

    @Test
    void makeMove_appliesPassiveAndAggressiveMoves() {
        Game game = Game.start(Stone.WHITE);

        Move passive = move(BoardId.WHITE_LIGHT, 0, 0, 2, Direction.DOWN);
        Move aggressive = move(BoardId.BLACK_DARK, 0, 1, 2, Direction.DOWN);

        Game result = game.makeMove(new Turn(passive, aggressive));

        assertMoveApplied(result, passive, Stone.WHITE);
        assertMoveApplied(result, aggressive, Stone.WHITE);
        assertEquals(Stone.BLACK, result.getSideToMove());
    }

    private static Move move(BoardId boardId, int row, int col, int distance, Direction direction) {
        return new Move(boardId, new Position(row, col), distance, direction);
    }

    private static void assertMoveApplied(Game game, Move move, Stone expectedStone) {
        Board board = game.getBoardById(move.boardId());

        assertNull(board.getStoneAt(move.start()));
        assertEquals(expectedStone, board.getStoneAt(move.end()));
    }
}