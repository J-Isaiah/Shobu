package com.shobu.domain;

import com.shobu.domain.errors.CannotPushOwnPieceException;
import com.shobu.domain.errors.InvalidMoveException;
import com.shobu.domain.errors.PieceOutOfBoundsException;

public class Board {

    private final Stone[][] grid;

    public Board(Stone[][] grid) {
        if (grid == null) {
            throw new IllegalArgumentException("Grid cannot be null");
        }

        if (grid.length != 4) {
            throw new IllegalArgumentException("Grid must have a row length of 4");
        }
        for (Stone[] grid1 : grid) {
            if (grid1 == null) {
                throw new IllegalArgumentException("Cols Must not be null");
            }
            if (grid1.length != 4) {
                throw new IllegalArgumentException("Grid must have a cols lengh of 4");
            }
        }

        this.grid = copyGrid(grid);

    }

    private static Stone[][] copyGrid(Stone[][] source) {
        Stone[][] copy = new Stone[4][4];
        for (int i = 0; i < 4; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    public static Board inital() {

        Stone[][] board = new Stone[4][4];

        for (int c = 0; c < 4; c++) {
            board[0][c] = Stone.WHITE;
            board[3][c] = Stone.BLACK;

        }

        return new Board(board);

    }

    public Stone getStoneAt(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Postion value must not be null");
        }
        int col = position.getCol();
        int row = position.getRow();
        return grid[row][col];

    }

    public Board applyMove(Position from, Direction direction, int distance, Stone sideToMove) {

        if (from == null || direction == null) {
            throw new IllegalArgumentException("From Position Direction Or distance cannot be null");
        }
        if (distance <= 0 || distance > 2) {
            throw new IllegalArgumentException("Distance Cannot be less then or equal to one");
        }

        int newRow = from.getRow() + direction.dx * distance;
        int newCol = from.getCol() + direction.dy * distance;

        if (newRow >= 4 || newRow < 0 || newCol >= 4 || newCol < 0) {
            throw new PieceOutOfBoundsException("Current move set moves piece out side of playable area");
        }

        Stone stone = this.grid[from.getRow()][from.getCol()];
        if (stone == null) {
            throw new IllegalArgumentException("From position must contain a stone");
        }

        if (stone != sideToMove) {
            throw new InvalidMoveException("You cannot select an opponants stone to move");
        }
        // Check Immidate Stone Path
        int check1Row = from.getRow() + direction.dx;
        int check1Col = from.getCol() + direction.dy;
        int check2Row = from.getRow() + direction.dx;
        int check2Col = from.getCol() + direction.dy;

        Stone oppStoneColor = (stone == Stone.BLACK) ? Stone.WHITE : Stone.BLACK;

        if (!isInBounds(check1Row, check1Col, this.grid)) {
            throw new InvalidMoveException("Cannot Commit suiside");
        }

        if (distance == 1 && this.grid[check1Row][check1Col] != null) {

            return move1Rock(from, direction, distance, sideToMove, this.grid);
        } else if (distance == 2 && isInBounds(check2Row, check2Col, grid)) {
            if ((this.grid[check1Row][check1Col] != null || this.grid[check2Row][check2Col] != null)) {
                // Push Stone 2 Spaces
            }
        } else if (distance == 2 && !isInBounds(check2Col, check2Row, this.grid)) {
            throw new InvalidMoveException("Cannot Kill Your self");
        }

        Stone[][] next = copyGrid(grid);
        next[from.getRow() + direction.dx * distance][from.getCol() + direction.dy * distance] = stone;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }

    private Board move1Rock(Position from, Direction direction, int distance, Stone sideToMove, Stone[][] grid) {
        Stone stoneToMove = grid[from.getRow()][from.getCol()];
        int check1Row = from.getRow() + direction.dx; // Space for cur stone
        int check1Col = from.getCol() + direction.dy;
        int check2Row = from.getRow() + direction.dx * 2;// Space for pushed rock
        int check2Col = from.getCol() + direction.dy * 2;

        if (stoneToMove != sideToMove) {
            throw new CannotPushOwnPieceException("Cannot Push Your OWn Stone");
        }

        // Check if stone that needs to be pushed is blocked
        if (isInBounds(check2Row, check2Col, grid) && grid[check2Row][check2Col] != null) {
            throw new InvalidMoveException("Stone Blocked cannot push");
        }
        Stone[][] next = copyGrid(grid);
        Stone rockToMove = next[check1Row][check1Col]; // Get Rock color to

        // Normal Push
        if (isInBounds(check2Row, check2Col, grid)) {
            // Move Opp Stone Out Of the way
            next[check1Row][check1Col] = null;
            next[check2Row][check2Col] = rockToMove;

            // Set prev stone position to null

        } else {
            next[check1Row][check1Col] = null;
        }
        next[from.getRow() + direction.dx * distance][from.getCol() + direction.dy * distance] = stoneToMove;
        next[from.getRow()][from.getCol()] = null;

        return new Board(next);

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                Stone s = grid[r][c];
                sb.append(s == null ? "." : s.name().charAt(0));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean isInBounds(int row, int col, Stone[][] grid) {
        return row >= 0 && row < grid.length &&
                col >= 0 && col < grid[0].length;
    }
}
