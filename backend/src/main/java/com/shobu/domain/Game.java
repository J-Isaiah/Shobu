package com.shobu.domain;

import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.errors.PieceOutOfBoundsException;

public class Game {
    private Board curboard;
    private Stone sideToMove;

    private Board blackLight;
    private Board blackDark;
    private Board whiteLight;
    private Board whiteDark;

    public Game(Stone sideToMove) {
        this.sideToMove = sideToMove;
    }

    public static Game start() {

        return new Game(Stone.WHITE);
    }

    // TODO: Configure initial board setup and starting side
    public Boolean init() {
        this.blackLight = Board.inital();
        this.blackDark = Board.inital();

        this.whiteLight = Board.inital();
        this.whiteDark = Board.inital();

        return true;

    }

    public void makeMove(Position from, Direction direction, int distance) {
        validateBasicMove(from, direction, distance);
        this.curboard = this.curboard.applyMove(from, direction, distance);
    }

    // TODO: Validate move direction and distance
    private void validateBasicMove(Position from, Direction direction, int distance) {
        if (from == null || direction == null) {
            throw new IllegalArgumentException("From and Direction Cannot Be Null");
        }

        if (distance > 2 || distance < 0) {
            throw new IllegalArgumentException("Distance must be 2 or below and more then 0");
        }

        Stone curStone = this.curboard.getStoneAt(from);

        if (curStone != this.sideToMove) {
            throw new InvalidMoveException("Not Your Turn");
        }
        int newRow = from.getRow() + direction.dx * distance;
        int newCol = from.getCol() + direction.dy * distance;
        if (newRow >= 4 || newRow < 0 || newCol >= 4 || newCol < 0) {
            throw new PieceOutOfBoundsException("Current move set moves piece out side of playable area");
        }

    }

    // TODO: Implement stone push mechanic

    public Stone checkBoardWin(Board board) {
        int white = 0;
        int black = 0;
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Stone cell = board.getStoneAt(new Position(r, c));

                if (cell == Stone.WHITE) {
                    white += 1;
                } else if (cell == Stone.BLACK) {
                    black += 1;
                }
            }
        }
        if (white == 0) {

            return Stone.BLACK;
        }
        if (black == 0) {

            return Stone.WHITE;
        }
        return null;
    }

}
