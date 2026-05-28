package com.shobu.domain;

import com.shobu.domain.enums.BoardId;
import com.shobu.domain.enums.Direction;
import com.shobu.domain.enums.Stone;
import com.shobu.domain.errors.InvalidMoveException;
import org.junit.jupiter.api.Test;

import com.shobu.domain.moveData.Move;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void start_createsGameWithWhiteToMove() {
        Game game = Game.start(Stone.WHITE);

        assertEquals(Stone.WHITE, game.getSideToMove());
    }

    @Test
    void makeMove_throwsErrorWhenPassiveMoveMovesStone(){
        Game game = Game.start(Stone.WHITE);
        System.out.println(game);

        Move passive = move(BoardId.WHITE_LIGHT, 3, 0, 2, Direction.UP);
        Move aggressive = move(BoardId.BLACK_DARK, 3, 1, 2, Direction.UP);

        Game passive1= game.makeMove(passive);

        Game aggressive1= passive1.makeMove(aggressive);



        Move passive2 = move(BoardId.WHITE_LIGHT, 0, 0, 2, Direction.DOWN);
        Move aggressive2 = move(BoardId.BLACK_DARK, 3, 1, 2, Direction.DOWN);

        assertThrows(
                InvalidMoveException.class,
                () -> {game.makeMove(passive2);
                game.makeMove(aggressive2);}
        );









    }

    @Test
    void makeMove_appliesPassiveAndAggressiveMoves() {
        Game game = Game.start(Stone.WHITE);

        Move passive = move(BoardId.WHITE_LIGHT, 3, 0, 2, Direction.UP);
        Move aggressive = move(BoardId.BLACK_DARK, 3, 1, 2, Direction.UP);

        Game passiveResult = game.makeMove(passive);
        Game aggressiveResult = passiveResult.makeMove(aggressive);

        assertMoveApplied(aggressiveResult, passive, Stone.WHITE);
        assertMoveApplied(aggressiveResult, aggressive, Stone.WHITE);
        assertEquals(Stone.BLACK, aggressiveResult.getSideToMove());
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