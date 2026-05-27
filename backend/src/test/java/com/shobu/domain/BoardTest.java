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
            assertEquals(Stone.BLACK, board.getStoneAt(new Position(0, c)));
            assertEquals(Stone.WHITE, board.getStoneAt(new Position(3, c)));
        }
    }

    private void assertPush(
            Position whiteStart,
            Direction direction,
            int distance,
            Position blackStart,
            Position expectedWhite,
            Position expectedBlack) {

        Stone[][] grid = new Stone[4][4];

        grid[whiteStart.getRow()][whiteStart.getCol()] = Stone.WHITE;
        grid[blackStart.getRow()][blackStart.getCol()] = Stone.BLACK;

        Board board = new Board(grid);

        Board newBoard = board.applyMove(
                whiteStart,
                direction,
                distance,
                Stone.WHITE, null);

        assertNull(newBoard.getStoneAt(whiteStart));

        assertEquals(
                Stone.WHITE,
                newBoard.getStoneAt(expectedWhite));

        assertEquals(
                Stone.BLACK,
                newBoard.getStoneAt(expectedBlack));
    }

    // =========================
    // DISTANCE 1
    // =========================

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneRight() {
        assertPush(
                new Position(1, 1),
                Direction.RIGHT,
                1,
                new Position(1, 2),
                new Position(1, 2),
                new Position(1, 3));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneLeft() {
        assertPush(
                new Position(1, 2),
                Direction.LEFT,
                1,
                new Position(1, 1),
                new Position(1, 1),
                new Position(1, 0));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneUp() {
        assertPush(
                new Position(2, 1),
                Direction.UP,
                1,
                new Position(1, 1),
                new Position(1, 1),
                new Position(0, 1));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneDown() {
        assertPush(
                new Position(1, 1),
                Direction.DOWN,
                1,
                new Position(2, 1),
                new Position(2, 1),
                new Position(3, 1));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneUpRight() {
        assertPush(
                new Position(2, 1),
                Direction.UP_RIGHT,
                1,
                new Position(1, 2),
                new Position(1, 2),
                new Position(0, 3));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneUpLeft() {
        assertPush(
                new Position(2, 2),
                Direction.UP_LEFT,
                1,
                new Position(1, 1),
                new Position(1, 1),
                new Position(0, 0));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneDownRight() {
        assertPush(
                new Position(1, 1),
                Direction.DOWN_RIGHT,
                1,
                new Position(2, 2),
                new Position(2, 2),
                new Position(3, 3));
    }

    @Test
    void applyMove_distance1_pushesAdjacentOpponentStoneDownLeft() {
        assertPush(
                new Position(1, 2),
                Direction.DOWN_LEFT,
                1,
                new Position(2, 1),
                new Position(2, 1),
                new Position(3, 0));
    }

    // =========================
    // DISTANCE 2
    // =========================

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneRight() {
        assertPush(
                new Position(1, 0),
                Direction.RIGHT,
                2,
                new Position(1, 1),
                new Position(1, 2),
                new Position(1, 3));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneLeft() {
        assertPush(
                new Position(1, 3),
                Direction.LEFT,
                2,
                new Position(1, 2),
                new Position(1, 1),
                new Position(1, 0));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneUp() {
        assertPush(
                new Position(3, 1),
                Direction.UP,
                2,
                new Position(2, 1),
                new Position(1, 1),
                new Position(0, 1));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneDown() {
        assertPush(
                new Position(0, 1),
                Direction.DOWN,
                2,
                new Position(1, 1),
                new Position(2, 1),
                new Position(3, 1));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneUpRight() {
        assertPush(
                new Position(3, 0),
                Direction.UP_RIGHT,
                2,
                new Position(2, 1),
                new Position(1, 2),
                new Position(0, 3));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneUpLeft() {
        assertPush(
                new Position(3, 3),
                Direction.UP_LEFT,
                2,
                new Position(2, 2),
                new Position(1, 1),
                new Position(0, 0));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneDownRight() {
        assertPush(
                new Position(0, 0),
                Direction.DOWN_RIGHT,
                2,
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3));
    }

    @Test
    void applyMove_distance2_pushesAdjacentOpponentStoneDownLeft() {
        assertPush(
                new Position(0, 3),
                Direction.DOWN_LEFT,
                2,
                new Position(1, 2),
                new Position(2, 1),
                new Position(3, 0));
    }

    // =========================
    // SPECIAL CASES
    // =========================

    @Test
    void applyMove_distance2_pushesOpponentStoneWithGapRight() {
        assertPush(
                new Position(1, 0),
                Direction.RIGHT,
                2,
                new Position(1, 2),
                new Position(1, 2),
                new Position(1, 3));
    }

    @Test
    void applyMove_distance2_pushesOpponentStoneOffBoardRight() {

        Stone[][] grid = new Stone[4][4];

        grid[1][1] = Stone.WHITE;
        grid[1][2] = Stone.BLACK;

        Board board = new Board(grid);

        Board newBoard = board.applyMove(
                new Position(1, 1),
                Direction.RIGHT,
                2,
                Stone.WHITE, null);

        assertNull(newBoard.getStoneAt(new Position(1, 1)));
        assertNull(newBoard.getStoneAt(new Position(1, 2)));

        assertEquals(
                Stone.WHITE,
                newBoard.getStoneAt(new Position(1, 3)));
    }

    @Test
    void applyMove_distance1_pushesOpponentStoneOffBoardRight() {

        Stone[][] grid = new Stone[4][4];

        grid[2][2] = Stone.WHITE;
        grid[2][3] = Stone.BLACK;

        Board board = new Board(grid);

        Board newBoard = board.applyMove(
                new Position(2, 2),
                Direction.RIGHT,
                1,
                Stone.WHITE, null);

        assertNull(newBoard.getStoneAt(new Position(2, 2)));

        assertEquals(
                Stone.WHITE,
                newBoard.getStoneAt(new Position(2, 3)));
    }

    @Test
    void applyMove_distance1_throwsWhenPushIsBlockedRight() {

        Stone[][] grid = new Stone[4][4];

        grid[2][0] = Stone.WHITE;
        grid[2][1] = Stone.BLACK;
        grid[2][2] = Stone.BLACK;

        Board board = new Board(grid);

        assertThrows(
                InvalidMoveException.class,
                () -> board.applyMove(
                        new Position(2, 0),
                        Direction.RIGHT,
                        1,
                        Stone.WHITE,null));
    }
}