package com.shobu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.shobu.domain.errors.InvalidMoveException;

class BoardTest {
    @Test
    void initialBoard_hasCorrectSetup() {
        Board board = Board.inital();

        for (int c = 0; c < 4; c++) {
            assertEquals(Stone.WHITE, board.getStoneAt(new Position(0, c)));

            assertEquals(Stone.BLACK, board.getStoneAt(new Position(3, c)));
        }

    }

    @Test
    void applyMove_pushesOneOpponentStone() {
        Stone[][] grid = new Stone[4][4];

        grid[1][1] = Stone.WHITE;
        grid[1][2] = Stone.BLACK;
        Board board = new Board(grid);
        System.out.println(board.toString());

        Board newBoard = board.applyMove(
                new Position(1, 1), Direction.RIGHT, 1, Stone.WHITE);
        assertNull(newBoard.getStoneAt(new Position(1, 1)));

        assertEquals(Stone.WHITE, newBoard.getStoneAt(new Position(1, 2)));

        assertEquals(Stone.BLACK, newBoard.getStoneAt(new Position(1, 3)));

        System.out.println(newBoard.toString());

    }

    @Test
    void applyMove_pushesOneOpponentStoneOffBoard() {
        Stone[][] grid = new Stone[4][4];

        grid[2][2] = Stone.WHITE;
        grid[2][3] = Stone.BLACK;
        Board board = new Board(grid);
        System.out.println(board.toString());

        Board newBoard = board.applyMove(
                new Position(2, 2), Direction.RIGHT, 1, Stone.WHITE);
        assertNull(newBoard.getStoneAt(new Position(2, 2)));

        assertEquals(Stone.WHITE, newBoard.getStoneAt(new Position(2, 3)));

        System.out.println(newBoard.toString());

    }

    @Test
    void applyMove_throwsWhenPushIsBlocked() {
        Stone[][] grid = new Stone[4][4];

        grid[2][0] = Stone.WHITE;
        grid[2][1] = Stone.BLACK;
        grid[2][2] = Stone.BLACK;

        Board board = new Board(grid);

        Position from = new Position(2, 0);
        assertThrows(
                InvalidMoveException.class,
                () -> board.applyMove(
                        from,
                        Direction.RIGHT,
                        1,
                        Stone.WHITE));
    }
}
